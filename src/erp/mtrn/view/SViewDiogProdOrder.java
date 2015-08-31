/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.gui.SModuleUtilities;
import erp.lib.SLibConstants;
import erp.lib.data.SDataSqlUtilities;
import erp.lib.form.SFormUtilities;
import erp.lib.gui.SGuiModule;
import erp.lib.table.STabFilterDatePeriod;
import erp.lib.table.STabFilterDeleted;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import erp.mtrn.data.STrnDiogComplement;
import erp.mtrn.data.STrnUtilities;
import erp.table.SFilterConstants;
import erp.table.STabFilterCompanyBranchEntity;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import sa.gui.util.SUtilConsts;

/**
 *
 * @author Sergio Flores
 */
public class SViewDiogProdOrder extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private int[] manIogTypeKey;
    private javax.swing.JButton mjbAssign;
    private javax.swing.JButton mjbReturn;
    private javax.swing.JButton mjbViewNotes;
    private javax.swing.JButton jbPrint;
    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;
    private erp.lib.table.STabFilterDatePeriod moTabFilterDatePeriod;
    private erp.table.STabFilterCompanyBranchEntity moTabFilterCompanyBranchEntity;

    /**
     * @param client GUI client interface.
     * @param tabTitle View's title.
     * @param auxType01 View's auxiliar type 1 (constants defined in SDataConstants.TRNX_DIOG_MFG_...).
     * @param auxType02 View's auxiliar type 2 (constants defined in SDataConstants.TRNX_DIOG_MFG_MOVE...).
     */
    public SViewDiogProdOrder(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01, int auxType02) {
        super(client, tabTitle, SDataConstants.TRNX_DIOG_MFG, auxType01, auxType02);
        initComponents();
    }

    private void initComponents() {
        int i = 0;
        int levelRight = SDataConstantsSys.UNDEFINED;
        int levelRightAssign = SDataConstantsSys.UNDEFINED;
        int levelRightReturn = SDataConstantsSys.UNDEFINED;
        boolean createButtons = false;
        boolean hasRightAssign = false;
        boolean hasRightReturn = false;
        String toolTipAssign = "";
        String toolTipReturn = "";
        ImageIcon imageAssign = null;
        ImageIcon imageReturn = null;

        moTabFilterDeleted = new STabFilterDeleted(this);
        moTabFilterDatePeriod = new STabFilterDatePeriod(miClient, this, SLibConstants.GUI_DATE_AS_YEAR_MONTH);
        moTabFilterCompanyBranchEntity = new STabFilterCompanyBranchEntity(miClient, this, SDataConstantsSys.CFGS_CT_ENT_WH);

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDatePeriod);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterCompanyBranchEntity);

        levelRight = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_OUT_MFG).Level;

        switch (mnTabTypeAux01) {
            case SDataConstants.TRNX_DIOG_MFG_RM:
                createButtons = true;
                levelRightAssign = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_MFG_RM_ASG).Level;
                levelRightReturn = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_MFG_RM_DEV).Level;
                hasRightAssign = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_MFG_RM_ASG).HasRight;
                hasRightReturn = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_MFG_RM_DEV).HasRight;
                toolTipAssign = "Crear doc. entrega MP";
                toolTipReturn = "Crear doc. devolución MP";
                imageAssign = new ImageIcon(getClass().getResource("/erp/img/icon_std_mfg_rm_asd.gif"));
                imageReturn = new ImageIcon(getClass().getResource("/erp/img/icon_std_mfg_rm_ret.gif"));
                manIogTypeKey = mnTabTypeAux02 == SDataConstants.TRNX_DIOG_MFG_MOVE_ASG ? SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_RM_ASD : SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_RM_RET;
                break;
            case SDataConstants.TRNX_DIOG_MFG_WP:
                createButtons = true;
                levelRightAssign = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_MFG_WP_ASG).Level;
                levelRightReturn = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_MFG_WP_DEV).Level;
                hasRightAssign = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_MFG_WP_ASG).HasRight;
                hasRightReturn = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_MFG_WP_DEV).HasRight;
                toolTipAssign = "Crear doc. entrega PP";
                toolTipReturn = "Crear doc. devolución PP";
                imageAssign = new ImageIcon(getClass().getResource("/erp/img/icon_std_mfg_wp_asd.gif"));
                imageReturn = new ImageIcon(getClass().getResource("/erp/img/icon_std_mfg_wp_ret.gif"));
                manIogTypeKey = mnTabTypeAux02 == SDataConstants.TRNX_DIOG_MFG_MOVE_ASG ? SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_ASD : SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_WP_RET;
                break;
            case SDataConstants.TRNX_DIOG_MFG_FG:
                createButtons = true;
                levelRightAssign = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_MFG_FG_ASG).Level;
                levelRightReturn = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_MFG_FG_DEV).Level;
                hasRightAssign = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_MFG_FG_ASG).HasRight;
                hasRightReturn = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_MFG_FG_DEV).HasRight;
                toolTipAssign = "Crear doc. entrega PT";
                toolTipReturn = "Crear doc. devolución PT";
                imageAssign = new ImageIcon(getClass().getResource("/erp/img/icon_std_mfg_fg_asd.gif"));
                imageReturn = new ImageIcon(getClass().getResource("/erp/img/icon_std_mfg_fg_ret.gif"));
                manIogTypeKey = mnTabTypeAux02 == SDataConstants.TRNX_DIOG_MFG_MOVE_ASG ? SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_FG_ASD : SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_FG_RET;
                break;
            case SDataConstants.TRNX_DIOG_MFG_CON:
                manIogTypeKey = mnTabTypeAux02 == SDataConstants.TRNX_DIOG_MFG_MOVE_IN ? SDataConstantsSys.TRNS_TP_IOG_IN_MFG_CON : SDataConstantsSys.TRNS_TP_IOG_OUT_MFG_CON;
                break;
            default:
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
        }

        if (createButtons) {
            addTaskBarUpperSeparator();

            mjbAssign = new JButton(imageAssign);
            mjbAssign.setPreferredSize(new Dimension(23, 23));
            mjbAssign.setToolTipText(toolTipAssign);
            mjbAssign.addActionListener(this);
            addTaskBarUpperComponent(mjbAssign);

            mjbReturn = new JButton(imageReturn);
            mjbReturn.setPreferredSize(new Dimension(23, 23));
            mjbReturn.setToolTipText(toolTipReturn);
            mjbReturn.addActionListener(this);
            addTaskBarUpperComponent(mjbReturn);
        }

        addTaskBarUpperSeparator();

        mjbViewNotes = new JButton(miClient.getImageIcon(SLibConstants.ICON_NOTES));
        mjbViewNotes.setPreferredSize(new Dimension(23, 23));
        mjbViewNotes.addActionListener(this);
        mjbViewNotes.setToolTipText("Ver notas del documento");
        addTaskBarUpperComponent(mjbViewNotes);

        jbPrint = new JButton(miClient.getImageIcon(SLibConstants.ICON_PRINT));
        jbPrint.setPreferredSize(new Dimension(23, 23));
        jbPrint.addActionListener(this);
        jbPrint.setToolTipText("Imprimir documento");
        addTaskBarUpperComponent(jbPrint);

        jbNew.setEnabled(false);
        jbEdit.setEnabled(levelRight >= SUtilConsts.LEV_AUTHOR);
        jbDelete.setEnabled(levelRight == SUtilConsts.LEV_MANAGER);
        mjbViewNotes.setEnabled(true);
        jbPrint.setEnabled(true);

        if (createButtons) {
            mjbAssign.setEnabled(mnTabTypeAux02 == SDataConstants.TRNX_DIOG_MFG_MOVE_ASG && hasRightAssign && levelRightAssign >= SUtilConsts.LEV_AUTHOR);
            mjbReturn.setEnabled(mnTabTypeAux02 == SDataConstants.TRNX_DIOG_MFG_MOVE_RET && hasRightReturn && levelRightReturn >= SUtilConsts.LEV_AUTHOR);
        }

        STableField[] aoKeyFields = new STableField[2];
        STableColumn[] aoTableColumns = new STableColumn[28];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "iog.id_year");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "iog.id_doc");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "iog.dt", "Fecha doc.", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_num", "Folio doc.", STableConstants.WIDTH_DOC_NUM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tp_iog.code", "Código tipo doc.", STableConstants.WIDTH_CODE_DOC);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tp_iog.tp_iog", "Tipo doc.", 125);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb.code", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ent.code", "Almacén", STableConstants.WIDTH_CODE_COB_ENT);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "iog.val_r", "Valor $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_num_cp", "Folio doc. complemento", STableConstants.WIDTH_DOC_NUM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tp_iog_cp.code", "Código tipo doc. complemento", STableConstants.WIDTH_CODE_DOC);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tp_iog_cp.tp_iog", "Tipo doc. complemento", 125);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb_cp.code", "Sucursal empresa complemento", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ent_cp.code", "Almacén complemento", STableConstants.WIDTH_CODE_COB_ENT);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_mfg", "Ord. prod.", STableConstants.WIDTH_DOC_NUM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_rec", "Folio póliza", STableConstants.WIDTH_RECORD_NUM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "iog.b_audit", "Auditado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ua.usr", "Usr. auditoría", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "iog.ts_audit", "Auditoría", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "iog.b_authorn", "Autorizado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ut.usr", "Usr. autorización", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "iog.ts_authorn", "Autorización", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "iog.b_sys", "Sistema", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "iog.b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un.usr", "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "iog.ts_new", "Creación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ue.usr", "Usr. modificación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "iog.ts_edit", "Modificación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ud.usr", "Usr. eliminación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "iog.ts_del", "Eliminación", STableConstants.WIDTH_DATE_TIME);

        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        SFormUtilities.createActionMap(this, this, "publicActionPrint", "print", KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK);

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.MFG_ORD);
        mvSuscriptors.add(SDataConstants.TRN_DIOG);
        mvSuscriptors.add(SDataConstants.TRNX_DPS_SUPPLY_PEND);
        mvSuscriptors.add(SDataConstants.TRNX_DPS_SUPPLY_PEND_ETY);
        mvSuscriptors.add(SDataConstants.TRNX_DPS_RETURN_PEND);
        mvSuscriptors.add(SDataConstants.TRNX_DPS_RETURN_PEND_ETY);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        populateTable();
    }

    private void actionMove() {
        SGuiModule module = miClient.getGuiModule(SDataConstants.MOD_INV);

        module.setFormComplement(new STrnDiogComplement(manIogTypeKey));
        if (module.showForm(SDataConstants.TRN_DIOG, null) == SLibConstants.DB_ACTION_SAVE_OK) {
            module.refreshCatalogues(SDataConstants.TRN_DIOG);
        }
    }

    private void actionViewNotes() {
        if (mjbViewNotes.isEnabled()) {
            SModuleUtilities.showDocumentNotes(miClient, SDataConstants.TRN_DIOG, moTablePane.getSelectedTableRow());
        }
    }

    private void actionPrint() {
        if (jbPrint.isEnabled()) {
             if (moTablePane.getSelectedTableRow() != null) {
                 STrnUtilities.printDiog(miClient, (int[]) moTablePane.getSelectedTableRow().getPrimaryKey());
             }
        }
    }

    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {
            if (miClient.getGuiModule(SDataConstants.MOD_INV).showForm(SDataConstants.TRN_DIOG, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                miClient.getGuiModule(SDataConstants.MOD_INV).refreshCatalogues(mnTabType);
            }
        }
    }

    @Override
    public void actionEdit() {
        if (jbEdit.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                if (miClient.getGuiModule(SDataConstants.MOD_INV).showForm(SDataConstants.TRN_DIOG, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                    miClient.getGuiModule(SDataConstants.MOD_INV).refreshCatalogues(SDataConstants.TRN_DIOG);
                }
            }
        }
    }

    @Override
    public void actionDelete() {
       if (jbDelete.isEnabled()) {
           if (moTablePane.getSelectedTableRow() != null) {
                if (miClient.showMsgBoxConfirm(SLibConstants.MSG_CNF_REG_DELETE) == JOptionPane.YES_OPTION) {
                    if (miClient.getGuiModule(SDataConstants.MOD_INV).deleteRegistry(SDataConstants.TRN_DIOG, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_DELETE_OK) {
                        miClient.getGuiModule(SDataConstants.MOD_INV).refreshCatalogues(SDataConstants.TRN_DIOG);
                    }
                }
            }
        }
    }

    public void publicActionPrint() {
        actionPrint();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void createSqlQuery() {
        int[] key = null;
        String sqlWhere = "";
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_DELETED && setting.getStatus() == STableConstants.STATUS_ON) {
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "iog.b_del = 0 ";
            }
            else if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + SDataSqlUtilities.composePeriodFilter((int[]) setting.getSetting(), "iog.dt");
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_COB_ENT) {
                key = (int[]) setting.getSetting();
                if (key != null) {
                    if (key[0] != SLibConstants.UNDEFINED) {
                        sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "iog.fid_cob = " + key[0] + " ";
                    }
                    if (key[1] != SLibConstants.UNDEFINED) {
                        sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "iog.fid_wh = " + key[1] + " ";
                    }
                }
            }
        }

        sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "iog.fid_ct_iog = " + manIogTypeKey[0] + " AND iog.fid_cl_iog = " + manIogTypeKey[1] + " AND iog.fid_tp_iog = " + manIogTypeKey[2] + " ";

        msSql = "SELECT iog.id_year, iog.id_doc, iog.dt, iog.val_r, iog.b_audit, iog.b_authorn, iog.b_sys, iog.b_del, " +
                "CONCAT(iog.num_ser, IF(LENGTH(iog.num_ser) = 0, '', '-'), erp.lib_fix_int(iog.num, " + SDataConstantsSys.NUM_LEN_IOG + ")) AS f_num, " +
                "tp_iog.tp_iog, tp_iog.code, bpb.code, ent.code, " +
                "CONCAT(iog_cp.num_ser, IF(LENGTH(iog_cp.num_ser) = 0, '', '-'), erp.lib_fix_int(iog_cp.num, " + SDataConstantsSys.NUM_LEN_IOG + ")) AS f_num_cp, " +
                "tp_iog_cp.tp_iog, tp_iog_cp.code, bpb_cp.code, ent_cp.code, " +
                "CASE WHEN iog.fid_mfg_year_n IS NULL THEN '' ELSE CONCAT(mo.id_year, '-', mo.num) END as f_mfg, " +
                "CONCAT(r.fid_rec_tp_rec, '-', erp.lib_fix_int(r.fid_rec_num, " + SDataConstantsSys.NUM_LEN_FIN_REC + ")) as f_rec, " +
                "iog.ts_audit, iog.ts_authorn, iog.ts_new, iog.ts_edit, iog.ts_del, ut.usr, ua.usr, un.usr, ue.usr, ud.usr " +
                "FROM trn_diog AS iog " +
                "INNER JOIN erp.trns_ct_iog AS ct_iog ON iog.fid_ct_iog = ct_iog.id_ct_iog " +
                "INNER JOIN erp.trns_cl_iog AS cl_iog ON iog.fid_cl_iog = cl_iog.id_cl_iog AND iog.fid_ct_iog = cl_iog.id_ct_iog " +
                "INNER JOIN erp.trns_tp_iog AS tp_iog ON iog.fid_tp_iog = tp_iog.id_tp_iog AND iog.fid_ct_iog = tp_iog.id_ct_iog AND iog.fid_cl_iog = tp_iog.id_cl_iog " +
                "INNER JOIN erp.bpsu_bpb AS bpb ON iog.fid_cob = bpb.id_bpb " +
                "INNER JOIN erp.cfgu_cob_ent AS ent ON iog.fid_cob = ent.id_cob AND iog.fid_wh = ent.id_ent " +
                "INNER JOIN erp.usru_usr AS ua ON iog.fid_usr_audit = ua.id_usr " +
                "INNER JOIN erp.usru_usr AS ut ON iog.fid_usr_authorn = ut.id_usr " +
                "INNER JOIN erp.usru_usr AS un ON iog.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON iog.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON iog.fid_usr_del = ud.id_usr " +
                "LEFT OUTER JOIN trn_diog AS iog_cp ON iog.fid_diog_year_n = iog_cp.id_year AND iog.fid_diog_doc_n = iog_cp.id_doc " +
                "LEFT OUTER JOIN erp.trns_tp_iog AS tp_iog_cp ON iog_cp.fid_tp_iog = tp_iog_cp.id_tp_iog AND iog_cp.fid_ct_iog = tp_iog_cp.id_ct_iog AND iog_cp.fid_cl_iog = tp_iog_cp.id_cl_iog " +
                "LEFT OUTER JOIN erp.bpsu_bpb AS bpb_cp ON iog_cp.fid_cob = bpb_cp.id_bpb " +
                "LEFT OUTER JOIN erp.cfgu_cob_ent AS ent_cp ON iog_cp.fid_cob = ent_cp.id_cob AND iog_cp.fid_wh = ent_cp.id_ent " +
                "LEFT OUTER JOIN mfg_ord AS mo ON iog.fid_mfg_year_n = mo.id_year AND iog.fid_mfg_ord_n = mo.id_ord " +
                "LEFT OUTER JOIN trn_diog_rec AS r ON iog.id_year = r.id_iog_year AND iog.id_doc = r.id_iog_doc " +
                (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere) +
                "ORDER BY iog.dt, f_num, iog.id_year, iog.id_doc ";
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (javax.swing.JButton) e.getSource();

            if (button == mjbAssign) {
                actionMove();
            }
            else if (button == mjbReturn) {
                actionMove();
            }
            else if (button == mjbViewNotes) {
                actionViewNotes();
            }
            else if (button == jbPrint) {
                actionPrint();
            }
        }
    }
}
