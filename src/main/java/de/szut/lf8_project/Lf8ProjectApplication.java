package de.szut.lf8_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class Lf8ProjectApplication {
    public static boolean DISABLE_CACHE = false;

    public static void main(String[] args) {
        DISABLE_CACHE = Arrays.stream(args).anyMatch(i -> i.equalsIgnoreCase("--disable-cache"));
        SpringApplication.run(Lf8ProjectApplication.class, args);
    }
}
