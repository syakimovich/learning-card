package com.learningcards.services;

import com.learningcards.dto.CardDTO;
import com.learningcards.dto.ReviewTimeDTO;
import com.learningcards.entities.Card;
import com.learningcards.entities.CardLearningState;
import com.learningcards.entities.User;
import com.learningcards.repositories.CardLearningStateRepository;
import com.learningcards.repositories.CardRepository;
import com.learningcards.repositories.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class LearningService {

    public static final int LEARN_NEW_LIMIT = 3;
    public static final int REVIEW_LIMIT = 3;
    public static final long MILLISEC_TO_REPEAT_NEW = 60 * 1000;
    public static final ReviewTimeDTO[] REVIEW_TIMES = {
            new ReviewTimeDTO(10 * 60 * 1000, "10 minutes"),
            new ReviewTimeDTO(24 * 60 * 60 * 1000, "1 day"),
            new ReviewTimeDTO(7 * 24 * 60 * 60 * 1000, "1 week")};

    CardLearningStateRepository learningStateRepository;
    CardRepository cardRepository;
    UserRepository userRepository;

    public LearningService(CardLearningStateRepository learningStateRepository, CardRepository cardRepository, UserRepository userRepository) {
        this.learningStateRepository = learningStateRepository;
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
    }

    public void addLearningStatesToCards(long deckId, String username) {
        List<CardLearningState> learningStates = learningStateRepository.findAllByDeckIdAndUsername(deckId, username);
        List<Card> cards = cardRepository.findByDeckId(deckId);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username %s not found".formatted(username)));
        Set<Long> cardStatesAlreadyExist = new HashSet<>();
        for(CardLearningState learningState: learningStates) {
            cardStatesAlreadyExist.add(learningState.getCard().getId());
        }
        for (Card card : cards) {
            if (!cardStatesAlreadyExist.contains(card.getId())) {
                learningStateRepository.save(new CardLearningState(card, user, false, false));
            }
        }
    }

    public void saveLearningResult(String username, long cardId, long timeToReview) {
        CardLearningState learningState = learningStateRepository.findByUsernameAndCardId(username, cardId);
        learningState.setInLearning(true);
        learningState.setToReview(new Timestamp(timeToReview));
        learningStateRepository.save(learningState);
    }

    public Optional<CardDTO> getNextNewCardToLearn(long deckId, String username) {
        List<CardLearningState> toLearn = learningStateRepository.findNextNewToLearn(deckId, username,
                PageRequest.of(0, 1, Sort.by("id").ascending()));
        if (toLearn.size() < 1) {
            return Optional.empty();
        }
        Card card = toLearn.get(0).getCard();
        return Optional.of(new CardDTO(card.getId(), card.getFront(), card.getBack()));
    }

    public int getNumberOfNewCardsToLearn(long deckId, String username) {
        return learningStateRepository.countNewToLearn(deckId, username);
    }

    public Optional<CardDTO> getNextCardToReview(long deckId, String username) {
        List<CardLearningState> toLearn = learningStateRepository.findNextToReview(deckId, username,
                PageRequest.of(0, 1, Sort.by("toReview").ascending()));
        if (toLearn.size() < 1) {
            return Optional.empty();
        }
        Card card = toLearn.get(0).getCard();
        return Optional.of(new CardDTO(card.getId(), card.getFront(), card.getBack()));
    }

    public int getNumberOfCardsToReview(long deckId, String username) {
        return learningStateRepository.countToReview(deckId, username);
    }

    public List<CardDTO> getCardsWithLearningState(long deckId, String username) {
        return learningStateRepository.findAllByDeckIdAndUsername(deckId, username).stream()
                .map(ls -> new CardDTO(ls.getCard().getId(), ls.getCard().getFront(),
                        ls.getCard().getBack(), ls.getToReview() == null ? "" : ls.getToReview().toString(),
                        ls.isInLearning() ? "learning" : ls.isLearned() ? "learned" : "not started")).toList();
    }
}
