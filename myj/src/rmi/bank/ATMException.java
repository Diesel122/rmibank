package rmi.bank;

/**
 * This exception defines the custom exception thrown in the ATM simulation
 */
public class ATMException extends Exception {
   public ATMException(String msg) {
      super(msg);
   }
}
