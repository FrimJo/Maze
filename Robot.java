/* This is an abstract parent class Robot */
public abstract class Robot {

	private Position position;
	protected Maze maze;
	
	/* Constructor of the abstract class Robot. */
	public Robot(Maze maze){
		this.maze = maze;
		this.position = maze.getStartPosition();
	}
	
	/* The abstract method move is used by the child
	 * classes. */
	public abstract void move();
	
	/* Return the current position of the robot. */
	public Position getCurrentPosition(){
		return this.position;
	}
	
	/* Sets the current position of the robot. */
	protected void setCurrentPosition(Position position){
		this.position = position;
	}
	
	/* This method check if the robot has reached the goal. */
	public boolean hasReachedGoal(){		
		return this.maze.isGoal(this.position);
	}
}
