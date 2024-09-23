package com.mitocode.springreactor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "tuitions")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Tuition {
    @Id
    @EqualsAndHashCode.Include
    private String id;
    @Field
    private LocalDate registerDate;
    @Field
    private Student student;
    @Field
    private List<Course> courses;
    @Field
    private Boolean status;


}
