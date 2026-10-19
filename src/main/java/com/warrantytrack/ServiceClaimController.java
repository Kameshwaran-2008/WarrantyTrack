
package com.warrantytrack;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.List;

@Controller
public class ServiceClaimController {

    private final ServiceClaimRepository claimRepository;
    private final ProductRepository productRepository;
    private final AppUserRepository appUserRepository;

    public ServiceClaimController(
            ServiceClaimRepository claimRepository,
            ProductRepository productRepository,
            AppUserRepository appUserRepository) {
        this.claimRepository = claimRepository;
        this.productRepository = productRepository;
        this.appUserRepository = appUserRepository;
    }

    @GetMapping("/claims")
    public String claims(Model model, Authentication authentication) {
        AppUser user = getCurrentUser(authentication);

        model.addAttribute(
                "products",
                productRepository.findByOwner_Id(user.getId())
        );

        model.addAttribute(
                "claims",
                claimRepository.findByOwner_Id(user.getId())
        );

        return "claims";
    }

    @PostMapping("/claims")
    public String addClaim(
            @RequestParam Long productId,
            @RequestParam String issueDescription,
            Authentication authentication) {

        AppUser user = getCurrentUser(authentication);

        Product product = productRepository
                .findByIdAndOwner_Id(productId, user.getId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Product not found or access denied"
                        )
                );

        ServiceClaim claim = new ServiceClaim();
        claim.setProduct(product);
        claim.setOwner(user);
        claim.setIssueDescription(issueDescription);
        claim.setClaimDate(LocalDate.now());
        claim.setStatus("Pending");

        claimRepository.save(claim);

        return "redirect:/claims";
    }

    @PostMapping("/claims/update-status/{id}")
    public String updateStatus(
            @PathVariable Long id,
            @RequestParam String status,
            Authentication authentication) {

        List<String> validStatuses = List.of(
                "Pending",
                "In Progress",
                "Approved",
                "Rejected"
        );

        if (!validStatuses.contains(status)) {
            throw new IllegalArgumentException("Invalid claim status");
        }

        AppUser user = getCurrentUser(authentication);

        ServiceClaim claim = claimRepository
                .findByIdAndOwner_Id(id, user.getId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Claim not found or access denied"
                        )
                );

        claim.setStatus(status);
        claimRepository.save(claim);

        return "redirect:/claims";
    }

    private AppUser getCurrentUser(Authentication authentication) {
        return appUserRepository.findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new IllegalStateException("User not found")
                );
    }
}
