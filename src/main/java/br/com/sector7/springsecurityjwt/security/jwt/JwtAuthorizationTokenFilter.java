package br.com.sector7.springsecurityjwt.security.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
