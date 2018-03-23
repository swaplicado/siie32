/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.gui.SModuleUtilities;
import erp.lib.SLibConstants;
import erp.lib.data.SDataSqlUtilities;
import erp.lib.form.SFormUtilities;
import erp.lib.table.STabFilterDatePeriod;
import erp.lib.table.STabFilterDeleted;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import erp.mod.SModSysConsts;
import erp.mtrn.data.SDataDiog;
import erp.mtrn.data.STrnDiogComplement;
import erp.mtrn.data.STrnUtilities;
import erp.table.SFilterConstants;
import erp.table.STabFilterCompanyBranchEntity;
import erp.table.STabFilterDocumentType;
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
public class SViewDiogMaintMovement extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private javax.swing.JButton jbAdjIn1;
    private javax.swing.JButton jbAdjIn2;
    private javax.swing.JButton jbIntInTra;
    private javax.swing.JButton jbAdjOut1;
    private javax.swing.JButton jbAdjOut2;
    private javax.swing.JButton jbIntOutTra;
    private javax.swing.JButton mjbViewNotes;
    private javax.swing.JButton jbPrint;
    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;
    private erp.lib.table.STabFilterDatePeriod moTabFilterDatePeriod;
    private erp.table.STabFilterCompanyBranchEntity moTabFilterCompanyBranchEntity;
    private erp.table.STabFilterDocumentType moTabFilterTypeDocument;

    /**
     * Creates view for stock-movements related to maintenance.
     * @param client GUI client.
     * @param tabTitle GUI-tab title.
     * @param auxType01 Use case of view: SModSysConsts.TRNX_MAINT_...
     */
    public SViewDiogMaintMovement(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01) {
        super(client, tabTitle, SDataConstants.TRNX_DIOG_MAINT, auxType01);
        initComponents();
    }

    private void initComponents() {
        int i = 0;
        int levelRightInOtherInt = SDataConstantsSys.UNDEFINED;
        int levelRightOutOtherInt = SDataConstantsSys.UNDEFINED;

        moTabFilterDeleted = new STabFilterDeleted(this);
        moTabFilterDatePeriod = new STabFilterDatePeriod(miClient, this, SLibConstants.GUI_DATE_AS_YEAR_MONTH);
        moTabFilterCompanyBranchEntity = new STabFilterCompanyBranchEntity(miClient, this, SDataConstantsSys.CFGS_CT_ENT_WH);
        moTabFilterTypeDocument = new STabFilterDocumentType(miClient, this, SDataConstants.TRNS_TP_IOG);

        /* XXX
        if (mnTabTypeAux01 != SLibConstants.UNDEFINED && mnTabTypeAux02 != SLibConstants.UNDEFINED) {
            moTabFilterTypeDocument.setFixedDocumentType(new int[] { mnTabTypeAux01, mnTabTypeAux02, 1 });
        }
        */

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDatePeriod);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterCompanyBranchEntity);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterTypeDocument);

        addTaskBarUpperSeparator();
        
        String[][] toolTipTexts = null;
        
        switch (mnTabTypeAux01) {
            case SModSysConsts.TRNX_MAINT_PART:
                toolTipTexts = new String[][] {
                    new String[] {
                        "Consumo refacción c/empleado",
                        "Consumo refacción c/contratista"
                    },
                    new String[] {
                        "Recuperación refacción c/empleado",
                        "Recuperación refacción c/contratista"
                    },
                };
                break;
            case SModSysConsts.TRNX_MAINT_TOOL_AV:
                break;
            case SModSysConsts.TRNX_MAINT_TOOL_LENT:
                break;
            case SModSysConsts.TRNX_MAINT_TOOL_MAINT:
                break;
            case SModSysConsts.TRNX_MAINT_TOOL_LOST:
                break;
            default:
        }

        jbAdjOut1 = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_stk_adj_out.gif")));    // XXX change button icon!
        jbAdjOut1.setPreferredSize(new Dimension(23, 23));
        jbAdjOut1.setToolTipText(toolTipTexts[0][0]);
        jbAdjOut1.addActionListener(this);
        addTaskBarUpperComponent(jbAdjOut1);

        jbAdjOut2 = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_stk_inv_out.gif")));    // XXX change button icon!
        jbAdjOut2.setPreferredSize(new Dimension(23, 23));
        jbAdjOut2.setToolTipText(toolTipTexts[0][1]);
        jbAdjOut2.addActionListener(this);
        addTaskBarUpperComponent(jbAdjOut2);

        /* XXX
        jbIntOutTra = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_stk_tra_out.gif")));
        jbIntOutTra.setPreferredSize(new Dimension(23, 23));
        jbIntOutTra.setToolTipText("Salida traspaso");
        jbIntOutTra.addActionListener(this);
        addTaskBarUpperComponent(jbIntOutTra);
        */
        
        addTaskBarUpperSeparator();

        jbAdjIn1 = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_stk_adj_in.gif")));  // XXX change button icon!
        jbAdjIn1.setPreferredSize(new Dimension(23, 23));
        jbAdjIn1.setToolTipText(toolTipTexts[1][0]);
        jbAdjIn1.addActionListener(this);
        addTaskBarUpperComponent(jbAdjIn1);

        jbAdjIn2 = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_stk_inv_in.gif")));  // XXX change button icon!
        jbAdjIn2.setPreferredSize(new Dimension(23, 23));
        jbAdjIn2.setToolTipText(toolTipTexts[1][1]);
        jbAdjIn2.addActionListener(this);
        addTaskBarUpperComponent(jbAdjIn2);

        /* XXX
        jbIntInTra = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_stk_tra_in.gif")));
        jbIntInTra.setPreferredSize(new Dimension(23, 23));
        jbIntInTra.setToolTipText("Entrada traspaso");
        jbIntInTra.addActionListener(this);
        addTaskBarUpperComponent(jbIntInTra);
        */
        
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

        levelRightInOtherInt = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_IN_INT).Level;
        levelRightOutOtherInt = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_OUT_INT).Level;

        jbNew.setEnabled(false);
        jbEdit.setEnabled(levelRightInOtherInt >= SUtilConsts.LEV_AUTHOR || levelRightOutOtherInt >=  SUtilConsts.LEV_AUTHOR);
        jbDelete.setEnabled(levelRightInOtherInt >= SUtilConsts.LEV_AUTHOR || levelRightOutOtherInt >=  SUtilConsts.LEV_AUTHOR);
        jbAdjIn1.setEnabled(levelRightInOtherInt >= SUtilConsts.LEV_AUTHOR);
        jbAdjIn2.setEnabled(levelRightInOtherInt >= SUtilConsts.LEV_AUTHOR);
        // XXX jbIntInTra.setEnabled(false);   // this stock move is only a consequence of its counterpart
        jbAdjOut1.setEnabled(levelRightOutOtherInt >= SUtilConsts.LEV_AUTHOR);
        jbAdjOut2.setEnabled(levelRightOutOtherInt >= SUtilConsts.LEV_AUTHOR);
        // XXX jbIntOutTra.setEnabled(levelRightOutOtherInt >= SUtilConsts.LEV_AUTHOR);
        mjbViewNotes.setEnabled(true);
        jbPrint.setEnabled(true);

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
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_rec", "Folio póliza", STableConstants.WIDTH_RECORD_NUM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "iog.ref", "Referencia", STableConstants.WIDTH_RECORD_NUM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "iog.b_audit", "Auditado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "uaud.usr", "Usr. auditoría", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "iog.ts_audit", "Auditoría", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "iog.b_authorn", "Autorizado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "uaut.usr", "Usr. autorización", STableConstants.WIDTH_USER);
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

        SFormUtilities.createActionMap(this, this, "actionPrint", "print", KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK);

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        populateTable();
    }

    private void actionMove(final int maintMovementType, final int maintUserType) {
        STrnDiogComplement complement = new STrnDiogComplement();
        complement.setMaintMovementType(maintMovementType);
        complement.setMaintUserType(maintUserType);
        
        miClient.getGuiModule(SDataConstants.MOD_INV).setFormComplement(complement);
        if (miClient.getGuiModule(SDataConstants.MOD_INV).showForm(mnTabType, null) == SLibConstants.DB_ACTION_SAVE_OK) {
            miClient.getGuiModule(SDataConstants.MOD_INV).refreshCatalogues(mnTabType);
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void actionEdit() {
        if (jbEdit.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                if (miClient.getGuiModule(SDataConstants.MOD_INV).showForm(mnTabType, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                    miClient.getGuiModule(SDataConstants.MOD_INV).refreshCatalogues(mnTabType);
                }
            }
        }
    }

    @Override
    public void actionDelete() {
       if (jbDelete.isEnabled()) {
           if (moTablePane.getSelectedTableRow() != null) {
                if (miClient.showMsgBoxConfirm(SLibConstants.MSG_CNF_REG_DELETE) == JOptionPane.YES_OPTION) {
                    SDataDiog diog = (SDataDiog) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DIOG, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_VERBOSE);

                    if (!SDataUtilities.isPeriodOpen(miClient, diog.getDate())) {
                        miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_PER_CLOSE);
                    }
                    else {
                        if (miClient.getGuiModule(SDataConstants.MOD_INV).deleteRegistry(mnTabType, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_DELETE_OK) {
                            miClient.getGuiModule(SDataConstants.MOD_INV).refreshCatalogues(mnTabType);
                        }
                    }
                }
            }
        }
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
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_DOC_TP) {
                key = (int[]) setting.getSetting();
                if (key != null) {
                    sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "iog.fid_ct_iog = " + key[0] + " AND iog.fid_cl_iog = " + key[1] + " AND iog.fid_tp_iog = " + key[2] + " ";
                }
            }
        }

        switch (mnTabTypeAux01) {
            case SModSysConsts.TRNX_MAINT_PART:
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "iog.fid_maint_mov_tp IN (" + SModSysConsts.TRNS_TP_MAINT_MOV_IN_CONS_PART + ", " + SModSysConsts.TRNS_TP_MAINT_MOV_OUT_CONS_PART + ") ";
                break;
            case SModSysConsts.TRNX_MAINT_TOOL_AV:
                break;
            case SModSysConsts.TRNX_MAINT_TOOL_LENT:
                break;
            case SModSysConsts.TRNX_MAINT_TOOL_MAINT:
                break;
            case SModSysConsts.TRNX_MAINT_TOOL_LOST:
                break;
            default:
        }
        
        msSql = "SELECT iog.id_year, iog.id_doc, iog.dt, iog.val_r, iog.ref, iog.b_audit, iog.b_authorn, iog.b_sys, iog.b_del, " +
                "CONCAT(iog.num_ser, IF(LENGTH(iog.num_ser) = 0, '', '-'), erp.lib_fix_int(iog.num, " + SDataConstantsSys.NUM_LEN_IOG + ")) AS f_num, " +
                "tp_iog.tp_iog, tp_iog.code, bpb.code, ent.code, " +
                "CONCAT(iog_cp.num_ser, IF(LENGTH(iog_cp.num_ser) = 0, '', '-'), erp.lib_fix_int(iog_cp.num, " + SDataConstantsSys.NUM_LEN_IOG + ")) AS f_num_cp, " +
                "tp_iog_cp.tp_iog, tp_iog_cp.code, bpb_cp.code, ent_cp.code, " +
                "CONCAT(r.fid_rec_tp_rec, '-', erp.lib_fix_int(r.fid_rec_num, " + SDataConstantsSys.NUM_LEN_FIN_REC + ")) as f_rec, " +
                "iog.ts_audit, iog.ts_authorn, iog.ts_new, iog.ts_edit, iog.ts_del, uaud.usr, uaut.usr, un.usr, ue.usr, ud.usr " +
                "FROM trn_diog AS iog " +
                "INNER JOIN erp.trns_ct_iog AS ct_iog ON iog.fid_ct_iog = ct_iog.id_ct_iog " +
                "INNER JOIN erp.trns_cl_iog AS cl_iog ON iog.fid_cl_iog = cl_iog.id_cl_iog AND iog.fid_ct_iog = cl_iog.id_ct_iog " +
                "INNER JOIN erp.trns_tp_iog AS tp_iog ON iog.fid_tp_iog = tp_iog.id_tp_iog AND iog.fid_ct_iog = tp_iog.id_ct_iog AND iog.fid_cl_iog = tp_iog.id_cl_iog " +
                "INNER JOIN erp.bpsu_bpb AS bpb ON iog.fid_cob = bpb.id_bpb " +
                "INNER JOIN erp.cfgu_cob_ent AS ent ON iog.fid_cob = ent.id_cob AND iog.fid_wh = ent.id_ent " +
                "INNER JOIN erp.usru_usr AS uaud ON iog.fid_usr_audit = uaud.id_usr " +
                "INNER JOIN erp.usru_usr AS uaut ON iog.fid_usr_authorn = uaut.id_usr " +
                "INNER JOIN erp.usru_usr AS un ON iog.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON iog.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON iog.fid_usr_del = ud.id_usr " +
                "LEFT OUTER JOIN trn_diog AS iog_cp ON iog.fid_diog_year_n = iog_cp.id_year AND iog.fid_diog_doc_n = iog_cp.id_doc " +
                "LEFT OUTER JOIN erp.trns_tp_iog AS tp_iog_cp ON iog_cp.fid_tp_iog = tp_iog_cp.id_tp_iog AND iog_cp.fid_ct_iog = tp_iog_cp.id_ct_iog AND iog_cp.fid_cl_iog = tp_iog_cp.id_cl_iog " +
                "LEFT OUTER JOIN erp.bpsu_bpb AS bpb_cp ON iog_cp.fid_cob = bpb_cp.id_bpb " +
                "LEFT OUTER JOIN erp.cfgu_cob_ent AS ent_cp ON iog_cp.fid_cob = ent_cp.id_cob AND iog_cp.fid_wh = ent_cp.id_ent " +
                "LEFT OUTER JOIN trn_diog_rec AS r ON iog.id_year = r.id_iog_year AND iog.id_doc = r.id_iog_doc " +
                (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere) +
                "ORDER BY iog.dt, f_num, iog.id_year, iog.id_doc ";
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (javax.swing.JButton) e.getSource();

            if (button == jbAdjOut1) {
                actionMove(SModSysConsts.TRNS_TP_MAINT_MOV_OUT_CONS_PART, SModSysConsts.TRNX_TP_MAINT_USER_EMPLOYEE);   // XXX
            }
            else if (button == jbAdjOut2) {
                actionMove(SModSysConsts.TRNS_TP_MAINT_MOV_OUT_CONS_PART, SModSysConsts.TRNX_TP_MAINT_USER_CONTRACTOR); // XXX
            }
            /* XXX
            else if (button == jbIntInTra) {
                actionMove(SDataConstantsSys.TRNS_TP_IOG_IN_INT_TRA);
            }
            */
            else if (button == jbAdjIn1) {
                actionMove(SModSysConsts.TRNS_TP_MAINT_MOV_IN_CONS_PART, SModSysConsts.TRNX_TP_MAINT_USER_EMPLOYEE);    // XXX
            }
            else if (button == jbAdjIn2) {
                actionMove(SModSysConsts.TRNS_TP_MAINT_MOV_IN_CONS_PART, SModSysConsts.TRNX_TP_MAINT_USER_CONTRACTOR);  // XXX
            }
            /* XXX
            else if (button == jbIntOutTra) {
                actionMove(SDataConstantsSys.TRNS_TP_IOG_OUT_INT_TRA);
            }
            */
            else if (button == mjbViewNotes) {
                actionViewNotes();
            }
            else if (button == jbPrint) {
                actionPrint();
            }
        }
    }
}
