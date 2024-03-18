/**
 * @author Julia Schaming
 */
import java.util.ArrayList;
import java.util.Collections;

public class AStar {
    
    /**
     * Pseudocode referenced from 03_AStarExample Slide from class
     * Numbered comments (Ex. 5. return searchNode) are directly from pseudocode
     * Runs A* algorithm on one instance of a Classic Board shape and prints results according to verbosity setting
     * @param verbosity - sets degree of console output 
     *                      1 = Final result 
     *                      2 = Initial board && final board
     *                      3 = Initial board && each set of tiles removed in order && final board
     */
    private ArrayList<Tile[]> runInstance(int verbosity, Board startingNode){
        float startTime = System.currentTimeMillis();
        System.out.printf("Start Time: %.02f ms\n", startTime);

        // Print starting board
        if(verbosity == 2 || verbosity == 3){
            System.out.println("\nStarting board -> No tiles removed yet.");
            System.out.println(startingNode);
        }
        
        // 1. Initialize fringe to starting state, closed list to empty
        ArrayList<Board> fringe = new ArrayList<Board>();
        ArrayList<Board> closedList = new ArrayList<Board>();
        Board lastSearchedBoard = startingNode; //used to print last board at the end when verbosity == 2 or 3
        fringe.add(startingNode);

        // 2. While fringe is not empty:
        while(!fringe.isEmpty()){
            // 3. searchNode <- fringe.removeFirst()
            Board searchNode = fringe.get(0);
            fringe.remove(0);
            lastSearchedBoard = searchNode;

            // print path to searchNode
            if(verbosity == 2){
                ArrayList<Tile[]> currentNodePath = searchNode.getPath();
                if(currentNodePath.isEmpty()){
                    System.out.println("\nBelow lists the path of removed tiles from each searchNode");
                }
                System.out.print("\nRemaining tiles: " + searchNode.getExistentTileCount());
                for(Tile[] tilePair : currentNodePath){
                    System.out.print(" " + tilePair[0].toString() + tilePair[1].toString());
                }
                System.out.println();
            }

            // 4. if isGoal(searchNode.endingState):
            if(searchNode.getExistentTileCount() == 0){
                // Print ending board
                System.out.println("\nFinal board searched -> this board is the goal state.");
                System.out.println(searchNode);
                System.out.println("\nPath to this board: " + searchNode.getPath());
                float endTime = System.currentTimeMillis();
                System.out.printf("Time taken: %.02f ms\n", (endTime-startTime));

                // 5. return searchNode
                return (searchNode.getPath());
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

        System.out.println("\nFinal board searched -> no more moves remaining and goal state is unreachable.");
        System.out.println(lastSearchedBoard);
        System.out.println("\nPath to this board: " + lastSearchedBoard.getPath());
        float endTime = System.currentTimeMillis();
        System.out.printf("Time taken: %.02f ms\n", (endTime-startTime));
        return lastSearchedBoard.getPath();
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



    // All public Constructors

    /**
     * Runs classic board with default verbosity of 1
     * @param seed -- seeds random generator to create unique game boards
     */
    public ArrayList<Tile[]> runClassicInstance(long seed){
        Board classic = new Board(seed, "classic");
        return runInstance(1, classic);
    }
    
    /**
     * Runs classic board with default verbosity of 1
     * @param seed -- seeds random generator to create unique game boards
     * @param verbosity -- sets degree of console output 
     */
    public ArrayList<Tile[]> runClassicInstance(long seed, int verbosity){
        Board classic = new Board(seed, "classic");
        return runInstance(verbosity, classic);
    }

    /**
     * Runs pyramid board with default verbosity of 1
     * @param seed -- seeds random generator to create unique game boards
     */
    public ArrayList<Tile[]> runPyramidInstance(long seed){
        Board pyramid = new Board(seed, "pyramid");
        return runInstance(1, pyramid);
    }

    /**
     * Runs pyramid board with default verbosity of 1
     * @param seed -- seeds random generator to create unique game boards
     * @param verbosity -- sets degree of console output 
     */
    public ArrayList<Tile[]> runPyramidInstance(long seed, int verbosity){
        Board pyramid = new Board(seed, "pyramid");
        return runInstance(verbosity, pyramid);
    }

    /**
     * Runs fish board with default verbosity of 1
     * @param seed -- seeds random generator to create unique game boards
     */
    public ArrayList<Tile[]> runFishInstance(long seed){
        Board fish = new Board(seed, "fish");
        return runInstance(1, fish);
    }

    /**
     * Runs fish board with default verbosity of 1
     * @param seed -- seeds random generator to create unique game boards
     * @param verbosity -- sets degree of console output ,
     */
    public ArrayList<Tile[]> runFishInstance(long seed, int verbosity){
        Board fish = new Board(seed, "fish");
        return runInstance(verbosity, fish);
    }
}