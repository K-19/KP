package by.k19.model;

import lombok.Data;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Entity
@Table(name = "\"actions\"")
public class UserAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String descriptionAction;

    @Column
    private Date time;

    public UserAction(User user, String descriptionAction) {
        this.user = user;
        this.descriptionAction = descriptionAction;
        this.time = new Date();
    }

    public UserAction() {}

    public String getDescription(int word) {
        switch(word) {
            case 1: return user.getFio() + ", ID: " + user.getId();
            case 2: return new SimpleDateFormat("dd.MM.yyyy hh:mm").format(time);
            case 3: return descriptionAction;
            default: return "";
        }
    }
}
