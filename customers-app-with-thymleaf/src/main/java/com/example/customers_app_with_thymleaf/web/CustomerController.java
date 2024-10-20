package com.example.customers_app_with_thymleaf.web;

import com.example.customers_app_with_thymleaf.entity.Customer;
import com.example.customers_app_with_thymleaf.model.Product;
import com.example.customers_app_with_thymleaf.repository.CustomerRepo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
public class CustomerController {

    CustomerRepo customerRepo;
    private ClientRegistrationRepository clientRegistrationRepository;

    @GetMapping("/customers")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String customers(Model model) {
        List<Customer> customerList = customerRepo.findAll();
        model.addAttribute("customers", customerList);
        return "customers";
    }
    @GetMapping("/notAuthorized")
    public String notAuthorized() {
        return "notAuthorized";
    }
    @GetMapping("/products")
    public String products(Model model) {
        /*****Récupérer JWT******/
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        OAuth2AuthenticationToken oAuth2AuthenticationToken= (OAuth2AuthenticationToken) authentication;
        DefaultOidcUser oidcUser = (DefaultOidcUser) oAuth2AuthenticationToken.getPrincipal();
        String jwtTokenValue=oidcUser.getIdToken().getTokenValue();
        /*****Envoyer la requète à Inventory microservice******/
        RestClient restClient = RestClient.create("http://localhost:8098");
        List<Product> products = restClient.get()
                .uri("/products")
                .headers(httpHeaders -> httpHeaders.set(HttpHeaders.AUTHORIZATION, "Bearer "+jwtTokenValue))
                .retrieve()
                .body(new ParameterizedTypeReference<>(){});
        model.addAttribute("products",products);
        return "products";
    }
    @GetMapping("/auth")
    @ResponseBody
    public Authentication authentication(Authentication authentication) {
        return authentication;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/oauth2Login")
    public String oauth2Login(Model model) {
        String authorizationRequestBaseUri = "oauth2/authorization";
        Map<String, String> oauth2AuthenticationUrls = new HashMap<>();
        Iterable<ClientRegistration> clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
        clientRegistrations.forEach(registration ->{
            oauth2AuthenticationUrls.put(registration.getClientName(),
                    authorizationRequestBaseUri + "/" + registration.getRegistrationId());
        });
        model.addAttribute("urls", oauth2AuthenticationUrls);
        return "oauth2Login";
    }


}
