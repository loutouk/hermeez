package boursier.louis.hermeez.backend.entities.route;

import lombok.Getter;
import lombok.Setter;

/**
 * Corresponds to the response of the OSRM API routing server for routing queries.
 * Doc is available at http://project-osrm.org/docs/v5.5.1/api/
 */
@Getter
@Setter
public class OSRMResponse {

    private String response;

    private Class Route () {
        float distance;
        float durantion_seconds;
        Geometry geometry;
    }

    public OSRMResponse(String response) {
        this.response = response;
    }

}
