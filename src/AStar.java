/**
 * @author Julia Schaming
 */
public class AStar {

    // Your solver method or class should have a single verbosity parameter that defaults to showing the final result
    // only but can be set to another value (or values) to print out more details about the steps the algorithm is taking.
    // For example, for verbosity=1, my CSP solver might print out every time it tries to assign a value to a variable, and
    // with verbosity=2, it prints out each domain of the variables in addition.

    public void runInstance(){
        ClassicBoard board = new ClassicBoard(543891); //3829432);
        ClassicBoard boardClone = board.deepCopy();

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
            System.out.println(board);
            System.out.println(boardClone);
        }
    }
}
