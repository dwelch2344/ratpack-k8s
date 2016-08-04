package com.example.playground;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ratpack.handling.Handler;
import ratpack.spring.config.EnableRatpack;

import java.util.HashMap;
import java.util.Map;

@EnableRatpack
@SpringBootApplication
public class Main {
    public static void main(String... args) throws Exception {

        SpringApplication.run(Main.class, args);

//        RatpackServer.start(server -> server
//                .registry(spring(MyConfiguration.class))
//                .handlers(chain -> chain
//                        .get(ctx -> ctx.render(json(map))
//                        .get(":message", ctx -> ctx.render("Hello " + ctx.getPathTokens().get("message") + "!")
//                        )
//                )
//        );
    }




    @Bean
    public Handler handler() {
        final Map inner = new HashMap(){{
            put("Test", "bye");
            put("Test1", false);
        }};


        final Map map = new HashMap(){{
            put("Test", "Hi");
            put("Test1", true);
            put("Test2", inner);
        }};

        return context -> context.render(map);

        // no worky either
//        return context -> context.render(json(map));
    }
}