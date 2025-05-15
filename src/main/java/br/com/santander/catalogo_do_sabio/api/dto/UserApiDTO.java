package br.com.santander.catalogo_do_sabio.api.dto;

import br.com.santander.catalogo_do_sabio.domain.model.UserApi;

import java.io.Serializable;
import java.util.List;

public class UserApiDTO implements Serializable {

    private String username;
    private List<String> roles;

    public UserApiDTO(UserApi user) {
        this.username = user.getUsername();
        this.roles = user.getRoles();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}