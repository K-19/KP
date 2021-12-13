package by.k19.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "\"sales\"")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "outlet_id")
    private Outlet outlet;

    @Column
    private int percent;

    public String getPercentString() {
        return percent + "%";
    }

    @Override
    public String toString() {
        return  "Продукт:" + product +
                ", Торговая точка: " + outlet +
                ", Процент: " + percent + "%";
    }
}
