package br.com.sector7.springsecurityjwt.security.jwt;

import br.com.sector7.springsecurityjwt.model.Authority;
import br.com.sector7.springsecurityjwt.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe factory JwtUser
 */
public class JwtUserFactory {

    public static JwtUser create(User user) {
        return new JwtUser(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                mapToGrantedAuthorities(user.getAuthorities()),
                user.isEnabled());
    }

    /**
     * Faz o parse de um Authority para um GrantedAuthority
     * @param authorities
     * @return
     */
    public static List<GrantedAuthority> mapToGrantedAuthorities(List<Authority> authorities) {
        return authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getRoleName().name()))
                .collect(Collectors.toList());
    }
}
