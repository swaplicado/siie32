/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.view;


import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.table.STabFilterDatePeriodRange;
import erp.lib.table.STabFilterDeleted;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import erp.mod.SModSysConsts;
import java.util.Date;
import javax.swing.JButton;

/**
 *
 * @author Néstor Ávalos
 */
public class SViewProductionOrderProgramMonitoring extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;
    private erp.lib.table.STabFilterDatePeriodRange moTabFilterDatePeriodRange;

    public SViewProductionOrderProgramMonitoring(erp.client.SClientInterface client, java.lang.String tabTitle) {
        super(client, tabTitle, SDataConstantsSys.REP_MFG_PROG_MON);
        initComponents();
    }

    private void initComponents() {
        int i=0;

        moTabFilterDeleted = new STabFilterDeleted(this);
        moTabFilterDatePeriodRange = new STabFilterDatePeriodRange(miClient, this);

        jbNew.setEnabled(false);
        jbEdit.setEnabled(false);
        //jbDelete.setEnabled(false);

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDatePeriodRange);

        erp.lib.table.STableField[] aoKeyFields = new STableField[7];
        erp.lib.table.STableColumn[] aoTableColumns = new STableColumn[19];

        i=0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "s.id_year");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "s.id_item");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "s.id_unit");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "s.id_lot");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "s.id_cob");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "s.id_wh");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "s.id_mov");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i=0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "o.dt_dly", "F. entrega prog.", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_turn_dly", "T. programado", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_bcode", "Sucursal", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_wcode", "Almacén", STableConstants.WIDTH_CODE_COB_ENT);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "t.tp", "Tipo", 120);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_num", "Folio", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "o.ref", "Referencia", 300);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "f_dt_start", "F. inicio", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_turn_start", "T. inicio", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "f_hr_start", "Hrs. iniciada", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un.symbol", "Unidad", STableConstants.WIDTH_UNIT_SYMBOL);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "o.qty", "Cant. programada", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "u.symbol", "Unidad", STableConstants.WIDTH_UNIT_SYMBOL);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_balance", "Cant. recibida", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "us.symbol", "Unidad", STableConstants.WIDTH_UNIT_SYMBOL);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "f_dt_end", "F. termino", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_turn_end", "T. termino", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "f_hr_end", "Hrs. prod.", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un.symbol", "Unidad", STableConstants.WIDTH_UNIT_SYMBOL);

        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.MFG_ORD);
        mvSuscriptors.add(SDataConstants.BPSU_BPB);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        populateTable();
    }

    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {
            if (miClient.getGuiModule(SDataConstants.MOD_MFG).showForm(mnTabType, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                miClient.getGuiModule(SDataConstants.MOD_MFG).refreshCatalogues(mnTabType);
            }
        }
    }

    @Override
    public void actionEdit() {
        if (jbEdit.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                if (miClient.getGuiModule(SDataConstants.MOD_MFG ).showForm(mnTabType, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                    miClient.getGuiModule(SDataConstants.MOD_MFG).refreshCatalogues(mnTabType);
                }
            }
        }
    }

    @Override
    public void actionDelete() {
        if (jbDelete.isEnabled()) {

        }
    }

    @Override
    public void createSqlQuery() {
        Date[] vRange = null;
        String sSqlDatePeriod = "";
        String sSqlWhere = "";
        STableSetting oSetting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            oSetting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (oSetting.getType() == STableConstants.SETTING_FILTER_DELETED && oSetting.getStatus() == STableConstants.STATUS_ON) {
                sSqlWhere += (sSqlWhere.length() == 0 ? "" : "AND ") + "s.b_del = FALSE ";
            }
            else if (oSetting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                vRange = (java.util.Date[])oSetting.getSetting();
                sSqlDatePeriod += " AND o.dt_dly BETWEEN '" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(vRange[0]) + "' AND '" +
                        miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(vRange[1]) + "' ";
            }
        }

        msSql = "SELECT o.dt_dly, tu.turn AS f_turn_dly, b.code AS f_bcode, c.code AS f_wcode, t.tp, CONCAT(o.id_year, '-',erp.lib_fix_int(o.id_ord,6)) AS f_num, o.ref, " +
                "COALESCE(o.ts_start_n, '(N/A)') AS f_dt_start, COALESCE(TIMESTAMPDIFF(hour, TIMESTAMP(o.dt_dly, tu.hr_start), o.ts_start_n), 0) AS f_hr_start, " +
                "tus.turn AS f_turn_start, o.qty, u.symbol, SUM(s.mov_in - s.mov_out) AS f_balance, us.symbol, COALESCE(o.ts_end_n, '(N/A)') AS f_dt_end, " +
                "COALESCE(TIMESTAMPDIFF(hour, o.ts_start_n, o.ts_end_n), 0) AS f_hr_end, tue.turn AS f_turn_end, un.symbol, s.* " +
                "FROM trn_stk AS s " +
                "INNER JOIN mfg_ord AS o ON s.fid_mfg_year_n = o.id_year AND s.fid_mfg_ord_n = o.id_ord AND o.b_del = 0 " +
                "INNER JOIN mfg_ord_chg AS oc ON s.fid_mfg_year_n = oc.id_year AND s.fid_mfg_ord_n = oc.id_ord AND s.fid_mfg_chg_n = oc.id_chg AND " +
                "s.fid_ct_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_FG_ASD[0] + " AND s.fid_cl_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_FG_ASD[1] +
                " AND s.fid_tp_iog = " + SDataConstantsSys.TRNS_TP_IOG_IN_MFG_FG_ASD[2] + " AND s.b_del = 0 AND oc.b_del = 0 " +
                "INNER JOIN erp.mfgu_tp_ord AS t ON o.fid_tp_ord = t.id_tp " +
                "INNER JOIN erp.bpsu_bpb AS b ON o.fid_cob = b.id_bpb " +
                "INNER JOIN erp.cfgu_cob_ent AS c ON o.fid_cob = c.id_cob AND o.fid_ent = c.id_ent " +
                "INNER JOIN erp.itmu_item AS i ON o.fid_item_r = i.id_item " +
                "INNER JOIN erp.itmu_unit AS u ON o.fid_unit_r = u.id_unit " +
                "INNER JOIN erp.itmu_unit AS us ON s.id_unit = us.id_unit " +
                "INNER JOIN erp.mfgu_turn AS tu ON o.fid_turn_dly = tu.id_turn " +
                "INNER JOIN erp.mfgu_turn AS tus ON o.fid_turn_start = tus.id_turn " +
                "INNER JOIN erp.mfgu_turn AS tue ON o.fid_turn_end = tue.id_turn " +
                "INNER JOIN erp.itmu_unit AS un ON un.id_unit = " + SModSysConsts.ITMU_UNIT_HOUR + " " +
                (sSqlWhere.length() == 0 ? "" : "WHERE " + sSqlWhere) + sSqlDatePeriod + " " +
                "ORDER BY o.dt_dly, tu.turn, b.code, c.code, f_num;";
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (javax.swing.JButton) e.getSource();
        }
    }
}
