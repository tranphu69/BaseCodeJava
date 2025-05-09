package com.example.baseCode.service;

import com.example.baseCode.dto.request.AuthenticationRequest;
import com.example.baseCode.dto.request.IntrospectRequest;
import com.example.baseCode.dto.request.LogoutRequest;
import com.example.baseCode.dto.request.RefreshRequest;
import com.example.baseCode.dto.response.AuthenticationResponse;
import com.example.baseCode.dto.response.IntrospecResponse;
import com.example.baseCode.entity.InvalidatedToken;
import com.example.baseCode.entity.User;
import com.example.baseCode.exception.AppException;
import com.example.baseCode.exception.ErrorCode;
import com.example.baseCode.repository.InvalidatedTokenRepository;
import com.example.baseCode.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InvalidatedTokenRepository invalidatedTokenRepository;
    @NonFinal
    @Value("${jwt.signerkey}")
    protected String SIGNER_KEY;

    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;


    public IntrospecResponse introspect(IntrospectRequest request)
            throws ParseException, JOSEException {
        var token = request.getToken();
        IntrospecResponse response = new IntrospecResponse();
        try {
            verifyToken(token, false);
            response.setValid(true);
        } catch (AppException e) {
            response.setValid(false);
        }
        return response;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request){
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if(!authenticated) throw  new AppException(ErrorCode.UNAUTHENTICATED);
        var token = generateToken(user);
        AuthenticationResponse response = new AuthenticationResponse();
        response.setToken(token);
        response.setAuthenticated(authenticated);
        return response;
    }

    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        var signJWT = verifyToken(request.getToken(), true);
        var jit = signJWT.getJWTClaimsSet().getJWTID();
        var expiryTime = signJWT.getJWTClaimsSet().getExpirationTime();
        InvalidatedToken invalidatedToken = new InvalidatedToken();
        invalidatedToken.setId(jit);
        invalidatedToken.setExpiryTime(expiryTime);
        invalidatedTokenRepository.save(invalidatedToken);
        var username = signJWT.getJWTClaimsSet().getSubject();
        var user = userRepository.findByUsername(username).orElseThrow(
                () -> new AppException(ErrorCode.USER_EXSITED)
        );
        var token = generateToken(user);
        AuthenticationResponse response = new AuthenticationResponse();
        response.setToken(token);
        response.setAuthenticated(true);
        return response;
    }

    private String generateToken(User user){
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("baseJava.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    private String buildScope(User user){
        StringJoiner stringJoiner = new StringJoiner(" ");
        if(!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if(!CollectionUtils.isEmpty(role.getPermissions()))
                    role.getPermissions()
                            .forEach(permission -> stringJoiner.add(permission.getName()));
            });
        return stringJoiner.toString();
    }

    public void logout(LogoutRequest logoutRequest) throws ParseException, JOSEException{
        try {
            var signToken = verifyToken(logoutRequest.getToken(), true);
            String jit = signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();
            InvalidatedToken invalidatedToken = new InvalidatedToken();
            invalidatedToken.setId(jit);
            invalidatedToken.setExpiryTime(expiryTime);
            invalidatedTokenRepository.save(invalidatedToken);
        } catch (AppException exception) {
            System.out.println("Token already expired!");
        }
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expiryTime = (isRefresh)
                ? new Date(signedJWT.getJWTClaimsSet().getIssueTime().toInstant().plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS).toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();
        var verified = signedJWT.verify(verifier);
        if(!(verified && expiryTime.after(new Date())))
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        if(invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        return signedJWT;
    }
}
