package it.unicam.cs.ids.loyalty.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/creaProgrammaFedelta")
    public String creaProgrammaFedelta() {
        return "creaProgrammaFedelta"; // Nome della tua nuova pagina HTML
    }

    @GetMapping("/merchant-login")
    public String merchantDashboard() {
        return "merchant-login"; // Nome della tua pagina HTML della dashboard aziendale
    }
}

