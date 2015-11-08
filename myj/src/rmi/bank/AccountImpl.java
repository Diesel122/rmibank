/**
 *  Manuel W. Mendez
 */
package rmi.bank;

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;

/**
 * The class <code> AccountImpl </code> implements the basic behavior of an
 * account as specified by the <code> Account </code> interface,
 * i.e. storing a balance.
 *
 * This AccountImpl does allow for the storage and running balances with
 * precision beyond $0.01, say, to calculate daily interest. Whether this
 * makes sense or not, depends on the bank's requirements.
 *
 * AccountImpl instances are remote RMI object, and they resides in the Accounts
 * class (itself not an RMI) object, which resides in turn in BankImpl. BankImpl
 * doles the appropriate Account via the Bank RMI interface.
 */
public class AccountImpl extends UnicastRemoteObject implements Account {

   //// State variables ////

   private float balance;  // The balance
   private int id;         // The ID

   //// Constructor ////

   /**
    * Constructor for AccountImpl. Use <code> AccountImpl </code> to create a new
    * instance of an account implementation.
    */
   public AccountImpl() throws RemoteException {

   // In the real world, the balance would be obtained from reading the
   // database
      balance = 0.0f;

   }

   //// Member functions ////

    /**
    * Use <code> setId </code> to set an instance of AccountImpl to its
    * unique identifier
    */
   @Override
   public void setId(int newId) throws RemoteException
   {
      // In the real world, we would call an object/interface that would
      // give us a unique ID, that would make some sense (say per branch,
      // city, last name).
      id = newId;
      LogHelper.finer("AccountImpl set id to " + id);
   }

   /**
    * <code> deposit </code> implements increasing the Account's balance by
    * the amount deposited.
    *
    * @param amount
    *    The amount to deposit.
    * @return balance
    *    The current balance, which assumes that all
    *    deposits clear instantly and in full.
    **/
    @Override
    public float deposit (float amount) throws ATMException
    {
      if (amount < 0) {
         LogHelper.fine (" Avoided the deposit of a negative amount:" + id);
         throw new ATMException ("Cannot deposit negative amounts!");
      }

      if (amount == 0) {
         LogHelper.fine (" Avoided depositing zero:" + id);
         throw new ATMException ("Cannot deposit a zero amount!");
      }

      balance += amount;
      return balance;
    }

   /**
    * <code> withdraw </code> implements decreasing the account's balance by
    * the amount deposited.
    *
    * @param amount
    *    The amount to withdraw
    * @return
    *    The current balance. This implementation
    *    assumes that no overdrafts are possible.
    **/
    @Override
    public float withdraw (float amount) throws ATMException
    {
      if (amount < 0) {
         LogHelper.fine (" Avoided the withdrawing of a negative amount:" + id);
         throw new ATMException ("Cannot withdraw negative amounts!");
      }

      if (balance - amount < 0) {
         LogHelper.fine (" Avoided overdraft for account with id:" + id);
         throw new ATMException ("Overdrafts not allowed!");
      }

      balance -= amount;
      return balance;
    }

   /**
    * <code> getBalance </code> implements returning the account's balance
    *
    * @return
    *    The current balance
    **/
    @Override
    public float getBalance () throws RemoteException
    {
      return balance;
    }

   /**
    * <code> getId </code> implements retrieving the current id for this
    * account.
    *
    * @return
    *    The account id
    *
    */
    @Override
   public int id() throws RemoteException
   {
      return id;
   } // End id method


} // End class AccountImpl
