import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

/**
 * @author Tyler James
 * https://www.geeksforgeeks.org/introduction-to-beam-search-algorithm/
 */
public class BeamStack {

    // Your solver method or class should have a single verbosity parameter that defaults to showing the final result
    // only but can be set to another value (or values) to print out more details about the steps the algorithm is taking.
    // For example, for verbosity=1, my CSP solver might print out every time it tries to assign a value to a variable, and
    // with verbosity=2, it prints out each domain of the variables in addition.

    // Comment the algorithm code liberally. Include a link to the paper or reference you used for the pseudocode of
    // the algorithm e.g., “Used pseudocode from Figure 1 of https://cdn.aaai.org/ICAPS/2005/ICAPS05-010.pdf”).
    // Your comments should note the connections between the code and the pseudocode, like “This block
    // implements finding the best nodes in the open list (line 1 of the pseudocode)”.
    private int w;
    private int l;

    public BeamStack(int w, int l) {
        this.w = w;
        this.l = l;
    }

    public Board runInstance(Board startingBoard, int verbosity){
        //Stores a board for each of the paths being expanded
        Board[] boards = new Board[w];

        //Initalizes openList with possible values
        ArrayList<Board> fringe = getFutureBoards(startingBoard);
        // printBoardVerbage(startingBoard, verbosity);
        while(!fringe.isEmpty()) {
            // Get best board, remove it from its board, and then save its board to be expanded later
            Collections.sort(fringe);
            for (int i = 0; i < w; i++) {
                try {
                    //Get best board from openList
                    Board bestBoard = fringe.remove(0);

                    //Check to see if it would cause a success
                    if (bestBoard.getExistentTileCount() == 0) {
                        return bestBoard;
                    }

                    //Save the board for future reference
                    boards[i] = bestBoard;
                } catch(Exception e) {
                    if (verbosity >= 1) {
                        System.out.println(e);
                    }
                } 
            }

            //Clear the list to add new Pair choices for the new boards
            fringe.clear();

            for (Board board: boards) {
                //For each board add possible next moves to openList utilizing dfs
                ArrayList<Board> futureBoards = dfs(board, 0);
                System.out.println("future Boards return size: " + futureBoards.size());
                fringe.addAll(futureBoards);
            }
        } 

        // if (verbosity >= 1) {
        //     System.out.println("A soultion was found!");
        // }
        // printBoardVerbage(startingBoard, verbosity);
        return boards[0];
    }

    private ArrayList<Board> dfs(Board initalBoard, int depth) {
        ArrayList<Board> possibleBoards = new ArrayList<>();

        //Base case for bottom of search
        if (depth > l) return possibleBoards;

        Stack<Board> fringe = new Stack<>();
        fringe.addAll(getFutureBoards(initalBoard));
        while (!fringe.isEmpty()) {
            Board board = fringe.pop();

            ArrayList<Board> futureBoards = getFutureBoards(board);
            // System.out.println("Board fringe size: " + fringe.size());
            //Adds a board if it is an end node
            if (depth == l || futureBoards.isEmpty()) {
                possibleBoards.add(board);
            }
            possibleBoards.addAll(dfs(board, depth + 1));
        }

        //Base case for empty fringe
        return possibleBoards;
    }

    private ArrayList<Board> getFutureBoards(Board board) {
        ArrayList<Board> futureBoards = new ArrayList<>();

        for (Tile[] pair: getRemovablePairs(board)) {
            Board newBoard = board.deepCopy();
            newBoard.addToPath(pair[0], pair[1]);
            newBoard.removeTiles(pair[0], pair[1]);

            futureBoards.add(newBoard);
        }

        return futureBoards;
    }

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

    private void printBoardVerbage(Board board, int verbosity) {
        if (verbosity != 0) {
            if (verbosity >= 1) {
                System.out.println("This is a board");
                System.out.println("Its path length is " + board.getPath().size());
            }
            if (verbosity >= 2) {
                System.out.println("Its current board depth is " + board.getDepth());
                System.out.println("Its current remaining tile count is " + board.getExistentTileCount());
            }
            if (verbosity >= 3) {
                System.out.println("It looks like this: ");
                System.out.println(board);
            }
        }
    }
}
