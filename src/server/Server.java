package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import common.Transport;

public class Server implements AutoCloseable {

	private ServerSocket serverSocket; // socket care accepta Client TCP
	private ExecutorService executorService;

	@Override
	public void close() throws Exception { // from AutoCLoseable
		// TODO Auto-generated method stub

	}

	public void start(int port) throws IOException { // for server.start() from Program
		stop();

		serverSocket = new ServerSocket(port);

		// pt fire de executie multiple, in cazul in care participa mai multi clienti
		// threadPool pt a putea gestiona eficient deschiderea mai multor thread uri
		// simultane
		executorService = Executors.newFixedThreadPool(50 * Runtime.getRuntime().availableProcessors());

		// lista clientilor conectati
		final List<Socket> clients = Collections.synchronizedList(new ArrayList<Socket>());

		executorService.execute(() -> {

			while (serverSocket != null && !serverSocket.isClosed()) {
				try {
					final Socket socket = serverSocket.accept();

					clients.forEach(client -> {
						try {
							if (!client.equals(socket)) {
								Transport.send(socket.getInetAddress().toString() + " port: " + socket.getLocalPort(),
										client);
							}
						} catch (Exception e) {
							// TODO: handle exception
						}
					});

					executorService.submit(() -> {
						try {

//							for(int i = 0; i < clients.size(); i++) {
//								Socket client = clients.get(i);
//								Transport.send(socket.getInetAddress().toString() + " port: " + socket.getLocalPort(), client);
//							}

							clients.add(socket);
							Transport.send("", socket);

							// primesc mesajul de la clienti
							while (socket != null && !socket.isClosed()) {
								try {
									// parcurg clientii
									String message = Transport.receive(socket);

									// trimit mesajul catre clienti
									clients.forEach(client -> {
										try {
											Transport.send(message, client);
										} catch (Exception e) {
											// TODO: handle exception
										}
									});
								} catch (Exception e) {
									// TODO: handle exception
								}
							}
						} catch (Exception e) {
							// TODO: handle exception
						} finally {
							clients.remove(socket);
//							clients.forEach(client -> {
//								try {
//									if (!client.equals(socket)) {
//										Transport.send(socket.getInetAddress().toString() + " port: " + socket.getLocalPort(),
//												client);
//									}
//								} catch (Exception e) {
//									// TODO: handle exception
//								}
//							});
						
						}
					});
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			//if(serverSocket.isClosed()) {
				clients.forEach(client -> {
					try {
							Transport.send("ceva", client);
					} catch (Exception e) {
						// TODO: handle exception
					}
				});
			//}
		}); 

	}

	public void stop() throws IOException {
		// ma asigur ca exista inainte de a le inchide

		if (executorService != null) {
			executorService.shutdown();
			executorService = null;
		}

		if (serverSocket != null) {
			serverSocket.close();
			serverSocket = null;
		}
	}

}
