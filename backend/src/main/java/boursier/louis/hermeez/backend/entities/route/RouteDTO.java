package boursier.louis.hermeez.backend.entities.route;

import java.io.Serializable;

public class RouteDTO implements Serializable {

    public String rawContent;

    public RouteDTO(Route route) {
        this.rawContent = route.getRawContent();
    }
}
