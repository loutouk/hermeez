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

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OAuthErrorHandler {

    private static final Logger LOGGER = LogManager.getLogger(OAuthErrorHandler.class);
    private static final HttpMessageConverter<String> messageConverter = new StringHttpMessageConverter();
    private static final ObjectMapper mapper = new ObjectMapper();

    private OAuthErrorHandler() {
        // Private constructor to hide the implicit public one as this is a utility class
    }

    public static void handle(String exceptionName, HttpServletResponse httpServletResponse) throws IOException {
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
