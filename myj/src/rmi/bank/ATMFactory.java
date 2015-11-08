/**
 *  Manuel W. Mendez
 */
package rmi.bank;

/**
 * The ATMFactory interface defines the public interface
 * of the ATM factory. It consists of a single method, to get
 * a remote-aware instance of ATM.
 */
public interface ATMFactory extends java.rmi.Remote  {

   public ATM getATM()
      throws java.rmi.RemoteException;

}
