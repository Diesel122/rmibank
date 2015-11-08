  package rmi.bank;

  import java.rmi.*;
  import java.rmi.server.UnicastRemoteObject;
  import java.net.MalformedURLException;
  import java.rmi.Naming;
  import java.rmi.NotBoundException;
  import java.rmi.RemoteException;
  import java.rmi.UnknownHostException;

 /**
  * <code> Client </code> is the test class for the Final Project.
  * The test code is specified by the final project specs
  */
  public class Client extends UnicastRemoteObject implements ATMListener {

  // localhost is set to contain all objects
  private static final String AMT_FACTORY = "//localhost/atmfactory";

   // A reference to ourselves as class, so that we can handle notifications
   // (need an instance of ourselves, versus just a main entrypoint!)
   private static Client notificationHandler;

   //// Constructor ////
   Client() throws RemoteException {
      super();
   }

   /*** Implementations of ATMListener, so that we can Handle Notifications ***/

   @Override
   public void handleNotification(TransactionNotification msg) {

      // Log and Print all notifications received
      // Note: In this project we do create notifications for BALANCE operations
      // and print them out --our output will be slightly different than
      // the one specified in the specs when printBalances() is called as
      // the balances will be interleaved with the notifications themselves.
      LogHelper.fine("Received notification " + msg.toString());
         System.out.println();
         System.out.println(msg.toString());
   }

   // We do not generate notifications as a client, nor do we register or
   // unregister listeners. The overrides of ATMListener for register and
   // unregister are implemented accordingly below:

   /**
    * Return false to all callers wanting to register for notifications
    * A <code> Client </code> never registers a listener
    *
    * @return
    *    false
    *
    */
   @Override
   public boolean registerForNotifications(ATMListener listener) {
      LogHelper.warn ("Client: Incorrect notification registration request!");
      return false;  // We are a destination, not a source of messages
   }

   /**
    * Return and log a warning when a Client is called by a third party to
    * unregister. Clients are sinks, but never sources of notifications.
    *
    */
   @Override
   public void unregisterForNotifications(ATMListener listener) {
      LogHelper.warn ("Client: Incorrect notification unregistration request!");
      return;  // We do nothing (we do not keep a register of listeners as
               // we do not generate notifications, only receive them.
   }


   /*** Testing Routines (code copied from the spec) ***/

   /**
    * Run tests
    */
    public static void testATM(ATM atm) {
      if (atm!=null) {
         printBalances(atm);
         performTestOne(atm);
         performTestTwo(atm);
         performTestThree(atm);
         performTestFour(atm);
         performTestFive(atm);
         performTestSix(atm);
         performTestSeven(atm);
         performTestEight(atm);
         performTestNine(atm);
         printBalances(atm);
      }
   }

   public static void printBalances(ATM atm) {
      try {
         System.out.println("Balance(0000001): "+atm.getBalance(getAccountInfo(0000001, 1234)));
         System.out.println("Balance(0000002): "+atm.getBalance(getAccountInfo(0000002, 2345)));
         System.out.println("Balance(0000003): "+atm.getBalance(getAccountInfo(0000003, 3456)));
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public static void performTestOne(ATM atm) {
      try {
         atm.getBalance(getAccountInfo(0000001, 5555));
      } catch (Exception e) {
         System.out.println("Failed as expected: "+e);
      }
   }

   public static void performTestTwo(ATM atm) {
      try {
         atm.withdraw(getAccountInfo(0000002, 2345), 500);
      } catch (Exception e) {
         System.out.println("Failed as expected: "+e);
      }
   }

   public static void performTestThree(ATM atm) {
      try {
         atm.withdraw(getAccountInfo(0000001, 1234), 50);
      } catch (Exception e) {
         System.out.println("Failed as expected: "+e);
      }
   }

   public static void performTestFour(ATM atm) {
      try {
         atm.deposit(getAccountInfo(0000001, 1234), 500);
      } catch (Exception e) {
         System.out.println("Unexpected error: "+e);
      }
   }

   public static void performTestFive(ATM atm) {
      try {
         atm.deposit(getAccountInfo(0000002, 2345), 100);
      } catch (Exception e) {
         System.out.println("Unexpected error: "+e);
      }
   }

   public static void performTestSix(ATM atm) {
      try {
         atm.withdraw(getAccountInfo(0000001, 1234), 100);
      } catch (Exception e) {
         System.out.println("Unexpected error: "+e);
      }
   }

   public static void performTestSeven(ATM atm) {
      try {
         atm.withdraw(getAccountInfo(0000003, 3456), 300);
      } catch (Exception e) {
         System.out.println("Unexpected error: "+e);
      }
   }

   public static void performTestEight(ATM atm) {
      try {
         atm.withdraw(getAccountInfo(0000001, 1234), 200);
      } catch (Exception e) {
         System.out.println("Failed as expected: "+e);
      }
   }

   public static void performTestNine(ATM atm) {
      try {
         atm.transfer(getAccountInfo(0000001, 1234),getAccountInfo(0000002, 2345), 100);
      } catch (Exception e) {
         System.out.println("Unexpected error: "+e);
      }
   }

   /*** Helper functions ***/
   private static AccountInfo getAccountInfo (int accountId, int pin) {
      return new AccountInfo (accountId, pin);
   }

   /*** Main ***/

   /**
    * Main entry point for the test program for the final project
    *
    * @param
    *    args   the entry point array vector.
    */
     public static void main(String[] args) {
        ATM atm = null;
        try {

           // Get an ATM object and self-register as a listener.
           LogHelper.finer ("Client is looking up " + AMT_FACTORY);
           ATMFactory factory = (ATMFactory)Naming.lookup(AMT_FACTORY);
           assert factory != null : "Client failed to obtain an ATMFactory!";
           LogHelper.finer ("Client is requesting at ATM from ATMFactory");
           atm = factory.getATM();

           // Stop if failure:
           if (atm == null)
            {
               // Write to error logs and exit with -1 to signify failure:
               LogHelper.warn("Client unable to instantiate the ATM object!");
               System.err.println ("Unable to test the ATM, exiting with status -1!");
               System.exit(-1); // set unix exit status just to be good citizens
            }

           // We register for notifications
           LogHelper.finer ("Client will set itself as a notifications handler");
           notificationHandler = new Client();
           atm.registerForNotifications(notificationHandler);

        } catch (MalformedURLException mue) {
           mue.printStackTrace();
        } catch (NotBoundException nbe) {
           nbe.printStackTrace();
        } catch (UnknownHostException uhe) {
           uhe.printStackTrace();
        } catch (RemoteException re) {
           re.printStackTrace();
        }

        /* Run the tests */
        testATM(atm);

        // Cleanup
        try
        {
            atm.unregisterForNotifications(notificationHandler);
        }
        catch (RemoteException re)
        {
            LogHelper.fine("Client Unable to unregister for notifications!");
         }
         System.exit(0); // exit with success

     } // End Main


  } // End Class Client


