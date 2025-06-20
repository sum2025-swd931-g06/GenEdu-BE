package com.genedu.media.repository;

import com.genedu.media.model.MediaFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaFileRepository extends JpaRepository<MediaFile, Long> {
}
