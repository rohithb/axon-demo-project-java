package com.example.giftcard.coreapi.command;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

@Value
public class RedeemCardCmd {
    @TargetAggregateIdentifier UUID cardId;
    UUID transactionId;
    int amount;
}
