package com.learningcards.controllers;

import com.learningcards.dto.DeckDTO;
import com.learningcards.services.CardService;
import com.learningcards.services.DeckService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class HomeController {

    private DeckService deckService;

    public HomeController(DeckService deckService) {
        this.deckService = deckService;
    }

    @GetMapping("/home")
    public String home(Principal principal, Model model) {
        model.addAttribute("decks", deckService.getDecksByUsername(principal.getName()));
        return "home";
    }
}
