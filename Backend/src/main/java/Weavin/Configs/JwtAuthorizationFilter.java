package Weavin.Configs;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import Weavin.Entities.User;
import Weavin.Models.WeavinUserDetails;
import Weavin.Services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthorizationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtService jwtService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
        super(authenticationManager);
        this.jwtService = jwtService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;

            if (! jwtService.isHeaderValid((HttpServletRequest) request)) {
                request.setAttribute("Exception", "Invalid header");
                chain.doFilter(request, response);
                return;
            }

            String accessToken = httpRequest.getHeader("Authorization").replace("Bearer ", "");
            String refreshToken = httpRequest.getHeader("Authentication-refresh").replace("Bearer ", "");

            if (! jwtService.isTokenValid(refreshToken)) {
                request.setAttribute("Exception", "Invalid refresh token");
                chain.doFilter(request, response);
                return;
            }

            User user = jwtService.getUserByRefreshToken(refreshToken);
            String username = user.getUsername();
            String email = user.getEmail();
            String role = user.getRole().name();

            if (jwtService.isNeedToUpdateRefreshToken(refreshToken)) {
                String newRefreshToken = jwtService.createRefreshToken();
                jwtService.setRefreshToken(username, newRefreshToken);
                httpResponse.addHeader("Authentication-refresh", "Bearer " + newRefreshToken);
            }

            if (jwtService.isExpiredToken(accessToken)) {
                accessToken = jwtService.createAccessToken(username, email, role);
                httpResponse.addHeader("Authorization", "Bearer " + accessToken);
            }

            WeavinUserDetails userDetails = new WeavinUserDetails(user);
            Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        chain.doFilter(request, response);
    }
}