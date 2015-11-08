/**
 *  Manuel W. Mendez
 */
package rmi.bank;


/**
 * The Bank interface defines the public interface of 'Bank' functionality.
 * The interface is an RMI interface as well, and is used for remote
 * invocation when ATM accesses Bank objects.
 */
public interface Bank extends java.rmi.Remote {

   public Account getAccount(AccountInfo info)
      throws java.rmi.RemoteException, ATMException;

}

