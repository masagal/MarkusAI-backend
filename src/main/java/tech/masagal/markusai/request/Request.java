package tech.masagal.markusai.request;

import jakarta.persistence.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.masagal.markusai.user.User;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Entity
@Table(name = "requests")
public class Request {
    @Transient
    Logger logger = LogManager.getLogger();

    public boolean isTheSameAs(Request otherRequest) {
        // Gee whiz Batman, major Hackington!
        logger.info("evaluating sameyness");
        if(this.getProducts().size() != otherRequest.getProducts().size()) {
            return false;
        }
        logger.info("it was not the size: {} vs {}", this.getProducts().size(), otherRequest.getProducts().size());
        logger.info("it was not the 0th product: {} vs {}", this.getProducts().get(0).getId(), otherRequest.getProducts().get(0).getId());

        if(this.getProducts().get(0).getId().longValue() != otherRequest.getProducts().get(0).getId().longValue()) return false;
        logger.info("it was not the 0th product: {} vs {}", this.getProducts().get(0).getId(), otherRequest.getProducts().get(0).getId());

        if(this.getProducts().get(0).getQuantity() != otherRequest.getProducts().get(0).getQuantity()) return false;

        logger.info("it was very samey! returning true");

        return true;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<RequestProduct> products;

    private boolean isApproved;

    public Request() {
        this.products = new ArrayList<>();
        this.isApproved = false;
    }

    public Request(User user) {
        this();
        this.user = user;
    }

    public void setUserTest(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public List<RequestProduct> getProducts() {
        return products;
    }

    public void setProducts(List<RequestProduct> products) {
        this.products = products;
    }

    public Long getId() {
        return id;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public boolean isApproved() {
        return isApproved;
    }
}
