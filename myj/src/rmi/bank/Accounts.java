/**
 *  Manuel W. Mendez
 */
package rmi.bank;

import java.util.Map;
import java.util.HashMap;

/**
 *         The class <code> Accounts </code> holds
 *         individual accounts of type AccountImpl implementing the
 *         behavior specified by the interface <code> Account </code>.
 *
 *          Notice that Accounts is not RMI-remote enabled, but Account is.
 *          Accounts is used by Bank to store individual AccountImpl instances,
 *          which are doled (as remote RMI objects) to ATM instances as needed
 *          by BankImpl via the Account interface.
 */
public class Accounts {

   /*** State variables for class Accounts */
   // The accounts themselves, implemented as a Map at this time.
   private Map<Integer, Account> accounts = new HashMap<Integer, Account>();

   /* Constructor for Accounts */
   public Accounts() {
      // In an actual implementation, we would get ready to retrieve
      // accounts, probably by establishing a link to a database. Here
      // we create three accounts for the final project, with the initial
      // balances as specified
      LogHelper.fine("Accounts: preloading three accounts! ");
      int i;
      try {
         for (i = 1; i <= 3; i++) {
            Account account = new AccountImpl();
            account.setId(i);
            if (i == 2) account.deposit(100);
            if (i == 3) account.deposit(500);
            accounts.put(i, account);
         }
      }
      catch (Exception e)
      {
         assert false : "Unable to seed Accounts with initial balances!";
         LogHelper.warn("Failed to seed accounts with initial balances!");
         return;
      }

      // Log success
      LogHelper.finer("Constructor of Accounts Completed! Accounts seeded.");
   }

   /**
    * <code> get </code> returns the account for a given id.
    *
    * @param id
    *            the id of the account to retreive
    * @return 
    *			the account matching id, or null in case no account is found
    */
   public Account get(int id) throws ATMException {
      LogHelper.finer ("Accounts: Request for account received. id = " + id);
      Account account = accounts.get(id);
      if (account == null) {
         LogHelper.warn ("Accounts: Request for non-existent id: " + id);
         throw new ATMException ("No account for this id!");
      }
      return account;
   } // end method get

} // end class Accounts

