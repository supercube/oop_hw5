package highwaysimulate;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

public class InfoPanel extends JPanel{
	
	private Highway highway;
	private SubmitButton submitButton;
	private JLabel[] infoLabel;
	private JTextField[] infoField;
	private String[] info = {"HighwayLength:", "NumberOfHighwayLane:", "NumberOfCarAtEntry:", "NumberOfCarAtInterchange:", "PositionOfIntechage:"};
	public InfoPanel(Highway highway){
		this.highway = highway;
		setLayout(null);
		
		infoLabel = new JLabel[Constant.NO_PARAMETER];
		infoField = new JTextField[Constant.NO_PARAMETER];
		for(int i = 0; i < Constant.NO_PARAMETER; i++)
			infoLabel[i] = new JLabel(info[i]);
		
		infoField[0] = new JTextField("10");
		infoField[1] = new JTextField("1");
		infoField[2] = new JTextField("5");
		infoField[3] = new JTextField("0");
		infoField[4] = new JTextField("5");
		
		infoLabel[0].setBounds(20, 20, 100, 40);
		infoField[0].setBounds(120, 27, 40, 25);
		infoLabel[1].setBounds(180, 20, 150, 40);
		infoField[1].setBounds(330, 27, 40, 25);
		infoLabel[2].setBounds(390, 20, 130, 40);
		infoField[2].setBounds(520, 27, 40, 25);
		infoLabel[3].setBounds(580, 20, 170, 40);
		infoField[3].setBounds(750, 27, 40, 25);
		infoLabel[4].setBounds(810, 20, 130, 40);
		infoField[4].setBounds(940, 27, 40, 25);
		
		for(int i = 0; i < Constant.NO_PARAMETER; i++){
			this.add(infoLabel[i]);
			this.add(infoField[i]);
		}
		submitButton = new SubmitButton("Confirm");
		submitButton.setBounds(1090, 60, 90, 40);
		this.add(submitButton);
	}
	
	private class SubmitButton extends JButton implements MouseListener{

		public SubmitButton(String text){
			super(text);
			addMouseListener(this);
		}
		public void mouseClicked(MouseEvent e) {
			try{
				if(SwingUtilities.isLeftMouseButton(e)){
					System.out.println("clicked");
					if(!submit(Integer.parseInt(infoField[0].getText()), Integer.parseInt(infoField[1].getText()), Integer.parseInt(infoField[2].getText()), Integer.parseInt(infoField[3].getText()), Integer.parseInt(infoField[4].getText()))){
						throw new Exception();
					}
				}
			}catch(Exception excp){
				System.out.println("false info");
			}
		}

		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
	}
	
	public boolean submit(int length, int lane, int num_car_entry, int num_car_interchange, int interchange){
		return highway.set(length, lane, num_car_entry, num_car_interchange, interchange);
	}
}
