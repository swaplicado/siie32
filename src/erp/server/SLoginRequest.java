/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.server;

import java.io.Serializable;
import sa.lib.srv.SSrvLoginRequest;

/**
 *
 * @author Sergio Flores
 */
public class SLoginRequest extends SSrvLoginRequest implements Serializable {

    public SLoginRequest(String name, String password, int companyId) {
        super(name, password, companyId);
    }
}
