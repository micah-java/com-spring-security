package com.spring.security.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class SmsLoginAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = 5601L;
    private final Object principal;
    private Object credentials;

    public SmsLoginAuthenticationToken(Object principal,Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(false);
    }

    public SmsLoginAuthenticationToken(Collection<? extends GrantedAuthority> authorities, Object principal,Object credentials) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }


    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        if(authenticated){
            throw new AuthenticationServiceException("不能设置为信任");
        }
        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.credentials = null;
    }
}
