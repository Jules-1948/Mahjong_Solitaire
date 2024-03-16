public class AStar {

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
