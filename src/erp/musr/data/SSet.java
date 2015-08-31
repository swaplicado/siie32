/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.musr.data;

import java.util.Vector;

/**
 *
 * @author Alfonso Flores
 */
public class SSet {
    private java.util.Vector<Integer> mvSet;

    public SSet() {
        mvSet = new Vector<Integer>();
    }

    public void add(int n) {
        int i = 0;
        boolean bExists = false;

        for (i = 0; i < mvSet.size(); i++) {
            if (n == mvSet.get(i)) {
                bExists = true;
                break;
            }
        }

        if (!bExists) {
            mvSet.add(n);
        }
    }

    public boolean containsInt(int n) {
        int i = 0;
        Boolean bIsContained = false;

        for (i = 0; i < mvSet.size(); i++) {
            if (n == mvSet.get(i)) {
                bIsContained = true;
                break;
            }
        }

        return bIsContained;
    }
}
