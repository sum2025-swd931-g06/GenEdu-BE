package com.genedu.lecturecontent.dto;

import java.util.List;

public record Activity(String name, List<Instruction> instructions) {}
