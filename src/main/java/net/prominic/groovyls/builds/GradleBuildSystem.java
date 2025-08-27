package net.prominic.groovyls.builds;

import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;
import org.gradle.tooling.model.DomainObjectSet;
import org.gradle.tooling.model.eclipse.EclipseExternalDependency;
import org.gradle.tooling.model.eclipse.EclipseProject;

import java.io.File;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handle operations for Gradle builds
 * @author stefano.boriero@gmail.com
 */
class GradleBuildSystem implements BuildSystem {
    @Override
    public String getName() {
        return "GRADLE";
    }

    @Override
    public boolean isSupported(Path root) {
        File[] files = root.toFile().listFiles(this::isGradleFileMarker);
        if (files == null) {
            return false;
        }

        return files.length > 0;
    }

    private boolean isGradleFileMarker(File file) {
        return file.getName().equals("build.gradle") || file.getName().equals("build.gradle.kts") || file.getName().equals("gradlew");
    }

    @Override
    public List<String> listDependencies(Path workspaceRoot) {
        GradleConnector connector = GradleConnector.newConnector()
                .forProjectDirectory(workspaceRoot.toFile());
        try (ProjectConnection connection = connector.connect()) {
            EclipseProject build = connection.model(EclipseProject.class).get();
            DomainObjectSet<? extends EclipseExternalDependency> classpath = build.getClasspath();
            return classpath.stream()
                    .map(dependency -> dependency.getFile().getAbsolutePath())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return Collections.emptyList();
    }
}
