package com.genedu.lecturecontent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;

public record Slide(
        String type,
        String title,
        /**
         * The main data payload for the slide. Its concrete type is determined
         * by the top-level 'type' field, using Jackson's external property mapping.
         */
        @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "type")
        @JsonSubTypes({
                @JsonSubTypes.Type(value = Slide.WelcomeSlideData.class, name = "welcome"),
                @JsonSubTypes.Type(value = Slide.ContentSlideData.class, name = "content"),
                @JsonSubTypes.Type(value = Slide.ListSlideData.class, name = "list"),
                @JsonSubTypes.Type(value = Slide.CompareSlideData.class, name = "compare"),
                @JsonSubTypes.Type(value = Slide.ThanksSlideData.class, name = "thanks")
        })
        SlideData data,
        String narrationScript
) {
    public sealed interface SlideData permits WelcomeSlideData, ContentSlideData, ListSlideData, CompareSlideData, ThanksSlideData {
    }

    public record WelcomeSlideData(String subtitle) implements SlideData {}
    public record ContentSlideData(String body) implements SlideData {}
    public record ListSlideData(List<String> items) implements SlideData {}
    public record CompareSlideData(
            // BEST PRACTICE: Use camelCase for Java fields for internal consistency.
            // The @JsonProperty annotation maps the Java field to the snake_case JSON property,
            // ensuring the external API contract for the frontend remains unchanged.
            @JsonProperty("left_header") String leftHeader,
            @JsonProperty("left_points") List<String> leftPoints,
            @JsonProperty("right_header") String rightHeader,
            @JsonProperty("right_points") List<String> rightPoints
    ) implements SlideData {}
    public record ThanksSlideData(String message) implements SlideData {}
}
