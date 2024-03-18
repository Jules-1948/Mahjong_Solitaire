import java.util.function.Supplier;

/**
 * @author Tyler James and Julia Schaming
 */
public class Main{

    public static void main(String[] args) {
        // Can create Boards of type "classic", "pyramid", and "fish"
        Board classicBoard = new Board(238493280, "classic");
    

        BeamStack algorithm2 = new BeamStack(2, 2);
        Board aStarStart = algorithm2.runInstance(classicBoard, 1);
        System.out.print(aStarStart);

        AStar aStar = new AStar();
        System.out.print(aStar.runInstance(aStarStart, 1));
    }
    
    public static Board recordRuntime(Supplier<Board> function) {
        float startTime = System.currentTimeMillis();
        Board result = function.get();
        float endTime = System.currentTimeMillis();
        float runtime = endTime - startTime;
        System.out.printf("Time taken: %.02f ms\n", runtime);
        return result;
    }
}