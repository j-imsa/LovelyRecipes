package ir.jimsa.LovelyRecipes;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Lovely Recipes API",
                version = "0.3.0",
                description = "Lovely Recipes Api",
                contact = @Contact(name = "Iman", url = "iman.com", email = "cse.isalehi@gmail.com")
        ),
        servers = {
                @Server(
                        description = "Localhost",
                        url = "http://localhost:8090/"
                )
        }
)
public class LovelyRecipesApplication {

    public static void main(String[] args) {
        SpringApplication.run(LovelyRecipesApplication.class, args);
    }

}
