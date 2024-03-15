import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class ClassicBoard {
    // Board Variables
    private int boardX; // x dimension of the board
    private int boardY; // y dimension of the board
    private int boardZ; // z dimension of the board -- 0 is the bottom layer
    private Tile[][][] board; // represents actual game board

    // Tile Generation Variables
    private int suitNum; // total number of suits used in the game
    private int existentTileCount; // number of existant and in play tiles
    final private int[] facesPerSuit = {9, 9, 9, 4, 3, 4, 4}; // number of faces in each suit
                        // index num above corresponds with string order of suits in Tile class
    final private int[] multiplesOfSuit = {4, 4, 4, 4, 4, 1, 1}; // how many multiples of each suit
    private int[] suitCounts; // number of tiles for each suit remaining to be created
    private int[][] faceCounts; // number of faces within each suit remaining to be created
    private int[][] classicBoardTileSpaces; //array of 3D locations for tiles(left corner only)
    
    public ClassicBoard(long seed){
        // Initialize all values according to static rules, tile counts, suit counts, and board layout of the game
        boardX = 30;
        boardY = 16;
        boardZ = 5;
        board = new Tile[boardX][boardY][boardZ];
        suitNum = 7;
        existentTileCount = 0;
        classicBoardTileSpaces = new int[144][3];

        suitCounts = new int[suitNum];
        for(int i=0; i<suitNum; i++){suitCounts[i] = facesPerSuit[i]*multiplesOfSuit[i];}

        faceCounts = new int[suitNum][facesPerSuit[0]];
        for(int i=0; i<suitNum; i++){ //iterates through suits
            for(int j=0; j<facesPerSuit[i]; j++){ //iterates through faces
                faceCounts[i][j] = multiplesOfSuit[i];
            }
        }

        // Prep board with nulls and fetch tile locations from file
        initializeNullBoard();
        initializeTileSpaces();

        // Create and place all tiles for board
        Random rand = new Random(seed);
        for(int[] location: classicBoardTileSpaces){
            int suit = getNextSuit(rand);
            int face = getNextFace(suit, rand);
            createNewTile(location, suit, face);
        }

        System.out.println(toString());

    }

    public void isExposed(){
        
    }

    public void removeTile(){

    }

    // getter for existentTileCount
    public int getExistentTileCount(){
        return existentTileCount;
    }

    @Override
    public String toString(){
        String boardString = "Listed bottom layer to top:";

        for(int z=0; z<boardZ; z++){
            boardString = boardString + "\n\nLayer " + z + "\n";

            for(int y=0; y<boardY; y++){
                for(int x=0; x<boardX; x++){
                    if(board[x][y][z] == null){
                        boardString = boardString + "|N";
                    } else {
                        boardString = boardString + "|" + board[x][y][z].toString();
                    }
                }
                boardString = boardString + "|\n";
            }
        }

        return boardString;
    }







    //Below are all helper functions to create the board instance



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
        Tile newTile = new Tile(x, y, suit, face);
        board[x][y][z] = newTile;
        board[x+1][y][z] = newTile;
        board[x][y+1][z] = newTile;
        board[x+1][y+1][z] = newTile;

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
            File locationInput = new File("src\\BoardTileLocations\\ClassicBoard.txt");
            Scanner spaceScnr = new Scanner(locationInput);

            int readLoctions = 0;
            while(spaceScnr.hasNextInt()){
                // want all coordinates to be 0 indexed
                int x = spaceScnr.nextInt() - 1;
                int y = spaceScnr.nextInt() - 1;
                int z = spaceScnr.nextInt() - 1;
                classicBoardTileSpaces[readLoctions] = new int[]{x, y, z};
                readLoctions++;
            }

            spaceScnr.close();

        } catch(FileNotFoundException e){
            System.err.println("Tile location file not found");
        }

    }

    // initialize board to null values
    private void initializeNullBoard(){
        for (int x = 0; x < boardX; x++){
            for(int y = 0; y < boardY; y++){
                for(int z = 0; z < boardZ; z++){
                    board[x][y][z] = null;
                }
            }
        }
    }
}
