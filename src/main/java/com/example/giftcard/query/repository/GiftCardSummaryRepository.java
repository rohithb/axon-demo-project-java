package com.example.giftcard.query.repository;

import com.example.giftcard.query.entity.GiftCardSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GiftCardSummaryRepository extends JpaRepository<GiftCardSummary, UUID> {
}
