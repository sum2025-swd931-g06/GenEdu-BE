package com.genedu.commonlibrary.model;

import com.genedu.commonlibrary.model.listener.CustomAuditingEntityListener;
import com.genedu.commonlibrary.model.listener.CustomTimeAuditingEntityListener;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.ZonedDateTime;

@MappedSuperclass
@Getter
@Setter
@EntityListeners(CustomTimeAuditingEntityListener.class)
public class AbstractTimeAuditEntity {
    @CreationTimestamp
    private ZonedDateTime createdOn;

    @UpdateTimestamp
    private ZonedDateTime lastModifiedOn;

    private boolean isDeleted = false;
}
