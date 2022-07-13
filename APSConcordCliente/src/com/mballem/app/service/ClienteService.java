package com.mballem.app.service;

import com.mballem.app.bean.ChatMessage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClienteService 
{
    private Socket socket;
    private ObjectOutputStream output;
    
    public Socket connect(String ip)
    {
        try 
        {
            this.socket = new Socket(ip, 1111);
            this.output = new ObjectOutputStream(socket.getOutputStream());
        }
        catch(UnknownHostException ex)
        {
            Logger.getLogger(ClienteService.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) 
        {
            Logger.getLogger(ClienteService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return socket;
    }
    
    public void send(ChatMessage chat)
    {
        try 
        {
            output.writeObject(chat);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(ClienteService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
