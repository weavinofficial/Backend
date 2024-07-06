package Weavin.Configs;

import java.io.IOException;
import java.util.HashMap;

import javax.crypto.SecretKey;

import org.json.simple.JSONObject;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import Weavin.Entities.User;
import Weavin.Enums.Role;
import Weavin.Models.WeavinUserDetails;
import Weavin.Services.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    private final String jwtSecret = "tempSecretKey";

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = this.obtainUsername(request);
        String password = this.obtainPassword(request);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
                password);

        Authentication authentication = this.authenticationManager.authenticate(authenticationToken);

        return authentication;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        System.out.println("Filtering Authentication");

        String jwtHeader = httpRequest.getHeader("Authorization");
        if (jwtHeader == null || !jwtHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);

            return;
        }

        String jwtToken = jwtHeader.replace("Bearer ", "");
        String username, email = null;
        Role role = Role.UNAUTHENTICATED;

        try {
            JwtParser parser = Jwts
                    .parser()
                    .verifyWith(this.getSigningKey())
                    .build();

            Claims claims = parser.parseSignedClaims(jwtToken).getPayload();
            username = claims.get("username", String.class);
            email = claims.get("email", String.class);
            String roleString = claims.get("role", String.class);

            Role.getRoleFromString(roleString);
        } catch (Exception e) {
            chain.doFilter(request, response);
            
            return;
        }

        WeavinUserDetails userDetails = new WeavinUserDetails(new User(username, email, role));
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(httpRequest, httpResponse);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);

        WeavinUserDetails userDetails = (WeavinUserDetails) authResult.getPrincipal();// 10 minutes
        String jwt = jwtService.createAccessToken(userDetails.getUsername(), userDetails.getUser().getEmail(),
                userDetails.getUser().getRole().name());
        String refreshToken = jwtService.createRefreshToken();

        jwtService.setRefreshToken(userDetails.getUsername(), refreshToken);

        response.addHeader("Authorization-refresh", refreshToken);
        response.addHeader("Authorization", "Bearer " + jwt);
        setSuccessResponse(response, "Logged in");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);

        String failReason = failed.getMessage();
        setFailureResponse(response, failReason);
    }

    private void setSuccessResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");

        HashMap<String, Object> map = new HashMap<>();

        map.put("success", true);
        map.put("code", 1);
        map.put("message", message);

        JSONObject jsonObject = new JSONObject(map);
        response.getWriter().print(jsonObject.toJSONString());
    }

    private void setFailureResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        HashMap<String, Object> map = new HashMap<>();

        map.put("success", false);
        map.put("code", -1);
        map.put("message", message);

        JSONObject jsonObject = new JSONObject(map);
        response.getWriter().print(jsonObject.toJSONString());
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
