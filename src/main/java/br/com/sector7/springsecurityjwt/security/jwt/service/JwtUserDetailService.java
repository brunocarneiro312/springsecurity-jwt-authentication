package br.com.sector7.springsecurityjwt.security.jwt.service;

import br.com.sector7.springsecurityjwt.model.User;
import br.com.sector7.springsecurityjwt.repository.UserRepository;
import br.com.sector7.springsecurityjwt.security.jwt.JwtUserFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Classe responsável por autenticar o usuários na aplicação
 */
@Service
public class JwtUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException(
                    String.format("Não foi encontrado um usuário cadastrado com o email %s", email));
        }
        else {
            return JwtUserFactory.create(user);
        }
    }
}
