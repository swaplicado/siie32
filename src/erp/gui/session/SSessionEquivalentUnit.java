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
public class SSessionEquivalentUnit implements Serializable {

    private int mnPkUnitId;
    private int mnPkEquivalentUnitId;
    private int mnFkUnitTypeId;
    private int mnFkEquivalentUnitTypeId;
    private double mdUnitEquivalence;

    public SSessionEquivalentUnit(int idUnit, int idEquivalentUnit) {
        mnPkUnitId = idUnit;
        mnPkEquivalentUnitId = idEquivalentUnit;
    }

    public int getPkUnitId() { return mnPkUnitId; }
    public int getPkEquivalentUnitId() { return mnPkEquivalentUnitId; }
    public int getFkUnitTypeId() { return mnFkUnitTypeId; }
    public int getFkEquivalentUnitTypeId() { return mnFkEquivalentUnitTypeId; }
    public double getUnitEquivalence() { return mdUnitEquivalence; }

    public void setPkUnitId(int n) { mnPkUnitId = n; }
    public void setPkEquivalentUnitId(int n) { mnPkEquivalentUnitId = n; }
    public void setFkUnitTypeId(int n) { mnFkUnitTypeId = n; }
    public void setFkEquivalentUnitTypeId(int n) { mnFkEquivalentUnitTypeId = n; }
    public void setUnitEquivalence(double d) { mdUnitEquivalence = d; }
}
