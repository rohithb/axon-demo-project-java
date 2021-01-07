package com.example.giftcard.restapi.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IssueCardRequest {
    private Integer amount;
}
