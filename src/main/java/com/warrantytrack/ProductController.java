package com.warrantytrack;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;

@Controller
public class ProductController {

    private final ProductRepository productRepository;
    private final AppUserRepository appUserRepository;

    public ProductController(
            ProductRepository productRepository,
            AppUserRepository appUserRepository) {
        this.productRepository = productRepository;
        this.appUserRepository = appUserRepository;
    }

    @GetMapping("/products")
    public String products(Model model, Authentication authentication) {
        AppUser user = getCurrentUser(authentication);

        model.addAttribute("product", new Product());
        model.addAttribute(
                "products",
                productRepository.findByOwner_Id(user.getId())
        );

        return "products";
    }

    @PostMapping("/products")
    public String addProduct(
            @ModelAttribute Product product,
            Authentication authentication,
            Model model) {

        if (!isValidProduct(product)) {
            AppUser user = getCurrentUser(authentication);

            model.addAttribute("product", product);
            model.addAttribute(
                    "products",
                    productRepository.findByOwner_Id(user.getId())
            );
            model.addAttribute(
                    "error",
                    "Enter valid product details, a purchase date that is not in the future, and a warranty between 1 and 120 months."
            );

            return "products";
        }

        AppUser user = getCurrentUser(authentication);
        product.setOwner(user);
        productRepository.save(product);

        return "redirect:/products";
    }

    @GetMapping("/products/view/{id}")
    public String viewProduct(
            @PathVariable Long id,
            Authentication authentication,
            Model model) {

        AppUser user = getCurrentUser(authentication);

        Product product = productRepository
                .findByIdAndOwner_Id(id, user.getId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Product not found or access denied"));

        model.addAttribute("product", product);

        return "product-details";
    }

    @GetMapping("/products/edit/{id}")
    public String editProduct(
            @PathVariable Long id,
            Authentication authentication,
            Model model) {

        AppUser user = getCurrentUser(authentication);

        Product product = productRepository
                .findByIdAndOwner_Id(id, user.getId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Product not found or access denied"));

        model.addAttribute("product", product);
        model.addAttribute("editMode", true);

        return "edit-product";
    }

    @PostMapping("/products/edit/{id}")
    public String updateProduct(
            @PathVariable Long id,
            @ModelAttribute Product updatedProduct,
            Authentication authentication,
            Model model) {

        AppUser user = getCurrentUser(authentication);

        Product product = productRepository
                .findByIdAndOwner_Id(id, user.getId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Product not found or access denied"));

        if (!isValidProduct(updatedProduct)) {
            model.addAttribute("product", updatedProduct);
            model.addAttribute(
                    "error",
                    "Enter valid product details, a purchase date that is not in the future, and a warranty between 1 and 120 months."
            );

            return "edit-product";
        }

        product.setName(updatedProduct.getName());
        product.setBrand(updatedProduct.getBrand());
        product.setPurchaseDate(updatedProduct.getPurchaseDate());
        product.setWarrantyMonths(updatedProduct.getWarrantyMonths());

        productRepository.save(product);

        return "redirect:/products";
    }

    @PostMapping("/products/delete/{id}")
    public String deleteProduct(
            @PathVariable Long id,
            Authentication authentication) {

        AppUser user = getCurrentUser(authentication);

        productRepository.findByIdAndOwner_Id(id, user.getId())
                .ifPresent(productRepository::delete);

        return "redirect:/products";
    }

    private boolean isValidProduct(Product product) {
        return product.getName() != null
                && !product.getName().trim().isEmpty()
                && product.getBrand() != null
                && !product.getBrand().trim().isEmpty()
                && product.getPurchaseDate() != null
                && !product.getPurchaseDate().isAfter(LocalDate.now())
                && product.getWarrantyMonths() >= 1
                && product.getWarrantyMonths() <= 120;
    }

    private AppUser getCurrentUser(Authentication authentication) {
        return appUserRepository.findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new IllegalStateException("User not found"));
    }
}