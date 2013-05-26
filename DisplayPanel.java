import java.awt.Color;
import javax.swing.JPanel;


public class DisplayPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public void setWall() {
		this.setBackground(Color.BLACK);
		
	}

	public void setEmpty() {
		this.setBackground(Color.WHITE);
		
	}

	public void setRobot() {
		this.setBackground(Color.RED);
		
	}

	public void setGoal() {
		this.setBackground(Color.GREEN);
		
	}

}
