package net.prominic.groovyls.builds;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class ProjectTest {

    @Test
    void shouldIdentifyGradleProjectAndReturnDependencies() {

        StubbedSupportedBuildSystem stubbedSupportedBuildSystem = new StubbedSupportedBuildSystem();
        Project project = new Project(null, Collections.singletonList(stubbedSupportedBuildSystem));

        assertFalse(project.listDependencies().isEmpty());
        assertEquals(1, stubbedSupportedBuildSystem.listDependencyCallCount.get());
    }

    @Test
    void shouldNotAttemptToListDependenciesIfNoBuildSystemIsIdentified() {
        StubbedUnsuportedBuildSystem stubbedUnsuportedBuildSystem = new StubbedUnsuportedBuildSystem();
        Project project = new Project(null, Collections.singletonList(stubbedUnsuportedBuildSystem));

        assertTrue(project.listDependencies().isEmpty());
        assertEquals(0, stubbedUnsuportedBuildSystem.listDependencyCallCount.get());
    }

    public static class StubbedUnsuportedBuildSystem implements BuildSystem {
        public static String junitJar = "/home/stefano/.gradle/caches/modules-2/files-2.1/org.junit.jupiter/junit-jupiter-api/5.12.2/6de3a3256c5d90f4a439edcb6c2e8dc5180907b0/junit-jupiter-api-5.12.2.jar";
        public AtomicInteger listDependencyCallCount = new AtomicInteger(0);

        @Override
        public String getName() {
            return "TEST_UNSUPPORTED";
        }

        @Override
        public boolean isSupported(Path workspaceRoot) {
            return false;
        }

        @Override
        public List<String> listDependencies(Path workspaceRoot) {
            listDependencyCallCount.incrementAndGet();
            return Collections.singletonList(junitJar);
        }
    }

    public static class StubbedSupportedBuildSystem implements BuildSystem {
        public static String junitJar = "/home/stefano/.gradle/caches/modules-2/files-2.1/org.junit.jupiter/junit-jupiter-api/5.12.2/6de3a3256c5d90f4a439edcb6c2e8dc5180907b0/junit-jupiter-api-5.12.2.jar";
        public AtomicInteger listDependencyCallCount = new AtomicInteger(0);

        @Override
        public String getName() {
            return "TEST_SUPPORTED";
        }

        @Override
        public boolean isSupported(Path workspaceRoot) {
            return true;
        }

        @Override
        public List<String> listDependencies(Path workspaceRoot) {
            listDependencyCallCount.incrementAndGet();
            return Collections.singletonList(junitJar);
        }
    }
}
