package com.example.giftcard.coreapi.event;

import lombok.Value;

import java.util.UUID;

@Value
public class CardRedeemedEvent {
    UUID cardId;
    UUID transactionId;
    int amount;
}
