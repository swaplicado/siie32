/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.view;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.data.SDataSqlUtilities;
import erp.lib.table.STabFilterDatePeriod;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;

/**
 *
 * @author Isabel Servín
 */
public class SViewDpsUpdateDateLog extends erp.lib.table.STableTab {

    private erp.lib.table.STabFilterDatePeriod moTabFilterDatePeriod;
    
    public SViewDpsUpdateDateLog(SClientInterface client, String tabTitle) {
        super(client, tabTitle, SDataConstants.TRN_DPS_UPD_DT_LOG);
        initComponents();
    }
    
    private void initComponents() {
        int i;
        
        jbNew.setEnabled(false);
        jbEdit.setEnabled(false);
        jbDelete.setEnabled(false);
        
        moTabFilterDatePeriod = new STabFilterDatePeriod(miClient, this, SLibConstants.GUI_DATE_AS_YEAR);
        addTaskBarUpperComponent(moTabFilterDatePeriod);
        
        STableField[] aoKeyFields = new STableField[1];
        STableColumn[] aoTableColumns = new STableColumn[8];
        
        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "l.id_dps_upd_dt_log");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }
        
        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "dt.code", "Tipo documento", STableConstants.WIDTH_CODE_DOC);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "d.num", "Folio", STableConstants.WIDTH_DOC_NUM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "d.num_ref", "Referencia", STableConstants.WIDTH_DOC_NUM_REF);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bp", "Cliente", 200);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "l.old_dt", "Fecha anterior", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "l.new_dt", "Fecha nueva", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "u.usr", "Usuario", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "l.ts_new", "Fecha cambio", STableConstants.WIDTH_DATE_TIME);
        
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }
        
        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.TRN_DPS);
        mvSuscriptors.add(SDataConstants.BPSU_BP);
        mvSuscriptors.add(SDataConstants.USRU_USR);
        
        populateTable();
    }

    @Override
    public void createSqlQuery() {
        String sqlWhere = "";
        STableSetting setting;
        
        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD){
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + SDataSqlUtilities.composePeriodFilter((int[]) setting.getSetting(), "l.ts_new");
            }
        }
        msSql = "SELECT l.id_dps_upd_dt_log, l.old_dt, l.new_dt, l.ts_new, d.num, d.num_ref, b.bp, u.usr, dt.code " +
                "FROM trn_dps_upd_dt_log AS l " +
                "INNER JOIN trn_dps AS d ON l.fid_year = d.id_year AND l.fid_doc = d.id_doc " +
                "INNER JOIN erp.trnu_tp_dps AS dt ON d.fid_ct_dps = id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps " +
                "INNER JOIN erp.bpsu_bp AS b ON d.fid_bp_r = b.id_bp " +
                "INNER JOIN erp.usru_usr AS u ON l.fid_usr_new = u.id_usr " + 
                (sqlWhere.isEmpty() ? "" : "WHERE " + sqlWhere);
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
