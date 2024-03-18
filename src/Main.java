/**
 * @author Tyler James and Julia Schaming
 */
public class Main{

    public static void main(String[] args) {
        // AStar aStar = new AStar();
        // aStar.runClassicInstance(238493280, 1);


        Board classicBoard = new Board(238493280, "classic");
        BeamStack algorithm2 = new BeamStack(2, 2);
        System.out.println(algorithm2.runInstance(classicBoard, 3));
    }
    
}