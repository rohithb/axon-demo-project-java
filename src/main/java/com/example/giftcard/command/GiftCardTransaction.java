package com.example.giftcard.command;

import com.example.giftcard.coreapi.command.ReimburseCardCmd;
import com.example.giftcard.coreapi.event.CardReimbursedEvent;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.modelling.command.EntityId;

import java.util.UUID;

@Slf4j
@Data
public class GiftCardTransaction {

    @EntityId
    private final UUID transactionId;
    private final int transactionValue;
    private boolean reimbursed = false;

    public GiftCardTransaction(UUID transactionId, int transactionValue) {
        this.transactionId = transactionId;
        this.transactionValue = transactionValue;
    }

    @CommandHandler
    public void handle(ReimburseCardCmd cmd){
        if(reimbursed){
            throw new IllegalArgumentException("Transaction already reimbursed");
        }
        AggregateLifecycle.apply(new CardReimbursedEvent(cmd.getCardId(), transactionId, transactionValue ));
    }


    @EventSourcingHandler
    public void on(CardReimbursedEvent event){
        if (!transactionId.equals(event.getTransactionId())) {
            return;
        }
        reimbursed = true;
    }
}
