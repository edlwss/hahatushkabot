package ru.edel.java.hahatushkabot.model;

import org.springframework.security.core.GrantedAuthority;

public enum UserAuthority implements GrantedAuthority {

    USER,
    MODERATOR,
    ADMIN;

    @Override
    public String getAuthority() {
        return this.name();
    }

}
