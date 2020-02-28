package org.web.controller;

import com.sun.net.httpserver.BasicAuthenticator;
import org.test.di.annotations.Autowired;
import org.test.di.annotations.Component;

@Component
public class CustomAuthenticator extends BasicAuthenticator {

    @Autowired
    private static UserAuthentication authentication;

    public CustomAuthenticator(){
        super("/");
    }
    
    public CustomAuthenticator(String realm) {
        super(realm);
    }

    @Override
    public boolean checkCredentials(String user, String pwd) {
        return authentication.authenticate(user, pwd);
    }
}