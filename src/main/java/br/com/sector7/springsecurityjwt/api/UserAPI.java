package br.com.sector7.springsecurityjwt.api;

import br.com.sector7.springsecurityjwt.model.User;
import br.com.sector7.springsecurityjwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * ---------------
 * API de usuários
 * ---------------
 * @author bruno.carneiro
 * @since 04/2019
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserAPI {

    private UserService userService;

    @Autowired
    public UserAPI(UserService userService) {
        this.userService = userService;
    }

    /**
     * -----------
     * save [POST]
     * -----------
     */
    @PostMapping
    public ResponseEntity<User> save(@RequestBody User user) {

        HttpStatus responseStatus;
        User responseUser = null;

        try {
            responseUser = this.userService.cadastrar(user);
            responseStatus = responseUser != null
                    ? HttpStatus.OK
                    : HttpStatus.NO_CONTENT;
        }
        catch (Exception e) {
            e.printStackTrace();
            responseStatus = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(responseUser, responseStatus);

    }

    /**
     * ---------------
     * delete [DELETE]
     * ---------------
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<User> remove(@PathVariable("userId") Long id) {

        User responseUser = null;
        HttpStatus responseStatus;

        try {
            User user = this.userService.buscarPorId(id);
            responseUser = this.userService.deletar(user);
            responseStatus = responseUser != null
                    ? HttpStatus.OK
                    : HttpStatus.NO_CONTENT;
        }
        catch (Exception e) {
            e.printStackTrace();
            responseStatus = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(responseUser, responseStatus);
    }

    /**
     * ------------
     * update [PUT]
     * ------------
     */
    @PutMapping
    public ResponseEntity<User> update(@RequestBody User user) {

        User responseUser = null;
        HttpStatus responseStatus;

        try {
            responseUser = this.userService.alterar(user);
            responseStatus = responseUser != null
                    ? HttpStatus.OK
                    : HttpStatus.NO_CONTENT;
        }
        catch (Exception e) {
            e.printStackTrace();
            responseStatus = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(responseUser, responseStatus);
    }

    /**
     * -------------
     * findOne [GET]
     * -------------
     */
    @GetMapping("/{userId}")
    public ResponseEntity<User> findById(@PathVariable("userId") Long id) {

        User responseUser = null;
        HttpStatus responseStatus;

        try {
            responseUser = this.userService.buscarPorId(id);
            responseStatus = responseUser != null
                    ? HttpStatus.OK
                    : HttpStatus.NO_CONTENT;
        }
        catch (Exception e) {
            e.printStackTrace();
            responseStatus = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(responseUser, responseStatus);
    }

    /**
     * ----------
     * list [GET]
     * ----------
     */
    @GetMapping
    public ResponseEntity<List<User>> findAll() {

        List<User> responseUserList = new ArrayList<>();
        HttpStatus responseStatus;

        try {
            responseUserList = this.userService.listar();
            responseStatus = responseUserList.size() > 0
                    ? HttpStatus.OK
                    : HttpStatus.NO_CONTENT;
        }
        catch (Exception e) {
            e.printStackTrace();
            responseStatus = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(responseUserList, responseStatus);
    }
}
