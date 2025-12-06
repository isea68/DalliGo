package com.human.dalligo.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnvConfig {

    @Bean
    public Dotenv dotenv() {
        return Dotenv.configure()
                .directory("./")     // .env가 루트에 있을 때
                .ignoreIfMalformed()
                .ignoreIfMissing()
                .load();
    }
}
