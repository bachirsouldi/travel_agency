package fr.lernejo.prediction;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ClassPathFileLoaderTest {

    @Test
    void loading_from_non_existing_file_throws() {
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> new ClassPathFileLoader().readLines("not-existing.txt"))
            .withMessage("No file named not-existing.txt in classPath");
    }

    @Test
    void loading_first_manifest() {
        Stream<String> lines = new ClassPathFileLoader().readLines("sample.txt");

        assertThat(lines).containsExactly("first line", "second line");
    }
}
