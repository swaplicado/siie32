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
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;

/**
 *
 * @author Sergio Flores, Claudio Peña
 */
public class SViewMaintDiog extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private javax.swing.JButton jbAdjIn1;
    private javax.swing.JButton jbAdjIn2;
    private javax.swing.JButton jbAdjOut1;
    private javax.swing.JButton jbAdjOut2;
    private javax.swing.JButton jbViewNotes;
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
    public SViewMaintDiog(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01) {
        this(client, tabTitle, auxType01, SUtilConsts.PER_DOC);
    }

    /**
     * Creates view for stock-movements related to maintenance.
     * @param client GUI client.
     * @param tabTitle GUI-tab title.
     * @param auxType01 Use case of view: SModSysConsts.TRNX_MAINT_...
     * @param auxType02 Grouping level: by document (SUtilConsts.PER_DOC) or by item (SUtilConsts.PER_ITEM)
     */
    public SViewMaintDiog(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01, int auxType02) {
        super(client, tabTitle, SDataConstants.TRNX_MAINT_DIOG, auxType01, auxType02);
        initComponents();
    }

    private void initComponents() {
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
        String[][] icons = null;
        
        switch (mnTabTypeAux01) {
            case SModSysConsts.TRNX_MAINT_PART:
                toolTipTexts = new String[][] {
                    new String[] {
                        "Consumo refacción empleado",
                        "Consumo refacción contratista"
                    },
                    new String[] {
                        "Recuperación refacción empleado",
                        "Recuperación refacción contratista"
                    },
                };
                icons = new String[][] {
                    new String[] {
                        "icon_std_stk_adj_out",
                        "icon_std_stk_inv_out"
                    },
                    new String[] {
                        "icon_std_stk_adj_in",
                        "icon_std_stk_inv_in"
                    },
                };
                break;
                
            case SModSysConsts.TRNX_MAINT_TOOL:
                toolTipTexts = new String[][] {
                    new String[] {
                        "Consumo herramienta empleado",
                        "Consumo herramienta contratista"
                    },
                    new String[] {
                        "Recuperación herramienta empleado",
                        "Recuperación herramienta contratista"
                    },
                };
                icons = new String[][] {
                    new String[] {
                        "icon_std_stk_adj_out",
                        "icon_std_stk_inv_out"
                    },
                    new String[] {
                        "icon_std_stk_adj_in",
                        "icon_std_stk_inv_in"
                    },
                };
                break;
                
            case SModSysConsts.TRNX_MAINT_TOOL_LENT:
                toolTipTexts = new String[][] {
                    new String[] {
                        "Préstamo herramienta empleado",
                        "Préstamo herramienta contratista"
                    },
                    new String[] {
                        "Devolución herramienta empleado",
                        "Devolución herramienta contratista"
                    },
                };
                icons = new String[][] {
                    new String[] {
                        "icon_std_stk_maint_lent_emp_out",
                        "icon_std_stk_maint_lent_cont_out"
                    },
                    new String[] {
                        "icon_std_stk_maint_lent_emp_in",
                        "icon_std_stk_maint_lent_cont_in"
                    },
                };
                break;
                
            case SModSysConsts.TRNX_MAINT_TOOL_MAINT:
                toolTipTexts = new String[][] {
                    new String[] {
                        "Mantenimiento herramienta"
                    },
                    new String[] {
                        "Devolución herramienta mantenimiento"
                    },
                };
                icons = new String[][] {
                    new String[] {
                        "icon_std_stk_maint_maint_out"
                    },
                    new String[] {
                        "icon_std_stk_maint_maint_in"
                    },
                };
                break;
                
            case SModSysConsts.TRNX_MAINT_TOOL_LOST:
                toolTipTexts = new String[][] {
                    new String[] {
                        "Extravío herramienta"
                    },
                    new String[] {
                        "Devolución herramienta extraviada"
                    },
                };
                icons = new String[][] {
                    new String[] {
                        "icon_std_stk_maint_lost_out"
                    },
                    new String[] {
                        "icon_std_stk_maint_lost_in"
                    },
                };
                break;
                
            default:
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
        }

        jbAdjOut1 = new JButton(new ImageIcon(getClass().getResource("/erp/img/" + icons[0][0] + ".gif")));
        jbAdjOut1.setPreferredSize(new Dimension(23, 23));
        jbAdjOut1.setToolTipText(toolTipTexts[0][0]);
        jbAdjOut1.addActionListener(this);
        addTaskBarUpperComponent(jbAdjOut1);

        if (isButton2Needed()) {
            jbAdjOut2 = new JButton(new ImageIcon(getClass().getResource("/erp/img/" + icons[0][1] + ".gif")));
            jbAdjOut2.setPreferredSize(new Dimension(23, 23));
            jbAdjOut2.setToolTipText(toolTipTexts[0][1]);
            jbAdjOut2.addActionListener(this);
            addTaskBarUpperComponent(jbAdjOut2);
        }

        addTaskBarUpperSeparator();

        jbAdjIn1 = new JButton(new ImageIcon(getClass().getResource("/erp/img/" + icons[1][0] + ".gif")));
        jbAdjIn1.setPreferredSize(new Dimension(23, 23));
        jbAdjIn1.setToolTipText(toolTipTexts[1][0]);
        jbAdjIn1.addActionListener(this);
        addTaskBarUpperComponent(jbAdjIn1);

        if (isButton2Needed()) {
            jbAdjIn2 = new JButton(new ImageIcon(getClass().getResource("/erp/img/" + icons[1][1] + ".gif")));
            jbAdjIn2.setPreferredSize(new Dimension(23, 23));
            jbAdjIn2.setToolTipText(toolTipTexts[1][1]);
            jbAdjIn2.addActionListener(this);
            addTaskBarUpperComponent(jbAdjIn2);
        }

        addTaskBarUpperSeparator();

        jbViewNotes = new JButton(miClient.getImageIcon(SLibConstants.ICON_NOTES));
        jbViewNotes.setPreferredSize(new Dimension(23, 23));
        jbViewNotes.addActionListener(this);
        jbViewNotes.setToolTipText("Ver notas del documento");
        addTaskBarUpperComponent(jbViewNotes);

        jbPrint = new JButton(miClient.getImageIcon(SLibConstants.ICON_PRINT));
        jbPrint.setPreferredSize(new Dimension(23, 23));
        jbPrint.addActionListener(this);
        jbPrint.setToolTipText("Imprimir documento");
        addTaskBarUpperComponent(jbPrint);

        int levelRightInOtherInt = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_IN_INT).Level;
        int levelRightOutOtherInt = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_OUT_INT).Level;

        jbNew.setEnabled(false);
        jbEdit.setEnabled(levelRightInOtherInt >= SUtilConsts.LEV_AUTHOR || levelRightOutOtherInt >=  SUtilConsts.LEV_AUTHOR);
        jbDelete.setEnabled(levelRightInOtherInt >= SUtilConsts.LEV_AUTHOR || levelRightOutOtherInt >=  SUtilConsts.LEV_AUTHOR);
        
        jbAdjIn1.setEnabled(levelRightInOtherInt >= SUtilConsts.LEV_AUTHOR);
        if (isButton2Needed()) {
            jbAdjIn2.setEnabled(levelRightInOtherInt >= SUtilConsts.LEV_AUTHOR);
        }
        jbAdjOut1.setEnabled(levelRightOutOtherInt >= SUtilConsts.LEV_AUTHOR);
        if (isButton2Needed()) {
            jbAdjOut2.setEnabled(levelRightOutOtherInt >= SUtilConsts.LEV_AUTHOR);
        }
        jbViewNotes.setEnabled(true);
        jbPrint.setEnabled(true);

        STableField[] aoKeyFields;
        STableColumn[] aoTableColumns;
        
        switch (mnTabTypeAux02) {
            case SUtilConsts.PER_DOC:
                aoKeyFields = new STableField[2];
                aoTableColumns = new STableColumn[29];
                break;
            case SUtilConsts.PER_ITM:
                aoKeyFields = new STableField[3];
                aoTableColumns = new STableColumn[29];
                break;
            default:
                aoKeyFields = null;
                aoTableColumns = null;
        }

        int field = 0;
        switch (mnTabTypeAux02) {
            case SUtilConsts.PER_DOC:
                aoKeyFields[field++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "iog.id_year");
                aoKeyFields[field++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "iog.id_doc");
                break;
            case SUtilConsts.PER_ITM:
                aoKeyFields[field++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "ioge.id_year");
                aoKeyFields[field++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "ioge.id_doc");
                aoKeyFields[field++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "ioge.id_ety");
                break;
            default:
                aoKeyFields = null;
                aoTableColumns = null;
        }
        
        for (field = 0; field < aoKeyFields.length; field++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[field]); 
        }

        int col = 0;
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "iog.dt", "Fecha doc.", STableConstants.WIDTH_DATE);
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tp_iog.tp_iog", "Tipo doc.", 125);
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "maint.bp", "Responsable", 200);
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "_signed", "Firmado", STableConstants.WIDTH_BOOLEAN);
        if (mnTabTypeAux02 == SUtilConsts.PER_DOC) {
            aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "iog.val_r", "Total $", STableConstants.WIDTH_VALUE_2X);
        }
        if (mnTabTypeAux02 == SUtilConsts.PER_ITM) {
            aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "iogn.nts", "Comentarios", 150);
            aoTableColumns[col] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "ioge.qty", "Cantidad", STableConstants.WIDTH_QUANTITY);
            aoTableColumns[col++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
            aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item_key", "Clave ítem", STableConstants.WIDTH_ITEM);
            aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.name", "Ítem", 250);
            aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "maint.name", "Área mantenimiento", 150);
            aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "unit.symbol", "Unitad", STableConstants.WIDTH_UNIT_SYMBOL);
            aoTableColumns[col] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "_price", "Precio unitario $", STableConstants.WIDTH_VALUE_UNITARY);
            aoTableColumns[col++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererValueUnitary());
            aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "_value", "Importe $", STableConstants.WIDTH_VALUE_2X);
        }
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "supv.name", "Residente", 200);
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb.code", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ent.code", "Almacén", STableConstants.WIDTH_CODE_COB_ENT);
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_num", "Folio doc.", STableConstants.WIDTH_DOC_NUM);
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tp_iog.code", "Código tipo doc.", STableConstants.WIDTH_CODE_DOC);
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_num_cp", "Folio doc. complemento", STableConstants.WIDTH_DOC_NUM);
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tp_iog_cp.code", "Código tipo doc. complemento", STableConstants.WIDTH_CODE_DOC);
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tp_iog_cp.tp_iog", "Tipo doc. complemento", 125);
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb_cp.code", "Sucursal empresa complemento", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ent_cp.code", "Almacén complemento", STableConstants.WIDTH_CODE_COB_ENT);
        if (mnTabTypeAux02 == SUtilConsts.PER_DOC) {
            aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "iog.b_audit", "Auditado", STableConstants.WIDTH_BOOLEAN);
            aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "uaud.usr", "Usr. auditoría", STableConstants.WIDTH_USER);
            aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "iog.ts_audit", "Auditoría", STableConstants.WIDTH_DATE_TIME);
            aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "iog.b_authorn", "Autorizado", STableConstants.WIDTH_BOOLEAN);
            aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "uaut.usr", "Usr. autorización", STableConstants.WIDTH_USER);
            aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "iog.ts_authorn", "Autorización", STableConstants.WIDTH_DATE_TIME);
            aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "iog.b_sys", "Sistema", STableConstants.WIDTH_BOOLEAN);
        }
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, (mnTabTypeAux02 == SUtilConsts.PER_DOC ? "iog" : "ioge") + ".b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);      
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un.usr", "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "iog.ts_new", "Creación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ue.usr", "Usr. modificación", STableConstants.WIDTH_USER);
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "iog.ts_edit", "Modificación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ud.usr", "Usr. eliminación", STableConstants.WIDTH_USER);
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "iog.ts_del", "Eliminación", STableConstants.WIDTH_DATE_TIME);
        
        for (col = 0; col < aoTableColumns.length; col++) {
            moTablePane.addTableColumn(aoTableColumns[col]);
        }

        SFormUtilities.createActionMap(this, this, "actionPrint", "print", KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK);

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        populateTable();
    }
    
    private boolean isButton2Needed() {
        return !SLibUtils.belongsTo(mnTabTypeAux01, new int[] { SModSysConsts.TRNX_MAINT_TOOL_MAINT, SModSysConsts.TRNX_MAINT_TOOL_LOST });
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
        if (jbViewNotes.isEnabled()) {
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
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "NOT " + (mnTabTypeAux02 == SUtilConsts.PER_DOC ? "iog" : "ioge") + ".b_del ";
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
            case SModSysConsts.TRNX_MAINT_TOOL:
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "iog.fid_maint_mov_tp IN (" + SModSysConsts.TRNS_TP_MAINT_MOV_IN_CONS_TOOL + ", " + SModSysConsts.TRNS_TP_MAINT_MOV_OUT_CONS_TOOL + ") ";
                break;
            case SModSysConsts.TRNX_MAINT_TOOL_LENT:
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "iog.fid_maint_mov_tp IN (" + SModSysConsts.TRNS_TP_MAINT_MOV_IN_STAT_TOOL_LENT + ", " + SModSysConsts.TRNS_TP_MAINT_MOV_OUT_STAT_TOOL_LENT + ") ";
                break;
            case SModSysConsts.TRNX_MAINT_TOOL_MAINT:
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "iog.fid_maint_mov_tp IN (" + SModSysConsts.TRNS_TP_MAINT_MOV_IN_STAT_TOOL_MAINT + ", " + SModSysConsts.TRNS_TP_MAINT_MOV_OUT_STAT_TOOL_MAINT + ") ";
                break;
            case SModSysConsts.TRNX_MAINT_TOOL_LOST:
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "iog.fid_maint_mov_tp IN (" + SModSysConsts.TRNS_TP_MAINT_MOV_IN_STAT_TOOL_LOST + ", " + SModSysConsts.TRNS_TP_MAINT_MOV_OUT_STAT_TOOL_LOST + ") ";
                break;
            default:
        }
        
        switch (mnTabTypeAux02) {
            case SUtilConsts.PER_DOC:
                break;
            case SUtilConsts.PER_ITM:
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "NOT iog.b_del ";
                break;
            default:
        }
        
        Date date = moTabFilterDatePeriod.getDate();

        msSql = "SELECT " ;
                if (mnTabTypeAux02 == SUtilConsts.PER_DOC) {
                    msSql += "iog.id_year, iog.id_doc, iog.b_del, ";
                }
                else if (mnTabTypeAux02 == SUtilConsts.PER_ITM) {
                    msSql +=  "ioge.id_year, ioge.id_doc, ioge.id_ety, ioge.b_del, iogn.nts, " ;
                }
                msSql += "iog.dt, iog.val_r, iog.b_audit, iog.b_authorn, iog.b_sys, " +
                "CONCAT(iog.num_ser, IF(LENGTH(iog.num_ser) = 0, '', '-'), erp.lib_fix_int(iog.num, " + SDataConstantsSys.NUM_LEN_IOG + ")) AS f_num, " +
                "tp_iog.tp_iog, tp_iog.code, bpb.code, ent.code, " +
                "maint.bp, supv.name, ";
                if (mnTabTypeAux02 == SUtilConsts.PER_ITM) {
                   msSql +=  "ioge.qty, ioge.val_u, ioge.fid_item, ioge.fid_unit, ioge.fid_maint_area, i.item_key, i.name, unit.symbol, maint.name, " + 
                             "IF(iog.fid_ct_iog = " + SDataConstantsSys.TRNS_CT_IOG_IN + " , ioge.val_u, t._avg_prc) AS _price, " +
                             "IF(iog.fid_ct_iog = " + SDataConstantsSys.TRNS_CT_IOG_IN + " , ioge.val_u, t._avg_prc) * ioge.qty AS _value, ";
                }
                msSql += "CONCAT(iog_cp.num_ser, IF(LENGTH(iog_cp.num_ser) = 0, '', '-'), erp.lib_fix_int(iog_cp.num, " + SDataConstantsSys.NUM_LEN_IOG + ")) AS f_num_cp, " +
                "tp_iog_cp.tp_iog, tp_iog_cp.code, bpb_cp.code, ent_cp.code, " +
                "iog.ts_audit, iog.ts_authorn, iog.ts_new, iog.ts_edit, iog.ts_del, uaud.usr, uaut.usr, un.usr, ue.usr, ud.usr, " +
                "(SELECT COUNT(*) FROM trn_maint_diog_sig AS sig WHERE sig.fk_diog_year = iog.id_year AND sig.fk_diog_doc = iog.id_doc AND sig.ts_usr_ins >= iog.ts_edit) > 0 AS _signed " +
                "FROM trn_diog AS iog " +
                "INNER JOIN erp.trns_tp_iog AS tp_iog ON iog.fid_tp_iog = tp_iog.id_tp_iog AND iog.fid_ct_iog = tp_iog.id_ct_iog AND iog.fid_cl_iog = tp_iog.id_cl_iog " +
                "INNER JOIN erp.bpsu_bpb AS bpb ON iog.fid_cob = bpb.id_bpb " +
                "INNER JOIN erp.cfgu_cob_ent AS ent ON iog.fid_cob = ent.id_cob AND iog.fid_wh = ent.id_ent " +
                "INNER JOIN erp.usru_usr AS uaud ON iog.fid_usr_audit = uaud.id_usr " +
                "INNER JOIN erp.usru_usr AS uaut ON iog.fid_usr_authorn = uaut.id_usr " +
                "INNER JOIN trn_maint_user_supv AS supv ON iog.fid_maint_user_supv = supv.id_maint_user_supv ";
                if (mnTabTypeAux02 == SUtilConsts.PER_ITM) {
                   msSql +=  "INNER JOIN trn_diog_ety AS ioge ON ioge.id_year = iog.id_year AND ioge.id_doc = iog.id_doc " +
                           "INNER JOIN erp.itmu_item AS i ON i.id_item = ioge.fid_item " +
                           "INNER JOIN erp.itmu_unit AS unit ON unit.id_unit = ioge.fid_unit " +
                           "INNER JOIN trn_maint_area AS maint ON maint.id_maint_area = ioge.fid_maint_area " +
                           "LEFT JOIN trn_diog_nts AS iogn ON iogn.id_year = iog.id_year AND iogn.id_doc = iog.id_doc ";
                }
                msSql += "INNER JOIN erp.usru_usr AS un ON " + (mnTabTypeAux02 == SUtilConsts.PER_DOC ? "iog" : "ioge") + ".fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON " + (mnTabTypeAux02 == SUtilConsts.PER_DOC ? "iog" : "ioge") + ".fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON " + (mnTabTypeAux02 == SUtilConsts.PER_DOC ? "iog" : "ioge") + ".fid_usr_del = ud.id_usr ";
                if (mnTabTypeAux02 == SUtilConsts.PER_ITM) {
                    msSql += "LEFT OUTER JOIN " +
                            "(SELECT *, COALESCE(t._ext_dbt / t._ext_mov_in, 0.0) AS _avg_prc " +
                            "FROM (" +
                            "SELECT id_item, id_unit, id_cob, id_wh, " +
                            "SUM(mov_in) AS _mov_in, SUM(mov_out) AS _mov_out, SUM(mov_in - mov_out) AS _stk, " +
                            "SUM(debit) AS _dbt, SUM(credit) AS _cdt, SUM(debit - credit) AS _bal, " +
                            "SUM(IF(fid_ct_iog = " + SDataConstantsSys.TRNS_CL_IOG_IN_EXT[0] + " AND fid_cl_iog <= " + SDataConstantsSys.TRNS_CL_IOG_IN_EXT[1] + ", mov_in, 0)) AS _ext_mov_in, " +
                            "SUM(IF(fid_ct_iog = " + SDataConstantsSys.TRNS_CL_IOG_IN_EXT[0] + " AND fid_cl_iog <= " + SDataConstantsSys.TRNS_CL_IOG_IN_EXT[1] + ", debit, 0)) AS _ext_dbt " +
                            "FROM trn_stk " +
                            "WHERE id_year = " + SLibTimeUtils.digestYear(date)[0] + " AND dt < '" + SLibUtils.DbmsDateFormatDate.format(date) + "' AND NOT b_del " +
                            "GROUP BY id_item, id_unit, id_cob, id_wh " +
                            "ORDER BY id_item, id_unit, id_cob, id_wh) AS t) AS t ON " +
                            "t.id_item = ioge.fid_item AND t.id_unit = ioge.fid_unit AND t.id_cob = iog.fid_cob AND t.id_wh = iog.fid_wh " ;
                }
                msSql += "LEFT OUTER JOIN erp.bpsu_bp AS maint ON iog.fid_maint_user_n = maint.id_bp " +
                "LEFT OUTER JOIN trn_diog AS iog_cp ON iog.fid_diog_year_n = iog_cp.id_year AND iog.fid_diog_doc_n = iog_cp.id_doc " +
                "LEFT OUTER JOIN erp.trns_tp_iog AS tp_iog_cp ON iog_cp.fid_tp_iog = tp_iog_cp.id_tp_iog AND iog_cp.fid_ct_iog = tp_iog_cp.id_ct_iog AND iog_cp.fid_cl_iog = tp_iog_cp.id_cl_iog " +
                "LEFT OUTER JOIN erp.bpsu_bpb AS bpb_cp ON iog_cp.fid_cob = bpb_cp.id_bpb " +
                "LEFT OUTER JOIN erp.cfgu_cob_ent AS ent_cp ON iog_cp.fid_cob = ent_cp.id_cob AND iog_cp.fid_wh = ent_cp.id_ent " +
                (sqlWhere.isEmpty() ? "" : "WHERE " + sqlWhere) +
                "ORDER BY iog.dt, f_num, ";
                if (mnTabTypeAux02 == SUtilConsts.PER_DOC) {
                    msSql += "iog.id_year, iog.id_doc " ;
                }
                else if (mnTabTypeAux02 == SUtilConsts.PER_ITM) {
                    msSql +=  "ioge.id_year, ioge.id_doc, ioge.id_ety ";
                }
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (javax.swing.JButton) e.getSource();
            int[] movements = null;
            int[] users = null;

            switch (mnTabTypeAux01) {
                case SModSysConsts.TRNX_MAINT_PART:
                    movements = new int[] {
                        SModSysConsts.TRNS_TP_MAINT_MOV_OUT_CONS_PART,
                        SModSysConsts.TRNS_TP_MAINT_MOV_IN_CONS_PART
                    };
                    users = new int[] {
                        SModSysConsts.TRNX_TP_MAINT_USER_EMPLOYEE,
                        SModSysConsts.TRNX_TP_MAINT_USER_CONTRACTOR
                    };
                    break;
                    
                case SModSysConsts.TRNX_MAINT_TOOL:
                    movements = new int[] {
                        SModSysConsts.TRNS_TP_MAINT_MOV_OUT_CONS_TOOL,
                        SModSysConsts.TRNS_TP_MAINT_MOV_IN_CONS_TOOL
                    };
                    users = new int[] {
                        SModSysConsts.TRNX_TP_MAINT_USER_EMPLOYEE,
                        SModSysConsts.TRNX_TP_MAINT_USER_CONTRACTOR
                    };
                    break;
                    
                case SModSysConsts.TRNX_MAINT_TOOL_LENT:
                    movements = new int[] {
                        SModSysConsts.TRNS_TP_MAINT_MOV_OUT_STAT_TOOL_LENT,
                        SModSysConsts.TRNS_TP_MAINT_MOV_IN_STAT_TOOL_LENT
                    };
                    users = new int[] {
                        SModSysConsts.TRNX_TP_MAINT_USER_EMPLOYEE,
                        SModSysConsts.TRNX_TP_MAINT_USER_CONTRACTOR
                    };
                    break;
                    
                case SModSysConsts.TRNX_MAINT_TOOL_MAINT:
                    movements = new int[] {
                        SModSysConsts.TRNS_TP_MAINT_MOV_OUT_STAT_TOOL_MAINT,
                        SModSysConsts.TRNS_TP_MAINT_MOV_IN_STAT_TOOL_MAINT
                    };
                    users = new int[] {
                        SModSysConsts.TRNX_TP_MAINT_USER_TOOLS_MAINT_PROV
                    };
                    break;
                    
                case SModSysConsts.TRNX_MAINT_TOOL_LOST:
                    movements = new int[] {
                        SModSysConsts.TRNS_TP_MAINT_MOV_OUT_STAT_TOOL_LOST,
                        SModSysConsts.TRNS_TP_MAINT_MOV_IN_STAT_TOOL_LOST
                    };
                    users = new int[] {
                        SModSysConsts.TRNX_TP_MAINT_USER_EMPLOYEE
                    };
                    break;
                    
                default:
            }
            
            if (button == jbAdjOut1) {
                    actionMove(movements[0], users[0]);
            }
            else if (button == jbAdjOut2) {
                actionMove(movements[0], users[1]);
            }
            else if (button == jbAdjIn1) {
                actionMove(movements[1], users[0]);
            }
            else if (button == jbAdjIn2) {
                actionMove(movements[1], users[1]);
            }
            else if (button == jbViewNotes) {
                actionViewNotes();
            }
            else if (button == jbPrint) {
                actionPrint();
            }
        }
    }
}
