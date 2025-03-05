package work.javiermantilla.tax;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@EnableAutoConfiguration(exclude = R2dbcAutoConfiguration.class)
//@ComponentScan(basePackages = {
//		"work.javiermantilla.tax"
//})
public class TaxesDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaxesDemoApplication.class, args);
	}

}
