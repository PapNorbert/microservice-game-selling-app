package edu.ubb.consolegamesales.backend.model;

import lombok.*;

@Data()
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class GameDisc extends BaseEntity {
    private String name;
    private GameDiscType type;
    private Integer gameYear;
}

