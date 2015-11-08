/**
 *  Manuel W. Mendez
 */
package rmi.bank;

import java.rmi.server.UnicastRemoteObject;

/**
 * The ATMFactoryImpl object returns a reference to a new
 * object that the new client can act upon via the ATM
 * interface.
 */
public class ATMFactoryImpl
   extends UnicastRemoteObject implements ATMFactory {

   public ATMFactoryImpl() throws java.rmi.RemoteException
   {
      super();
   }


   /*** ATMFactoryImpl Implementation Methods ***/

   /**
    * get a reference to an ATMImpl RMI object the client can use
    * via the ATM interface
    *
    */
   @Override
   public ATM getATM() throws java.rmi.RemoteException {
      LogHelper.finer("ATMFactoryImpl returning a requested new instance of ATMImpl!");
      return new ATMImpl();
   }

}
