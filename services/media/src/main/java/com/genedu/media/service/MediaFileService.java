package com.genedu.media.service;

import com.genedu.commonlibrary.enumeration.FileType;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface MediaFileService<Request, Response> {
    Response saveMediaFile(Request mediaFile) throws IOException;
    String getMediaFileUrlById(Long id);
    void deleteMediaFile(Long id);
    Response updateMediaFile(Long id, Request mediaFile);
    List<Response> getAllMediaFilesByOwnerId(UUID ownerId);
    List<Response> getMediaFilesByType(FileType type);
    List<Response> getMediaFilesByOwnerIdAndType(UUID ownerId, FileType type);
}
