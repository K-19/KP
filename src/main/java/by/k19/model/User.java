package by.k19.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

@Data
@Entity
@Table(name = "\"users\"")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "login", nullable = false, length = -1)
    private String login;

    @Column(name = "password", nullable = false, length = -1)
    private Integer password;

    @Column(name = "name", nullable = false, length = -1)
    private String name;

    @Column(name = "surname", nullable = false, length = -1)
    private String surname;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private UserType type;

    @Column
    private Date birthday;

    public String getFio() {
        return name + " " + surname;
    }
}
