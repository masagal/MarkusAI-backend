package tech.masagal.markusai.products;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(ProductController.ENDPOINT)
public class ProductController {
    static public final String ENDPOINT = "/product";

    private ProductDbRepo repo;

    public ProductController(ProductDbRepo repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Product> getProducts() {
        return repo.findAll();
    }
}
