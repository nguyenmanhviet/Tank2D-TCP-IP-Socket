package Client;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author Mohamed Talaat Saad
 */
public class BoardGame extends JPanel {

	/** Creates a new instance of GameBoardPanel */
	private Tank clientTank;
	private int width = 1000;
	private int height = 630;
	private static ArrayList<Tank> tanks;
	private boolean gameStatus;
	private String notice = "Press Register to Play...";
	private Brick brick;
	private Client client;
	private ImageIcon imageIcon = new ImageIcon("bg.png");
	private static Explore explore;
	public static Explore getExplore() {
		return explore;
	}

	public static void setExplore(Explore explore) {
		BoardGame.explore = explore;
	}

	private static boolean played = false;
	private int stepAnimation = 1;

	public BoardGame(Tank tank, Client client, boolean gameStatus) {
		this.clientTank = tank;
		this.gameStatus = gameStatus;
		this.client = client;
		setSize(width, height);
		setFocusable(true);
		addKeyListener(new InputManager(tank));
		tanks = new ArrayList<Tank>(5);
		brick = new Brick();
		for (int i = 0; i < 5; i++) {
			tanks.add(null);
		}

	}

	public void paintComponent(Graphics g) {
		g.setColor(new Color(255, 204, 51));
		g.fillRect(0, 0, 900, 650);
		g.drawImage(new ImageIcon("bg1.jpg").getImage(), 0, 0, 650, 600, null);
//		g.fillRect(0, 0, 650, 600);

		g.setColor(Color.red);

		brick.drawSolids(this, g);
		brick.draw(this, g);
		g.drawImage(new ImageIcon("logo.jpg").getImage(), 660, 20, 200, 150, null);
		g.drawImage(new ImageIcon("bg.jpg").getImage(), 660, 210, 200, 390, null);
		g.drawImage(new ImageIcon("healthbar.png").getImage(), 730, 225, 120, 50, null);
		new ImageIcon("player" + clientTank.getTankID() + "_tank_up.png").paintIcon(this, g, 670, 225);
		g.fillRect(757, 242, clientTank.getScore(), 23);

		if (gameStatus) {

			int pos = 225;
			for (int i = 1; i < tanks.size(); i++) {
				if (tanks.get(i) != null) {
					pos += 50;
					g.drawImage(new ImageIcon("healthbar.png").getImage(), 730, pos, 120, 50, null);
					new ImageIcon("player" + tanks.get(i).getTankID() + "_tank_up.png").paintIcon(this, g, 670, pos);
					g.fillRect(757, pos + 17, tanks.get(i).getScore(), 23);
				}
			}

			clientTank.getTank().paintIcon(this, g, clientTank.getTankX(), clientTank.getTankY());
			if (clientTank.getPlayerBullet() != null && clientTank.isShoot) {
				if (clientTank.getDir() == 0) {
					clientTank.setBulletDirection(0);
				} else if (clientTank.getDir() == 1) {
					clientTank.setBulletDirection(1);
				} else if (clientTank.getDir() == 2) {
					clientTank.setBulletDirection(2);
				} else if (clientTank.getDir() == 3) {
					clientTank.setBulletDirection(3);
				}
				clientTank.isShoot = false;
			} else {
				if (clientTank.getPlayerBullet() != null) {
					clientTank.getPlayerBullet().move(clientTank.getBulletDirection());
					clientTank.getPlayerBullet().draw(g);
					if (Brick.checkCollision(clientTank.getPlayerBullet().getX(), clientTank.getPlayerBullet().getY())
							|| Brick.checkSolidCollision(clientTank.getPlayerBullet().getX(),
									clientTank.getPlayerBullet().getY())
							|| clientTank.getPlayerBullet().getX() < 0 || clientTank.getPlayerBullet().getX() >= 630
							|| clientTank.getPlayerBullet().getY() < 0 || clientTank.getPlayerBullet().getY() >= 590) {
						explore = new Explore(clientTank.getPlayerBullet().getX(), clientTank.getPlayerBullet().getY());
						clientTank.setPlayerBullet(null);
					}
					for (int i = 1; i < tanks.size(); i++) {
						if (tanks.get(i) != null) {
							if (clientTank.getPlayerBullet() != null
									&& new Rectangle(clientTank.getPlayerBullet().getX(),
											clientTank.getPlayerBullet().getY(), 10, 10)
													.intersects(new Rectangle(tanks.get(i).getTankX(),
															tanks.get(i).getTankY(), 50, 50))) {
								client.sendToServer(
										new Protocol().Bullet(tanks.get(i).getTankID(), clientTank.getTankID()));
								explore = new Explore(clientTank.getPlayerBullet().getX(), clientTank.getPlayerBullet().getY());
								clientTank.setPlayerBullet(null);
								break;
							}
						}
					}
				}
			}
			for (int i = 1; i < tanks.size(); i++) {
				if (tanks.get(i) != null) {
					tanks.get(i).getTank().paintIcon(this, g, tanks.get(i).getTankX(), tanks.get(i).getTankY());
					if (tanks.get(i).getPlayerBullet() != null && tanks.get(i).isShoot) {
						if (tanks.get(i).getDir() == 0) {
							tanks.get(i).setBulletDirection(0);
						} else if (tanks.get(i).getDir() == 1) {
							tanks.get(i).setBulletDirection(1);
						} else if (tanks.get(i).getDir() == 2) {
							tanks.get(i).setBulletDirection(2);
						} else if (tanks.get(i).getDir() == 3) {
							tanks.get(i).setBulletDirection(3);
						}
						tanks.get(i).isShoot = false;
					} else {
						if (tanks.get(i).getPlayerBullet() != null) {
							tanks.get(i).getPlayerBullet().move(tanks.get(i).getBulletDirection());
							tanks.get(i).getPlayerBullet().draw(g);
							if (Brick.checkCollision(tanks.get(i).getPlayerBullet().getX(),
									tanks.get(i).getPlayerBullet().getY())
									|| Brick.checkSolidCollision(tanks.get(i).getPlayerBullet().getX(),
											tanks.get(i).getPlayerBullet().getY())
									|| tanks.get(i).getPlayerBullet().getX() < 0
									|| tanks.get(i).getPlayerBullet().getX() >= 630
									|| tanks.get(i).getPlayerBullet().getY() < 0
									|| tanks.get(i).getPlayerBullet().getY() >= 590) {
								explore = new Explore(tanks.get(i).getPlayerBullet().getX(), tanks.get(i).getPlayerBullet().getY());
								tanks.get(i).setPlayerBullet(null);
							}
							if (tanks.get(i).getPlayerBullet() != null && new Rectangle(
									tanks.get(i).getPlayerBullet().getX(), tanks.get(i).getPlayerBullet().getY(), 10,
									10).intersects(
											new Rectangle(clientTank.getTankX(), clientTank.getTankY(), 50, 50))) {
								explore = new Explore(tanks.get(i).getPlayerBullet().getX(), tanks.get(i).getPlayerBullet().getY());
								tanks.get(i).setPlayerBullet(null);
							}
						}
					}
					if (tanks.get(i).getScore() <= 0) {
						explore = new Explore(tanks.get(i).getTankX() + 22, tanks.get(i).getTankY() + 25);
						tanks.set(i, null);
					}
				}
			}
			 if(explore != null) {
	            	explore.draw(g, stepAnimation);
	            	stepAnimation += 1;
	            	if(stepAnimation == 48) {
	            		stepAnimation = 1;
	            		explore = null;
	            	}
	            }
			if (clientTank.getScore() == 0) {
				gameStatus = false;
				notice = "You Lose !!!";
			}
			if (played) {
				boolean winner = true;
				for (int i = 1; i < tanks.size(); i++) {
					if (tanks.get(i) != null) {
						winner = false;
					}
				}
				if (winner) {
					client.sendToServer(new Protocol().Win(clientTank.getTankID()));
				}

			}
			g.dispose();
		} else {
			g.setColor(Color.red);
			g.setFont(new Font("Verdana", Font.BOLD, 25));
			g.drawString(notice, 180, 300);
		}
		repaint();
	}

	public static boolean isPlayed() {
		return played;
	}

	public static void setPlayed(boolean played) {
		BoardGame.played = played;
	}

	public Tank getClientTank() {
		return clientTank;
	}

	public void setClientTank(Tank clientTank) {
		this.clientTank = clientTank;
	}

	public Brick getBrick() {
		return brick;
	}

	public void setBrick(Brick brick) {
		this.brick = brick;
	}

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

	public void registerNewTank(Tank newTank) {
		tanks.set(newTank.getTankID(), newTank);
	}

	public void removeTank(int tankID) {
		tanks.set(tankID, null);
	}

	public Tank getTank(int id) {
		return tanks.get(id);
	}

	public void setGameStatus(boolean status) {
		gameStatus = status;
	}

	public static ArrayList<Tank> getClients() {
		return tanks;
	}

	public void ResetClients() {
		BoardGame.tanks = new ArrayList<Tank>(5);
		for (int i = 0; i < 5; i++) {
			tanks.add(null);
		}
	}
}
