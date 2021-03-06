package br.com.sector7.springsecurityjwt.service.impl;

import br.com.sector7.springsecurityjwt.model.User;
import br.com.sector7.springsecurityjwt.repository.UserRepository;
import br.com.sector7.springsecurityjwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User cadastrar(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User alterar(User user) {
        user = userRepository.findById(user.getId()).get();
        return userRepository.saveAndFlush(user);
    }

    @Override
    public User buscarPorId(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public User deletar(User user) {
        userRepository.delete(user);
        return user;
    }

    @Override
    public List<User> listar() {
        return userRepository.findAll();
    }

    @Override
    public User buscarPorUsername(String email) {
        return userRepository.findByEmail(email);
    }
}
