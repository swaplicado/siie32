/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.musr.data;

/**
 *
 * @author Adrián Avilés
 */
public class SDataUserAppRow extends erp.lib.table.STableRow {
    
    protected int appId;
    protected String appName;
    protected boolean hasAccess;
    protected int accessType;

    public SDataUserAppRow(int appId, String appName, boolean hasAccess, int accessType) {
        this.appId = appId;
        this.appName = appName;
        this.hasAccess = hasAccess;
        this.accessType = accessType;
        
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        mvValues.clear();
//        mvValues.add(appId);
        mvValues.add(appName);
        mvValues.add(hasAccess);
        mvValues.add(accessType);
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public boolean isHasAccess() {
        return hasAccess;
    }

    public void setHasAccess(boolean hasAccess) {
        this.hasAccess = hasAccess;
    }

    public int getAccessType() {
        return accessType;
    }

    public void setAccessType(int accessType) {
        this.accessType = accessType;
    }
}
