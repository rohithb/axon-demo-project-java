package com.example.giftcard.command;

import com.example.giftcard.coreapi.command.IssueCardCmd;
import com.example.giftcard.coreapi.event.CardIssuedEvent;
import com.example.giftcard.coreapi.command.RedeemCardCmd;
import com.example.giftcard.coreapi.event.CardRedeemEvent;
import com.example.giftcard.coreapi.event.CardReimbursedEvent;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.modelling.command.AggregateMember;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Aggregate
@NoArgsConstructor
@Slf4j
public class GiftCard {
    @AggregateIdentifier
    private UUID id;
    private int remainingValue;

    @AggregateMember
    private List<GiftCardTransaction> transactions;

    @CommandHandler
    public GiftCard(IssueCardCmd cmd){
        log.debug("handling {}", cmd);
        if(cmd.getAmount() <= 0) throw new IllegalArgumentException("Amount <=0");
        AggregateLifecycle.apply(new CardIssuedEvent(cmd.getCardId(), cmd.getAmount()));
    }

    @CommandHandler
    public void handle(RedeemCardCmd cmd){
        log.debug("handling {}", cmd);

        if(cmd.getAmount() <= 0) throw new IllegalArgumentException("Amount <=0");
        if(cmd.getAmount() > remainingValue) throw new IllegalArgumentException("amount > remaining");
        if (transactions.stream().map(GiftCardTransaction::getTransactionId).anyMatch(cmd.getTransactionId()::equals)) {
            throw new IllegalStateException("TransactionId must be unique");
        }
        AggregateLifecycle.apply(new CardRedeemEvent(cmd.getCardId(), cmd.getTransactionId(),cmd.getAmount()));
    }

    @EventSourcingHandler
    public void on (CardIssuedEvent event){
        log.debug("applying {}", event);
        id = event.getCardId();
        remainingValue = event.getAmount();
        transactions = new ArrayList<>();
    }

    @EventSourcingHandler
    public void on(CardRedeemEvent event){
        log.debug("applying {}", event);
        remainingValue -= event.getAmount();
        transactions.add(new GiftCardTransaction(event.getTransactionId(), event.getAmount()));
    }

    @EventSourcingHandler
    public void on(CardReimbursedEvent event){
        log.debug("applying {}", event);
        remainingValue += event.getAmount();
    }
}
