/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.link.db;

/**
 *
 * @author Edwin Carmona
 */
public class SDataRow {
    
    private int idEmployee;
    private int delayMins;
    private int absences;
    private boolean hasNoChecks;
    private boolean lostBonus;

    public int getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(int idEmployee) {
        this.idEmployee = idEmployee;
    }

    public int getDelayMins() {
        return delayMins;
    }

    public void setDelayMins(int delayMins) {
        this.delayMins = delayMins;
    }

    public int getAbsences() {
        return absences;
    }

    public void setAbsences(int absences) {
        this.absences = absences;
    }

    public boolean isHasNoChecks() {
        return hasNoChecks;
    }

    public void setHasNoChecks(boolean hasNoChecks) {
        this.hasNoChecks = hasNoChecks;
    }

    public boolean isLostBonus() {
        return lostBonus;
    }

    public void setLostBonus(boolean lostBonus) {
        this.lostBonus = lostBonus;
    }
    
}
