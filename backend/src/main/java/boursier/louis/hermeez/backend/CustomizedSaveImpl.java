package boursier.louis.hermeez.backend;

import boursier.louis.hermeez.backend.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;

/**
 * Lets Spring know that this class may override {@link boursier.louis.hermeez.backend.UserRepository} auto
 * generated functions behaviour thanks to the Impl postfix. Spring will prioritize our implementation over theirs.
 */

/**
 * @deprecated replaced by Spring constraint validation {@link boursier.louis.hermeez.backend.userconstraints}
 */
@Deprecated
interface CustomizedSave<T> {
    <S extends T> S save(S entity);
}

/**
 * @deprecated replaced by Spring constraint validation {@link boursier.louis.hermeez.backend.userconstraints}
 */
@Deprecated
class CustomizedSaveImpl<T> implements CustomizedSave<T> {

    @Autowired
    private MongoOperations mongoOperations;

    public <S extends T> S save(S entity) {
        User user = (User) entity;
        // TODO place business logic here
        mongoOperations.save(user);
        return (S) user;
    }
}
