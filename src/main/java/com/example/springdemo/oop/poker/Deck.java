package com.example.springdemo.oop.poker;

import jakarta.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.apache.commons.lang3.ArrayUtils.swap;

/**
 * 牌桌类
 * 数据：所有牌
 * 行为：洗牌、发牌
 * 可以优化的点：很好扩展地支持多个人来玩，多加一副或者少一副牌来玩儿
 */
public class Deck {

  private final Card[] cards;
  /**
   * 已经发出去多少张牌
   */
  private int cardsHasBeenDeal;

  public Deck() {
    // 初始化所有牌，这时的牌是新的，即所有的牌都是按照同一花色从 A-K 排列好的
    cards = new Card[52];
    Suit[] allSuits = Suit.values();
    for (int j = 0;j < allSuits.length;j++) {
      for (int i = 0;i < 13;i++) {
        Card card = new Card(allSuits[j], i+1);
        cards[(j*13) + i] = card;
      }
    }
    System.out.println("all cards has been reset");
    this.showCards();
    System.out.println("shuffle cards");
    this.shuffle();
    showCards();
  }

  /**
   * 洗牌，将所有牌都收回牌桌，重新开始
   * 或者是完成将所有牌的顺序打乱
   * O(N)
   */
  public void shuffle() {
    Random random = new Random(cards.length-1);
    for (int i = 0;i < cards.length;i++) {
      swap(cards, i, random.nextInt());
    }
  }

  /**
   * 发牌，随机发牌
   */
  @Nonnull
  public Card deal() {
    return cards[cardsHasBeenDeal++];
  }

  /**
   * 查看当前牌桌还剩下多少牌，主要用来看是不是牌都发完了
   * @return
   */
  public int cardsLeft() {
    return cards.length - cardsHasBeenDeal;
  }

  /**
   * 查看当前所有牌，证明没少牌没出老千
   */
  public void showCards() {
    for (Card card : cards) {
      System.out.print(card + " ");
    }
    System.out.println();
  }

}
