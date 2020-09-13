package boursier.louis.hermeez.backend.apierror.routeoperationserror;

public class OSRMQueryException extends RuntimeException {

    private OSRMQueryException(String message) {
        // Constructor with String parameter made private to avoid giving clients too much information about the error
    }

    public OSRMQueryException() {
        super("routing server error");
    }
}