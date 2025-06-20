package com.genedu.commonlibrary.model.listener;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Configurable
public class CustomTimeAuditingEntityListener extends AuditingEntityListener {
    public CustomTimeAuditingEntityListener(ObjectFactory<AuditingHandler> handler) {
        super.setAuditingHandler(handler);
    }
}
