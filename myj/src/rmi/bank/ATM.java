/**
 *  Manuel W. Mendez
 */
package rmi.bank;

/**
 * The ATM interface defines the public interface of the ATM.
 * The interface is an RMI enabled-interface.
 */
public interface ATM extends ATMListener, java.rmi.Remote {

   public void deposit(AccountInfo accountInfo, float amount)
      throws ATMException, java.rmi.RemoteException;

   public void withdraw(AccountInfo accountInfo, float amount)
      throws ATMException, java.rmi.RemoteException;

   public Float getBalance(AccountInfo accountInfo)
      throws ATMException, java.rmi.RemoteException;

   public void transfer(AccountInfo fromAccount, AccountInfo toAccount, float amount)
      throws ATMException, java.rmi.RemoteException;
}
