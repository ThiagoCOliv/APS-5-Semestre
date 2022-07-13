package com.mballem.app.service;

import com.mballem.app.bean.ChatMessage;
import com.mballem.app.bean.ChatMessage.Action;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServidorService 
{
    private ServerSocket serverSocket;
    private Socket socket;
    private Map<String, ObjectOutputStream> mapOnlines = new HashMap<String, ObjectOutputStream>();
    private Map<String, String> mapCityTopic = new HashMap<String, String>();
    
    public ServidorService()
    {
        try 
        {
            serverSocket = new ServerSocket(1111);
            
            while(true)
            {
                socket = serverSocket.accept();
                new Thread(new ListenerSocket(socket)).start();
            }
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private class ListenerSocket implements Runnable
    {
        private ObjectOutputStream output;
        private ObjectInputStream input;
        
        public ListenerSocket(Socket socket)
        {
            try 
            {
                this.output = new ObjectOutputStream(socket.getOutputStream());
                this.input = new ObjectInputStream(socket.getInputStream());
            } 
            catch (IOException ex) 
            {
                Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void run() 
        {
            ChatMessage chat = null;
            
            try 
            {
                while((chat = (ChatMessage) input.readObject()) != null)
                {
                    Action action = chat.getAction();
                    
                    switch (action) {
                        case CONNECT:
                            boolean isConnected = connect(chat, output);
                            if(isConnected)
                            {
                                mapOnlines.put(chat.getUsername(), output);
                                mapCityTopic.put(chat.getUsername(), chat.getCity_topic());
                                sendListOnlines(chat);
                            }   break;
                        case DISCONNECT:
                            throw new IOException();
                        case SEND_ALL:
                            sendAll(chat);
                            break;
                        case SEND_ONE:
                            sendOne(chat, output);
                            break;
                        default:
                            break;
                    }
                }
            } 
            catch (IOException ex) 
            {
                ChatMessage cm = new ChatMessage();
                cm.setUsername(chat.getUsername());
                cm.setCity_topic(chat.getCity_topic());
                disconnect(cm, output);
                sendListOnlines(cm);
            } 
            catch (ClassNotFoundException ex) 
            {
                Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private boolean connect(ChatMessage chat, ObjectOutputStream output)
    {
        boolean retorno = false;
        if(mapOnlines.isEmpty())
        {
            chat.setMensagem("YES");
            sendOne(chat, output);
            retorno = true;
        }
        
        if(mapOnlines.containsKey(chat.getUsername()))
        {
            chat.setMensagem("NO");
            sendOne(chat, output);
            retorno = false;
        }
        else
        {
            chat.setMensagem("YES");
            sendOne(chat, output);
            retorno = true;
        }
        
        return retorno;
    }
    
    private void disconnect(ChatMessage chat, ObjectOutputStream output)
    {
        mapOnlines.remove(chat.getUsername());
        mapCityTopic.remove(chat.getUsername());
        
        chat.setMensagem("bye!");
        
        chat.setAction(Action.SEND_ONE);
        sendAll(chat);
        
        System.out.println(chat.getUsername() + " saiu da sala");
    }
    
    private void sendOne(ChatMessage chat, ObjectOutputStream output)
    {
        try 
        {
            output.writeObject(chat);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void sendAll(ChatMessage chat)
    {
        for(Map.Entry<String, ObjectOutputStream> kv : mapOnlines.entrySet())
        {
            for(Map.Entry<String, String> kvct : mapCityTopic.entrySet())
            {
                if(kvct.getValue().equals(chat.getCity_topic()))
                {
                    if(kvct.getKey().equals(kv.getKey()) && !kv.getKey().equals(chat.getUsername()))
                    {
                        chat.setAction(Action.SEND_ONE);

                        try 
                        {
                            kv.getValue().writeObject(chat);
                        } 
                        catch (IOException ex) 
                        {
                            Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }
    }
    
    private void sendListOnlines(ChatMessage cm)
    {
        Set<String> setNames = new HashSet<String>();
        ChatMessage chat = new ChatMessage();
        chat.setCity_topic(cm.getCity_topic());
        
        for(Map.Entry<String, ObjectOutputStream> kv : mapOnlines.entrySet())
        {
            for(Map.Entry<String, String> kvct : mapCityTopic.entrySet())
            {
                if(kvct.getValue().equals(chat.getCity_topic()) && kv.getKey().equals(kvct.getKey()))
                {
                    setNames.add(kv.getKey());
                }
            }
        }
        
        
        chat.setAction(Action.USERS_ONLINE);
        chat.setSetOnlines(setNames);
        
        for(Map.Entry<String, ObjectOutputStream> kv : mapOnlines.entrySet())
        {
            chat.setUsername(kv.getKey());
            
            for(Map.Entry<String, String> kvct : mapCityTopic.entrySet())
            {
                if(kvct.getValue().equals(chat.getCity_topic()) && kv.getKey().equals(kvct.getKey()))
                {
                    try 
                    {
                        kv.getValue().writeObject(chat);
                    } 
                    catch (IOException ex) 
                    {
                        Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }
}
