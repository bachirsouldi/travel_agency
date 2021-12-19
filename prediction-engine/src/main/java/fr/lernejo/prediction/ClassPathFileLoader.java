package fr.lernejo.prediction;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

public class ClassPathFileLoader {

    public Stream<String> readLines(String classPath) {
        InputStream resource = TemperatureService.class.getClassLoader().getResourceAsStream(classPath);
        if (resource == null) {
            throw new IllegalArgumentException("No file named " + classPath + " in classPath");
        }
        try {
            String content = new String(resource.readAllBytes(), StandardCharsets.UTF_8);
            return content.lines();
        } catch (IOException e) {
            // surely dead code
            throw new IllegalStateException("valid resource URL should be readable");
        }
    }
}
