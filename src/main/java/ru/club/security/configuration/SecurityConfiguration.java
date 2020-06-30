package ru.club.security.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import ru.club.models.Role;
import ru.club.security.filters.TokenAuthFilter;
import ru.club.security.providers.TokenAuthentificationProvider;

@Configuration
@ComponentScan(basePackages = "ru.club")
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private TokenAuthentificationProvider provider;
    @Autowired
    private TokenAuthFilter filter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
//                .authorizeRequests().antMatchers("/**").permitAll();
                .addFilterBefore(filter, BasicAuthenticationFilter.class)
                .antMatcher("/**")
                .authenticationProvider(provider)
                .authorizeRequests()
                .antMatchers("/login", "/signup/**").permitAll()
                .antMatchers("/users").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
                .antMatchers("/clubs/**").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name());

        http
                .csrf().disable()
                .sessionManagement().disable();
    }
}
