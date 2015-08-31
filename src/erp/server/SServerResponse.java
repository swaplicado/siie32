/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.server;

import java.io.Serializable;
import sa.lib.srv.SSrvResponse;

/**
 *
 * @author Sergio Flores
 */
public class SServerResponse extends SSrvResponse implements Serializable {

    /**
     * @param responseType Constants defined in sa.lib.srv.SSrvConsts.
     */
    public SServerResponse(int responseType) {
        this(responseType, null);
    }

    /**
     * @param responseType Constants defined in sa.lib.srv.SSrvConsts.
     * @param packet Response information packet.
     */
    public SServerResponse(int responseType, Object packet) {
        super(responseType, packet);
    }
}
