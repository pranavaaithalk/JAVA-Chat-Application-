import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.*;
import java.util.Scanner;

import javax.swing.*;
public class Client extends JFrame
{
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    private JLabel heading=new JLabel("Client Area");
    private JTextArea messageArea=new JTextArea();
    private JTextField messageInput=new JTextField();
    Scanner scan = new Scanner(System.in);
    
    public Client()
    {
        try {
                System.out.println("Enter Server IP Address: ");
                String ip=scan.next();

                System.out.println("Enter Server Port Number: ");
                int pn=scan.nextInt();
                
                System.out.println("Sending request to server...");
                socket=new Socket(ip,pn);
                
                System.out.println("Connection done...");
                
                br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out=new PrintWriter(socket.getOutputStream());
            createGUI();
            handleEvents();
            startReading();
            // startWriting();


        } catch (Exception e) {
           e.printStackTrace(); 
        }
    }
    private void createGUI()
    {
        this.setTitle("Client Messenger");
        this.setSize(500,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        heading.setFont(new Font("Arial", Font.BOLD, 28));
        heading.setForeground(new Color(0, 102, 204));

        messageArea.setFont(new Font("Calibri", Font.PLAIN, 18));
        messageArea.setEditable(false);
        messageArea.setBackground(new Color(240, 240, 240));
        messageArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        messageInput.setFont(new Font("Calibri", Font.PLAIN, 18));
        messageInput.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 102, 204), 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        heading.setIcon(new ImageIcon("new logo.png"));
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setVerticalAlignment(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        messageArea.setEditable(false);
        

        this.setLayout(new BorderLayout());

        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollpane=new JScrollPane(messageArea);
        jScrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.add(jScrollpane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);

        this.setVisible(true);
    }
    private void handleEvents()
    {
        messageInput.addKeyListener(new KeyListener(){

            @Override
            public void keyTyped(KeyEvent e) {
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode()==10){
                    String contentToSend=messageInput.getText();
                    messageArea.append("Me: "+contentToSend+"\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                    
                    
                }
            }

        });
    }
    public void startReading()
    {
        Runnable r1=()->{
            System.out.println("reader started...");
            try{
                while(true)
                {
                    String msg=br.readLine();
                    if(msg.equals("over and out"))
                    {
                        System.out.println("Server terminated the chat");
                        JOptionPane.showMessageDialog(this, "Server terminated the chat");
                        messageInput.setEnabled(false);
                        messageArea.setEnabled(false);
                        socket.close();
                        break;
                    }

                    // System.out.println("Server: "+msg);
                    messageArea.append("Server: "+msg+"\n");
                
                }
            }catch(Exception e)
        {
            e.printStackTrace();
        }
        };
        new Thread(r1).start();
    }


    public void startWriting()
    {
        Runnable r2=()->{
            System.out.println("writer started...");
            try{
            while(!socket.isClosed())
            {

                    BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));

                    String content=br1.readLine();
                    out.println(content);
                    out.flush();
                    
                }
            }catch(Exception e){
                System.out.println("Connection is closed.");
            }
        };
        
        new Thread(r2).start();
    }

    public static void main(String[] args)
    {
        System.out.println("This is client...");
        new Client();
    }
    
}
