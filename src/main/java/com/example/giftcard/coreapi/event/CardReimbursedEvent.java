package com.example.giftcard.coreapi.event;

import lombok.Value;

import java.util.UUID;

@Value
public class CardReimbursedEvent {
    UUID cardId;
    UUID transactionId;
    int amount;
}
