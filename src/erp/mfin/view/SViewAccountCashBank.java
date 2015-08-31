/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.table.STabFilterDeleted;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import erp.mcfg.data.SDataParamsCompany;
import javax.swing.JButton;
import sa.gui.util.SUtilConsts;

/**
 *
 * @author Sergio Flores
 */
public class SViewAccountCashBank extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;

    public SViewAccountCashBank(erp.client.SClientInterface client, java.lang.String tabTitle) {
        super(client, tabTitle, SDataConstants.FINX_ACC_CASH_BANK);
        initComponents();
    }

    private void initComponents() {
        int i;
        int levelRightEdit = SDataConstantsSys.UNDEFINED;

        moTabFilterDeleted = new STabFilterDeleted(this);

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);

        STableField[] aoKeyFields = new STableField[2];
        STableColumn[] aoTableColumns = new STableColumn[19];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "ac.id_cob");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "ac.id_acc_cash");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cob.bpb", "Sucursal empresa", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "e.ent", "Cuenta bancaria", 200);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "e.code", "Clave", 50);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "c.cur_key", "Moneda", STableConstants.WIDTH_CURRENCY_KEY);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "t.tp_acc_cash", "Tipo de cuenta", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ba.bank_acc", "Cuenta bancaria empresa", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bk.bp_comm", "Banco", 200);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_acc", "No. cuenta contable", STableConstants.WIDTH_ACCOUNT_ID);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "a.acc", "Cuenta contable", STableConstants.WIDTH_ACCOUNT);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "ac.b_check_wal", "Chequera", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cf.check_fmt", "Formato chequera", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cfg.check_fmt_gp", "Formato chequera gráfica", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "ac.b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un.usr", "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "ac.ts_new", "Creación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ue.usr", "Usr. modificación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "ac.ts_edit", "Modificación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ud.usr", "Usr. eliminación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "ac.ts_del", "Eliminación", STableConstants.WIDTH_DATE_TIME);
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        levelRightEdit = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_FIN_ACC_CASH).Level;

        jbNew.setEnabled(levelRightEdit >= SUtilConsts.LEV_AUTHOR);
        jbEdit.setEnabled(levelRightEdit >= SUtilConsts.LEV_AUTHOR);
        //jbDelete.setEnabled(false);

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.CFGU_CO);
        mvSuscriptors.add(SDataConstants.CFGU_COB_ENT);
        mvSuscriptors.add(SDataConstants.CFGU_CUR);
        mvSuscriptors.add(SDataConstants.BPSU_BP);
        mvSuscriptors.add(SDataConstants.BPSU_BPB);
        mvSuscriptors.add(SDataConstants.BPSU_BANK_ACC);
        mvSuscriptors.add(SDataConstants.FIN_ACC);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        populateTable();
    }

    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {
            if (miClient.getGuiModule(SDataConstants.MOD_FIN).showForm(mnTabType, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                miClient.getGuiModule(SDataConstants.MOD_FIN).refreshCatalogues(mnTabType);
            }
        }
    }

    @Override
    public void actionEdit() {
        if (jbEdit.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                if (miClient.getGuiModule(SDataConstants.MOD_FIN).showForm(mnTabType, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                    miClient.getGuiModule(SDataConstants.MOD_FIN).refreshCatalogues(mnTabType);
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
        String sqlWhere = "";
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_DELETED && setting.getStatus() == STableConstants.STATUS_ON) {
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "ac.b_del = FALSE ";
            }
        }

        msSql = "SELECT ac.id_cob, ac.id_acc_cash, e.ent, e.code, ac.b_check_wal, ac.b_del, ac.fid_acc, f_acc_usr(" + ((SDataParamsCompany) miClient.getSession().getConfigCompany()).getMaskAccount() + ", a.code) AS f_acc, ac.ts_new, ac.ts_edit, " +
                "ac.ts_del, cob.id_bpb, cob.bpb, a.acc, t.tp_acc_cash, ba.bank_acc, bk.bp_comm, c.cur_key, cf.check_fmt, " +
                "cfg.check_fmt_gp, un.usr, ue.usr, ud.usr " +
                "FROM fin_acc_cash AS ac " +
                "INNER JOIN erp.cfgu_cob_ent AS e ON " +
                "ac.id_cob = e.id_cob AND ac.id_acc_cash = e.id_ent " +
                "INNER JOIN erp.bpsu_bpb AS cob ON " +
                "ac.id_cob = cob.id_bpb " +
                "INNER JOIN fin_acc AS a ON " +
                "ac.fid_acc = a.id_acc " +
                "INNER JOIN erp.fins_tp_acc_cash AS t ON " +
                "ac.fid_ct_acc_cash = t.id_ct_acc_cash AND ac.fid_tp_acc_cash = t.id_tp_acc_cash " +
                "INNER JOIN erp.bpsu_bank_acc AS ba ON " +
                "ac.fid_bpb_n = ba.id_bpb AND ac.fid_bank_acc_n = ba.id_bank_acc " +
                "INNER JOIN erp.bpsu_bp AS bk ON " +
                "ba.fid_bank = bk.id_bp " +
                "INNER JOIN erp.cfgu_cur AS c ON " +
                "ac.fid_cur = c.id_cur " +
                "INNER JOIN erp.usru_usr AS un ON " +
                "ac.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON " +
                "ac.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON " +
                "ac.fid_usr_del = ud.id_usr " +
                "LEFT OUTER JOIN erp.finu_check_fmt AS cf ON " +
                "ac.fid_check_fmt_n = cf.id_check_fmt " +
                "LEFT OUTER JOIN erp.finu_check_fmt_gp AS cfg ON " +
                "ac.fid_check_fmt_gp_n = cfg.id_check_fmt_gp " +
                "WHERE cob.fid_bp = " + miClient.getSessionXXX().getCurrentCompany().getPkCompanyId() + " AND ac.fid_ct_acc_cash = " + SDataConstantsSys.FINS_CT_ACC_CASH_BANK + " " +
                (sqlWhere.length() == 0 ? "" : "AND " + sqlWhere) +
                "ORDER BY cob.bpb, cob.id_bpb, e.ent, ac.id_cob, ac.id_acc_cash ";
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (JButton) e.getSource();
        }
    }
}
