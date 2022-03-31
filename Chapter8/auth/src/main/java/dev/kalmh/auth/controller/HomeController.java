package dev.kalmh.auth.controller;

import dev.kalmh.auth.infra.AuthenticationFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("home")
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    private final AuthenticationFacade authenticationFacade;

    public HomeController (
            AuthenticationFacade authenticationFacade
    ) {
        this.authenticationFacade = authenticationFacade;
    }

    @GetMapping
    public String home() {
        try {
            logger.info("connected user : {}", authenticationFacade.getUserName());
        } catch (NullPointerException e) {
            logger.info("No User logged in");
        }
        return "index";
    }

    @GetMapping("/principal")
    public String homePrincipal(Principal principal) {
        try {
            logger.info("connected user : ", principal.getName());
        } catch (NullPointerException e) {
            logger.info("no user logged in");
        }
        return "index";
    }

    @GetMapping("/authentication")
    public String homeAuthentication(Authentication authentication) {
        try {
            logger.info("connected user : ", authentication.getName());
        } catch (NullPointerException e) {
            logger.info("no user logged in");
        }
        return "index";
    }
}
