package com.genedu.media.model;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;


public class ClasspathMultipartFile implements MultipartFile {
    private final Resource resource;
    private final String filename;

    public ClasspathMultipartFile(Resource resource, String filename) {
        this.resource = resource;
        this.filename = filename;
    }

    @Override
    public String getName() {
        return "file";
    }

    @Override
    public String getOriginalFilename() {
        return this.filename;
    }

    @Override
    public String getContentType() {
        return new org.springframework.http.MediaType("text", "markdown", StandardCharsets.UTF_8).toString();
    }

    @Override
    public boolean isEmpty() {
        try {
            return this.resource.contentLength() == 0;
        } catch (IOException e) {
            return true;
        }
    }

    @Override
    public long getSize() {
        try {
            return this.resource.contentLength();
        } catch (IOException e) {
            return 0;
        }
    }

    @Override
    public byte[] getBytes() throws IOException {
        return this.resource.getInputStream().readAllBytes();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return this.resource.getInputStream();
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        try (InputStream in = getInputStream()) {
            Files.copy(in, dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }
}