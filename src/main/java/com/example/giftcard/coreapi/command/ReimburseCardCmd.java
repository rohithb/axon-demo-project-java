package com.example.giftcard.coreapi.command;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

@Value
public class ReimburseCardCmd {
    @TargetAggregateIdentifier
    UUID cardId;
    UUID transactionId;
}
