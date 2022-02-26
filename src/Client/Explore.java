package Client;

import java.awt.Graphics;

import javax.swing.ImageIcon;

public class Explore {
	private int posX;
	private int posY;
	public Explore(int x, int y) {
		this.posX = x;
		this.posY = y;
	}
	public void draw(Graphics g, int stepAnimation) {
		g.drawImage(new ImageIcon("explore_" + stepAnimation / 8 + ".png").getImage(), posX-22, posY-25, 50, 50, null);
	}
}
