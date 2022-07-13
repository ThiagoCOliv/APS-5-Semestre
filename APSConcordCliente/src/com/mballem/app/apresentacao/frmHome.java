/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mballem.app.apresentacao;

import com.mballem.app.bean.ChatMessage;
import com.mballem.app.bean.ChatMessage.Action;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import com.mballem.app.service.ClienteService;

/**
 *
 * @author Usuario
 */
public class frmHome extends javax.swing.JFrame 
{
    private String ip;
    private Socket socket;
    private ChatMessage chat;
    private ClienteService service;
    
    public frmHome() {
        initComponents();
    }
    
    public void passarIP(String ip)
    {
        this.ip = ip;
    }
    
    private class ListenerSocket implements Runnable
    {
        private ObjectInputStream input;
        
        public ListenerSocket(Socket socket)
        {
            try 
            {
                this.input = new ObjectInputStream(socket.getInputStream());
            } 
            catch (IOException ex) 
            {
                Logger.getLogger(frmHome.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void run() 
        {
            ChatMessage chat = null;
            try {
                while((chat = (ChatMessage) input.readObject()) != null)
                {
                    Action action = chat.getAction();
                    
                    if(action.equals(Action.CONNECT))
                    {
                        connected(chat);
                    }
                    else if(action.equals(Action.DISCONNECT))
                    {
                        disconnected();
                        socket.close();
                    }
                    else if(action.equals(Action.SEND_ONE))
                    {
                        receive(chat);
                    }
                    else if(action.equals(Action.USERS_ONLINE))
                    {
                        refreshOnlines(chat);
                    }
                }
            } 
            catch (IOException ex) 
            {
                Logger.getLogger(frmHome.class.getName()).log(Level.SEVERE, null, ex);
            } 
            catch (ClassNotFoundException ex) 
            {
                Logger.getLogger(frmHome.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void connected(ChatMessage chat)
    {
        if(chat.getMensagem().equals("NO"))
        {
            this.txtUsername.setText("");
            JOptionPane.showMessageDialog(this, "Conexão não realizada!");
            return;
        }
        
        this.chat = chat;
        
        this.btnConectar.setEnabled(false);
        this.txtUsername.setEnabled(false);
        this.btnDesconectar.setEnabled(true);
        this.btnEnviar.setEnabled(true);
        this.txtEnviar.setEnabled(true);
        this.txaChat.setEnabled(true);
        this.ListOnlines.setEnabled(true);
        
        JOptionPane.showMessageDialog(this, "Você está conectado!");
    }
    
    private void disconnected()
    {
        this.btnConectar.setEnabled(true);
        this.txtUsername.setEnabled(true);
        this.btnDesconectar.setEnabled(false);
        this.btnEnviar.setEnabled(false);
        this.txtEnviar.setText("");
        String[] array = new String[0];
        this.ListOnlines.setListData(array);
        this.txtEnviar.setEnabled(false);
        this.txaChat.setEnabled(false);
        this.txaChat.setText("");
        this.ListOnlines.setEnabled(false);

        JOptionPane.showMessageDialog(this, "Você saiu da sala!");
    }
    
    private void receive(ChatMessage chat)
    {
        this.txaChat.append(chat.getUsername() + " diz: " + chat.getMensagem() + "\n");
    }
    
    private void refreshOnlines(ChatMessage chat)
    {
        Set<String> names = chat.getSetOnlines();
        
        String[] array = (String[]) names.toArray(new String[names.size()]);
        
        this.ListOnlines.setListData(array);
    }
    
    public void keyPressed(KeyEvent e)
    {
        if(e.getKeyCode() == KeyEvent.VK_ENTER)
        {
            enviarMensagem();
        }
    }
    
    public void enviarMensagem()
    {
        String username = this.txtUsername.getText();
        String mensagem = this.txtEnviar.getText();
        String ct = conferirChat(ListCidades.getSelectedValue(), ListTopicos.getSelectedValue());
        
        if(!mensagem.isEmpty())
        {
            this.chat = new ChatMessage();
            this.chat.setUsername(username);
            this.chat.setMensagem(mensagem);
            this.chat.setCity_topic(ct);
            this.chat.setAction(Action.SEND_ALL);

            this.service.send(this.chat);

            this.txaChat.append("Você disse: " + mensagem +"\n");
            this.txtEnviar.setText("");
        }
        else
        {
            JOptionPane.showMessageDialog(this, "Digite algo para enviar a mensagem!");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        ListCidades = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        ListTopicos = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtUsername = new javax.swing.JTextPane();
        jLabel2 = new javax.swing.JLabel();
        btnConectar = new javax.swing.JButton();
        btnDesconectar = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        txaChat = new javax.swing.JTextArea();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtEnviar = new javax.swing.JTextPane();
        btnEnviar = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        ListOnlines = new javax.swing.JList<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Concord");
        setBackground(new java.awt.Color(102, 102, 102));
        setName(""); // NOI18N

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));

        ListCidades.setBackground(new java.awt.Color(153, 153, 153));
        ListCidades.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        ListCidades.setForeground(new java.awt.Color(255, 255, 255));
        ListCidades.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Sorocaba", "Votorantim", "Itu", "Jundiaí" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        ListCidades.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        ListCidades.setSelectedIndex(0);
        ListCidades.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                ListCidadesValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(ListCidades);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mballem/app/apresentacao/images/Logo_Concord2.png"))); // NOI18N

        ListTopicos.setBackground(new java.awt.Color(153, 153, 153));
        ListTopicos.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        ListTopicos.setForeground(new java.awt.Color(255, 255, 255));
        ListTopicos.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Lixo", "Rios", "Energia", "Poluição" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        ListTopicos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        ListTopicos.setSelectedIndex(0);
        ListTopicos.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                ListTopicosValueChanged(evt);
            }
        });
        jScrollPane3.setViewportView(ListTopicos);

        jScrollPane2.setViewportView(txtUsername);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("User:");

        btnConectar.setText("Conectar");
        btnConectar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConectarActionPerformed(evt);
            }
        });

        btnDesconectar.setText("Desconectar");
        btnDesconectar.setEnabled(false);
        btnDesconectar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDesconectarActionPerformed(evt);
            }
        });

        txaChat.setEditable(false);
        txaChat.setBackground(new java.awt.Color(153, 153, 153));
        txaChat.setColumns(20);
        txaChat.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txaChat.setForeground(new java.awt.Color(255, 255, 255));
        txaChat.setRows(5);
        txaChat.setEnabled(false);
        jScrollPane4.setViewportView(txaChat);

        txtEnviar.setBackground(new java.awt.Color(153, 153, 153));
        txtEnviar.setEnabled(false);
        jScrollPane5.setViewportView(txtEnviar);

        btnEnviar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mballem/app/apresentacao/images/sendWhite2.png"))); // NOI18N
        btnEnviar.setEnabled(false);
        btnEnviar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnEnviarMousePressed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(102, 102, 102));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Onlines", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14), new java.awt.Color(255, 255, 255))); // NOI18N

        ListOnlines.setBackground(new java.awt.Color(102, 102, 102));
        ListOnlines.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        ListOnlines.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        ListOnlines.setForeground(new java.awt.Color(255, 255, 255));
        ListOnlines.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        ListOnlines.setEnabled(false);
        jScrollPane6.setViewportView(ListOnlines);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(btnConectar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnDesconectar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel2)
                                    .addGap(18, 18, 18)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane5)
                        .addGap(18, 18, 18)
                        .addComponent(btnEnviar))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 606, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(58, 58, 58)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(btnConectar)
                                    .addComponent(btnDesconectar)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 384, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnEnviar)))))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnEnviarMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEnviarMousePressed
        enviarMensagem();
    }//GEN-LAST:event_btnEnviarMousePressed

    private void btnConectarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConectarActionPerformed
        String ct = conferirChat(ListCidades.getSelectedValue(), ListTopicos.getSelectedValue());
        String username = this.txtUsername.getText();
        
        if(!username.isEmpty())
        {
            this.chat = new ChatMessage();
            this.chat.setAction(Action.CONNECT);
            this.chat.setUsername(username);
            this.chat.setCity_topic(ct);
            
            this.service = new ClienteService();
            this.socket = this.service.connect(this.ip);

            new Thread(new ListenerSocket(this.socket)).start();
            
            this.service.send(chat);
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Digite seu nome de usuário!");
        }
        
    }//GEN-LAST:event_btnConectarActionPerformed

    private void btnDesconectarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDesconectarActionPerformed
        desconectar();
    }//GEN-LAST:event_btnDesconectarActionPerformed

    private void ListCidadesValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_ListCidadesValueChanged
        if(btnDesconectar.isEnabled())
        {
            desconectar();
        }
    }//GEN-LAST:event_ListCidadesValueChanged

    private void ListTopicosValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_ListTopicosValueChanged
        if(btnDesconectar.isEnabled())
        {
            desconectar();
        }
    }//GEN-LAST:event_ListTopicosValueChanged
    
    private void desconectar()
    {
        ChatMessage cm = new ChatMessage();
        cm.setUsername(this.txtUsername.getText());
        cm.setCity_topic(conferirChat(ListCidades.getSelectedValue(), ListTopicos.getSelectedValue()));
        cm.setAction(Action.DISCONNECT);
        this.service.send(cm);
        disconnected();
    }
    
    private String conferirChat(String cidade, String topico)
    {
        String retorno = "";
        if(cidade.equals("Sorocaba"))
        {
            if(topico.equals("Lixo"))
            {
                retorno = "sl";
            }
            else if(topico.equals("Rios"))
            {
                retorno = "sr";
            }
            else if(topico.equals("Energia"))
            {
                retorno = "se";
            }
            else if(topico.equals("Poluiçao"))
            {
                retorno = "sp";
            }
        }
        else if(cidade.equals("Votorantim"))
        {
            if(topico.equals("Lixo"))
            {
                retorno = "vl";
            }
            else if(topico.equals("Rios"))
            {
                retorno = "vr";
            }
            else if(topico.equals("Energia"))
            {
                retorno = "ve";
            }
            else if(topico.equals("Poluiçao"))
            {
                retorno = "vp";
            }
        }
        else if(cidade.equals("Itu"))
        {
            if(topico.equals("Lixo"))
            {
                retorno = "il";
            }
            else if(topico.equals("Rios"))
            {
                retorno = "ir";
            }
            else if(topico.equals("Energia"))
            {
                retorno = "ie";
            }
            else if(topico.equals("Poluiçao"))
            {
                retorno = "ip";
            }
        }
        else if(cidade.equals("Jundiai"))
        {
            if(topico.equals("Lixo"))
            {
                retorno = "jl";
            }
            else if(topico.equals("Rios"))
            {
                retorno = "jr";
            }
            else if(topico.equals("Energia"))
            {
                retorno = "je";
            }
            else if(topico.equals("Poluiçao"))
            {
                retorno = "jp";
            }
        }
        return retorno;
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmHome.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmHome.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmHome.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmHome.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmHome().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<String> ListCidades;
    private javax.swing.JList<String> ListOnlines;
    private javax.swing.JList<String> ListTopicos;
    private javax.swing.JButton btnConectar;
    private javax.swing.JButton btnDesconectar;
    private javax.swing.JLabel btnEnviar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTextArea txaChat;
    private javax.swing.JTextPane txtEnviar;
    private javax.swing.JTextPane txtUsername;
    // End of variables declaration//GEN-END:variables
}
