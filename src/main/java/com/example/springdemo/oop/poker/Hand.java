package com.example.springdemo.oop.poker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * 对于一手牌，
 * 数据有一手牌
 * 班牌、打牌两种行为
 * 进一步打牌过程中可能要告诉别人你手里有多少张牌
 * 牌放手里，还需要排序，比如按照牌面一样的排在一起 （如果按照花色一样的排在一起怎么处理？）
 *
 */
public class Hand {

  private final Card[] cards = new Card[13];
  private int cardCount;

  public void receiveCard(Card card) {
    cards[cardCount++] = card;
    if (cardCount == cards.length) {
      sortCardsByValue();
    }
  }

  public void removeCard(Card card) {
    for (int i = 0;i < cards.length;i++) {
      if (cards[i].equals(card)) {
        cards[i] = null;
        cardCount--;
      }
    }
  }

  public int getCardsCount() {
    return cardCount;
  }

  /**
   * 按照花色一样的放在一起
   */
  private void sortCardsBySuit() {
    Arrays.sort(cards, Comparator.comparing(Card::getSuit));
  }

  /**
   * 按照大小一样的放在一起
   */
  private void sortCardsByValue() {
    Arrays.sort(cards, Comparator.comparing(Card::getValue));
  }

  /**
   * 展示自己手里的所有牌
   */
  public void showCards() {
    for (Card card : cards) {
      System.out.print(card + " ");
    }
    System.out.println();
  }

}
