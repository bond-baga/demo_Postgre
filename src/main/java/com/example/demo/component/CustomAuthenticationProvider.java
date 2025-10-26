package com.example.demo.component;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.dto.LoginForm;
import com.example.demo.service.LoginService;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final LoginService loginService;
    private final BCryptPasswordEncoder passwordEncoder;
    public CustomAuthenticationProvider(LoginService loginService, BCryptPasswordEncoder passwordEncoder) {
        this.loginService = loginService;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = (String) authentication.getPrincipal();
        String Password = (String) authentication.getCredentials();

        LoginForm loginForm = loginService.select(username);
        
        if(loginForm.getLoginid() == 0){
            throw new UsernameNotFoundException("入力されたユーザーIDは存在しませんでした。");
        }

        if (passwordEncoder.matches(Password, loginForm.getPassword())) {
            return new UsernamePasswordAuthenticationToken(username, Password, null);
        } else {
            throw new BadCredentialsException("パスワードが一致しませんでした。");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
