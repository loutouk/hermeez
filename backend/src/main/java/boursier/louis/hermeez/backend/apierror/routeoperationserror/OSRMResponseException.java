package boursier.louis.hermeez.backend.apierror.routeoperationserror;

public class OSRMResponseException extends RuntimeException {

    private OSRMResponseException(String message) {
        // Constructor with String parameter made private to avoid giving clients too much information about the error
    }

    public OSRMResponseException() {
        super("routing server error");
    }
}