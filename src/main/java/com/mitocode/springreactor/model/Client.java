package com.mitocode.springreactor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "clients")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Client {
    @Id
    @EqualsAndHashCode.Include
    private String id;
    @Field
    private String firsName;
    @Field
    private String lastName;
    @Field
    private LocalDate birthDate;
    @Field
    private String urlPhoto;
}
