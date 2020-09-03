package boursier.louis.hermeez.backend.security;

import boursier.louis.hermeez.backend.apierror.oauth.CustomAccessDeniedHandler;
import boursier.louis.hermeez.backend.apierror.oauth.CustomAuthenticationEntryPoint;
import boursier.louis.hermeez.backend.utils.Constants;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer // The service who actually supplies the resources
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    public ResourceServerConfiguration(CustomAuthenticationEntryPoint customAuthenticationEntryPoint) {
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }

    /**
     * Definition of resource identification in order to match the client’s access defined in OAuthConfiguration.
     * See {@link boursier.louis.hermeez.backend.security.OAuthConfiguration#configure(ClientDetailsServiceConfigurer)}.
     *
     * @param resources
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(Constants.API_NAME);
    }

    /**
     * Order matters for URLs matching, so URLs must be defined from the most specific to the more general path,
     * so that no path is overridden by the following ones.
     *
     * @param http
     * @throws Exception
     */
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
                .antMatchers("/" + Constants.API_NAME + "/users", "/" + Constants.API_NAME + "/users/**").hasAuthority("OMNISCIENT")
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint).accessDeniedHandler(new CustomAccessDeniedHandler());
    }

}