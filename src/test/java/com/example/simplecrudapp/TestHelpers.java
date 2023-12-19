package com.example.simplecrudapp;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@UtilityClass
public class TestHelpers {

    static final Path resourcesPath;

    static {
        resourcesPath = Paths.get("src/test/resources/");
    }

    @SneakyThrows
    public static String readFileAsString(String uri) {;
        var path = resourcesPath.resolve(uri);
        System.out.println(path.toAbsolutePath());
        return Files.readString(path);
    }
}
