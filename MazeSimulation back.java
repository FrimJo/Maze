import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;


import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;


public class MazeSimulation {
	
	public enum RobotType {Memory,RightHand,Random}
	
	private class RobotButtonListener implements ActionListener {
		private RobotType type;
		private MazeSimulation mazeSimulation;
		
		public RobotButtonListener(RobotType r, MazeSimulation mazeSimulation) {
			type=r;
			this.mazeSimulation=mazeSimulation;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			new Thread(new Runnable(){

				@Override
				public void run() {
					if(type==RobotType.Memory) {
						mazeSimulation.runSimulation(new MemoryRobot(maze));
					}
					else if(type==RobotType.RightHand) {
						mazeSimulation.runSimulation(new RightHandRuleRobot(maze));
					}else if(type==RobotType.Random){
						mazeSimulation.runSimulation(new RandomRobot(maze));
					}
					
				}}).start();
			
		}
		
	}
	
	public static final int MAX_STEPS=2000;
	private DisplayPanel[][] mazeDisplay;
	private Maze maze;
	private JTextField messageField;
	private JPanel mazePanel;
	private Dimension d;
	private static MazeSimulation ms;

	public MazeSimulation() {

		JFrame theWindow;
		File file=null;
		boolean done=false;
		d = null;
		JFileChooser fileChooser;

		//Setup main window
		theWindow = new JFrame("RoboSim");
		theWindow.setSize(500, 500);
		theWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    theWindow.setBackground(Color.WHITE);
		mazePanel = new JPanel();
		theWindow.add(mazePanel,BorderLayout.CENTER);
		messageField = new JTextField();
		theWindow.add(messageField,BorderLayout.SOUTH);
		JPanel topPanel=new JPanel();
		topPanel.setLayout(new GridLayout(1,3));
		JButton memButton = new JButton("MemoryRobot");
		topPanel.add(memButton);
		JButton rightButton = new JButton("RightHandRuleRobot");
		topPanel.add(rightButton);
		JButton randomButton = new JButton("RandomRobot");
		topPanel.add(randomButton);
		JButton clearButton = new JButton("Clear");
		clearButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {	
				setupMazePanel();
			}});
		topPanel.add(clearButton);
		theWindow.add(topPanel,BorderLayout.NORTH);

		//Load maze
		fileChooser=new JFileChooser();
		FileFilter filter = new FileNameExtensionFilter("Maze file", "maze");
		fileChooser.setFileFilter(filter);
		do {
			int returnVal=fileChooser.showOpenDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				file = fileChooser.getSelectedFile();
			} else {
				System.exit(0); //Canceled by user
			}
			try {
				d=calcSize(new FileReader(file));
				mazeDisplay=new DisplayPanel[d.width][d.height];
				mazePanel.setLayout(new GridLayout(d.width,d.height));

				//Create a maze from a text file
				maze=new Maze(new FileReader(file));
				done=true;
			} catch (FileNotFoundException e) {
				System.err.println("Unable to open maze file");
			} catch (IOException e) {
				System.err.println("Unable to read maze file");
			}
		} while(!done);
		
		//Configure button actions
		ActionListener memRobot=new RobotButtonListener(RobotType.Memory,this);
		memButton.addActionListener(memRobot);
		ActionListener rightRobot=new RobotButtonListener(RobotType.RightHand,this);
		rightButton.addActionListener(rightRobot);
		ActionListener randomRobot=new RobotButtonListener(RobotType.Random,this);
		randomButton.addActionListener(randomRobot);
		
		//Initialize the display
		setupMazePanel();
		theWindow.setVisible(true);

	}

	private void setupMazePanel() {
		mazePanel.removeAll();
		for(int i=0;i<d.width;i++) {
			for(int j=0;j<d.height;j++) {
				DisplayPanel dp=new DisplayPanel();
				if(!maze.isMovable(new Position(j,i)))
					dp.setWall();
				else if(maze.isGoal(new Position(j,i)))
					dp.setGoal();
				else
					dp.setEmpty();
				mazePanel.add(dp);
				mazeDisplay[i][j]=dp;
			}
		}
		mazePanel.doLayout();
	}

	public void runSimulation(Robot r) {
		
		Position rhRobotPos=r.getCurrentPosition();
		moveRobot(rhRobotPos);
		int i=0;
		do { //Simulate the robot moving
			removeRobot(r.getCurrentPosition());
			r.move();
			moveRobot(r.getCurrentPosition());
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				return;
			}
			i++;
		} while(!r.hasReachedGoal() &&
				i<MAX_STEPS);
		if(r.hasReachedGoal())
			updateMessage("Robot reached goal after "+i+" steps");		
	}

	private void updateMessage(final String string) {
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				messageField.setText(string);

			}

		});

	}

	private void removeRobot(final Position pos) {
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				mazeDisplay[pos.getY()][pos.getX()].setEmpty();

			}

		});

	}

	private void moveRobot(final Position rhRobotPos) {
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				mazeDisplay[rhRobotPos.getY()][rhRobotPos.getX()].setRobot();

			}

		});

	}

	private Dimension calcSize(Reader r) throws IOException {
		int maxX=0;
		int maxY=0;
		BufferedReader in = new BufferedReader(r);
		String str;
		while((str=in.readLine())!=null) {
			maxY++;
			if(maxX<str.length())
				maxX=str.length();
		}
		in.close();
		return new Dimension(maxY,maxX);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		setMs(new MazeSimulation());
	}

	public static MazeSimulation getMs() {
		return ms;
	}

	public static void setMs(MazeSimulation ms) {
		MazeSimulation.ms = ms;
	}
}
