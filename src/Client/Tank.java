package Client;

import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Tank {
	static int[] posX = {200, 400, 200, 400};
	static int[] posY = {500, 500, 100, 100};
	PlayerBullet playerBullet;
	int bulletDirection = 5;
	
	public int getBulletDirection() {
		return bulletDirection;
	}

	public void setBulletDirection(int bulletDirection) {
		this.bulletDirection = bulletDirection;
	}

	boolean isShoot = false;

	public boolean isShoot() {
		return isShoot;
	}

	public void setShoot(boolean isShoot) {
		this.isShoot = isShoot;
	}

	public static int[] getPosX() {
		return posX;
	}

	public static void setPosX(int[] posX) {
		Tank.posX = posX;
	}

	public static int[] getPosY() {
		return posY;
	}

	public static void setPosY(int[] posY) {
		Tank.posY = posY;
	}

	private int tankID;
	private int tankX = 0;
	private int tankY = 0;
	private String name = "Player";
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private  ImageIcon tank;
	private int dir = 0;
    private int score = 85;
    

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score -= score;
	}

	public void ResetScore() {
		this.score = 0;
	}
	
	public Tank() {
		tankX = 0;
		tankY = 0;
		dir = 0;
		tank = new ImageIcon("player" + tankID + "_tank_" + takeDir(dir) + ".png");
	}

	public Tank(int x, int y, int dir, int tankID, String name) {
		tankX = x;
		tankY = y;
		this.tankID = tankID;
		this.dir = dir;
		this.name = name;
		tank = new ImageIcon("player" + tankID + "_tank_" + takeDir(dir) + ".png");
	}


	public int getTankID() {
		return tankID;
	}

	public void setTankID(int tankID) {
		this.tankID = tankID;
	}

	public int getTankX() {
		return tankX;
	}

	public void setTankX(int tankX) {
		this.tankX = tankX;
	}

	public int getTankY() {
		return tankY;
	}

	public void setTankY(int tankY) {
		this.tankY = tankY;
	}

	public ImageIcon getTank() {
		return tank;
	}

	public void setTank(ImageIcon tank) {
		this.tank = tank;
	}

	public int getDir() {
		return dir;
	}

	public  void setDir(int dir) {
		this.dir = dir;
	}

	public String takeDir(int dir) {
		switch (dir) {
		case 0:
			return "up";
		case 1:
			return "down";
		case 2:
			return "left";
		default:
			return "right";
		}
	}

	public void moveLeft() {
		dir = 2;
		tank = new ImageIcon("player" + tankID + "_tank_" + takeDir(dir) + ".png");
	    if(!Brick.checkMove(tankX-10, tankY) && (tankX-10) >=0 ) {
	    	System.out.println("bi chan");
	    	tankX -= 10;
	    }
	}

	public void moveRight() {
		dir = 3;
		tank = new ImageIcon("player" + tankID + "_tank_" + takeDir(dir) + ".png");
		if(!Brick.checkMove(tankX+10, tankY) && (tankX+10) <= 600) {
	    	tankX += 10;
	    }
	}

	public void moveUp() {
		dir = 0;
		tank = new ImageIcon("player" + tankID + "_tank_" + takeDir(dir) + ".png");
		if(!Brick.checkMove(tankX, tankY-10) && (tankY-10) >=0) {
	    	tankY -= 10;
	    }
	}

	public void moveDown() {
		dir = 1;
		tank = new ImageIcon("player" + tankID + "_tank_" + takeDir(dir) + ".png");
		if(!Brick.checkMove(tankX, tankY+10) && (tankY+10) <= 550) {
	    	tankY += 10;
	    }
	}
	public void shoot() {
		if(dir == 0)
		{					
			playerBullet = new PlayerBullet(tankX + 20, tankY);
		}
		else if(dir == 1)
		{					
			playerBullet = new PlayerBullet(tankX + 20, tankY + 40);
		}
		else if(dir == 3)
		{				
			playerBullet = new PlayerBullet(tankX + 40, tankY + 20);
		}
		else if(dir == 2)
		{			
			playerBullet = new PlayerBullet(tankX, tankY + 20);
		}
	}

	public PlayerBullet getPlayerBullet() {
		return playerBullet;
	}

	public void setPlayerBullet(PlayerBullet playerBullet) {
		this.playerBullet = playerBullet;
	}
}
