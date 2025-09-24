package com.example.sigmabank.io;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public final class FileScanner {
    public static List<Path> findSbFiles(Path dir){
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(dir, "*.sb")){
            List<Path> res = new ArrayList<>();
            for (Path p : ds) res.add(p);
            res.sort(Comparator.comparing(Path::getFileName));
            return res;
        } catch (IOException e){
            return Collections.emptyList();
        }
    }
}