public class ClassicBoard {
    // Board Variables
    private int boardX = 30; // x dimension of the board
    private int boardY = 16; // y dimension of the board
    private int boardZ = 5; // z dimension of the board -- 0 is the bottom layer
    private Tile[][][] board = new Tile[boardX][boardY][boardZ]; //x, y, z

    // Tile Generation Variables
    private int suitNum = 7;
    private int[] faceCounts = {9, 9, 9, 4, 3, 4, 4}; // index num corresponds with string order of suits in Tile.java
    private int existantTileCount;
    private int[][] classicBoardTileSpaces = { //array of 3D locations for tiles(left corner only)
        {1, 1, 1},
        {1, 2, 1},
    };
        
    
    public ClassicBoard(){
        existantTileCount = 0;
        initializeNullBoard();
        //readClassicBoardTileSpaces();

        // Create and place all tiles for board
        for(int[] location: classicBoardTileSpaces){
            int suit = getNextSuit();
            int face = getNextFace(suit);
            createNewTile(location, suit, face);
        }
    }

    private void createNewTile(int[] location, int suit, int face){
        // Return if not a valid location
        if(location.length != 3){
            System.out.println("invalid location in list. Tile not created");
            return;
        } else if (location[0] >= boardX || location[1] >= boardY || location[2] >= boardZ){
            System.out.println("tile location larger than board index");
            return;
        }

        // Create Tile and place in board
        Tile newTile = new Tile(location[0], location[1], suit, face);
        board[location[0]][location[1]][location[2]] = newTile;
        existantTileCount++;
    }

    private int getNextFace(int suit){

        return 0;
    }

    private int getNextSuit(){

        return 0;
    }

    private void initializeNullBoard(){
        // initialize board to null values
        for (int x = 0; x < boardX; x++){
            for(int y = 0; y < boardY; y++){
                for(int z = 0; z < boardZ; z++){
                    board[x][y][z] = null;
                }
            }
        }
    }

    public int getExistantTileCount(){
        return existantTileCount;
    }

}
