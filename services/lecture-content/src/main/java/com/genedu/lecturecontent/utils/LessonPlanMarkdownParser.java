package com.genedu.lecturecontent.utils;

import com.genedu.lecturecontent.model.ActivityPlan;
import com.genedu.lecturecontent.model.InstructionItem;
import com.genedu.lecturecontent.model.LessonPlanTemplate;
import com.vladsch.flexmark.ast.BulletList;
import com.vladsch.flexmark.ast.Heading;
import com.vladsch.flexmark.ast.Paragraph;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;

import java.util.ArrayList;
import java.util.List;

public class LessonPlanMarkdownParser {
    public static LessonPlanTemplate parseMarkdown(String markdown) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        LessonPlanTemplate lessonPlan = new LessonPlanTemplate();
        List<ActivityPlan> activities = new ArrayList<>();
        List<String> objectives = new ArrayList<>();

        ActivityPlan currentActivity = null;
        InstructionItem currentInstruction = null;

        String lastHeaderLevel1 = null;
        String lastHeaderLevel2 = null;

        for (Node node : document.getChildren()) {
            if (node instanceof Heading) {
                Heading heading = (Heading) node;
                String text = heading.getText().toString();
                switch (heading.getLevel()) {
                    case 1 -> lessonPlan.setLessonTitle(text);
                    case 2 -> {
                        if (text.trim().equalsIgnoreCase("MỤC TIÊU")) {
                            lastHeaderLevel2 = "OBJECTIVES";
                        } else {
                            currentActivity = new ActivityPlan();
                            currentActivity.setTitle(cleanActivityTitle(text));
                            currentActivity.setInstructions(new ArrayList<>());
                            activities.add(currentActivity);
                            lastHeaderLevel2 = "ACTIVITY";
                        }
                    }
                    case 3 -> {
                        currentInstruction = new InstructionItem();
                        currentInstruction.setTitle(text);
                        if (currentActivity != null) {
                            currentActivity.getInstructions().add(currentInstruction);
                        }
                    }
                }
            } else if (node instanceof BulletList && "OBJECTIVES".equals(lastHeaderLevel2)) {
                for (Node item : node.getChildren()) {
                    objectives.add(item.getFirstChild().getChars().toString());
                }
            } else if (node instanceof Paragraph && currentInstruction != null) {
                currentInstruction.setInstruction(((Paragraph) node).getContentChars().toString());
            }
        }
        https://refactoring.guru/design-patterns/creational-patterns
        lessonPlan.setObjectives(objectives);
        lessonPlan.setActivities(activities);
        return lessonPlan;
    }

    private static String cleanActivityTitle(String text) {
        if (text.toUpperCase().startsWith("HOẠT ĐỘNG:")) {
            return text.substring("HOẠT ĐỘNG:".length()).trim();
        }
        return text;
    }
}
