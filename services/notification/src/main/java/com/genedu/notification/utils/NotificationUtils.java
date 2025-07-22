package com.genedu.notification.utils;

import com.genedu.commonlibrary.utils.AuthenticationUtils;

public class NotificationUtils {
    public static String getCurrentUserId() {
        return AuthenticationUtils.extractUserId();
    }
}
