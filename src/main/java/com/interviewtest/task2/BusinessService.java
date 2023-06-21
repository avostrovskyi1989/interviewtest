package com.interviewtest.task2;

import lombok.NonNull;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

public class BusinessService {

    public long calculate(@NonNull final String pathToDirectory) {
        if (pathToDirectory.isBlank()) {
            throw new IllegalArgumentException("Empty path");
        }
        final Optional<File> lastCreatedTxtFile = getLastCreatedTxtFile(pathToDirectory);
        if (lastCreatedTxtFile.isEmpty()) {
            return 0L;
        }
        final String fileContent = readFile(lastCreatedTxtFile.get());
        return countLines(fileContent);
    }

    @SneakyThrows(IOException.class)
    private Optional<File> getLastCreatedTxtFile(@NonNull final String pathToDirectory) {
        try (Stream<Path> paths = Files.walk(Paths.get(pathToDirectory))) {
            return paths
                    .filter(Files::isRegularFile)
                    .filter(f -> f.toFile().getName().endsWith(".txt"))
                    .max(TIME_COMPARATOR)
                    .map(Path::toFile);
        }
    }

    @SneakyThrows(IOException.class)
    private String readFile(@NonNull final File file) {
        return new String(Files.readAllBytes(file.toPath()));
    }

    private long countLines(@NonNull final String content) {
        if (content.isBlank()) {
            return 0L;
        }
        return content.split("\r\n|\r|\n").length;
    }

    private static final Comparator<Path> TIME_COMPARATOR =
            (f1, f2) -> {
                try {
                    BasicFileAttributes attr1 = Files.readAttributes(f1, BasicFileAttributes.class);
                    BasicFileAttributes attr2 = Files.readAttributes(f2, BasicFileAttributes.class);
                    return attr1.creationTime().compareTo(attr2.creationTime());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            };
}
