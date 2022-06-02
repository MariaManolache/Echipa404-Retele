package server;

import java.util.Scanner;

import common.Settings;

public class Program {
	
	public static void main(String[] args) {
		try(Server server = new Server()) {
			
			//pornesc server ul
			server.start(Settings.PORT1);
			
			System.out.println("Server 1 is running, type 'exit' to stop it");
			
			//monitorizam comenzile rulate de utilizator
			try(Scanner scanner = new Scanner(System.in)) {
				while(true) {
					String command = scanner.nextLine();
					if(command == null || "exit".equalsIgnoreCase(command)) {
						break;
					}
				}
				
			}
			
		} catch (Exception e) {	//daca intampin vreo eroare
			System.out.println("Error: " + e.getMessage());
		} finally {
			System.exit(0);
		}
	}

}
//creez server si il pornesc