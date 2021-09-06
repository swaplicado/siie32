/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.data.SDataSqlUtilities;
import erp.lib.table.STabFilterDatePeriod;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.table.SFilterConstants;
import erp.table.STabFilterFunctionalArea;

/**
 *
 * @author Daniel López, Sergio Flores, Edwin Carmona
 */
public class SViewBol extends erp.lib.table.STableTab {
    
    private erp.lib.table.STabFilterDatePeriod moTabFilterDatePeriod;
    private erp.table.STabFilterFunctionalArea moTabFilterFunctionalArea;

    public SViewBol(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01) {
        super(client, tabTitle, SDataConstants.TRNX_DOC_REMISSION, auxType01);
        initComponents();
    }

    private void initComponents() {
        int i;
       
        moTabFilterDatePeriod = new STabFilterDatePeriod(miClient, this, SLibConstants.GUI_DATE_AS_DATE);
        moTabFilterFunctionalArea = new STabFilterFunctionalArea(miClient, this);
        
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDatePeriod);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterFunctionalArea);
      
        erp.lib.table.STableColumn[] aoTableColumns = new STableColumn[11];

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "dt.code", "Tipo doc.", STableConstants.WIDTH_CODE_DOC);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_num", "Folio doc.", STableConstants.WIDTH_DOC_NUM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "d.num_ref", "Referencia doc.", STableConstants.WIDTH_DOC_NUM_REF);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "d.dt", "Fecha doc.", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_cob_code", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Cliente", 200);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpc.bp_key", "Clave cliente", 50);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb.bpb", "Sucursal cliente", 75);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "d.tot_cur_r", "Total mon $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_cur_key", "Moneda", STableConstants.WIDTH_CURRENCY_KEY);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "notes", "Numero remisión", STableConstants.WIDTH_ITEM_3X);
        
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        jbNew.setEnabled(false);
        jbEdit.setEnabled(false);

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        populateTable();
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

    @Override
    public void createSqlQuery() {
        java.lang.String sqlWhere = "";
        erp.lib.table.STableSetting setting = null;
        
        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                sqlWhere += "AND " + SDataSqlUtilities.composePeriodFilter((int[]) setting.getSetting(), "d.dt");
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_FUNC_AREA) {
                if (!((String) setting.getSetting()).isEmpty()) {
                    sqlWhere += "AND " + "d.fid_func IN (" + ((String) setting.getSetting()) + ") ";
                }
            }
        }

        msSql = "SELECT dt.code, CONCAT(d.num_ser, IF(length(d.num_ser) = 0, '', '-'), d.num) AS f_num, d.num_ref, d.dt, "
                + "(SELECT cob.code FROM erp.bpsu_bpb AS cob WHERE d.fid_cob = cob.id_bpb) AS f_cob_code, "
                + "bp.bp, bpc.bp_key, bpb.bpb, "
                + "d.tot_cur_r, "
                + "(SELECT c.cur_key FROM erp.cfgu_cur AS c WHERE d.fid_cur = c.id_cur) AS f_cur_key, "
                + "(SELECT GROUP_CONCAT(nts) FROM trn_dps_nts WHERE id_year = d.id_year AND id_doc = d.id_doc AND NOT b_del) AS notes "
                + "FROM trn_dps AS d "
                + "INNER JOIN erp.trnu_tp_dps AS dt ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps "
                + "INNER JOIN erp.bpsu_bp AS bp ON d.fid_bp_r = bp.id_bp "
                + "INNER JOIN erp.bpsu_bp_ct AS bpc ON bp.id_bp = bpc.id_bp AND bpc.id_ct_bp = " + SDataConstantsSys.BPSS_CT_BP_CUS + " "
                + "INNER JOIN erp.bpsu_bpb AS bpb ON d.fid_bpb = bpb.id_bpb "
                + "WHERE d.fid_ct_dps = " + (mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_SAL ? SDataConstantsSys.TRNU_TP_DPS_SAL_INV[0] : SDataConstantsSys.TRNU_TP_DPS_PUR_INV[0]) + " "
                + "AND d.fid_cl_dps = " + (mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_SAL ? SDataConstantsSys.TRNU_TP_DPS_SAL_INV[1] : SDataConstantsSys.TRNU_TP_DPS_PUR_INV[1]) + " "
                + "AND d.fid_tp_dps = " + (mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_SAL ? SDataConstantsSys.TRNU_TP_DPS_SAL_INV[2] : SDataConstantsSys.TRNU_TP_DPS_PUR_INV[2]) + " "
                + "AND d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " "
                + "AND NOT d.b_del " + sqlWhere
                + "ORDER BY f_num";
    }
}