/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.gui.session;

import erp.mod.SModSysConsts;
import java.io.Serializable;
import sa.lib.SLibConsts;

/**
 *
 * @author Sergio Flores
 */
public class SSessionItem implements Serializable {

    private int mnPkItemId;
    private int mnFkPropManufacturerId;
    private int mnFkPropBrandId;
    private int mnFkItemLineId;
    private int mnFkItemGenericId;
    private int mnFkItemGroupId;
    private int mnFkItemFamilyId;
    private int mnFkTypeTypeId;
    private int mnFkTypeClassId;
    private int mnFkTypeCategoryId;
    private int mnFkAltUnitTypeId;
    private double mdAltUnitBaseEquivalence;

    public SSessionItem(int idItem) {
        mnPkItemId = idItem;
    }

    public int getPkItemId() { return mnPkItemId; }
    public int getFkPropManufacturerId() { return mnFkPropManufacturerId; }
    public int getFkPropBrandId() { return mnFkPropBrandId; }
    public int getFkItemLineId() { return mnFkItemLineId; }
    public int getFkItemGenericId() { return mnFkItemGenericId; }
    public int getFkItemGroupId() { return mnFkItemGroupId; }
    public int getFkItemFamilyId() { return mnFkItemFamilyId; }
    public int getFkTypeTypeId() { return mnFkTypeTypeId; }
    public int getFkTypeClassId() { return mnFkTypeClassId; }
    public int getFkTypeCategoryId() { return mnFkTypeCategoryId; }
    public int getFkAltUnitTypeId() { return mnFkAltUnitTypeId; }
    public double getAltUnitBaseEquivalence() { return mdAltUnitBaseEquivalence; }

    public void setPkItemId(int n) { mnPkItemId = n; }
    public void setFkPropManufacturerId(int n) { mnFkPropManufacturerId = n; }
    public void setFkPropBrandId(int n) { mnFkPropBrandId = n; }
    public void setFkItemLineId(int n) { mnFkItemLineId = n; }
    public void setFkItemGenericId(int n) { mnFkItemGenericId = n; }
    public void setFkItemGroupId(int n) { mnFkItemGroupId = n; }
    public void setFkItemFamilyId(int n) { mnFkItemFamilyId = n; }
    public void setFkTypeTypeId(int n) { mnFkTypeTypeId = n; }
    public void setFkTypeClassId(int n) { mnFkTypeClassId = n; }
    public void setFkTypeCategoryId(int n) { mnFkTypeCategoryId = n; }
    public void setFkAltUnitTypeId(int n) { mnFkAltUnitTypeId = n; }
    public void setAltUnitBaseEquivalence(double d) { mdAltUnitBaseEquivalence = d; }

    public int getReferenceId(int idLink) {
        int referenceId = SLibConsts.UNDEFINED;

        switch (idLink) {
            case SModSysConsts.ITMS_LINK_ITEM:
                referenceId = mnPkItemId;
                break;
            case SModSysConsts.ITMS_LINK_MFR:
                referenceId = mnFkPropManufacturerId;
                break;
            case SModSysConsts.ITMS_LINK_BRD:
                referenceId = mnFkPropBrandId;
                break;
            case SModSysConsts.ITMS_LINK_LINE:
                referenceId = mnFkItemLineId;
                break;
            case SModSysConsts.ITMS_LINK_IGEN:
                referenceId = mnFkItemGenericId;
                break;
            case SModSysConsts.ITMS_LINK_IGRP:
                referenceId = mnFkItemGroupId;
                break;
            case SModSysConsts.ITMS_LINK_IFAM:
                referenceId = mnFkItemFamilyId;
                break;
            case SModSysConsts.ITMS_LINK_TP_ITEM:
                referenceId = mnFkTypeTypeId;
                break;
            case SModSysConsts.ITMS_LINK_CL_ITEM:
                referenceId = mnFkTypeClassId;
                break;
            case SModSysConsts.ITMS_LINK_CT_ITEM:
                referenceId = mnFkTypeCategoryId;
                break;
            default:
        }

        return referenceId;
    }
}
