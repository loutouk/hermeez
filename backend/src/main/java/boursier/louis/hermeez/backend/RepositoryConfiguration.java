package boursier.louis.hermeez.backend;

import boursier.louis.hermeez.backend.utils.Constants;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

/**
 * Changes the default base bath of the CRUD User repository by prepending our API name to it.
 * See {@link boursier.louis.hermeez.backend.UserRepository}.
 * This allows for applying security filter on this repository and regulate its access.
 * See {@link boursier.louis.hermeez.backend.security.ResourceServerConfiguration}.
 */
@Configuration
public class RepositoryConfiguration extends RepositoryRestConfigurerAdapter
{

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config)
    {
        config.setBasePath("/" + Constants.API_NAME);
    }
}