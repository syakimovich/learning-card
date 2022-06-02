package com.learningcards.controllers;

import com.learningcards.dto.CardDTO;
import com.learningcards.dto.DeckDTO;
import com.learningcards.services.DeckService;
import com.learningcards.services.LearningService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Optional;

@Controller
public class LearningController {

    public static final int LEARN_NEW_LIMIT = 3;
    public static final long MILLISEC_TO_REPEAT_NEW = 60 * 1000;

    private LearningService learningService;
    private DeckService deckService;

    public LearningController(LearningService learningService, DeckService deckService) {
        this.learningService = learningService;
        this.deckService = deckService;
    }

    @GetMapping("/deck/{deckId}/learn-new")
    public String learnNew(@PathVariable("deckId") Long deckId, Model model, Principal principal) {
        learningService.addLearningStatesToCards(deckId, principal.getName());
        int numCards = learningService.getNumberOfNewCardsToLearn(deckId, principal.getName());
        if (numCards < 1) {
            return "redirect:/deck/" + deckId;
        }
        Optional<CardDTO> cardOptional = learningService.getNextNewCardToLearn(deckId, principal.getName());
        if (cardOptional.isEmpty()) {
            return "redirect:/deck/" + deckId;
        }
        CardDTO card = cardOptional.get();
        DeckDTO deck = deckService.getDeck(deckId);

        model.addAttribute("deckName", deck.getName());
        model.addAttribute( "cardToLearn", card);
        model.addAttribute( "stage", 0);
        model.addAttribute( "maxStage", Math.min(numCards, LEARN_NEW_LIMIT));
        return "learn-new";
    }

    @PostMapping("/deck/{deckId}/learn-new")
    public String processLearnNew(@PathVariable("deckId") Long deckId, @RequestParam("stage") int stage,
                           @RequestParam("maxStage") int maxStage, @RequestParam("cardId") Long cardId,
                           Model model, Principal principal) {
        learningService.saveLearningResult(principal.getName(), cardId, System.currentTimeMillis() + MILLISEC_TO_REPEAT_NEW);
        if (stage + 1 >= maxStage) {
            return "redirect:/deck/" + deckId;
        }
        Optional<CardDTO> cardOptional = learningService.getNextNewCardToLearn(deckId, principal.getName());
        if (cardOptional.isEmpty()) {
            return "redirect:/deck/" + deckId;
        }
        CardDTO card = cardOptional.get();
        DeckDTO deck = deckService.getDeck(deckId);

        model.addAttribute("deckName", deck.getName());
        model.addAttribute( "cardToLearn", card);
        model.addAttribute( "stage", stage + 1);
        model.addAttribute( "maxStage", maxStage);
        return "learn-new";
    }
}
