/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package respiteinjector;
 import org.apache.commons.cli.*;
import java.util.HashMap;
import java.util.Scanner;

/**
 *
 * @author user
 */
public class RespiteInjector {
  static Boolean DebugMode = true;
 public static boolean Connected = false;
  private FileConfig config = new FileConfig();
  public static final String ANSI_BRIGHT_RED    = "\u001B[91m";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.print(
"  ____                 _ _             \n" +
" |  _ \\ ___  ___ _ __ (_) |_ ___       \n" +
" | |_) / _ \\/ __| '_ \\| | __/ _ \\      \n" +
" |  _ <  __/\\__ \\ |_) | | ||  __/      \n" +
" |_|_\\_\\___||___/ .__/|_|\\__\\___|      \n" +
" |_ _|_ __  (_) |_|  ___| |_ ___  _ __ \n" +
"  | || '_ \\ | |/ _ \\/ __| __/ _ \\| '__|\n" +
"  | || | | || |  __/ (__| || (_) | |   \n" +
" |___|_| |_|/ |\\___|\\___|\\__\\___/|_| Made in Java \n" +
"          |__/                         \n\n");
        System.out.print("Respite HTTP Header injector Engine v0.11 by Alizer.\n");
        System.out.print("Decompilation or reuse of this tool is not prohibited unless you have my permission, it wasn't easy making this, you know.\n\n");
        Options options = new Options();
         
          
        Option input = new Option("p", "payload", true, "[Payload]");
        input.setRequired(true);
        options.addOption(input);
        input = new Option("li", "listenInterface", true, "[Address:Port]");
        input.setRequired(true);
        options.addOption(input);
        input = new Option("pr", "proxy", true,  "[Address:Port]");
        input.setRequired(true);
        options.addOption(input);
        input = new Option("r", "respite", true,  "[true/false]");
        input.setRequired(false);
        options.addOption(input);
        
        
        
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;
          try {
            cmd = parser.parse(options, args);
       
        String proxy = "proxy.jagoanssh.com";
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);
               System.out.print("\nExample:\nRespite -p \"CONNECT [host_port]@www.facebook.com [protocol][crlf] Host: www.facebook.com[crlf][crlf]\" -li \"127.0.0.1:1070\" -pr \"proxy.example.com:3128\" \n\n");

            System.exit(1);
        }
                  
        String payload = cmd.getOptionValue("payload");
        String listenIntf =cmd.getOptionValue("listenInterface");
        String proxy =cmd.getOptionValue("proxy");
        Injector injector;
         injector = new Injector(listenIntf.split(":")[0],Integer.parseInt( listenIntf.split(":")[1]), proxy.split(":")[0], Integer.parseInt( proxy.split(":")[1]))
          {
            public void onLogReceived(String log, int level, Exception e) {
              System.out.print(log);
            }
          };
         
        injector.setPayload(payload);
        
        new Thread(injector).start();
        
//         
//           
//  public Injector(String listeningAddr, int listeningPort, String proxyAddr, int proxyPort) throws IllegalArgumentException {
//    setListeningAddr(listeningAddr);
//    setListeningPort(listeningPort);
//    setProxyAddr(proxyAddr);
//    setProxyPort(proxyPort);
//  }
//  
    }
    public void startInjecting(String payload, String listenIntf, String proxy)
    {
    
    }
    
}
