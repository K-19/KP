package by.k19.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "\"products\"")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type")
    private ProductType type;

    @Column
    private Double purchasePrice;

    @Column
    private Double sellingPrice;

    @Column
    private Date manufactureDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manufacturercountry")
    private Country manufacturerCountry;

    @Column
    private Date shelfLife;

    @Column
    private Integer amount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "provider_id")
    private ProductProvider provider;

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", purchasePrice=" + purchasePrice +
                ", sellingPrice=" + sellingPrice +
                ", manufactureDate=" + manufactureDate +
                ", manufacturerCountry=" + manufacturerCountry +
                ", shelfLife=" + shelfLife +
                ", amount=" + amount +
                ", provider=" + provider.getName() + ", " + provider.getCountry().getName() +
                '}';
    }
}
