package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import Client.Tank;
/*
 * Server.java
 *
 * Created on 21 „«—”, 2008, 09:41 ’
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author Mohamed Talaat Saad
 */
public class Server extends Thread {

	/**
	 * Creates a new instance of Server
	 */
	private ArrayList<ClientInfo> clients;
	private ServerSocket serverSocket;
	private int serverPort;

	private DataInputStream reader;
	private DataOutputStream writer;

	private ProtocolServer protocol;
	private boolean running = true;
    private boolean isStarted = false;
	
	public DataOutputStream getWriter() {
		return writer;
	}

	public void setWriter(DataOutputStream writer) {
		this.writer = writer;
	}

	public ProtocolServer getProtocol() {
		return protocol;
	}

	public void setProtocol(ProtocolServer protocol) {
		this.protocol = protocol;
	}

	public Server(int port) throws SocketException {
		this.serverPort = port;
		clients = new ArrayList<ClientInfo>();
		protocol = new ProtocolServer();

		try {
			serverSocket = new ServerSocket(serverPort);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void run() {
		Socket clientSocket = null;
		while (running) {
			try {
				clientSocket = serverSocket.accept();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			String sentence = "";
			try {
				reader = new DataInputStream(clientSocket.getInputStream());
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			try {
				sentence = reader.readUTF();
			} catch (IOException ex) {
				ex.printStackTrace();
			}

			System.out.println(sentence);
			if (sentence.startsWith("Hello")) {
				if(isStarted) {
					try {
						writer = new DataOutputStream(clientSocket.getOutputStream());
					} catch (IOException ex) {
						ex.printStackTrace();
					}
					sendToClient(protocol.Started());
				}
				else if (clients.size() >= 4) {
					try {
						writer = new DataOutputStream(clientSocket.getOutputStream());
					} catch (IOException ex) {
						ex.printStackTrace();
					}
					sendToClient(protocol.Full());
				} else {
					int pos = sentence.indexOf(',');
					int pos2 = sentence.indexOf('|');
					int x = Integer.parseInt(sentence.substring(5, pos));
					int y = Integer.parseInt(sentence.substring(pos + 1, pos2));
					String name = sentence.substring(pos2+1, sentence.length());

					try {
						writer = new DataOutputStream(clientSocket.getOutputStream());
					} catch (IOException ex) {
						ex.printStackTrace();
					}
					sendToClient(protocol.IDPacket(clients.size() + 1));
					try {
						BroadCastMessage(protocol.NewClientPacket(Tank.getPosX()[clients.size()],
								Tank.getPosY()[clients.size()], 0, clients.size() + 1, name));
						sendAllClients(writer);
					} catch (IOException ex) {
						ex.printStackTrace();
					}

					clients.add(
							new ClientInfo(writer, Tank.getPosX()[clients.size()], Tank.getPosY()[clients.size()], 0, name));
					if(clients.size() < 2) {
						try {
							writer = new DataOutputStream(clientSocket.getOutputStream());
						} catch (IOException ex) {
							ex.printStackTrace();
						}
						sendToClient(protocol.Wait());
					}
					else {
						try {
							BroadCastMessage(protocol.Play());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}

			else if (sentence.startsWith("Update")) {
				int pos1 = sentence.indexOf(',');
				int pos2 = sentence.indexOf('-');
				int pos3 = sentence.indexOf('|');
				int x = Integer.parseInt(sentence.substring(6, pos1));
				int y = Integer.parseInt(sentence.substring(pos1 + 1, pos2));
				int dir = Integer.parseInt(sentence.substring(pos2 + 1, pos3));
				int id = Integer.parseInt(sentence.substring(pos3 + 1, sentence.length()));
				if (clients.get(id - 1) != null) {
					clients.get(id - 1).setPosX(x);
					clients.get(id - 1).setPosY(y);
					clients.get(id - 1).setDirection(dir);
					try {
						BroadCastMessage(sentence);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}

			} else if (sentence.startsWith("Shot")) {
				try {
					BroadCastMessage(sentence);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			} 
			
			else if (sentence.startsWith("Started")) {
				isStarted = true;
			} 
			
			else if (sentence.startsWith("Win")) {
				try {
					BroadCastMessage(sentence);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				clients = new ArrayList<ClientInfo>();
				System.out.println(clients.size());
			}


			else if (sentence.startsWith("Bullet")) {
				try {
					BroadCastMessage(sentence);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}

			else if (sentence.startsWith("Remove")) {
				int id = Integer.parseInt(sentence.substring(6));

				try {
					BroadCastMessage(sentence);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				clients.set(id - 1, null);
			} else if (sentence.startsWith("Exit")) {
				int id = Integer.parseInt(sentence.substring(4));

				try {
					BroadCastMessage(sentence);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				if (clients.get(id - 1) != null)
					clients.set(id - 1, null);
			}
		}

		try {
			reader.close();
			writer.close();
			serverSocket.close();
			clientSocket.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void stopServer() throws IOException {
		running = false;
	}

	public void BroadCastMessage(String mess) throws IOException {
		for (int i = 0; i < clients.size(); i++) {
			if (clients.get(i) != null)
				clients.get(i).getWriterStream().writeUTF(mess);
		}
	}

	public void sendToClient(String message) {
		if (message.equals("exit"))
			System.exit(0);
		else {
			try {
				writer.writeUTF(message);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public void sendAllClients(DataOutputStream writer) {
		int x, y, dir;
		String name = "";
		for (int i = 0; i < clients.size(); i++) {
			if (clients.get(i) != null) {
				x = clients.get(i).getX();
				y = clients.get(i).getY();
				dir = clients.get(i).getDir();
				name = clients.get(i).getName();
				try {
					writer.writeUTF(protocol.NewClientPacket(x, y, dir, i + 1, name));
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	public class ClientInfo {
		DataOutputStream writer;
		int posX, posY, direction;
		String name;

		public ClientInfo(DataOutputStream writer, int posX, int posY, int direction, String name) {
			this.writer = writer;
			this.posX = posX;
			this.posY = posY;
			this.direction = direction;
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setPosX(int x) {
			posX = x;
		}

		public void setPosY(int y) {
			posY = y;
		}

		public void setDirection(int dir) {
			direction = dir;
		}

		public DataOutputStream getWriterStream() {
			return writer;
		}

		public int getX() {
			return posX;
		}

		public int getY() {
			return posY;
		}

		public int getDir() {
			return direction;
		}
	}

}
