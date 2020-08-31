package boursier.louis.hermeez.backend;

import boursier.louis.hermeez.backend.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * This class delivers auto generated CRUD operations on the chosen entity thanks to the MongoRepository interface.
 * <p>
 * Repositories may be enhanced with multiple custom implementations.
 * Custom implementations have a higher priority than the base implementation and repository aspects.
 * This ordering overrides base repository and aspect methods and resolves ambiguity
 * if two fragments contribute the same method signature.
 * <p>
 * Extending the fragment interface with a repository interface combines the CRUD
 * and custom functionality and makes it available to clients with the HATEOAS architecture.
 */

@RepositoryRestResource(collectionResourceRel = "users", path = "users")
public interface UserRepository extends MongoRepository<User, String>, CustomizedUserService {

    User findByEmail(@Param("email") String email);

}