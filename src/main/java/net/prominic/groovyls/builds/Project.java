package net.prominic.groovyls.builds;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Class representing a project, exposing operations to manage the project lifecycle
 *
 * @author stefano.boriero@gmail.com
 */
public class Project {
    private final Path workspaceRoot;
    private final List<BuildSystem> supportedBuildSystems;

    public Project(Path workspaceRoot) {
        this(workspaceRoot, Arrays.asList(new GradleBuildSystem()));
    }

    protected Project(Path workspaceRoot, List<BuildSystem> supportedBuildSystems) {
        this.workspaceRoot = workspaceRoot;
        this.supportedBuildSystems = supportedBuildSystems;
    }

    /**
     * Returns the absolute paths for the external dependencies jars for the project.
     * Delegates the work to list the to the build system used. If the build system
     * cannot be inferred or is not supported, returns an empty list.
     *
     * @return the list of file paths
     */
    public List<String> listDependencies() {
        return supportedBuildSystems.stream()
                .filter(buildSystem -> buildSystem.isSupported(workspaceRoot))
                .findAny()
                .map(buildSystem -> buildSystem.listDependencies(workspaceRoot))
                .orElse(Collections.emptyList());
    }

}
