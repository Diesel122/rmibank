/**
 *  Manuel W. Mendez
 */
package rmi.bank;

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;

/**
 * The ATMServer class registers the ATMFactoryImpl in the RMI registry,
 * then the registry doles out ATMImpl instances to remote clients,
 * that, in turn, access these via the ATM interface.
 */
public class ATMServer extends UnicastRemoteObject
{
   private static final String HOSTNAME = "localhost";
   private static final String IP = "localhost";
   private static final String FACTORY = "atmfactory";
   private static final String HOSTNAME_PROP = "java.rmi.server.hostname";
   private static String fullRmiName;

   //// Constructor ////
   public ATMServer() throws java.rmi.RemoteException
   {
      super();
   }


   /**
    * Entry point of the server, simply register the ATMfactory
    *
    * @param
    *    args  the entry point array vector.
    */
   public static void main(String args[])
   {

   // Build the names to register in RMI
   fullRmiName = "//" + HOSTNAME + "/" + FACTORY;
   LogHelper.fine("ATMServer main called! fullRmiName will be " + fullRmiName);

   // Create and install a security manager
   // System.setSecurityManager(new RMISecurityManager());
   try {
      // In my testing, the jvm never had access to a TCP name, not
      // even localhost, so we set up its hostname explicitly.
      // In the real world, the proper network name, allowable via
      // firewalls, would need to be found and passed to the jvm.
      String curHostname = System.getProperty(HOSTNAME_PROP);
      if (curHostname == null) {
         LogHelper.fine("ATMServer: " + HOSTNAME_PROP +
         " is null and will be reset.");
         System.setProperty(HOSTNAME_PROP, HOSTNAME);
      }
         // Never can be too careful with RMI
      curHostname = System.getProperty(HOSTNAME_PROP);
      assert curHostname != null :
         "Failed to set the jvm the name of this server!";
      LogHelper.fine("ATMServer: The jvm hostname is now " + curHostname);

      // Get an instance of our Factory and register it:
      ATMFactoryImpl factoryImpl = new ATMFactoryImpl();
      Naming.rebind(fullRmiName, factoryImpl);

      // Check that the object was actually registered
      Remote rem = Naming.lookup(fullRmiName);
      if (rem == null) {
          System.err.println(fullRmiName + " was not registered!");
          System.exit(1);
       }
      LogHelper.info("ATMServer: Successfully registered " + fullRmiName);
   } catch (Exception e) {
       System.err.println ("ATMServer error: " + e.getMessage());
       e.printStackTrace();
       System.exit(1);
       }
   } // End of function main

} // End of class ATMServer
