package com.example.moija_project.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "conditions")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Condition {
    @Id
    private String conditionId;
    private Long recruitId; // ObjectId("recruit_id_1")
    private String question;
    private String answer;
}