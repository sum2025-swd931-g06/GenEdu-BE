package com.genedu.project.controller;

import com.genedu.project.dto.StreamingSlideResponse;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
public class SlideStreamingController {
    @GetMapping(
            value = "/stream/slide-content",
            produces = "text/event-stream;charset=UTF-8"
    )
    public Flux<ServerSentEvent<StreamingSlideResponse>> streamSlides() {
        record Slide(int slideId, String slideType, String content) {
        }

        List<Slide> slides = List.of(
                new Slide(1,
                        "Open Slide",
                        """ 
                                    # Hello
                                    ## Hello this is my slide
                                """),
                new Slide(2,
                        "Topic Slide",
                        """ 
                                    # Topic Slide
                                    ## Topic 1 What Spring AI?
                                """),
                new Slide(3,
                        "Content Slide",
                        """ 
                                    # Content Slide
                                    ## Content 1 What is Spring AI?
                                    - Spring AI is a framework for building AI applications.
                                    - It provides tools for chat, document processing, and more.
                                    ## Content 2 How does it work?
                                    - It integrates with various AI models and services.
                                    - It allows developers to create applications that can understand and generate text.
                                    ## Content 3 Why use Spring AI?
                                    - It simplifies the development of AI applications.
                                    - It provides a consistent API for working with different AI models.
                                    - It supports reactive programming for better performance.
                                """),
                new Slide(4,
                        "Content Slide",
                        """ 
                                    # Content Slide
                                    ## Content 1 What is Spring AI?
                                    - Spring AI is a framework for building AI applications.
                                    - It provides tools for chat, document processing, and more.
                                    ## Content 2 How does it work?
                                    - It integrates with various AI models and services.
                                    - It allows developers to create applications that can understand and generate text.
                                    ## Content 3 Why use Spring AI?
                                    - It simplifies the development of AI applications.
                                    - It provides a consistent API for working with different AI models.
                                    - It supports reactive programming for better performance.
                                """),
                new Slide(5,
                        "Conclusion Slide",
                        """ 
                                    # Conclusion Slide
                                    ## Key Takeaways
                                    - Spring AI is a powerful framework for AI applications.
                                    - It simplifies the development process.
                                    - It supports reactive programming for better performance.
                                """),
                new Slide(6,
                        "Closing Slide",
                        """ 
                                    # Closing Slide
                                    ## Thank you for your attention!
                                    - Questions?
                                    - Feedback?
                                """)
        );

        return Flux.fromIterable(slides)
                .concatMap(slide -> {
                    String[] words = slide.content().split(" ");

                    return Flux.range(0, words.length)
                            .delayElements(Duration.ofMillis(500)) // word-by-word delay
                            .map(i -> ServerSentEvent.<StreamingSlideResponse>builder()
                                    .id(slide.slideId() + "-" + (i + 1))
                                    .event("slide-content")
                                    .data(new StreamingSlideResponse(
                                            slide.slideId(),
                                            slide.slideType(),
                                            words[i]
                                    ))
                                    .build()
                            )
                            .concatWith(
                                    Mono.delay(Duration.ofSeconds(1)).then(Mono.empty())
                            );
                });
    }
}
