package br.com.sector7.springsecurityjwt.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeRestAPI {

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        try {
            return ResponseEntity.ok("HELLO GUEST");
        }
        catch (AccessDeniedException e) {
            return new ResponseEntity<>("Acesso negado", HttpStatus.UNAUTHORIZED);
        }
        catch (Exception e) {
            return new ResponseEntity<>("Erro durante execução do serviço", HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/helloAdmin")
    public ResponseEntity<String> helloAdmin() {
        try {
            return ResponseEntity.ok("HELLO ADMIN");
        }
        catch (AccessDeniedException e) {
            return new ResponseEntity<>("Acesso negado", HttpStatus.UNAUTHORIZED);
        }
        catch (Exception e) {
            return new ResponseEntity<>("Erro durante execução do serviço", HttpStatus.BAD_REQUEST);
        }
    }
}
