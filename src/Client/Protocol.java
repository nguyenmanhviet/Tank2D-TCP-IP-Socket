package Client;
/*
 * Protocol.java
 *
 * Created on 25 „«—”, 2008, 10:32 „
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author Mohamed Talaat Saad
 */
public class Protocol {
    
    
    /**
     * Creates a new instance of Protocol
     */
    private String message="";
    public Protocol() 
    {
        
    }
    
    public String Started() {
    	message="Started";
    	return message;
    }
    
    public String RegisterPacket(int x,int y, String name)
    {
        message="Hello"+x+","+y+ "|" +name;
        return message;
    }
    public String UpdatePacket(int x,int y,int id,int dir)
    {
        message="Update"+x+","+y+"-"+dir+"|"+id;
        return message;
    }
    public String ShotPacket(int id)
    {
        message="Shot"+id;
        return message;
    }
    
    public String Win(int id) {
    	message="Win"+id;
    	return message;
    }
    
    public String Bullet(int idisShooted,int idShoot) {
    	message = "Bullet"+idisShooted+","+idShoot;
    	return message;
    }
    public String RemoveClientPacket(int id)
    {
        message="Remove"+id;
        return message;
    }
    
    public String PlayAgain(int id) {
    	message = "PlayAgain"+id;
    	return message;
    }
    
    public String ExitMessagePacket(int id)
    {
        message="Exit"+id;
        return message;
    }
}

