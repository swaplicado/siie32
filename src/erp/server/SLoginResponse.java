/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.server;

import java.io.Serializable;
import sa.lib.srv.SSrvLoginResponse;

/**
 *
 * @author Sergio Flores
 */
public class SLoginResponse extends SSrvLoginResponse implements Serializable {

    private erp.server.SSessionXXX moSessionXXX;

    public SLoginResponse(int responseType, SSessionXXX sessionXXX) {
        super(responseType);
        moSessionXXX = sessionXXX;
    }

    public SSessionXXX getSession() {
        return moSessionXXX;
    }
}
