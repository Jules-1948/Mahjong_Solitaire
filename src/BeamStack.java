import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

/**
 * @author Tyler James
 * Beam Stack Search
 * Pseudo code came from: https://cdn.aaai.org/ICAPS/2005/ICAPS05-010.pdf
 */
public class BeamStack extends Timeout {
    private int w; //The width of the beam
    private long numberOfNodesSearched = 0; //Total number of search nodes explored for the run

    /**
     * Creates a beam stack search
     * @param w The width of the beam
     */
    public BeamStack(int w) {
        super(0);
        this.w = w;
    }
/**
     * Creates a beam stack search
     * @param w The width of the beam
     * @param timeout The time the program should wait before timing out and returning its result
     */
    public BeamStack(int w,long timeout) {
        super(timeout);
        this.w = w;
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

        //Sets starting value to 0
        numberOfNodesSearched = 0;

        //Stores a stack of board choices
        Stack<Board[]> boardsSavedForLayer = new Stack<>();
        Board[] firstBoardLayer = new Board[w];
        firstBoardLayer[0] = startingBoard;
        boardsSavedForLayer.push(firstBoardLayer);

        Board bestBoardFoundDuringRun = startingBoard;
        int worstScore = -1;
        //Initalizes fringe with possible values of starting board
        while(!boardsSavedForLayer.isEmpty()) {
            //Verbage
            if (verbosity >= 1) {
                System.out.println("Stack Size: " + boardsSavedForLayer.size());
            }
            //Breaks the loop if it runs too long
            if (timedout) {
                break;
            }
            //Gets top layers nodes/boards
            Board[] layersBoards = boardsSavedForLayer.peek();
            
            //Create fringe and add next possible board states
            ArrayList<Board> fringe = new ArrayList<>();
            for (int i = 0; i < w; i++) {
                if (layersBoards[i] != null) {
                    ArrayList<Board> futureBoards = getFutureBoards(layersBoards[i]);
                    int tempWorstScore = worstScore;
                    //Only adds future board states that havent been previously checked
                    futureBoards.removeIf(board -> scoreBoard(board) <= tempWorstScore);
                    fringe.addAll(futureBoards);
                }
            }

            //Verage
            if (verbosity >= 2) {
                System.out.println("Fringe size: " + fringe.size());
            }
            
            //Base case, if fringe is empty then must be bad nodes, so back up a step in search
            if (fringe.isEmpty()) {
                worstScore = getHighestScoredBoard(boardsSavedForLayer.pop());
                continue;
            }

            // Sort the board
            Collections.sort(fringe, (o1, o2) -> Integer.compare(scoreBoard(o1), scoreBoard(o2)));
            Collections.reverse(fringe);

            //Save w best new board nodes
            Board[] wBoards = new Board[w];
            for (int i = 0; i < w; i++) {
                if (fringe.size() >= 1) {

                    //Get best board from fringe
                    Board bestBoard = fringe.remove(0);
                    numberOfNodesSearched++;

                    //Print verbage
                    if (verbosity >= 2) {
                        System.out.println("BoardScore: " + scoreBoard(bestBoard));
                    }
                    if (verbosity >= 2) {
                        System.out.println("Best board[i] where i = " + i);
                        System.out.println("Boards overall depth is: " + bestBoard.getDepth());
                        System.out.println("Remaining tile count: " + bestBoard.getExistentTileCount());
                    }
                    if (verbosity >= 3) {
                        System.out.println(bestBoard);
                    }

                    //Check to see if it would find a goal state and return if so
                    if (bestBoard.getExistentTileCount() == 0) {
                        cancelAndResetClock();
                        if (verbosity >= 1) {
                            System.out.println("Solution Found");
                            System.out.println(bestBoard);
                        } 
                        return bestBoard;
                    }

                    //Save w best boards
                    wBoards[i] = bestBoard;
                    if (scoreBoard(bestBoard) < scoreBoard(bestBoardFoundDuringRun) ) {
                        bestBoardFoundDuringRun = bestBoard;
                    }
                } 
            } 
            //Add new saved layer to stack
            boardsSavedForLayer.push(wBoards);
        }

        cancelAndResetClock();
        if (verbosity >= 1) {
            System.out.println("No solution found");
            System.out.println(bestBoardFoundDuringRun);
        } 
        //Would normally return null, but we are choosing to return the best that we found from a run
        return bestBoardFoundDuringRun;
    }

    /**
     * Checks to see if all of the vaues in boards is null
     * @param boards
     * @return
     */
    private boolean checkIfAllNull(Board[] boards) {
        for (Board board: boards) {
            if (board != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns a score heuristic for the algorithm
     * Note: This is a relatively simple scoring system but it proved to be the most acurate
     * @param board
     * @return
     */
    private int scoreBoard(Board board) {
        int score = 0;
        score += getRemovablePairs(board).size();

        return score;
    }

    /**
     * Returns the highest board score (which is the worst board)
     * @param boards
     * @return
     */
    private int getHighestScoredBoard(Board[] boards) {
        Board highestScoreBoard = boards[0];
        if (highestScoreBoard == null) {
            return Integer.MAX_VALUE;
        }
        for (Board board: boards) {
            if (board != null && scoreBoard(board) > scoreBoard(highestScoreBoard)) {
                highestScoreBoard = board;
            }
        }
        return scoreBoard(highestScoreBoard);
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
        Board mySpecialBoard = startingBoard;
        int count = 0;

        //Starts timeout clock
        if (timeout > 0) {
            startTimeoutClock();
        }

        //Sets starting value to 0
        numberOfNodesSearched = 0;

        //Stores a stack of board choices
        Stack<Board[]> boardsSavedForLayer = new Stack<>();
        Board[] firstBoardLayer = new Board[w];
        firstBoardLayer[0] = startingBoard;
        boardsSavedForLayer.push(firstBoardLayer);

        Board bestBoardFoundDuringRun = startingBoard;
        int worstScore = -1;
        //Initalizes fringe with possible values of starting board
        while(!boardsSavedForLayer.isEmpty()) {
            if (verbosity >= 1) {
                System.out.println("Stack Size: " + boardsSavedForLayer.size());
            }
            Board[] layersBoards = boardsSavedForLayer.peek();

            // System.out.println("Checking boards childeren");
            
            //Create fringe and add next possible board states
            ArrayList<Board> fringe = new ArrayList<>();
            for (int i = 0; i < w; i++) {
                if (layersBoards[i] != null) {
                    ArrayList<Board> futureBoards = getFutureBoards(layersBoards[i]);
                    int tempWorstScore = worstScore;
                    futureBoards.removeIf(board -> scoreBoard(board) <= tempWorstScore);
                    fringe.addAll(futureBoards);
                }
            }
            
            if (checkIfAllNull(layersBoards) || fringe.isEmpty()) {
                // System.out.println("Backing up because no candiate childeren");
                boardsSavedForLayer.pop();
                if (boardsSavedForLayer.size() != 0) {
                    worstScore = getHighestScoredBoard(boardsSavedForLayer.peek());
                } else {
                    break;
                }
                continue;
            }

            // Sort the board
            Collections.sort(fringe, (o1, o2) -> Integer.compare(scoreBoard(o1), scoreBoard(o2)));
            Collections.reverse(fringe);

            //Save w best new board nodes
            Board[] wBoards = new Board[w];
            for (int i = 0; i < w; i++) {
                try {
                    //Get best board from fringe
                    Board bestBoard = fringe.remove(0);
                    numberOfNodesSearched++;
                    count++;

                    //Print verbage
                    if (verbosity >= 2) {
                        System.out.println("Best board[i] where i = " + i);
                        System.out.println("Boards overall depth is: " + bestBoard.getDepth());
                        System.out.println("Remaining tile count: " + bestBoard.getExistentTileCount());
                    }
                    if (verbosity >= 3) {
                        System.out.println(bestBoard);
                    }

                    //Check to see if it would find a goal state and return if so
                    if (bestBoard.getExistentTileCount() == 0) {
                        cancelAndResetClock();
                        if (verbosity >= 1) {
                            System.out.println(bestBoard);
                        } 
                        return bestBoard;
                    }

                    //Save w best boards
                    wBoards[i] = bestBoard;
                    if (scoreBoard(bestBoard) < scoreBoard(bestBoardFoundDuringRun) ) {
                        bestBoardFoundDuringRun = bestBoard;
                        if (count % 15 == 0) {
                            mySpecialBoard = bestBoard;
                        }
                    }
                } catch(Exception e) {
                    if (verbosity >= 2) {
                        System.out.println(e);
                    }
                } 
                boardsSavedForLayer.push(wBoards);
            } 
        }

        cancelAndResetClock();
        System.out.println("No solution found");
        if (verbosity >= 1) {
            System.out.println(mySpecialBoard);
        } 
        //Would normally return null, but we are choosing to return the best that we found from a run
        return mySpecialBoard;
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
