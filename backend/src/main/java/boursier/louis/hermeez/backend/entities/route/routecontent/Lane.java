package boursier.louis.hermeez.backend.entities.route.routecontent;

import lombok.Getter;
import lombok.Setter;

/**
 * A Lane represents a turn lane at the corresponding turn location.
 */
@Getter
@Setter
public class Lane {

    // An indication (e.g. marking on the road) specifying the turn lane. A road can have multiple indications
    // (e.g. an arrow pointing straight and left). The indications are given in an array,
    // each containing one of the following types. Further indications might be added on without an API version change.
    private String[] indications; // "indications": ["left", "straight"]
    // a boolean flag indicating whether the lane is a valid choice in the current maneuver
    private boolean valid;

}
