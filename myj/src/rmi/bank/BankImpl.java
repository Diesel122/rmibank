/**
 *  Manuel W. Mendez
 */
package rmi.bank;

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;

/**
 *         <code> BankImpl </code> implements the
 *         behavior specified by Bank. Bank indicates that BankImpl
 *         will contain (or have access) to Account(s). Bank specifies
 *         how these accounts can be accesed, i.e.
 *         via <code> getAccount </code> and the remote interface
 *         <code> Account </code>.
 *
 */
public class BankImpl extends UnicastRemoteObject implements Bank {

   private static final Accounts accounts = new Accounts();
   private static Security security;

   //// Constructor ////
   public BankImpl(Security sec) throws java.rmi.RemoteException
   {
      super(); // Call any RMI work to do
      security = sec; // Security set and passed in by BankServer
      LogHelper.fine("Bank: constructor completed!");
   }

   /**
    * ATMImpl objects (implementing ATM) request a remote reference to
    * an AccountImpl via the Account interface using <code> getAccount </code>
    *
    * @param
    *    info   the account id and its pin
    *
    */
   @Override
   public Account getAccount(AccountInfo info)
      throws java.rmi.RemoteException, ATMException
   {
      // We verify that the AccountInfo can be sent to an ATM (not all
      // accounts in a bank are necessarily
      // accessible in ATMs -- hence I have added an additional ad-hoc
      // security method called isAccountAtmAccessOk).
      if (security.isAuthenticationOk(info) == true &&
         security.isAccountAtmAccessOk(info) == true)
            return accounts.get(info.getId());
      else {
         LogHelper.fine("BankImpl, getAccount: access to account prohibited!");
         throw new ATMException ("BankImpl, getAccount: Account not ATM Accessible!!");
      }
   }

 } // end Bank
