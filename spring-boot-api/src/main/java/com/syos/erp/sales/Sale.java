package com.syos.erp.sales;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sales")
@Getter
@Setter
@NoArgsConstructor
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sale_id")
    private Long id;

    @NotNull
    @Column(name = "sale_date", nullable = false)
    private LocalDate saleDate;

    @NotNull
    @Column(name = "sale_timestamp", nullable = false)
    private LocalDateTime saleTimestamp;

    @NotBlank
    @Size(max = 255)
    @Column(name = "cashier_name", nullable = false)
    private String cashierName;

    @NotBlank
    @Size(max = 50)
    @Column(name = "transaction_type", nullable = false, length = 50)
    private String transactionType;
}
