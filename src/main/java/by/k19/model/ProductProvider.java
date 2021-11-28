package by.k19.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "\"providers\"")
public class ProductProvider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column
    private String name;

    @Column
    private String address;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "country")
    private Country country;

    @Column
    private Integer deliveryDays;

    @ElementCollection(fetch = FetchType.EAGER)
    private Map<Product, Integer> productMap;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductProvider provider = (ProductProvider) o;
        return id == provider.id && Objects.equals(name, provider.name) && Objects.equals(address, provider.address) && Objects.equals(country, provider.country) && Objects.equals(deliveryDays, provider.deliveryDays) && Objects.equals(productMap, provider.productMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address, country, deliveryDays, productMap);
    }

    @Override
    public String toString() {
        return "ProductProvider{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", country=" + country +
                ", deliveryDays=" + deliveryDays +
                ", productMap=" + productMap.keySet().stream().map(product -> product.getName() + " : " + productMap.get(product).toString()).collect(Collectors.joining("|")) +
                '}';
    }
}
