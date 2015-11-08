/**
 *  Manuel W. Mendez
 */
package rmi.bank;

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;


/**
 *          <code> ATMImpl </code> implements ATM.
 *          The remote world acts via the ATM interface,
 *          yet it is ATMImpl the class that *actually* implements the
 *          behaviors specified in ATM
 */
public class ATMImpl extends UnicastRemoteObject implements ATM {

   private static Bank bank;         // Will point to the bank servicing this ATM
   private static Security security; // Will point to the security object servicing this ATM
   private static int cash;
   private static final int INITIAL_CASH = 500;


   // Implement the listeners as a Set (i.e. one listener reference per listener)
   static private final Set<ATMListener> listeners
      = new HashSet<ATMListener>();

   //// Constructor ////

/**
 *  <code> ATMImpl </code> constructor
 *        
 *	 Throws a remote exception in case RMI cannot be started at localhost
 */
   public ATMImpl() throws java.rmi.RemoteException
   {
      super(); // Call any RMI work to do

      LogHelper.fine("AtmImpl: constructor begun!");
      cash = INITIAL_CASH; // The ATM begins with a set cash amount

      // Now get a reference to bank and security
      try {
         bank = (Bank) Naming.lookup("//localhost/bank");
         security = (Security) Naming.lookup("//localhost/security");
      }
      catch (Exception e) {
         System.err.print("ATMImpl, (implementor of ATM) Error: ");
         System.err.println("Unsuccessful RMI Bank and/or Security Lookups!");
         e.printStackTrace();
         System.exit(-1);
      }

      // Otherwise, write success to the log:
      LogHelper.fine("AtmImpl: constructor finished successfully!");
   }

   /*** private functions ***/

   /**
    * check that the account authenticates, if not throw an exception
    *
    * @param amount
    *    AccountInfo, specifying the account and its pin
    */
   private void checkAuthentication(AccountInfo info)
      throws ATMException, RemoteException
   {
      if (security.isAuthenticationOk(info) == false) {
         LogHelper.fine("checkAuthentication: authentication failed!");
         throw new ATMException("Account failed to authenticate!");
      }
   }


   /*** ATM Interface Implementation Methods ***/

   /**
    * On behalf of a client, deposit a given amount
    *
    * @param amount
    *    AccountInfo, specifying the account and its pin
    *    amount, the amount to deposit
    */
   @Override
   public void deposit(AccountInfo info, float amount)
      throws ATMException, RemoteException {

         sendNotification(info, null, Operation.DEPOSIT, amount);
         checkAuthentication(info); // throws exception if we fail.
         if (security.isDepositOk(info) == true) {
            // This operation is allowed...
            Account account = bank.getAccount(info);
            account.deposit(amount);
            // We do not add deposits to the ATM's cash... they could be
            // checks, or even if bills, the ATM cannot open the deposit
            // envelopes and re-sort the bills...
         }
         else {
            LogHelper.fine("AtmImpl, deposit: Not authorized for deposit!");
            throw new ATMException ("Not authorized to deposit!");
         }
   }

   /**
    * On behalf of a client, withdraw a given amount
    *
    * @param amount
    *    AccountInfo, specifying the account and its pin
    *    amount, the amount to withdraw
    */
   @Override
   public void withdraw(AccountInfo info, float amount)
      throws ATMException, RemoteException {

         // No need to proceed further if not enough cash!
         if (cash - amount < 0)
            throw new ATMException ("Not enough cash on hand at this ATM! Cannot proceed!");

         sendNotification(info, null, Operation.WITHDRAW, amount);
         checkAuthentication(info); // throws exception if we fail.
         if (security.isWithdrawOk(info) == true) {
            // This operation is allowed...
            Account account = bank.getAccount(info);
            account.withdraw(amount);
            cash -= amount;
         }
         else {
            LogHelper.fine("AtmImpl, withdraw: Not authorized to withdraw!");
            throw new ATMException ("Not authorized to withdraw!");
         }
   }

   /**
    * On behalf of a client, request a balance
    *
    * @param
    *    info - specifying the account and its pin
    *
    * @return
    *    The current balance
    */
   @Override
   public Float getBalance(AccountInfo info)
      throws ATMException, RemoteException {

         LogHelper.finer("AtmImpl, getBalance called!");
         sendNotification(info, null, Operation.BALANCE, 0.0f);
         checkAuthentication(info); // throws exception if we fail.
         if (security.isBalanceOk(info) == true) {
            Account account = bank.getAccount(info);
            return account.getBalance();
         }
         else {
            LogHelper.fine("AtmImpl, getBalance: Not authorized to get a balance!");
            throw new ATMException ("Not authorized to see balances!");
         }
   }

   /**
    * On behalf of a client, transfer from one account to another
    *
    * @param
    *    fromAccount - specifying the source account and its pin
    *    toAccount - specifying the destination account and its pin
    *    amount - the amount to transfer
    *
    */
   @Override
   public void transfer(AccountInfo fromAccount, AccountInfo toAccount, float amount)
      throws ATMException, RemoteException {

         sendNotification(fromAccount, toAccount, Operation.TRANSFER, amount);

         checkAuthentication(fromAccount); // throws exception if we fail.
         checkAuthentication(toAccount); // throws exception if we fail.

         // To transfer, we must have withdraw privs in fromAccount, and
         // then deposit privs in toAccount

         if (security.isWithdrawOk(fromAccount) == true &&
             security.isDepositOk(toAccount) == true) {
            // The transfer operation is ok, proceed.
            Account from = bank.getAccount(fromAccount);
            Account to = bank.getAccount(toAccount);
            from.withdraw(amount);
            to.deposit(amount);
            // Success assumed, in a real implementation the withdrawal
            // might need rollback if the deposit, for some reason, failed.
         }
         else {
            LogHelper.fine("AtmImpl, transfer: Transfer not allowed!");
            throw new ATMException ("Not authorized to transfer!");
         }
   } // end transfer

   /*** ATMListener Implementation Methods ***/

   /**
    * If this ATM receives a notification, print it.
    *
    * @param
    *    msg - the transaction notification sent by a third party
    *
    */
   @Override
   public void handleNotification(TransactionNotification msg) {

      // Print the notification received.
      System.out.println(msg.toString());

   }

   /**
    * If a third party wants to be notified about events handled by this
    * particular ATMImpl instance, the third party calls this method and
    * registers itself as a listener.
    *
    * @param
    *    listener - the listener requesting future notifications
    *
    */
   @Override
   public boolean registerForNotifications(ATMListener listener) {

      // A listener has asked us to register with us, we store a
      // reference and will call the registered object when we choose
      if (listeners.contains(listener)) return true; // No need to add
      LogHelper.fine("ATMImpl adding listener for notifications! listener is: "
         + listener);
      return listeners.add(listener);
   }

   /**
    * When third parties that requested notifications of events no longer
    * want to be notified of events, say when they finish their work, these
    * instances should call this method.
    *
    * @param
    *    listener - the listener no longer requesting future notifications
    *
    */
   @Override
   public void unregisterForNotifications(ATMListener listener) {

      // A (hopefully) previously registered listener has asked us to
      // unregister with us once it is done
      if (listeners.contains(listener)) {
         LogHelper.fine("ATMImpl removing a listener perviosuly registered: "
            + listener);
         listeners.remove(listener);
      }
      else
         LogHelper.warn("ATMImpl asked to remove an unregistered a listener: "
            + listener);
   }



   /*** Other private functions ***/

   // Helper function to send a Notification message to all listeners.
   private void sendNotification (AccountInfo info1, AccountInfo info2,
      Operation operation, Float amount) {

      LogHelper.finer ("ATMImpl sendNotification called with operation " +
         operation);

      // Create a new encapsulation of the TransactionNotification
      // containing a message
      TransactionNotification msg =
         new TransactionNotification (info1, info2, operation, amount);

      // Call all registered callers.
      Iterator itr = listeners.iterator();
      while (itr.hasNext()) {
         ATMListener listener = (ATMListener) itr.next();
         try {
            LogHelper.finer ("Sending notification to listener " + listener);
            listener.handleNotification(msg); // Call each listener.
         }
         catch (Exception e) {
            // We log and print to stderr, cannot do much more...
            System.err.println("AtmImpl, Failed to call a handler!");
            LogHelper.fine("AtmImpl, Failed to call a handler!");
            e.printStackTrace();
         } // catch
      } // while
   } // sendNotification


}  // end ATMImpl



