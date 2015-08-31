/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.gui.SGuiModule;
import erp.lib.table.STabFilterDeleted;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import erp.mcfg.data.SDataParamsCompany;
import erp.mfin.data.SDataAccount;
import erp.mfin.data.SFinUtilities;
import java.awt.Cursor;
import java.awt.Dimension;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import sa.gui.util.SUtilConsts;

/**
 *
 * @author Sergio Flores
 */
public class SViewAccount extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private javax.swing.JButton mjbNewMajor;
    private javax.swing.JButton mjbChangeDeep;
    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;

    public SViewAccount(erp.client.SClientInterface client, java.lang.String tabTitle) {
        super(client, tabTitle, SDataConstants.FIN_ACC);
        initComponents();
    }

    private void initComponents() {
        int i;
        int levelRightEdit = SDataConstantsSys.UNDEFINED;

        mjbNewMajor = new JButton(miClient.getImageIcon(SLibConstants.ICON_NEW_MAIN));
        mjbChangeDeep = new JButton(miClient.getImageIcon(SLibConstants.ICON_ACTION));

        mjbNewMajor.setPreferredSize(new Dimension(23, 23));
        mjbChangeDeep.setPreferredSize(new Dimension(23, 23));

        mjbNewMajor.addActionListener(this);
        mjbChangeDeep.addActionListener(this);

        mjbNewMajor.setToolTipText("Crear cuenta de mayor");
        mjbChangeDeep.setToolTipText("Cambiar profundidad");

        moTabFilterDeleted = new STabFilterDeleted(this);

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(mjbNewMajor);        
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(mjbChangeDeep);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);

        STableField[] aoKeyFields = new STableField[1];
        STableField[] aoAditionalFields = new STableField[1];
        STableColumn[] aoTableColumns = new STableColumn[29];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_STRING, "a.id_acc");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        aoAditionalFields[i++] = new STableField(SLibConstants.DATA_TYPE_BOOLEAN, "f_is_major");
        for (i = 0; i < aoAditionalFields.length; i++) {
            moTablePane.getAditionalFields().add(aoAditionalFields[i]);
        }

        i = 0;
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_acc", "No. cuenta contable", STableConstants.WIDTH_ACCOUNT_ID);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererStyle());
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "a.acc", "Cuenta contable", STableConstants.WIDTH_ACCOUNT);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererStyle());
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "a.deep", "Profundidad", STableConstants.WIDTH_NUM_SMALLINT);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "a.lev", "Nivel", STableConstants.WIDTH_NUM_SMALLINT);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "a.dt_start", "Ini. vigencia", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "a.dt_end_n", "Fin. vigencia", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "a.b_req_cc", "Centro costo", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "a.b_req_ent", "Entidad", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "a.b_req_bp", "Asoc. negocios", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "a.b_req_item", "Ítem", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "a.b_contra_acc", "Subsidiaria", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "a.b_act", "Activa", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tpa.tp_acc_usr", "Tipo cuenta", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cla.cl_acc_usr", "Clase cuenta", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "clsa.cls_acc_usr", "Subclase cuenta", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "asys.tp_acc_sys", "Cuenta sistema", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "aled.tp_acc_ledger", "Cuenta libro mayor", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "aspe.name", "Cuenta especializada", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "aebi.tp_acc_ebitda", "Cuenta EBITDA", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cad.tp_adm_cpt", "Concepto administrativo", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ctx.tp_tax_cpt", "Concepto impuestos", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_acc_fiscal", "Código agrupador SAT", 200);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "a.b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un.usr", "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "a.ts_new", "Creación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ue.usr", "Usr. modificación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "a.ts_edit", "Modificación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ud.usr", "Usr. eliminación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "a.ts_del", "Eliminación", STableConstants.WIDTH_DATE_TIME);
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        levelRightEdit = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_FIN_ACC).Level;

        jbNew.setEnabled(levelRightEdit >= SUtilConsts.LEV_AUTHOR);
        jbEdit.setEnabled(levelRightEdit >= SUtilConsts.LEV_AUTHOR);
        mjbNewMajor.setEnabled(levelRightEdit >= SUtilConsts.LEV_AUTHOR);
        mjbChangeDeep.setEnabled(levelRightEdit >= SUtilConsts.LEV_AUTHOR);

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.FINU_TP_ACC_USR);
        mvSuscriptors.add(SDataConstants.FINU_CL_ACC_USR);
        mvSuscriptors.add(SDataConstants.FINU_CLS_ACC_USR);
        mvSuscriptors.add(SDataConstants.FINU_TP_ACC_LEDGER);
        mvSuscriptors.add(SDataConstants.FINU_TP_ACC_EBITDA);
        mvSuscriptors.add(SDataConstants.FINU_TP_ADM_CPT);
        mvSuscriptors.add(SDataConstants.FINU_TP_TAX_CPT);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        populateTable();
    }

    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {
            if (miClient.getGuiModule(SDataConstants.MOD_FIN).showForm(mnTabType, 1, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                miClient.getGuiModule(SDataConstants.MOD_FIN).refreshCatalogues(mnTabType);
            }
        }
    }

    @Override
    public void actionEdit() {
        if (jbEdit.isEnabled()) {
            int formType = mnTabType;

            if (moTablePane.getSelectedTableRow() != null) {
                if ((Boolean) ((Object[]) moTablePane.getSelectedTableRow().getData())[0]) {
                    formType = SDataConstants.FINX_ACC_MAJOR;
                }

                if (miClient.getGuiModule(SDataConstants.MOD_FIN).showForm(formType, SDataConstantsSys.FINX_ACC, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
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

    public void actionNewMajor() {
        if (miClient.getGuiModule(SDataConstants.MOD_FIN).showForm(SDataConstants.FINX_ACC_MAJOR, null) == SLibConstants.DB_ACTION_SAVE_OK) {
            miClient.getGuiModule(SDataConstants.MOD_FIN).refreshCatalogues(mnTabType);
        }
    }

    public void actionChangeDeep() {
        SDataAccount oldAccount = null;
        SDataAccount newAccount = null;
        SGuiModule guiModule = null;
        Cursor cursor = getCursor();
        
        if (mjbChangeDeep.isEnabled()) {
            setCursor(new Cursor(Cursor.WAIT_CURSOR));
            oldAccount = (SDataAccount) SDataUtilities.readRegistry(miClient, SDataConstants.FIN_ACC, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_VERBOSE);
            guiModule = miClient.getGuiModule(SDataConstants.MOD_FIN);
            try {
                if (SFinUtilities.changeDeepAccount(miClient, new int[] {oldAccount.getPkAccountId()}, 2, true)) {
                    guiModule.setFormComplement(oldAccount);
                    
                    if (guiModule.showForm(mnTabType, SDataConstantsSys.FINX_ACC_DEEP, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                        newAccount = (SDataAccount) SDataUtilities.readRegistry(miClient, SDataConstants.FIN_ACC, ((SDataAccount) guiModule.getRegistry()).getPrimaryKey(), SLibConstants.EXEC_MODE_VERBOSE);
                        SFinUtilities.changeRecortEntriesAccount(miClient, new int[] {oldAccount.getPkAccountId()},new int[] {newAccount.getPkAccountId()});
                    }
                    else {
                        SFinUtilities.changeDeepAccount(miClient, new int[] {oldAccount.getPkAccountId()}, 1, false);
                    }
                    
                    miClient.getGuiModule(SDataConstants.MOD_FIN).refreshCatalogues(mnTabType);
                }
            } catch (Exception ex) {
                miClient.showMsgBoxWarning(ex.getMessage());
            }
        }
        setCursor(cursor);
    }

    @Override
    public void createSqlQuery() {
        String sqlWhere = "";
        STableSetting setting = null;
        String account = miClient.getSessionXXX().getParamsErp().getFormatAccountId().replace('9', '0');
        Vector<Integer> levels = SDataUtilities.getAccountLevels(account);

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_DELETED && setting.getStatus() == STableConstants.STATUS_ON) {
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "a.b_del = FALSE ";
            }
        }

        msSql = "SELECT a.id_acc, f_acc_usr(" + ((SDataParamsCompany) miClient.getSession().getConfigCompany()).getMaskAccount() + ", a.code) AS f_acc, a.acc, a.deep, a.lev, a.dt_start, a.dt_end_n, a.b_req_cc, a.b_req_ent, a.b_req_bp, a.b_req_item, a.b_contra_acc, a.b_act, a.b_del, " +
                "tpa.tp_acc_usr, cla.cl_acc_usr, clsa.cls_acc_usr, asys.tp_acc_sys, aled.tp_acc_ledger, aebi.tp_acc_ebitda, aspe.name, " +
                "cad.tp_adm_cpt, ctx.tp_tax_cpt, CONCAT(fa.code, ' - ', fa.name) AS f_acc_fiscal, a.ts_new, a.ts_edit, a.ts_del, un.usr, ue.usr, ud.usr, a.lev = 1 AS f_is_major, " +
                "IF(a.lev = 1, " + STableConstants.STYLE_BOLD + ", IF(a.lev < am.deep, " + STableConstants.STYLE_ITALIC + ", " + STableConstants.UNDEFINED + ")) AS f_style " +
                "FROM fin_acc AS a " +
                "INNER JOIN fin_acc AS am ON " +
                "CONCAT(LEFT(a.id_acc, " + (levels.get(1) - 1) + "), '" + account.substring(levels.get(1) - 1) + "') = am.id_acc " +
                "INNER JOIN erp.finu_tp_acc_usr AS tpa ON " +
                "a.fid_tp_acc_usr = tpa.id_tp_acc_usr " +
                "INNER JOIN erp.finu_cl_acc_usr AS cla ON " +
                "a.fid_tp_acc_usr = cla.id_tp_acc_usr AND a.fid_cl_acc_usr = cla.id_cl_acc_usr " +
                "INNER JOIN erp.finu_cls_acc_usr AS clsa ON " +
                "a.fid_tp_acc_usr = clsa.id_tp_acc_usr AND a.fid_cl_acc_usr = clsa.id_cl_acc_usr AND a.fid_cls_acc_usr = clsa.id_cls_acc_usr " +
                "INNER JOIN erp.fins_tp_acc_sys AS asys ON " +
                "a.fid_tp_acc_sys = asys.id_tp_acc_sys " +
                "INNER JOIN erp.finu_tp_acc_ledger AS aled ON " +
                "a.fid_tp_acc_ledger = aled.id_tp_acc_ledger " +
                "INNER JOIN erp.fins_tp_acc_spe AS aspe ON " +
                "a.fid_tp_acc_spe = aspe.id_tp_acc_spe " +
                "INNER JOIN erp.finu_tp_acc_ebitda AS aebi ON " +
                "a.fid_tp_acc_ebitda = aebi.id_tp_acc_ebitda " +
                "INNER JOIN erp.finu_tp_adm_cpt AS cad ON " +
                "a.fid_tp_adm_cpt = cad.id_tp_adm_cpt " +
                "INNER JOIN erp.finu_tp_tax_cpt AS ctx ON " +
                "a.fid_tp_tax_cpt = ctx.id_tp_tax_cpt " +
                "INNER JOIN erp.fins_fiscal_acc AS fa ON " +
                "a.fid_fiscal_acc = fa.id_fiscal_acc " +
                "INNER JOIN erp.usru_usr AS un ON " +
                "a.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON " +
                "a.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON " +
                "a.fid_usr_del = ud.id_usr " +
                (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere) +
                "ORDER BY a.id_acc ";
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (JButton) e.getSource();

            if (button == mjbNewMajor) {
                actionNewMajor();
            }
            if (button == mjbChangeDeep) {
                actionChangeDeep();
            }
        }
    }
}
