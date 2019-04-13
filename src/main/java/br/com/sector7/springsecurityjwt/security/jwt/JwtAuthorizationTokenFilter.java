package br.com.sector7.springsecurityjwt.security.jwt;

import br.com.sector7.springsecurityjwt.security.jwt.service.JwtUserDetailService;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * Filtro de autenticação STATELESS.
 * Responsável por validar a token JWT presente no request a cada requisição.
 *
 * @author bruno.carneiro
 */
@Component
public class JwtAuthorizationTokenFilter extends OncePerRequestFilter {

    // LOGGER
    Logger logger = LoggerFactory.getLogger(this.getClass());

    // Lista de URIs que serão ignoradas pelo filtro
    private static final String [] IGNORED_ASSETS = {
            "/h2-console",
            ".css",
            ".gif",
            ".ico"
    };

    @Autowired
    private JwtUserDetailService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.header}")
    private String tokenHeader;

    /**
     * Filtro responsável pela validação do token JWT.
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {

        // Verifica se URI solicitada deve ser filtrada
        if (!devoIgnorar(request)) {

            // Iniciando regra de filtragem
            logger.info("Essa requisição foi interceptada. URI {}", request.getRequestURI());

            final String requestHeader = request.getHeader(this.tokenHeader);

            String username = null;
            String authToken = null;

            if (request != null && requestHeader.startsWith("Bearer ")) {

                authToken = requestHeader.substring(7);

                try {
                    username = jwtTokenUtil.getUsernameFromToken(authToken);
                }
                catch (IllegalArgumentException e) {
                    logger.error("Um erro aconteceu durante a obtenção do username do token", e);
                }
                catch (ExpiredJwtException e) {
                    logger.error("Token expirada.", e);
                }
            }
            else {
                logger.warn("Não foi possível encontrar a string Bearer no token, ignorando header...");
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails;

                try {
                    userDetails = userDetailsService.loadUserByUsername(username);
                }
                catch (UsernameNotFoundException e) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
                    return;
                }

                if (jwtTokenUtil.validateToken(authToken, userDetails)) {

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    logger.info("Usuário autorizado '{}'", username);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        chain.doFilter(request, response);
    }

    /**
     * Verifica se o request deve ser ignorado pelo filtro.
     * @return true: deve ser ignorado;
     *         false: não deve ser ignorado.
     */
    private boolean devoIgnorar(HttpServletRequest request) {
        return Arrays.stream(IGNORED_ASSETS)
                .filter(path -> request.getRequestURI().contains(path)).count() > 0;
    }
}
