package by.k19.model;

import lombok.Data;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Data
@Entity
@Table(name = "\"productRequests\"")
public class ProductRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @ElementCollection(fetch = FetchType.EAGER)
    private Map<Product, Integer> productMap;

    @Column
    private Date startTime;

    @Column
    private Date acceptTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "outlet_id")
    private Outlet outlet;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public ProductRequest(Map<Product, Integer> productMap, Date startTime, Outlet outlet, User user) {
        this.productMap = productMap;
        this.startTime = startTime;
        this.outlet = outlet;
        this.user = user;
    }

    public ProductRequest() {
    }

    @Override
    public String toString() {
        return  "Продукты: " + productMap +
                ", Дата запроса: " + new SimpleDateFormat("dd.MM.yyyy").format(startTime) +
                ", Дата доставки: " + (acceptTime == null ? "Не доставлено" : new SimpleDateFormat("dd.MM.yyyy").format(acceptTime)) +
                ", Торговая точка: " + outlet;
    }

    @Transient
    public String getAcceptTimeString() {
        if (acceptTime == null)
            return "В процессе";
         return new SimpleDateFormat("dd.MM.yyyy").format(acceptTime);
    }

    @Transient
    public String getStartTimeString() {
        if (startTime == null)
            return "NaN";
        return new SimpleDateFormat("dd.MM.yyyy").format(startTime);
    }
}
