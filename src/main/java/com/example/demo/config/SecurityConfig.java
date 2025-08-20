package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.example.demo.component.CustomAuthenticationProvider;

@Configuration
@EnableWebSecurity
public class SecurityConfig {  

	private final CustomAuthenticationProvider customAuthenticationProvider;

    public SecurityConfig(CustomAuthenticationProvider customAuthenticationProvider){
        this.customAuthenticationProvider = customAuthenticationProvider;
    }	

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
	  http// カスタム認証プロバイダを設定
      .authenticationProvider(customAuthenticationProvider)
	  .formLogin((login) -> login
    	.loginProcessingUrl("/authenticate")
        .loginPage("/login")
        .defaultSuccessUrl("/",true)
        .failureUrl("/login?failed=true") 
        .permitAll()
    ).logout((logout) -> logout
    	.logoutSuccessUrl("/login?logout=true")
    	.permitAll()        
    ).authorizeHttpRequests((authz) -> authz
        .requestMatchers("/register","/open","/open_page","/css/**","/videofiles/**").permitAll()
    	//.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()    		
        .anyRequest().authenticated()
     );
	//.csrf((csrf) -> csrf.disable());  
    return http.build();
  }
}
  
  
