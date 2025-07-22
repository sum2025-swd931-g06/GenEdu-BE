package com.genedu.commonlibrary.webclient;

import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.lang.Nullable;
import org.springframework.lang.NonNull;
import java.io.*;

public class CustomMultipartFile implements MultipartFile {
    private final String name;
    private final String originalFilename;
    @Nullable
    private final String contentType;
    private final byte[] content;

    /**
     * Creates a new {@code CustomMultipartFile} with the given file data.
     *
     * @param name             The name of the parameter in the multipart form (e.g., "file"). Must not be empty.
     * @param originalFilename The original filename to be associated with the file (e.g., "narration.mp3"). Must not be empty.
     * @param contentType      The content type of the file (e.g., "audio/mpeg"), which can be null.
     * @param content          The raw byte content of the file. Must not be null.
     */
    public CustomMultipartFile(String name, String originalFilename, @Nullable String contentType, byte[] content) {
        // BEST PRACTICE: Validate inputs in the constructor to guarantee a valid object state.
        Assert.hasLength(name, "Name must not be null or empty");
        Assert.hasLength(originalFilename, "OriginalFilename must not be null or empty");
        Assert.notNull(content, "Content must not be null");

        this.name = name;
        this.originalFilename = originalFilename;
        this.contentType = contentType;
        this.content = content;
    }

    @Override
    @NonNull
    public String getName() {
        return this.name;
    }

    @Override
    @NonNull
    public String getOriginalFilename() {
        return this.originalFilename;
    }

    @Override
    @Nullable
    public String getContentType() {
        return this.contentType;
    }

    @Override
    public boolean isEmpty() {
        // This is now safe because the constructor ensures 'content' is never null.
        return this.content.length == 0;
    }

    @Override
    public long getSize() {
        // This is now safe.
        return this.content.length;
    }

    @Override
    @NonNull
    public byte[] getBytes() {
        return this.content;
    }

    @Override
    @NonNull
    public InputStream getInputStream() {
        return new ByteArrayInputStream(this.content);
    }

    @Override
    public void transferTo(@NonNull File destination) throws IOException, IllegalStateException {
        Assert.notNull(destination, "Destination file cannot be null");
        // Using try-with-resources is a clean and safe way to handle file streams.
        try (FileOutputStream fos = new FileOutputStream(destination)) {
            fos.write(this.content);
        }
    }
}
