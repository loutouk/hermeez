package boursier.louis.hermeez.backend.scheduling;


import boursier.louis.hermeez.backend.usecases.user.MongoUserOperations;
import boursier.louis.hermeez.backend.utils.Constants;
import org.apache.logging.log4j.LogManager;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@EnableAsync
public class UserRoleUpdateTask {

    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(MongoUserOperations.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Async
    @Scheduled(fixedDelay = Constants.UPDATE_USER_ROLE_TASK_DELAY_MILLISECONDS,
            initialDelay = Constants.UPDATE_USER_ROLE_TASK_DELAY_MILLISECONDS)
    public void reportCurrentTime() {
        LOGGER.info("The time is now {}", dateFormat.format(new Date()));
        // TODO
    }
}