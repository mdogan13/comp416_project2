package client;


import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.HashMap;

public class Server
{
  ServerSocket ss;
  Socket s;
  DataInputStream dis;
  DataOutputStream dos;
  HashMap<String,String> data = new HashMap<String,String>();
  
  public Server()
  {
      try
      {
          System.out.println("Server Started");
      ss=new ServerSocket(12);
      s=ss.accept();
      System.out.println(s);
      System.out.println("CLIENT CONNECTED");
          dis= new DataInputStream(s.getInputStream());
          dos= new DataOutputStream(s.getOutputStream());
       
        	  ServerChat();
          
          
      }
      catch(Exception e)
      {
           System.out.println(e);
      }
  }

  public static void main (String as[])
  {
       new Server();
  }

  public void ServerChat() throws IOException
  {
       String str, s1;
       do
       {
           str=dis.readUTF();
           System.out.println("Client Message:"+str);
       String submitCommand =str.substring(0,6);
       String getCommand =str.substring(0,3);
       if(submitCommand.equals("submit")) {
     
    	   String[] keyValuePair = str.substring(7,str.length()).split(",");
    	   data.put(keyValuePair[0],keyValuePair[1]);
    	   System.out.println(Arrays.asList(data));
    	   dos.writeUTF("b");
    	   dos.flush();
       }else if(getCommand.equals("get")) {
    	   String key =str.substring(4,str.length());
    	   String value = data.get(key);
    	   dos.writeUTF(value);
    	   dos.flush();
       }
       //BufferedReader br=new BufferedReader(new   InputStreamReader(System.in));
       //s1=br.readLine();
       //dos.writeUTF(s1);
      // dos.flush();
   }
   while(true);
  }
}