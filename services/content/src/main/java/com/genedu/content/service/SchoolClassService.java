package com.genedu.content.service;

import com.genedu.content.dto.SchoolClassResponseDTO;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;

public interface SchoolClassService {
    List<SchoolClassResponseDTO> getAllSchoolClasses(int pageNo);
}
