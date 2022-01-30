package com.laur.licenta.service;


import com.laur.licenta.dto.AuthentificationResponse;
import com.laur.licenta.dto.LoginRequest;
import com.laur.licenta.dto.RefreshTokenRequest;
import com.laur.licenta.dto.SignupRequest;
import com.laur.licenta.exceptions.TokenNotFoundException;
import com.laur.licenta.models.NotificationEmail;
import com.laur.licenta.models.User;
import com.laur.licenta.models.VerificationToken;
import com.laur.licenta.repository.UserRepository;
import com.laur.licenta.repository.VerificationTokenRepository;
import com.laur.licenta.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {
    @Autowired
    public UserRepository userRepository;
    @Autowired
    public VerificationTokenRepository verificationTokenRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    public MailService mailService;
    @Autowired
    public JwtProvider jwtProvider;
    @Autowired
    public RefreshTokenService refreshTokenService;
    @Autowired
    private AuthenticationManager authenticationManager;

    public void signup(SignupRequest signupRequest){

        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(encoder.encode(signupRequest.getPassword()));
        user.setEnabled(false);
        ;
        userRepository.save(user);
        String token = generateVerificationToken(user);
        mailService.sendMail(new NotificationEmail("Please Activate your Account",
                user.getEmail(), "Thank you for signing up, " +
                "please click on the below url to activate your account : " +
                "http://localhost:9090/api/auth/accountVerification/" + token));
    }

    public User getCurrentUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getUsername()));
    }

    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String username=verificationToken.getUser().getUsername();
        User user=userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("User not found with name-"+username));
        user.setEnabled(true);
        userRepository.save(user);
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken=new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;
    }


    public void verifyAccount(String token){

        try {
            Optional<VerificationToken> verificationToken= verificationTokenRepository.findByToken(token);
            verificationToken.orElseThrow(()->new TokenNotFoundException("invalid token"));
            fetchUserAndEnable(verificationToken.get());
        }
        catch (TokenNotFoundException exception){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Invalid Token",exception);
        }

    }

    public AuthentificationResponse login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);
        return AuthentificationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(loginRequest.getUsername())
                .build();
    }

    public AuthentificationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUsername());
        return AuthentificationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(refreshTokenRequest.getUsername())
                .build();
    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }

}
