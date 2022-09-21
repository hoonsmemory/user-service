package me.hoon.userservice.config;


import me.hoon.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Autowired
    private Environment env;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
       // http.authorizeRequests().antMatchers("/users/**").permitAll();
        http.authorizeRequests()
                        .antMatchers("/**")
                        .hasIpAddress("192.168.35.181")
                        .and()
                        .addFilter(authenticationFilter());

        http.headers().frameOptions().disable();
    }

    private AuthenticationFilter authenticationFilter() throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(userService, env, authenticationManager());
        return authenticationFilter;
    }

    //스프링 시큐리티가 사용자를 인증하는 방법이 담긴 객체.
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                //Select pwd from users where email = ?
                .userDetailsService(userService)
                //db 안에 있는 pwd(encrypted) == 입력한 pwd
                //패스워드를 암호화 한다.
                .passwordEncoder(passwordEncoder());
    }
}
