package by.k19.model;

import lombok.Data;

import javax.persistence.*;
import java.text.SimpleDateFormat;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "outlet")
    private Outlet outlet;

    public String getFio() {
        return name + " " + surname;
    }

    public String getShortName() {
        return surname + " " + name.substring(0, 0).toUpperCase() + ". ";
    }

    public String getOutletName() {
        if (outlet == null) {
            return "-/-/-";
        }
        return outlet.toString();
    }

    @Override
    public String toString() {
        return getFio() +
                ", логин: " + login +
                ", пароль: " + password +
                ", тип: " + type.getName() +
                ", дата рождения:" + new SimpleDateFormat("dd.MM.yyyy").format(birthday) +
                (outlet != null ? ", торговая точка: " + outlet : "");
    }
}
