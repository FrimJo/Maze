import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/* This class represents a Maze */
public class Maze {
		
	private Position startPosition;
	public HashMap<Position,Character> mazeData;
	
	/* Constructor of class maze, receives a reader and
	 * uses it to hash the values representing the
	 * different parts of the maze. */
	public Maze(FileReader reader){
		this.mazeData = new HashMap<Position,Character>();

		try {
			int data, x = 0, y = 0;
			char dataChar;
						
			while( (data = reader.read() ) != -1 ){	/* While the reader hasn't reached end of file. */
				
				if (data == 10){					/* If the reader has reached new line. */
					x = 0;
					y++;
				}else{
		    		dataChar = (char) data; 		/* Else convert ASCI to Character and add the character to mazeDatata. */
		    		this.mazeData.put(new Position(x, y), dataChar);
		    		
		    		if(dataChar == 'S'){
		    			if(this.startPosition == null){
		    				this.startPosition = new Position(x, y);
		    			}else{
			    			throw new Error("The maze contains to meny start positions.\n" +
			    					"Please recheck the maze-file and make sure that there is one S and atleast one G represented in the maze-file.");
		    			}
		    		}
		    		x++;
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(this.startPosition == null || !this.mazeData.containsValue('G')){
			throw new Error("The maze does not contain goal positions and/or a start position.\n" +
					"Please recheck the maze-file and make sure that there is one S and atleast one G represented in the maze-file.");
		}
	}
	
	/*A method to check whether a position it movable */
	public boolean isMovable(Position position){
		try{
			return (this.mazeData.get(position) != '*');
		}catch(NullPointerException e){
			return false;		/* If the the position does not exist, return false. */
		}
	}
	
	/* Check to see if a specific position is the goal position. */
	public boolean isGoal(Position position){
		try{
			return (this.mazeData.get( position ) == 'G');
		}catch(NullPointerException e){
			return false;		/* If the the position does not exist, return false. */
		}
	}
	
	/* Gets and returns the position of startPosition. */
	public Position getStartPosition(){
		return this.startPosition;
	}
}