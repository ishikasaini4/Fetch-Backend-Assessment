package com.fetch.assessment.backend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.UUID;


@Entity
@Table(name = "Receipts")
@Getter
@Setter
@ToString
public class Receipt{

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "retailer", nullable = false)
    @Size(min = 1, message = "Retailer name cannot be empty")
    private String retailer;

    @Column(name="purchaseDate", nullable = false)
    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$", message = "purchase date must be in format YYYY-MM-DD")
    @NotEmpty(message = "purchaseDate cannot be empty")
    private String purchaseDate;

    @Column(name = "purchaseTime", nullable = false)
    @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$", message = "purchase time must be in format HH:mm")
    @NotEmpty(message = "purchaseTime cannot be empty")
    private String purchaseTime;

    @Column(name = "total", nullable = false)
    @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$", message = "Invalid total amount")
    @NotEmpty(message = "total cannot be empty")
    private String total;

    @Column(name="items", columnDefinition = "json", nullable = false)
    private String items;

}
