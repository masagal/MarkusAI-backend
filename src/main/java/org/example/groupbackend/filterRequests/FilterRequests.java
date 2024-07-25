package org.example.groupbackend.filterRequests;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.groupbackend.user.User;
import org.example.groupbackend.user.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component
public class FilterRequests extends OncePerRequestFilter {
    Logger logger = LogManager.getLogger();

    private final UserRepository userRepo;

    public FilterRequests(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if(request.getMethod().equals("OPTIONS") || request.getRequestURI().contains("chat")) {
            //preflight
            //add any routes that should not care about user tokens to this condition
            doFilter(request, response, filterChain);
            return;
        }
        String token = request.getHeader("Authorization");
        if(token == null) {
            logger.warn("Null token received on request for {}", request.getRequestURI());
            logger.warn("Headers look like: {}", request.getHeaderNames().toString());
            logger.warn("Auth header looks like: {}", request.getHeader("Authorization"));
        }

        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String payload = new String(decoder.decode(chunks[1]));
        SubDto sub = new ObjectMapper().readValue(payload, SubDto.class);
        User user = userRepo.findByClerkId(sub.sub());
        if(user == null) {
            response.sendError(400, "User not found");
            logger.warn("Read a token that referred to a user not found in our systems. " +
                    "Killing the request and returning 400.");
            return;
        }

        request.setAttribute("user", user);

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return "/api/users".equals(path) || path.contains("h2-console");
    }
}
