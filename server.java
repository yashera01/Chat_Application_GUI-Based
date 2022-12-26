import java.net.*;
import java.io.*;
public class server
{
    //constructor
    ServerSocket Server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    public server()
    {
        try {
            Server=new ServerSocket(7778);
            System.out.println("Server is ready to accept connection");
            System.out.println("Waiting");
            socket=Server.accept();

            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out=new PrintWriter(socket.getOutputStream());
            startReading();
            startWriting();


        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    public void startReading()
    {
        //multithreading : thread read  krk deta rahega
        Runnable r1=()->{
            System.out.println("reader started");
            try{
            while(true)
            {
                
                String msg=br.readLine();
                if(msg.equals("exit"))
                {
                    System.out.println("Client terminate the chat");
                    break;
                }

                System.out.println("Client: "+msg);
            }
               
            }
            catch(Exception e)
            {
               // e.printStackTrace();
               System.out.println("connection closed");
            }
        };
        new Thread(r1).start(); //call to thread



    }

    public void startWriting()
    {
        Runnable r2=()->{
            System.out.println("Writer started");

            try{
            while(true && !socket.isClosed())
            {
               
                     BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
                     String content=br1.readLine();

                     
                     out.println(content);
                     out.flush(); 
                     if(content.equals("exit"))
                     {
                        //System.out.println("Client terminate the chat");
                        socket.close();
                        break;
                    }     
            }
            System.out.println("connection closed");
            
            }
            catch(Exception e){
                e.printStackTrace();
            }

        };
        new Thread(r2).start();

    }



    public static void main(String[] args) {
        System.out.println("This is server");
        new server();
    }
}