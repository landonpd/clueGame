package clueGame;

import java.awt.Color;

public class Card {
	private String cardName;
	private CardType cardType;
	private Color holderColor;
	
	public Card(String cardName, CardType cardType, Color holderColor) {
		this.cardName = cardName;
		this.cardType = cardType;
		this.holderColor=holderColor;
	}
	
	public Card(String cardName, CardType cardType) {
		this.cardName = cardName;
		this.cardType = cardType;
		this.holderColor=Color.white;
	}
	
	@Override
	public boolean equals(Object target) {
		if (target == this) {
            return true;
       }
       if (target == null) {
          return false;
       }
       if (target instanceof Card) {
            Card arg = (Card) target;
            if (arg.cardName.equals(cardName) && arg.cardType == cardType) {
                return true;
            }
        }
        return false;
	}
	
	@Override
	public int hashCode() {
	      return cardName.hashCode();
	}

	
	
	//getters and setters

	@Override
	public String toString() {
		return "Card [cardName=" + cardName + ", cardType=" + cardType + ", holderColor=" + holderColor + "]";
	}

	public String getCardName() {
		return cardName;
	}


	public CardType getCardType() {
		return cardType;
	}
	
	public Color getHolderColor() {
		return holderColor;
	}
	
	public void setHolderColor(Color newColor) {
		holderColor=newColor;
	}
	
}
