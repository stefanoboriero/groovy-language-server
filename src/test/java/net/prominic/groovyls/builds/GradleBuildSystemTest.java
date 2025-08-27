package net.prominic.groovyls.builds;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GradleBuildSystemTest {
    @TempDir
    Path tempDir;

    @ParameterizedTest
    @ValueSource(strings = {"build.gradle", "build.gradle.kts", "gradlew"})
    void shouldIdentifyGradleProjectAndReturnDependencies(String filename) throws IOException {
        Path gradleFile = tempDir.resolve(filename);
        List<String> lines = Arrays.asList("1", "2", "3");
        Files.write(gradleFile, lines);

        GradleBuildSystemTest.StubbedGradleBuildSystem stubbedGradleBuildSystem = new GradleBuildSystemTest.StubbedGradleBuildSystem();
        Project project = new Project(tempDir, Collections.singletonList(stubbedGradleBuildSystem));

        assertFalse(project.listDependencies().isEmpty());
        assertEquals(1, stubbedGradleBuildSystem.listDependencyCallCount.get());
    }

    @Test
    void shouldNotAttemptToListDependenciesIfNoBuildSystemIsIdentified() {
        GradleBuildSystemTest.StubbedGradleBuildSystem stubbedGradleBuildSystem = new GradleBuildSystemTest.StubbedGradleBuildSystem();
        Project project = new Project(tempDir, Collections.singletonList(stubbedGradleBuildSystem));

        assertTrue(project.listDependencies().isEmpty());
        assertEquals(0, stubbedGradleBuildSystem.listDependencyCallCount.get());
    }

    public static class StubbedGradleBuildSystem extends GradleBuildSystem {
        public static String junitJar = "/home/stefano/.gradle/caches/modules-2/files-2.1/org.junit.jupiter/junit-jupiter-api/5.12.2/6de3a3256c5d90f4a439edcb6c2e8dc5180907b0/junit-jupiter-api-5.12.2.jar";
        public AtomicInteger listDependencyCallCount = new AtomicInteger(0);

        @Override
        public String getName() {
            return "TEST";
        }

        @Override
        public List<String> listDependencies(Path workspaceRoot) {
            listDependencyCallCount.incrementAndGet();
            return Collections.singletonList(junitJar);
        }
    }
}
