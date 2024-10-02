package com.mitocode.springreactor.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mitocode.springreactor.model.Course;
import com.mitocode.springreactor.model.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TuitionDTO {
    private String id;
    private LocalDate registerDate;
    private Student student;
    private List<Course> courses;
    private Boolean status;
}
