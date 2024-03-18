import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

/**
 * @author Tyler James
 * Beam Stack Search
 * Pseudo code came from: https://www.geeksforgeeks.org/introduction-to-beam-search-algorithm/
 */
public class BeamStack extends Timeout {
    private int w; //The width of the beam
    private int l; //The search depth that should be checked to find new canidate nodes
    private long numberOfNodesSearched = 0; //Total number of search nodes explored for the run

    /**
     * Creates a beam stack search
     * @param w The width of the beam
     * @param l The search depth that should be checked to find new canidate nodes
     */
    public BeamStack(int w, int l) {
        super(0);
        this.w = w;
        this.l = l;
    }
/**
     * Creates a beam stack search
     * @param w The width of the beam
     * @param l The search depth that should be checked to find new canidate nodes
     * @param timeout The time the program should wait before timing out and returning its result
     */
    public BeamStack(int w, int l, long timeout) {
        super(timeout);
        this.w = w;
        this.l = l;
    }

    /**
     * Runs Beam Stack search on the instance
     * @param startingBoard
     * @return
     */
    public Board runInstance(Board startingBoard) {
        return runInstance(startingBoard, 1);
    }
    /**
     * Runs Beam Stack search on the instance
     * @param startingBoard
     * @param verbosity How much printing should be done while running, on a scale of 1-3
     * @return
     */
    public Board runInstance(Board startingBoard, int verbosity){
        //Starts timeout clock
        if (timeout > 0) {
            startTimeoutClock();
        }

        //Stores a board for each of the paths being expanded
        Board[] boards = new Board[w];
        //Sets starting value to 0
        numberOfNodesSearched = 0;

        //Initalizes fringe with possible values of starting board
        ArrayList<Board> fringe = getFutureBoards(startingBoard);
        while(!fringe.isEmpty()) {
            // Checks if should give up searching
            if (timedout) {
                break;
            }

            // Sort the board
            Collections.sort(fringe);
            
            //Save w best new board nodes
            for (int i = 0; i < w; i++) {
                try {
                    //Get best board from fringe
                    Board bestBoard = fringe.remove(0);

                    //Check to see if it would find a goal state and return if so
                    if (bestBoard.getExistentTileCount() == 0) {
                        return bestBoard;
                    }

                    //Print verbage
                    if (verbosity >= 2) {
                        System.out.println("Best board[i] where i = " + i);
                        System.out.println("Boards overall depth is: " + bestBoard.getDepth());
                        System.out.println("Remaining tile count: " + bestBoard.getExistentTileCount());
                    }
                    if (verbosity >= 3) {
                        System.out.println(bestBoard);
                    }

                    //Save the board for future reference
                    boards[i] = bestBoard;
                } catch(Exception e) {
                    if (verbosity >= 1) {
                        System.out.println(e);
                    }
                } 
            }

            //Clear the list to add new next moves
            fringe.clear();

            for (Board board: boards) {
                try {
                    //For each board, add possible next moves to fringe utilizing dfs
                    ArrayList<Board> futureBoards = dlfs(board, 0);
                    
                    //Print verbage
                    if (verbosity >= 1) {
                        System.out.println("Future board choices return size: " + futureBoards.size());
                    }
                    fringe.addAll(futureBoards);
                } catch(Exception e) {
                    if (verbosity >= 1) {
                        System.out.println(e);
                    }
                }
            }
        } 

        cancelAndResetClock();
        if (verbosity >= 1) {
            System.out.println(boards[0]);
        } 
        return boards[0];
    }


    /**
     * Runs Beam Stack search on the instance
     * @param startingBoard
     * @param verbosity How much printing should be done while running, on a scale of 1-3
     * @return a smaller (partially completed) board for AStar to run on
     */
    public Board getSmallerBoard(Board startingBoard, int verbosity){
        System.out.println("\nGenerating a smaller board...");
        //Stores a board for each of the paths being expanded
        Board[] boards = new Board[w];
        Board mySpecialBoard = startingBoard;
        int count = 0;


        //Initalizes fringe with possible values of starting board
        ArrayList<Board> fringe = getFutureBoards(startingBoard);
        while(!fringe.isEmpty()) {
            // Sort the board
            Collections.sort(fringe);
            
            //Save w best new board nodes
            for (int i = 0; i < w; i++) {
                try {
                    //Get best board from fringe
                    Board bestBoard = fringe.remove(0);

                    //Check to see if it would find a goal state and return if so
                    if (bestBoard.getExistentTileCount() == 0) {
                        return bestBoard;
                    }

                    //Print verbage
                    if (verbosity >= 2) {
                        System.out.println("Best board[i] where i = " + i);
                        System.out.println("Boards overall depth is: " + bestBoard.getDepth());
                        System.out.println("Remaining tile count: " + bestBoard.getExistentTileCount());
                    }
                    if (verbosity >= 3) {
                        System.out.println(bestBoard);
                    }

                    //Save the board for future reference
                    boards[i] = bestBoard;
                } catch(Exception e) {
                    if (verbosity >= 1) {
                        System.out.println(e);
                    }
                } 
            }

            if (count % 20 == 0) {
                mySpecialBoard = boards[0].deepCopy();
            }
            count++;
            //Clear the list to add new next moves
            fringe.clear();

            for (Board board: boards) {
                //For each board, add possible next moves to fringe utilizing dfs
                ArrayList<Board> futureBoards = dlfs(board, 0);
                
                //Print verbage
                if (verbosity >= 1) {
                    System.out.println("Future board choices return size: " + futureBoards.size());
                }
                fringe.addAll(futureBoards);
            }
        } 

        return mySpecialBoard;
    }

    /**
     * Runs depth first search up to the depth limit for an intial board
     * @param initalBoard
     * @param depth Current depth from initial board
     * @return
     */
    private ArrayList<Board> dlfs(Board initalBoard, int depth) {
        ArrayList<Board> possibleBoards = new ArrayList<>();

        //Base case for bottom of search
        if (depth > l) return possibleBoards;

        Stack<Board> fringe = new Stack<>();
        fringe.addAll(getFutureBoards(initalBoard));
        while (!fringe.isEmpty()) {
            if (timedout) {
                break;
            }

            Board board = fringe.pop();

            ArrayList<Board> futureBoards = getFutureBoards(board);
            //Adds a board if it is an end node
            if (depth == l || futureBoards.isEmpty()) {
                possibleBoards.add(board);
            }
            possibleBoards.addAll(dlfs(board, depth + 1));
        }

        //Base case for empty fringe
        return possibleBoards;
    }

    /**
     * Gets the possible future states for a board
     * @param board
     * @return
     */
    private ArrayList<Board> getFutureBoards(Board board) {
        ArrayList<Board> futureBoards = new ArrayList<>();

        for (Tile[] pair: getRemovablePairs(board)) {
            Board newBoard = board.deepCopy();
            newBoard.addToPath(pair[0], pair[1]);
            newBoard.removeTiles(pair[0], pair[1]);
            numberOfNodesSearched++;

            futureBoards.add(newBoard);
        }

        return futureBoards;
    }

    /**
     * Finds the combinations of pairs that can be removed from a board
     * @param board
     * @return
     */
    private ArrayList<Tile[]> getRemovablePairs(Board board) {
        ArrayList<Tile> exposedTiles = board.getExposedTiles();
        
        ArrayList<Tile[]> tilePairs = new ArrayList<>();
        for(int i = 0; i < exposedTiles.size(); i++){
            for(int j = i + 1; j < exposedTiles.size(); j++ ){
                Tile tile1 = exposedTiles.get(i);
                Tile tile2 = exposedTiles.get(j);
                if(board.canRemoveTiles(tile1, tile2)){
                    Tile[] pair = {tile1, tile2};
                    tilePairs.add(pair);
                }
            }
        }

        return tilePairs; 
    }

    /**
     * Gets the number of nodes searched for the last run of search
     * @return
     */
    public long getNodesSearched() {
        return numberOfNodesSearched;
    }
}
