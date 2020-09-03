package boursier.louis.hermeez.backend;

import boursier.louis.hermeez.backend.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * Mongo specific interface for generic CRUD operations on a repository for the specific User type.
 * Extending MongoRepository allows the class to map the database operations into RESTful API endpoints
 * so external clients can use them. Spring will automatically create REST endpoints for the User POJO.
 * See {@link boursier.louis.hermeez.backend.entities.User}.
 * <p>
 * Repositories may be enhanced with multiple custom implementations.
 * Custom implementations have a higher priority than the base implementation and repository aspects.
 * This ordering overrides base repository and aspect methods and resolves ambiguity
 * if two fragments contribute the same method signature.
 * <p>
 * Extending the fragment interface with a repository interface combines the CRUD
 * and custom functionality and makes it available to clients with the HATEOAS architecture.
 */
public interface UserRepository extends MongoRepository<User, String> {

    @RestResource(exported = false)
    User findByEmail(@Param("email") String email);

    //@RestResource(exported = false)
    List<User> findAll();
}