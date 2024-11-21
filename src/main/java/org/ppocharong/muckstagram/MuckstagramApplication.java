package org.ppocharong.muckstagram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "org.ppocharong.muckstagram.repository")
public class MuckstagramApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(MuckstagramApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(MuckstagramApplication.class);
    }

}
