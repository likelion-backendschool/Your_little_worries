package likelion.ylw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class YlwApplication {

	public static void main(String[] args) {
		System.out.println("");
		SpringApplication.run(YlwApplication.class, args);
	}

}
