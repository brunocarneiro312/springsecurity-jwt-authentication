package br.com.sector7.springsecurityjwt.service.impl;

import br.com.sector7.springsecurityjwt.model.User;
import br.com.sector7.springsecurityjwt.repository.UserRepository;
import br.com.sector7.springsecurityjwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User cadastrar(User user) {
        return userRepository.save(user);
    }

    @Override
    public User alterar(User user) {
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
