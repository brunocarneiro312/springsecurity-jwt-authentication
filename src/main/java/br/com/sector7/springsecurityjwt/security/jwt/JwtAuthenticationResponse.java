package br.com.sector7.springsecurityjwt.security.jwt;

import java.io.Serializable;

/**
 * Representa usuário logado na aplicação.
 */
public class JwtAuthenticationResponse implements Serializable {

    private static final long serialVersionUID = 1l;

    private String token;
    private JwtUser jwtUser;

    public JwtAuthenticationResponse(String token, JwtUser jwtUser) {
        this.token = token;
        this.jwtUser = jwtUser;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public JwtUser getJwtUser() {
        return jwtUser;
    }

    public void setJwtUser(JwtUser jwtUser) {
        this.jwtUser = jwtUser;
    }
}
