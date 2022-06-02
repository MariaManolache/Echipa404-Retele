package client;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import common.Transport;

public class Client implements Closeable {
	
	//pt a trimite mesajul catre interfata grafica
	public interface ClientCallback { //message to communicate to the graphical interface
		void  onTalk(String message);
	}
	
	private Socket socket;
	
	public Client(String host, int port, ClientCallback callback) throws UnknownHostException, IOException {
		
		socket = new Socket(host, port);
		
		//tcp: odata conectat clientul, serverul nu mai trebuie anuntat
		
		//creez fir de executie pe care trimit mesajul
		new Thread( () -> {
			while(socket != null && !socket.isClosed()) {
				try {
					callback.onTalk(Transport.receive(socket));
					
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}).start(); 
		
	}
	
	public void send(String message) throws IOException { //pt Client Program
		Transport.send(message, socket);
	}

	@Override
	public void close() throws IOException {
		socket.close();
	}

}
