package com.sontaypham.storemvc.dto.response.order;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItemResponse {
    UUID productId;
    String productName;
    String productImage;
    int quantity;
    BigDecimal unitPrice;
    BigDecimal totalPrice;

    @JsonProperty("unitPriceFormatted")
    public String getUnitPriceFormatted() {
        return unitPrice != null ? "₫" + NumberFormat.getInstance(new Locale("vi", "VN")).format(unitPrice) : "";
    }

    @JsonProperty("totalPriceFormatted")
    public String getTotalPriceFormatted() {
        return totalPrice != null ? "₫" + NumberFormat.getInstance(new Locale("vi", "VN")).format(totalPrice) : "";
    }
}
