/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import sa.lib.srv.SSrvRequest;
import sa.lib.srv.SSrvResponse;
import sa.lib.srv.SSrvSessionServerSide;

/**
 *
 * @author Sergio Flores
 */
public interface SSessionServerRemote extends Remote, SSrvSessionServerSide {

    @Override
    public SSrvResponse request(SSrvRequest request) throws RemoteException;

    public SServerResponse request(SServerRequest request) throws RemoteException;
}
