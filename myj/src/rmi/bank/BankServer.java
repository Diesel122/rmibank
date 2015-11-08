/**
 *  Manuel W. Mendez
 */
package rmi.bank;

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;

/**
 * The BankServer class registers the Bank and Security implementation
 * classes in the RMI registry.
 * As these objects are unique and service all ATMs (and by extension all clients)
 * no factories are needed.
 */
public class BankServer extends UnicastRemoteObject
{
   private static final String HOSTNAME = "localhost";
   private static final String IP = "localhost";
   private static final String BANK = "bank";
   private static final String SECURITY = "security";
   private static final String HOSTNAME_PROP = "java.rmi.server.hostname";
   private static String fullRmiName;

   /*** The security object co-local to BankServer and BankImpl ***/
   private static SecurityImpl securityImpl;

   /*** The bank object, co-local to BankServer and SecurityImp ***/
   private static BankImpl bankImpl;

   //// Constructor ////
   public BankServer() throws java.rmi.RemoteException
   {
      super();
   }


   /**
    * Entry point of BankServer, register implementors of Bank and Security
    *
    * @param
    *    args the entry point array vector.
    */
   public static void main(String args[])
   {
      // Create and install a security manager
      // System.setSecurityManager(new RMISecurityManager());
      try {
         // In my testing, the jvm never had access to a TCP name, not
         // even localhost, so we set up its hostname explicitly.
         // In the real world, the proper network name, allowable via
         // firewalls, would need to be found and passed to the jvm.
         String curHostname = System.getProperty(HOSTNAME_PROP);
         if (curHostname == null) {
            LogHelper.fine("BankServer: " + HOSTNAME_PROP +
            " is null and will be reset.");
            System.setProperty(HOSTNAME_PROP, HOSTNAME);
         }
         // Never can be too careful with RMI
         curHostname = System.getProperty(HOSTNAME_PROP);
         assert curHostname != null :
            "BankServer: Failed to tell the jvm the name of this server!";
         LogHelper.fine("BankServer: The jvm hostname is now " + curHostname);

         /*** Now we build & register the Bank and Security Implementors ***/
         buildObjects();
         registerSecurity();
         registerBank();

      } catch (Exception e) {
         System.err.println ("BankServer error: " + e.getMessage());
         e.printStackTrace();
         System.exit(-1);
      }
   }

   private static void buildObjects()
   {
      // Now create the single instances servicing the Security and Bank
      // interfaces, check for any exceptions:
      try {
         securityImpl = new SecurityImpl();
         if (securityImpl != null)
            bankImpl = new BankImpl(securityImpl);
      } catch (Exception e) {
         System.err.println ("Cannot build implementations of Security or Bank!");
         System.err.println ("BankServer Constructor error: " + e.getMessage());
         e.printStackTrace();
         System.exit(-1);
      }

      // Now double-check that all is well.
      if (securityImpl == null || bankImpl == null) {
          System.err.println ( "Can't create security and/or bank implementors!");
          System.exit(-1);
      }

      // Otherwise, log success!
      LogHelper.fine("BankServer buildObjects successfully built implementors!");
   }


   private static void registerBank ()
   {
      // Build the names to register in RMI
      fullRmiName = "//" + HOSTNAME + "/" + BANK;
      LogHelper.fine("BankServer registerBank called! fullRmiName will be "
         + fullRmiName);

      // Get an instance of Bank, feed security to it, and register it:
      try {
         Naming.rebind(fullRmiName, bankImpl);

         // Check that the object was actually registered
         Remote rem = Naming.lookup(fullRmiName);
         if (rem == null) {
            System.err.println(fullRmiName + " was not registered!");
            System.exit(1);
         }
      }
      catch (Exception e) {
         System.err.println("registerBank, Registering the Bank: ");
         System.err.println(e.getMessage());
         System.err.println("Unsuccessful RMI !!!!");
         e.printStackTrace();
         System.exit(1);
      }

      // Success!
      LogHelper.info("BankServer registerBank: Successfully registered "
      + fullRmiName);
   }


   private static void registerSecurity ()
   {
      // Build the names to register in RMI
      fullRmiName = "//" + HOSTNAME + "/" + SECURITY;
      LogHelper.fine("registerSecurity called! fullRmiName will be "
         + fullRmiName);

      // Get an instance of Security and register it:
      try {
         Naming.rebind(fullRmiName, securityImpl);

         // Check that the object was actually registered
         Remote rem = Naming.lookup(fullRmiName);
         if (rem == null) {
             System.err.println(fullRmiName + " was not registered!");
             System.exit(1);
          }
      }
      catch (Exception e) {
         System.err.println("registerSecurity, Registering Security: ");
         System.err.println(e.getMessage());
         System.err.println("Unsuccessful RMI !!!!");
         e.printStackTrace();
         System.exit(1);
      }

       // Success!!
      LogHelper.info ("BankServer registerSecurity: Successfully registered "
         + fullRmiName);
   }

} // end class BankServer
