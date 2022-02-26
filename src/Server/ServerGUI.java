package Server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.SocketException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
/*
 * ServerGUI.java
 *
 * Created on 07 ÃÈÑíá, 2008, 06:32 ã
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author Mohamed Talaat Saad
 */
public class ServerGUI extends JFrame implements ActionListener {

	private JLabel portLabel;
	private JTextField portText;
	private JButton startServerButton;
	private JButton stopServerButton;
	private JLabel statusLabel;

	private Server server;

	/** Creates a new instance of ServerGUI */
	public ServerGUI() {
		setTitle("Game Server GUI");
		setBounds(350, 200, 300, 200);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setLayout(null);

		portLabel = new JLabel("Create Room: ");
		portLabel.setBounds(55, 40, 100, 25);

		portText = new JTextField();
		portText.setBounds(140, 40, 100, 25);

		startServerButton = new JButton("Start Server");
		startServerButton.setBounds(20, 80, 120, 25);
		startServerButton.addActionListener(this);

		stopServerButton = new JButton("Stop Server");
		stopServerButton.setBounds(150, 80, 120, 25);
		stopServerButton.addActionListener(this);

		statusLabel = new JLabel();
		statusLabel.setBounds(80, 100, 200, 25);

		getContentPane().add(portLabel);
		getContentPane().add(portText);
		getContentPane().add(statusLabel);
		getContentPane().add(startServerButton);
		getContentPane().add(stopServerButton);

		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == startServerButton) {
			if (Integer.parseInt(portText.getText()) > 65353 || Integer.parseInt(portText.getText()) <= 1024) {
				JOptionPane.showMessageDialog(new JFrame(), "Invalid Room Number", "Dialog", JOptionPane.ERROR_MESSAGE);
			} else {
				try {
					server = new Server(Integer.parseInt(portText.getText()));
				} catch (SocketException ex) {
					ex.printStackTrace();
				}
				server.start();
				startServerButton.setEnabled(false);
				statusLabel.setText("Server is running.....");
			}
		}

		if (e.getSource() == stopServerButton) {
			try {
				server.BroadCastMessage(new ProtocolServer().EndServer());
				server.stopServer();
				statusLabel.setText("Server is stopping.....");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
				System.exit(0);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

}
