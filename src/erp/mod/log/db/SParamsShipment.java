/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.log.db;

import java.util.Date;

/**
 *
 * @author Néstor Ávalos
 */
public class SParamsShipment {

    protected int mnFkShipmentCobId;
    protected int mnFkShipmentTypeId;
    protected int mnFkDeliveryTypeId;
    protected int mnFkDocShipmentTypeId;
    protected int mnFkDpsYearId;
    protected int mnFkDpsDocId;
    protected int mnFkDiogYearId;
    protected int mnFkDiogDocId;
    protected int mnFkSpotTypeId;
    protected int mnFkSpotId;
    protected Date mtDate;
    protected boolean mbSave;

    public SParamsShipment() {
        mnFkShipmentCobId = 0;
        mnFkShipmentTypeId = 0;
        mnFkDeliveryTypeId = 0;
        mnFkDocShipmentTypeId = 0;
        mnFkDpsYearId = 0;
        mnFkDpsDocId = 0;
        mnFkDiogYearId = 0;
        mnFkDiogDocId = 0;
        mnFkSpotTypeId = 0;
        mnFkSpotId = 0;
        mtDate = null;
        mbSave = true;
    }

    public void setFkShipmentCobId(int n) { mnFkShipmentCobId = n; }
    public void setFkShipmentTypeId(int n) { mnFkShipmentTypeId = n; }
    public void setFkDeliveryTypeId(int n) { mnFkDeliveryTypeId = n; }
    public void setFkDocShipmentTypeId(int n) { mnFkDocShipmentTypeId = n; }
    public void setFkDpsYearId(int n) { mnFkDpsYearId = n; }
    public void setFkDpsDocId(int n) { mnFkDpsDocId = n; }
    public void setFkDiogYearId(int n) { mnFkDiogYearId = n; }
    public void setFkDiogDocId(int n) { mnFkDiogDocId = n; }
    public void setFkSpotTypeId(int n) { mnFkSpotTypeId = n; }
    public void setFkSpotId(int n) { mnFkSpotId = n; }
    public void setDate(Date t) { mtDate = t; }
    public void setSave(boolean b) { mbSave = b; }

    public int getFkShipmentCobId() { return mnFkShipmentCobId; }
    public int getFkShipmentTypeId() { return mnFkShipmentTypeId; }
    public int getFkDeliveryTypeId() { return mnFkDeliveryTypeId; }
    public int getFkDocShipmentTypeId() { return mnFkDocShipmentTypeId; }
    public int getFkDpsYearId() { return mnFkDpsYearId; }
    public int getFkDpsDocId() { return mnFkDpsDocId; }
    public int getFkDiogYearId() { return mnFkDiogYearId; }
    public int getFkDiogDocId() { return mnFkDiogDocId; }
    public int getFkSpotTypeId() { return mnFkSpotTypeId; }
    public int getFkSpotId() { return mnFkSpotId; }
    public Date getDate() { return mtDate; }
    public boolean getSave() { return mbSave; }
}
