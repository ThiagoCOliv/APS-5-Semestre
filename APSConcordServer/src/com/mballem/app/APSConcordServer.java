package com.mballem.app;

import com.mballem.app.apresentacao.frmServer;
import com.mballem.app.service.ServidorService;

public class APSConcordServer 
{
    public static void main(String[] args) 
    {
        frmServer frmS = new frmServer();
        frmS.setVisible(true);
        ServidorService sl = new ServidorService();
    }
}
