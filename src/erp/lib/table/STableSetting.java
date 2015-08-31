/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.lib.table;

/**
 *
 * @author Sergio Flores
 */
public class STableSetting {

    private int mnType;
    private int mnStatus;
    private java.lang.Object moSetting;

    /**
     * @param type Constants defined in erp.lib.SLibConstants. Can be a filter or an option for views.
     * @param status Constants defined in erp.lib.SLibConstants. Can be ON or OFF.
     */
    public STableSetting(int type, int status) {
        mnType = type;
        mnStatus = status;
        moSetting = null;
    }

    /**
     * @param type Constants defined in erp.lib.SLibConstants. Can be a filter or an option for views.
     * @param setting Table setting.
     */
    public STableSetting(int type, java.lang.Object setting) {
        mnType = type;
        mnStatus = STableConstants.UNDEFINED;
        moSetting = setting;
    }

    public void setType(int n) { mnType = n; }
    public void setStatus(int n) { mnStatus = n; }
    public void setSetting(java.lang.Object o) { moSetting = o; }

    public int getType() { return mnType; }
    public int getStatus() { return mnStatus; }
    public java.lang.Object getSetting() { return moSetting; }
}
