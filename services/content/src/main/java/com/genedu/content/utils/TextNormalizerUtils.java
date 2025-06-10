package com.genedu.content.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class TextNormalizerUtils {
    /**
     * Chuyển đổi text tiếng Việt có dấu thành không dấu sử dụng Normalizer (phương pháp 1)
     *
     * @param text Text cần chuyển đổi
     * @return Text không dấu, viết thường
     */
    public static String normalizeTextUsingNormalizer(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        // Normalize Unicode và loại bỏ dấu
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{M}");
        String result = pattern.matcher(normalized).replaceAll("");

        // Xử lý riêng cho chữ Đ/đ
        result = result.replace("Đ", "D").replace("đ", "d");

        // Chuyển thành chữ thường
        return result.toLowerCase();
    }

    /**
     * Convert Vietnamese text with diacritics to a normalized form for comparison
     *
     * @param text The Vietnamese text to normalize
     * @return Normalized text without diacritics and whitespace
     * */
    public static String normalizeTextForComparison(String text) {
        String normalized = normalizeTextUsingNormalizer(text);
        return normalized.replaceAll("\\s+", "");
    }

    /**
     * Comparing two Vietnamese texts after normalizing them
     * @param text1 First text to compare
     * @param text2 Second text to compare
     * @return true if both texts are equal after normalization, false otherwise
     */
    public static boolean compareVietnameseText(String text1, String text2) {
        if (text1 == null && text2 == null) {
            return true;
        }
        if (text1 == null || text2 == null) {
            return false;
        }

        String normalized1 = normalizeTextForComparison(text1);
        String normalized2 = normalizeTextForComparison(text2);

        return normalized1.equals(normalized2);
    }


}
