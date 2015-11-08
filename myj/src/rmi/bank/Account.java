/**
 *  Manuel W. Mendez
 */
package rmi.bank;

/**
 * The <code> Account </code> interface defines the public interface of Account.
 * The interface is an RMI interface as well. The BankImpl object
 * holds objects of type AccountImpl, which implement Account, and
 * which are accessed remotely via Bank and Account by ATMImpl, on behalf of
 * Client(s). Clients never interact with Account or AccountImpl
 * directly, and the accounts themselves (as AccountImpl) remain in
 * BankImpl.
 */
public interface Account extends java.rmi.Remote {

   public void setId(int newId) throws java.rmi.RemoteException;

   public int id() throws java.rmi.RemoteException;

   public float deposit (float amount) throws ATMException, java.rmi.RemoteException;

   public float withdraw (float amount) throws ATMException, java.rmi.RemoteException;

   public float getBalance () throws java.rmi.RemoteException;

}
