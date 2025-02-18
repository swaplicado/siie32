/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Sergio Flores
 */
public interface SServerRemote extends Remote {

    public String getVersion() throws RemoteException;
    public SLoginResponse login(SLoginRequest request) throws RemoteException;
    public void logout(int sessionId) throws RemoteException;
}
