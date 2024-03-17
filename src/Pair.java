public class Pair {
    private Tile entry1;
    private Tile entry2;
    private Board origin;

    public Pair(Tile entry1, Tile entry2, Board origin) {
        this.entry1 = entry1;
        this.entry2 = entry2;
        this.origin = origin;
    }

    public Tile getEntry1() {
        return entry1;
    }

    public Tile getEntry2() {
        return entry2;
    }

    public Board getBoard() {
        return origin;
    }

    public boolean contains(Tile tile) {
        return entry1 == tile || entry2 == tile;
    }
}
