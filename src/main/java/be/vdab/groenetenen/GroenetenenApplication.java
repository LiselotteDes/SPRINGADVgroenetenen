package be.vdab.groenetenen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class GroenetenenApplication {

	public static void main(String[] args) {
		SpringApplication.run(GroenetenenApplication.class, args);
	}
}
