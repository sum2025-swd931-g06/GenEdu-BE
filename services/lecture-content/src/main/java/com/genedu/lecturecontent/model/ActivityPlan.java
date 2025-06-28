package com.genedu.lecturecontent.model;
import lombok.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class ActivityPlan {
    private String title;
    private List<InstructionItem> instructions;
}
