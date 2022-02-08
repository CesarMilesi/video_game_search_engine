package fr.lernejo.fileinjector;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class Launcher {

    public static void main(String[] args) throws Exception {
        try (AbstractApplicationContext springContext = new AnnotationConfigApplicationContext(Launcher.class)) {
            if (args.length != 1) {
                throw new Exception("Wrong argument");
            }
            else {
                RabbitTemplate rabbitTemplate = springContext.getBean(RabbitTemplate.class);
                ObjectMapper mapper = new ObjectMapper();
                List<GameJson> gameTable = Arrays.asList(mapper.readValue(new File(args[0]), GameJson[].class));
                rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
                for (GameJson message : gameTable) {
                    rabbitTemplate.convertAndSend("","game_info", message, m -> {
                        m.getMessageProperties().getHeaders().put("game_id", message.id);
                        return m;
                    });
                }
            }
        }
    }
}
