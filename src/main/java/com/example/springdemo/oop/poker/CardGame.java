package com.example.springdemo.oop.poker;

public class CardGame {
  public static void main(String[] args) {
    Deck deck = new Deck();
    Hand[] hands = new Hand[4];
    for (int i = 0;i < hands.length;i++) {
      hands[i] = new Hand();
    }
    // 当前该给谁发牌
    int curDealHand = 0;
    // 发牌
    while (deck.cardsLeft() != 0) {
      Card card = deck.deal();
      hands[curDealHand % 4].receiveCard(card);
      curDealHand++;
    }
    // 每个人亮出自己的牌
    for (Hand hand : hands) {
      hand.showCards();
    }
  }
}
