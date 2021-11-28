package by.k19.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "provider_id")
    private ProductProvider provider;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id && Objects.equals(name, product.name) && Objects.equals(type, product.type) && Objects.equals(purchasePrice, product.purchasePrice) && Objects.equals(sellingPrice, product.sellingPrice) && Objects.equals(manufactureDate, product.manufactureDate) && Objects.equals(manufacturerCountry, product.manufacturerCountry) && Objects.equals(shelfLife, product.shelfLife);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type, purchasePrice, sellingPrice, manufactureDate, manufacturerCountry, shelfLife);
    }

    @Override
    public String toString() {
        return name + " #" + id + " | Поставщик: " + provider.getName() + ", " + provider.getCountry().getName();
    }
}
