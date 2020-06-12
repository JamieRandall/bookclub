package ru.club.security.token;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class TokenAuthentification implements Authentication {
    private String token;
    private boolean isAuthentificated;
    private UserDetails userDetails;

    public TokenAuthentification(String token) {
        this.token = token;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userDetails.getAuthorities();
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return userDetails;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public boolean isAuthenticated() {
        return this.isAuthentificated;
    }

    @Override
    public void setAuthenticated(boolean isAuthentificated) throws IllegalArgumentException {
        this.isAuthentificated = isAuthentificated;
    }

    @Override
    public String getName() {
        return token;
    }
}
