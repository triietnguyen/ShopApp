package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDTO {
    @JsonProperty("order_id")
    @Min(value = 1, message = "Order id must be > 0")
    private Long orderId;

    @JsonProperty("product_id")
    @Min(value = 1, message = "Product id must be > 0")
    private Long productId;

    @Min(value = 0, message = "Price must be > 0")
    private Long price;

    @Min(value = 1, message = "number of products id must be >= 1")
    @JsonProperty("number_of_products")
    private int numberOfProducts;

    @Min(value = 0, message = "total money must be >= 0")
    @JsonProperty("total_money")
    private int totalMoney;

    private String color;
}
