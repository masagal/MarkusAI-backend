package tech.masagal.markusai.filterRequests;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.masagal.markusai.user.User;
import tech.masagal.markusai.user.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;
import java.util.Set;

@Component
public class FilterRequests extends OncePerRequestFilter {
    Logger logger = LogManager.getLogger();

    Set<String> pathsNotRequiringAuth = Set.of("/version",
            "/api/users/resolve-invitation");

    private final UserRepository userRepo;

    public FilterRequests(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    private void breakDueToAuthMissing(HttpServletResponse response) throws IOException {
        response.sendError(403, "Not authorized to access this resource. Please try again with a valid token.");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if(request.getMethod().equals("OPTIONS")) {
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
            String path = request.getRequestURI();
            if(pathsNotRequiringAuth.contains(path)) {
                doFilter(request, response, filterChain);
                return;
            }
            else {
                breakDueToAuthMissing(response);
                return;
            }
        }

        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String payload = new String(decoder.decode(chunks[1]));
        SubDto sub = new ObjectMapper().readValue(payload, SubDto.class);
        request.setAttribute("clerkId", sub.sub());

        User user = userRepo.findByClerkId(sub.sub());
        request.setAttribute("user", user);

        if(user == null && request.getParameter("token") == null) {
            response.sendError(400, "User not found");
            logger.warn("There was a Clerk token, but the user did not exist, and did not have an invite. " +
                    "Killing the request and returning 400.");
            return;
        }
        logger.info("user filter completed, inserting user data into attribute");

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.contains("h2-console") || path.contains("/chat");
    }
}
