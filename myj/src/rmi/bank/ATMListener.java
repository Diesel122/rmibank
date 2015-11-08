/**
 *  Manuel W. Mendez
 */
package rmi.bank;

/**
 * The ATMListener interfaces defines how an object will
 * will receive ATMListener notification messages, as well as
 * and register, and unregister to receive (or not) such messages.
 */
public interface ATMListener extends java.rmi.Remote {

   // The caller will receive notifications in a TransactionNotification
   // object via handleNotification:
   public void handleNotification(TransactionNotification msg)
      throws java.rmi.RemoteException;

   // A listener can register itself to receive notifications via
   // this method. This method returns true if registration is successful.
   public boolean registerForNotifications(ATMListener listener)
      throws java.rmi.RemoteException;

   // A listener should unregister itself when done via this method
   public void unregisterForNotifications(ATMListener listener)
      throws java.rmi.RemoteException;

}
