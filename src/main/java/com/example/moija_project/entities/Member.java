package com.example.moija_project.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Builder
@Table(name = "member")
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@IdClass(TeamId.class)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id", nullable = false)
    private Long teamId;

    @Id
    @Column(name = "user_id", nullable = false)
    private String userId;

    @Id
    @Column(name = "recruit_id", nullable = false)
    private Long recruitId;

    @Column(name = "score_team")
    private Float scoreTeam;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "recruit_id", referencedColumnName = "recruit_id", insertable = false, updatable = false)
    private Recruit recruit;

}

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
class TeamId implements Serializable {
    private Long teamId;
    private String userId;
    private Long recruitId;
}