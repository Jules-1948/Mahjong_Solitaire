/**
 * @author Tyler James and Julia Schaming
 */
public class Main{

    public static void main(String[] args) {
        // Can create Boards of type "classic", "pyramid", and "fish"
        Board classicBoard = new Board(238493280, "classic");

        AStar aStar = new AStar();
        System.out.print(aStar.runInstance(classicBoard, 1));
    

        BeamStack algorithm2 = new BeamStack(2, 2);
        System.out.println(algorithm2.runInstance(classicBoard, 1));
    }
    
}