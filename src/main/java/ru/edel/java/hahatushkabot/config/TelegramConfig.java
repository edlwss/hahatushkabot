package ru.edel.java.hahatushkabot.config;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramConfig {

    @Value("${telegram.bot.token}")
    private String telegramToken;

    @Bean
    TelegramBot telegramBot() {
        return new TelegramBot(telegramToken);
    }
}
