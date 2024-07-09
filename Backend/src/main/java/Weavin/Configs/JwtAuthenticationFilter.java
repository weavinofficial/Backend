package Weavin.Configs;

import java.io.IOException;
import java.util.HashMap;


import org.json.simple.JSONObject;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import Weavin.Models.WeavinUserDetails;
import Weavin.Services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
        super(authenticationManager);
        this.jwtService = jwtService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("Attempting Authentication");
        String username = this.obtainUsername(request);
        String password = this.obtainPassword(request);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
                password);

        Authentication authentication = this.getAuthenticationManager().authenticate(authenticationToken);

        return authentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {

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

        System.out.println("Fail Authentication");

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


}
