/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.data.SDataSqlUtilities;
import erp.lib.form.SFormUtilities;
import erp.lib.table.STabFilterDatePeriod;
import erp.lib.table.STabFilterDeleted;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mtrn.data.SDataDps;
import erp.mtrn.form.SDialogAnnulCfdi;
import erp.mtrn.form.SDialogDpsFinder;
import erp.table.SFilterConstants;
import erp.table.STabFilterDocumentNature;
import erp.table.STabFilterUsers;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import sa.gui.util.SUtilConsts;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiParams;

/**
 *
 * @author Sergio Flores
 *
 * BUSINESS PARTNER BLOCKING NOTES:
 * Business Partner Blocking applies only to order and document for purchases and sales,
 * aswell as printing them.
 * Estimates, contracts and credit notes are independent.
 */
public class SViewDpsCancelled extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private javax.swing.JButton jbAnnul;
    private javax.swing.JButton jbImport;
    private javax.swing.JButton jbCopy;
    private erp.table.STabFilterUsers moTabFilterUser;
    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;
    private erp.lib.table.STabFilterDatePeriod moTabFilterDatePeriod;
    private erp.table.STabFilterDocumentNature moTabFilterDocumentNature;
    private erp.mtrn.form.SDialogDpsFinder moDialogDpsFinder;
    private erp.mtrn.form.SDialogAnnulCfdi moDialogAnnulCfdi;

    private boolean mbIsCategoryPur = false;
    private boolean mbIsCategorySal = false;
    private boolean mbIsEstEst = false;
    private boolean mbIsEstCon = false;
    private boolean mbIsOrd = false;
    private boolean mbIsDoc = false;
    private boolean mbIsDocAdj = false;
    private boolean mbHasRightEdit = false;
    private boolean mbHasRightAuthor = false;

    /**
     * View to audit documents.
     * @param client GUI client interface.
     * @param tabTitle View tab title.
     * @param auxType01 Constants defined in SDataConstantsSys (TRNS_CT_DPS...).
     * @param auxType02 Constants defined in SDataConstantsSys (TRNX_TP_DPS...).
     */
    public SViewDpsCancelled(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01, int auxType02) {
        super(client, tabTitle, SDataConstants.TRN_DPS, auxType01, auxType02);
        initComponents();
    }

    private void initComponents() {
        int i = 0;
        int levelDoc = SDataConstantsSys.UNDEFINED;
        int typeImportFinder = SLibConstants.UNDEFINED;
        boolean createImportFinder = false;

        mbIsCategoryPur = mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_PUR;
        mbIsCategorySal = mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_SAL;
        mbIsEstEst = mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_DPS_EST_EST;
        mbIsEstCon = mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_DPS_EST_CON;
        mbIsOrd = mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_DPS_ORD;
        mbIsDoc = mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_DPS_DOC;
        mbIsDocAdj = mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_DPS_ADJ;
        mbHasRightEdit = false;
        mbHasRightAuthor = false;

        if (mbIsCategoryPur) {
            if (mbIsEstEst || mbIsEstCon) {
                levelDoc = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_EST).Level;
            }
            else if (mbIsOrd) {
                levelDoc = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_ORD).Level;
            }
            else if (mbIsDoc) {
                levelDoc = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_TRN).Level;
            }
            else if (mbIsDocAdj) {
                levelDoc = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_TRN_ADJ).Level;
            }

        }
        else {
            if (mbIsEstEst || mbIsEstCon) {
                levelDoc = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DOC_EST).Level;
            }
            else if (mbIsOrd) {
                levelDoc = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DOC_ORD).Level;
            }
            else if (mbIsDoc) {
                levelDoc = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DOC_TRN).Level;
            }
            else if (mbIsDocAdj) {
                levelDoc = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DOC_TRN_ADJ).Level;
            }

        }

        
        mbHasRightEdit = levelDoc >= SUtilConsts.LEV_AUTHOR;
        mbHasRightAuthor = levelDoc == SUtilConsts.LEV_AUTHOR;

        jbAnnul = new JButton(miClient.getImageIcon(SLibConstants.ICON_ANNUL));
        jbAnnul.setPreferredSize(new Dimension(23, 23));
        jbAnnul.addActionListener(this);
        jbAnnul.setToolTipText("Anular documento");

        jbImport = new JButton(miClient.getImageIcon(SLibConstants.ICON_DOC_IMPORT));
        jbImport.setPreferredSize(new Dimension(23, 23));
        jbImport.addActionListener(this);
        jbImport.setToolTipText("Importar documento");

        jbCopy = new JButton(miClient.getImageIcon(SLibConstants.ICON_COPY));
        jbCopy.setPreferredSize(new Dimension(23, 23));
        jbCopy.addActionListener(this);
        jbCopy.setToolTipText("Copiar documento");

        moTabFilterUser = new STabFilterUsers(miClient, this);
        moTabFilterUser.removeButtonUser();
        moTabFilterUser.setUserId(mbHasRightAuthor ? miClient.getSession().getUser().getPkUserId() : SDataConstantsSys.UNDEFINED);

        moTabFilterDeleted = new STabFilterDeleted(this);
        moTabFilterDatePeriod = new STabFilterDatePeriod(miClient, this, mbIsEstCon ? SLibConstants.GUI_DATE_AS_YEAR : SLibConstants.GUI_DATE_AS_YEAR_MONTH);
        moTabFilterDocumentNature = new STabFilterDocumentNature(miClient, this, SDataConstants.TRNU_DPS_NAT);
        moDialogAnnulCfdi = new SDialogAnnulCfdi(miClient, true);

        if (mbIsOrd || mbIsDoc || mbIsDocAdj) {
            createImportFinder = true;

            if (mbIsOrd || mbIsDoc) {
                typeImportFinder = SDataConstants.TRNX_DPS_PEND_LINK;
            }
            else {
                typeImportFinder = SDataConstants.TRNX_DPS_PEND_ADJ;
            }
        }

        moDialogDpsFinder = !createImportFinder ? null : new SDialogDpsFinder(miClient, typeImportFinder);

        addTaskBarUpperComponent(jbAnnul);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(jbImport);
        addTaskBarUpperComponent(jbCopy);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDatePeriod);
        addTaskBarUpperSeparator();
        addTaskBarLowerComponent(moTabFilterUser);
        addTaskBarLowerSeparator();
        addTaskBarLowerComponent(moTabFilterDocumentNature);

        jbNew.setEnabled(false);
        jbEdit.setEnabled(false);
        jbDelete.setEnabled(false);
        jbAnnul.setEnabled(false);
        jbImport.setEnabled(false);
        jbCopy.setEnabled(false);

        STableField[] aoKeyFields = new STableField[2];
        STableColumn[] aoTableColumns = null;

        if (mbIsDoc || mbIsDocAdj) {
            aoTableColumns = new STableColumn[30];  // four extra columns for accounting record
        }
        else {
            aoTableColumns = new STableColumn[25];
        }

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "d.id_year");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "d.id_doc");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        if (getDpsSortingType() == SDataConstantsSys.CFGS_TP_SORT_BIZ_P_DOC) {
            if (mbIsCategoryPur) {
                if (miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpc.bp_key", "Clave proveedor", 50);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Proveedor", 200);
                }
                else {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Proveedor", 200);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpc.bp_key", "Clave proveedor", 50);
                }
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb.bpb", "Sucursal proveedor", 75);
            }
            else {
                if (miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpc.bp_key", "Clave cliente", 50);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Cliente", 200);
                }
                else {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Cliente", 200);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpc.bp_key", "Clave cliente", 50);
                }
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb.bpb", "Sucursal cliente", 75);
            }

            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "f_ico", "Estatus", STableConstants.WIDTH_ICON);
            aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererIcon());
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "f_ico_xml", "XML", STableConstants.WIDTH_ICON);
            aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererIcon());
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "dt.code", "Tipo doc.", STableConstants.WIDTH_CODE_DOC);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_num", "Folio doc.", STableConstants.WIDTH_DOC_NUM);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "d.num_ref", "Referencia doc.", STableConstants.WIDTH_DOC_NUM_REF);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "d.dt", "Fecha doc.", STableConstants.WIDTH_DATE);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_cob_code", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "d.dt_doc_delivery_n", "Entrega programada", STableConstants.WIDTH_DATE);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tp_dps_ann", "Causas de Cancelación", 200);
        }
        else {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "dt.code", "Tipo doc.", STableConstants.WIDTH_CODE_DOC);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_num", "Folio doc.", STableConstants.WIDTH_DOC_NUM);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "d.num_ref", "Referencia doc.", STableConstants.WIDTH_DOC_NUM_REF);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "d.dt", "Fecha doc.", STableConstants.WIDTH_DATE);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_cob_code", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "d.dt_doc_delivery_n", "Entrega programada", STableConstants.WIDTH_DATE);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tp_dps_ann", "Causas de Cancelación", 200);
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "f_ico", "Estatus", STableConstants.WIDTH_ICON);
            aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererIcon());
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "f_ico_xml", "CFD", STableConstants.WIDTH_ICON);
            aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererIcon());

            if (mbIsCategoryPur) {
                if (miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpc.bp_key", "Clave proveedor", 50);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Proveedor", 200);
                }
                else {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Proveedor", 200);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpc.bp_key", "Clave proveedor", 50);
                }
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb.bpb", "Sucursal proveedor", 75);
            }
            else {
                if (miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpc.bp_key", "Clave cliente", 50);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Cliente", 200);
                }
                else {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Cliente", 200);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpc.bp_key", "Clave cliente", 50);
                }
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb.bpb", "Sucursal cliente", 75);
            }
        }

        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "d.tot_cur_r", "Total mon $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_cur_key", "Moneda", STableConstants.WIDTH_CURRENCY_KEY);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "d.exc_rate", "T cambio", STableConstants.WIDTH_EXCHANGE_RATE);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererExchangeRate());
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "d.tot_r", "Total $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++].setSumApplying(true);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_cur_key_local", "Moneda local", STableConstants.WIDTH_CURRENCY_KEY);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_dn_code", "Naturaleza doc.", STableConstants.WIDTH_CODE_DOC);

        if (mbIsDoc || mbIsDocAdj) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_ord_num", "Folio ped.", STableConstants.WIDTH_DOC_NUM);
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_rper", "Período póliza", STableConstants.WIDTH_YEAR_PERIOD);
            aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererDefaultColorBlueDark());
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_rbc_code", "Centro contable", STableConstants.WIDTH_CODE_COB);
            aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererDefaultColorBlueDark());
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_rcb_code", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
            aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererDefaultColorBlueDark());
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_rnum", "Folio póliza", STableConstants.WIDTH_RECORD_NUM);
            aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererDefaultColorBlueDark());
        }

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

        SFormUtilities.createActionMap(this, this, "publicActionPrint", "print", KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK);
        setIsSummaryApplying(true);

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.BPSU_BP);
        mvSuscriptors.add(SDataConstants.BPSU_BP_CT);
        mvSuscriptors.add(SDataConstants.BPSU_BPB);
        mvSuscriptors.add(SDataConstants.TRNX_DPS_AUDIT_PEND);
        mvSuscriptors.add(SDataConstants.TRNX_DPS_AUDITED);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        populateTable();
    }

    private int getDpsSortingType() {
        int type = SLibConstants.UNDEFINED;

        if (mbIsCategoryPur && (mbIsDoc || mbIsDocAdj)) {
            type = miClient.getSessionXXX().getParamsErp().getFkSortingDpsSupplierTypeId();
        }
        else {
            type = miClient.getSessionXXX().getParamsErp().getFkSortingDpsCustomerTypeId();
        }

        return type;
    }

    private int[] getDpsTypeKey() {
        int[] key = null;

        switch (mnTabTypeAux02) {
            case SDataConstantsSys.TRNX_TP_DPS_EST_EST:
                key = mbIsCategoryPur ? SDataConstantsSys.TRNU_TP_DPS_PUR_EST : SDataConstantsSys.TRNU_TP_DPS_SAL_EST;
                break;
            case SDataConstantsSys.TRNX_TP_DPS_EST_CON:
                key = mbIsCategoryPur ? SDataConstantsSys.TRNU_TP_DPS_PUR_CON : SDataConstantsSys.TRNU_TP_DPS_SAL_CON;
                break;
            case SDataConstantsSys.TRNX_TP_DPS_ORD:
                key = mbIsCategoryPur ? SDataConstantsSys.TRNU_TP_DPS_PUR_ORD : SDataConstantsSys.TRNU_TP_DPS_SAL_ORD;
                break;
            case SDataConstantsSys.TRNX_TP_DPS_DOC:
                key = mbIsCategoryPur ? SDataConstantsSys.TRNU_TP_DPS_PUR_INV : SDataConstantsSys.TRNU_TP_DPS_SAL_INV;
                break;
            case SDataConstantsSys.TRNX_TP_DPS_ADJ:
                key = mbIsCategoryPur ? SDataConstantsSys.TRNU_TP_DPS_PUR_CN : SDataConstantsSys.TRNU_TP_DPS_SAL_CN;
                break;
            default:
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
        }

        return key;
    }

    private int[] getDpsClassPreviousKey() {
        int[] key = null;

        switch (mnTabTypeAux02) {
            case SDataConstantsSys.TRNX_TP_DPS_ORD:
                key = mbIsCategoryPur ? SDataConstantsSys.TRNS_CL_DPS_PUR_EST : SDataConstantsSys.TRNS_CL_DPS_SAL_EST;
                break;
            case SDataConstantsSys.TRNX_TP_DPS_DOC:
                key = mbIsCategoryPur ? SDataConstantsSys.TRNS_CL_DPS_PUR_ORD : SDataConstantsSys.TRNS_CL_DPS_SAL_ORD;
                break;
            case SDataConstantsSys.TRNX_TP_DPS_ADJ:
                key = mbIsCategoryPur ? SDataConstantsSys.TRNS_CL_DPS_PUR_DOC : SDataConstantsSys.TRNS_CL_DPS_SAL_DOC;
                break;
            default:
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
        }

        return key;
    }

    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {
            int gui = mbIsCategoryPur ? SDataConstants.MOD_PUR : SDataConstants.MOD_SAL;    // GUI module

            miClient.getGuiModule(gui).setFormComplement(getDpsTypeKey());  // document type key
            if (miClient.getGuiModule(gui).showForm(mnTabType, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                miClient.getGuiModule(gui).refreshCatalogues(mnTabType);
                SDataUtilities.showDpsRecord(miClient, (SDataDps) miClient.getGuiModule(gui).getRegistry());
            }
        }
    }

    @Override
    public void actionEdit() {
       if (jbEdit.isEnabled()) {
           if (moTablePane.getSelectedTableRow() != null && !moTablePane.getSelectedTableRow().getIsSummary()) {
                int gui = mbIsCategoryPur ? SDataConstants.MOD_PUR : SDataConstants.MOD_SAL;    // GUI module

                miClient.getGuiModule(gui).setFormComplement(getDpsTypeKey());  // document type key
                miClient.getGuiModule(gui).setCurrentUserPrivilegeLevel(mbHasRightEdit ? SUtilConsts.LEV_AUTHOR : SUtilConsts.LEV_READ);

                if (miClient.getGuiModule(gui).showForm(mnTabType, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                    miClient.getGuiModule(gui).refreshCatalogues(mnTabType);
                    SDataUtilities.showDpsRecord(miClient, (SDataDps) miClient.getGuiModule(gui).getRegistry());
                }
            }
        }
    }

    @Override
    public void actionDelete() {
       if (jbDelete.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                if (miClient.showMsgBoxConfirm(SLibConstants.MSG_CNF_REG_DELETE) == JOptionPane.YES_OPTION) {
                    int gui = mbIsCategoryPur ? SDataConstants.MOD_PUR : SDataConstants.MOD_SAL;    // GUI module

                    if (miClient.getGuiModule(gui).deleteRegistry(mnTabType, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_DELETE_OK) {
                        miClient.getGuiModule(gui).refreshCatalogues(mnTabType);
                    }
                }
            }
        }
    }

    private void actionAnnul() throws Exception {
        SDataDps dps = null;
        SGuiParams params = null;
        boolean annul = true;

        if (jbAnnul.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                if (miClient.showMsgBoxConfirm(SLibConstants.MSG_CNF_REG_ANNUL) == JOptionPane.YES_OPTION) {
                    int gui = mbIsCategoryPur ? SDataConstants.MOD_PUR : SDataConstants.MOD_SAL;    // GUI module
                    dps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);

                    if (dps.getDbmsDataCfd() != null && dps.getDbmsDataCfd().getFkXmlTypeId() == SDataConstantsSys.TRNS_TP_XML_CFDI) {
                        annul = false;
                        params = new SGuiParams();

                        if (dps.getDbmsDataCfd().isStamped()) {
                            moDialogAnnulCfdi.formReset();
                            moDialogAnnulCfdi.formRefreshCatalogues();
                            moDialogAnnulCfdi.setValue(SGuiConsts.PARAM_DATE, dps.getDate());
                            moDialogAnnulCfdi.setValue(SGuiConsts.PARAM_DPS_TP, dps.getCfdTipoDeComprobante());
                            moDialogAnnulCfdi.setVisible(true);

                            if (moDialogAnnulCfdi.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                                annul = true;
                                params.getParamsMap().put(SGuiConsts.PARAM_DATE, moDialogAnnulCfdi.getDate());
                                params.getParamsMap().put(SGuiConsts.PARAM_REQ_DOC, moDialogAnnulCfdi.getAnnulSat()); // SGuiConsts.PARAM_REQ_DOC is used to indicate if SAT cancellation is required
                                params.getParamsMap().put(SModConsts.TRNU_TP_DPS_ANN, moDialogAnnulCfdi.getTpDpsAnn());
                            }
                        }
                        else {
                            annul = true;
                            params.getParamsMap().put(SGuiConsts.PARAM_DATE, miClient.getSession().getCurrentDate());
                            params.getParamsMap().put(SGuiConsts.PARAM_REQ_DOC, false);
                            params.getParamsMap().put(SModConsts.TRNU_TP_DPS_ANN, SModSysConsts.TRNU_TP_DPS_ANN_NA);
                        }
                    }

                    if (annul) {
                        if (miClient.getGuiModule(gui).annulRegistry(mnTabType, moTablePane.getSelectedTableRow().getPrimaryKey(), params) == SLibConstants.DB_ACTION_ANNUL_OK) {
                            miClient.getGuiModule(gui).refreshCatalogues(mnTabType);
                        }
                    }
                }
            }
        }
    }

    private void actionImport() {
        if (jbImport.isEnabled()) {
            moDialogDpsFinder.formReset();
            moDialogDpsFinder.setValue(SLibConstants.VALUE_FILTER_KEY, getDpsClassPreviousKey());
            moDialogDpsFinder.setVisible(true);

            if (moDialogDpsFinder.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                int gui = mbIsCategoryPur ? SDataConstants.MOD_PUR : SDataConstants.MOD_SAL;    // GUI module
                int[] adjustmentSubtypeKey = (int[]) moDialogDpsFinder.getValue(SDataConstants.TRNS_STP_DPS_ADJ);
                Object complement = null;

                if (adjustmentSubtypeKey == null) {
                    complement = new Object[] { getDpsTypeKey(), moDialogDpsFinder.getValue(SDataConstants.TRN_DPS), null, moDialogDpsFinder.getValue(SLibConstants.VALUE_CURRENCY_LOCAL) };
                }
                else {
                    complement = new Object[] { getDpsTypeKey(), moDialogDpsFinder.getValue(SDataConstants.TRN_DPS), adjustmentSubtypeKey, moDialogDpsFinder.getValue(SLibConstants.VALUE_CURRENCY_LOCAL) };
                }

                miClient.getGuiModule(gui).setFormComplement(complement);   // document type key, reference document and adjustment type (optional)
                if (miClient.getGuiModule(gui).showForm(mnTabType, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                    miClient.getGuiModule(gui).refreshCatalogues(mnTabType);
                    SDataUtilities.showDpsRecord(miClient, (SDataDps) miClient.getGuiModule(gui).getRegistry());
                }
            }
        }
    }

    private void actionCopy() {
        if (jbCopy.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                int gui = mbIsCategoryPur ? SDataConstants.MOD_PUR : SDataConstants.MOD_SAL;    // GUI module

                miClient.getGuiModule(gui).setFormComplement(getDpsTypeKey());  // document type key
                if (miClient.getGuiModule(gui).showFormForCopy(mnTabType, moTablePane.getSelectedTableRow().getPrimaryKey()) == SLibConstants.DB_ACTION_SAVE_OK) {
                    miClient.getGuiModule(gui).refreshCatalogues(mnTabType);
                    SDataUtilities.showDpsRecord(miClient, (SDataDps) miClient.getGuiModule(gui).getRegistry());
                }
            }
        }
    }

    @Override
    public void createSqlQuery() {
        String sqlWhere = "";
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_DELETED && setting.getStatus() == STableConstants.STATUS_ON) {
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "d.b_del = 0 ";
            }
            else if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + SDataSqlUtilities.composePeriodFilter((int[]) setting.getSetting(), "d.dt");
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_DOC_NAT) {
                if (((Integer) setting.getSetting()) != SLibConstants.UNDEFINED) {
                    sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "d.fid_dps_nat = " + ((Integer) setting.getSetting()) + " ";
                }
            }
        }
        
        sqlWhere += "AND d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_ANNULED + " ";

        msSql = "SELECT d.id_year, d.id_doc, d.dt, d.dt_doc_delivery_n, d.num_ref, d.comms_ref, d.exc_rate, " +
                "d.stot_r, d.tax_charged_r, d.tax_retained_r, d.tot_r, " +
                "d.stot_cur_r, d.tax_charged_cur_r, d.tax_retained_cur_r, d.tot_cur_r, " +
                "d.b_copy, d.b_link, d.b_close, d.b_audit, d.b_del, d.ts_link, d.ts_close, d.ts_new, d.ts_edit, d.ts_del, dt.code, " +
                "(SELECT tp_dps_ann FROM erp.trnu_tp_dps_ann where id_tp_dps_ann = d.fid_tp_dps_ann) AS tp_dps_ann, " +
                "(SELECT dn.code FROM erp.trnu_dps_nat AS dn WHERE d.fid_dps_nat = dn.id_dps_nat) AS f_dn_code, " +
                "CONCAT(d.num_ser, IF(length(d.num_ser) = 0, '', '-'), d.num) AS f_num, " +
                "(SELECT CONCAT(src.num_ser, IF(length(src.num_ser) = 0, '', '-'), src.num) AS id_ped " +
                "FROM trn_dps AS src INNER JOIN trn_dps_dps_supply AS spl ON src.id_doc = spl.id_src_doc AND src.id_year = spl.id_src_year " +
                "WHERE spl.id_des_doc = d.id_doc AND src.id_year = d.id_year AND src.b_del = 0 LIMIT 1) AS f_ord_num, " +
                "(SELECT CONCAT(ord.id_year, '-', ord.num) FROM mfg_ord AS ord WHERE d.fid_mfg_year_n = ord.id_year AND d.fid_mfg_ord_n = ord.id_ord) AS num_ord, " +
                "IF(d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_ANNULED + ", " + STableConstants.ICON_ST_ANNUL + ", " + STableConstants.ICON_NULL + ") AS f_ico, " +
                "IF(x.ts IS NULL OR doc_xml = '', " + STableConstants.ICON_NULL  + ", " + /* not is CFD not is CFDI */
                "IF(x.fid_tp_xml = " + SDataConstantsSys.TRNS_TP_XML_CFD + " OR x.fid_tp_xml = " + SDataConstantsSys.TRNS_TP_XML_NA + ", " + STableConstants.ICON_XML + ", " + /* is CFD */
                "IF(x.fid_st_xml = " + SDataConstantsSys.TRNS_ST_DPS_NEW + " OR LENGTH(uuid) = 0, " + STableConstants.ICON_XML_PEND + ", " + /* CFDI pending sign */
                "IF(LENGTH(x.ack_can_xml) = 0 AND x.ack_can_pdf_n IS NULL, " + STableConstants.ICON_XML_SIGN  + ", " + /* CFDI signed, canceled only SIIE */
                "IF(LENGTH(x.ack_can_xml) != 0, " + STableConstants.ICON_XML_CANC_XML + ", " + /* CFDI canceled with cancellation acknowledgment in XML format */
                "IF(x.ack_can_pdf_n IS NOT NULL, " + STableConstants.ICON_XML_CANC_PDF + ", " + /* CFDI canceled with cancellation acknowledgment in PDF format */
                STableConstants.ICON_XML_SIGN + " " + /* CFDI signed, canceled only SIIE */
                ")))))) AS f_ico_xml, " +
                "bp.id_bp, bp.bp, bpc.bp_key, bpb.id_bpb, bpb.bpb, " +
                "(SELECT c.cur_key FROM erp.cfgu_cur AS c WHERE d.fid_cur = c.id_cur) AS f_cur_key, " +
                "'" + miClient.getSession().getSessionCustom().getLocalCurrencyCode() + "' AS f_cur_key_local, " +
                "(SELECT cob.code FROM erp.bpsu_bpb AS cob WHERE d.fid_cob = cob.id_bpb) AS f_cob_code, ul.usr, uc.usr, un.usr, ue.usr, ud.usr ";

        if (mbIsDoc || mbIsDocAdj) {
            msSql += ", (SELECT rbc.code FROM fin_bkc AS rbc WHERE r.id_bkc = rbc.id_bkc) AS f_rbc_code, (SELECT rcb.code FROM erp.bpsu_bpb AS rcb WHERE r.fid_cob = rcb.id_bpb) AS f_rcb_code, CONCAT(r.id_year, '-', erp.lib_fix_int(r.id_per, 2)) as f_rper, " +
                    "CONCAT(r.id_tp_rec, '-', erp.lib_fix_int(r.id_num, " + SDataConstantsSys.NUM_LEN_FIN_REC + ")) as f_rnum ";
        }

        msSql +=
                "FROM trn_dps AS d " +
                "INNER JOIN erp.bpsu_bp AS bp ON d.fid_bp_r = bp.id_bp " +
                "INNER JOIN erp.bpsu_bp_ct AS bpc ON bp.id_bp = bpc.id_bp AND bpc.id_ct_bp = " +
                (mbIsCategoryPur ? SDataConstantsSys.BPSS_CT_BP_SUP : SDataConstantsSys.BPSS_CT_BP_CUS) + " " +
                "INNER JOIN erp.bpsu_bpb AS bpb ON d.fid_bpb = bpb.id_bpb " +
                "INNER JOIN erp.trnu_tp_dps AS dt ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps AND ";

        switch (mnTabTypeAux02) {
            case SDataConstantsSys.TRNX_TP_DPS_EST_EST:
            case SDataConstantsSys.TRNX_TP_DPS_EST_CON:
                msSql += (mbIsCategoryPur ?
                        "d.fid_ct_dps = " + SDataConstantsSys.TRNS_CL_DPS_PUR_EST[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNS_CL_DPS_PUR_EST[1] + " " :
                        "d.fid_ct_dps = " + SDataConstantsSys.TRNS_CL_DPS_SAL_EST[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNS_CL_DPS_SAL_EST[1] + " ");

                if (mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_DPS_EST_EST) {
                    msSql += (mbIsCategoryPur ?
                            "AND d.fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_EST[2] + " " :
                            "AND d.fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_EST[2] + " ");
                }
                else if (mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_DPS_EST_CON) {
                    msSql += (mbIsCategoryPur ?
                            "AND d.fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_CON[2] + " " :
                            "AND d.fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_CON[2] + " ");
                }
                break;

            case SDataConstantsSys.TRNX_TP_DPS_ORD:
                msSql += (mbIsCategoryPur ?
                        "d.fid_ct_dps = " + SDataConstantsSys.TRNS_CL_DPS_PUR_ORD[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNS_CL_DPS_PUR_ORD[1] + " " :
                        "d.fid_ct_dps = " + SDataConstantsSys.TRNS_CL_DPS_SAL_ORD[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNS_CL_DPS_SAL_ORD[1] + " ");
                break;

            case SDataConstantsSys.TRNX_TP_DPS_DOC:
                msSql += (mbIsCategoryPur ?
                        "d.fid_ct_dps = " + SDataConstantsSys.TRNS_CL_DPS_PUR_DOC[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNS_CL_DPS_PUR_DOC[1] + " " :
                        "d.fid_ct_dps = " + SDataConstantsSys.TRNS_CL_DPS_SAL_DOC[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNS_CL_DPS_SAL_DOC[1] + " ");
                break;

            case SDataConstantsSys.TRNX_TP_DPS_ADJ:
                msSql += (mbIsCategoryPur ?
                        "d.fid_ct_dps = " + SDataConstantsSys.TRNS_CL_DPS_PUR_ADJ[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNS_CL_DPS_PUR_ADJ[1] + " " :
                        "d.fid_ct_dps = " + SDataConstantsSys.TRNS_CL_DPS_SAL_ADJ[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNS_CL_DPS_SAL_ADJ[1] + " ");
                break;
            default:
        }

        msSql +=
                "INNER JOIN erp.usru_usr AS ul ON d.fid_usr_link = ul.id_usr " +
                "INNER JOIN erp.usru_usr AS uc ON d.fid_usr_close = uc.id_usr " +
                "INNER JOIN erp.usru_usr AS un ON d.fid_usr_new = un.id_usr " + (mbHasRightAuthor ? " AND d.fid_usr_new = " + miClient.getSession().getUser().getPkUserId() + " " : "") +
                "INNER JOIN erp.usru_usr AS ue ON d.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON d.fid_usr_del = ud.id_usr " +
                "LEFT OUTER JOIN trn_cfd AS x ON d.id_year = x.fid_dps_year_n AND d.id_doc = x.fid_dps_doc_n ";

        if (mbIsDoc || mbIsDocAdj) {
            msSql +=
                    "LEFT OUTER JOIN trn_dps_rec AS dr ON d.id_year = dr.id_dps_year AND d.id_doc = dr.id_dps_doc " +
                    "LEFT OUTER JOIN fin_rec AS r ON dr.fid_rec_year = r.id_year AND dr.fid_rec_per = r.id_per AND dr.fid_rec_bkc = r.id_bkc AND dr.fid_rec_tp_rec = r.id_tp_rec AND dr.fid_rec_num = r.id_num ";
        }

        msSql += (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere);

        if (getDpsSortingType() == SDataConstantsSys.CFGS_TP_SORT_BIZ_P_DOC) {
            msSql += "ORDER BY ";

            if ((mbIsCategoryPur && miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) ||
                (mbIsCategorySal && miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME)) {
                msSql += "bpc.bp_key, bp.bp, ";
            }
            else {
                msSql += "bp.bp, bpc.bp_key, ";
            }

            msSql += "bp.id_bp, bpb.bpb, bpb.id_bpb, dt.code, d.num_ser, CAST(d.num AS UNSIGNED INTEGER), d.num, d.dt ";
        }
        else {
            msSql += "ORDER BY dt.code, d.num_ser, CAST(d.num AS UNSIGNED INTEGER), d.num, d.dt, ";

            if ((mbIsCategoryPur && miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) ||
                (mbIsCategorySal && miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME)) {
                msSql += "bpc.bp_key, bp.bp, ";
            }
            else {
                msSql += "bp.bp, bpc.bp_key, ";
            }

            msSql += "bp.id_bp, bpb.bpb, bpb.id_bpb ";
        }
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        super.actionPerformed(evt);

        try {
            if (evt.getSource() instanceof javax.swing.JButton) {
                JButton button = (JButton) evt.getSource();

                if (button == jbAnnul) {
                    actionAnnul();
                }
                else if (button == jbImport) {
                    actionImport();
                }
                else if (button == jbCopy) {
                    actionCopy();
                }
            }
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }
    }
}
