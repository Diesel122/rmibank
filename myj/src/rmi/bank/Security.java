/**
 *  Manuel W. Mendez
 */
package rmi.bank;


/**
 * The Security interface defines the public interface that ATM
 * and Bank implementors use to check security, either locally (in case of
 * BankImpl) or remotely (in case of ATMImpl objects implementing ATM).
 * The interface is an RMI-enabled interface.
 */
public interface Security extends java.rmi.Remote {

   public boolean isAuthenticationOk(AccountInfo info)
      throws java.rmi.RemoteException;

   public boolean isAccountAtmAccessOk(AccountInfo info)
      throws java.rmi.RemoteException;

   public boolean isDepositOk(AccountInfo info)
      throws java.rmi.RemoteException;

   public boolean isWithdrawOk(AccountInfo info)
      throws java.rmi.RemoteException;

   public boolean isBalanceOk(AccountInfo info)
      throws java.rmi.RemoteException;

}
