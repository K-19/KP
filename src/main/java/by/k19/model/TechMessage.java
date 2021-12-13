package by.k19.model;

import lombok.Data;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Entity
@Table(name = "techmessages")
public class TechMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private Date questionDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String question;

    @Column
    private Date answerDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "admin_id")
    private User admin;

    @Column
    private String answer;

    public String getIdString() {
        return "Вопрос №" + id;
    }

    public String getUserString() {
        return getIdString() + " от " + (user.getType() == UserType.DIRECTOR ? " директора " : " менеджера " + user.getOutlet().getName() + " ") + user.getFio();
    }

    public String getQuestionDateString() {
        return new SimpleDateFormat("dd.MM.yyyy hh:mm").format(questionDate);
    }

    public String getAnswerDateString() {
        return answer != null ? new SimpleDateFormat("dd.MM.yyyy hh:mm").format(answerDate) : "Ждём ответа администратора...";
    }

    public String getAdminString() {
        return admin != null ? "Ответ от администратора " + admin.getFio() : "";
    }

    public String getFullQuestion() {
        return "Вопрос №" + id + "\n" + new SimpleDateFormat("dd.MM.yyyy hh:mm").format(questionDate) + "\n" + question;
    }

    public String getFullAnswer() {
        if (answer != null)
            return "Ответ от администратора " + admin.getFio() + "\n" + new SimpleDateFormat("dd.MM.yyyy hh:mm").format(answerDate) + "\n" + answer;
        else return "Ждём ответа администратора...";
    }
}
