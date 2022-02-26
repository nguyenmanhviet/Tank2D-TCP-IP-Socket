package Server;
/*
 * Protocol.java
 *
 * Created on 01 √»—Ì·, 2008, 09:38 „
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author Mohamed Talaat Saad
 */
public class ProtocolServer {
    
    private String message="";
    /** Creates a new instance of Protocol */
    public ProtocolServer() {
    }
    
    public String EndServer() {
    	message = "End";
    	return message;
    }
    
    public String Started() {
    	message = "Started";
    	return message;
    }
    
    public String IDPacket(int id)
    {
        message="ID"+id;
        return message;
    }
    
    public String Full() {
    	message="Full";
    	return message;
    }
    
    public String Wait() {
    	message = "Wait";
    	return message;
    }
    
    public String Play() {
    	message="Play";
    	return message;
    }
    
    public String NewClientPacket(int x,int y,int dir,int id, String name)
    {
        message="NewClient"+x+","+y+"-"+dir+"|"+id + "." + name;
        return message;   
    }
    
}
