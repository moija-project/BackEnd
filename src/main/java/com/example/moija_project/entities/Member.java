package com.example.moija_project.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@Table(name = "member")
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    @EmbeddedId
    private TeamId teamId;

    @Column(name = "score_team")
    private Float scoreTeam;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "recruit_id", referencedColumnName = "recruit_id", insertable = false, updatable = false)
    private Recruit recruit;

}
