package com.hingecloud.apppubs.pub.utils;

import org.gradle.tooling.BuildLauncher;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;

import java.io.File;

public class GradleUtils {

    public static boolean buildProject(String buildPath, String... tasks) throws RuntimeException {
        File buildFile = new File(buildPath);
        GradleConnector connector = GradleConnector.newConnector();
        connector.forProjectDirectory(buildFile);
        ProjectConnection connection = connector.connect();
        BuildLauncher build = connection.newBuild();
        build.forTasks(tasks);
        build.setStandardOutput(System.out);
        try {
            build.run();
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            connection.close();
        }
        return true;
    }
}
