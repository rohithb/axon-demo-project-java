package com.example.giftcard.query.projectors;

import com.example.giftcard.coreapi.event.CardIssuedEvent;
import com.example.giftcard.coreapi.event.CardRedeemedEvent;
import com.example.giftcard.coreapi.event.CardReimbursedEvent;
import com.example.giftcard.coreapi.query.GiftCardSummaryQuery;
import com.example.giftcard.query.entity.GiftCardSummary;
import com.example.giftcard.query.repository.GiftCardSummaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class SummaryProjector {

    private final GiftCardSummaryRepository repo;

    @EventHandler
    public void on(CardIssuedEvent event){
        log.debug("projecting {}", event);
        repo.save(new GiftCardSummary(event.getCardId(), event.getAmount(), event.getAmount()));
    }

    @EventHandler
    public void on(CardRedeemedEvent event){
        log.debug("projecting {}", event);
        repo.findById(event.getCardId()).ifPresent(
                giftCardSummary -> giftCardSummary.setRemainingValue(
                        giftCardSummary.getRemainingValue() - event.getAmount()
                )
        );
    }

    @EventHandler
    public void on(CardReimbursedEvent event){
        log.debug("projecting {}", event);
        repo.findById(event.getCardId()).ifPresent(
                giftCardSummary -> giftCardSummary.setRemainingValue(
                        giftCardSummary.getRemainingValue() + event.getAmount()
                )
        );
    }

    @QueryHandler
    public Optional<GiftCardSummary> handle(GiftCardSummaryQuery query){
        log.debug("handling query {}", query);
        return repo.findById(query.getId());
    }
}
