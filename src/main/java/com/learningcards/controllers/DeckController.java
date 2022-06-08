package com.learningcards.controllers;

import com.learningcards.dto.CardDTO;
import com.learningcards.dto.DeckDTO;
import com.learningcards.services.CardService;
import com.learningcards.services.DeckService;
import com.learningcards.services.LearningService;
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
    private LearningService learningService;

    public DeckController(DeckService deckService, CardService cardService, LearningService learningService) {
        this.deckService = deckService;
        this.cardService = cardService;
        this.learningService = learningService;
    }

    @GetMapping("/deck/{deckId}")
    public String viewDeck(@PathVariable Long deckId, Model model, Principal principal) {
        List<CardDTO> cardsDTO = learningService.getCardsWithLearningState(deckId, principal.getName());
        DeckDTO deck = deckService.getDeck(deckId);
        int newToLearn = learningService.getNumberOfNewCardsToLearn(deckId, principal.getName());
        int newToLearnNow = Math.min(newToLearn, LearningService.LEARN_NEW_LIMIT);
        int toReview = learningService.getNumberOfCardsToReview(deckId, principal.getName());
        int toReviewNow = Math.min(toReview, LearningService.REVIEW_LIMIT);
        model.addAttribute("cards", cardsDTO);
        model.addAttribute("deckId", deckId);
        model.addAttribute("deckName", deck.getName());
        model.addAttribute("newToLearn", newToLearn);
        model.addAttribute("newToLearnNow", newToLearnNow);
        model.addAttribute("toReview", toReview);
        model.addAttribute("toReviewNow", toReviewNow);
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
