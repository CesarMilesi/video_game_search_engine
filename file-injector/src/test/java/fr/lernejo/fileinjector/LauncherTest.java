package fr.lernejo.fileinjector;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class LauncherTest {

    @Test
    void main_terminates_before_15_sec() {
        assertTimeoutPreemptively(
            Duration.ofSeconds(15L),
            () -> Launcher.main(new String[]{"src/test/resources/games.json"}));
    }
    @Test
    void main_failed_test() {
        assertThrows(Exception.class,
            () -> Launcher.main(new String[]{}));
    }
}
