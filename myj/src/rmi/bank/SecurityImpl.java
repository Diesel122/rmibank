/**
 *  Manuel W. Mendez
 */
package rmi.bank;

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

/**
 *         <code> SecurityImpl </code> implements the permissions
 *         and the methods to check on allowable operations
 *         on Account access and operations. It implements the RMI-aware
 *         Security interface, which is used by ATMImpl and BankImpl to
 *         check security.
 *
 */
public class SecurityImpl extends UnicastRemoteObject implements Security {

   /*** State variables for class Security */

   // Permissions, implemented as a Set at this time, of
   // Account id (but not stored in Accounts, as it might be a bit more
   // dangerous to store them in the accounts themselves. By having
   // them separatedly inside SecurityImpl, a compromise of Accounts records
   // or even of BankImpl would still keep the pins and permissions safe.
   //
   // If an account id is present in these sets, then it has the permission.

   static private final Set<Integer> withdrawPermissions
      = new HashSet<Integer>();

   static private final Set<Integer> depositPermissions
      = new HashSet<Integer>();

   static private final Set<Integer> balancePermissions
      = new HashSet<Integer>();

   // Pins for authentication certainly not stored in AccountImpl records
   static private final Map<Integer, Integer> pins
      = new HashMap<Integer, Integer>();

   public SecurityImpl() throws java.rmi.RemoteException
   {
      super(); // Call any RMI work to do
      LogHelper.fine("Security: constructor called!");

      // Preload security values (from a database in a real implementation)
      depositPermissions.add(1); withdrawPermissions.add(1); balancePermissions.add(1);
      depositPermissions.add(2); balancePermissions.add(2);
      withdrawPermissions.add(3); balancePermissions.add(3);
      LogHelper.fine("Security: constructor preloaded account permissions!");

      // Load pins (from a pin database in a real implementation)
      pins.put(1,1234);
      pins.put(2,2345);
      pins.put(3,3456);
      LogHelper.fine("Security: constructor preloaded account pins!");

   }

   /**
    * <code> isAuthenticationOk </code> returns true if the AccountInfo
    * object is correct (i.e. the account id exists and the pin provided
    * matches the account's actual pin.)
    *
    * @return
    *    true iff the account authenticates.
    */
   @Override
   public boolean isAuthenticationOk(AccountInfo info)
   {
      // First, the account must be a known account with a pin:
      int accountId = info.getId();
      boolean ok = pins.containsKey(accountId);
      if (ok == false) {
         LogHelper.fine ("SecurityImpl: No pin for account: " + accountId);
         return false;
      }

      // Second, the pins must match
      ok = (pins.get(accountId)) == info.getPin();
      if (ok == false) {
         LogHelper.fine ("SecurityImpl: Mismached pin for account: " + accountId);
         return false;
      }

      // Success
      LogHelper.finer ("SecurityImpl: authorization ok for account: " + accountId);
      return true;
   }

   /**
    * <code> isAuthenticationOk </code> returns true if the account id provided
    * is ATM-enabled. Currently, this routine is a stub and always returns true.
    * (In an actual bank, not all accounts are ATM-accessible)
    *
    * @return
    *    true, always, at this point.
    */
   @Override
   public boolean isAccountAtmAccessOk(AccountInfo info)
   {
      LogHelper.finer("isAccountAtmAccessOk returning true for " + info);
      return true; // Wide open security for future implementation
   }

   /**
    * <code> isDepositOk </code> returns true if the AccountInfo
    * object permits the Deposit operation.
    *
    * @return
    *    true iff the account allows deposits.
    */
   @Override
   public boolean isDepositOk(AccountInfo info)
   {
      boolean ok = depositPermissions.contains(info.getId());
      LogHelper.finer("isDepositOk returning " + ok +
         "for account " + info.getId());
      return ok;
   }

   /**
    * <code> isWithdrawOk </code> returns true if the AccountInfo
    * object permits the withdraw operation.
    *
    * @return
    *    true iff the account allows withdrawals.
    */
   @Override
   public boolean isWithdrawOk(AccountInfo info)
   {
      boolean ok = withdrawPermissions.contains(info.getId());
      LogHelper.finer("isWithdrawOk returning " + ok +
         " for account " + info.getId());
      return ok;
   }

   /**
    * <code> isBalanceOk </code> returns true if the AccountInfo
    * object permits the balance inquiry operation.
    *
    * @return
    *    true iff the account allows balance inquiries.
    */
   @Override
   public boolean isBalanceOk(AccountInfo info)
   {
      boolean ok = balancePermissions.contains(info.getId());
      LogHelper.finer("isBalanceOk returning " + ok +
         " for account " + info.getId());
      return ok;
   }

 } // end Bank
