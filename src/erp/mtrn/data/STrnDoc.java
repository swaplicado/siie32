/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import java.io.Serializable;

/**
 *
 * @author Juan Barajas
 */
public final class STrnDoc implements Serializable {

    private int[] manPkDoc;
    private String msNumberSeries;
    private String msNumber;

    public STrnDoc() {
        manPkDoc = null;
        msNumberSeries = "";
        msNumber = "";
    }

    public void setPkDoc(int[] o) { manPkDoc = o; }
    public void setNumberSeries(String s) { msNumberSeries = s; }
    public void setNumber(String s) { msNumber = s; }

    public int[] getPkDoc() { return manPkDoc; }
    public String getNumberSeries() { return msNumberSeries; }
    public String getNumber() { return msNumber; }
}
