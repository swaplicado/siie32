/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.view;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.table.STabFilterDeleted;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.mod.fin.db.SFinConsts;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author Juan Barajas
 */
public class SViewLayoutBank extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private javax.swing.JButton jbPayment;
    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;

    public SViewLayoutBank(erp.client.SClientInterface client, java.lang.String tabTitle) {
        super(client, tabTitle, SDataConstants.FIN_LAY_BANK);
        initComponents();
    }

    private void initComponents() {
        int i;


        jbPayment = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_money.gif")));
        jbPayment.setPreferredSize(new Dimension(23, 23));
        jbPayment.setToolTipText("Realizar pagos");
        jbPayment.addActionListener(this);

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(jbPayment);

        moTabFilterDeleted = new STabFilterDeleted(this);

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);

        jbNew.setEnabled(true);
        jbEdit.setEnabled(true);
        //jbDelete.setEnabled(false);

        STableField[] aoKeyFields = new STableField[1];
        STableColumn[] aoTableColumns = new STableColumn[19];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "l.id_lay_bank");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "l.dt_lay", "Pago", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "l.dt_due", "Vencimiento", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_lay", "Layout", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_tp_lay", "Tipo layout", 200);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_ent", "Cuenta cargo", 250);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "l.cpt", "Concepto", 250);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "l.con", "Consecutivo", 50);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "l.amt", "Monto $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "l.amt_pay", "Monto pagado $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_amt_x_pay", "Monto x pagar $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_cur_key_local", "Moneda", STableConstants.WIDTH_CURRENCY_KEY);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "l.dps", "Documentos", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "l.dps_pay", "Documentos pagados", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "f_doc_x_pay", "Documentos x pagar", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "l.b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un.usr", "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "l.ts_usr_ins", "Creación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ue.usr", "Usr. modificación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "l.ts_usr_upd", "Modificación", STableConstants.WIDTH_DATE_TIME);
        
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        mvSuscriptors.add(mnTabType);
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

    public void actionPayment() {
        if (jbPayment.isEnabled()) {
        }
    }

    @Override
    public void createSqlQuery() {
        java.lang.String sqlWhere = "";
        erp.lib.table.STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_DELETED && setting.getStatus() == STableConstants.STATUS_ON) {
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "l.b_del = FALSE ";
            }
        }

        msSql = "SELECT l.id_lay_bank, l.dt_lay, l.dt_due, l.cpt, l.con, l.amt, l.amt_pay, (l.amt - l.amt_pay) AS f_amt_x_pay, l.dps, l.dps_pay, (l.dps - l.dps_pay) AS f_doc_x_pay, tp.tp_lay_bank AS f_tp_lay, " +
                "IF(l.fk_lay_bank = " + SFinConsts.LAY_BANK_HSBC + ", '" + SFinConsts.TXT_LAY_BANK_HSBC + "', IF(l.fk_lay_bank = " + SFinConsts.LAY_BANK_SANTANDER + ", '" + SFinConsts.TXT_LAY_BANK_SANTANDER + "', '" + SFinConsts.TXT_LAY_BANK_BANBAJIO + "')) AS f_lay, " +
                "CONCAT(e.ent, ' (', e.code, ')') AS f_ent, l.b_del, " +
                "(SELECT c.cur_key FROM erp.cfgu_cur AS c WHERE a.fid_cur = c.id_cur) AS f_cur_key, 'MXN' AS f_cur_key_local, " +
                "l.ts_usr_ins, l.ts_usr_upd, un.usr, ue.usr " +
                "FROM fin_lay_bank AS l " +
                "INNER JOIN erp.finu_tp_lay_bank AS tp ON " +
                "l.fk_tp_lay_bank = tp.id_tp_lay_bank " +
                "INNER JOIN fin_acc_cash AS a ON " +
                "a.id_cob = l.fk_bank_cob AND a.id_acc_cash = l.fk_bank_acc_cash " +
                "INNER JOIN erp.cfgu_cob_ent AS e ON " +
                "a.id_cob = e.id_cob AND a.id_acc_cash = e.id_ent " +
                "INNER JOIN erp.usru_usr AS un ON " +
                "l.fk_usr_ins = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON " +
                "l.fk_usr_upd = ue.id_usr " +
                (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere) +
                "ORDER BY l.id_lay_bank ";
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbPayment) {
                actionPayment();
            }
        }
    }
}
