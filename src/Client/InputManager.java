package Client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.Socket;
import javax.swing.event.MenuKeyEvent;
/*
 * InputManager.java
 *
 * Created on 25 „«—”, 2008, 02:57 „
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author Mohamed Talaat Saad
 */
public class InputManager implements KeyListener {
	private final int LEFT = 37;
	private final int RIGHT = 39;
	private final int UP = 38;
	private final int DOWN = 40;
	private static int status = 0;

	private Tank tank;
	private Client client;

	/** Creates a new instance of InputManager */
	public InputManager(Tank tank) {
		this.client = Client.getGameClient();
		this.tank = tank;

	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == LEFT) {
			tank.moveLeft();
			client.sendToServer(
					new Protocol().UpdatePacket(tank.getTankX(), tank.getTankY(), tank.getTankID(), tank.getDir()));
		} else if (e.getKeyCode() == RIGHT) {
			tank.moveRight();
			client.sendToServer(
					new Protocol().UpdatePacket(tank.getTankX(), tank.getTankY(), tank.getTankID(), tank.getDir()));
		} else if (e.getKeyCode() == UP) {
			tank.moveUp();
			client.sendToServer(
					new Protocol().UpdatePacket(tank.getTankX(), tank.getTankY(), tank.getTankID(), tank.getDir()));
		} else if (e.getKeyCode() == DOWN) {
			tank.moveDown();
			client.sendToServer(
					new Protocol().UpdatePacket(tank.getTankX(), tank.getTankY(), tank.getTankID(), tank.getDir()));
		} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			if (tank.getPlayerBullet() == null) {
				client.sendToServer(new Protocol().ShotPacket(tank.getTankID()));
				tank.setShoot(true);
				tank.shoot();
			}
			client.sendToServer(
					new Protocol().Started());
		}
		else if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			client.sendToServer(new Protocol().PlayAgain(tank.getTankID()));
		}
	}

	public void keyReleased(KeyEvent e) {
	}

}
