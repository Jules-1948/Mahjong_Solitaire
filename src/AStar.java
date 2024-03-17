/**
 * @author Julia Schaming
 */
import java.util.ArrayList;

public class AStar {
    
    /**
     * Pseudocode referenced from 03_AStarExample Slide from class
     * Numbered comments (Ex. 5. return searchNode) are directly from pseudocode
     * Runs A* algorithm on one instance of a Classic Board shape and prints results according to verbosity setting
     * @param verbosity - sets degree of console output 
     *                      1 = final result 
     *                      2 = Initial board && each set of tiles removed in order && final board
     */
    private ArrayList<Tile> runInstance(int verbosity, Board board){
        if(verbosity == 2){System.out.println(board);}
        
        // 1. Initialize fringe to starting state, closed list to empty
        ArrayList<Board> fringe = new ArrayList<Board>();
        fringe.add(board);
        ArrayList<Board> closedList = new ArrayList<Board>();
        ArrayList<Tile> pathToGoal = new ArrayList<>();

        // 2. While fringe is not empty:
        while(!fringe.isEmpty()){
            // 3. searchNode <- fringe.removeFirst()
            Board searchNode = fringe.get(0);
            fringe.remove(0);

            // 4. if isGoal(searchNode.endingState):
            if(searchNode.getTiles().isEmpty()){
                // 5. return searchNode
                return pathToGoal;

            }
                
            // 6. closedList.add(searchNode.endingState)
            // 7. children <- expand(searchNode)
            // 8. for each child in children:
                // 9. if child.endingState is in closedList: continue;
                // 10. if there is a search node in fringe that ends in child.endingState:
                    // 11. update fringe to contain only the better of the two paths
                // 12. else:
                    // 13. add child to fringe


        }

        return null;
    }



    // All public Constructors

    /**
     * Runs classic board with default verbosity of 1
     * @param seed -- seeds random generator to create unique game boards
     */
    public ArrayList<Tile> runClassicInstance(long seed){
        Board classic = new Board(seed, "classic");
        return runInstance(1, classic);
    }
    
    /**
     * Runs classic board with default verbosity of 1
     * @param seed -- seeds random generator to create unique game boards
     * @param verbosity -- sets degree of console output 
     */
    public ArrayList<Tile> runClassicInstance(long seed, int verbosity){
        Board classic = new Board(seed, "classic");
        return runInstance(verbosity, classic);
    }

    /**
     * Runs pyramid board with default verbosity of 1
     * @param seed -- seeds random generator to create unique game boards
     */
    public ArrayList<Tile> runPyramidInstance(long seed){
        Board pyramid = new Board(seed, "pyramid");
        return runInstance(1, pyramid);
    }

    /**
     * Runs pyramid board with default verbosity of 1
     * @param seed -- seeds random generator to create unique game boards
     * @param verbosity -- sets degree of console output 
     */
    public ArrayList<Tile> runPyramidInstance(long seed, int verbosity){
        Board pyramid = new Board(seed, "pyramid");
        return runInstance(verbosity, pyramid);
    }

    /**
     * Runs fish board with default verbosity of 1
     * @param seed -- seeds random generator to create unique game boards
     */
    public ArrayList<Tile> runFishInstance(long seed){
        Board fish = new Board(seed, "fish");
        return runInstance(1, fish);
    }

    /**
     * Runs fish board with default verbosity of 1
     * @param seed -- seeds random generator to create unique game boards
     * @param verbosity -- sets degree of console output ,
     */
    public ArrayList<Tile> runFishInstance(long seed, int verbosity){
        Board fish = new Board(seed, "fish");
        return runInstance(verbosity, fish);
    }
}