package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class Server {

	int port;
	ServerSocket server=null;
	Socket client=null;
	ExecutorService pool = null;
	int clientcount=0;
	static HashMap<String,String> data = new HashMap<String,String>();

	public static void main(String[] args) throws IOException {
		Server serverobj=new Server(5000);
		serverobj.startServer();
	}

	Server(int port){
		this.port=port;
		pool = Executors.newFixedThreadPool(5);
	}

	public void startServer() throws IOException {

		server=new ServerSocket(5000);
		System.out.println("Server Booted");
		System.out.println("Any client can stop the server by sending -1");
		while(true)
		{
			client=server.accept();
			clientcount++;
			ServerThread runnable= new ServerThread(client,clientcount,this);
			pool.execute(runnable);
		}

	}

	private static class ServerThread implements Runnable {

		Server server=null;
		Socket client=null;
		BufferedReader cin;
		PrintStream cout;
		Scanner sc=new Scanner(System.in);
		int id;
		String s;

		ServerThread(Socket client, int count ,Server server ) throws IOException {

			this.client=client;
			this.server=server;
			this.id=count;
			System.out.println("Connection "+id+" established with client "+client);

			cin=new BufferedReader(new InputStreamReader(client.getInputStream()));
			cout=new PrintStream(client.getOutputStream());

		}

		@Override
		public void run() {
			int x=1;
			try{
				while(true){
					s=cin.readLine();

					System. out.println("Client("+id+") :"+s);
					System.out.println("Server : ");

					if(s.contains("submit")) {

						String[] keyValuePair = s.substring(7,s.length()).split(",");
						data.put(keyValuePair[0],keyValuePair[1]);
						cout.println("OK");
						//cout.println(Arrays.asList(data));
						//cout.println("received");

					}else if(s.contains("get")) {
						String key =s.substring(4,s.length());
						if(data.containsKey(key)) {
							String value = data.get(key);
							cout.println(value);
						}else {
							cout.println("No stored value for "+key);
						}


					}else if (s.equalsIgnoreCase("bye")){
						cout.println("BYE");
						x=0;
						System.out.println("Connection ended by server");
						break;
					}else {
						cout.println("Incorrect command/usage. Try submit <key,value> or get <key>");
					}

				}


				cin.close();
				client.close();
				cout.close();
				if(x==0) {
					System.out.println( "Server cleaning up." );
					System.exit(0);
				}
			} 
			catch(IOException ex){
				System.out.println("Error : "+ex);
			}


		}
	}

}
