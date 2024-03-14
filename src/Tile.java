import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class Tile {
    private int topLeftX;
    private int topLeftY;
    private int suit;
    private int number;

    public Tile(int leftX, int leftY, int suit, int number){
        topLeftX = leftX;
        topLeftY = leftY;
        this.suit = suit;
        this.number = number;
    }

    @Override
    public String toString(){
        return "( " + topLeftX + ", " + topLeftY + " ) -> " + number + " of " + suit;
    }

    public int getTopLeftX() {
        return topLeftX;
    }

    public int getTopLeftY() {
        return topLeftY;
    }

    public int getSuit() {
        return suit;
    }

    public int getNumber() {
        return number;
    }
}
