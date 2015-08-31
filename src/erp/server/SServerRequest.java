/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.server;

import java.io.Serializable;
import sa.lib.SLibConsts;
import sa.lib.srv.SSrvRequest;

/**
 *
 * @author Sergio Flores
 */
public class SServerRequest extends SSrvRequest implements Serializable {

    private int mnRegistryType;             // XXX SIIE 3
    private Object moPrimaryKey;  // XXX SIIE 3

    /**
     * @param requestType Constants defined in sa.lib.srv.SSrvConsts.
     */
    public SServerRequest(int requestType) {
        this(requestType, null);
    }

    /**
     * @param requestType Constants defined in sa.lib.srv.SSrvConsts.
     * @param packet Request information packet.
     */
    public SServerRequest(int requestType, Object packet) {
        super(requestType, packet);
        mnRegistryType = SLibConsts.UNDEFINED;
        moPrimaryKey = null;
    }

    public void setRegistryType(int n) { mnRegistryType = n; }
    public void setPrimaryKey(Object key) { moPrimaryKey = key; }

    public int getRegistryType() { return mnRegistryType; }
    public Object getPrimaryKey() { return moPrimaryKey; }
}
