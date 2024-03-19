This program implements random boards of Mahjong Solitaire and solves them using A* search and BeamStack search

To run:
1. Create a new Board instance with a long seed parameter and a string parameter of board type (ex. "classic", "pyramid", "fish")
2. Create a new instance of either BeamStack or AStar and call runInstance(Board board) or runInstance(Board board, int verbosity)

Note: To get A* to finish and return an answer (due to runtime limits), 
        you can uncomment the code at the bottom of main and follow its running.

Note: For a higher setting in A* verbosity, it outputs the path of tiles removed to get to the current board. This looks like
        pairs of highlighted colors. Each individual number is a single tile and the highlighted backround represents the suit.
