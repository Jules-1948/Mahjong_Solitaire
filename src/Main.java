/**
 * @author Tyler James and Julia Schaming
 */
public class Main{
    private static int iterations = 10; //Number of boards to run searches on

    public static void main(String[] args) {
        // Initalize starting boards
        Board[] boards = {
            new Board(238493280, "classic"),
            new Board(218028333, "classic"),
            new Board(930128322, "classic"),
            new Board(908280111, "classic"),
            new Board(157483684, "classic"),
            new Board(803839321, "classic"),
            new Board(782929272, "classic"),
            new Board(493839388, "classic"),
            new Board(637781668, "classic"),
            new Board(902992984, "classic")
        };

        long timeout = 300000; // If takes longer that 4 minutes time out
        //Initalize Results Storage
        Board[] beamStackResults = new Board[iterations];
        long[] beamStackNodeSearchedResults= new long[iterations];
        Board[] aStarResults = new Board[iterations];
        long[] aStarNodeSearchedResults= new long[iterations];
        
        // //Initalize algorithms
        BeamStack beamStack = new BeamStack(6, timeout);
        AStar aStar = new AStar(timeout);
        
        //Run iterations on beamstack using boards
        System.out.println("Running iterations on BeamStack");
        for (int i = 0; i < iterations; i++) {
            System.out.println("Iteration Number for BeamStack: " + i);
            beamStackResults[i] = beamStack.runInstance(boards[i]);
            beamStackNodeSearchedResults[i] = beamStack.getNodesSearched();
        }
        //Run iterations on A* using boards
        System.out.println("Running iterations on A Star");
        for (int i = 0; i < iterations; i++) {
            System.out.println("Iteration Number for A Star: " + i);
            aStarResults[i] = aStar.runInstance(boards[i]);
            aStarNodeSearchedResults[i] = aStar.getNodesSearched();
        }

        // Print the results
        System.out.println("Printing results");
        //Results of beamstack
        System.out.println("Beam Stack Search");
        printSolutionRatios(beamStackResults);
        printAvergageRemainingTiles(beamStackResults);
        printAverageNodes(beamStackNodeSearchedResults);

        //Results of A*
        System.out.println("A Star Search");
        printSolutionRatios(aStarResults);
        printAvergageRemainingTiles(aStarResults);
        printAverageNodes(aStarNodeSearchedResults);

        
        // Run AStar search with smaller board to show its working
        // Board board = new Board(238493280, "mini");
        // AStar aStar2 = new AStar(timeout);
        // aStar2.runInstance(board, 2);
    }

    public static void printAverageNodes(long[] nodeSearchedResult) {
        long totalNodes = 0;
        for (long nodes: nodeSearchedResult) {
            totalNodes += nodes;
        }
        System.out.println("Average nodes explored: " + totalNodes/iterations);
    }

    /**
     * Prints the number of solutions for a set of boards
     * @param resultingBoards
     */
    public static void printSolutionRatios(Board[] resultingBoards) {
        int count = 0;
        for (Board board: resultingBoards) {
            if (board.getExistentTileCount() == 0) {
                count++;
            }
        }
        System.out.println("Solutions found " + (double)count/iterations);
    }

    /**
     * Prints the average remaining tiles for a set of boards
     * @param resultingBoards
     */
    public static void printAvergageRemainingTiles(Board[] resultingBoards) {
        long tileCount = 0;
        for (Board board: resultingBoards) {
            tileCount += board.getExistentTileCount();
        }
        System.out.println("Average remaining tiles found were: " + tileCount/iterations);
    }
}