
package com.warrantytrack;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegisterController {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterController(
            AppUserRepository appUserRepository,
            PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam String fullName,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String accountType,
            @RequestParam(required = false) String companyName,
            Model model) {

        email = email.trim().toLowerCase();

        if (appUserRepository.existsByEmail(email)) {
            model.addAttribute("error", "This email is already registered.");
            return "register";
        }

        if (!accountType.equals("PERSONAL")
                && !accountType.equals("COMPANY")) {
            model.addAttribute("error", "Please select a valid account type.");
            return "register";
        }

        if (accountType.equals("COMPANY")
                && (companyName == null || companyName.trim().isEmpty())) {
            model.addAttribute("error", "Please enter your company name.");
            return "register";
        }

        AppUser user = new AppUser();
        user.setFullName(fullName.trim());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setAccountType(accountType);
        user.setCompanyName(
                accountType.equals("COMPANY") ? companyName.trim() : null
        );

        appUserRepository.save(user);

        return "redirect:/login";
    }
}
