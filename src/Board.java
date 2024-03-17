/**
 * @author Julia Schaming
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Board {
    // Board Variables
    final private int numDimensions = 3; // width, length, depth -- used only in contructor
    private String boardType;
    private int boardX; // x dimension of the board
    private int boardY; // y dimension of the board
    private int boardZ; // z dimension of the board -- 0 is the bottom layer
    private Tile[][][] board; // represents actual game board

    // Tile Generation Variables
    final private int suitNum = 7; // total number of suits used in the game
    final private int totalTileCount = 144; // total number of tiles in the start of the game
    final private int[] facesPerSuit = {9, 9, 9, 4, 3, 4, 4}; // number of faces in each suit
                        // index num above corresponds with string order of suits in Tile class
    final private int[] multiplesOfSuit = {4, 4, 4, 4, 4, 1, 1}; // how many multiples of each suit
    private int[] suitCounts; // number of tiles for each suit remaining to be created
    private int[][] faceCounts; // number of faces within each suit remaining to be created
    private int[][] boardTileSpaces; // array of 3D locations for tiles(left corner only)
    private ArrayList<Tile> tiles; // arrayList of all tiles currently in game
    private int existentTileCount; // number of existant and in play tiles


    /**
     * Creates an instance of a mahjong solitaire game board
     * @param seed -- used for the Random number generator used for tile placement
     * @param boardType -- options are "classic" "pyramid" and "fish". Anything else will default to classic.
     */
    public Board(long seed, String boardType){
        // Set dimensions for given board
        if(boardType.toLowerCase().equals("pyramid")){
            //needs updated
            this.boardType = "pyramid";
            boardX = -1;
            boardY = -1;
            boardZ = -1;
        } else if(boardType.toLowerCase().equals("fish")){
            //needs updated
            this.boardType = "fish";
            boardX = -1;
            boardY = -1;
            boardZ = -1;
        } else{
            this.boardType = "classic";
            boardX = 30;
            boardY = 16;
            boardZ = 5;
        }

        // Initialize values according to static rules, suit counts, and face counts of the game
        board = new Tile[boardX][boardY][boardZ];
        existentTileCount = 0;
        boardTileSpaces = new int[totalTileCount][numDimensions];
        tiles = new ArrayList<>();

        suitCounts = new int[suitNum];
        for(int i=0; i<suitNum; i++){suitCounts[i] = facesPerSuit[i]*multiplesOfSuit[i];}

        faceCounts = new int[suitNum][facesPerSuit[0]];
        for(int i=0; i<suitNum; i++){ //iterates through suits
            for(int j=0; j<facesPerSuit[i]; j++){ //iterates through faces
                faceCounts[i][j] = multiplesOfSuit[i];
            }
        }

        // Fetch tile locations from file
        initializeTileSpaces();

        // Create and place all tiles for board
        Random rand = new Random(seed);
        for(int[] location: boardTileSpaces){
            int suit = getNextSuit(rand);
            int face = getNextFace(suit, rand);
            createNewTile(location, suit, face);
        }
    }

    //Used for deep copying of board
    private Board(Board parentBoard) {
        boardX = parentBoard.boardX;
        boardY = parentBoard.boardY;
        boardZ = parentBoard.boardZ;
        existentTileCount = 0;
        tiles = new ArrayList<>();
        boardTileSpaces = new int[totalTileCount][numDimensions]; //not really used in this constructor
        boardType = parentBoard.boardType; //also not really used in this constructor
        board = new Tile[boardX][boardY][boardZ];
        for(int z=0; z<boardZ; z++){
            for(int y=0; y<boardY-1; y++){
                for(int x=0; x<boardX-1; x++){
                    if(parentBoard.board[x][y][z] != null && board[x][y][z] == null){
                        //this method also fills tiles array
                        setBoardTile(x, y, z, parentBoard.board[x][y][z].deepCopy());
                    }
                }
            }
        }


        suitCounts = new int[suitNum];
        for(int i=0; i<suitNum; i++){suitCounts[i] = facesPerSuit[i]*multiplesOfSuit[i];}

        faceCounts = new int[suitNum][facesPerSuit[0]];
        for(int i=0; i<suitNum; i++){ //iterates through suits
            for(int j=0; j<facesPerSuit[i]; j++){ //iterates through faces
                faceCounts[i][j] = multiplesOfSuit[i];
            }
        }
    }

    // returns whether given tile is an exposed(playable) tile
    public boolean isExposed(Tile tile){
        int x = tile.getTopLeftX();
        int y = tile.getTopLeftY();
        int z = tile.getZLayer();

        int sideExposed = 0;
        boolean sides = false;
        Boolean above = false;

        // check if vertically exposed
        if(z+1 < boardZ){
            if(y+1 < boardY && board[x][y][z+1] == null && board[x+1][y][z+1] == null && board[x][y+1][z+1] == null && board[x+1][y+1][z+1] == null){
                above = true;
            }
        } else {above = true;}

        // check if horizontally exposed
        if(x-1 >= 0){
            if(board[x-1][y][z] == null){sideExposed++;} //top left
            if(y+1 >= 0){
                if(board[x-1][y+1][z] == null){sideExposed++;} //bottom left
            }
        } else if(x == 0){sideExposed = 2;}
        if(sideExposed == 2){sides = true;}

        sideExposed = 0; //both exposed results must be on the same side
        if(x+2 < boardX){
            if(board[x+2][y][z] == null){sideExposed++;} //top right
            if(y+1 < boardY){
                if(board[x+2][y+1][z] == null){sideExposed++;} //bottom right
            }
        } else if(x+2 == boardX){sideExposed = 2;}
        if(sideExposed == 2){sides = true;}
        
        // Must have all four above exposed as well as both sides on at least one side
        return sides && above;
    }

    // removes tile from board if exposed
    public boolean removeTiles(Tile tile1, Tile tile2){
        // tiles are not exposed and cannot be removed
        if(!isExposed(tile1) || !isExposed(tile2)){return false;}
        // check that tiles are not the same instance
        if(tile1 == tile2){return false;}
        // check if tiles match
        if(!tile1.getUniqueString().equals(tile2.getUniqueString())){return false;}

        setBoardNull(tile1);
        setBoardNull(tile2);

        return true;
    }

    // getter for existentTileCount
    public int getExistentTileCount(){
        return existentTileCount;
    }

    // getter for all tiles
    public ArrayList<Tile> getTiles(){
        return tiles;
    }

    // returns list of all exposed(playable) tiles
    public ArrayList<Tile> getExposedTiles(){
        ArrayList<Tile> exposed = new ArrayList<>();

        for(Tile tile: tiles){
            if(isExposed(tile)){
                exposed.add(tile);
            }
        }

        return exposed;
    }

    // prints current game board layer by layer
    @Override
    public String toString(){
        String boardString = "\n\n";

        for(int z=0; z<boardZ; z++){
            boardString = boardString + "Layer " + z + "\n";

            for(int y=0; y<boardY; y++){
                for(int x=0; x<boardX; x++){
                    if(board[x][y][z] == null){
                        boardString = boardString + "|N";
                    } else {
                        if(isExposed(board[x][y][z])){
                            boardString = boardString + "\u001B[41m" + "|" + "\u001B[0m" + board[x][y][z].toString();
                        } else{
                            boardString = boardString + "|" + board[x][y][z].toString();
                        }
                    }
                }
                boardString = boardString + "|\n";
            }
        }

        return boardString;
    }

    public Board deepCopy() {
        return new Board(this);
    }





    //Below are all private helper functions mostly to create the board instance



    // Creates a new tile and stores it in a valid location on the board
    private void createNewTile(int[] location, int suit, int face){
        int x = location[0];
        int y = location[1];
        int z = location[2];

        // Return if not a valid location
        if(location.length != 3){
            System.err.println("Invalid location in list. Tile not created.");
            return;
        } else if (x >= boardX || y >= boardY || z >= boardZ){
            System.err.println("Tile location out of bounds. Tile not created.");
            return;
        }

        // Create Tile and place in all 4 board coordinates
        Tile newTile = new Tile(x, y, z, suit, face);
        setBoardTile(x, y, z, newTile);
        existentTileCount++;
    }

    // gets a face within a given suit for which not all tiles have been created
    private int getNextFace(int suit, Random rand){
        boolean valid = false;
        int nextFace = -1;
        while(!valid){
            nextFace = rand.nextInt(0, facesPerSuit[suit]);
            if(faceCounts[suit][nextFace] > 0){
                valid = true;
                faceCounts[suit][nextFace]--;
            }
        }

        return nextFace;
    }

    // gets a suit for which not all tiles have yet been created
    private int getNextSuit(Random rand){
        boolean valid = false;
        int nextSuit = -1;
        while(!valid){
            nextSuit = rand.nextInt(0, suitNum);
            if(suitCounts[nextSuit] > 0){
                valid = true;
                suitCounts[nextSuit]--;
            }
        }

        return nextSuit;
    }

    // initialize double int array of all tile locations to be filled with a tile
    private void initializeTileSpaces(){
        try{
            File locationInput;
            if(boardType.equals("pyramid")){
                locationInput = new File("src\\BoardTileLocations\\PyramidBoard.txt");
            } else if(boardType.equals("fish")){
                locationInput = new File("src\\BoardTileLocations\\FishBoard.txt");
            }else{
                locationInput = new File("src\\BoardTileLocations\\ClassicBoard.txt");
            }
            Scanner spaceScnr = new Scanner(locationInput);

            int readLoctions = 0;
            while(spaceScnr.hasNextInt()){
                // want all coordinates to be 0 indexed
                int x = spaceScnr.nextInt() - 1;
                int y = spaceScnr.nextInt() - 1;
                int z = spaceScnr.nextInt() - 1;
                boardTileSpaces[readLoctions] = new int[]{x, y, z};
                readLoctions++;
            }

            spaceScnr.close();

        } catch(FileNotFoundException e){
            System.err.println("Tile location file not found");
        }

    }

    private void setBoardTile(int x, int y, int z, Tile tile){
        board[x][y][z] = tile;
        board[x+1][y][z] = tile;
        board[x][y+1][z] = tile;
        board[x+1][y+1][z] = tile;

        tiles.add(tile);
    }

    private void setBoardNull(Tile tile){
        
        int x = tile.getTopLeftX();
        int y = tile.getTopLeftY();
        int z = tile.getZLayer();

        board[x][y][z] = null;
        board[x+1][y][z] = null;
        board[x][y+1][z] = null;
        board[x+1][y+1][z] = null;

        tiles.remove(tile);
    }
}
