 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.view;

import cfd.ver4.DCfdVer4Consts;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
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
import erp.mtrn.data.SCfdUtils;
import erp.mtrn.data.SDataCfd;
import erp.mtrn.data.SDataDps;
import erp.mtrn.data.SDataUserDnsDps;
import erp.mtrn.data.STrnUtilities;
import erp.table.SFilterConstants;
import erp.table.STabFilterBizPartner;
import erp.table.STabFilterCompanyBranch;
import erp.table.STabFilterDnsDps;
import erp.table.STabFilterDocumentNature;
import erp.table.STabFilterFunctionalArea;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 *
 * @author Juan Barajas, Isabel Servín, Sergio Flores
 */
public class SViewDpsSend extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private javax.swing.JButton mjbSendClose;
    private javax.swing.JButton mjbViewDps;
    private javax.swing.JButton mjbViewNotes;
    private javax.swing.JButton mjbViewLinks;
    private javax.swing.JButton mjbSend;
    private erp.lib.table.STabFilterDateCutOff moTabFilterDateCutOff;
    private erp.lib.table.STabFilterDatePeriod moTabFilterDatePeriod;
    private erp.table.STabFilterCompanyBranch moTabFilterCompanyBranch;
    private erp.table.STabFilterBizPartner moTabFilterBizPartner;
    private erp.table.STabFilterDocumentNature moTabFilterDocumentNature;
    private erp.table.STabFilterFunctionalArea moTabFilterFunctionalArea;
    private erp.table.STabFilterDnsDps moTabFilterDnsDps;
    private String msBizPartner;
    private boolean mbIsSortKeyName;

    /**
     * View to send documents.
     * @param client GUI client interface.
     * @param tabTitle View tab title.
     * @param type Constants defined in SDataConstants (TRNX_DPS_SEND_PEND or TRNX_DPS_SENT).
     * @param auxType01 Constants defined in SDataConstantsSys (TRNS_CT_DPS...).
     * @param auxType02 Constants defined in SDataConstantsSys (TRNX_TP_DPS...).
     */
    public SViewDpsSend(erp.client.SClientInterface client, java.lang.String tabTitle, int type, int auxType01, int auxType02) {
        super(client, tabTitle, type, auxType01, auxType02);
        initComponents();
    }

    private void initComponents() {
        int i;
        STableField[] aoKeyFields = null;
        STableColumn[] aoTableColumns = null;

        mjbSendClose = new JButton(miClient.getImageIcon(SLibConstants.ICON_APPROVE));
        mjbViewDps = new JButton(miClient.getImageIcon(SLibConstants.ICON_LOOK));
        mjbViewNotes = new JButton(miClient.getImageIcon(SLibConstants.ICON_NOTES));
        mjbViewLinks = new JButton(miClient.getImageIcon(SLibConstants.ICON_LINK));
        mjbSend = new JButton(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_std_mail.gif")));

        mjbSendClose.setPreferredSize(new Dimension(23, 23));
        mjbViewDps.setPreferredSize(new Dimension(23, 23));
        mjbViewNotes.setPreferredSize(new Dimension(23, 23));
        mjbViewLinks.setPreferredSize(new Dimension(23, 23));
        mjbSend.setPreferredSize(new Dimension(23, 23));

        mjbSendClose.addActionListener(this);
        mjbViewDps.addActionListener(this);
        mjbViewNotes.addActionListener(this);
        mjbViewLinks.addActionListener(this);
        mjbSend.addActionListener(this);

        mjbSendClose.setToolTipText("Marcar como enviado");
        mjbViewDps.setToolTipText("Ver documento");
        mjbViewNotes.setToolTipText("Ver notas del documento");
        mjbViewLinks.setToolTipText("Ver vínculos del documento");
        mjbSend.setToolTipText("Enviar documento");

        if (isDpsSendPending()) {
            moTabFilterDateCutOff = new STabFilterDateCutOff(miClient, this, SLibTimeUtilities.getEndOfYear(miClient.getSessionXXX().getWorkingDate()));
            moTabFilterDatePeriod = null;
        }
        else {
            moTabFilterDateCutOff = null;
            moTabFilterDatePeriod = new STabFilterDatePeriod(miClient, this, SLibConstants.GUI_DATE_AS_YEAR_MONTH);
        }
        
        moTabFilterCompanyBranch = new STabFilterCompanyBranch(miClient, this);
        moTabFilterBizPartner = new STabFilterBizPartner(miClient, this, SDataConstantsSys.BPSS_CT_BP_CUS);
        moTabFilterDocumentNature = new STabFilterDocumentNature(miClient, this, SDataConstants.TRNU_DPS_NAT);
        moTabFilterFunctionalArea = new STabFilterFunctionalArea(miClient, this);
        moTabFilterDnsDps = new STabFilterDnsDps(miClient, this);

        removeTaskBarUpperComponent(jbNew);
        removeTaskBarUpperComponent(jbEdit);
        removeTaskBarUpperComponent(jbDelete);

        addTaskBarUpperComponent(mjbSendClose);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(mjbSend);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(isDpsSendPending() ? moTabFilterDateCutOff : moTabFilterDatePeriod);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterCompanyBranch);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterBizPartner);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(mjbViewDps);
        addTaskBarUpperComponent(mjbViewNotes);
        addTaskBarUpperComponent(mjbViewLinks);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDocumentNature);
        addTaskBarUpperComponent(moTabFilterFunctionalArea);
        addTaskBarUpperComponent(moTabFilterDnsDps);

        mjbSendClose.setEnabled(isDpsSendPending());
        mjbViewDps.setEnabled(true);
        mjbViewNotes.setEnabled(true);
        mjbViewLinks.setEnabled(true);
        mjbSend.setEnabled(true);
        moTabFilterDnsDps.setVisible(mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_DPS_ORD);

        aoKeyFields = new STableField[2];
        aoTableColumns = new STableColumn[12];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_year");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_doc");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_type_code", "Tipo doc.", STableConstants.WIDTH_CODE_DOC);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_num", "Folio doc.", STableConstants.WIDTH_DOC_NUM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_num_ref", "Referencia doc.", STableConstants.WIDTH_DOC_NUM_REF);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "d.dt", "Fecha doc.", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_cob_code", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "d.dt_doc_delivery_n", "Entrega programada", STableConstants.WIDTH_DATE);

        msBizPartner = "";
        mbIsSortKeyName = false;
        
        if (isDpsPurchases()) {
            msBizPartner = "Proveedor";
            mbIsSortKeyName = miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME;
        }
        else {
            msBizPartner = "Cliente";
            mbIsSortKeyName = miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME;
        }
        
        if (mbIsSortKeyName) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bc.bp_key", "Clave " + msBizPartner.toLowerCase(), 50);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bp", msBizPartner, 250);
        }
        else {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bp", msBizPartner, 250);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bc.bp_key", "Clave " + msBizPartner.toLowerCase(), 50);
        }
        
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bb.bpb", "Sucursal " + msBizPartner.toLowerCase(), 75);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "d.tot_cur_r", "Total mon $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "c.cur_key", "Moneda", STableConstants.WIDTH_CURRENCY_KEY);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_count", "Envíos", 50);

        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.BPSU_BP);
        mvSuscriptors.add(SDataConstants.BPSU_BP_CT);
        mvSuscriptors.add(SDataConstants.BPSU_BPB);
        mvSuscriptors.add(SDataConstants.TRN_DPS);
        mvSuscriptors.add(SDataConstants.TRNX_DPS_AUTHORIZE_PEND);
        mvSuscriptors.add(SDataConstants.TRNX_DPS_SEND_PEND);
        mvSuscriptors.add(SDataConstants.TRNX_DPS_SENT);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        getTablePane().setDoubleClickAction(this, "publicActionViewDps");

        populateTable();
    }

    private boolean isDpsSendPending() {
        return mnTabType == SDataConstants.TRNX_DPS_SEND_PEND;
    }
    
    private boolean isDpsPurchases() {
        return mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_PUR;
    }
    
    private boolean isCfd() {
        return !isDpsPurchases() && (mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_DPS_DOC || mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_DPS_ADJ);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void createSqlQuery() {
        String sqlDatePeriod = "";
        String sqlCompanyBranch = "";
        String sqlBizPartner = "";
        String sqlDocNature = "";
        String sqlDocFuncArea = "";
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);

            if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                if (isDpsSendPending()) {
                    sqlDatePeriod = setting.getSetting() == null ? "" : " AND d.dt <= '" + (new java.sql.Date(((java.util.Date) setting.getSetting()).getTime())) + "' ";
                }
                else {
                    sqlDatePeriod = "AND " + SDataSqlUtilities.composePeriodFilter((int[]) setting.getSetting(), "d.dt");
                }
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_COB) {
                sqlCompanyBranch = ((Integer) setting.getSetting() == 0 ? "" : "AND d.fid_cob = " + (Integer) setting.getSetting() + " ");
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_BP) {
                sqlBizPartner = ((Integer) setting.getSetting() == 0 ? "" : "AND d.fid_bp_r = " + (Integer) setting.getSetting() + " ");
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_DOC_NAT) {
                if (((Integer) setting.getSetting()) != 0) {
                    sqlDocNature = ((Integer) setting.getSetting() == 0 ? "" : "AND d.fid_dps_nat = " + (Integer) setting.getSetting() + " ");
                }
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_FUNC_AREA) {
                if (!((String) setting.getSetting()).isEmpty()) {
                    sqlDocFuncArea = " AND d.fid_func IN (" + ((String) setting.getSetting()) + ") ";
                }
            }
        }
        
        String sqlDocSeries = "";
        boolean hasRightAllDns = false;
        
        if (mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_DPS_ORD) {
            if (isDpsPurchases()) {
                hasRightAllDns = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_ORD_ALL_DNS).HasRight;
            }
            else {
                hasRightAllDns = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DOC_ORD_ALL_DNS).HasRight;
            }
            
            if (!hasRightAllDns) {
                ArrayList<SDataUserDnsDps> usrDnsDpss = miClient.getSessionXXX().getUser().getDbmsConfigurationTransaction().getUserDnsDps();
                
                if (!usrDnsDpss.isEmpty()) {
                    for (SDataUserDnsDps usrDnsDps : usrDnsDpss) {
                        sqlDocSeries += sqlDocSeries.isEmpty() ? "(" : "OR ";
                        sqlDocSeries += "d.num_ser = '" + usrDnsDps.getDocumentNumberSeries().getDocNumberSeries() + "' ";
                    }
                    
                    sqlDocSeries += ") ";
                }
            }
        }
        
        int[] dpsTypeKey = null;
        
        switch (mnTabTypeAux02) {
            case SDataConstantsSys.TRNX_TP_DPS_ORD:
                dpsTypeKey = isDpsPurchases() ? SDataConstantsSys.TRNU_TP_DPS_PUR_ORD : SDataConstantsSys.TRNU_TP_DPS_SAL_ORD;
                break;
            case SDataConstantsSys.TRNX_TP_DPS_DOC:
                dpsTypeKey = isDpsPurchases() ? SDataConstantsSys.TRNU_TP_DPS_PUR_INV : SDataConstantsSys.TRNU_TP_DPS_SAL_INV;
                break;
            case SDataConstantsSys.TRNX_TP_DPS_ADJ:
                dpsTypeKey = isDpsPurchases() ? SDataConstantsSys.TRNU_TP_DPS_PUR_CN : SDataConstantsSys.TRNU_TP_DPS_SAL_CN;
                break;
            default:
        }
        
        String sqlDocType = "AND d.fid_ct_dps = " + dpsTypeKey[0] + " AND d.fid_cl_dps = " + dpsTypeKey[1] + " AND d.fid_tp_dps = " + dpsTypeKey[2] + " ";
        String sqlWhere = "NOT d.b_del AND d.fid_st_dps <> " + SDataConstantsSys.TRNS_ST_DPS_ANNULED + " " +
                sqlDatePeriod + sqlCompanyBranch + sqlBizPartner + sqlDocNature + sqlDocFuncArea + sqlDocType;
        
        if (mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_DPS_ORD) {
            sqlWhere += "AND d.b_authorn ";
        }

        msSql = "SELECT d.id_year, d.id_doc, d.dt, d.dt_doc_delivery_n, "
                + "CONCAT(d.num_ser, IF(d.num_ser = '', '', '-'), d.num) AS _num, d.num_ref AS _num_ref, "
                + "d.tot_cur_r, dt.code AS _type_code, cob.code AS _cob_code, c.cur_key, "
                + "b.bp, bc.bp_key, bb.bpb, SUM(COALESCE(xsl.b_snd, 0)) AS _count "
                + "FROM trn_dps AS d "
                + "INNER JOIN erp.trnu_tp_dps AS dt ON dt.id_ct_dps = d.fid_ct_dps AND dt.id_cl_dps = d.fid_cl_dps AND dt.id_tp_dps = d.fid_tp_dps "
                + "INNER JOIN erp.cfgu_cur AS c ON c.id_cur = d.fid_cur "
                + "INNER JOIN erp.bpsu_bp AS b ON b.id_bp = d.fid_bp_r "
                + "INNER JOIN erp.bpsu_bp_ct AS bc ON bc.id_bp = b.id_bp AND bc.id_ct_bp = " + (isDpsPurchases() ? SDataConstantsSys.BPSS_CT_BP_SUP : SDataConstantsSys.BPSS_CT_BP_CUS) + " "
                + "INNER JOIN erp.bpsu_bpb AS bb ON bb.id_bpb = d.fid_bpb "
                + "INNER JOIN erp.bpsu_bpb AS cob ON cob.id_bpb = d.fid_cob ";
        
        if (isCfd()) {
            msSql += "INNER JOIN trn_cfd AS cfd ON cfd.fid_dps_year_n = d.id_year AND cfd.fid_dps_doc_n = d.id_doc AND cfd.fid_st_xml = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " "
                    + "LEFT OUTER JOIN trn_cfd_snd_log AS xsl ON xsl.id_cfd = cfd.id_cfd AND xsl.b_snd ";
        }
        else {
            msSql += "LEFT OUTER JOIN trn_dps_snd_log AS xsl ON xsl.id_year = d.id_year AND xsl.id_doc = d.id_doc AND xsl.b_snd ";
        }
        
        msSql += "WHERE " + sqlWhere
                + "GROUP BY d.id_year, d.id_doc, d.dt, d.dt_doc_delivery_n, "
                + "d.num_ser, d.num, d.num_ref, "
                + "d.tot_cur_r, dt.code, cob.code, c.cur_key, "
                + "b.bp, bc.bp_key, bb.bpb "
                + "HAVING SUM(COALESCE(xsl.b_snd, 0)) " + (isDpsSendPending() ? "=" : "<>") + " 0 "
                + "ORDER BY dt.code, d.num_ser, LPAD(d.num, " + DCfdVer4Consts.LEN_UUID + ", '0'), d.num, d.num_ref, d.dt, d.dt_doc_delivery_n, ";
                
        if (mbIsSortKeyName) {
            msSql += "bc.bp_key, b.bp, ";
        }
        else {
            msSql += "b.bp, bc.bp_key, ";
        }
        
        msSql += "b.id_bp, d.id_year, d.id_doc ";
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

    private void actionSendClose() {
        boolean sendClose = true;
        int gui = (isDpsPurchases() ? SDataConstants.MOD_PUR : SDataConstants.MOD_SAL);

        if (mjbSendClose.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
               if (miClient.showMsgBoxConfirm("¿Está seguro que desea marcar el documento como enviado?") == JOptionPane.YES_OPTION) {
                    try {
                        SDataDps oDps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_VERBOSE);
                        
                        switch (mnTabTypeAux02) {
                            case SDataConstantsSys.TRNX_TP_DPS_DOC:
                            case SDataConstantsSys.TRNX_TP_DPS_ADJ:
                                if (oDps.getDbmsDataCfd() == null) {
                                    miClient.showMsgBoxInformation("No se encontró el archivo XML del documento.");
                                    sendClose = false;
                                }
                                break;
                            case SDataConstantsSys.TRNX_TP_DPS_ORD:
                                break;
                            default:
                        }
                        
                        if (sendClose) {
                            switch (mnTabTypeAux02) {
                                case SDataConstantsSys.TRNX_TP_DPS_DOC:
                                case SDataConstantsSys.TRNX_TP_DPS_ADJ:
                                    if (!SCfdUtils.insertCfdSendLog(miClient, oDps.getDbmsDataCfd(), "N/A", true)) {
                                        sendClose = false;
                                    }
                                    break;
                                case SDataConstantsSys.TRNX_TP_DPS_ORD:
                                    if (!STrnUtilities.insertDpsSendLog(miClient, oDps, "N/A", true)) {
                                        sendClose = false;
                                    }
                                    break;
                                default:
                            }
                        }
                        
                        if (sendClose) {
                            miClient.getGuiModule(gui).refreshCatalogues(mnTabType);
                        }
                        
                    }
                    catch (Exception e) {
                        SLibUtilities.renderException(this, e);
                    }
                }
            }
        }
    }

    private void actionViewDps() {
        int gui = (isDpsPurchases() ? SDataConstants.MOD_PUR : SDataConstants.MOD_SAL);

        if (moTablePane.getSelectedTableRow() != null) {
            miClient.getGuiModule(gui).setFormComplement(SDataConstantsSys.TRNU_TP_DPS_SAL_INV);
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

    private void actionSend() {
        boolean send = false;
        int gui = (isDpsPurchases() ? SDataConstants.MOD_PUR : SDataConstants.MOD_SAL);
        
        if (mjbSend.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                try {
                    switch (mnTabTypeAux02) {
                        case SDataConstantsSys.TRNX_TP_DPS_DOC:
                        case SDataConstantsSys.TRNX_TP_DPS_ADJ:
                            SDataCfd cfd = SCfdUtils.getCfd(miClient, SDataConstantsSys.TRNS_TP_CFD_INV, (int[]) moTablePane.getSelectedTableRow().getPrimaryKey());
                            SCfdUtils.sendCfd(miClient, cfd, 0, true);
                            send = true;
                            break;
                            
                        case SDataConstantsSys.TRNX_TP_DPS_ORD:
                            boolean returnPfdFile = false;
                            STrnUtilities.sendDpsOrder(miClient, (int[]) moTablePane.getSelectedTableRow().getPrimaryKey(), true, returnPfdFile);
                            send = true;
                            break;
                            
                        default:
                            // do nothing
                    }
                    
                    if (send) {
                        miClient.getGuiModule(gui).refreshCatalogues(mnTabType);
                    }
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }
            }
        }
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (javax.swing.JButton) e.getSource();

            if (button == mjbSendClose) {
                actionSendClose();
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
            else if (button == mjbSend) {
                actionSend();
            }
        }
    }
}
