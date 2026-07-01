package com.syos.erp.inventory;

import com.syos.erp.product.Product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "reorder_levels",
        uniqueConstraints = @UniqueConstraint(name = "uk_reorder_levels_product", columnNames = "product_id")
)
@Getter
@Setter
@NoArgsConstructor
public class ReorderLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reorder_level_id")
    private Long id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotNull
    @Min(0)
    @Column(name = "threshold_quantity", nullable = false)
    private Integer thresholdQuantity = 50;

    @NotNull
    @Min(0)
    @Column(name = "total_stock", nullable = false)
    private Integer totalStock = 0;
}
