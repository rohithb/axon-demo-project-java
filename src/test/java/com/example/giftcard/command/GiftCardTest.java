package com.example.giftcard.command;

import com.example.giftcard.coreapi.event.CardIssuedEvent;
import com.example.giftcard.coreapi.event.CardRedeemEvent;
import com.example.giftcard.coreapi.command.IssueCardCmd;
import com.example.giftcard.coreapi.command.RedeemCardCmd;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.*;

import java.util.UUID;

class GiftCardTest {

    private FixtureConfiguration<GiftCard> fixture;
    private UUID cardId;
    private UUID txnId;


    @BeforeEach
    void setup() {
        fixture = new AggregateTestFixture<>(GiftCard.class);
        cardId = UUID.randomUUID();
        txnId = UUID.randomUUID();
    }

    @Test
    void shouldIssueGiftCard() {
        UUID cardId = UUID.randomUUID();
        fixture.givenNoPriorActivity()
                .when(new IssueCardCmd(cardId, 100))
                .expectEvents(new CardIssuedEvent(cardId, 100));
    }
    @Test
    void shouldRedeemGiftCard() {
        fixture.given(new CardIssuedEvent(cardId, 100))
                .when(new RedeemCardCmd(cardId, txnId,20))
                .expectEvents(new CardRedeemEvent(cardId, txnId,20));
    }

    @Test
    void shouldNotRedeemWithNegativeAmount() {
        fixture.given(new CardIssuedEvent(cardId, 100))
                .when(new RedeemCardCmd(cardId, txnId, -10))
                .expectException(IllegalArgumentException.class);
    }

    @Test
    void shouldNotRedeemWhenThereIsNotEnoughMoney() {
        fixture.given(new CardIssuedEvent(cardId, 100))
                .when(new RedeemCardCmd(cardId, txnId, 110))
                .expectException(IllegalArgumentException.class);
    }
}
