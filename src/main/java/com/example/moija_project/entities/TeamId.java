package com.example.moija_project.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;


@Getter
@Setter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TeamId implements Serializable {
    @Column(name = "team_id", nullable = false)
    private Long teamId;
    @Column(name = "user_id", nullable = false)
    private String userId;
    @Column(name = "recruit_id", nullable = false)
    private Long recruitId;

    public TeamId(String leaderId, Long recruitId) {
        this.userId = leaderId;
        this.recruitId = recruitId;
    }
}
