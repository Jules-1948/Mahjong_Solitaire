import java.util.ArrayList;

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

    public BeamStack(int w) {
        this.w = w;
    }

    public ArrayList<Tile[]> runInstance(Board startingBoard, int verbosity){
        //Stores a board for each of the paths being expanded
        Board[] boards = new Board[w];

        //Initalizes openList with possible values
        ArrayList<Pair> openList = getRemovableTilePairs(startingBoard);
        while(!openList.isEmpty()) {

            // Get best pair, remove it from its board, and then save its board to be expanded later
            for (int i = 0; i < w; i++) {
                try {
                    //Get best pair from openList
                    Pair bestPair = getBestPair(openList);
                    openList.remove(bestPair);

                    //Apply move to the pair's board
                    Board newBoard = bestPair.getBoard().deepCopy();
                    newBoard.addToPath(bestPair.getEntry1(), bestPair.getEntry2());
                    newBoard.removeTiles(bestPair.getEntry1(), bestPair.getEntry2());

                    //Check to see if it would cause a success
                    if (newBoard.getExistentTileCount() == 0) {
                        return newBoard.getPath();
                    }

                    //Save the board for future reference
                    boards[i] = newBoard;
                } catch(Exception e) {
                    // System.out.println(e);
                } 
            }

            //Clear the list to add new Pair choices for the new boards
            openList.clear();

            for (Board board: boards) {
                //For each board add possible next moves to open list
                ArrayList<Pair> removableTilePairs = getRemovableTilePairs(board);
                openList.addAll(removableTilePairs);
            }
        } 

        //Algoithm failed to find a path, so finds largest path, and returns that
        Board bestBoard = boards[0];
        for (Board board: boards){
            if (board.getPath().size() > bestBoard.getPath().size()) {
                bestBoard = board;
            }
        }

        return bestBoard.getPath();
    }

    private ArrayList<Pair> getRemovableTilePairs(Board board) {
        ArrayList<Tile> exposedTiles = board.getExposedTiles();
        
        ArrayList<Pair> tilePairs = new ArrayList<>();
        for(int i = 0; i < exposedTiles.size(); i++){
            for(int j = i + 1; j < exposedTiles.size(); j++ ){
                Tile tile1 = exposedTiles.get(i);
                Tile tile2 = exposedTiles.get(j);
                if(board.canRemoveTiles(tile1, tile2)){
                    tilePairs.add(new Pair(tile1, tile2, board));
                }
            }
        }

        return tilePairs;
    }

    private Pair getBestPair(ArrayList<Pair> pairs) {
        Pair pair1 = pairs.get(0);

        for (Pair pair2: pairs){
            if (getPairScore(pair2) > getPairScore(pair1)) {
                pair1 = pair2;
            }
        }

        return pair1;
    }

    //This is the heuristic for this beam search
    private int getPairScore(Pair pair) {
        int tile1Score = 0;
        int tile2Score = 0;

        tile1Score += pair.getEntry1().getZLayer();
        tile2Score += pair.getEntry1().getZLayer();

        if (pair.getEntry1().getSuit() == 5 || pair.getEntry1().getSuit() == 6) {
            tile1Score += 2;
        }
        if (pair.getEntry2().getSuit() == 5 || pair.getEntry2().getSuit() == 6) {
            tile2Score += 2;
        }

        return tile1Score + tile2Score;
    }

    

    private void printBoardVerbage(Board board, int verbosity) {
        if (verbosity != 0) {
            if (verbosity >= 2) {
                System.out.println("Selected a board");
                System.out.println("Its path length is " + board.getPath().size());
                System.out.println("Its current depth is " + board.getDepth());
                System.out.println("Its current remaining tile count is " + board.getTiles().size());
            }
            if (verbosity >= 3) {
                System.out.println("It looks like this: ");
                System.out.println(board);
            }
        }
    }
}
