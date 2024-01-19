package com.example.moija_project.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.*;

import java.io.Serializable;


@Data
public class TeamId implements Serializable {

    private Long teamId;

    private String userId;

    private Long recruitId;

}
