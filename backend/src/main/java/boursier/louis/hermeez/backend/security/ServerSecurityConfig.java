package boursier.louis.hermeez.backend.security;


import boursier.louis.hermeez.backend.apierror.CustomAccessDeniedHandler;
import boursier.louis.hermeez.backend.apierror.CustomAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Order(1)
@Configuration
/**
 * EnableWebSecurity will provide configuration via HttpSecurity on the url pattern level.
 * See {@link boursier.louis.hermeez.backend.security.ServerSecurityConfig#configure(HttpSecurity)}.
 */
@EnableWebSecurity
/**
 * The @EnableGlobalMethodSecurity permits to specify security on the method level,
 * some of annotation it will enable are PreAuthorize PostAuthorize.
 * Its attribute proxyTargetClass is set in order to have this working for RestController’s methods,
 * because controllers are usually classes, not implementing any interfaces.
 * See {@link boursier.louis.hermeez.backend.Controller}.
 */
//@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ServerSecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    private final UserDetailsService userDetailsService;

    public ServerSecurityConfig(CustomAuthenticationEntryPoint customAuthenticationEntryPoint, @Qualifier("userService")
            UserDetailsService userDetailsService) {
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * Restrict Spring Data REST endpoints access to their owner only.
     * See {@link boursier.louis.hermeez.backend.UserRepository}.
     */
    @Component("userSecurity")
    public class UserSecurity { public boolean hasUserId(Principal principal, String userId) {
            System.out.println(principal.getName());
            System.out.println(userId);
            return true;
        }
    }

    /**
     * URL of the HATEOAS architecture have been auto generated with the MongoRepository interface.
     * Access to those endpoints should be regulated for security reasons.
     * TODO Only authorize admin role to access those endpoint, and authorize user & premium for the remaining ones
     * <p>
     * When matching the specified patterns against an incoming request,
     * the matching is done in the order in which the elements are declared.
     * So the most specific matches patterns should come first and the most general should come last.
     * <p>
     * SessionCreationPolicy.STATELESS has the direct implication that cookies are not used and
     * so each and every request needs to be re-authenticated. In accordance with the REST architecture.
     *
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                // TEST
                .authorizeRequests()
                .antMatchers("/")
                .permitAll()
                .and()

                // TEST
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/users")
                .permitAll()
                .and()

                .authorizeRequests()
                .antMatchers("/users/{userId}")
                .access("@userSecurity.hasUserId(authentication,#userId)")
                .and()

                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint).accessDeniedHandler(new CustomAccessDeniedHandler());
    }

    /**
     * Allows unauthenticated users to access the registration and log in functions.
     * This will grant them access to an OAuth token that will be needed for other requests.
     * <p>
     * Because this is the weak point, measures should be taken to protect it:
     * Client side: reCAPTCHA
     * Server side: IP tracking, call rate limiting and DDOS protection
     * <p>
     * An email validation protocol could be used to reduce the creation of fake accounts
     *
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        // TODO figure out a way to provide OAuth token with response and best practices about it
        web
                .ignoring().antMatchers("/signin").and()
                .ignoring().antMatchers("/register");
    }

}
