package boursier.louis.hermeez.backend;

import boursier.louis.hermeez.backend.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


interface CustomizedUserService {
    List<User> findByNameAndFirstName(@Param("lastName") String lastName, @Param("firstName") String firstName);
}

/**
 * Lets Spring know that this class may override {@link boursier.louis.hermeez.backend.UserRepository} auto
 * generated functions behaviour thanks to the Impl postfix. Spring will prioritize our implementation over theirs.
 * <p>
 * Having the repository implementing this interface allows clients to see the implemented functions,
 * discoverable as the HATEOAS principles recommend.
 */
@Primary
@Service
public class CustomizedUserServiceImpl implements CustomizedUserService {

    @Autowired
    private MongoOperations mongoOperations;

    @Override
    public List<User> findByNameAndFirstName(String name, String firstName) {
        List<User> users = new ArrayList<>();
        Query searchQuery = new Query();
        searchQuery.addCriteria(Criteria.where("lastName").is(name).and("firstName").is(firstName));
        users = mongoOperations.find(searchQuery, User.class);
        return users;
    }

}