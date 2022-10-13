package com.omar.org;

public class Card {
    Height cardHeight;
    Card.Color cardColor;
    public Card(Height cardHeight, Color cardColor) {
        this.cardHeight = cardHeight;
        this.cardColor = cardColor;
    }
    public Card() {

    }
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Card))
            return false;
        return obj.hashCode()==this.hashCode();

    }
    @Override
    public int hashCode() {
        return Integer.valueOf(this.cardColor+""+this.cardHeight);
    }
    public enum Height {
        ACE_1(1),
        TWO_2(2),
        THREE_3(3),
        FOUR_4(4),
        FIVE_5(5),
        SIX_6(6),
        SEVEN_7(7),
        EIGHT_8(8),
        NINE_9(9),
        TEN_10(10),
        JACK_11(11),
        QUEEN_12(12),
        KING_13(13);

        public int getIntValue() {
            return value;
        }
        private int value=0;
        Height(int value) {
            this.value = value;
        }

    }
    public enum Color{
        CARREAU(1),CŒUR(2),PIQUE(3),TRÈFLE(4);
        int value;
        Color(int value) {
            this.value = value;
        }
        public int getIntValue() {
            return value;
        }
    }
}
