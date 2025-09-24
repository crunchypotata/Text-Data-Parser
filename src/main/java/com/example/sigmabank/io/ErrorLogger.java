package com.example.sigmabank.io;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ErrorLogger implements AutoCloseable {
    private final BufferedWriter bw;
    private final Path path;
    public ErrorLogger(Path path) throws IOException {
        this.path = path;
        Files.createDirectories(path.getParent());
        this.bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path.toFile(), false), StandardCharsets.UTF_8));
    }
    public Path getPath(){ return path; }
    public synchronized void log(String rawLine, String reason){
        try {
            bw.write(rawLine);
            bw.write("\t# ");
            bw.write(reason);
            bw.newLine();
        } catch (IOException ignored) {}
    }
    @Override public void close() throws Exception { bw.close(); }
}