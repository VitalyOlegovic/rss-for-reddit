package reddit_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;

@EnableScheduling
@SpringBootApplication
@ComponentScan(basePackages = { "reddit_bot" })
@EnableAutoConfiguration
@PropertySource("classpath:application.properties")
public class SpringBootStart {

    public static void main(String ... args){
        SpringApplication.run(SpringBootStart.class, args);
    }

}
