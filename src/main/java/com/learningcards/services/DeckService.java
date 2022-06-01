package com.learningcards.services;

import com.learningcards.dto.DeckDTO;
import com.learningcards.entities.Deck;
import com.learningcards.entities.User;
import com.learningcards.repositories.DeckRepository;
import com.learningcards.repositories.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeckService {
    private DeckRepository deckRepository;
    private UserRepository userRepository;

    public DeckService(DeckRepository deckRepository, UserRepository usersRepository) {
        this.deckRepository = deckRepository;
        this.userRepository = usersRepository;
    }

    public List<DeckDTO> getDecksByUsername(String username) {
        return deckRepository.findByUsername(username).stream().map(d -> new DeckDTO(d.getId(), d.getName())).toList();
    }

    public void addDeck(DeckDTO deckDTO, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username %s not found".formatted(username)));
        deckRepository.save(new Deck(user, deckDTO.getName()));
    }
}
