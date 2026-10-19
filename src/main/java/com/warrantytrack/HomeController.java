package com.warrantytrack;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Controller
public class HomeController {

    private final ProductRepository productRepository;
    private final AppUserRepository appUserRepository;
    private final ServiceClaimRepository claimRepository;

    public HomeController(
            ProductRepository productRepository,
            AppUserRepository appUserRepository,
            ServiceClaimRepository claimRepository) {
        this.productRepository = productRepository;
        this.appUserRepository = appUserRepository;
        this.claimRepository = claimRepository;
    }

    @GetMapping("/")
    public String home(Model model, Authentication authentication) {

        AppUser user = appUserRepository.findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new IllegalStateException("User not found"));

        List<Product> products =
                productRepository.findByOwner_Id(user.getId());

        long totalClaims =
                claimRepository.countByOwner_Id(user.getId());

        List<ServiceClaim> recentClaims =
                claimRepository.findTop5ByOwner_IdOrderByClaimDateDescIdDesc(
                        user.getId());

        LocalDate today = LocalDate.now();

        long active = products.stream()
                .filter(p -> p.getWarrantyEndDate() != null)
                .filter(p -> p.getWarrantyEndDate()
                        .isAfter(today.plusDays(30)))
                .count();

        long expiring = products.stream()
                .filter(p -> p.getWarrantyEndDate() != null)
                .filter(p -> !p.getWarrantyEndDate().isBefore(today))
                .filter(p -> !p.getWarrantyEndDate()
                        .isAfter(today.plusDays(30)))
                .count();

        long expired = products.stream()
                .filter(p -> p.getWarrantyEndDate() != null)
                .filter(p -> p.getWarrantyEndDate().isBefore(today))
                .count();

        List<Product> warrantyAlerts = products.stream()
                .filter(p -> p.getWarrantyEndDate() != null)
                .filter(p -> p.getWarrantyStatus().equals("Expired")
                        || p.getWarrantyStatus().equals("Expiring Soon"))
                .sorted(Comparator.comparing(Product::getWarrantyEndDate))
                .toList();

        model.addAttribute("totalProducts", products.size());
        model.addAttribute("activeWarranties", active);
        model.addAttribute("expiringSoon", expiring);
        model.addAttribute("expiredWarranties", expired);
        model.addAttribute("totalClaims", totalClaims);
        model.addAttribute("recentClaims", recentClaims);
        model.addAttribute("warrantyAlerts", warrantyAlerts);

        return "index";
    }
}