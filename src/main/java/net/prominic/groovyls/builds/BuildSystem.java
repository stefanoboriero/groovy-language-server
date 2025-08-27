package net.prominic.groovyls.builds;

import java.nio.file.Path;
import java.util.List;

public interface BuildSystem {
    boolean isSupported(Path workspaceRoot);
    String getName();
    List<String> listDependencies(Path workspaceRoot);
}
