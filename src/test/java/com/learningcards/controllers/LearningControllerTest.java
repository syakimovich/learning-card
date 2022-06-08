package com.learningcards.controllers;

import com.learningcards.dto.CardDTO;
import com.learningcards.dto.DeckDTO;
import com.learningcards.services.DeckService;
import com.learningcards.services.LearningService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(LearningController.class)
class LearningControllerTest {

    @MockBean
    private LearningService learningService;

    @MockBean
    private DeckService deckService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "user1")
    void learnNew_noNewCardsToLearn() throws Exception {
        long deckId = 3;
        String username = "user1";
        when(learningService.getNumberOfNewCardsToLearn(deckId, username)).thenReturn(0);

        mockMvc.perform(MockMvcRequestBuilders.get("/deck/%d/learn-new".formatted(deckId)))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/deck/" + deckId));

        verify(learningService).addLearningStatesToCards(deckId, username);
    }

    @Test
    @WithMockUser(username = "user1")
    void learnNew_ok() throws Exception {
        long deckId = 3;
        String username = "user1";
        CardDTO card = new CardDTO(3L, "front1", "back1");
        DeckDTO deck = new DeckDTO(2L, "deck1");

        when(learningService.getNumberOfNewCardsToLearn(deckId, username)).thenReturn(5);

        when(learningService.getNextNewCardToLearn(deckId, username)).thenReturn(Optional.of(card));
        when(deckService.getDeck(deckId)).thenReturn(deck);

        mockMvc.perform(MockMvcRequestBuilders.get("/deck/%d/learn-new".formatted(deckId)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("learn-new"))
                .andExpect(MockMvcResultMatchers.model().attribute("deckName", deck.getName()))
                .andExpect(MockMvcResultMatchers.model().attribute("stage", 0))
                .andExpect(MockMvcResultMatchers.model().attribute("maxStage", LearningService.LEARN_NEW_LIMIT));

        verify(learningService).addLearningStatesToCards(deckId, username);
    }
}