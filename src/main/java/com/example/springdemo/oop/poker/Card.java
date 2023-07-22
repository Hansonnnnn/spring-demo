package com.example.springdemo.oop.poker;

import java.util.Objects;

/**
 * 牌类，有花色和大小两个属性
 */
public class Card {

   private final Suit suit;
   private final int value;

   public Card(Suit suit, int value) {
     this.suit = suit;
     this.value = value;
   }

  public Suit getSuit() {
    return suit;
  }

  public int getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Card card = (Card) o;
    return value == card.value && suit == card.suit;
  }

  @Override
  public int hashCode() {
    return Objects.hash(suit, value);
  }

  @Override
  public String toString() {
    return suit + String.valueOf(value);
  }
}
