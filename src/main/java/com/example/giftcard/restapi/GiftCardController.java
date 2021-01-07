package com.example.giftcard.restapi;

import com.example.giftcard.coreapi.command.IssueCardCmd;
import com.example.giftcard.coreapi.command.RedeemCardCmd;
import com.example.giftcard.coreapi.command.ReimburseCardCmd;
import com.example.giftcard.coreapi.query.GiftCardSummaryQuery;
import com.example.giftcard.query.entity.GiftCardSummary;
import com.example.giftcard.restapi.request.IssueCardRequest;
import com.example.giftcard.restapi.request.RedeemRequest;
import com.example.giftcard.restapi.response.IssueCardResponse;
import com.example.giftcard.restapi.response.RedeemResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@Slf4j
public class GiftCardController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    @PostMapping("/card")
    public IssueCardResponse issueCard(@RequestBody IssueCardRequest request) throws InterruptedException {
        UUID id = UUID.randomUUID();
        commandGateway.sendAndWait(new IssueCardCmd(id, request.getAmount()));
        return new IssueCardResponse(id);
    }

    @PostMapping("/card/{cardId}/redeem")
    public RedeemResponse redeem(@PathVariable UUID cardId, @RequestBody  RedeemRequest request){
        UUID transactionID = UUID.randomUUID();
        // TODO: check error handling
        commandGateway.sendAndWait(new RedeemCardCmd(cardId, transactionID, request.getAmount()));
        return new RedeemResponse(transactionID);
    }

    @PostMapping("/card/{cardId}/reimburse/{txnId}")
    public void reimburse( @PathVariable UUID cardId,  @PathVariable UUID txnId){
        commandGateway.sendAndWait(new ReimburseCardCmd(cardId, txnId));
    }

    @GetMapping("/card/{cardId}")
    public CompletableFuture<GiftCardSummary> getGiftCardSummary(@PathVariable UUID cardId){
        log.debug("querying ");
        return queryGateway.query(
                new GiftCardSummaryQuery(cardId),
                ResponseTypes.instanceOf(GiftCardSummary.class));
    }
}
