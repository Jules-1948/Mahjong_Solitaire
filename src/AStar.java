/**
 * @author Julia Schaming
 */
public class AStar {

    
    // Runs the A* algorithm -- called from constructors

    /**
     * Runs A* algorithm on one instance of a Classic Board shape and prints results according to verbosity setting
     * @param verbosity - sets degree of console output 
     *                      1 = final result 
     *                      2 = Initial board && each set of tiles removed in order && final board
     */
    private void runInstance(int verbosity, Board board){
        Board boardClone = board.deepCopy();

        boolean noneRemoved = true;
        while(!boardClone.getExposedTiles().isEmpty() && noneRemoved){
            noneRemoved = false;
            for(Tile tile1: boardClone.getExposedTiles()){
                for(Tile tile2: boardClone.getExposedTiles()){
                    if(boardClone.removeTiles(tile1, tile2)){
                        noneRemoved = true;
                        break;
                    }
                }
            }
        }
    }



    // All public Constructors

    /**
     * Runs classic board with default verbosity of 1
     * @param seed -- seeds random generator to create unique game boards
     */
    public void runClassicInstance(long seed){
        Board classic = new Board(seed, "classic"); //3829432
        runInstance(1, classic);
    }
    
    /**
     * Runs classic board with default verbosity of 1
     * @param seed -- seeds random generator to create unique game boards
     * @param verbosity -- sets degree of console output 
     */
    public void runClassicInstance(long seed, int verbosity){
        Board classic = new Board(seed, "classic"); //3829432
        runInstance(verbosity, classic);
    }

    /**
     * Runs pyramid board with default verbosity of 1
     * @param seed -- seeds random generator to create unique game boards
     */
    public void runPyramidInstance(long seed){
        Board pyramid = new Board(seed, "pyramid");
        runInstance(1, pyramid);
    }

    /**
     * Runs pyramid board with default verbosity of 1
     * @param seed -- seeds random generator to create unique game boards
     * @param verbosity -- sets degree of console output 
     */
    public void runPyramidInstance(long seed, int verbosity){
        Board pyramid = new Board(seed, "pyramid");
        runInstance(verbosity, pyramid);
    }

    /**
     * Runs fish board with default verbosity of 1
     * @param seed -- seeds random generator to create unique game boards
     */
    public void runFishInstance(long seed){
        Board fish = new Board(seed, "fish");
        runInstance(1, fish);
    }

    /**
     * Runs fish board with default verbosity of 1
     * @param seed -- seeds random generator to create unique game boards
     * @param verbosity -- sets degree of console output ,
     */
    public void runFishInstance(long seed, int verbosity){
        Board fish = new Board(seed, "fish");
        runInstance(verbosity, fish);
    }
}
