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
    private ArrayList<Tile[]> runInstance(int verbosity, Board startingNode){
        // Print starting board
        if(verbosity == 2){System.out.println(startingNode);}
        
        // 1. Initialize fringe to starting state, closed list to empty
        ArrayList<Board> fringe = new ArrayList<Board>();
        ArrayList<Board> closedList = new ArrayList<Board>();
        fringe.add(startingNode);

        // 2. While fringe is not empty:
        while(!fringe.isEmpty()){
            // 3. searchNode <- fringe.removeFirst()
            Board searchNode = fringe.get(0);
            fringe.remove(0);

            // print path to searchNode
            if(verbosity == 2){
                ArrayList<Tile[]> currentNodePath = searchNode.getPath();
                if(currentNodePath.isEmpty()){
                    System.out.println("\nBelow lists the path of removed tiles from each searchNode");
                }
                System.out.print("Remaining tiles: " + searchNode.getExistentTileCount());
                for(Tile[] tilePair : currentNodePath){
                    System.out.print(" " + tilePair[0].toString() + tilePair[1].toString());
                }
                System.out.println();
            }

            // 4. if isGoal(searchNode.endingState):
            if(searchNode.getTiles().isEmpty()){
                // Print ending board
                if(verbosity == 2){System.out.println(searchNode);}

                // 5. return searchNode
                return (searchNode.getPath());
            }
                
            // 6. closedList.add(searchNode.endingState)
            closedList.add(searchNode);

            // 7. children <- expand(searchNode)
            ArrayList<Tile[]> tilePairs = new ArrayList<>();
            for(Tile tile1 : searchNode.getExposedTiles()){
                for(Tile tile2 : searchNode.getExposedTiles()){
                    if(searchNode.canRemoveTiles(tile1, tile2)){
                        tilePairs.add(new Tile[]{tile1, tile2});
                    }
                }
            }

            // Make a list of children for searchNode
            ArrayList<Board> children = new ArrayList<>();
            for(Tile[] child : tilePairs){
                Board childBoard = searchNode.deepCopy();
                children.add(childBoard);
                childBoard.removeTiles(child[0], child[1]);
                childBoard.addToPath(child[0], child[1]);
            }


            // 8. for each child in children:
            for(Board child : children){
                System.out.print(" " + child.getPath().get(0)[0].toString() + child.getPath().get(0)[1].toString());
                // 9. if child.endingState is in closedList: continue;
                boolean containsChild = false;
                for(Board closed : closedList){
                    if(child.areEqualBoards(closed)){
                        containsChild = true;
                        break;
                    }
                }
                if(containsChild){continue;}
            

                for(Board node : fringe){
                    // 10. if there is a search node in fringe that ends in child.endingState:
                    if(child.getExistentTileCount() == node.getExistentTileCount()){
                        // 11. update fringe to contain only the better of the two paths
                        // Heuristics: 1. depth -> the few layers the better; outranks 2nd heuristic
                        //             have 2nd heuristic bc depths will be largely similar
                        //             2. numExposesd tiles -> more exposed tiles the better
                        if(child.getDepth() == node.getDepth()){
                            if(child.getExposedTiles().size() > node.getExposedTiles().size()){
                                //child is better
                                fringe.remove(node);
                                fringe.add(child);
                            } // else previous node is better
                        } else if (child.getDepth() > node.getDepth()){
                            //child is better
                            fringe.remove(node);
                            fringe.add(child);
                        } // else previous node is better
                    }
                    // 12. else: 
                    else{
                        // 13. add child to fringe
                        fringe.add(child);
                    }
                }

                System.out.println("NO");
                // In case fringe was empty and the for loop never ran
                if(fringe.isEmpty()){
                    fringe.add(child);
                }
            }
            System.out.println("here");

        }

        return null;
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