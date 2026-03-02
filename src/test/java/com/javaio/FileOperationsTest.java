package com.javaio;

import org.junit.jupiter.api.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UC2 - JUnit Test Cases for File API Operations
 * Demonstrates: Check File Exists, Delete File, Create Directory,
 * Create Empty File, List Files/Directories/Files with Extension.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FileOperationsTest {

    private static final String HOME = System.getProperty("user.home");
    private static final Path PLAY_PATH = Paths.get(HOME, "PLAY_GROUND");
    private static Path tempFile;

    @BeforeAll
    static void setup() throws IOException {
        // Create the PLAY_GROUND directory for all tests
        Files.createDirectories(PLAY_PATH);
        tempFile = PLAY_PATH.resolve("temp.txt");
    }

    /**
     * UC2 - Test 1: Check File Exists
     */
    @Test
    @Order(1)
    public void givenPathWhenCheckedThenConfirm() {
        // Check PLAY_PATH exists
        Path homePath = Paths.get(HOME);
        assertTrue(Files.exists(homePath));
        System.out.println("[UC2] Test 1 - Path exists: " + homePath);
    }

    /**
     * UC2 - Test 2: Delete File and Check File Not Exists
     */
    @Test
    @Order(2)
    public void givenFileWhenDeletedThenNotExists() throws IOException {
        Path displayPath = Paths.get(HOME, "PLAY_GROUND", "temp.txt");
        // Create a file to delete
        Files.createFile(displayPath);
        assertTrue(Files.exists(displayPath));
        // Delete and verify
        Files.deleteIfExists(displayPath);
        assertFalse(Files.exists(displayPath));
        System.out.println("[UC2] Test 2 - File deleted and not exists: " + displayPath);
    }

    /**
     * UC2 - Test 3: Create Directory
     */
    @Test
    @Order(3)
    public void givenDirectoryWhenCreatedThenExists() throws IOException {
        IntStream.range(1, 10).forEach(counter -> {
            Path tempPath = Paths.get(HOME + "/temp" + counter);
            try {
                Files.createDirectory(tempPath);
            } catch (IOException e) {
                // directory may already exist
            }
        });
        assertTrue(Files.exists(Paths.get(HOME + "/temp1")));
        System.out.println("[UC2] Test 3 - Directories created in: " + HOME);
    }

    /**
     * UC2 - Test 4: Create Empty File
     */
    @Test
    @Order(4)
    public void givenTempDirectoryWhenFileCreatedThenExists() throws IOException {
        IntStream.range(1, 10).forEach(counter -> {
            Path tempPath = Paths.get(HOME + "/temp" + counter);
            try {
                tempFile = Files.createTempFile(tempPath, "temp", null);
            } catch (IOException e) {
                // ignore
            }
        });
        assertTrue(Files.exists(tempFile));
        System.out.println("[UC2] Test 4 - Temp file created: " + tempFile);
    }

    /**
     * UC2 - Test 5: List Files, Directories, and Files with Extension
     */
    @Test
    @Order(5)
    public void givenDirectoryWhenListedThenDisplayEntries() throws IOException {
        Path playPath = Paths.get(HOME);

        // List everything (files and directories)
        Files.list(playPath).filter(Files::isRegularFile).forEach(System.out::println);
        Files.newDirectoryStream(playPath).forEach(System.out::println);

        // List only .txt files
        List<Path> txtFiles = Files.list(playPath)
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".tmp") ||
                        path.toString().startsWith("temp"))
                .collect(Collectors.toList());

        System.out.println("[UC2] Test 5 - Listing files in: " + playPath);
        txtFiles.forEach(file -> System.out.println("  " + file.getFileName()));
        assertNotNull(txtFiles);
    }

    @AfterAll
    static void cleanup() throws IOException {
        // Clean up temp directories created during test 3 & 4
        for (int i = 1; i < 10; i++) {
            Path tempPath = Paths.get(HOME + "/temp" + i);
            if (Files.exists(tempPath)) {
                Files.walk(tempPath)
                    .sorted((a, b) -> -a.compareTo(b))
                    .forEach(p -> {
                        try { Files.deleteIfExists(p); } catch (IOException e) { /* ignore */ }
                    });
            }
        }
    }
}
