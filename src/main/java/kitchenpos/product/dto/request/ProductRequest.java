package kitchenpos.product.dto.request;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;

public class ProductRequest {

    private String name;
    private BigDecimal price;

    private ProductRequest() {
    }

    public ProductRequest(final String name, final Long price) {
        this(name, BigDecimal.valueOf(price));
    }

    public ProductRequest(final String name, final BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Product toEntity() {
        return new Product(name, price);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
