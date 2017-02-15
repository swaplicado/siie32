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

/**
 *
 * @author Daniel López
 */
public class SViewBol extends erp.lib.table.STableTab {
    
    private erp.lib.table.STabFilterDatePeriod moTabFilterDatePeriod;

    public SViewBol(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01) {
        super(client, tabTitle, SDataConstants.TRNX_DOC_REMISSION, auxType01);
        initComponents();
    }

    private void initComponents() {
        int i;
       
        moTabFilterDatePeriod = new STabFilterDatePeriod(miClient, this, SLibConstants.GUI_DATE_AS_DATE);
        
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDatePeriod);
      
        erp.lib.table.STableColumn[] aoTableColumns = new STableColumn[8];

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "dt.code", "Tipo doc.", STableConstants.WIDTH_CODE_DOC);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_num", "Folio doc.", STableConstants.WIDTH_DOC_NUM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "d.num_ref", "Referencia doc.", STableConstants.WIDTH_DOC_NUM_REF);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "d.dt", "Fecha doc.", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_cob_code", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "d.tot_cur_r", "Total mon $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_cur_key", "Moneda", STableConstants.WIDTH_CURRENCY_KEY);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "dn.nts", "Numero remisión", STableConstants.WIDTH_ITEM_2X);
        
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
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + SDataSqlUtilities.composePeriodFilter((int[]) setting.getSetting(), "d.dt");
            }
        }

        msSql = "SELECT dt.code, CONCAT(d.num_ser, IF(length(d.num_ser) = 0, '', '-'), d.num) AS f_num, d.num_ref, d.dt, "
                + "(SELECT cob.code FROM erp.bpsu_bpb AS cob WHERE d.fid_cob = cob.id_bpb) AS f_cob_code, d.tot_cur_r, "
                + "(SELECT c.cur_key FROM erp.cfgu_cur AS c WHERE d.fid_cur = c.id_cur) AS f_cur_key, dn.nts "
                + "FROM trn_dps AS d INNER JOIN trn_dps_nts AS dn ON (d.id_year = dn.id_year AND d.id_doc = dn.id_doc) "
                + "INNER JOIN erp.trnu_tp_dps AS dt ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps "
                + "WHERE d.fid_ct_dps = " + (mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_SAL ? SDataConstantsSys.TRNU_TP_DPS_SAL_INV[0] : SDataConstantsSys.TRNU_TP_DPS_PUR_INV[0]) + " "
                + "AND d.fid_cl_dps = " + (mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_SAL ? SDataConstantsSys.TRNU_TP_DPS_SAL_INV[1] : SDataConstantsSys.TRNU_TP_DPS_PUR_INV[1]) + " "
                + "AND d.fid_tp_dps = " + (mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_SAL ? SDataConstantsSys.TRNU_TP_DPS_SAL_INV[2] : SDataConstantsSys.TRNU_TP_DPS_PUR_INV[2]) + " "
                + "AND d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " "
                + "AND not d.b_del AND NOT dn.b_del AND " + sqlWhere;
    }
}