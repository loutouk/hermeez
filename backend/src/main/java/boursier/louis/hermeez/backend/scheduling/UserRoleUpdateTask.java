package boursier.louis.hermeez.backend.scheduling;


import boursier.louis.hermeez.backend.usecases.user.MongoUserOperations;
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
    private static final int UPDATE_USER_ROLE_TASK_DELAY_MILLISECONDS = 60000;

    @Async
    @Scheduled(fixedDelay = UPDATE_USER_ROLE_TASK_DELAY_MILLISECONDS,
            initialDelay = UPDATE_USER_ROLE_TASK_DELAY_MILLISECONDS)
    public void reportCurrentTime() {
        LOGGER.info("The time is now {}", dateFormat.format(new Date()));
        // TODO
    }
}