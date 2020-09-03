package boursier.louis.hermeez.backend;

import boursier.louis.hermeez.backend.controllers.Controller;
import boursier.louis.hermeez.backend.controllers.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication implements CommandLineRunner {

    @Autowired
    private UserRepository repository;

    @Autowired
    private Controller controller;

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //repository.deleteAll();
    }

}