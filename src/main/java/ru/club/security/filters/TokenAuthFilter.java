package ru.club.security.filters;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.club.security.token.TokenAuthentification;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class TokenAuthFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        String token = req.getHeader("Authorization");

        TokenAuthentification authentification = new TokenAuthentification(token);

        if (token == null) {
            authentification.setAuthenticated(false);
        } else {
            SecurityContextHolder.getContext().setAuthentication(authentification);
        }

        chain.doFilter(request, response);

    }

    @Override
    public void destroy() {

    }
}
