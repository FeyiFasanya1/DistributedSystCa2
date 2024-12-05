package entity;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class Emissions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;
    private String description;
    private double value;
    private String scenario;
    private int year;

}
