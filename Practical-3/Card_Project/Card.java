import java.util.Objects;

public class Card {

    private String suit;
    private String rank;

    public Card(String suit, String rank) {
        this.suit = suit;
        this.rank = rank;
    }

    @Override
    public String toString() {
        return suit + " of " + rank;
    }

    @Override
    public boolean equals(Object obj) {
        Card c = (Card) obj;

        return suit == c.suit && rank == c.rank;
    }

    @Override
    public int hashCode() {
        return Objects.hash(suit, rank);
    }
}
