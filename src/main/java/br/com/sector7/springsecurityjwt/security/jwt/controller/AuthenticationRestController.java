package br.com.sector7.springsecurityjwt.security.jwt.controller;

import br.com.sector7.springsecurityjwt.model.User;
import br.com.sector7.springsecurityjwt.security.jwt.*;
import br.com.sector7.springsecurityjwt.security.jwt.service.JwtUserDetailService;
import br.com.sector7.springsecurityjwt.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Objects;

@RestController
public class AuthenticationRestController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUserDetailService userDetailsService;

    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest)
            throws AuthenticationException {

        try {
            authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

            final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
            final String token = jwtTokenUtil.generateToken(userDetails);

            User loggedUser = this.userService.buscarPorUsername(userDetails.getUsername());

            ArrayList<GrantedAuthority> authorities = new ArrayList<>(userDetails.getAuthorities());

            JwtUser jwtUser = new JwtUser(
                    loggedUser.getId(),
                    loggedUser.getEmail(),
                    loggedUser.getPassword(),
                    JwtUserFactory.mapToGrantedAuthorities(loggedUser.getAuthorities()),
                    loggedUser.isEnabled());

            return ResponseEntity.ok(new JwtAuthenticationResponse(token, jwtUser));

        }
        catch (AuthenticationException authException) {
            String errorString = "Credenciais inválidas!";
            logger.error(errorString, authException);
            return new ResponseEntity<>("Credenciais inválidas!", HttpStatus.UNAUTHORIZED);
        }
        catch (Exception e) {
            String errorString = null;
            if (authenticationRequest.getUsername() == null || authenticationRequest.getPassword() == null) {
                errorString = "Parâmetros inválidos. Certifique-se de que o username e password foram informados";
            }
            else {
                errorString = "Erro durante a autenticação do usuário.";
            }
            logger.error(errorString, e);
            return new ResponseEntity<>(errorString, HttpStatus.BAD_REQUEST);

        }
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String authToken = request.getHeader(tokenHeader);
        final String token = authToken.substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);

        if (jwtTokenUtil.canTokenBeRefreshed(token)) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken, user));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    private void authenticate(String username, String password) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new AuthenticationException("Usuário não habilitado", e);
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Erro de autenticação", e);
        }
    }
}
