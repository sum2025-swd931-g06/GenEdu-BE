package com.genedu.media.repository;

import com.genedu.commonlibrary.enumeration.FileType;
import com.genedu.media.model.MediaFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MediaFileRepository extends JpaRepository<MediaFile, Long> {
    Optional<MediaFile> findFirstByFileUrlAndDeletedIsFalseOrderByUploadedOnDesc(String fileUrl);
    Optional<MediaFile> findFirstByFileNameAndFileTypeAndDeletedIsFalseOrderByUploadedOnDesc(String fileName, FileType fileType);

    /**
     * Finds the most recent, non-deleted file for a given project ID and file type.
     * This is the cleanest and safest way to get a single, latest result.
     *
     * - 'findFirstBy' is the equivalent of 'SELECT TOP 1'.
     * - 'FileUrlStartingWith' handles the 'LIKE ...%' logic automatically.
     * - 'OrderBy...Desc' ensures the "top" record is the most recent one.
     *
     * @param fileUrlPrefix The prefix of the S3 file URL (e.g., "projects/some-project-id/").
     * @param fileType The type of the file to search for.
     * @return An Optional containing the latest MediaFile if found.
     */
    Optional<MediaFile> findFirstByFileUrlStartingWithAndFileTypeAndDeletedIsFalseOrderByUploadedOnDesc(String fileUrlPrefix, FileType fileType);

}
