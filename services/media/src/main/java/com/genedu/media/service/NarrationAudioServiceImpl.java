package com.genedu.media.service;

import com.genedu.commonlibrary.enumeration.FileType;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class NarrationAudioServiceImpl implements MediaFileService{

    @Override
    public void checkValidFormat(Object mediaFile) {

    }

    @Override
    public Object saveMediaFile(Object mediaFile) throws IOException {
        return null;
    }

    @Override
    public Object systematicSaveMediaFile(Object mediaFile) throws IOException {
        return null;
    }

    @Override
    public Object getMediaFileByFileNameAndFileType(String fileName, FileType fileType) {
        return null;
    }

    @Override
    public String getMediaFileUrlById(Long id) {
        return "";
    }

    @Override
    public void deleteMediaFile(Long id) {

    }

    @Override
    public Object updateMediaFile(Long id, Object mediaFile) {
        return null;
    }

    @Override
    public List getAllMediaFilesByOwnerId(UUID ownerId) {
        return List.of();
    }

    @Override
    public List getMediaFilesByType(FileType type) {
        return List.of();
    }

    @Override
    public List getMediaFilesByOwnerIdAndType(UUID ownerId, FileType type) {
        return List.of();
    }

    @Override
    public Object readFileContent(Long id) {
        return null;
    }

    @Override
    public Object getMediaFileByProjectId(String projectId) {
        return null;
    }
}
