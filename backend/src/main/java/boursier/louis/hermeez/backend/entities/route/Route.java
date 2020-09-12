package boursier.louis.hermeez.backend.entities.route;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Route {

    private String rawContent;

    public Route(OSRMResponse osrmResponse) {
        this.rawContent = osrmResponse.getResponse();
    }
}
