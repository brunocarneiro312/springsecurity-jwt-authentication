package br.com.sector7.springsecurityjwt.security;

import br.com.sector7.springsecurityjwt.security.jwt.JwtAuthenticationEntryPoint;
import br.com.sector7.springsecurityjwt.security.jwt.JwtAuthorizationTokenFilter;
import br.com.sector7.springsecurityjwt.security.jwt.JwtUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtUserDetailService jwtUserDetailService;

    @Autowired
    private JwtAuthorizationTokenFilter jwtAuthorizationTokenFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // Desabilitando proteção CSRF. A token JWT é invulnerável a esse tipo de ataque.
        http.csrf().disable();

        // Criando política de segurança da aplicação
        http.exceptionHandling()
            .authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .authorizeRequests()
                .antMatchers("/h2-console/**/**").permitAll()
                .antMatchers("auth/**").permitAll()
                .anyRequest().authenticated();

        // Adicionando filtro que intercepta todas as requisições
        http.addFilterBefore(jwtAuthorizationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        // Configurações adicionais
        http
            .headers()
            .frameOptions().sameOrigin()
            .cacheControl();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
