/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataReadDescriptions;
import erp.data.SDataUtilities;
import erp.data.SProcConstants;
import erp.gui.SModuleUtilities;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.SLibUtilities;
import erp.lib.data.SDataSqlUtilities;
import erp.lib.table.STabFilterDateCutOff;
import erp.lib.table.STabFilterDatePeriod;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import erp.mbps.data.SDataBizPartnerBranch;
import erp.mbps.data.SDataBizPartnerBranchAddress;
import erp.mbps.data.SDataBizPartnerBranchContact;
import erp.mcfg.data.SDataCurrency;
import erp.mitm.data.SDataUnit;
import erp.mtrn.data.SCfdUtils;
import erp.mtrn.data.SDataDps;
import erp.mtrn.data.SDataUserDnsDps;
import erp.mtrn.data.STrnUtilities;
import erp.musr.data.SDataUser;
import erp.print.SDataConstantsPrint;
import erp.table.SFilterConstants;
import erp.table.STabFilterBizPartner;
import erp.table.STabFilterCompanyBranch;
import erp.table.STabFilterDnsDps;
import erp.table.STabFilterDocumentNature;
import erp.table.STabFilterFunctionalArea;
import erp.table.STabFilterUsers;
import java.awt.Cursor;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Map;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;

/**
 *
 * @author Alfonso Flores, Claudio Peña, Sergio Flores, Isabel Servín, Adrián Avilés
 */
public class SViewDpsPendAuthorized extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private javax.swing.JButton mjbAuthorize;
    private javax.swing.JButton mjbReject;
    private javax.swing.JButton mjbViewDps;
    private javax.swing.JButton mjbViewNotes;
    private javax.swing.JButton mjbViewLinks;
    private javax.swing.JButton mjPrint;        
    private erp.table.STabFilterCompanyBranch moTabFilterCompanyBranch;
    private erp.table.STabFilterBizPartner moTabFilterBizPartner;
    private erp.lib.table.STabFilterDateCutOff moTabFilterDateCutOff;
    private erp.lib.table.STabFilterDatePeriod moTabFilterDatePeriod;
    private erp.table.STabFilterUsers moTabFilterUser;
    private erp.table.STabFilterDocumentNature moTabFilterDocumentNature;
    private erp.table.STabFilterFunctionalArea moTabFilterFunctionalArea;
    private erp.table.STabFilterDnsDps moTabFilterDnsDps;

    private boolean mbHasRightAuthor = false;
    private boolean mbHasRightRejectOwn = false;
    private boolean mbIsOrd;

    public SViewDpsPendAuthorized(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01) {
        super(client, tabTitle, SDataConstants.TRNX_DPS_AUTHORIZE_PEND, auxType01);
        
        mbIsOrd = SLibUtils.belongsTo(mnTabTypeAux01, new int[] {
            SDataConstantsSys.TRNX_DPS_PUR_ORD_AUT_PEND,
            SDataConstantsSys.TRNX_DPS_PUR_ORD_AUT_AUT,
            SDataConstantsSys.TRNX_DPS_PUR_ORD_AUT_REJ, 
            SDataConstantsSys.TRNX_DPS_SAL_ORD_AUT_PEND, 
            SDataConstantsSys.TRNX_DPS_SAL_ORD_AUT_AUT, 
            SDataConstantsSys.TRNX_DPS_SAL_ORD_AUT_REJ });
        
        initComponents();
    }

    private void initComponents() {
        int i = 0;
        int levelDoc = SDataConstantsSys.UNDEFINED;
        boolean hasRightAuthorize = false;
        STableField[] aoKeyFields = null;
        STableColumn[] aoTableColumns = null;

        mbHasRightAuthor = false;

        if (isConPur()) {
            hasRightAuthorize = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_EST_AUTHORN).HasRight;
            levelDoc = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_EST_AUTHORN).Level;
        }else if (isConSal()) {
            hasRightAuthorize = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DOC_EST_AUTHORN).HasRight;
            levelDoc = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DOC_EST_AUTHORN).Level;
        }
        else if (isOrderPur()) {
            hasRightAuthorize = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_ORD_AUTHORN).HasRight;
            mbHasRightRejectOwn = !hasRightAuthorize && miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_ORD_REJECT_OWN).HasRight;
            levelDoc = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_ORD_AUTHORN).Level;
            moTabFilterDnsDps = new STabFilterDnsDps(miClient, this);
        }
        else if (isOrderSal()) {
            hasRightAuthorize = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DOC_ORD_AUTHORN).HasRight;
            mbHasRightRejectOwn = !hasRightAuthorize && miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DOC_ORD_REJECT_OWN).HasRight;
            levelDoc = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DOC_ORD_AUTHORN).Level;
            moTabFilterDnsDps = new STabFilterDnsDps(miClient, this);
        }
        else if (isDocPur()) {
            hasRightAuthorize = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_TRN).Level == SUtilConsts.LEV_MANAGER;
            levelDoc = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_TRN).Level;
        }
        else if (isDocSal()) {
            hasRightAuthorize = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DOC_TRN).Level == SUtilConsts.LEV_MANAGER;
            levelDoc = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DOC_TRN).Level;
        }
        mbHasRightAuthor = levelDoc == SUtilConsts.LEV_AUTHOR;

        mjbAuthorize = new JButton(miClient.getImageIcon(SLibConstants.ICON_APPROVE));
        mjbReject = new JButton(miClient.getImageIcon(SLibConstants.ICON_APPROVE_NO));
        mjbViewDps = new JButton(miClient.getImageIcon(SLibConstants.ICON_LOOK));
        mjbViewNotes = new JButton(miClient.getImageIcon(SLibConstants.ICON_NOTES));
        mjbViewLinks = new JButton(miClient.getImageIcon(SLibConstants.ICON_LINK));
        mjPrint = new JButton(miClient.getImageIcon(SLibConstants.ICON_PRINT));

        mjbAuthorize.setPreferredSize(new Dimension(23, 23));
        mjbReject.setPreferredSize(new Dimension(23, 23));
        mjbViewDps.setPreferredSize(new Dimension(23, 23));
        mjbViewNotes.setPreferredSize(new Dimension(23, 23));
        mjbViewLinks.setPreferredSize(new Dimension(23, 23));
        mjPrint.setPreferredSize(new Dimension(23, 23));

        mjbAuthorize.addActionListener(this);
        mjbReject.addActionListener(this);
        mjbViewDps.addActionListener(this);
        mjbViewNotes.addActionListener(this);
        mjbViewLinks.addActionListener(this);
        mjPrint.addActionListener(this);
        
        mjbAuthorize.setToolTipText("Autorizar documento");
        mjbReject.setToolTipText("Rechazar documento");
        mjbViewDps.setToolTipText("Ver documento");
        mjbViewNotes.setToolTipText("Ver notas");
        mjbViewLinks.setToolTipText("Ver vínculos del documento");
        mjPrint.setToolTipText("Imprimir documento");

        moTabFilterCompanyBranch = new STabFilterCompanyBranch(miClient, this);
        moTabFilterBizPartner = new STabFilterBizPartner(miClient, this, isPurchase() ? SDataConstantsSys.BPSS_CT_BP_SUP : SDataConstantsSys.BPSS_CT_BP_CUS);

        moTabFilterUser = new STabFilterUsers(miClient, this);
        moTabFilterUser.removeButtonUser();
        moTabFilterUser.setUserId(mbHasRightAuthor ? miClient.getSession().getUser().getPkUserId() : SDataConstantsSys.UNDEFINED);
        moTabFilterDocumentNature = new STabFilterDocumentNature(miClient, this, SDataConstants.TRNU_DPS_NAT);
        moTabFilterFunctionalArea = new STabFilterFunctionalArea(miClient, this);

        if (isViewDocsPending()) {
            moTabFilterDateCutOff = new STabFilterDateCutOff(miClient, this, SLibTimeUtilities.getEndOfYear(miClient.getSessionXXX().getWorkingDate()));
            moTabFilterDatePeriod = null;
        }
        else {
            moTabFilterDateCutOff = null;
            moTabFilterDatePeriod = new STabFilterDatePeriod(miClient, this, SLibConstants.GUI_DATE_AS_YEAR_MONTH);
        }

        removeTaskBarUpperComponent(jbNew);
        removeTaskBarUpperComponent(jbEdit);
        removeTaskBarUpperComponent(jbDelete);

        addTaskBarUpperComponent(mjbAuthorize);
        addTaskBarUpperComponent(mjbReject);
        addTaskBarUpperComponent(mjPrint);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(isViewDocsPending() ? moTabFilterDateCutOff : moTabFilterDatePeriod);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterCompanyBranch);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterBizPartner);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(mjbViewDps);
        addTaskBarUpperComponent(mjbViewNotes);
        addTaskBarUpperComponent(mjbViewLinks);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterUser);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDocumentNature);
        addTaskBarUpperComponent(moTabFilterFunctionalArea);
        if (isOrderPur() || isOrderSal()) {
            addTaskBarUpperComponent(moTabFilterDnsDps);
        }

        mjbAuthorize.setEnabled(hasRightAuthorize && (isViewDocsPending() || isViewDocsRejected()));
        mjbReject.setEnabled((hasRightAuthorize || mbHasRightRejectOwn) && (isViewDocsPending() || isViewDocsAuthorized()));
        if (isOrderPur()) {
            mjPrint.setEnabled(true);
        }
        else {
            mjPrint.setEnabled(false);
        }
            
        mjbViewDps.setEnabled(true);
        mjbViewNotes.setEnabled(true);
        mjbViewLinks.setEnabled(true);

        aoKeyFields = new STableField[2];
        aoTableColumns = new STableColumn[(isViewDocsPending() || isViewDocsRejected() ? 16 : 15)];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "d.id_year");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "d.id_doc");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "dt.code", "Tipo doc.", STableConstants.WIDTH_CODE_DOC);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_num", "Folio doc.", STableConstants.WIDTH_DOC_NUM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "d.num_ref", "Referencia doc.", STableConstants.WIDTH_DOC_NUM_REF);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "d.dt", "Fecha doc.", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cb.code", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "d.dt_doc_delivery_n", "Entrega programada", STableConstants.WIDTH_DATE);
        if (isPurchase()) {
            if (miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bc.bp_key", "Clave proveedor", 50);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bp", "Proveedor", 200);
            }
            else {
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bp", "Proveedor", 200);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bc.bp_key", "Clave proveedor", 50);
            }
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bb.bpb", "Sucursal proveedor", 75);
        }
        else {
            if (miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bc.bp_key", "Clave cliente", 50);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bp", "Cliente", 200);
            }
            else {
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bp", "Cliente", 200);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bc.bp_key", "Clave cliente", 50);
            }
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bb.bpb", "Sucursal cliente", 75);
        }
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "d.tot_cur_r", "Total mon $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "c.cur_key", "Moneda", STableConstants.WIDTH_CURRENCY_KEY);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un.usr", "Usuario", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "sa.st_dps_authorn", "Estatus autorización", 100);
        if (isViewDocsPending() || isViewDocsRejected()) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_aut_auth_rej", "No autorización automática", 200);
        }
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ua.usr", "Usr. autorización", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "d.ts_authorn", "Autorizado", STableConstants.WIDTH_DATE_TIME);
        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.BPSU_BP);
        mvSuscriptors.add(SDataConstants.BPSU_BP_CT);
        mvSuscriptors.add(SDataConstants.BPSU_BPB);
        mvSuscriptors.add(SDataConstants.TRN_DPS);
        mvSuscriptors.add(SDataConstants.TRNX_DPS_AUTHORIZE_PEND);
        mvSuscriptors.add(SDataConstants.TRNX_DPS_AUTHORIZED);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        mvSuscriptors.add(SDataConstants.USRU_USR);

        getTablePane().setDoubleClickAction(this, "publicActionViewDps");

        populateTable();
    }

    private boolean isPurchase() {
        return
            mnTabTypeAux01 == SDataConstantsSys.TRNX_DPS_PUR_CON_AUT_AUT ||
            mnTabTypeAux01 == SDataConstantsSys.TRNX_DPS_PUR_CON_AUT_PEND ||
            mnTabTypeAux01 == SDataConstantsSys.TRNX_DPS_PUR_CON_AUT_REJ ||    
            mnTabTypeAux01 == SDataConstantsSys.TRNX_DPS_PUR_ORD_AUT_AUT ||
            mnTabTypeAux01 == SDataConstantsSys.TRNX_DPS_PUR_ORD_AUT_PEND ||
            mnTabTypeAux01 == SDataConstantsSys.TRNX_DPS_PUR_ORD_AUT_REJ ||
            mnTabTypeAux01 == SDataConstantsSys.TRNX_DPS_PUR_DOC_AUT_AUT ||
            mnTabTypeAux01 == SDataConstantsSys.TRNX_DPS_PUR_DOC_AUT_PEND ||
            mnTabTypeAux01 == SDataConstantsSys.TRNX_DPS_PUR_DOC_AUT_REJ;
    }
    
    private boolean isConPur() {
        return
            mnTabTypeAux01 == SDataConstantsSys.TRNX_DPS_PUR_CON_AUT_AUT ||
            mnTabTypeAux01 == SDataConstantsSys.TRNX_DPS_PUR_CON_AUT_PEND ||
            mnTabTypeAux01 == SDataConstantsSys.TRNX_DPS_PUR_CON_AUT_REJ;
    }

    private boolean isConSal() {
        return
            mnTabTypeAux01 == SDataConstantsSys.TRNX_DPS_SAL_CON_AUT_AUT ||
            mnTabTypeAux01 == SDataConstantsSys.TRNX_DPS_SAL_CON_AUT_PEND ||
            mnTabTypeAux01 == SDataConstantsSys.TRNX_DPS_SAL_CON_AUT_REJ;
    }

    private boolean isOrderPur() {
        return
            mnTabTypeAux01 == SDataConstantsSys.TRNX_DPS_PUR_ORD_AUT_AUT ||
            mnTabTypeAux01 == SDataConstantsSys.TRNX_DPS_PUR_ORD_AUT_PEND ||
            mnTabTypeAux01 == SDataConstantsSys.TRNX_DPS_PUR_ORD_AUT_REJ;
    }

    private boolean isOrderSal() {
        return
            mnTabTypeAux01 == SDataConstantsSys.TRNX_DPS_SAL_ORD_AUT_AUT ||
            mnTabTypeAux01 == SDataConstantsSys.TRNX_DPS_SAL_ORD_AUT_PEND ||
            mnTabTypeAux01 == SDataConstantsSys.TRNX_DPS_SAL_ORD_AUT_REJ;
    }

    private boolean isDocPur() {
        return
            mnTabTypeAux01 == SDataConstantsSys.TRNX_DPS_PUR_DOC_AUT_AUT ||
            mnTabTypeAux01 == SDataConstantsSys.TRNX_DPS_PUR_DOC_AUT_PEND ||
            mnTabTypeAux01 == SDataConstantsSys.TRNX_DPS_PUR_DOC_AUT_REJ;
    }

    private boolean isDocSal() {
        return
            mnTabTypeAux01 == SDataConstantsSys.TRNX_DPS_SAL_DOC_AUT_AUT ||
            mnTabTypeAux01 == SDataConstantsSys.TRNX_DPS_SAL_DOC_AUT_PEND ||
            mnTabTypeAux01 == SDataConstantsSys.TRNX_DPS_SAL_DOC_AUT_REJ;
    }
    

    private boolean isViewDocsPending() {
        return
            mnTabTypeAux01 == SDataConstantsSys.TRNX_DPS_PUR_CON_AUT_PEND ||
            mnTabTypeAux01 == SDataConstantsSys.TRNX_DPS_SAL_CON_AUT_PEND ||
            mnTabTypeAux01 == SDataConstantsSys.TRNX_DPS_PUR_ORD_AUT_PEND ||
            mnTabTypeAux01 == SDataConstantsSys.TRNX_DPS_SAL_ORD_AUT_PEND ||
            mnTabTypeAux01 == SDataConstantsSys.TRNX_DPS_PUR_DOC_AUT_PEND ||
            mnTabTypeAux01 == SDataConstantsSys.TRNX_DPS_SAL_DOC_AUT_PEND;
    }

    private boolean isViewDocsAuthorized() {
        return
            mnTabTypeAux01 == SDataConstantsSys.TRNX_DPS_PUR_CON_AUT_AUT ||
            mnTabTypeAux01 == SDataConstantsSys.TRNX_DPS_SAL_CON_AUT_AUT ||
            mnTabTypeAux01 == SDataConstantsSys.TRNX_DPS_PUR_ORD_AUT_AUT ||
            mnTabTypeAux01 == SDataConstantsSys.TRNX_DPS_SAL_ORD_AUT_AUT ||
            mnTabTypeAux01 == SDataConstantsSys.TRNX_DPS_PUR_DOC_AUT_AUT ||
            mnTabTypeAux01 == SDataConstantsSys.TRNX_DPS_SAL_DOC_AUT_AUT;
    }

    private boolean isViewDocsRejected() {
        return
            mnTabTypeAux01 == SDataConstantsSys.TRNX_DPS_PUR_CON_AUT_REJ ||
            mnTabTypeAux01 == SDataConstantsSys.TRNX_DPS_SAL_CON_AUT_REJ ||
            mnTabTypeAux01 == SDataConstantsSys.TRNX_DPS_PUR_ORD_AUT_REJ ||
            mnTabTypeAux01 == SDataConstantsSys.TRNX_DPS_SAL_ORD_AUT_REJ ||
            mnTabTypeAux01 == SDataConstantsSys.TRNX_DPS_PUR_DOC_AUT_REJ ||
            mnTabTypeAux01 == SDataConstantsSys.TRNX_DPS_SAL_DOC_AUT_REJ;
    }

    private void actionAuthorizeDoc() {
        if (mjbAuthorize.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                if (miClient.showMsgBoxConfirm(SLibConstants.MSG_CNF_DOC_AUTHORIZE) == JOptionPane.YES_OPTION) {
                    Vector<Object> params = new Vector<Object>();

                    params.add(((int[])moTablePane.getSelectedTableRow().getPrimaryKey())[0]);
                    params.add(((int[])moTablePane.getSelectedTableRow().getPrimaryKey())[1]);
                    params.add(SDataConstantsSys.UPD_DPS_FL_AUTHORN);
                    params.add(SDataConstantsSys.TRNS_ST_DPS_AUTHORN_AUTHORN);
                    params.add(miClient.getSession().getUser().getPkUserId());
                    params = SDataUtilities.callProcedure(miClient, SProcConstants.TRN_DPS_UPD, params, SLibConstants.EXEC_MODE_SILENT);

                    if (params.isEmpty()) {
                        miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_DB_REG_PROCESS);
                    }
                    else {
                        if (SLibUtilities.parseInt((String) params.get(0)) != 0) {
                            miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_DB_REG_PROCESS + "\n" + params.get(1) + " (Error: " + params.get(0) + ")");
                        }
                        else {
                            miClient.getGuiModule(isPurchase() ? SDataConstants.MOD_PUR : SDataConstants.MOD_SAL).refreshCatalogues(mnTabType);
                        }
                    }
                }
            }
        }
    }

    private void actionRejectDoc() {
        boolean reject = true;
        SDataDps dps = null;
        
        if (mjbReject.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                if (miClient.showMsgBoxConfirm(SLibConstants.MSG_CNF_DOC_REJECT) == JOptionPane.YES_OPTION) {
                    try {
                        dps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);

                        if (mbHasRightRejectOwn) {
                            if (miClient.getSession().getUser().getPkUserId() == dps.getFkUserNewId()) {
                                reject = isOrderPur() || isOrderSal();
                            }
                            else {
                                reject = false;
                                miClient.showMsgBoxWarning("El documento no puede ser rechado porque:\n -El usuario '" + miClient.getSessionXXX().getUser().getName() + "' es distinto al usuario de creación.");
                            }
                        }

                        if (reject) {
                            Vector<Object> params = new Vector<>();

                            params.add(((int[])moTablePane.getSelectedTableRow().getPrimaryKey())[0]);
                            params.add(((int[])moTablePane.getSelectedTableRow().getPrimaryKey())[1]);
                            params.add(SDataConstantsSys.UPD_DPS_FL_AUTHORN);
                            params.add(SDataConstantsSys.TRNS_ST_DPS_AUTHORN_REJECT);
                            params.add(miClient.getSession().getUser().getPkUserId());
                            params = SDataUtilities.callProcedure(miClient, SProcConstants.TRN_DPS_UPD, params, SLibConstants.EXEC_MODE_SILENT);

                            if (params.isEmpty()) {
                                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_DB_REG_PROCESS);
                            }
                            else {
                                if (SLibUtilities.parseInt((String) params.get(0)) != 0) {
                                    miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_DB_REG_PROCESS + "\n" + params.get(1) + " (Error: " + params.get(0) + ")");
                                }
                                else {
                                    miClient.getGuiModule(isPurchase() ? SDataConstants.MOD_PUR : SDataConstants.MOD_SAL).refreshCatalogues(mnTabType);
                                }
                            }
                        }
                    }
                    catch (Exception e) {
                        SLibUtilities.printOutException(this, e);
                    }
                }
            }
        }
    }

    private void actionViewDps() {
        int gui = isPurchase() ? SDataConstants.MOD_PUR : SDataConstants.MOD_SAL;    // GUI module
        int[] type = isConPur() ? SDataConstantsSys.TRNU_TP_DPS_PUR_CON : isOrderPur() ? SDataConstantsSys.TRNU_TP_DPS_PUR_ORD : isDocPur() ? SDataConstantsSys.TRNU_TP_DPS_PUR_INV : 
            isConSal() ? SDataConstantsSys.TRNU_TP_DPS_SAL_CON : isOrderSal() ? SDataConstantsSys.TRNU_TP_DPS_SAL_ORD : SDataConstantsSys.TRNU_TP_DPS_SAL_INV;

        if (moTablePane.getSelectedTableRow() != null) {
            miClient.getGuiModule(gui).setFormComplement(type);  // document type key
            miClient.getGuiModule(gui).showForm(SDataConstants.TRNX_DPS_RO, moTablePane.getSelectedTableRow().getPrimaryKey());
        }
    }

    private void actionViewNotes() {
        if (mjbViewNotes.isEnabled()) {
            SModuleUtilities.showDocumentNotes(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow());
        }
    }

    private void actionViewLinks() {
        if (mjbViewLinks.isEnabled()) {
            SModuleUtilities.showDocumentLinks(miClient, moTablePane.getSelectedTableRow());
        }
    }

    private void actionPrint() {
        if (mjPrint.isEnabled()) {
            int i = 0;
            String sUserBuyer = "";
            String sUserAuthorize = "";
            int nFkEmiAddressFormatTypeId_n = 0;
            int nFkRecAddressFormatTypeId_n = 0;
            boolean bincludeCountry = false;
            int[] keyDpsType = null;
            String[] addressOficial = null;
            String[] addressContract = null;
            String[] addressDelivery = null;
            String[] addressDeliveryCompany = null;
            Cursor cursor = getCursor();
            Map<String, Object> map = null;
            JasperPrint jasperPrint = null;
            JasperViewer jasperViewer = null;
            SDataDps oDps = null;
            SDataBizPartnerBranch oCompanyBranch = null;
            SDataBizPartnerBranch oBizPartnerBranch = null;
            SDataBizPartnerBranchAddress oAddress = null;
            SDataBizPartnerBranchContact oContact = null;
            SDataUser oUserBuyer = null;
            SDataUser oUserAuthorize = null;
            SDataCurrency oCurrency = null;
            SDataUnit oUnit = null;
            Vector<String> vPrice = null;
            Vector<String> vUnit = null;
            String sContact = "";

            if (moTablePane.getSelectedTableRow() == null/* && !isBizPartnerBlocked()*/|| moTablePane.getSelectedTableRow().getIsSummary()) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                oDps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_SILENT);

                if (oDps.getDbmsDataCfd() != null) {
                    try {
                        SCfdUtils.printCfd(miClient, oDps.getDbmsDataCfd(), SLibConstants.UNDEFINED, SDataConstantsPrint.PRINT_MODE_VIEWER, 1, false);
                    }
                    catch (Exception e) {
                        SLibUtilities.renderException(this, e);
                    }
                }
                else {
                    oCompanyBranch = (SDataBizPartnerBranch) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BPB, new int[] { oDps.getFkCompanyBranchId() }, SLibConstants.EXEC_MODE_SILENT);
                    oBizPartnerBranch = (SDataBizPartnerBranch) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BPB, new int[] { oDps.getFkBizPartnerBranchId() }, SLibConstants.EXEC_MODE_SILENT);
                    oAddress = (SDataBizPartnerBranchAddress) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BPB_ADD, new int [] { oDps.getFkBizPartnerBranchId(),
                        oDps.getFkBizPartnerBranchAddressId() }, SLibConstants.EXEC_MODE_SILENT);
                    oCurrency = (SDataCurrency) SDataUtilities.readRegistry(miClient, SDataConstants.CFGU_CUR, new int[] { oDps.getFkCurrencyId() }, SLibConstants.EXEC_MODE_SILENT);

                    if (oBizPartnerBranch.getFkAddressFormatTypeId_n() != SLibConstants.UNDEFINED) {
                        nFkRecAddressFormatTypeId_n = oBizPartnerBranch.getFkAddressFormatTypeId_n();
                    }
                    else {
                        nFkRecAddressFormatTypeId_n = miClient.getSessionXXX().getParamsCompany().getFkDefaultAddressFormatTypeId_n();
                    }

                    if (oCompanyBranch.getFkAddressFormatTypeId_n() != SLibConstants.UNDEFINED) {
                        nFkEmiAddressFormatTypeId_n = oAddress.getFkAddressTypeId();
                    }
                    else {
                        nFkEmiAddressFormatTypeId_n = miClient.getSessionXXX().getParamsCompany().getFkDefaultAddressFormatTypeId_n();
                    }

                    if (oAddress.getFkCountryId_n() == oCompanyBranch.getDbmsBizPartnerBranchAddressOfficial().getFkCountryId_n()) {
                        bincludeCountry = false;
                    }
                    else {
                        bincludeCountry = true;
                    }

                    addressOficial = oBizPartnerBranch.getDbmsBizPartnerBranchAddressOfficial().obtainAddress(nFkRecAddressFormatTypeId_n,
                        SDataBizPartnerBranchAddress.ADDRESS_4ROWS, bincludeCountry);
                    addressContract = oBizPartnerBranch.getDbmsBizPartnerBranchAddressOfficial().obtainAddress(nFkRecAddressFormatTypeId_n,
                        SDataBizPartnerBranchAddress.ADDRESS_4ROWS, bincludeCountry);
                    addressDelivery = oAddress.obtainAddress(nFkRecAddressFormatTypeId_n,
                        SDataBizPartnerBranchAddress.ADDRESS_4ROWS, bincludeCountry);
                    addressDeliveryCompany = oCompanyBranch.getDbmsBizPartnerBranchAddressOfficial().obtainAddress(nFkEmiAddressFormatTypeId_n,
                        SDataBizPartnerBranchAddress.ADDRESS_4ROWS, bincludeCountry);
                    oUserBuyer = (SDataUser) SDataUtilities.readRegistry(miClient, SDataConstants.USRU_USR, new int[] { oDps.getFkUserNewId() }, SLibConstants.EXEC_MODE_SILENT);
                    oUserAuthorize = (SDataUser) SDataUtilities.readRegistry(miClient, SDataConstants.USRU_USR, new int[] { oDps.getFkUserAuthorizedId() }, SLibConstants.EXEC_MODE_SILENT);

                    sUserBuyer = SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.USRU_USR, new int[] { oDps.getFkUserNewId() });
                    sUserAuthorize = SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.USRU_USR, new int[] { oDps.getFkUserAuthorizedId() });

                    keyDpsType = new int[] { oDps.getFkDpsCategoryId(), oDps.getFkDpsClassId(), oDps.getFkDpsTypeId() };

                    if (SLibUtilities.compareKeys(keyDpsType, SDataConstantsSys.TRNU_TP_DPS_PUR_ORD) ||
                        SLibUtilities.compareKeys(keyDpsType, SDataConstantsSys.TRNU_TP_DPS_SAL_ORD)) {
                        // Order:

                        if (SLibUtilities.compareKeys(keyDpsType, SDataConstantsSys.TRNU_TP_DPS_PUR_ORD) && oDps.getIsAuthorized()) {
                            miClient.showMsgBoxWarning("No se puede imprimir el documento porque:\n-No está autorizado.");
                        }
                        else {
                            try {
                                
                                STrnUtilities.createReportOrder(miClient, null, oDps, SDataConstantsPrint.PRINT_MODE_VIEWER);
                            }
                            catch (Exception e) {
                                SLibUtilities.renderException(this, e);
                            }
                            finally {
                                setCursor(cursor);
                            }
                        }
                    }
                    else if (SLibUtilities.compareKeys(keyDpsType, SDataConstantsSys.TRNU_TP_DPS_SAL_INV)) {
                        // Sales document:

                        try {
                            setCursor(new Cursor(Cursor.WAIT_CURSOR));
                            oContact = miClient.getSessionXXX().getCompany().getDbmsDataCompany().getDbmsHqBranch().getDbmsBizPartnerBranchContacts().size() <= 1 ? null :
                                    miClient.getSessionXXX().getCompany().getDbmsDataCompany().getDbmsHqBranch().getDbmsBizPartnerBranchContacts().get(1);

                            map = miClient.createReportParams();
                            map.put("nIdYear", oDps.getPkYearId());
                            map.put("nIdDoc", oDps.getPkDocId());
                            map.put("sAddressLine1", addressOficial[0]);
                            map.put("sAddressLine2", addressOficial[1]);
                            map.put("sAddressLine3", addressOficial[2]);
                            map.put("sAddressLine4", addressOficial.length > 3 ? addressOficial[3] : "");
                            map.put("sAddressDelivery1", addressDelivery[0].compareTo(addressOficial[0]) == 0 ? "": addressDelivery[0]);
                            map.put("sAddressDelivery2", addressDelivery[1].compareTo(addressOficial[1]) == 0 ? "DOMICILIO DEL CLIENTE": addressDelivery[1]);
                            map.put("sAddressDelivery3", addressDelivery[2].compareTo(addressOficial[2]) == 0 ? "": addressDelivery[2]);
                            map.put("sAddressDelivery4", addressDelivery.length > 3 && addressOficial.length > 3 ?
                                addressDelivery[3].compareTo(addressOficial[3]) == 0 ? "": addressDelivery[3] : "");
                            map.put("nBizPartnerCategory", SDataConstantsSys.BPSS_CT_BP_CUS);
                            map.put("sValueText", SLibUtilities.translateValueToText(oDps.getTotalCy_r(), 2,
                                oDps.getFkCurrencyId() == miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getPkCurrencyId() ? SLibConstants.LAN_SPANISH :
                                SLibConstants.LAN_ENGLISH, oCurrency.getTextSingular(), oCurrency.getTextPlural(), oCurrency.getTextPrefix(), oCurrency.getTextSuffix()));
                            map.put("sErpCurrencyKey", miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getKey());
                            map.put("bShowBackground", false);
                            map.put("sManagerFinance", oContact == null ? "" : SLibUtilities.textTrim(oContact.getContactPrefix() + " " + oContact.getFirstname() + " " + oContact.getLastname()));

                            jasperPrint = SDataUtilities.fillReport(miClient, SDataConstantsSys.REP_TRN_DPS, map);
                            jasperViewer = new JasperViewer(jasperPrint, false);
                            jasperViewer.setTitle("Impresión de factura");
                            jasperViewer.setVisible(true);
                        }
                        catch (Exception e) {
                            SLibUtilities.renderException(this, e);
                        }
                        finally {
                            setCursor(cursor);
                        }
                    }
                }
            }
        }
    }        

    @Override
    @SuppressWarnings("unchecked")
    public void createSqlQuery() {
        String sqlFilter = "";
        String sqlDatePeriod = "";
        String sqlCompanyBranch = "";
        String sqlBizPartner = "";
        String sqlDocNature = "";
        String sqlFunctionalArea = "";
        String sqlOrderBy = "";
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);

            if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                if (isViewDocsPending()) {
                    sqlDatePeriod += setting.getSetting() == null ? "" : "d.dt <= '" + (new java.sql.Date(((java.util.Date) setting.getSetting()).getTime())) + "' ";
                }
                else {
                    sqlDatePeriod += SDataSqlUtilities.composePeriodFilter((int[]) setting.getSetting(), "d.dt");
                }
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_COB) {
                sqlCompanyBranch += ((Integer) setting.getSetting() == SLibConstants.UNDEFINED ? "" : "d.fid_cob = " + (Integer) setting.getSetting() + " ");
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_BP) {
                sqlBizPartner += ((Integer) setting.getSetting() == SLibConstants.UNDEFINED ? "" : "d.fid_bp_r = " + (Integer) setting.getSetting() + " ");
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_DOC_NAT) {
                if (((Integer) setting.getSetting()) != SLibConstants.UNDEFINED) {
                    sqlDocNature += (sqlDocNature.isEmpty() ? "" : "AND ") + "d.fid_dps_nat = " + ((Integer) setting.getSetting()) + " ";
                }
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_FUNC_AREA) {
                if (!((String) setting.getSetting()).isEmpty()) {
                    sqlFunctionalArea += (sqlFunctionalArea.isEmpty() ? "" : " AND ") + "d.fid_func IN ( " + ((String) setting.getSetting()) + ") ";
                }
            }
        }

        switch (mnTabTypeAux01) {
            case SDataConstantsSys.TRNX_DPS_PUR_CON_AUT_AUT:
            case SDataConstantsSys.TRNX_DPS_PUR_CON_AUT_PEND:
            case SDataConstantsSys.TRNX_DPS_PUR_CON_AUT_REJ:
                sqlFilter = " AND d.fid_ct_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_CON[0] + " " +
                        "AND d.fid_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_CON[1] + " " +
                        "AND d.fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_CON[2] + " ";
                break;
            case SDataConstantsSys.TRNX_DPS_SAL_CON_AUT_AUT:
            case SDataConstantsSys.TRNX_DPS_SAL_CON_AUT_PEND:
            case SDataConstantsSys.TRNX_DPS_SAL_CON_AUT_REJ:
                sqlFilter = " AND d.fid_ct_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_CON[0] + " " +
                        "AND d.fid_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_CON[1] + " " +
                        "AND d.fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_CON[2] + " ";
                break;
            case SDataConstantsSys.TRNX_DPS_PUR_ORD_AUT_AUT:
            case SDataConstantsSys.TRNX_DPS_PUR_ORD_AUT_PEND:
            case SDataConstantsSys.TRNX_DPS_PUR_ORD_AUT_REJ:
                sqlFilter = " AND d.fid_ct_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[0] + " " +
                        "AND d.fid_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[1] + " " +
                        "AND d.fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[2] + " ";
                break;
            case SDataConstantsSys.TRNX_DPS_SAL_ORD_AUT_AUT:
            case SDataConstantsSys.TRNX_DPS_SAL_ORD_AUT_PEND:
            case SDataConstantsSys.TRNX_DPS_SAL_ORD_AUT_REJ:
                sqlFilter = " AND d.fid_ct_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_ORD[0] + " " +
                        "AND d.fid_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_ORD[1] + " " +
                        "AND d.fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_ORD[2] + " ";
                break;
            case SDataConstantsSys.TRNX_DPS_PUR_DOC_AUT_AUT:
            case SDataConstantsSys.TRNX_DPS_PUR_DOC_AUT_PEND:
            case SDataConstantsSys.TRNX_DPS_PUR_DOC_AUT_REJ:
                sqlFilter = " AND d.fid_ct_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_INV[0] + " " +
                        "AND d.fid_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_INV[1] + " " +
                        "AND d.fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_INV[2] + " ";
                break;
            case SDataConstantsSys.TRNX_DPS_SAL_DOC_AUT_AUT:
            case SDataConstantsSys.TRNX_DPS_SAL_DOC_AUT_PEND:
            case SDataConstantsSys.TRNX_DPS_SAL_DOC_AUT_REJ:
                sqlFilter = " AND d.fid_ct_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_INV[0] + " " +
                        "AND d.fid_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_INV[1] + " " +
                        "AND d.fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_INV[2] + " ";
                break;
            default:
        }

        switch (isPurchase() &&
                ((isDocPur() || isDocSal())) ?
            miClient.getSessionXXX().getParamsErp().getFkSortingDpsSupplierTypeId() :
            miClient.getSessionXXX().getParamsErp().getFkSortingDpsCustomerTypeId()) {

            case SDataConstantsSys.CFGS_TP_SORT_DOC_BIZ_P:
                sqlOrderBy = "ORDER BY dt.code, d.num_ser, d.num, d.dt, b.bp, b.id_bp, bb.bpb, bb.id_bpb ";
                break;
            case SDataConstantsSys.CFGS_TP_SORT_BIZ_P_DOC:
                sqlOrderBy += "ORDER BY b.bp, b.id_bp, bb.bpb, bb.id_bpb, dt.code, d.num_ser, d.num, d.dt ";
                break;
            default:
                sqlOrderBy += "ORDER BY dt.code, d.num_ser, d.num, d.dt, b.bp, b.id_bp, bb.bpb, bb.id_bpb ";
        }
        
        String sqlSeries = "";
        boolean dnsRight = false; 
        if (mbIsOrd) {
            if (isOrderPur()) {
                dnsRight = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_ORD_DNS).HasRight;
            }
            else if (isOrderSal()) {
                dnsRight = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DOC_ORD_DNS).HasRight;
            }
            if (!dnsRight) {
                ArrayList<SDataUserDnsDps> usrDnsDpss = miClient.getSessionXXX().getUser().getDbmsConfigurationTransaction().getUserDnsDps();
                if (!usrDnsDpss.isEmpty()) {
                    for (SDataUserDnsDps usrDnsDps : usrDnsDpss) {
                        sqlSeries += sqlSeries.isEmpty() ? "(" : "OR ";
                        sqlSeries += "d.num_ser = '" + usrDnsDps.getDocumentNumberSeries().getDocNumberSeries() + "' ";
                    }
                    sqlSeries += ") ";
                }
            }
        }

        msSql = "SELECT d.id_year, d.id_doc, d.dt, d.dt_doc_delivery_n, d.num_ref, d.b_close, d.b_del, d.ts_close, " +
                "CONCAT(d.num_ser, IF(length(d.num_ser) = 0, '', '-'), d.num) AS f_num, " +
                (isViewDocsPending() || isViewDocsRejected() ? (
                "IF(d.aut_authorn_rej = " + SDataDps.AUT_AUTHORN_REJ_LIM_USR + ", '" + SDataDps.AutAuthornRejMap.get(SDataDps.AUT_AUTHORN_REJ_LIM_USR) + "', " +
                "IF(d.aut_authorn_rej = " + SDataDps.AUT_AUTHORN_REJ_LIM_USR_MON + ", '" + SDataDps.AutAuthornRejMap.get(SDataDps.AUT_AUTHORN_REJ_LIM_USR_MON) + "', " +
                "IF(d.aut_authorn_rej = " + SDataDps.AUT_AUTHORN_REJ_LIM_FUNC_MON_ORD + ", '" + SDataDps.AutAuthornRejMap.get(SDataDps.AUT_AUTHORN_REJ_LIM_FUNC_MON_ORD) + "', " +
                "IF(d.aut_authorn_rej = " + SDataDps.AUT_AUTHORN_REJ_LIM_FUNC_MON_DOC + ", '" + SDataDps.AutAuthornRejMap.get(SDataDps.AUT_AUTHORN_REJ_LIM_FUNC_MON_DOC) + "', '" +
                SDataDps.AutAuthornRejMap.get(SDataDps.AUT_AUTHORN_REJ_NA) + "')))) AS _aut_auth_rej, ") : "") + 
                "d.ts_authorn, d.tot_cur_r, dt.code, c.cur_key, " + 
                "b.id_bp, b.bp, bc.bp_key, bb.id_bpb, bb.bpb, cb.code, " +
                "sa.st_dps_authorn, ua.usr, un.usr " +
                "FROM trn_dps AS d " +
                "INNER JOIN erp.trnu_tp_dps AS dt ON " +
                "d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps" + sqlFilter +
                "INNER JOIN erp.trns_st_dps_authorn AS sa ON d.fid_st_dps_authorn = sa.id_st_dps_authorn " +
                "INNER JOIN erp.cfgu_cur AS c ON d.fid_cur = c.id_cur " +
                "INNER JOIN erp.bpsu_bp AS b ON d.fid_bp_r = b.id_bp " +
                "INNER JOIN erp.bpsu_bp_ct AS bc ON b.id_bp = bc.id_bp AND bc.id_ct_bp = " + (isPurchase() ? SDataConstantsSys.BPSS_CT_BP_SUP : SDataConstantsSys.BPSS_CT_BP_CUS) + " " +
                "INNER JOIN erp.bpsu_bpb AS cb ON d.fid_cob = cb.id_bpb " +
                "INNER JOIN erp.bpsu_bpb AS bb ON d.fid_bpb = bb.id_bpb " +
                "INNER JOIN erp.usru_usr AS ua ON d.fid_usr_authorn = ua.id_usr " +
                "INNER JOIN erp.usru_usr AS un ON d.fid_usr_new = un.id_usr " + (mbHasRightAuthor ? " AND d.fid_usr_new = " + miClient.getSession().getUser().getPkUserId() + " " : "") +
                "WHERE NOT d.b_del AND d.fid_st_dps_authorn IN (" +
                (isViewDocsPending() ? "" + SDataConstantsSys.TRNS_ST_DPS_AUTHORN_NA + ", " + SDataConstantsSys.TRNS_ST_DPS_AUTHORN_PENDING :
                    isViewDocsAuthorized() ? "" + SDataConstantsSys.TRNS_ST_DPS_AUTHORN_AUTHORN : "" + SDataConstantsSys.TRNS_ST_DPS_AUTHORN_REJECT) + ") " +
                (sqlDatePeriod.isEmpty() ? "" : "AND " + sqlDatePeriod) +
                (sqlCompanyBranch.isEmpty() ? "" : "AND " + sqlCompanyBranch) +
                (sqlBizPartner.isEmpty() ? "" : "AND " + sqlBizPartner) +
                (sqlDocNature.isEmpty() ? "" : "AND " + sqlDocNature) +
                (sqlFunctionalArea.isEmpty() ? "" : "AND " + sqlFunctionalArea) +
                (sqlSeries.isEmpty() ? "" : "AND " + sqlSeries) +
                sqlOrderBy;
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

    public void publicActionViewDps() {
        actionViewDps();
    }
    
    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (javax.swing.JButton) e.getSource();

            if (button == mjbAuthorize) {
                actionAuthorizeDoc();
            }
            else if (button == mjbReject) {
                actionRejectDoc();
            }
            else if (button == mjbViewDps) {
                actionViewDps();
            }
            else if (button == mjbViewNotes) {
                actionViewNotes();
            }
            else if (button == mjbViewLinks) {
                actionViewLinks();
            }
            else if (button == mjPrint) {
                actionPrint();
            }
        }
    }
}
