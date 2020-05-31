package bot.hostkeeper.config;

import bot.hostkeeper.server.Bot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    @Bean("bot")
    public Bot getBot() {
        return new Bot();
    }
}
