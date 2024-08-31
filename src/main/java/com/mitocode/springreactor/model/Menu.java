package com.mitocode.springreactor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "menus")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Menu {
    @Id
    @EqualsAndHashCode.Include
    private String id;

    @Field
    private String icon;

    @Field
    private String name;

    @Field
    private String uri;

    @Field
    private List<String> roles;
}
