package boursier.louis.hermeez.backend.security;


import boursier.louis.hermeez.backend.apierror.CustomAccessDeniedHandler;
import boursier.louis.hermeez.backend.apierror.CustomAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
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
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
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
