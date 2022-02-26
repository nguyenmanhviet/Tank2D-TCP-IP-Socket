package Client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

public class PlayerBullet {
	private double x, y;

	public PlayerBullet(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public void move(int dir) {
		if (dir == 3)
			x += 10;
		else if (dir == 2)
			x -= 10;
		else if (dir == 0)
			y -= 10;
		else if (dir == 1)
			y += 10;
	}

	public void draw(Graphics g) {
			g.setColor(Color.yellow);
			g.fillOval((int) x, (int) y, 10, 10);
	}

	public int getX() {
		return (int) x;
	}

	public int getY() {
		return (int) y;
	}

}
