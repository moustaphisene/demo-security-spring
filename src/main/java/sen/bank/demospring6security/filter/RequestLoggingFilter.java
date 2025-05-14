package sen.bank.demospring6security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class RequestLoggingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String clientIP = request.getRemoteAddr();
        String requestURI = request.getRequestURI();

        System.out.println("[RequestLoggingFilter] IP: " + clientIP + " | URI: " + requestURI);

        // Continuer la chaîne de filtres
        filterChain.doFilter(request, response);
    }
}
