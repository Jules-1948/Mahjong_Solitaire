public class Tile {
    private String[] suits = {"characters", "circles", "bamboos", "winds", "dragons", "seasons", "flowers"};
    private int topLeftX;
    private int topLeftY;
    private int zLayer;
    private int suit;
    private int face;

    // colors for making the toString method more readable
    private final String[] suitColors = {"\u001B[41m", "\u001B[44m", "\u001B[42m", "\u001B[46m", "\u001B[45m", "\u001B[47m", "\u001B[43m"};
                //characters = red, circles = blue, bamboos = green, winds = cyan, dragons = magenta, seasons = white, flowers = yellow 
    private final String resetColor = "\u001B[0m";

    private Tile(Tile parentTile) {
        topLeftX = parentTile.topLeftX;
        topLeftY = parentTile.topLeftY;
        zLayer = parentTile.zLayer;
        suit = parentTile.suit;
        face = parentTile.face;
    }

    public Tile(int leftX, int leftY, int zLayer, int suit, int face){
        topLeftX = leftX;
        topLeftY = leftY;
        this.zLayer = zLayer;
        this.suit = suit;
        this.face = face;
    }

    @Override
    public String toString(){
        return suitColors[suit] + face + resetColor;
    }

    public String toString(boolean indices){
        if(indices){
            return "(" + topLeftX + ", " + topLeftY + ", " + zLayer + ") " + face + " of " + suit + " aka " + suits[suit];
        } else {
            return "  " + face + " of " + suit + " aka " + suits[suit];
        }
    }

    // Only use this function within board classes
    public String getUniqueString(){
        // return single matching id for all seasons and flowers tiles for 
        // the rule that they can all be intermatched
        if(suit == 5 || suit == 6){
            return "Cinderella";
        }

        return face + " of " + suit;
    }

    public int getTopLeftX() {
        return topLeftX;
    }

    public int getTopLeftY() {
        return topLeftY;
    }

    public int getZLayer(){
        return zLayer;
    }

    public int getSuit() {
        return suit;
    }

    public int getFace() {
        return face;
    }

    public Tile deepCopy() {
        return new Tile(this);
    }
}
