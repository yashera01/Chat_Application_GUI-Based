import java.net.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.io.*;
import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.*;

public class client extends JFrame{

    Socket socket;
    BufferedReader br;
    PrintWriter out;
    //declare componants
    private JLabel heading=new JLabel("Client Area");
    private JTextArea messageArea=new JTextArea();
    private JTextField messageInput=new JTextField();
    private Font font=new Font("Roboto",Font.PLAIN,20);

    public client()
    {
        try {
            System.out.println("Sending request to server");
            socket=new Socket("127.0.0.1",7778);
            System.out.println("Connection done");

            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out=new PrintWriter(socket.getOutputStream());
            createGUI();
            handleEvents();
            startReading();
            // startWriting();


        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    private void handleEvents()
    {
        messageInput.addKeyListener(new KeyListener(){
             //@Override
             public void keyTyped(KeyEvent e)
             {

             }
            // @Override
             public void keyPressed(KeyEvent e)
             {

             }
             //@Override
             public void keyReleased(KeyEvent e)
             {
                System.out.println("key released"+e.getKeyCode());
                if(e.getKeyCode()==10)
                {
                   // System.out.println("You have pressed enter button");
                   //messageArea.append("Me:"+contentToSend+"\n");
                   String contentToSend=messageInput.getText();
                   messageArea.append("Me:"+contentToSend+"\n");
                   out.println(contentToSend);
                   out.flush();
                   messageInput.setText("");
                   messageInput.requestFocus();

                }
             }


        });
    }

    private void createGUI()
    {
        this.setTitle("client messenger[END]");
        this.setSize(600,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      //  this.setVisible(true);
        //coding for componant
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        //heading.setIcon(new ImageIcon("logo.png"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);


        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        messageArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);


        this.setLayout(new BorderLayout());
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane=new JScrollPane(messageArea);
        this.add(jScrollPane=new JScrollPane(messageArea));
        this.add(messageArea,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);
        


        this.setVisible(true);

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
                    System.out.println("Server terminated the chat");
                    JOptionPane.showMessageDialog(this, "Server terminated chat");
                    messageInput.setEnabled(false);
                    socket.close();
                    break;
                }

                //System.out.println("Server: "+msg);
                messageArea.append("Server: "+msg+"\n");
                }
                //System.out.println("connection closed");

                
            }
           // System.out.println("connection closed");
            catch(Exception e)
            {
                    //e.printStackTrace();
                    System.out.println("connection closed");
            }
        };
        new Thread(r1).start(); //call to thread



    }

    public void startWriting()
    {
        Runnable r2=()->{
            System.out.println("Writer started");
            try {
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
                System.out.println("connection is closed"); 
            }
            
            catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }

        };
        new Thread(r2).start();
    }

    public static void main(String[] args) {
        System.out.println("This is client");
        new client();
    }
}
