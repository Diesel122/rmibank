/**
 *  Manuel W. Mendez
 */
package rmi.bank;

import java.io.Serializable;

/**
 *         The class <code> AccountInfo </code> encapsulates
 *         the account number and a PIN. It is used in the ATM simulation
 *         to signify an account that can, in turn, be validated (via its PIN)
 *         and authorized to carry ATM interface operations.
 */
public class AccountInfo implements Serializable {

   /*** State variables for class AccountInfo */
   private int account_id;    // The account id
   private int pin;           // A pin provided for this account

   //// Constructor for AccountInfo ////
   public AccountInfo(int creation_id, int creation_pin) {
      // In an actual implementation, the pin would be encrypted
      // (and the account number as well, with a shared secret
      // key between server and client, with perhaps even PKI
      // to doubly-protect the pin -or a hash, vs. the pin itself
      // might be sent).
      LogHelper.fine("AccountInfo: constructor with id and pin called ");
      account_id = creation_id;
      pin = creation_pin;
   }

   /* Constructor we do not want */
    public AccountInfo() {
      LogHelper.fine("AccountInfo: error, parameterless constructor called! ");
      System.err.println("AccountInfo: error, parameterless constructor called!");
      /* Set up some invalid values, so we can catch these later! */
      account_id = -1;
      pin = -1;
   }

   /**
    * <code> getId </code> returns the account for a given AccountInfo instance.
    *
    * @return the account matching id,for this AccountInfo instance.
    */
   public int getId() {
      return account_id;
   } // end method getId

   /**
    * <code> getPin </code> returns the pin for a given AccountInfo instance.
    *
    * @return the pin provided for this AccountInfo instance.
    */
   public int getPin() {
      return pin;
   } // end method getPin

} // end class Accountinfo

