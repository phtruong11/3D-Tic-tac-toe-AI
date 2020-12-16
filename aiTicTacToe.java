import java.util.*;
public class aiTicTacToe {

	public int player; //1 for player 1 and 2 for player 2
	positionTicTacToe myNextMove = new positionTicTacToe(0,0,0);
	private List<List<positionTicTacToe>> winningLines = initializeWinningLines();
	private int getStateOfPositionFromBoard(positionTicTacToe position, List<positionTicTacToe> board)
	{
		//a helper function to get state of a certain position in the Tic-Tac-Toe board by given position TicTacToe
		int index = position.x*16+position.y*4+position.z;
		return board.get(index).state;
	}

	//Random AI
	public positionTicTacToe myAIAlgorithmRandom(List<positionTicTacToe> board, int player)
    {
        do
            {
                Random rand = new Random();
                int x = rand.nextInt(4);
                int y = rand.nextInt(4);
                int z = rand.nextInt(4);
                myNextMove = new positionTicTacToe(x,y,z);
            }while(getStateOfPositionFromBoard(myNextMove,board)!=0);
        return myNextMove;
    }

	//My AI
    public positionTicTacToe myAIAlgorithm(List<positionTicTacToe> board, int player)
    {  
       minimax(board, 4, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, true);
       return myNextMove;
    }

	//Minimax algorithm with alpha-beta pruning
	private int minimax(List<positionTicTacToe> board, int depth, double alpha, double beta, boolean maximizingPlayer)
    {
        if(depth == 0 || isEnded(board) !=0) {
            return heuristic(board, player);
        }
        //Maximizing player choose max value
        if (maximizingPlayer) {
            double max_value = Double.NEGATIVE_INFINITY;
            for(positionTicTacToe move : board) {
                if(getStateOfPositionFromBoard(move, board) != 0)
                  continue;
                //Create child node
                List<positionTicTacToe> child = deepCopyATicTacToeBoard(board);
                if(player == 1) {
                  for(int i=0;i<child.size();i++){
                    if(child.get(i).x==move.x && child.get(i).y==move.y && child.get(i).z==move.z){
                      child.get(i).state = 1;
                      }
                  }
                }
                else {
                  for(int i=0;i<child.size();i++){
                    if(child.get(i).x==move.x && child.get(i).y==move.y && child.get(i).z==move.z) {
                      child.get(i).state = 2;
                      }
                  }
                }

                int value = minimax(child, depth-1, alpha, beta, false);
                if( value > max_value) {
                    myNextMove = new positionTicTacToe(move.x, move.y, move.z);
                }
                max_value = Math.max(max_value, value);
                //Alpha-beta pruning
                alpha = Math.max(alpha, value);
                if(beta <= alpha) {
                  break;
                }
            }
            return (int) max_value;
        }
        //Minimizing player
        else {
            double min_value = Double.POSITIVE_INFINITY;
            for(positionTicTacToe move : board) {
                if(getStateOfPositionFromBoard(move, board) != 0)
                  continue;
                //Create child node
                List<positionTicTacToe> child = deepCopyATicTacToeBoard(board);
                if(player == 1) {
                  for(int i=0;i<child.size();i++){
                    if(child.get(i).x==move.x && child.get(i).y==move.y && child.get(i).z==move.z) {
                      child.get(i).state = 2;
                      }
                  }
                }
                else {
                  for(int i=0;i<child.size();i++){
                    if(child.get(i).x==move.x && child.get(i).y==move.y && child.get(i).z==move.z){
                      child.get(i).state = 1;
                      }
                  }
                }
                int value = minimax(child, depth-1, alpha, beta, true);
                if(value < min_value) {
                    myNextMove = new positionTicTacToe(move.x, move.y, move.z);
                }
                min_value = Math.min(min_value, value);
                //Alpha-beta pruning
                beta = Math.min(beta, value);
                if(beta <= alpha) {
                  break;
                  }
            }
            return (int) min_value;
        }
    }
	//calculate heuristic value
	public int heuristic(List<positionTicTacToe> board, int player)
    {
        int score = 0;
        for (List<positionTicTacToe> line: winningLines)
        {
            int player1 = 0;
            int player2 = 0;
            for(positionTicTacToe winningPosition: line)
            {
                int state = getStateOfPositionFromBoard(winningPosition, board);
                if(state == player)
                  player1++;
                else if(state == 0){}
                else
                  player2++;
            }
            if(player1 > player2) {
              score +=100;
            }
            if(player1 < player2) {
              score -=100;
            }
            if(player1 == player2) {
              score +=0;
            }
        }
        return score;
    }

	private int isEnded(List<positionTicTacToe> board)
    {
        //test whether the current game is ended

        //brute-force
	    List<List<positionTicTacToe>> winningLines = initializeWinningLines();
        for(int i=0;i<winningLines.size();i++)
        {

            positionTicTacToe p0 = winningLines.get(i).get(0);
            positionTicTacToe p1 = winningLines.get(i).get(1);
            positionTicTacToe p2 = winningLines.get(i).get(2);
            positionTicTacToe p3 = winningLines.get(i).get(3);

            int state0 = getStateOfPositionFromBoard(p0,board);
            int state1 = getStateOfPositionFromBoard(p1,board);
            int state2 = getStateOfPositionFromBoard(p2,board);
            int state3 = getStateOfPositionFromBoard(p3,board);

            //if they have the same state (marked by same player) and they are not all marked.
            if(state0 == state1 && state1 == state2 && state2 == state3 && state0!=0)
            {
                //someone wins
                p0.state = state0;
                p1.state = state1;
                p2.state = state2;
                p3.state = state3;

                return state0;
            }
        }
        for(int i=0;i<board.size();i++)
        {
            if(board.get(i).state==0)
            {
                //game is not ended, continue
                return 0;
            }
        }
        return -1; //call it a draw
    }
	private List<positionTicTacToe> deepCopyATicTacToeBoard(List<positionTicTacToe> board)
    {
        //deep copy of game boards
        List<positionTicTacToe> copiedBoard = new ArrayList<positionTicTacToe>();
        for(int i=0;i<board.size();i++)
        {
            copiedBoard.add(new positionTicTacToe(board.get(i).x,board.get(i).y,board.get(i).z,board.get(i).state));
        }
        return copiedBoard;
    }
	private List<List<positionTicTacToe>> initializeWinningLines()
	{
		//create a list of winning line so that the game will "brute-force" check if a player satisfied any 	winning condition(s).
		List<List<positionTicTacToe>> winningLines = new ArrayList<List<positionTicTacToe>>();

		//48 straight winning lines
		//z axis winning lines
		for(int i = 0; i<4; i++)
			for(int j = 0; j<4;j++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(i,j,0,-1));
				oneWinCondtion.add(new positionTicTacToe(i,j,1,-1));
				oneWinCondtion.add(new positionTicTacToe(i,j,2,-1));
				oneWinCondtion.add(new positionTicTacToe(i,j,3,-1));
				winningLines.add(oneWinCondtion);
			}
		//y axis winning lines
		for(int i = 0; i<4; i++)
			for(int j = 0; j<4;j++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(i,0,j,-1));
				oneWinCondtion.add(new positionTicTacToe(i,1,j,-1));
				oneWinCondtion.add(new positionTicTacToe(i,2,j,-1));
				oneWinCondtion.add(new positionTicTacToe(i,3,j,-1));
				winningLines.add(oneWinCondtion);
			}
		//x axis winning lines
		for(int i = 0; i<4; i++)
			for(int j = 0; j<4;j++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(0,i,j,-1));
				oneWinCondtion.add(new positionTicTacToe(1,i,j,-1));
				oneWinCondtion.add(new positionTicTacToe(2,i,j,-1));
				oneWinCondtion.add(new positionTicTacToe(3,i,j,-1));
				winningLines.add(oneWinCondtion);
			}

		//12 main diagonal winning lines
		//xz plane-4
		for(int i = 0; i<4; i++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(0,i,0,-1));
				oneWinCondtion.add(new positionTicTacToe(1,i,1,-1));
				oneWinCondtion.add(new positionTicTacToe(2,i,2,-1));
				oneWinCondtion.add(new positionTicTacToe(3,i,3,-1));
				winningLines.add(oneWinCondtion);
			}
		//yz plane-4
		for(int i = 0; i<4; i++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(i,0,0,-1));
				oneWinCondtion.add(new positionTicTacToe(i,1,1,-1));
				oneWinCondtion.add(new positionTicTacToe(i,2,2,-1));
				oneWinCondtion.add(new positionTicTacToe(i,3,3,-1));
				winningLines.add(oneWinCondtion);
			}
		//xy plane-4
		for(int i = 0; i<4; i++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(0,0,i,-1));
				oneWinCondtion.add(new positionTicTacToe(1,1,i,-1));
				oneWinCondtion.add(new positionTicTacToe(2,2,i,-1));
				oneWinCondtion.add(new positionTicTacToe(3,3,i,-1));
				winningLines.add(oneWinCondtion);
			}

		//12 anti diagonal winning lines
		//xz plane-4
		for(int i = 0; i<4; i++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(0,i,3,-1));
				oneWinCondtion.add(new positionTicTacToe(1,i,2,-1));
				oneWinCondtion.add(new positionTicTacToe(2,i,1,-1));
				oneWinCondtion.add(new positionTicTacToe(3,i,0,-1));
				winningLines.add(oneWinCondtion);
			}
		//yz plane-4
		for(int i = 0; i<4; i++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(i,0,3,-1));
				oneWinCondtion.add(new positionTicTacToe(i,1,2,-1));
				oneWinCondtion.add(new positionTicTacToe(i,2,1,-1));
				oneWinCondtion.add(new positionTicTacToe(i,3,0,-1));
				winningLines.add(oneWinCondtion);
			}
		//xy plane-4
		for(int i = 0; i<4; i++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(0,3,i,-1));
				oneWinCondtion.add(new positionTicTacToe(1,2,i,-1));
				oneWinCondtion.add(new positionTicTacToe(2,1,i,-1));
				oneWinCondtion.add(new positionTicTacToe(3,0,i,-1));
				winningLines.add(oneWinCondtion);
			}

		//4 additional diagonal winning lines
		List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
		oneWinCondtion.add(new positionTicTacToe(0,0,0,-1));
		oneWinCondtion.add(new positionTicTacToe(1,1,1,-1));
		oneWinCondtion.add(new positionTicTacToe(2,2,2,-1));
		oneWinCondtion.add(new positionTicTacToe(3,3,3,-1));
		winningLines.add(oneWinCondtion);

		oneWinCondtion = new ArrayList<positionTicTacToe>();
		oneWinCondtion.add(new positionTicTacToe(0,0,3,-1));
		oneWinCondtion.add(new positionTicTacToe(1,1,2,-1));
		oneWinCondtion.add(new positionTicTacToe(2,2,1,-1));
		oneWinCondtion.add(new positionTicTacToe(3,3,0,-1));
		winningLines.add(oneWinCondtion);

		oneWinCondtion = new ArrayList<positionTicTacToe>();
		oneWinCondtion.add(new positionTicTacToe(3,0,0,-1));
		oneWinCondtion.add(new positionTicTacToe(2,1,1,-1));
		oneWinCondtion.add(new positionTicTacToe(1,2,2,-1));
		oneWinCondtion.add(new positionTicTacToe(0,3,3,-1));
		winningLines.add(oneWinCondtion);

		oneWinCondtion = new ArrayList<positionTicTacToe>();
		oneWinCondtion.add(new positionTicTacToe(0,3,0,-1));
		oneWinCondtion.add(new positionTicTacToe(1,2,1,-1));
		oneWinCondtion.add(new positionTicTacToe(2,1,2,-1));
		oneWinCondtion.add(new positionTicTacToe(3,0,3,-1));
		winningLines.add(oneWinCondtion);

		return winningLines;

	}
	public aiTicTacToe(int setPlayer)
	{
		player = setPlayer;
	}
}
