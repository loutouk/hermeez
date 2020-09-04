package boursier.louis.hermeez.backend.apierror.oauth;

import boursier.louis.hermeez.backend.apierror.ApiError;
import boursier.louis.hermeez.backend.utils.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This class is used by custom OAuth exceptions to write and return their errors.
 * <p>
 * Spring Security's request handling all takes place in the filter chain, before the dispatcher servlet is invoked,
 * so it doesn't know anything about Spring MVC exception handlers. This is why we can not handle OAuth exceptions like
 * the other REST exceptions with the @ExceptionHandler annotation in the CustomRestExceptionHandler.
 * See {@link boursier.louis.hermeez.backend.apierror.CustomRestExceptionHandler}.
 * <p>
 * Use defined in {@link boursier.louis.hermeez.backend.security.ResourceServerConfiguration#configure(HttpSecurity)}.
 */
public class OAuthErrorWriter {

    private static final Logger LOGGER = LogManager.getLogger(OAuthErrorWriter.class);
    private static final HttpMessageConverter<String> messageConverter = new StringHttpMessageConverter();
    private static final ObjectMapper mapper = new ObjectMapper();

    private OAuthErrorWriter() {
        // Private constructor to hide the implicit public one as this is a utility class
    }

    public static void handle(String exceptionName, HttpServletResponse httpServletResponse) throws IOException {
        // TODO differentiate between different http status according to the caller
        LOGGER.error("Exception: " + exceptionName);
        LOGGER.error("HttpServletResponse: " + httpServletResponse.toString());
        ApiError apiError = null;
        if (Constants.DEBUG) {
            apiError = new ApiError(HttpStatus.UNAUTHORIZED, exceptionName, httpServletResponse.toString());
        } else {
            apiError = new ApiError(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "UNAUTHORIZED");
        }
        ServerHttpResponse outputMessage = new ServletServerHttpResponse(httpServletResponse);
        outputMessage.setStatusCode(HttpStatus.UNAUTHORIZED);
        messageConverter.write(mapper.writeValueAsString(apiError), MediaType.APPLICATION_JSON, outputMessage);
    }
}
