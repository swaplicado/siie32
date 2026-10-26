/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.swap.utils;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * File management utilities.
 * @author Sergio Flores
 */
public abstract class SFileUtils {
    
    /**
     * Delete obsolete directories.
     * WARNING: All subdirectories in the base directory that match the globbing pattern will be recursively and permanently deleted!
     * @param baseDirectory Base directory.
     * @param globPattern Globbing matching pattern.
     * @throws java.io.IOException 
     */
    public static void deleteObsoleteDirectories(final String baseDirectory, final String globPattern) throws IOException {
        final Path pathToBase = Paths.get(baseDirectory);
        //PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:" + globPattern);
        
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(pathToBase, globPattern)) {
            for (Path dir : stream) {
                if (!Files.isDirectory(dir)) {
                    continue;
                }
                
                BasicFileAttributes attrs = Files.readAttributes(dir, BasicFileAttributes.class);
                LocalDate creationDate = attrs.creationTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                if (creationDate.isBefore(LocalDate.now())) {
                    deleteRecursively(dir);
                    System.out.println("Delete: " + dir);
                }
            }
        }
    }
    
    private static void deleteRecursively(Path root) throws IOException {
        Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
            
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }
            
            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
