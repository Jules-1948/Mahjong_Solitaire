public class Tile {
    private String[] suits = { "sticks", "wheels", "numbers", "winds", "dragons", "flowers", "seasons"};
    private int topLeftX;
    private int topLeftY;
    private int suit;
    private int face;


    public Tile(int leftX, int leftY, int suit, int face){
        topLeftX = leftX;
        topLeftY = leftY;
        this.suit = suit;
        this.face = face;
    }

    @Override
    public String toString(){
        return "( " + topLeftX + ", " + topLeftY + " ) -> " + face + " of " + suit + "(" + suits[suit] + ")";
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

    public int getFace() {
        return face;
    }
}
