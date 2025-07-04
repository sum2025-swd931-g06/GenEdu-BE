package com.genedu.lecturecontent.model;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class InstructionItem {
    private String title;
    private String instruction;
}
