/**
 * @author Julia Schaming
 */
import java.util.ArrayList;
import java.util.Collections;

public class AStar extends Timeout {
    private long numberOfNodesSearched = 0; //Total number of search nodes explored for the run

    /**
     * Constructor for A* algorithm
     */
    public AStar() {
        super(0);
    }

    /**
     * Constructor for A* algorithm with timeout
     * @param timeout The time the program should wait before timing out and returning its result
     */
    public AStar(long timeout) {
        super(timeout);
    }

    public Board runInstance(Board startingNode){
        return runInstance(startingNode, 1);
    }
    
    /**
     * Pseudocode referenced from 03_AStarExample Slide from class
     * Numbered comments (Ex. 5. return searchNode) are directly from pseudocode
     * Runs A* algorithm on one instance of a Classic Board shape and prints results according to verbosity setting
     * @param verbosity - sets degree of console output 
     *                      1 = Final board 
     *                      2 = Initial board, running total of numNodesSearched, & final board
     *                      3 = Initial board, each set of tiles removed in order, running total of numNodesSearched, & final board
     */
    public Board runInstance(Board startingNode, int verbosity){
        //Starts the timeout clock
        if (timeout > 0) {
            startTimeoutClock();
        }

        // Print starting board
        if(verbosity >= 2){
            System.out.println("\nStarting board -> No tiles removed yet.");
            System.out.println(startingNode);
        }

        // Initialize variables for verbosity and analysis
        Board lastSearchedBoard = startingNode; //used to print last board at the end when verbosity == 2 or 3
        numberOfNodesSearched = 0;
        
        // 1. Initialize fringe to starting state, closed list to empty
        ArrayList<Board> fringe = new ArrayList<Board>();
        ArrayList<Board> closedList = new ArrayList<Board>();
        fringe.add(startingNode);

        // 2. While fringe is not empty:
        while(!fringe.isEmpty()){
            //Checks if should stop running
            if (timedout) {
                break;
            }
            
            // 3. searchNode <- fringe.removeFirst()
            Board searchNode = fringe.get(0);
            fringe.remove(0);
            numberOfNodesSearched++;
            lastSearchedBoard = searchNode;

            // print path to searchNode
            if(verbosity >= 2){
                System.out.println("\nnumNodesSearched: " + numberOfNodesSearched);
            }
            if(verbosity >= 3){
                ArrayList<Tile[]> currentNodePath = searchNode.getPath();
                System.out.print("\nRemaining tiles: " + searchNode.getExistentTileCount());
                for(Tile[] tilePair : currentNodePath){
                    System.out.print(" " + tilePair[0].toString() + tilePair[1].toString());
                }
                System.out.println();
            }

            // 4. if isGoal(searchNode.endingState):
            if(searchNode.getExistentTileCount() == 0){
                // Print ending board
                if(verbosity >= 1){
                    System.out.println("\nFinal board searched -> this board is the goal state.");
                    System.out.println(searchNode);
                    System.out.println("\nPath to this board: ");
                    ArrayList<Tile[]> currentNodePath = lastSearchedBoard.getPath();
                    for(Tile[] tilePair : currentNodePath){
                        System.out.print(" " + tilePair[0].toString() + tilePair[1].toString());
                    } 
                }

                // 5. return searchNode
                return (searchNode);
            }
                
            // 6. closedList.add(searchNode.endingState)
            closedList.add(searchNode);

            // 7. children <- expand(searchNode)
            ArrayList<Board> children = getChildren(searchNode);

            // 8. for each child in children:
            for(Board child : children){
                // 9. if child.endingState is in closedList: continue;
                if(isInList(child, closedList)){continue;}


                // 10. if there is a search node in fringe that ends in child.endingState:
                if(isInList(child, fringe)){
                    // 11. update fringe to contain only the better of the two paths
                    // Since nodes in the same endstate are exactly equal, it doesn't matter which we keep
                    // so we will keep the node already in the fringe
                }
                // 12. else: 
                else{
                    // 13. add child to fringe
                    fringe.add(child);
                }
            }
            // Sort queue so that best child node is first
            Collections.sort(fringe);
        }

        if(verbosity >= 1){
            System.out.println("\nFinal board searched -> no more moves remaining and goal state is unreachable.");
            System.out.println(lastSearchedBoard);
            System.out.println("\nPath to this board: ");
            ArrayList<Tile[]> currentNodePath = lastSearchedBoard.getPath();
            for(Tile[] tilePair : currentNodePath){
                System.out.print(" " + tilePair[0].toString() + tilePair[1].toString());
            } 
        }

        //Resets the clock and returns the lastSearchedBoard
        cancelAndResetClock();
        return lastSearchedBoard;
    }

    // Checks if given board is already in the given list
    private boolean isInList(Board board, ArrayList<Board> list){
        boolean containsChild = false;
        for(Board node : list){
            if(board.areEqualBoards(node)){
                containsChild = true;
                break;
            }
        }
        return containsChild;
    }

    // Creates array of all children boards given a parent board
    private ArrayList<Board> getChildren(Board searchNode){
        ArrayList<Tile[]> tilePairs = getRemovableTilePairs(searchNode);
        ArrayList<Board> children = new ArrayList<>();
        for(Tile[] child : tilePairs){
            Board childBoard = searchNode.deepCopy();
            children.add(childBoard);
            childBoard.removeTiles(child[0], child[1]);
            childBoard.addToPath(child[0], child[1]);
        }
        return children;

    }

    // Finds all unique pairs of removable tile pairs for given board
    private ArrayList<Tile[]> getRemovableTilePairs(Board board) {
        ArrayList<Tile> exposedTiles = board.getExposedTiles();
        
        ArrayList<Tile[]> tilePairs = new ArrayList<>();
        for(int i = 0; i < exposedTiles.size(); i++){
            for(int j = i + 1; j < exposedTiles.size(); j++ ){
                Tile tile1 = exposedTiles.get(i);
                Tile tile2 = exposedTiles.get(j);
                if(board.canRemoveTiles(tile1, tile2)){
                    tilePairs.add(new Tile[]{tile1, tile2});
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