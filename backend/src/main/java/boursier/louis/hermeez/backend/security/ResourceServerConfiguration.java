package boursier.louis.hermeez.backend.security;

import boursier.louis.hermeez.backend.apierror.CustomAccessDeniedHandler;
import boursier.louis.hermeez.backend.apierror.CustomAuthenticationEntryPoint;
import boursier.louis.hermeez.backend.utils.Constants;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    public ResourceServerConfiguration(CustomAuthenticationEntryPoint customAuthenticationEntryPoint) {
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(Constants.API_NAME);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .antMatcher("/" + Constants.API_NAME + "/**")
                .authorizeRequests()
                .antMatchers(
                        "/" + Constants.API_NAME + "/signin",
                        "/" + Constants.API_NAME + "/register",
                        "/" + Constants.API_NAME + "/test").permitAll()
                .antMatchers("/" + Constants.API_NAME + "/updatetopremium").hasAuthority("USER")
                .antMatchers("/" + Constants.API_NAME + "/updateemail").hasAuthority("PREMIUM")
                .antMatchers("/" + Constants.API_NAME + "/**").authenticated()
                .antMatchers("/" + Constants.API_NAME + "/users", "/" + Constants.API_NAME + "/users/**").hasAuthority("OMNISCIENT")
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint).accessDeniedHandler(new CustomAccessDeniedHandler());
    }

}