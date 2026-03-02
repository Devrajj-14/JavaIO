package com.javaio;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * UC3 - Java File Watch Service
 * Watches a particular directory along with all Files and Sub Directories.
 * Uses File IO to count the number of entries in the watched directory.
 */
public class FileWatchService {

    private final Path watchDirectory;

    public FileWatchService(Path watchDirectory) {
        this.watchDirectory = watchDirectory;
    }

    /**
     * UC3: Start watching the given directory for CREATE, MODIFY, DELETE events.
     * Also counts entries in the directory using File IO.
     */
    public void startWatching() throws IOException, InterruptedException {
        System.out.println("=== UC3 - Java File Watch Service ===");
        System.out.println("Watching directory: " + watchDirectory.toAbsolutePath());
        System.out.println("Initial entry count: " + countEntries());

        WatchService watchService = FileSystems.getDefault().newWatchService();

        // Register the directory and all subdirectories
        registerAll(watchDirectory, watchService);

        System.out.println("Watch service started. Waiting for events (press Ctrl+C to stop)...\n");

        while (true) {
            WatchKey key = watchService.take(); // blocks until event occurs

            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();

                if (kind == StandardWatchEventKinds.OVERFLOW) {
                    continue;
                }

                @SuppressWarnings("unchecked")
                WatchEvent<Path> pathEvent = (WatchEvent<Path>) event;
                Path context = pathEvent.context();
                Path resolvedPath = ((Path) key.watchable()).resolve(context);

                System.out.println("[WATCH EVENT] " + kind.name() + " -> " + resolvedPath);

                // If a new directory is created, register it too
                if (kind == StandardWatchEventKinds.ENTRY_CREATE && Files.isDirectory(resolvedPath)) {
                    registerAll(resolvedPath, watchService);
                    System.out.println("[REGISTERED NEW DIR] " + resolvedPath);
                }

                System.out.println("Entry count after event: " + countEntries());
            }

            boolean valid = key.reset();
            if (!valid) {
                System.out.println("Watch key no longer valid. Stopping.");
                break;
            }
        }
    }

    /**
     * UC3: Count the number of entries (files + directories) in the watched directory recursively.
     */
    public long countEntries() throws IOException {
        AtomicInteger count = new AtomicInteger(0);
        if (!Files.exists(watchDirectory)) {
            return 0;
        }
        Files.walkFileTree(watchDirectory, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                count.incrementAndGet();
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                if (!dir.equals(watchDirectory)) {
                    count.incrementAndGet();
                }
                return FileVisitResult.CONTINUE;
            }
        });
        return count.get();
    }

    /**
     * Register the given directory and all its subdirectories with the watch service.
     */
    private void registerAll(Path start, WatchService watchService) throws IOException {
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                dir.register(watchService,
                        StandardWatchEventKinds.ENTRY_CREATE,
                        StandardWatchEventKinds.ENTRY_DELETE,
                        StandardWatchEventKinds.ENTRY_MODIFY);
                System.out.println("[REGISTERED] " + dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        String home = System.getProperty("user.home");
        Path watchDir = Paths.get(home, "PLAY_GROUND");
        Files.createDirectories(watchDir);

        FileWatchService service = new FileWatchService(watchDir);
        System.out.println("Number of entries before watch: " + service.countEntries());
        service.startWatching();
    }
}
