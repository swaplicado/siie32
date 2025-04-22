  /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.table.STabFilterDatePeriodRange;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableSetting;
import erp.mod.trn.db.STrnConsts;
import erp.table.SFilterConstants;
import erp.table.STabFilterBizPartner;
import erp.table.STabFilterCompanyBranch;
import erp.table.STabFilterCurrency;
import erp.table.STabFilterFunctionalArea;
import erp.table.STabFilterRelatedParts;
import erp.table.STabFilterUnitType;

/**
 *
 * @author Alfonso Flores, Juan Barajas, Edwin Carmona, Sergio Flores, Claudio Peña
 */
public class SViewQueryTotal extends erp.lib.table.STableTab {

    private int mnUnitTotalType;
    private boolean mbIsLocalCurrency = false;
    
    private erp.lib.table.STableColumn[] maoTableColumns;
    private erp.lib.table.STabFilterDatePeriodRange moTabFilterDatePeriodRange;
    private erp.table.STabFilterCompanyBranch moTabCompanyBranch;
    private erp.table.STabFilterUnitType moTabFilterUnitType;
    private erp.table.STabFilterBizPartner moTabFilterBizPartner;
    private erp.table.STabFilterCurrency moTabFilterCurrency;
    private erp.table.STabFilterRelatedParts moTabFilterRelatedParts;
    private erp.table.STabFilterFunctionalArea moTabFilterFunctionalArea;

    public SViewQueryTotal(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01) {
        super(client, tabTitle, SDataConstants.TRNX_DPS_QRY, auxType01);
        initComponents();
    }

    private void initComponents() {
        maoTableColumns = null;

        mnUnitTotalType = SDataConstantsSys.TRNX_TP_UNIT_TOT_QTY;
        moTabFilterDatePeriodRange = new STabFilterDatePeriodRange(miClient, this);
        moTabCompanyBranch = new STabFilterCompanyBranch(miClient, this);
        moTabFilterUnitType = new STabFilterUnitType(miClient, this);
        moTabFilterBizPartner = new STabFilterBizPartner(miClient, this, isPurchase() ? SDataConstantsSys.BPSS_CT_BP_SUP : SDataConstantsSys.BPSS_CT_BP_CUS);
        moTabFilterCurrency = new STabFilterCurrency(miClient, this);
        moTabFilterRelatedParts = new STabFilterRelatedParts(miClient, this);
        moTabFilterFunctionalArea = new STabFilterFunctionalArea(miClient, this);

        removeTaskBarUpperComponent(jbNew);
        removeTaskBarUpperComponent(jbEdit);
        removeTaskBarUpperComponent(jbDelete);
        
        addTaskBarUpperComponent(moTabFilterDatePeriodRange);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabCompanyBranch);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterUnitType);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterFunctionalArea);

        switch(mnTabTypeAux01) {
            case SDataConstantsSys.TRNX_SAL_TOT_BY_IGEN:
            case SDataConstantsSys.TRNX_PUR_TOT_BY_IGEN:
            case SDataConstantsSys.TRNX_SAL_TOT_BY_IGEN_BP:
            case SDataConstantsSys.TRNX_PUR_TOT_BY_IGEN_BP:
            case SDataConstantsSys.TRNX_PUR_TOT_BY_ITEM:
            case SDataConstantsSys.TRNX_SAL_TOT_BY_ITEM:
            case SDataConstantsSys.TRNX_PUR_TOT_BY_ITEM_BP:
            case SDataConstantsSys.TRNX_SAL_TOT_BY_ITEM_BP:
            case SDataConstantsSys.TRNX_PUR_TOT_BY_BP:
            case SDataConstantsSys.TRNX_SAL_TOT_BY_BP:
            case SDataConstantsSys.TRNX_PUR_TOT_BY_BP_ITEM:
            case SDataConstantsSys.TRNX_SAL_TOT_BY_BP_ITEM:
            case SDataConstantsSys.TRNX_SAL_TOT_BY_IGEN_IREF:
            case SDataConstantsSys.TRNX_SAL_TOT_BY_IREF:
            case SDataConstantsSys.TRNX_PUR_TOT_BY_IGEN_IREF:
            case SDataConstantsSys.TRNX_PUR_TOT_BY_IREF:
            case SDataConstantsSys.TRNX_SAL_TOT_BY_ITEM_AGT:            
            case SDataConstantsSys.TRNX_SAL_TOT_BY_ITEM_BP_AGT:
            case SDataConstantsSys.TRNX_SAL_TOT_BY_BP_AGT:
            case SDataConstantsSys.TRNX_SAL_TOT_BY_BP_ITEM_AGT:
                addTaskBarUpperSeparator();
                addTaskBarUpperComponent(moTabFilterBizPartner);
                break;
            default:
        }
        
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterCurrency);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterRelatedParts);

        setIsSummaryApplying(true);

        populateTable();
    }

        private void renderTableColumns() {
        int i = 0;

        moTablePane.reset();

        if (mnTabTypeAux01 == SDataConstantsSys.TRNX_PUR_TOT_BY_BP || mnTabTypeAux01 == SDataConstantsSys.TRNX_PUR_TOT_BY_ITEM ||
                mnTabTypeAux01 == SDataConstantsSys.TRNX_SAL_TOT_BY_BP || mnTabTypeAux01 == SDataConstantsSys.TRNX_SAL_TOT_BY_ITEM) {
            maoTableColumns = new STableColumn[12];
        }
        else if ( mnTabTypeAux01 == SDataConstantsSys.TRNX_SAL_TOT_BY_ITEM_AGT || mnTabTypeAux01 == SDataConstantsSys.TRNX_SAL_TOT_BY_BP_AGT) {
            maoTableColumns = new STableColumn[15];
        }
        else if (mnTabTypeAux01 == SDataConstantsSys.TRNX_PUR_TOT_BY_TP_BP || mnTabTypeAux01 == SDataConstantsSys.TRNX_SAL_TOT_BY_TP_BP
                || mnTabTypeAux01 == SDataConstantsSys.TRNX_PUR_TOT_BY_IGEN || mnTabTypeAux01 == SDataConstantsSys.TRNX_SAL_TOT_BY_IGEN
                || mnTabTypeAux01 == SDataConstantsSys.TRNX_SAL_TOT_BY_IGEN_IREF || mnTabTypeAux01 == SDataConstantsSys.TRNX_SAL_TOT_BY_IREF 
                || mnTabTypeAux01 == SDataConstantsSys.TRNX_PUR_TOT_BY_IGEN_IREF || mnTabTypeAux01 == SDataConstantsSys.TRNX_PUR_TOT_BY_IREF) {
            maoTableColumns = new STableColumn[11];
        }
        else  if (mnTabTypeAux01 == SDataConstantsSys.TRNX_PUR_TOT_BY_TP_BP_BP || mnTabTypeAux01 == SDataConstantsSys.TRNX_SAL_TOT_BY_TP_BP_BP || mnTabTypeAux01 == SDataConstantsSys.TRNX_SAL_TOT_BY_IGEN_BP || mnTabTypeAux01 == SDataConstantsSys.TRNX_PUR_TOT_BY_IGEN_BP) {
            maoTableColumns = new STableColumn[13];
        }
        else  if (mnTabTypeAux01 == SDataConstantsSys.TRNX_SAL_TOT_BY_ITEM_BP_AGT || mnTabTypeAux01 == SDataConstantsSys.TRNX_SAL_TOT_BY_BP_ITEM_AGT ) {
            maoTableColumns = new STableColumn[17];
        }
        else {
            maoTableColumns = new STableColumn[14];
        }

        i = 0;
        switch (mnTabTypeAux01) {
            case SDataConstantsSys.TRNX_PUR_TOT_BY_BP:
            case SDataConstantsSys.TRNX_SAL_TOT_BY_BP:
                if (isPurchase()) {
                    if (miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_key", "Clave", 100);
                        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp", "Asociado negocios", 200);
                    }
                    else {
                        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp", "Asociado negocios", 200);
                        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_key", "Clave", 100);
                    }
                }
                else {
                    if (miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_key", "Clave", 100);
                        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp", "Asociado negocios", 200);
                    }
                    else {
                        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp", "Asociado negocios", 200);
                        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_key", "Clave", 100);
                    }
                }
                break;
            case SDataConstantsSys.TRNX_SAL_TOT_BY_BP_AGT:
                    maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp", "Asociado negocios", 200);
                    maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_key", "Clave", 100);
                    maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "country", "País", 100);
                    maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "locality", "Localidad", 100);
                    maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "sales_agent", "Agente de ventas", 200);
                break;
            case SDataConstantsSys.TRNX_PUR_TOT_BY_ITEM:
            case SDataConstantsSys.TRNX_SAL_TOT_BY_ITEM:
                if (miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                    maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "item_key", "Clave ítem", STableConstants.WIDTH_ITEM_KEY);
                    maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "item", "Ítem", 200);
                }
                break;
             case SDataConstantsSys.TRNX_SAL_TOT_BY_ITEM_AGT:
                if (miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                    maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "item_key", "Clave ítem", STableConstants.WIDTH_ITEM_KEY);
                    maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "item", "Ítem", 200);
                    maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "country", "País", 100);
                    maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "locality", "Localidad", 100);
                    maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "sales_agent", "Agente de ventas", 200);
                }
                break;
            case SDataConstantsSys.TRNX_PUR_TOT_BY_IGEN:
            case SDataConstantsSys.TRNX_SAL_TOT_BY_IGEN:
            case SDataConstantsSys.TRNX_PUR_TOT_BY_IGEN_IREF:
            case SDataConstantsSys.TRNX_SAL_TOT_BY_IGEN_IREF:
                    maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "igen", "Ítem genérico", 200);
                break;
            case SDataConstantsSys.TRNX_PUR_TOT_BY_IREF:
            case SDataConstantsSys.TRNX_SAL_TOT_BY_IREF:
                    maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "item", "Ítem referencia", 200);
                    break;
            case SDataConstantsSys.TRNX_SAL_TOT_BY_IGEN_BP:
            case SDataConstantsSys.TRNX_PUR_TOT_BY_IGEN_BP:
                    maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "igen", "Ítem genérico", 200);
                    if (isPurchase()) {
                        if (miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                            maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_key", "Clave", 100);
                            maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp", "Asociado negocios", 200);
                        }
                        else {
                            maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp", "Asociado negocios", 200);
                            maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_key", "Clave", 100);
                        }
                    }
                    else {
                        if (miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                            maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_key", "Clave", 100);
                            maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp", "Asociado negocios", 200);
                        }
                        else {
                            maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp", "Asociado negocios", 200);
                            maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_key", "Clave", 100);
                        }
                    }
                break;
            case SDataConstantsSys.TRNX_PUR_TOT_BY_BP_ITEM:
            case SDataConstantsSys.TRNX_SAL_TOT_BY_BP_ITEM:
                if (isPurchase()) {
                    if (miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_key", "Clave", 100);
                        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp", "Asociado negocios", 200);
                    }
                    else {
                        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp", "Asociado negocios", 200);
                        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_key", "Clave", 100);
                    }
                }
                else {
                    if (miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_key", "Clave", 100);
                        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp", "Asociado negocios", 200);
                    }
                    else {
                        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp", "Asociado negocios", 200);
                        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_key", "Clave", 100);
                    }
                }
                if (miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                    maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "item_key", "Clave ítem", STableConstants.WIDTH_ITEM_KEY);
                    maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "item", "Ítem", 200);
                }
                else {
                    maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "item", "Ítem", 200);
                    maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "item_key", "Clave ítem", STableConstants.WIDTH_ITEM_KEY);
                }
                break;
            case SDataConstantsSys.TRNX_SAL_TOT_BY_BP_ITEM_AGT:
                if (isPurchase()) {
                    if (miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_key", "Clave", 100);
                        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp", "Asociado negocios", 200);
                    }
                    else {
                        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp", "Asociado negocios", 200);
                        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_key", "Clave", 100);
                    }
                }
                else {
                    if (miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_key", "Clave", 100);
                        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp", "Asociado negocios", 200);
                    }
                    else {
                        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp", "Asociado negocios", 200);
                        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_key", "Clave", 100);
                        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "country", "País", 100);
                        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "locality", "Localidad", 100);
                        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "sales_agent", "Agente de ventas", 200);
                    }
                }
                if (miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                    maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "item_key", "Clave ítem", STableConstants.WIDTH_ITEM_KEY);
                    maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "item", "Ítem", 200);
                }
                else {
                    maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "item", "Ítem", 200);
                    maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "item_key", "Clave ítem", STableConstants.WIDTH_ITEM_KEY);
                }
                break;
            case SDataConstantsSys.TRNX_PUR_TOT_BY_ITEM_BP:
            case SDataConstantsSys.TRNX_SAL_TOT_BY_ITEM_BP:
                if (miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                    maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "item_key", "Clave ítem", STableConstants.WIDTH_ITEM_KEY);
                    maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "item", "Ítem", 200);
                }
                else {
                    maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "item", "Ítem", 200);
                    maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "item_key", "Clave ítem", STableConstants.WIDTH_ITEM_KEY);
                }
                if (isPurchase()) {
                    if (miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_key", "Clave", 100);
                        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp", "Asociado negocios", 200);
                    }
                    else {
                        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp", "Asociado negocios", 200);
                        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_key", "Clave", 100);
                    }
                }
                else {
                    if (miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_key", "Clave", 100);
                        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp", "Asociado negocios", 200);
                    }
                    else {
                        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp", "Asociado negocios", 200);
                        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_key", "Clave", 100);
                    }
                }
                break;
            case SDataConstantsSys.TRNX_SAL_TOT_BY_ITEM_BP_AGT:
                if (miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                    maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "item_key", "Clave ítem", STableConstants.WIDTH_ITEM_KEY);
                    maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "item", "Ítem", 200);
                       if (!isPurchase()) {
                        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "country", "País", 100);
                        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "locality", "Localidad", 100);
                        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "sales_agent", "Agente de ventas", 200);
                       }
                }
                else {
                    maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "item", "Ítem", 200);
                    maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "item_key", "Clave ítem", STableConstants.WIDTH_ITEM_KEY);
                }
                if (isPurchase()) {
                    if (miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_key", "Clave", 100);
                        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp", "Asociado negocios", 200);
                    }
                    else {
                        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp", "Asociado negocios", 200);
                        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_key", "Clave", 100);
                    }
                }
                else {
                    if (miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_key", "Clave", 100);
                        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp", "Asociado negocios", 200);
                    }
                    else {
                        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp", "Asociado negocios", 200);
                        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_key", "Clave", 100);
                    }
                }
                break;
            case SDataConstantsSys.TRNX_PUR_TOT_BY_TP_BP:
            case SDataConstantsSys.TRNX_SAL_TOT_BY_TP_BP:
                maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tp_bp", "Tipo asociado negocios", 200);
                break;
            case SDataConstantsSys.TRNX_PUR_TOT_BY_TP_BP_BP:
            case SDataConstantsSys.TRNX_SAL_TOT_BY_TP_BP_BP:
                maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tp_bp", "Tipo asociado negocios", 200);
                if (isPurchase()) {
                    if (miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_key", "Clave", 100);
                        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp", "Asociado negocios", 200);
                    }
                    else {
                        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp", "Asociado negocios", 200);
                        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_key", "Clave", 100);
                    }
                }
                else {
                    if (miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_key", "Clave", 100);
                        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp", "Asociado negocios", 200);
                    }
                    else {
                        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp", "Asociado negocios", 200);
                        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_key", "Clave", 100);
                    }
                }
                break;
            default:
        }

        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_stot_r", "Total $", STableConstants.WIDTH_VALUE_2X);
        if (mbIsLocalCurrency) {
            maoTableColumns[i].setSumApplying(true);
        }
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_adj_r", "Devs. $", STableConstants.WIDTH_VALUE_2X);
        if (mbIsLocalCurrency) {
            maoTableColumns[i].setSumApplying(true);
        }
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_adj_d", "Descs. $", STableConstants.WIDTH_VALUE_2X);
        if (mbIsLocalCurrency) {
            maoTableColumns[i].setSumApplying(true);
        }
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_stot_net", "Total neto $", STableConstants.WIDTH_VALUE_2X);
        if (mbIsLocalCurrency) {
            maoTableColumns[i].setSumApplying(true);
        }
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cur_key", "Moneda", STableConstants.WIDTH_CURRENCY_KEY);

        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_qty", "Cant.", STableConstants.WIDTH_VALUE_2X);
        maoTableColumns[i].setSumApplying(true);
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_qty_r", "Cant. devs.", STableConstants.WIDTH_VALUE_2X);
        maoTableColumns[i].setSumApplying(true);
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_qty_net", "Cant. neta", STableConstants.WIDTH_VALUE_2X);
        maoTableColumns[i].setSumApplying(true);
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValue());
        maoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "symbol", "Unidad", STableConstants.WIDTH_UNIT_SYMBOL);
        maoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_avg_price", "Precio promedio $", STableConstants.WIDTH_VALUE_UNITARY);
        maoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValueUnitary());

        for (i = 0; i < maoTableColumns.length; i++) {
            moTablePane.addTableColumn(maoTableColumns[i]);
        }

        moTablePane.createTable(this);
   }

    private boolean isPurchase() {
        boolean purchase = false;

        if (mnTabTypeAux01 == SDataConstantsSys.TRNX_PUR_TOT_BY_BP || mnTabTypeAux01 == SDataConstantsSys.TRNX_PUR_TOT_BY_BP_ITEM ||
                mnTabTypeAux01 == SDataConstantsSys.TRNX_PUR_TOT_BY_ITEM || mnTabTypeAux01 == SDataConstantsSys.TRNX_PUR_TOT_BY_IGEN || mnTabTypeAux01 == SDataConstantsSys.TRNX_PUR_TOT_BY_IGEN_BP ||
                mnTabTypeAux01 == SDataConstantsSys.TRNX_PUR_TOT_BY_ITEM_BP || mnTabTypeAux01 == SDataConstantsSys.TRNX_PUR_TOT_BY_TP_BP || mnTabTypeAux01 == SDataConstantsSys.TRNX_PUR_TOT_BY_TP_BP_BP ||
                mnTabTypeAux01 == SDataConstantsSys.TRNX_PUR_TOT_BY_IGEN_IREF || mnTabTypeAux01 == SDataConstantsSys.TRNX_PUR_TOT_BY_IREF) {
            purchase = true;
        }

        return purchase;
    }

    private java.lang.String createColumnsUnits() {
        String columnsUnit = "";
        String columnStot = (mbIsLocalCurrency ? "e.stot_r" : "e.stot_cur_r");

        switch (mnUnitTotalType) {
            case SDataConstantsSys.TRNX_TP_UNIT_TOT_QTY:
                columnsUnit = "COALESCE(SUM(e.qty), 0) AS f_qty, " +
                        "0 AS f_qty_r, COALESCE(SUM(e.qty), 0) AS f_qty_net, " +
                        "COALESCE(COALESCE(SUM(" + columnStot + "), 0) / COALESCE(SUM(e.qty), 0), 0) AS f_avg_price ";
                break;
            case SDataConstantsSys.TRNX_TP_UNIT_TOT_LEN:
                columnsUnit = "COALESCE(SUM(e.len), 0) AS f_qty, " +
                        "0 AS f_qty_r, COALESCE(SUM(e.len), 0) AS f_qty_net, " +
                        "COALESCE(COALESCE(SUM(" + columnStot + "), 0) / COALESCE(SUM(e.len), 0), 0) AS f_avg_price ";
                break;
            case SDataConstantsSys.TRNX_TP_UNIT_TOT_SURF:
                columnsUnit = "COALESCE(SUM(e.surf), 0) AS f_qty, " +
                        "0 AS f_qty_r, COALESCE(SUM(e.surf), 0) AS f_qty_net, " +
                        "COALESCE(COALESCE(SUM(" + columnStot + "), 0) / COALESCE(SUM(e.surf), 0), 0) AS f_avg_price ";
                break;
            case SDataConstantsSys.TRNX_TP_UNIT_TOT_VOL:
                columnsUnit = "COALESCE(SUM(e.vol), 0) AS f_qty, " +
                        "0 AS f_qty_r, COALESCE(SUM(e.vol), 0) AS f_qty_net, " +
                        "COALESCE(COALESCE(SUM(" + columnStot + "), 0) / COALESCE(SUM(e.vol), 0), 0) AS f_avg_price ";
                break;
            case SDataConstantsSys.TRNX_TP_UNIT_TOT_MASS:
                columnsUnit = "COALESCE(SUM(e.mass), 0) AS f_qty, " +
                        "0 AS f_qty_r, COALESCE(SUM(e.mass), 0) AS f_qty_net, " +
                        "COALESCE(COALESCE(SUM(" + columnStot + "), 0) / COALESCE(SUM(e.mass), 0), 0) AS f_avg_price ";
                break;
            default:
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
        }

        return columnsUnit;
    }

    private java.lang.String createColumnsUnitsSum() {
        String columnsUnit = "";

        switch (mnUnitTotalType) {
            case SDataConstantsSys.TRNX_TP_UNIT_TOT_QTY:
                columnsUnit = "SUM(f_qty) AS f_qty, SUM(f_qty_r) AS f_qty_r, SUM(f_qty_net) AS f_qty_net, (SUM(f_stot_net)/SUM(f_qty_net)) AS f_avg_price";
                break;
            case SDataConstantsSys.TRNX_TP_UNIT_TOT_LEN:
                columnsUnit = "SUM(f_qty) AS f_qty, SUM(f_qty_r) AS f_qty_r, SUM(f_qty_net) AS f_qty_net, (SUM(f_stot_net)/SUM(f_qty_net)) AS f_avg_price";
                break;
            case SDataConstantsSys.TRNX_TP_UNIT_TOT_SURF:
                columnsUnit = "SUM(f_qty) AS f_qty, SUM(f_qty_r) AS f_qty_r, SUM(f_qty_net) AS f_qty_net, (SUM(f_stot_net)/SUM(f_qty_net)) AS f_avg_price";
                break;
            case SDataConstantsSys.TRNX_TP_UNIT_TOT_VOL:
                columnsUnit = "SUM(f_qty) AS f_qty, SUM(f_qty_r) AS f_qty_r, SUM(f_qty_net) AS f_qty_net, (SUM(f_stot_net)/SUM(f_qty_net)) AS f_avg_price";
                break;
            case SDataConstantsSys.TRNX_TP_UNIT_TOT_MASS:
                columnsUnit = "SUM(f_qty) AS f_qty, SUM(f_qty_r) AS f_qty_r, SUM(f_qty_net) AS f_qty_net, (SUM(f_stot_net)/SUM(f_qty_net)) AS f_avg_price";
                break;
            default:
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
        }

        return columnsUnit;
    }

    private java.lang.String createColumnsUnitsRet() {
        String columnsUnit = "";
        String columnStot = (mbIsLocalCurrency ? "e.stot_r" : "e.stot_cur_r");

        switch (mnUnitTotalType) {
            case SDataConstantsSys.TRNX_TP_UNIT_TOT_QTY:
                columnsUnit = ", 0 AS f_qty, " +
                        "COALESCE(SUM(e.qty), 0) AS f_qty_r, " +
                        "0 - COALESCE(SUM(e.qty), 0) AS f_qty_net, " +
                        "COALESCE((0 - COALESCE(SUM(" + columnStot + "), 0) / 0 - COALESCE(SUM(e.qty), 0)), 0) AS f_avg_price ";
                break;
            case SDataConstantsSys.TRNX_TP_UNIT_TOT_LEN:
                columnsUnit = ", 0 AS f_qty, " +
                        "COALESCE(SUM(e.len), 0) AS f_qty_r, " +
                        "0 - COALESCE(SUM(e.len), 0) AS f_qty_net, " +
                        "COALESCE((0 - COALESCE(SUM(" + columnStot + "), 0) / 0 - COALESCE(SUM(e.len), 0)), 0) AS f_avg_price ";
                break;
            case SDataConstantsSys.TRNX_TP_UNIT_TOT_SURF:
                columnsUnit = ", 0 AS f_qty, " +
                        "COALESCE(SUM(e.surf), 0) AS f_qty_r, " +
                        "0 - COALESCE(SUM(e.surf), 0) AS f_qty_net, " +
                        "COALESCE((0 - COALESCE(SUM(" + columnStot + "), 0) / 0 - COALESCE(SUM(e.surf), 0)), 0) AS f_avg_price ";
                break;
            case SDataConstantsSys.TRNX_TP_UNIT_TOT_VOL:
                columnsUnit = ", 0 AS f_qty, " +
                        "COALESCE(SUM(e.vol), 0) AS f_qty_r, " +
                        "0 - COALESCE(SUM(e.vol), 0) AS f_qty_net, " +
                        "COALESCE((0 - COALESCE(SUM(" + columnStot + "), 0) / 0 - COALESCE(SUM(e.vol), 0)), 0) AS f_avg_price ";
                break;
            case SDataConstantsSys.TRNX_TP_UNIT_TOT_MASS:
                columnsUnit = ", 0 AS f_qty, " +
                        "COALESCE(SUM(e.mass), 0) AS f_qty_r, " +
                        "0 - COALESCE(SUM(e.mass), 0) AS f_qty_net, " +
                        "COALESCE((0 - COALESCE(SUM(" + columnStot + "), 0) / 0 - COALESCE(SUM(e.mass), 0)), 0) AS f_avg_price ";
                break;
            default:
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
        }

        return columnsUnit;
    }

    private java.lang.String createColumnsUnitsDis() {
        String columnsUnit = "";

        switch (mnUnitTotalType) {
            case SDataConstantsSys.TRNX_TP_UNIT_TOT_QTY:
                columnsUnit = ", 0 AS f_qty, 0 AS f_qty_r, 0 AS f_qty_net, 0 AS f_avg_price ";
                break;
            case SDataConstantsSys.TRNX_TP_UNIT_TOT_LEN:
                columnsUnit = ", 0 AS f_qty, 0 AS f_qty_r, 0 AS f_qty_net, 0 AS f_avg_price ";
                break;
            case SDataConstantsSys.TRNX_TP_UNIT_TOT_SURF:
                columnsUnit = ", 0 AS f_qty, 0 AS f_qty_r, 0 AS f_qty_net, 0 AS f_avg_price ";
                break;
            case SDataConstantsSys.TRNX_TP_UNIT_TOT_VOL:
                columnsUnit = ", 0 AS f_qty, 0 AS f_qty_r, 0 AS f_qty_net, 0 AS f_avg_price ";
                break;
            case SDataConstantsSys.TRNX_TP_UNIT_TOT_MASS:
                columnsUnit = ", 0 AS f_qty, 0 AS f_qty_r, 0 AS f_qty_net, 0 AS f_avg_price ";
                break;
            default:
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
        }

        return columnsUnit;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void createSqlQuery() {
        java.util.Date[] range = null;
        String sqlDatePeriod = "";
        String sqlFunctAreas = "";
        String dateInit = "";
        String dateEnd = "";
        String sqlCompanyBranch = "";
        String sqlColumns = "";
        String sqlGroupOrder = "";
        String sqlColumnsUnit = "";
        String sqlBizPartner = "";
        String sqlCurrency = "";
        String columnStot = "";
        boolean withRelatedParts = false;
        STableSetting setting = null;
        
        for (int i = 0; i < mvTableSettings.size(); i++) {
           setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
           if (setting.getType() == SFilterConstants.SETTING_FILTER_CURRENCY) {
                mbIsLocalCurrency = ((Integer)setting.getSetting()) == STabFilterCurrency.TP_SYSTEM_CURRENCY;
            } 
        }

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                range = (java.util.Date[]) setting.getSetting();
                dateInit = miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(range[0]);
                dateEnd = miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(range[1]);
                sqlDatePeriod += "AND doc.dt BETWEEN '" + dateInit + "' AND '" + dateEnd + "' ";
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_FUNC_AREA) {
                if (!((String) setting.getSetting()).isEmpty()) {
                    sqlFunctAreas += "AND doc.fid_func IN (" + ((String) setting.getSetting()) + ") ";
                }
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_COB) {
                sqlCompanyBranch += ((Integer) setting.getSetting() == SLibConstants.UNDEFINED ? "" : "AND doc.fid_cob = " + (Integer) setting.getSetting() + " ");
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_UNIT_TP) {
                mnUnitTotalType = (Integer) setting.getSetting();
                sqlColumnsUnit = createColumnsUnits();
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_BP) {
                if ((Integer) setting.getSetting() != SLibConstants.UNDEFINED) {
                    sqlBizPartner += " AND bp.id_bp = " + (Integer) setting.getSetting() + " ";
                }
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_REL_PARTY) {
                withRelatedParts = ((Integer)setting.getSetting()) == STrnConsts.TRN_BPS_WITH_REL_PARTY;
            }
        }
        
        renderTableColumns();
        
        columnStot = (mbIsLocalCurrency ? "e.stot_r" : "e.stot_cur_r");
        sqlCurrency = (mbIsLocalCurrency ? "" : "cur_key, ");

        switch (mnTabTypeAux01) {
            case SDataConstantsSys.TRNX_PUR_TOT_BY_BP:
            case SDataConstantsSys.TRNX_SAL_TOT_BY_BP:
                sqlColumns = "bp, bp_key, ";
                sqlGroupOrder = "GROUP BY " + sqlCurrency + "bp, bp_key ORDER BY " + sqlCurrency +
                        (isPurchase() ? miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "bp_key, bp " :
                            "bp, bp_key " : miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "bp_key, bp " :
                                "bp, bp_key ");
                break;
            case SDataConstantsSys.TRNX_SAL_TOT_BY_BP_AGT:
                sqlColumns = "bp, bp_key, ";
                sqlGroupOrder = "GROUP BY " + sqlCurrency + "bp, bp_key ORDER BY " + sqlCurrency +
                        (isPurchase() ? miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "bp_key, bp " :
                            "bp, bp_key " : miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "bp_key, bp " :
                                "bp, bp_key ");
                break;
            case SDataConstantsSys.TRNX_PUR_TOT_BY_ITEM:
            case SDataConstantsSys.TRNX_SAL_TOT_BY_ITEM:
                sqlColumns = "item, item_key, ";
                sqlGroupOrder = "GROUP BY " + sqlCurrency + "item, item_key ORDER BY " + sqlCurrency +
                        (miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "item_key, item " : "item, item_key ");
                break;
            case SDataConstantsSys.TRNX_SAL_TOT_BY_ITEM_AGT:
                sqlColumns = "item, item_key, ";
                sqlGroupOrder = "GROUP BY " + sqlCurrency + "item, item_key ORDER BY " + sqlCurrency +
                        (miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "item_key, item " : "item, item_key ");
                break;
            case SDataConstantsSys.TRNX_PUR_TOT_BY_IGEN:
            case SDataConstantsSys.TRNX_SAL_TOT_BY_IGEN:
            case SDataConstantsSys.TRNX_PUR_TOT_BY_IGEN_IREF:
            case SDataConstantsSys.TRNX_SAL_TOT_BY_IGEN_IREF:
                sqlColumns = "igen, ";
                sqlGroupOrder = "GROUP BY " + sqlCurrency + "igen ORDER BY " + sqlCurrency +
                        (miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "igen " : "igen ");
                break;
            case SDataConstantsSys.TRNX_PUR_TOT_BY_IREF:
            case SDataConstantsSys.TRNX_SAL_TOT_BY_IREF:
                sqlColumns = "item, ";
                sqlGroupOrder = "GROUP BY " + sqlCurrency + "item ORDER BY " + sqlCurrency +
                        (miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "item " : "item ");
                break;
            case SDataConstantsSys.TRNX_PUR_TOT_BY_IGEN_BP:
            case SDataConstantsSys.TRNX_SAL_TOT_BY_IGEN_BP:
                sqlColumns = "igen, bp, bp_key, ";
                sqlGroupOrder = "GROUP BY " + sqlCurrency + "igen, bp, bp_key ORDER BY " + sqlCurrency +
                        (miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "igen, " : "igen, ") +
                        (isPurchase() ? miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "bp_key, bp " :
                            "bp, bp_key " : miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "bp_key, bp " :
                                "bp, bp_key ");
                break;
            case SDataConstantsSys.TRNX_PUR_TOT_BY_BP_ITEM:
            case SDataConstantsSys.TRNX_SAL_TOT_BY_BP_ITEM:
                sqlColumns = "bp, bp_key, item, item_key, ";
                sqlGroupOrder = "GROUP BY " + sqlCurrency + "bp, bp_key, item, item_key ORDER BY " + sqlCurrency +
                        (isPurchase() ? miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "bp_key, bp, " :
                            "bp, bp_key, " : miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "bp_key, bp, " :
                                "bp, bp_key, ") +
                                (miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "item_key, item " : "item, item_key ");
                break;
            case SDataConstantsSys.TRNX_SAL_TOT_BY_BP_ITEM_AGT:
                sqlColumns = "bp, bp_key, item, item_key, ";
                sqlGroupOrder = "GROUP BY " + sqlCurrency + "bp, bp_key, item, item_key ORDER BY " + sqlCurrency +
                        (isPurchase() ? miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "bp_key, bp, " :
                            "bp, bp_key, " : miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "bp_key, bp, " :
                                "bp, bp_key, ") +
                                (miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "item_key, item " : "item, item_key ");
                break;
            case SDataConstantsSys.TRNX_PUR_TOT_BY_ITEM_BP:
            case SDataConstantsSys.TRNX_SAL_TOT_BY_ITEM_BP:
                sqlColumns = "item, item_key, bp, bp_key, ";
                sqlGroupOrder = "GROUP BY " + sqlCurrency + "item, item_key, bp, bp_key ORDER BY " + sqlCurrency +
                        (miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "item_key, item, " : "item, item_key, ") +
                        (isPurchase() ? miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "bp_key, bp " :
                            "bp, bp_key " : miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "bp_key, bp " :
                                "bp, bp_key ");
                break;
            case SDataConstantsSys.TRNX_SAL_TOT_BY_ITEM_BP_AGT:
                sqlColumns = "item, item_key, bp, bp_key, ";
                sqlGroupOrder = "GROUP BY " + sqlCurrency + "item, item_key, bp, bp_key ORDER BY " + sqlCurrency +
                        (miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "item_key, item, " : "item, item_key, ") +
                        (isPurchase() ? miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "bp_key, bp " :
                            "bp, bp_key " : miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "bp_key, bp " :
                                "bp, bp_key ");
                break;
            case SDataConstantsSys.TRNX_PUR_TOT_BY_TP_BP:
            case SDataConstantsSys.TRNX_SAL_TOT_BY_TP_BP:
                sqlColumns = "tp_bp, ";
                sqlGroupOrder = "GROUP BY " + sqlCurrency + "tp_bp ORDER BY " + sqlCurrency + "tp_bp ";
                break;
            case SDataConstantsSys.TRNX_PUR_TOT_BY_TP_BP_BP:
            case SDataConstantsSys.TRNX_SAL_TOT_BY_TP_BP_BP:
                sqlColumns = "tp_bp, bp, bp_key, ";
                sqlGroupOrder = "GROUP BY " + sqlCurrency + "tp_bp, bp, bp_key ORDER BY " + sqlCurrency + "tp_bp, " +
                        (isPurchase() ? miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "bp_key, bp " :
                            "bp, bp_key " : miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "bp_key, bp " :
                                "bp, bp_key ");
                break;
            default:
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
        }

        msSql = "SELECT " + sqlColumns + " MAX(sales_agent) AS sales_agent, MAX(country) AS country, locality, SUM(f_stot_r) AS f_stot_r, SUM(f_adj_r) AS f_adj_r, SUM(f_adj_d) AS f_adj_d, SUM(f_stot_net) AS f_stot_net, symbol, cur_key, " +
                createColumnsUnitsSum() + " " +
                "FROM (" +
                "(SELECT " + sqlColumns + "COALESCE(SUM(" + columnStot + "), 0) AS f_stot_r, " +
                " (SELECT bp.bp " +
                " FROM trn_dps AS d " +
                " INNER JOIN erp.bpsu_bp AS bp on bp.id_bp = d.fid_sal_agt_n " +
                " WHERE d.id_year = doc.id_year AND doc.id_doc = doc.id_doc LIMIT 1 ) AS sales_agent, " +
                " (SELECT COALESCE(ct.cty, (SELECT cty FROM erp.LOCU_CTY WHERE id_cty = 1)) AS cty_final " +
                " FROM trn_dps AS d " +
                " INNER JOIN erp.BPSU_BPB AS bpb ON bpb.fid_bp = d.fid_bp_r " +
                " INNER JOIN erp.BPSU_BPB_ADD AS ad ON ad.id_bpb = bpb.id_bpb " +
                " LEFT JOIN erp.LOCU_CTY AS ct ON ct.id_cty = ad.fid_cty_n " +
                " WHERE d.id_year = doc.id_year AND d.id_doc = doc.id_doc LIMIT 1 ) AS country, " +
                " (SELECT ad.locality " +
                " FROM trn_dps AS d " +
                " INNER JOIN erp.BPSU_BPB AS bpb ON bpb.fid_bp = d.fid_bp_r " +
                " INNER JOIN erp.BPSU_BPB_ADD AS ad ON ad.id_bpb = bpb.id_bpb " +
                " LEFT JOIN erp.LOCU_CTY AS ct ON ct.id_cty = ad.fid_cty_n " +
                " WHERE d.id_year = doc.id_year AND d.id_doc = doc.id_doc LIMIT 1 ) AS locality, " +
                "0 AS f_adj_r, 0 AS f_adj_d, " +
                "COALESCE(SUM(" + columnStot + "), 0) AS f_stot_net, " +
                sqlColumnsUnit + ", (SELECT unit_base FROM erp.itmu_tp_unit WHERE id_tp_unit = " +
                (mnUnitTotalType == SDataConstantsSys.TRNX_TP_UNIT_TOT_QTY ? SDataConstantsSys.ITMU_TP_UNIT_QTY :
                    mnUnitTotalType == SDataConstantsSys.TRNX_TP_UNIT_TOT_LEN ? SDataConstantsSys.ITMU_TP_UNIT_LEN :
                        mnUnitTotalType == SDataConstantsSys.TRNX_TP_UNIT_TOT_MASS ? SDataConstantsSys.ITMU_TP_UNIT_MASS :
                            mnUnitTotalType == SDataConstantsSys.TRNX_TP_UNIT_TOT_SURF ? SDataConstantsSys.ITMU_TP_UNIT_SURF :
                                SDataConstantsSys.ITMU_TP_UNIT_VOL) +
                ") AS symbol, " +
                "(SELECT cur_key FROM erp.cfgu_cur WHERE id_cur = " + (mbIsLocalCurrency ? miClient.getSessionXXX().getParamsErp().getFkCurrencyId() : "doc.fid_cur") + ") AS cur_key " +
                "FROM trn_dps_ety AS e " +
                "INNER JOIN trn_dps AS doc ON " +
                "e.id_year = doc.id_year AND e.id_doc = doc.id_doc " +
                "INNER JOIN erp.bpsu_bp AS bp ON " +
                "doc.fid_bp_r = bp.id_bp " + (sqlBizPartner.length() > 0 ? sqlBizPartner : "") + " " +
                 "INNER JOIN erp.bpsu_bp_ct AS ct ON " +
                "bp.id_bp = ct.id_bp AND ct.id_ct_bp = " + (isPurchase() ? SDataConstantsSys.BPSS_CT_BP_SUP : SDataConstantsSys.BPSS_CT_BP_CUS) + " " +
                "INNER JOIN erp.bpsu_tp_bp AS tp ON " +
                "ct.fid_ct_bp = tp.id_ct_bp AND ct.fid_tp_bp = tp.id_tp_bp " +
                (mnTabTypeAux01 == SDataConstantsSys.TRNX_PUR_TOT_BY_IGEN_IREF || mnTabTypeAux01 == SDataConstantsSys.TRNX_SAL_TOT_BY_IGEN_IREF || 
                    mnTabTypeAux01 == SDataConstantsSys.TRNX_PUR_TOT_BY_IREF || mnTabTypeAux01 == SDataConstantsSys.TRNX_SAL_TOT_BY_IREF ? 
                        "" : "INNER JOIN erp.itmu_item AS i ON e.fid_item = i.id_item ") +
                (mnTabTypeAux01 == SDataConstantsSys.TRNX_PUR_TOT_BY_IGEN || mnTabTypeAux01 == SDataConstantsSys.TRNX_SAL_TOT_BY_IGEN ||
                    mnTabTypeAux01 == SDataConstantsSys.TRNX_PUR_TOT_BY_IGEN_BP || mnTabTypeAux01 == SDataConstantsSys.TRNX_SAL_TOT_BY_IGEN_BP ?
                        "INNER JOIN erp.itmu_igen AS ig ON i.fid_igen = ig.id_igen " : "" ) +
                (mnTabTypeAux01 == SDataConstantsSys.TRNX_PUR_TOT_BY_IGEN_IREF || mnTabTypeAux01 == SDataConstantsSys.TRNX_SAL_TOT_BY_IGEN_IREF || 
                    mnTabTypeAux01 == SDataConstantsSys.TRNX_PUR_TOT_BY_IREF || mnTabTypeAux01 == SDataConstantsSys.TRNX_SAL_TOT_BY_IREF ? 
                        "LEFT JOIN erp.itmu_item AS ir ON e.fid_item_ref_n = ir.id_item LEFT JOIN erp.itmu_igen AS irg ON ir.fid_igen = irg.id_igen " : "") +
                "INNER JOIN erp.bpsu_bpb AS cob ON " +
                "doc.fid_cob = cob.id_bpb " +
                "WHERE e.b_del = FALSE AND doc.b_del = FALSE " +
                "AND doc.fid_ct_dps = " + (isPurchase() ? SDataConstantsSys.TRNU_TP_DPS_PUR_INV[0] : SDataConstantsSys.TRNU_TP_DPS_SAL_INV[0]) + " " +
                "AND doc.fid_cl_dps = " + (isPurchase() ? SDataConstantsSys.TRNU_TP_DPS_PUR_INV[1] : SDataConstantsSys.TRNU_TP_DPS_SAL_INV[1]) + " " +
                "AND doc.fid_tp_dps = " + (isPurchase() ? SDataConstantsSys.TRNU_TP_DPS_PUR_INV[2] : SDataConstantsSys.TRNU_TP_DPS_SAL_INV[2]) + " " +
                "AND doc.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " AND doc.fid_st_dps_val = " + SDataConstantsSys.TRNS_ST_DPS_VAL_EFF + " " +
                (withRelatedParts ? "" : " AND bp.b_att_rel_pty = 0 ") +
                sqlCompanyBranch + sqlDatePeriod + sqlFunctAreas +
                sqlGroupOrder + ") " +
                "UNION " +
                "(SELECT " + sqlColumns + "0 AS f_stot_r, " +
                " (SELECT bp.bp " +
                " FROM trn_dps AS d " +
                " INNER JOIN erp.bpsu_bp AS bp on bp.id_bp = d.fid_sal_agt_n " +
                " WHERE d.id_year = doc.id_year AND doc.id_doc =doc.id_doc LIMIT 1 ) AS sales_agent, " +
                " (SELECT COALESCE(ct.cty, (SELECT cty FROM erp.LOCU_CTY WHERE id_cty = 1)) AS cty_final " +
                " FROM trn_dps AS d " +
                " INNER JOIN erp.BPSU_BPB AS bpb ON bpb.fid_bp = d.fid_bp_r " +
                " INNER JOIN erp.BPSU_BPB_ADD AS ad ON ad.id_bpb = bpb.id_bpb " +
                " LEFT JOIN erp.LOCU_CTY AS ct ON ct.id_cty = ad.fid_cty_n " +
                " WHERE d.id_year = doc.id_year AND d.id_doc = doc.id_doc LIMIT 1 ) AS country, " +
                " (SELECT ad.locality " +
                " FROM trn_dps AS d " +
                " INNER JOIN erp.BPSU_BPB AS bpb ON bpb.fid_bp = d.fid_bp_r " +
                " INNER JOIN erp.BPSU_BPB_ADD AS ad ON ad.id_bpb = bpb.id_bpb " +
                " LEFT JOIN erp.LOCU_CTY AS ct ON ct.id_cty = ad.fid_cty_n " +
                " WHERE d.id_year = doc.id_year AND d.id_doc = doc.id_doc LIMIT 1 ) AS locality, " +
                " COALESCE(SUM(" + columnStot + "), 0) AS f_adj_r, 0 AS f_adj_d, 0 - COALESCE(SUM(" + columnStot + "), 0) AS f_stot_net " +
                createColumnsUnitsRet() + ", (SELECT unit_base FROM erp.itmu_tp_unit WHERE id_tp_unit = " +
                (mnUnitTotalType == SDataConstantsSys.TRNX_TP_UNIT_TOT_QTY ? SDataConstantsSys.ITMU_TP_UNIT_QTY :
                    mnUnitTotalType == SDataConstantsSys.TRNX_TP_UNIT_TOT_LEN ? SDataConstantsSys.ITMU_TP_UNIT_LEN :
                        mnUnitTotalType == SDataConstantsSys.TRNX_TP_UNIT_TOT_MASS ? SDataConstantsSys.ITMU_TP_UNIT_MASS :
                            mnUnitTotalType == SDataConstantsSys.TRNX_TP_UNIT_TOT_SURF ? SDataConstantsSys.ITMU_TP_UNIT_SURF :
                                SDataConstantsSys.ITMU_TP_UNIT_VOL) +
                ") AS symbol, " +
                "(SELECT cur_key FROM erp.cfgu_cur WHERE id_cur = " + (mbIsLocalCurrency ? miClient.getSessionXXX().getParamsErp().getFkCurrencyId() : "doc.fid_cur") + ") AS cur_key " +
                "FROM trn_dps_ety AS e " +
                "INNER JOIN trn_dps AS doc ON " +
                "e.id_year = doc.id_year AND e.id_doc = doc.id_doc " +
                "INNER JOIN trn_dps_dps_adj AS j ON " +
                "e.id_year = j.id_adj_year AND e.id_doc = j.id_adj_doc AND e.id_ety = j.id_adj_ety " +
                "INNER JOIN trn_dps_ety AS o ON " +
                "j.id_dps_year = o.id_year AND j.id_dps_doc = o.id_doc AND j.id_dps_ety = o.id_ety " +
                "INNER JOIN erp.bpsu_bp AS bp ON " +
                "doc.fid_bp_r = bp.id_bp " + (sqlBizPartner.length() > 0 ? sqlBizPartner : "") + " " +
                "INNER JOIN erp.bpsu_bp_ct AS ct ON " +
                "bp.id_bp = ct.id_bp AND ct.id_ct_bp = " + (isPurchase() ? SDataConstantsSys.BPSS_CT_BP_SUP : SDataConstantsSys.BPSS_CT_BP_CUS) + " " +
                "INNER JOIN erp.bpsu_tp_bp AS tp ON " +
                "ct.fid_ct_bp = tp.id_ct_bp AND ct.fid_tp_bp = tp.id_tp_bp " +
                (mnTabTypeAux01 == SDataConstantsSys.TRNX_PUR_TOT_BY_IGEN_IREF || mnTabTypeAux01 == SDataConstantsSys.TRNX_SAL_TOT_BY_IGEN_IREF || 
                    mnTabTypeAux01 == SDataConstantsSys.TRNX_PUR_TOT_BY_IREF || mnTabTypeAux01 == SDataConstantsSys.TRNX_SAL_TOT_BY_IREF ? 
                    "" : "INNER JOIN erp.itmu_item AS i ON o.fid_item = i.id_item ") +
                (mnTabTypeAux01 == SDataConstantsSys.TRNX_PUR_TOT_BY_IGEN || mnTabTypeAux01 == SDataConstantsSys.TRNX_SAL_TOT_BY_IGEN ||
                mnTabTypeAux01 == SDataConstantsSys.TRNX_PUR_TOT_BY_IGEN_BP || mnTabTypeAux01 == SDataConstantsSys.TRNX_SAL_TOT_BY_IGEN_BP ?
                    "INNER JOIN erp.itmu_igen AS ig ON i.fid_igen = ig.id_igen " : "" ) +
                (mnTabTypeAux01 == SDataConstantsSys.TRNX_PUR_TOT_BY_IGEN_IREF || mnTabTypeAux01 == SDataConstantsSys.TRNX_SAL_TOT_BY_IGEN_IREF || 
                    mnTabTypeAux01 == SDataConstantsSys.TRNX_PUR_TOT_BY_IREF || mnTabTypeAux01 == SDataConstantsSys.TRNX_SAL_TOT_BY_IREF ? 
                        "LEFT JOIN erp.itmu_item AS ir ON e.fid_item_ref_n = ir.id_item LEFT JOIN erp.itmu_igen AS irg ON ir.fid_igen = irg.id_igen " : "") +
                "INNER JOIN erp.bpsu_bpb AS cob ON " +
                "doc.fid_cob = cob.id_bpb " +
                "WHERE e.b_del = FALSE AND doc.b_del = FALSE " +
                "AND doc.fid_ct_dps = " + (isPurchase() ? SDataConstantsSys.TRNU_TP_DPS_PUR_CN[0] : SDataConstantsSys.TRNU_TP_DPS_SAL_CN[0]) + " " +
                "AND doc.fid_cl_dps = " + (isPurchase() ? SDataConstantsSys.TRNU_TP_DPS_PUR_CN[1] : SDataConstantsSys.TRNU_TP_DPS_SAL_CN[1]) + " " +
                "AND doc.fid_tp_dps = " + (isPurchase() ? SDataConstantsSys.TRNU_TP_DPS_PUR_CN[2] : SDataConstantsSys.TRNU_TP_DPS_SAL_CN[2]) + " " +
                "AND doc.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " AND doc.fid_st_dps_val = " + SDataConstantsSys.TRNS_ST_DPS_VAL_EFF + " " +
                (withRelatedParts ? "" : " AND bp.b_att_rel_pty = 0 ") +
                "AND e.fid_tp_dps_adj = " + SDataConstantsSys.TRNS_TP_DPS_ADJ_RET + " " +
                sqlCompanyBranch + sqlDatePeriod + sqlFunctAreas +
                sqlGroupOrder + ") " +
                "UNION " +
                "(SELECT " + sqlColumns + "0 AS f_stot_r, " +
                " (SELECT bp.bp " +
                " FROM trn_dps AS d " +
                " INNER JOIN erp.bpsu_bp AS bp on bp.id_bp = d.fid_sal_agt_n " +
                " WHERE d.id_year = doc.id_year AND doc.id_doc = doc.id_doc LIMIT 1 ) AS sales_agent, " +
                " (SELECT COALESCE(ct.cty, (SELECT cty FROM erp.LOCU_CTY WHERE id_cty = 1)) AS cty_final " +
                " FROM trn_dps AS d " +
                " INNER JOIN erp.BPSU_BPB AS bpb ON bpb.fid_bp = d.fid_bp_r " +
                " INNER JOIN erp.BPSU_BPB_ADD AS ad ON ad.id_bpb = bpb.id_bpb " +
                " LEFT JOIN erp.LOCU_CTY AS ct ON ct.id_cty = ad.fid_cty_n " +
                " WHERE d.id_year = doc.id_year AND d.id_doc = doc.id_doc LIMIT 1 ) AS country, " +
                " (SELECT ad.locality " +
                " FROM trn_dps AS d " +
                " INNER JOIN erp.BPSU_BPB AS bpb ON bpb.fid_bp = d.fid_bp_r " +
                " INNER JOIN erp.BPSU_BPB_ADD AS ad ON ad.id_bpb = bpb.id_bpb " +
                " LEFT JOIN erp.LOCU_CTY AS ct ON ct.id_cty = ad.fid_cty_n " +
                " WHERE d.id_year = doc.id_year AND d.id_doc = doc.id_doc LIMIT 1 ) AS locality, " +
                 " 0 AS f_adj_r, COALESCE(SUM(" + columnStot + "), 0) AS f_adj_d, 0 - COALESCE(SUM(" + columnStot + "), 0) AS f_stot_net " +
                createColumnsUnitsDis() + ", (SELECT unit_base FROM erp.itmu_tp_unit WHERE id_tp_unit = " +
                (mnUnitTotalType == SDataConstantsSys.TRNX_TP_UNIT_TOT_QTY ? SDataConstantsSys.ITMU_TP_UNIT_QTY :
                    mnUnitTotalType == SDataConstantsSys.TRNX_TP_UNIT_TOT_LEN ? SDataConstantsSys.ITMU_TP_UNIT_LEN :
                        mnUnitTotalType == SDataConstantsSys.TRNX_TP_UNIT_TOT_MASS ? SDataConstantsSys.ITMU_TP_UNIT_MASS :
                            mnUnitTotalType == SDataConstantsSys.TRNX_TP_UNIT_TOT_SURF ? SDataConstantsSys.ITMU_TP_UNIT_SURF :
                                SDataConstantsSys.ITMU_TP_UNIT_VOL) +
                ") AS symbol, " +
                "(SELECT cur_key FROM erp.cfgu_cur WHERE id_cur = " + (mbIsLocalCurrency ? miClient.getSessionXXX().getParamsErp().getFkCurrencyId() : "doc.fid_cur") + ") AS cur_key " +
                "FROM trn_dps_ety AS e " +
                "INNER JOIN trn_dps AS doc ON " +
                "e.id_year = doc.id_year AND e.id_doc = doc.id_doc " +
                "INNER JOIN trn_dps_dps_adj AS j ON " +
                "e.id_year = j.id_adj_year AND e.id_doc = j.id_adj_doc AND e.id_ety = j.id_adj_ety " +
                "INNER JOIN trn_dps_ety AS o ON " +
                "j.id_dps_year = o.id_year AND j.id_dps_doc = o.id_doc AND j.id_dps_ety = o.id_ety " +
                "INNER JOIN erp.bpsu_bp AS bp ON " +
                "doc.fid_bp_r = bp.id_bp " + (sqlBizPartner.length() > 0 ? sqlBizPartner : "") + " " +
                "INNER JOIN erp.bpsu_bp_ct AS ct ON " +
                "bp.id_bp = ct.id_bp AND ct.id_ct_bp = " + (isPurchase() ? SDataConstantsSys.BPSS_CT_BP_SUP : SDataConstantsSys.BPSS_CT_BP_CUS) + " " +
                "INNER JOIN erp.bpsu_tp_bp AS tp ON " +
                "ct.fid_ct_bp = tp.id_ct_bp AND ct.fid_tp_bp = tp.id_tp_bp " +
                (mnTabTypeAux01 == SDataConstantsSys.TRNX_PUR_TOT_BY_IGEN_IREF || mnTabTypeAux01 == SDataConstantsSys.TRNX_SAL_TOT_BY_IGEN_IREF || 
                    mnTabTypeAux01 == SDataConstantsSys.TRNX_PUR_TOT_BY_IREF || mnTabTypeAux01 == SDataConstantsSys.TRNX_SAL_TOT_BY_IREF ? 
                    "" : "INNER JOIN erp.itmu_item AS i ON o.fid_item = i.id_item ") +
                (mnTabTypeAux01 == SDataConstantsSys.TRNX_PUR_TOT_BY_IGEN || mnTabTypeAux01 == SDataConstantsSys.TRNX_SAL_TOT_BY_IGEN ||
                    mnTabTypeAux01 == SDataConstantsSys.TRNX_PUR_TOT_BY_IGEN_BP || mnTabTypeAux01 == SDataConstantsSys.TRNX_SAL_TOT_BY_IGEN_BP ?
                        "INNER JOIN erp.itmu_igen AS ig ON i.fid_igen = ig.id_igen " : "" ) +
                (mnTabTypeAux01 == SDataConstantsSys.TRNX_PUR_TOT_BY_IGEN_IREF || mnTabTypeAux01 == SDataConstantsSys.TRNX_SAL_TOT_BY_IGEN_IREF || 
                    mnTabTypeAux01 == SDataConstantsSys.TRNX_PUR_TOT_BY_IREF || mnTabTypeAux01 == SDataConstantsSys.TRNX_SAL_TOT_BY_IREF ? 
                        "LEFT JOIN erp.itmu_item AS ir ON e.fid_item_ref_n = ir.id_item LEFT JOIN erp.itmu_igen AS irg ON ir.fid_igen = irg.id_igen " : "") +
                "INNER JOIN erp.bpsu_bpb AS cob ON " +
                "doc.fid_cob = cob.id_bpb " +
                "WHERE e.b_del = FALSE AND doc.b_del = FALSE " +
                "AND doc.fid_ct_dps = " + (isPurchase() ? SDataConstantsSys.TRNU_TP_DPS_PUR_CN[0] : SDataConstantsSys.TRNU_TP_DPS_SAL_CN[0]) + " " +
                "AND doc.fid_cl_dps = " + (isPurchase() ? SDataConstantsSys.TRNU_TP_DPS_PUR_CN[1] : SDataConstantsSys.TRNU_TP_DPS_SAL_CN[1]) + " " +
                "AND doc.fid_tp_dps = " + (isPurchase() ? SDataConstantsSys.TRNU_TP_DPS_PUR_CN[2] : SDataConstantsSys.TRNU_TP_DPS_SAL_CN[2]) + " " +
                "AND doc.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " AND doc.fid_st_dps_val = " + SDataConstantsSys.TRNS_ST_DPS_VAL_EFF + " " +
                (withRelatedParts ? "" : " AND bp.b_att_rel_pty = 0 ") +
                "AND e.fid_tp_dps_adj = " + SDataConstantsSys.TRNS_TP_DPS_ADJ_DISC + " " +
                sqlCompanyBranch + sqlDatePeriod + sqlFunctAreas +
                sqlGroupOrder + ")) " +
                "AS t " +
                sqlGroupOrder + " ";
    }

    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {

        }
    }

    @Override
    public void actionEdit() {
        if (jbEdit.isEnabled()) {

        }
    }

    @Override
    public void actionDelete() {
        if (jbDelete.isEnabled()) {

        }
    }
}
