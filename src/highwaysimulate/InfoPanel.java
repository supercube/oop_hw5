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
	private JLabel Warning;
	private String[] info = {"Length:", "#(Lane):", "#(Car At Entry):", "#(CarAtInterchange):", "Position(Interchage):", "Max Speed:", "Safe Factor:"};
	public InfoPanel(Highway highway){
		this.highway = highway;
		setLayout(null);
		
		infoLabel = new JLabel[info.length];
		infoField = new JTextField[info.length];
		for(int i = 0; i < info.length; i++)
			infoLabel[i] = new JLabel(info[i]);
		
		infoField[0] = new JTextField("100");
		infoField[1] = new JTextField("4");
		infoField[2] = new JTextField("20");
		infoField[3] = new JTextField("0");
		infoField[4] = new JTextField("50");
		infoField[5] = new JTextField("4");
		infoField[6] = new JTextField("2");
		
		infoLabel[0].setBounds(20, 20, 50, 40);
		infoField[0].setBounds(70, 27, 40, 25);
		infoLabel[1].setBounds(130, 20, 50, 40);
		infoField[1].setBounds(180, 27, 40, 25);
		infoLabel[2].setBounds(240, 20, 90, 40);
		infoField[2].setBounds(330, 27, 40, 25);
		infoLabel[3].setBounds(390, 20, 120, 40);
		infoField[3].setBounds(510, 27, 40, 25);
		infoLabel[4].setBounds(570, 20, 130, 40);
		infoField[4].setBounds(700, 27, 40, 25);
		infoLabel[5].setBounds(760, 20, 70, 40);
		infoField[5].setBounds(830, 27, 40, 25);
		infoLabel[6].setBounds(890, 20, 70, 40);
		infoField[6].setBounds(960, 27, 40, 25);
		
		for(int i = 0; i < info.length; i++){
			this.add(infoLabel[i]);
			this.add(infoField[i]);
		}
		Warning = new JLabel("Length > 0,    Length <= 240,    1 <= #(Lane) <= 8,    #(Car At Entry) >= 0,    #(CarAtInterchange) >= 0,    1 < Position(Interchage) <= Length,    Safe Factor >= 2");
		Warning.setBounds(20, 50, 930, 50);
		this.add(Warning);
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
					if(!submit(
							Integer.parseInt(infoField[0].getText()),
							Integer.parseInt(infoField[1].getText()),
							Integer.parseInt(infoField[2].getText()),
							Integer.parseInt(infoField[3].getText()),
							Integer.parseInt(infoField[4].getText()),
							Integer.parseInt(infoField[5].getText()),
							Integer.parseInt(infoField[6].getText())
							)
						){
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
	
	public boolean submit(int length, int lane, int num_car_entry, int num_car_interchange, int interchange, int limit, int safeFactor){
		return highway.set(length, lane, num_car_entry, num_car_interchange, interchange, limit, safeFactor);
	}
}
