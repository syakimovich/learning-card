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

@Controller
public class CardController {

    private DeckService deckService;
    private CardService cardService;

    public CardController(DeckService deckService, CardService cardService) {
        this.deckService = deckService;
        this.cardService = cardService;
    }

    @GetMapping("/deck/{deckId}/add-card")
    public String addCard(@PathVariable Long deckId, Model model) {
        DeckDTO deck = deckService.getDeck(deckId);
        model.addAttribute("deckName", deck.getName());
        model.addAttribute("deckId", deckId);
        return "add-card";
    }

    @PostMapping("/deck/{deckId}/add-card")
    public String processAddCard(@RequestParam("deckId") Long deckId,
                                 @RequestParam("front") String front, @RequestParam("back") String back) {
        cardService.addCard(new CardDTO(front, back), deckId);
        return "redirect:/deck/" + deckId;
    }
}
