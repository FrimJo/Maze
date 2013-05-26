/* ********************************************
 * 
 * ******************************************** */


/* This class represents a robot witch uses it's right hand to
 * find its way in the maze. */
public class RightHandRuleRobot extends Robot {
	
	private Position neighbors[];
	private int direction;
		
	/* Constructor of RightHandRuleRobot, sets defaults values. */
	public RightHandRuleRobot(Maze maze){
		super(maze);
		this.neighbors = new Position[4];
		this.direction = 0;
	}
	
	/* The move method of this robot class starts
	 * to look to the right and then rotate to the left
	 * until a valid space appears. */	
	@Override
	public void move(){
				
		/* Puts all the possible positions in to an array. */
		this.neighbors[0] = this.getCurrentPosition().getPosToNorth();
		this.neighbors[1] = this.getCurrentPosition().getPosToWest();
		this.neighbors[2] = this.getCurrentPosition().getPosToSouth();
		this.neighbors[3] = this.getCurrentPosition().getPosToEast();
		
		/* Set the neighborIndex to the right of the robots current direction. */
		int neighborsIndex = ( ( this.direction + 3 ) % 4 );
		
		/* Go through the four different possible positions*/
		for (int count = 0; count < 4; count++){
			
			/* If the index reaches end of array, set it to the
			 * beginning of the array. */
			if (neighborsIndex > 3)  
				neighborsIndex = 0;   
			
			/* If one of the neighbors is available, moves the robot
			 * to that position. */
			if (this.maze.isMovable(this.neighbors[neighborsIndex])){
				this.direction = neighborsIndex;
				this.setCurrentPosition(this.neighbors[neighborsIndex]);
				break;
			}
			
			neighborsIndex++;
		}
		
		if(this.getCurrentPosition().equals(this.maze.getStartPosition())){
			throw new Error("MemoryRobot can't reach goal!");
		}
	}
}
