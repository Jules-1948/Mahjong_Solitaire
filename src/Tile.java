public class Tile {
    private String[] suits = {"characters", "circles", "bamboos", "winds", "dragons", "seasons", "flowers"};
    private int topLeftX;
    private int topLeftY;
    private int suit;
    private int face;

    // colors for making the toString method more readable
    private final String[] suitColors = {"\u001B[41m", "\u001B[44m", "\u001B[42m", "\u001B[46m", "\u001B[45m", "\u001B[47m", "\u001B[43m"};
                //characters = red, circles = blue, bamboos = green, winds = cyan, dragons = magenta, seasons = white, flowers = yellow 
    private final String resetColor = "\u001B[0m";


    public Tile(int leftX, int leftY, int suit, int face){
        topLeftX = leftX;
        topLeftY = leftY;
        this.suit = suit;
        this.face = face;
    }

    @Override
    public String toString(){
        return suitColors[suit] + face + resetColor;
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
