package com.example.giftcard.restapi.response;

import lombok.Value;

import java.util.UUID;

@Value
public class RedeemResponse {
    UUID transactionId;
}
