/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package respiteinjector;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
/**
 *
 * @author user
 */
public class Injector   implements Loggable, Runnable {
    
  public static final int TIME_OUT_SERVER_LISTENNING = 2000;
  private String listeningAddr;
  private int listeningPort;
  private String proxyAddr;
  private int proxyPort;
  private String payload;
  private boolean isRunning;
  
  public Injector(Config config)
    throws IllegalArgumentException
  {
    this(config.getListenHost(), config.getListenPort(), config.getProxyHost(), config.getProxyPort());
  }
  
  public Injector(String listeningAddr, int listeningPort, String proxyAddr, int proxyPort) throws IllegalArgumentException {
    setListeningAddr(listeningAddr);
    setListeningPort(listeningPort);
    setProxyAddr(proxyAddr);
    setProxyPort(proxyPort);
  }
  
  public String getListeningAddr() {
    return listeningAddr;
  }
  
  public void setListeningAddr(String listeningAddr) throws IllegalArgumentException {
    if ((listeningAddr == null) || (listeningAddr.length() == 0)) {
      throw new IllegalArgumentException("<#> Address empty! How did you get past the guards?");
    }
    this.listeningAddr = listeningAddr;
  }
  
  public int getListeningPort() {
    return listeningPort;
  }
  
  public void setListeningPort(int listeningPort) {
    this.listeningPort = listeningPort;
  }
  
  public String getProxyAddr() {
    return proxyAddr;
  }
  
  public void setProxyAddr(String proxyAddr) throws IllegalArgumentException {
    if ((proxyAddr == null) || (proxyAddr.length() == 0)) {
      throw new IllegalArgumentException("<#> Proxy is empty! How did you get past the guards.");
    }
    this.proxyAddr = proxyAddr;
  }
  
  public int getProxyPort() {
    return proxyPort;
  }
  
  public void setProxyPort(int proxyPort) {
    this.proxyPort = proxyPort;
  }
  
  public String getPayload() {
    return payload;
  }
  
  public void setPayload(String payload) {
    this.payload = payload;
  }
  
  public boolean isRunning() {
    return isRunning;
  }
  
  public void stop() {
    isRunning = false;
  }
  static int timeout = 2000;
  public void start() throws IOException {
    ServerSocket serverSocket = null;
    try {
    
      serverSocket = serverFactory(listeningAddr, listeningPort);
      serverSocket.setSoTimeout(timeout);
      
      isRunning = true;
        new  GlobalVars().logThis("<-> Binding local interface " + listeningAddr + ":" + listeningPort);
      try {
        do {
          for (;;) {
            Socket acceptCliente = serverSocket.accept();
               new  GlobalVars().logThis("<-> Request received." );
            
            Host hostProxy = new Host(proxyAddr, proxyPort);
            Host hostCliente = new Host(acceptCliente);
            
            RequisicaoInject reqInject = new RequisicaoInject(hostProxy, hostCliente, acceptCliente.getPort())
            {
              public void onLogReceived(String log, int level, Exception e) {
                Injector.this.onLogReceived(log, level, e);
              }
            };
            reqInject.setPayload(payload);
            
            new Thread(reqInject).start();
          }
          
        } while (isRunning());
      }
      catch (SocketTimeoutException e) {
        if (serverSocket != null){
        serverSocket.close();}
      new  GlobalVars().logThis("<-> Socket taking too long to recieve, retrying with infinite timeout.");
      
       timeout = 90000;
       start();
      }
    }
    finally {
      isRunning = false;
      if (serverSocket != null)
        serverSocket.close();
       new  GlobalVars().logThis("<-> Server listening interrupted.");
   
    }
  }
  
  public void run()
  {
    try {
      start();
    } catch (IOException e) {
        new  GlobalVars().logThis("<#> Server Error. " + e.getMessage() );
      
    }
  }
  
  private ServerSocket serverFactory(String addr, int port) throws IOException {
    ServerSocket server = new ServerSocket(
            port, 50, InetAddress.getByName(addr));
    
    return server;
  }
  
  public void onLogReceived(String log, int level, Exception e) {
  new  GlobalVars().logThis(log);
  }
}
