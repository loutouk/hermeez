package boursier.louis.hermeez.backend.security;

import boursier.louis.hermeez.backend.apierror.CustomAccessDeniedHandler;
import boursier.louis.hermeez.backend.entities.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/*@Configuration
@EnableWebSecurity
@Order(Ordered.HIGHEST_PRECEDENCE)*/
public class Test extends WebSecurityConfigurerAdapter {

    /*@Override
    public void configure(HttpSecurity http) throws Exception {

        http
                .requestMatchers().antMatchers("/test")
                .and()
                .authorizeRequests().anyRequest().hasRole("ADMIN")
                .and()
                .httpBasic();

        http
                // REST so no session but OAuth2 JWT
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                // Restrict API endpoints to users
                .requestMatchers()
                .antMatchers("/test")
                //.hasAuthority(User.Role.USER.name())
                .permitAll()
                .and()

                // Restrict API endpoints to users
                .authorizeRequests()
                .antMatchers("/updatetopremium")
                .hasAuthority(User.Role.USER.name())
                .and()

                // Deny other endpoints (Spring Rest auto generated CRUD endpoints on User) for security
                .authorizeRequests()
                .anyRequest()
                .denyAll()
                .and()

                // No CSRF protection needed for this type of app
                .csrf().disable();
    }*/
}
