package com.learningcards.controllers;

import com.learningcards.dto.CardDTO;
import com.learningcards.dto.DeckDTO;
import com.learningcards.services.CardService;
import com.learningcards.services.DeckService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class DeckController {
    private DeckService deckService;
    private CardService cardService;

    public DeckController(DeckService deckService, CardService cardService) {
        this.deckService = deckService;
        this.cardService = cardService;
    }

    @GetMapping("/deck/{deckId}")
    public String viewDeck(@PathVariable Long deckId, Model model) {
        List<CardDTO> cardsDTO = cardService.findDeckCards(deckId);
        DeckDTO deck = deckService.getDeck(deckId);
        model.addAttribute("cards", cardsDTO);
        model.addAttribute("deckId", deckId);
        model.addAttribute("deckName", deck.getName());
        return "deck";
    }

    @GetMapping("/add-deck")
    public String addDeck() {
        return "add-deck";
    }

    @PostMapping("/add-deck")
    public String processAddDeck(Principal principal, @RequestParam("deckname") String deckname) {
        deckService.addDeck(new DeckDTO(deckname), principal.getName());
        return "redirect:/home";
    }
}
