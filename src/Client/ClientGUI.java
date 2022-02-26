package Client;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Mohamed Talaat Saad
 */
public class ClientGUI extends JFrame implements ActionListener, WindowListener {

	/** Creates a new instance of ClientGUI */
	private JLabel ipaddressLabel;
	private ImageIcon backgroundImage;
	private JLabel background;
	private JLabel portLabel;
	private JLabel nameLabel;
	private static JLabel scoreLabel;

	private JTextField ipaddressText;
	private JTextField portText;
	private JTextField nameText;
	private static JTextField scoreText;

	private JButton registerButton;

	private JPanel registerPanel;
	public static JPanel gameStatusPanel;
	private Client client;
	private Tank clientTank;

	int width = 900, height = 670;
	boolean isRunning = true;
	private BoardGame boardPanel;

	public ClientGUI() {
		setTitle("Multiclients Tanks Game");
		setSize(width, height);
		setLocation(60, 100);
		getContentPane().setBackground(new Color(255, 204, 51));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		addWindowListener(this);

		backgroundImage = new ImageIcon("bggame.jpg");
		background = new JLabel("", backgroundImage, JLabel.CENTER);
		background.setBounds(0, 0, 650, 600);

		ipaddressLabel = new JLabel("IP Address:");
		ipaddressLabel.setBounds(670, 50, 75, 25);

		portLabel = new JLabel("Input Port: ");
		portLabel.setBounds(670, 80, 75, 25);

		nameLabel = new JLabel("Your Name: ");
		nameLabel.setBounds(670, 110, 75, 25);

		ipaddressText = new JTextField("localhost");
		ipaddressText.setBounds(755, 50, 100, 25);
		ipaddressText.setEnabled(false);

		portText = new JTextField();
		portText.setBounds(755, 80, 100, 25);

		nameText = new JTextField();
		nameText.setBounds(755, 110, 100, 25);

		registerButton = new JButton("Register");
		registerButton.setBounds(720, 140, 90, 25);
		registerButton.addActionListener(this);

		client = Client.getGameClient();
		clientTank = new Tank();
		boardPanel = new BoardGame(clientTank, client, false);
		getContentPane().add(ipaddressLabel);
		getContentPane().add(portLabel);
		getContentPane().add(ipaddressText);
		getContentPane().add(portText);
		getContentPane().add(registerButton);
		getContentPane().add(nameLabel);
		getContentPane().add(nameText);
		getContentPane().add(background);
		setVisible(true);

	}

	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();

		if (obj == registerButton) {
			registerButton.setEnabled(false);
			portText.setEnabled(false);
			nameText.setEnabled(false);
			try {
				client.register(ipaddressText.getText(), Integer.parseInt(portText.getText()), clientTank.getTankX(),
						clientTank.getTankY(), nameText.getText());
				clientTank.setName(nameText.getText());
				getContentPane().add(boardPanel);
				boardPanel.setGameStatus(true);
				boardPanel.repaint();
				try {
					Thread.sleep(500);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
				new ClientRecivingThread(client.getSocket()).start();
				registerButton.setFocusable(false);
				boardPanel.setFocusable(true);
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(this, "The Server is not running, try again later!",
						"Tanks 2D Multiplayer Game", JOptionPane.INFORMATION_MESSAGE);
				registerButton.setEnabled(true);
				portText.setEnabled(true);
				nameText.setEnabled(true);
			}
		}

	}

	public void windowOpened(WindowEvent e) {

	}

	public void windowClosing(WindowEvent e) {

		int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit ?",
				"Tanks 2D Multiplayer Game!", JOptionPane.YES_NO_OPTION);

		client.sendToServer(new Protocol().ExitMessagePacket(clientTank.getTankID()));

	}

	public void windowClosed(WindowEvent e) {

	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowActivated(WindowEvent e) {
	}

	public void windowDeactivated(WindowEvent e) {
	}

	public class ClientRecivingThread extends Thread {
		Socket clientSocket;
		DataInputStream reader;

		public ClientRecivingThread(Socket clientSocket) {
			this.clientSocket = clientSocket;
			try {
				reader = new DataInputStream(clientSocket.getInputStream());
			} catch (IOException ex) {
				ex.printStackTrace();
			}

		}

		public void run() {
			while (isRunning) {
				String sentence = "";
				try {
					sentence = reader.readUTF();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				if (sentence.startsWith("Started")) {
					boardPanel.setNotice("Game is already started !");
					boardPanel.setGameStatus(false);
					try {
						Thread.sleep(4000);
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}
					boardPanel.setNotice("Try join another room !");
					registerButton.setEnabled(true);
					portText.setEnabled(true);
					nameText.setEnabled(true);

					getContentPane().remove(boardPanel);

				} else if (sentence.startsWith("Full")) {
					boardPanel.setNotice("Server is Full !");
					boardPanel.setGameStatus(false);
					try {
						Thread.sleep(4000);
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}
					boardPanel.setNotice("Try join another room !");
					registerButton.setEnabled(true);
					portText.setEnabled(true);
					nameText.setEnabled(true);

					getContentPane().remove(boardPanel);

				} 
				else if(sentence.startsWith("Wait")) {
					boardPanel.setGameStatus(false);
					boardPanel.setNotice("Wait for having 2 players or more to Play...");
				}
				
				else if(sentence.startsWith("Play")) {
					boardPanel.setPlayed(true);
					boardPanel.setGameStatus(true);
				}
				else if (sentence.startsWith("ID")) {
					boardPanel.setGameStatus(true);
					int id = Integer.parseInt(sentence.substring(2));
					clientTank.setTankID(id);
					clientTank.setTankX(clientTank.getPosX()[id - 1]);
					clientTank.setTankY(clientTank.getPosY()[id - 1]);
					clientTank.setTank(new ImageIcon("player" + clientTank.getTankID() + "_tank_"
							+ clientTank.takeDir(clientTank.getDir()) + ".png"));
				} else if (sentence.startsWith("NewClient")) {
					int pos1 = sentence.indexOf(',');
					int pos2 = sentence.indexOf('-');
					int pos3 = sentence.indexOf('|');
					int pos4 = sentence.indexOf('.');
					int x = Integer.parseInt(sentence.substring(9, pos1));
					int y = Integer.parseInt(sentence.substring(pos1 + 1, pos2));
					int dir = Integer.parseInt(sentence.substring(pos2 + 1, pos3));
					int id = Integer.parseInt(sentence.substring(pos3 + 1, pos4));
					String name = sentence.substring(pos4 + 1, sentence.length());
					if (id != clientTank.getTankID())
						boardPanel.registerNewTank(new Tank(x, y, dir, id, name));
				} else if (sentence.startsWith("Update")) {
					int pos1 = sentence.indexOf(',');
					int pos2 = sentence.indexOf('-');
					int pos3 = sentence.indexOf('|');
					int x = Integer.parseInt(sentence.substring(6, pos1));
					int y = Integer.parseInt(sentence.substring(pos1 + 1, pos2));
					int dir = Integer.parseInt(sentence.substring(pos2 + 1, pos3));
					int id = Integer.parseInt(sentence.substring(pos3 + 1, sentence.length()));

					if (id != clientTank.getTankID()) {
						boardPanel.getTank(id).setTankX(x);
						boardPanel.getTank(id).setTankY(y);
						boardPanel.getTank(id).setDir(dir);
						boardPanel.getTank(id).setTank(new ImageIcon("player" + boardPanel.getTank(id).getTankID()
								+ "_tank_" + boardPanel.getTank(id).takeDir(boardPanel.getTank(id).getDir()) + ".png"));
//                        boardPanel.repaint();
					}

				} else if (sentence.startsWith("Shot")) {
					int id = Integer.parseInt(sentence.substring(4));
					if (id != clientTank.getTankID()) {
						boardPanel.getTank(id).setShoot(true);
						boardPanel.getTank(id).shoot();
					}
				}

				else if (sentence.startsWith("Win")) {
					int id = Integer.parseInt(sentence.substring(3));
					if (id != clientTank.getTankID()) {
						boardPanel.setNotice(boardPanel.getTank(id).getName() + " Won!!!");
						boardPanel.setGameStatus(false);
					}
					else {
						boardPanel.setNotice("You Won!!!");
						boardPanel.setGameStatus(false);
					}
					try {
						Thread.sleep(4000);
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}
					System.exit(0);
				}

				else if (sentence.startsWith("Bullet")) {
					int pos = sentence.indexOf(',');
					int idisShooted = Integer.parseInt(sentence.substring(6, pos));
					int idShoot = Integer.parseInt(sentence.substring(pos + 1, sentence.length()));
					if (idisShooted == clientTank.getTankID()) {
						clientTank.setScore(17);
					} else {
						boardPanel.getTank(idisShooted).setScore(17);
						if (boardPanel.getTank(idShoot) != null) {
							boardPanel.setExplore(new Explore(boardPanel.getTank(idShoot).getPlayerBullet().getX(), boardPanel.getTank(idShoot).getPlayerBullet().getY()));
							boardPanel.getTank(idShoot).setPlayerBullet(null);
						}
					}
				}

				else if (sentence.startsWith("End")) {
					isRunning = false;
					boardPanel.setNotice("Server invalid!!!");
					boardPanel.setGameStatus(false);
					try {
						Thread.sleep(4000);
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}
					System.exit(0);
				}

				else if (sentence.startsWith("Remove")) {
					int id = Integer.parseInt(sentence.substring(6));

					if (id == clientTank.getTankID()) {
						int response = JOptionPane.showConfirmDialog(null,
								"Sorry, You are loss. Do you want to try again ?", "Tanks 2D Multiplayer Game",
								JOptionPane.OK_CANCEL_OPTION);
						if (response == JOptionPane.OK_OPTION) {
							// client.closeAll();
							setVisible(false);
							dispose();

							new ClientGUI();
						} else {
							System.exit(0);
						}
					} else {
						boardPanel.removeTank(id);
					}
				} else if (sentence.startsWith("Exit")) {
					int id = Integer.parseInt(sentence.substring(4));

					if (id != clientTank.getTankID()) {
						boardPanel.removeTank(id);
					}
				}

			}

			try {
				reader.close();
				clientSocket.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}

		}
	}

}
