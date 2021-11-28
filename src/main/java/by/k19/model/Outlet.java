package by.k19.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Data
@Entity
@Table(name = "\"outlets\"")
public class Outlet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "name", nullable = false, length = -1)
    private String name;

    @Column(name = "address", nullable = false, length = -1)
    private String address;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "country")
    private Country country;

    @OneToMany(mappedBy = "outlet", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<User> userList;

    @ElementCollection(fetch = FetchType.EAGER)
    private Map<Product, Integer> currentAssortment;

    @ElementCollection(fetch = FetchType.EAGER)
    private Map<Product, Integer> standardAssortment;

    @ElementCollection(fetch = FetchType.EAGER)
    private Map<Product, Integer> currentStorage;

    @ElementCollection(fetch = FetchType.EAGER)
    private Map<Product, Integer> standardStorage;

    @Override
    public String toString() {
        return name + ", " + address + ", " + country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Outlet outlet = (Outlet) o;
        return Objects.equals(name, outlet.name) && Objects.equals(address, outlet.address) && Objects.equals(country, outlet.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address, country);
    }
}
