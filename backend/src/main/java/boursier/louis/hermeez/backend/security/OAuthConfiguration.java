package boursier.louis.hermeez.backend.security;

import boursier.louis.hermeez.backend.utils.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@Configuration
@EnableAuthorizationServer
// The service handling authorization process, the middleman between client and resource owner
public class OAuthConfiguration extends AuthorizationServerConfigurerAdapter {

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    private final UserDetailsService userService;

    @Value("${jwt.clientId:" + Constants.JWT_CLIENT_ID + "}")
    private String clientId;

    @Value("${jwt.client-secret:" + Constants.JWT_CLIENT_SECRET + "}")
    private String clientSecret;

    @Value("${jwt.accessTokenValidititySeconds:" + Constants.ACCESS_TOKEN_VALIDITY_SECONDS + "}")
    private int accessTokenValiditySeconds;

    // Because we are both the resource server and resource owner, we can use the OAuth password grant flow
    @Value("${jwt.authorizedGrantTypes:password,authorization_code,refresh_token}")
    private String[] authorizedGrantTypes;

    @Value("${jwt.refreshTokenValiditySeconds:" + Constants.REFRESH_TOKEN_VALIDITY_SECONDS + "}")
    private int refreshTokenValiditySeconds;

    public OAuthConfiguration(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, UserDetailsService userService) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient(clientId)
                .secret(passwordEncoder.encode(clientSecret))
                .accessTokenValiditySeconds(accessTokenValiditySeconds)
                .refreshTokenValiditySeconds(refreshTokenValiditySeconds)
                .authorizedGrantTypes(authorizedGrantTypes)
                .scopes(Constants.HERMEEZ_MOBILE_APP_SCOPE)
                .resourceIds(Constants.API_NAME);
    }


    /**
     * Authorization server endpoint.
     * Define the use of a JWT token with accessTokenConverter.
     * Define the use of an UserDetService and AuthManager interfaces to perform authentication (as resource owner).
     *
     * @param endpoints
     */
    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .tokenStore(tokenStore())
                .accessTokenConverter(accessTokenConverter())
                .userDetailsService(userService)
                .authenticationManager(authenticationManager);
    }

    @Bean
    TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }

    @Bean
    JwtAccessTokenConverter accessTokenConverter() {
        return new JwtAccessTokenConverter();
    }

    @Bean
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }

}