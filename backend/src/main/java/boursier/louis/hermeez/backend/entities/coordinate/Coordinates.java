package boursier.louis.hermeez.backend.entities.coordinate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Coordinates {

    private final String rawContent;
    // TODO custom class constraint
    private Coordinate[] coordinates; // 90.0,90.0;90.0,90.0;90.0,90.0

    public Coordinates(String rawContent) {
        this.rawContent = rawContent;
    }

    // TODO put in a class level validation
    public static Coordinate[] extractCoordinates(String rawContent) {
        if (rawContent == null) {
            return null;
        }
        String[] rawCoordinates = rawContent.split(";");
        Coordinate[] coordinates = new Coordinate[rawCoordinates.length];
        for (int i = 0; i < rawCoordinates.length; i++) {
            coordinates[i] = new Coordinate(rawCoordinates[i]);
        }
        return coordinates;
    }
}
