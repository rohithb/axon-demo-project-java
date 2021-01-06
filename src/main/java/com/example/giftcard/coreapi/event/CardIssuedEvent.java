package com.example.giftcard.coreapi.event;

import lombok.Value;

import java.util.UUID;

@Value
public class CardIssuedEvent {
    UUID cardId;
    int amount;
}
