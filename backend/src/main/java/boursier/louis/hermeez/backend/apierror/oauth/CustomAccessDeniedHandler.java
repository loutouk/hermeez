package boursier.louis.hermeez.backend.apierror.oauth;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Exception used when OAuth authorization is denied.
 * See {@link boursier.louis.hermeez.backend.security.ResourceServerConfiguration#configure(HttpSecurity)}.
 */
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException {
        OAuthErrorWriter.handle(e.getClass().getName(), httpServletResponse);
    }
}