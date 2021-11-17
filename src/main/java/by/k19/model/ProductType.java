package by.k19.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "\"producttypes\"")
public class ProductType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column
    private String name;

    @Override
    public String toString() {
        return name;
    }
}
