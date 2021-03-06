package boursier.louis.hermeez.backend;

import boursier.louis.hermeez.backend.controllers.user.UserController;
import boursier.louis.hermeez.backend.controllers.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BackendApplication implements CommandLineRunner {

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserController controller;

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //repository.deleteAll();
    }

}