package by.k19.model;

import lombok.Data;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "\"purchases\"")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @ElementCollection(fetch = FetchType.EAGER)
    private Map<Product, Integer> products;

    @Column
    private Date time;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "outlet_id")
    private Outlet outlet;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Transient
    public String getTimeString() {
        return new SimpleDateFormat("dd.MM.yyyy hh:mm").format(time);
    }

    @Transient
    public String getProductsString() {
        String result = products.keySet().stream()
                .map(prod -> prod.getName() + " " + products.get(prod))
                .collect(Collectors.joining(", "));
        if (result.length() > 30)
            result = result.substring(0, 30);
        return result;
    }

    @Override
    public String toString() {
        return  "Продукты: " + products +
                ", Время: " + new SimpleDateFormat("dd.MM.yyyy hh:mm").format(time) +
                ", Торговая точка: " + outlet;
    }
}
