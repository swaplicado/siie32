/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.view;

import java.util.Date;
import javax.swing.JButton;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.table.STabFilterDatePeriod;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;

import net.sf.jasperreports.view.save.JRPdfSaveContributor.*;
import net.sf.jasperreports.view.JRViewer.*;
import net.sf.jasperreports.view.save.JRMultipleSheetsXlsSaveContributor.*;

/**
 *
 * @author Néstor Ávalos
 */
public class SViewDpsPay extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private erp.lib.table.STabFilterDatePeriod moTabFilterDatePeriod;

    public SViewDpsPay(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01, int auxType02) {
        super(client, tabTitle, (auxType02 == SDataConstants.TRNX_DPS_PAY_PEND ?
            SDataConstants.TRNX_DPS_PAY_PEND : SDataConstants.TRNX_DPS_PAYED), auxType01, auxType02);
        initComponents();
    }

    private void initComponents() {
        int i;

        moTabFilterDatePeriod = new STabFilterDatePeriod(miClient, this, SLibConstants.GUI_DATE_AS_DATE);

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDatePeriod);

        STableField[] aoKeyFields = new STableField[2];
        STableColumn[] aoTableColumns = new STableColumn[38];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "d.id_year");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "d.id_doc");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        switch (mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_PUR ?
            miClient.getSessionXXX().getParamsErp().getFkSortingDpsSupplierTypeId() :
            miClient.getSessionXXX().getParamsErp().getFkSortingDpsCustomerTypeId()) {

            case SDataConstantsSys.CFGS_TP_SORT_DOC_BIZ_P:
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "d.dt", "Fecha doc.", STableConstants.WIDTH_DATE);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tp.code", "Tipo doc.", STableConstants.WIDTH_CODE_DOC);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_num", "Folio doc.", STableConstants.WIDTH_DOC_NUM);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "d.num_ref", "Referencia doc.", STableConstants.WIDTH_DOC_NUM_REF);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cb.code", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);

                switch(mnTabTypeAux01) {
                    case SDataConstantsSys.TRNS_CT_DPS_SAL:
                        if (miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bc.bp_key", "Clave AN", 50);
                            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 200);
                        }
                        else {
                            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 200);
                            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bc.bp_key", "Clave AN", 50);
                        }
                        break;
                    case SDataConstantsSys.TRNS_CT_DPS_PUR:
                        if (miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bc.bp_key", "Clave AN", 50);
                            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 200);
                        }
                        else {
                            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 200);
                            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bc.bp_key", "Clave AN", 50);
                        }
                        break;
                }

                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb.bpb", "Sucursal AN", 100);
                break;
            case SDataConstantsSys.CFGS_TP_SORT_BIZ_P_DOC:

                switch(mnTabTypeAux01) {
                    case SDataConstantsSys.TRNS_CT_DPS_SAL:
                        if (miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bc.bp_key", "Clave AN", 50);
                            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 200);
                        }
                        else {
                            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 200);
                            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bc.bp_key", "Clave AN", 50);
                        }
                        break;
                    case SDataConstantsSys.TRNS_CT_DPS_PUR:
                        if (miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bc.bp_key", "Clave AN", 50);
                            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 200);
                        }
                        else {
                            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 200);
                            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bc.bp_key", "Clave AN", 50);
                        }
                        break;
                }

                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb.bpb", "Sucursal AN", 100);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "d.dt", "Fecha doc.", STableConstants.WIDTH_DATE);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tp.code", "Tipo doc.", STableConstants.WIDTH_CODE_DOC);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_num", "Folio doc.", STableConstants.WIDTH_DOC_NUM);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "d.num_ref", "Referencia doc.", STableConstants.WIDTH_DOC_NUM_REF);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cb.code", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
                break;
            default:
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "d.dt", "Fecha doc.", STableConstants.WIDTH_DATE);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tp.code", "Tipo doc.", STableConstants.WIDTH_CODE_DOC);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_num", "Folio doc.", STableConstants.WIDTH_DOC_NUM);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "d.num_ref", "Referencia doc.", STableConstants.WIDTH_DOC_NUM_REF);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cb.code", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 200);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bc.bp_key", "Clave AN", 50);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb.bpb", "Sucursal AN", 100);
        }
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_tot_net", "Total neto $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_balance", "Saldo $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cur_key_loc", "Moneda", STableConstants.WIDTH_CURRENCY_KEY);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_tot_net_cur", "Total neto mon $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_balance_cur", "Saldo mon $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "c.cur_key", "Moneda", STableConstants.WIDTH_CURRENCY_KEY);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "d.stot_r", "Subtot. $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "d.tax_charged_r", "Imp tras $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "d.tax_retained_r", "Imp ret $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "d.tot_r", "Total $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cur_key_loc", "Moneda", STableConstants.WIDTH_CURRENCY_KEY);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "d.exc_rate", "T cambio", STableConstants.WIDTH_EXCHANGE_RATE);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererExchangeRate());
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "d.stot_cur_r", "Subtot. mon $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "d.tax_charged_cur_r", "Imp tras mon $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "d.tax_retained_cur_r", "Imp ret mon $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "d.tot_cur_r", "Total mon $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "c.cur_key", "Moneda", STableConstants.WIDTH_CURRENCY_KEY);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "d.b_link", "Vinculado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ul.usr", "Usr. vinculación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "d.ts_link", "Vinculación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "d.b_close", "Cerrado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "uc.usr", "Usr. cierre", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "d.ts_close", "Cierre", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "d.b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un.usr", "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "d.ts_new", "Creación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ue.usr", "Usr. modificación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "d.ts_edit", "Modificación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ud.usr", "Usr. eliminación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "d.ts_del", "Eliminación", STableConstants.WIDTH_DATE_TIME);
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.TRN_DSM);
        mvSuscriptors.add(SDataConstants.TRN_DPS);
        mvSuscriptors.add(SDataConstants.CFGU_CUR);
        mvSuscriptors.add(SDataConstants.BPSU_BP);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        populateTable();
    }

    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {
            int gui = mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_PUR ? SDataConstants.MOD_PUR : SDataConstants.MOD_SAL;    // GUI module

            if (miClient.getGuiModule(gui).showForm(mnTabType, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                miClient.getGuiModule(gui).refreshCatalogues(mnTabType);
            }
        }
    }

    @Override
    public void actionEdit() {
        int gui = mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_PUR ? SDataConstants.MOD_PUR : SDataConstants.MOD_SAL;    // GUI module

        if (jbEdit.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                if (miClient.getGuiModule(gui).showForm(mnTabType, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                    miClient.getGuiModule(gui).refreshCatalogues(mnTabType);
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
        int[] period = null;
        Date tSqlDateCut = miClient.getSessionXXX().getWorkingDate();
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                period = (int[]) setting.getSetting();

                if (period.length < 3) {
                    miClient.showMsgBoxWarning("Favor de especificar una fecha de corte.");
                } else {
                    tSqlDateCut = SLibTimeUtilities.createDate(period[0], period[1], period[2]);
                }
            }
         }

        msSql = "SELECT bp.bp, bc.bp_key, bpb.bpb, d.id_year, d.id_doc, d.dt, d.num_ref, concat(d.num_ser, if(length(d.num_ser) = 0, '', '-'), d.num) as f_num, d.exc_rate, d.stot_r, "  +
            "d.tax_charged_r, d.tax_retained_r, d.tot_r, d.stot_cur_r, d.tax_charged_cur_r, d.tax_retained_cur_r, d.tot_cur_r, d.b_link, d.b_close, d.b_del, " +
            "d.ts_link, d.ts_close, d.ts_new, d.ts_edit, d.ts_del, tp.code, bp.id_bp, c.cur_key, '" +
            miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getKey() + "' AS cur_key_loc, cb.code, ul.usr, uc.usr, un.usr, ue.usr, ud.usr, " +
            "d.tot_cur_r - COALESCE((SELECT SUM(dda.val_cur) FROM trn_dps_dps_adj AS dda " +
            "WHERE dda.id_dps_year = d.id_year AND dda.id_dps_doc = d.id_doc), 0) AS f_tot_net_cur, " +
            "SUM(IF(d.fid_cur = re.fid_cur, IF(d.fid_ct_dps = " + SDataConstantsSys.TRNS_CT_DPS_PUR + ", -1, 1), 0) * (re.debit_cur - re.credit_cur)) AS f_balance_cur, " +
            "d.tot_r - COALESCE((SELECT SUM(dda.val) FROM trn_dps_dps_adj AS dda " +
            "WHERE dda.id_dps_year = d.id_year AND dda.id_dps_doc = d.id_doc), 0) AS f_tot_net, " +
            "SUM(IF(d.fid_ct_dps = " + SDataConstantsSys.TRNS_CT_DPS_PUR + ", -1, 1) * (re.debit - re.credit)) AS f_balance " +
            "FROM fin_rec AS r " +
            "INNER JOIN fin_rec_ety AS re ON " +
            "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num AND r.b_del = FALSE AND " +
            "re.b_del = FALSE AND r.id_year = " + (period.length > 0 ? period[0] : miClient.getSessionXXX().getWorkingYear())  + " AND r.dt <= '" +
            miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(tSqlDateCut) + "' AND " +
            "re.fid_ct_sys_mov_xxx = " + (mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_PUR ? SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[0] : SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS[0]) + " AND " +
            "re.fid_tp_sys_mov_xxx = " + (mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_PUR ? SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[1] : SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS[1]) + " " +
            "INNER JOIN erp.bpsu_bp AS bp ON re.fid_bp_nr = bp.id_bp " +
            "INNER JOIN erp.bpsu_bp_ct AS bc ON bp.id_bp = bc.id_bp AND bc.id_ct_bp = " + (mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_PUR ?
            SDataConstantsSys.BPSS_CT_BP_SUP :  SDataConstantsSys.BPSS_CT_BP_CUS) + " " +
            "LEFT OUTER JOIN trn_dps AS d ON re.fid_dps_year_n = d.id_year AND re.fid_dps_doc_n = d.id_doc AND d.b_del = FALSE " +
            "INNER JOIN erp.bpsu_bpb AS bpb ON d.fid_bpb = bpb.id_bpb " +
            "INNER JOIN erp.trnu_tp_dps AS tp ON d.fid_ct_dps = tp.id_ct_dps AND d.fid_cl_dps = tp.id_cl_dps AND d.fid_tp_dps = tp.id_tp_dps AND d.b_del = FALSE " +
            "INNER JOIN erp.cfgu_cur AS c ON d.fid_cur = c.id_cur " +
            "INNER JOIN erp.bpsu_bpb AS cb ON d.fid_cob = cb.id_bpb " +
            "INNER JOIN erp.usru_usr AS ul ON d.fid_usr_link = ul.id_usr " +
            "INNER JOIN erp.usru_usr AS uc ON d.fid_usr_close = uc.id_usr " +
            "INNER JOIN erp.usru_usr AS un ON d.fid_usr_new = un.id_usr " +
            "INNER JOIN erp.usru_usr AS ue ON d.fid_usr_edit = ue.id_usr " +
            "INNER JOIN erp.usru_usr AS ud ON d.fid_usr_del = ud.id_usr " +
            "GROUP BY d.id_year, d.id_doc, d.dt, d.num_ser, d.num, f_tot_net, tp.code, c.cur_key, cb.code " +
            "HAVING " + (mnTabTypeAux02 == SDataConstants.TRNX_DPS_PAY_PEND ? "f_balance_cur <> 0" : "f_balance_cur = 0") + " ";

        switch (mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_PUR ?
            miClient.getSessionXXX().getParamsErp().getFkSortingDpsSupplierTypeId() :
            miClient.getSessionXXX().getParamsErp().getFkSortingDpsCustomerTypeId()) {

            case SDataConstantsSys.CFGS_TP_SORT_DOC_BIZ_P:
                msSql += "ORDER BY d.dt, tp.code, d.num_ser, d.num, bp.bp, bp.id_bp ";
                break;
            case SDataConstantsSys.CFGS_TP_SORT_BIZ_P_DOC:
                msSql += "ORDER BY bp.bp, bp.id_bp, d.dt, tp.code, d.num_ser, d.num ";
                break;
            default:
                msSql += "ORDER BY d.dt, tp.code, d.num_ser, d.num, bp.bp, bp.id_bp ";
        }
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (JButton) e.getSource();
        }
    }
}
