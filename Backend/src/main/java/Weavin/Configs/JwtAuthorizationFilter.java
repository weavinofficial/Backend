package Weavin.Configs;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import Weavin.Entities.User;
import Weavin.Enums.Role;
import Weavin.Models.WeavinUserDetails;
import Weavin.Repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.IOException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private String SECRET_KEY = "ASecretKeyLongEnoughToBeSecureButNotTooLongToBeUnreadableDontLaughAtMePleaseXD";

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;

            String jwtHeader = httpRequest.getHeader("Authorization");
            if (jwtHeader == null || !jwtHeader.startsWith("Bearer ")) {
                chain.doFilter(request, response);

                return;
            }

            String jwtToken = jwtHeader.replace("Bearer ", "");
            String username, email = null;
            Role role = Role.UNAUTHENTICATED;

            JwtParser parser = Jwts
                    .parser()
                    .verifyWith(this.getSigningKey())
                    .build();

            Claims claims = parser.parseSignedClaims(jwtToken).getPayload();
            username = claims.get("username", String.class);
            email = claims.get("email", String.class);
            String roleString = claims.get("role", String.class);

            role = Role.getRoleFromString(roleString);

            WeavinUserDetails userDetails = new WeavinUserDetails(new User(username, email, role));
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                    userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);

            System.out.println("Authorization Success");

            chain.doFilter(httpRequest, httpResponse);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
