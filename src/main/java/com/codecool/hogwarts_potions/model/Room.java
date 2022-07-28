package com.codecool.hogwarts_potions.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer capacity;

    @OneToMany(mappedBy = "room", targetEntity = Student.class)
    private List<Student> residents = new ArrayList<>();

}
