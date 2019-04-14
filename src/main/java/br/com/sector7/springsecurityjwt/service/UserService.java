package br.com.sector7.springsecurityjwt.service;

import br.com.sector7.springsecurityjwt.model.User;

import java.util.List;

public interface UserService {

    User cadastrar(User user);

    User alterar(User user);

    User buscarPorId(Long userId);

    User deletar(User user);

    List<User> listar();

    User buscarPorUsername(String username);

}
