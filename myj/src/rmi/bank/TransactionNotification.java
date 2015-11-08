/**
 *  Manuel W. Mendez
 */
package rmi.bank;

import java.io.Serializable;

/**
 *         The class <code> TransactionNotification </code>
 *         encapsulates a notification regarding an account that is sent to
 *         all registered listeners (in the real world, it would be serialized
 *         with custom methods implementing encryption...
 */
public class TransactionNotification implements Serializable {

   /*** State variables for class AccountInfo */
   private int account1_id;      // The first and generally the only account id
   private int account2_id;      // The second account id, if any
   private Operation operation;  // The operation performed
   private Float amount;

   // Formatting Strings
   private static final String HEADER = "<Transaction Notification Message>";

   //// Constructor ////
   /* Constructor for TransactionNotification for 1 and 2 account operations */
   public TransactionNotification(AccountInfo info1, AccountInfo info2,
      Operation operationPerformed, Float amountTransacted)
   {
      assert (info1 != null) : "Missing AccountInfo object";

      if (operation == Operation.TRANSFER) {
         assert (info2 == null)
            : "Missing second AccountInfo object for a TRANSFER";
      }

      // Now save the data
      account1_id = info1.getId();
      operation = operationPerformed;
      amount = amountTransacted;

      // Save account2_id if provided, else mark explicitly as not to be used.
      if (info2 != null)
         account2_id = info2.getId();
      else
         account2_id = -1;

      LogHelper.fine ("TransactionNotification constructor created with operation "
         + operationPerformed);
   }


   /* Constructor we do not want */
    public TransactionNotification() {
      LogHelper.warn("TransactionNotification: error, parameterless constructor called! ");
      System.err.println("TransactionNotification: error, parameterless constructor called!");
      /* Set up some invalid values, so we can catch these later! */
      account1_id = -1;
      account2_id = -1;
      operation = Operation.UNINITALIZED;
   }

   /**
    * Return the operation performed as an enum
    *
    * @return
    *    The operation encoded in this <code> TransactionNotification </code> instance
    */
   public Operation getOperation() {
      return operation;
   }


   /**
    * Decode the notification and make it printable
    *
    * @return
    *    The encoded notification in a formatted string.
    */
   @Override
   public String toString() {

      String msg;
      switch (operation) {
         case TRANSFER:
            msg = String.format("%s\n%s $%.2f from account %d to account %d",
               HEADER, operation, amount, account1_id, account2_id);
            break;
         case DEPOSIT:
            msg = String.format("%s\n%s $%.2f, into account %d",
               HEADER, operation, amount, account1_id);
            break;
         case WITHDRAW:
            msg = String.format("%s\n%s $%.2f, from account %d",
               HEADER, operation, amount, account1_id);
            break;
         case BALANCE:
            msg = String.format("%s\n%s for account %d",
               HEADER, operation, account1_id);
            break;
         default:
            msg = String.format("%s enum received! (Internal Error!)",
               HEADER, operation);
         }
      LogHelper.finer("Transaction Notification toString called, returned: "
         + msg);
      return msg;
   }


} // end class TransactionNotification

