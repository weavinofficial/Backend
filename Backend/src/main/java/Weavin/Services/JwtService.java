package Weavin.Services;

import java.util.Calendar;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Weavin.Entities.User;
import Weavin.Repositories.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Getter
public class JwtService {
    private String SECRET_KEY = "tempSecret";

    private final int jwtExpirationMs = 900000;

    private final int jwtRefreshExpirationMs = 604800000;

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User getUserByRefreshToken(String refreshToken) {
        return userRepository.findByRefreshToken(refreshToken);
    }

    public void setRefreshToken(String username, String refreshToken) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            user.setRefreshToken(refreshToken);
        }
    }

    public void removeRefreshToken(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            user.setRefreshToken(null);
        }
    }

    public void logout(HttpServletRequest request) {
        if (! isHeaderValid(request)) {
            return;
        }

        String refreshToken = request
            .getHeader("Authentication-refresh")
            .replace("*", "");
        
        removeRefreshToken(refreshToken);
    }

    public String createAccessToken(String username, String email, String role) {
        return Jwts.builder()
            .subject("Weavin User Authentication JWT")
            .claim("username", username)
            .claim("email", email)
            .claim("role", role)
            .issuedAt(new Date())
            .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
            .signWith(this.getSigningKey())
            .compact();
    }

    public String createRefreshToken() {
        return Jwts.builder()
            .subject("Weavin User Authentication JWT")
            .issuedAt(new Date())
            .expiration(new Date((new Date()).getTime() + jwtRefreshExpirationMs))
            .signWith(this.getSigningKey())
            .compact();
    }

    public boolean isHeaderValid(HttpServletRequest request) {
        String accessJwt = request.getHeader("Authorzation");
        String refreshJwt = request.getHeader("Authentication-refresh");

        if (accessJwt == null || refreshJwt == null) {
            return false;
        }
        return true;
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser().verifyWith(this.getSigningKey()).build().parse(token);    
        }
        catch(Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean isExpiredToken(String token) {
        try {
            Jwts.parser().verifyWith(this.getSigningKey()).build().parse(token);
        }
        catch(ExpiredJwtException e) {
            return true;
        }

        return false;
    }

    public boolean isNeedToUpdateRefreshToken(String token) {
        try {
            Date expiresAt = Jwts.parser().verifyWith(this.getSigningKey()).build().parseSignedClaims(token).getPayload().getExpiration();

            Date current = new Date();
            Calendar calendar = Calendar.getInstance();

            calendar.setTime(current);
            calendar.add(Calendar.DATE, 7);

            if (expiresAt.before(calendar.getTime())) {
                return true;
            }
            return false;
        }
        catch(ExpiredJwtException e) {
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
