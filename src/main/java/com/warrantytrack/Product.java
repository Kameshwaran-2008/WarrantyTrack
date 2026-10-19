
package com.warrantytrack;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import java.time.LocalDate;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String brand;
    private LocalDate purchaseDate;
    private int warrantyMonths;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private AppUser owner;

    public Product() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public int getWarrantyMonths() {
        return warrantyMonths;
    }

    public void setWarrantyMonths(int warrantyMonths) {
        this.warrantyMonths = warrantyMonths;
    }

    public AppUser getOwner() {
        return owner;
    }

    public void setOwner(AppUser owner) {
        this.owner = owner;
    }

    public LocalDate getWarrantyEndDate() {
        if (purchaseDate == null) {
            return null;
        }

        return purchaseDate.plusMonths(warrantyMonths);
    }

    public String getWarrantyStatus() {
        LocalDate endDate = getWarrantyEndDate();

        if (endDate == null) {
            return "Unknown";
        }

        LocalDate today = LocalDate.now();

        if (endDate.isBefore(today)) {
            return "Expired";
        } else if (!endDate.isAfter(today.plusDays(30))) {
            return "Expiring Soon";
        } else {
            return "Active";
        }
    }
}
