/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.gui.session;

import java.io.Serializable;

/**
 *
 * @author Sergio Flores
 */
public class SSessionUnit implements Serializable {

    private int mnPkUnitId;
    private int mnFkUnitTypeId;
    private double mdUnitBaseEquivalence;

    public SSessionUnit(int idUnit) {
        mnPkUnitId = idUnit;
    }

    public int getPkUnitId() { return mnPkUnitId; }
    public int getFkUnitTypeId() { return mnFkUnitTypeId; }
    public double getUnitBaseEquivalence() { return mdUnitBaseEquivalence; }

    public void setPkUnitId(int n) { mnPkUnitId = n; }
    public void setFkUnitTypeId(int n) { mnFkUnitTypeId = n; }
    public void setUnitBaseEquivalence(double d) { mdUnitBaseEquivalence = d; }
}
