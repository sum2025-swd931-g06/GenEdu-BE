package com.genedu.media.runner;

import com.genedu.commonlibrary.webclient.dto.LessonPlanFileDownloadDTO;
import com.genedu.commonlibrary.webclient.dto.LessonPlanFileUploadDTO;
import com.genedu.media.model.ClasspathMultipartFile;
import com.genedu.media.service.MediaFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    // Inject the specific MediaFileService bean
    private final MediaFileService<LessonPlanFileUploadDTO, LessonPlanFileDownloadDTO> mediaFileService;

    private static final String TEMPLATE_FILENAME = "lesson-plan-template.md";
    private static final String TEMPLATE_CLASSPATH = "templates/" + TEMPLATE_FILENAME;
    private static final String TEMPLATE_PROJECT_ID = "TemplateProjectId";

    @Override
    public void run(String... args) throws Exception {
        try {
            log.info("Checking for default lesson plan template...");
            Resource resource = new ClassPathResource(TEMPLATE_CLASSPATH);

            if (!resource.exists()) {
                log.error("Default template file not found at: {}", TEMPLATE_CLASSPATH);
                return;
            }

            MultipartFile multipartFile = new ClasspathMultipartFile(resource, TEMPLATE_FILENAME);

            log.info("Uploading default lesson plan template...");

            // Create the DTO required by the service
            LessonPlanFileUploadDTO uploadDTO = new LessonPlanFileUploadDTO();
            uploadDTO.setMediaFile(multipartFile);
            uploadDTO.setProjectId(TEMPLATE_PROJECT_ID);

            // Call the existing service to save the file
            mediaFileService.systematicSaveMediaFile(uploadDTO);

            log.info("Successfully uploaded default lesson plan template.");

        } catch (Exception e) {
            log.error("Failed to initialize and upload default lesson plan template.", e);
        }
    }
}
