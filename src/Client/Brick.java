package Client;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class Brick {
	static int bricksXPos[] = { 50, 350, 450, 550, 50, 300, 350, 450, 550, 150, 150, 450, 550, 250, 50, 100, 150, 550,
			250, 350, 450, 550, 50, 250, 350, 550, 50, 150, 250, 300, 350, 550, 50, 150, 250, 350, 450, 550, 50, 250,
			350, 550 };

	static int bricksYPos[] = { 50, 50, 50, 50, 100, 100, 100, 100, 100, 150, 200, 200, 200, 250, 300, 300, 300, 300,
			350, 350, 350, 350, 400, 400, 400, 400, 450, 450, 450, 450, 450, 450, 500, 500, 500, 500, 500, 500, 550,
			550, 550, 550 };

	static int solidBricksXPos[] = { 150, 350, 150, 500, 450, 300, 600, 400, 350, 200, 0, 200, 500 };

	static int solidBricksYPos[] = { 0, 0, 50, 100, 150, 200, 200, 250, 300, 350, 400, 400, 450 };

	public static int brickON[] = new int[42];
	public static int indexBrickisDeleted = 0;

	private ImageIcon breakBrickImage;
	private ImageIcon solidBrickImage;

	public Brick() {
		breakBrickImage = new ImageIcon("break_brick.jpg");
		solidBrickImage = new ImageIcon("solid_brick.jpg");

		for (int i = 0; i < brickON.length; i++) {
			brickON[i] = 1;
		}
	}

	public void draw(Component c, Graphics g) {
		for (int i = 0; i < brickON.length; i++) {
			if (brickON[i] == 1) {
				breakBrickImage.paintIcon(c, g, bricksXPos[i], bricksYPos[i]);
			}
		}
	}

	public void drawSolids(Component c, Graphics g) {
		for (int i = 0; i < solidBricksXPos.length; i++) {
			solidBrickImage.paintIcon(c, g, solidBricksXPos[i], solidBricksYPos[i]);
		}
	}

	public static boolean checkCollision(int x, int y) {
		boolean collided = false;
		for (int i = 0; i < brickON.length; i++) {
			if (brickON[i] == 1) {
				if (new Rectangle(x, y, 10, 10).intersects(new Rectangle(bricksXPos[i], bricksYPos[i], 50, 50))) {
					indexBrickisDeleted = i;
					brickON[i] = 0;
					collided = true;
					break;
				}
			}
		}

		return collided;
	}

	public static boolean checkMove(int x, int y) {
		boolean collided = false;
		for (int i = 0; i < solidBricksXPos.length; i++) {
			if (new Rectangle(x, y, 50, 50).intersects(new Rectangle(solidBricksXPos[i], solidBricksYPos[i], 50, 50))) {
				collided = true;
				break;
			}
		}
		for (int i = 0; i < brickON.length; i++) {
			if (brickON[i] == 1) {
				if (new Rectangle(x, y, 50, 50).intersects(new Rectangle(bricksXPos[i], bricksYPos[i], 50, 50))) {
					collided = true;
					break;
				}
			}
		}
//		for (int i = 0; i < BoardGame.getClients().size(); i++) {
//			if (BoardGame.getClients().get(i) != null) {
//				if (new Rectangle(x, y, 50, 50).intersects(new Rectangle(BoardGame.getClients().get(i).getTankX(),
//						BoardGame.getClients().get(i).getTankY(), 50, 50))) {
//					collided = true;
//					break;
//				}
//			}
//		}

		return collided;
	}

	public static boolean checkSolidCollision(int x, int y) {
		boolean collided = false;
		for (int i = 0; i < solidBricksXPos.length; i++) {
			if (new Rectangle(x, y, 10, 10).intersects(new Rectangle(solidBricksXPos[i], solidBricksYPos[i], 50, 50))) {
				collided = true;
				break;
			}
		}

		return collided;
	}
}