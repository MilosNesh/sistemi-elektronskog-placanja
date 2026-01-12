package org.example.pspbackend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.pspbackend.domain.Merchant;
import org.example.pspbackend.service.MerchantService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final TokenUtil tokenUtil;
    private final MerchantService merchantService;

    public JwtAuthenticationFilter(TokenUtil tokenUtil, MerchantService merchantService) {
        this.tokenUtil = tokenUtil;
        this.merchantService = merchantService;
    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();

        return path.equals("/payment/status") ||
        path.matches("/payment/.*/make") ||
        path.matches("/payment-method/merchant/.*")
                || path.startsWith("/payment/redirect/");
    }
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String email;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);

        email = tokenUtil.getEmailFromToken(jwt);
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            Merchant merchant = merchantService.getByEmail(email);

            if (merchant != null && tokenUtil.validateToken(jwt, merchant)) {

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                List.of()
                        );
                //authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 9. Postavi autentifikaciju u SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }

}
