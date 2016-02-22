/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.gui;

import erp.cfd.SCfdConsts;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.gui.mod.cfg.SCfgMenu;
import erp.gui.mod.cfg.SCfgMenuSection;
import erp.gui.mod.cfg.SCfgMenuSectionItem;
import erp.gui.mod.cfg.SCfgMenuSectionSeparator;
import erp.gui.mod.cfg.SCfgModule;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mfin.data.SDataCostCenterItem;
import erp.mfin.form.SDialogRepBizPartnerAccountingMoves;
import erp.mfin.form.SDialogRepBizPartnerBalance;
import erp.mfin.form.SDialogRepBizPartnerBalanceDps;
import erp.mfin.form.SDialogRepBizPartnerBalanceDpsCollection;
import erp.mfin.form.SDialogRepBizPartnerJournal;
import erp.mfin.form.SDialogRepBizPartnerStatements;
import erp.mfin.form.SFormCostCenterItem;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.trn.form.SDialogRepContractStatus;
import erp.mod.trn.form.SDialogSendMailContract;
import erp.mtrn.data.SCfdUtils;
import erp.mtrn.data.SDataBizPartnerBlocking;
import erp.mtrn.data.SDataDiogDncDocumentNumberSeries;
import erp.mtrn.data.SDataDps;
import erp.mtrn.data.SDataDpsDncDocumentNumberSeries;
import erp.mtrn.data.SDataDsm;
import erp.mtrn.data.SDataSign;
import erp.mtrn.form.SDialogRepBizPartnerBalanceAging;
import erp.mtrn.form.SDialogRepContractStock;
import erp.mtrn.form.SDialogRepDpsBizPartner;
import erp.mtrn.form.SDialogRepDpsList;
import erp.mtrn.form.SDialogRepDpsMoves;
import erp.mtrn.form.SDialogRepDpsShipmentItem;
import erp.mtrn.form.SDialogRepDpsWithBalance;
import erp.mtrn.form.SDialogRepSalesPurchases;
import erp.mtrn.form.SDialogRepSalesPurchasesByConcept;
import erp.mtrn.form.SDialogRepSalesPurchasesByLocality;
import erp.mtrn.form.SDialogRepSalesPurchasesComparative;
import erp.mtrn.form.SDialogRepSalesPurchasesDetailByBizPartner;
import erp.mtrn.form.SDialogRepSalesPurchasesFileCsv;
import erp.mtrn.form.SDialogRepSalesPurchasesJournal;
import erp.mtrn.form.SDialogRepSalesPurchasesNet;
import erp.mtrn.form.SDialogRepSalesPurchasesPriceUnitary;
import erp.mtrn.form.SFormBizPartnerBlocking;
import erp.mtrn.form.SFormDncDocumentNumberSeries;
import erp.mtrn.form.SFormDps;
import erp.mtrn.form.SFormDsm;
import erp.mtrn.form.SFormStamp;
import erp.server.SServerConstants;
import erp.server.SServerRequest;
import erp.server.SServerResponse;
import java.util.Date;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import sa.gui.util.SUtilConsts;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiParams;
import sa.lib.srv.SSrvConsts;

/**
 *
 * @author Sergio Flores
 */
public class SGuiModuleTrnSal extends erp.lib.gui.SGuiModule implements java.awt.event.ActionListener {

    private javax.swing.JMenu jmCat;
    private javax.swing.JMenuItem jmiCatDpsDncDocumentNumberSeries;
    private javax.swing.JMenuItem jmiCatDiogDncDocumentNumberSeries;
    private javax.swing.JMenuItem jmiCatBizPartnerBlocking;
    private javax.swing.JMenu jmCatCfg;
    private javax.swing.JMenuItem jmiCatCfgCostCenterItem;
    private javax.swing.JMenu jmCatCfdi;
    private javax.swing.JMenuItem jmiCatCfdiStampAvailable;
    private javax.swing.JMenuItem jmiCatCfdiStampSign;
    private javax.swing.JMenuItem jmiCatCfdiStampSignPending;
    private javax.swing.JMenuItem jmiCatCfdiSendingLog;
    private javax.swing.JMenu jmEst;
    private javax.swing.JMenuItem jmiEstimates;
    private javax.swing.JMenuItem jmiEstimatesLinkPend;
    private javax.swing.JMenuItem jmiEstimatesLinkPendEntry;
    private javax.swing.JMenuItem jmiEstimatesLinked;
    private javax.swing.JMenuItem jmiEstimatesLinkedEntry;
    private javax.swing.JMenuItem jmiEstimatesLinks;
    private javax.swing.JMenu jmCon;
    private javax.swing.JMenuItem jmiContracts;
    private javax.swing.JMenuItem jmiContractsLinkPend;
    private javax.swing.JMenuItem jmiContractsLinkPendEntry;
    private javax.swing.JMenuItem jmiContractsLinked;
    private javax.swing.JMenuItem jmiContractsLinkedEntry;
    private javax.swing.JMenuItem jmiContractsLinks;
    private javax.swing.JMenuItem jmiContractsLinkPendEntryPrice;
    private javax.swing.JMenuItem jmiContractsLinkedEntryPrice;
    private javax.swing.JMenuItem jmiContractsSendMail;
    private javax.swing.JMenu jmOrd;
    private javax.swing.JMenuItem jmiOrders;
    private javax.swing.JMenuItem jmiOrdersLinkPend;
    private javax.swing.JMenuItem jmiOrdersLinkPendEntry;
    private javax.swing.JMenuItem jmiOrdersLinked;
    private javax.swing.JMenuItem jmiOrdersLinkedEntry;
    private javax.swing.JMenuItem jmiOrdersLinksSource;
    private javax.swing.JMenuItem jmiOrdersLinksDestiny;
    private javax.swing.JMenuItem jmiOrdersAutorizedPending;
    private javax.swing.JMenuItem jmiOrdersAutorizedAutorized;
    private javax.swing.JMenuItem jmiOrdersAutorizedRejected;
    private javax.swing.JMenuItem jmiOrdersPrice;
    private javax.swing.JMenuItem jmiOrdersPriceHistory;
    private javax.swing.JMenu jmDps;
    private javax.swing.JMenuItem jmiDpsDoc;
    private javax.swing.JMenuItem jmiDpsEntry;
    private javax.swing.JMenuItem jmiDpsAnnuled;
    private javax.swing.JMenuItem jmiDpsReissued;
    private javax.swing.JMenuItem jmiDpsReplaced;
    private javax.swing.JMenuItem jmiDpsLinksDestiny;
    private javax.swing.JMenuItem jmiDpsLinksTrace;
    private javax.swing.JMenuItem jmiDpsAutorizedPending;
    private javax.swing.JMenuItem jmiDpsAutorizedAutorized;
    private javax.swing.JMenuItem jmiDpsAutorizedReject;
    private javax.swing.JMenuItem jmiDpsAuditPending;
    private javax.swing.JMenuItem jmiDpsAudited;
    private javax.swing.JMenuItem jmiDpsSendPendingEmail;
    private javax.swing.JMenuItem jmiDpsSentEmail;
    private javax.swing.JMenuItem jmiDpsSendPendingWs;
    private javax.swing.JMenuItem jmiDpsApprovedWs;
    private javax.swing.JMenuItem jmiDpsRejectWs;
    private javax.swing.JMenuItem jmiDpsPrice;
    private javax.swing.JMenuItem jmiDpsPriceHistory;
    private javax.swing.JMenu jmDpsAdj;
    private javax.swing.JMenuItem jmiDpsAdjDoc;
    private javax.swing.JMenuItem jmiDpsAdjAnnuled;
    private javax.swing.JMenuItem jmiDpsAdjReissued;
    private javax.swing.JMenuItem jmiDpsAdjReplaced;
    private javax.swing.JMenuItem jmiDpsAdjSendPendingEmail;
    private javax.swing.JMenuItem jmiDpsAdjSentEmail;
    private javax.swing.JMenuItem jmiDpsAdjSendPendingWs;
    private javax.swing.JMenuItem jmiDpsAdjApprovedWs;
    private javax.swing.JMenuItem jmiDpsAdjRejectWs;
    private javax.swing.JMenu jmStkDvy;
    private javax.swing.JMenuItem jmiStkDvyPend;
    private javax.swing.JMenuItem jmiStkDvyPendEntry;
    private javax.swing.JMenuItem jmiStkDvySupplied;
    private javax.swing.JMenuItem jmiStkDvySuppliedEntry;
    private javax.swing.JMenuItem jmiStkDvyDiog;
    private javax.swing.JMenuItem jmiStkDvyStatsConsumption;
    private javax.swing.JMenu jmStkRet;
    private javax.swing.JMenuItem jmiStkRetPending;
    private javax.swing.JMenuItem jmiStkRetPendingEntry;
    private javax.swing.JMenuItem jmiStkRetReturned;
    private javax.swing.JMenuItem jmiStkRetReturnedEntry;
    private javax.swing.JMenuItem jmiStkRetDiog;
    private javax.swing.JMenu jmRep;
    private javax.swing.JMenu jmRepStats;
    private javax.swing.JMenu jmRepBackorder;
    private javax.swing.JMenuItem jmiRepTrnGlobal;
    private javax.swing.JMenuItem jmiRepTrnByMonth;
    private javax.swing.JMenuItem jmiRepTrnByItemGeneric;
    private javax.swing.JMenuItem jmiRepTrnByItemGenericBizPartner;
    private javax.swing.JMenuItem jmiRepTrnByItem;
    private javax.swing.JMenuItem jmiRepTrnByItemBizPartner;
    private javax.swing.JMenuItem jmiRepTrnByBizPartner;
    private javax.swing.JMenuItem jmiRepTrnByBizPartnerItem;
    private javax.swing.JMenuItem jmiRepTrnByBizPartnerType;
    private javax.swing.JMenuItem jmiRepTrnByBizPartnerTypeBizPartner;
    private javax.swing.JMenuItem jmiRepTrnDpsByItemBizPartner;
    private javax.swing.JMenuItem jmiRepBackorderContract;
    private javax.swing.JMenuItem jmiRepBackorderContractByItem;
    private javax.swing.JMenuItem jmiRepBackorderContractByItemBizPartner;
    private javax.swing.JMenuItem jmiRepBackorderContractByItemBizPartnerBra;
    private javax.swing.JMenuItem jmiRepBackorderOrder;
    private javax.swing.JMenuItem jmiRepBackorderOrderByItem;
    private javax.swing.JMenuItem jmiRepBackorderOrderByItemBizPartner;
    private javax.swing.JMenuItem jmiRepBackorderOrderByItemBizPartnerBra;
    private javax.swing.JMenuItem jmiRepBizPartnerBalanceAgingView;
    private javax.swing.JMenuItem jmiRepBizPartnerBalance;
    private javax.swing.JMenuItem jmiRepBizPartnerBalanceDps;
    private javax.swing.JMenuItem jmiRepBizPartnerBalanceAging;
    private javax.swing.JMenuItem jmiRepBizPartnerBalanceCollection;
    private javax.swing.JMenuItem jmiRepBizPartnerBalanceCollectionDps;
    private javax.swing.JMenuItem jmiRepAccountStatements;
    private javax.swing.JMenuItem jmiRepAccountStatementsDps;
    private javax.swing.JMenuItem jmiRepBizPartnerJournal;
    private javax.swing.JMenuItem jmiRepBizPartnerAccountingMoves;
    private javax.swing.JMenuItem jmiRepBizPartnerAccountingMovesDays;
    private javax.swing.JMenuItem jmiRepDpsList;
    private javax.swing.JMenuItem jmiRepDpsBizPartner;
    private javax.swing.JMenuItem jmiRepDpsWithBalance;
    private javax.swing.JMenuItem jmiRepTrn;
    private javax.swing.JMenuItem jmiRepTrnConcept;
    private javax.swing.JMenuItem jmiRepTrnLocality;
    private javax.swing.JMenuItem jmiRepTrnComparative;
    private javax.swing.JMenuItem jmiRepTrnDpsDetailBizPartner;
    private javax.swing.JMenuItem jmiRepTrnNetTotal;
    private javax.swing.JMenuItem jmiRepTrnNetAnalytic;
    private javax.swing.JMenuItem jmiRepTrnFileCsv;
    private javax.swing.JMenuItem jmiRepTrnJournal;
    private javax.swing.JMenuItem jmiRepTrnItemUnitaryPrice;
    private javax.swing.JSeparator jsRepTrn;
    private javax.swing.JMenuItem jmiRepTrnContractStatus;
    private javax.swing.JMenuItem jmiRepTrnContractBackorderStock;
    private javax.swing.JSeparator jsRepContract;
    private javax.swing.JMenuItem jmiRepTrnShipmentItem;
    private javax.swing.JSeparator jsRepTrnShipment;
    private javax.swing.JMenuItem jmiRepMoneyIn;

    private erp.mtrn.form.SFormDps moFormDps;
    private erp.mtrn.form.SFormDps moFormDpsRo;
    private erp.mtrn.form.SFormDsm moFormDsm;
    private erp.mtrn.form.SFormBizPartnerBlocking moFormBizPartnerBlocking;
    private erp.mtrn.form.SFormDncDocumentNumberSeries moFormDncDocumentNumberSeriesDps;
    private erp.mtrn.form.SFormDncDocumentNumberSeries moFormDncDocumentNumberSeriesDiog;
    private erp.mfin.form.SFormCostCenterItem moFormCostCenterItem;
    private erp.mtrn.form.SDialogRepDpsList moDialogRepDpsList;
    private erp.mtrn.form.SDialogRepDpsBizPartner moDialogRepDpsBizPartner;
    private erp.mtrn.form.SDialogRepDpsWithBalance moDialogRepDpsWithBalance;
    private erp.mtrn.form.SDialogRepSalesPurchases moDialogRepSalesPurchases;
    private erp.mtrn.form.SDialogRepSalesPurchasesByConcept moDialogRepSalesPurchasesByConcept;
    private erp.mtrn.form.SDialogRepSalesPurchasesByLocality moDialogRepSalesPurchasesByLocality;
    private erp.mtrn.form.SDialogRepSalesPurchasesComparative moDialogRepSalesPurchasesComparative;
    private erp.mtrn.form.SDialogRepSalesPurchasesDetailByBizPartner moDialogRepSalesPurchasesDetailByBizPartner;
    private erp.mtrn.form.SDialogRepSalesPurchasesNet moDialogRepSalesPurchasesNet;
    private erp.mtrn.form.SDialogRepSalesPurchasesFileCsv moDialogRepSalesPurchasesFileCsv;
    private erp.mtrn.form.SDialogRepSalesPurchasesJournal moDialogRepSalesPurchasesJournal;
    private erp.mtrn.form.SDialogRepSalesPurchasesPriceUnitary moDialogRepSalesPurchasesItemUnitaryPrice;
    private erp.mtrn.form.SDialogRepContractStock moDialogRepContractStock;
    private erp.mtrn.form.SDialogRepDpsShipmentItem moDialogRepDpsShipmentItem;
    private erp.mtrn.form.SDialogRepDpsMoves moDialogRepDpsMoves;
    private erp.mtrn.form.SFormStamp moFormStamp;

    public SGuiModuleTrnSal(erp.client.SClientInterface client) {
        super(client, SDataConstants.MOD_SAL);
        initComponents();
    }

    private void initComponents() {
        boolean hasRightDnsDps = false;
        boolean hasRightDnsDiog = false;
        boolean hasRightDocEstimate = false;
        boolean hasRightDocOrder = false;
        boolean hasRightDocOrderAuthorize = false;
        boolean hasRightDocTransaction = false;
        boolean hasRightDocTransactionAdjust = false;
        boolean hasRightBizPartnerBlocking = false;
        boolean hasRightInventoryIn = false;
        boolean hasRightInventoryOut = false;
        boolean hasRightReports = false;
        boolean hasRightItemConfig = false;
        int levelRightDocOrder = SDataConstantsSys.UNDEFINED;
        int levelRightDocTransaction = SDataConstantsSys.UNDEFINED;

        jmCat = new JMenu("Catálogos");
        jmiCatDpsDncDocumentNumberSeries = new JMenuItem("Folios de docs. de ventas");
        jmiCatDiogDncDocumentNumberSeries = new JMenuItem("Folios de docs. de inventarios");
        jmiCatBizPartnerBlocking = new JMenuItem("Bloqueo de clientes");
        jmCatCfg = new JMenu("Contabilización automática");
        jmiCatCfgCostCenterItem = new JMenuItem("Configuración de centros de costo vs. ítems");
        jmCatCfg.add(jmiCatCfgCostCenterItem);
        jmCat.add(jmiCatDpsDncDocumentNumberSeries);
        jmCat.add(jmiCatDiogDncDocumentNumberSeries);
        jmCat.addSeparator();
        jmCat.add(jmiCatBizPartnerBlocking);
        jmCat.addSeparator();
        jmCat.add(jmCatCfg);
        jmCatCfdi = new JMenu("Comprobantes fiscales digitales");
        jmiCatCfdiStampAvailable = new JMenuItem("Timbres disponibles");
        jmiCatCfdiStampSign = new JMenuItem("CFDI timbrados");
        jmiCatCfdiStampSignPending = new JMenuItem("CFDI por timbrar");
        jmiCatCfdiSendingLog = new JMenuItem("Bitácora de envíos de CFDI");
        jmCatCfdi.add(jmiCatCfdiStampAvailable);
        jmCatCfdi.add(jmiCatCfdiStampSign);
        jmCatCfdi.add(jmiCatCfdiStampSignPending);
        jmCatCfdi.addSeparator();
        jmCatCfdi.add(jmiCatCfdiSendingLog);
        jmCat.add(jmCatCfdi);

        jmEst = new JMenu("Cotizaciones");
        jmiEstimates = new JMenuItem("Cotizaciones de ventas");
        jmiEstimatesLinkPend = new JMenuItem("Cotizaciones por procesar");
        jmiEstimatesLinkPendEntry = new JMenuItem("Cotizaciones por procesar a detalle");
        jmiEstimatesLinked = new JMenuItem("Cotizaciones procesadas");
        jmiEstimatesLinkedEntry = new JMenuItem("Cotizaciones procesadas a detalle");
        jmiEstimatesLinks = new JMenuItem("Vínculos de cotizaciones como origen");
        jmEst.add(jmiEstimates);
        jmEst.addSeparator();
        jmEst.add(jmiEstimatesLinkPend);
        jmEst.add(jmiEstimatesLinkPendEntry);
        jmEst.addSeparator();
        jmEst.add(jmiEstimatesLinked);
        jmEst.add(jmiEstimatesLinkedEntry);
        jmEst.addSeparator();
        jmEst.add(jmiEstimatesLinks);

        jmCon = new JMenu("Contratos");
        jmiContracts = new JMenuItem("Contratos de ventas");
        jmiContractsLinkPend = new JMenuItem("Contratos por procesar");
        jmiContractsLinkPendEntry = new JMenuItem("Contratos por procesar a detalle");
        jmiContractsLinked = new JMenuItem("Contratos procesados");
        jmiContractsLinkedEntry = new JMenuItem("Contratos procesados a detalle");
        jmiContractsLinks = new JMenuItem("Vínculos de contratos como origen");
        jmiContractsLinkPendEntryPrice = new JMenuItem("Entregas mensuales de contratos por procesar");
        jmiContractsLinkedEntryPrice = new JMenuItem("Entregas mensuales de contratos procesados");
        jmiContractsSendMail = new JMenuItem("Envío de contratos por email");
        jmCon.add(jmiContracts);
        jmCon.addSeparator();
        jmCon.add(jmiContractsLinkPend);
        jmCon.add(jmiContractsLinkPendEntry);
        jmCon.addSeparator();
        jmCon.add(jmiContractsLinked);
        jmCon.add(jmiContractsLinkedEntry);
        jmCon.addSeparator();
        jmCon.add(jmiContractsLinks);
        jmCon.addSeparator();
        jmCon.add(jmiContractsLinkPendEntryPrice);
        jmCon.add(jmiContractsLinkedEntryPrice);
        jmCon.addSeparator();
        jmCon.add(jmiContractsSendMail);

        jmOrd = new JMenu("Pedidos");
        jmiOrders = new JMenuItem("Pedidos de ventas");
        jmiOrdersLinkPend = new JMenuItem("Pedidos por procesar");
        jmiOrdersLinkPendEntry = new JMenuItem("Pedidos por procesar a detalle");
        jmiOrdersLinked = new JMenuItem("Pedidos procesados");
        jmiOrdersLinkedEntry = new JMenuItem("Pedidos procesados a detalle");
        jmiOrdersLinksSource = new JMenuItem("Vínculos de pedidos como origen");
        jmiOrdersLinksDestiny = new JMenuItem("Vínculos de pedidos como destino");
        jmiOrdersAutorizedPending = new JMenuItem("Pedidos por autorizar");
        jmiOrdersAutorizedAutorized = new JMenuItem("Pedidos autorizados");
        jmiOrdersAutorizedRejected = new JMenuItem("Pedidos rechazados");
        jmiOrdersPriceHistory = new JMenuItem("Historial de precios de ventas");
        jmiOrdersPrice = new JMenuItem("Precios de ventas");
        jmOrd.add(jmiOrders);
        jmOrd.addSeparator();
        jmOrd.add(jmiOrdersLinkPend);
        jmOrd.add(jmiOrdersLinkPendEntry);
        jmOrd.addSeparator();
        jmOrd.add(jmiOrdersLinked);
        jmOrd.add(jmiOrdersLinkedEntry);
        jmOrd.addSeparator();
        jmOrd.add(jmiOrdersLinksSource);
        jmOrd.add(jmiOrdersLinksDestiny);
        jmOrd.addSeparator();
        jmOrd.add(jmiOrdersAutorizedPending);
        jmOrd.add(jmiOrdersAutorizedAutorized);
        jmOrd.add(jmiOrdersAutorizedRejected);
        jmOrd.addSeparator();
        jmOrd.add(jmiOrdersPriceHistory);
        jmOrd.add(jmiOrdersPrice);

        jmDps = new JMenu("Facturas");
        jmiDpsDoc = new JMenuItem("Facturas de ventas");
        jmiDpsEntry = new JMenuItem("Facturas de ventas a detalle");
        jmiDpsAnnuled = new JMenuItem("Anulación de facturas");
        jmiDpsReissued = new JMenuItem("Reimpresión de facturas");
        jmiDpsReplaced = new JMenuItem("Sustitución de facturas");
        jmiDpsLinksDestiny = new JMenuItem("Vínculos de facturas como destino");
        jmiDpsLinksTrace = new JMenuItem("Rastreo de vínculos de facturas");
        jmiDpsAutorizedPending = new JMenuItem("Facturas por autorizar");
        jmiDpsAutorizedAutorized = new JMenuItem("Facturas autorizadas");
        jmiDpsAutorizedReject = new JMenuItem("Facturas rechazadas");
        jmiDpsAuditPending = new JMenuItem("Facturas por auditar");
        jmiDpsAudited = new JMenuItem("Facturas auditadas");
        jmiDpsSendPendingEmail = new JMenuItem("Facturas por enviar por correo-e");
        jmiDpsSentEmail = new JMenuItem("Facturas enviadas por correo-e");
        jmiDpsSendPendingWs = new JMenuItem("Facturas por enviar por web-service");
        jmiDpsApprovedWs = new JMenuItem("Facturas aceptadas por web-service");
        jmiDpsRejectWs = new JMenuItem("Facturas rechazadas por web-service");
        jmiDpsPriceHistory = new JMenuItem("Historial de precios de ventas");
        jmiDpsPrice = new JMenuItem("Precios de ventas");
        jmDps.add(jmiDpsDoc);
        jmDps.add(jmiDpsEntry);
        jmDps.addSeparator();
        jmDps.add(jmiDpsAnnuled);
        jmDps.add(jmiDpsReissued);
        jmDps.add(jmiDpsReplaced);
        jmDps.addSeparator();
        jmDps.add(jmiDpsLinksDestiny);
        jmDps.add(jmiDpsLinksTrace);
        jmDps.addSeparator();
        jmDps.add(jmiDpsAutorizedPending);
        jmDps.add(jmiDpsAutorizedAutorized);
        jmDps.add(jmiDpsAutorizedReject);
        jmDps.addSeparator();
        jmDps.add(jmiDpsAuditPending);
        jmDps.add(jmiDpsAudited);
        jmDps.addSeparator();
        jmDps.add(jmiDpsSendPendingEmail);
        jmDps.add(jmiDpsSentEmail);
        jmDps.addSeparator();
        jmDps.add(jmiDpsSendPendingWs);
        jmDps.add(jmiDpsApprovedWs);
        jmDps.add(jmiDpsRejectWs);
        jmDps.addSeparator();
        jmDps.add(jmiDpsPriceHistory);

        jmDpsAdj = new JMenu("Notas crédito");
        jmiDpsAdjDoc = new JMenuItem("Notas de crédito de ventas");
        jmiDpsAdjAnnuled = new JMenuItem("Anulación de notas de crédito");
        jmiDpsAdjReissued = new JMenuItem("Reimpresión de notas de crédito");
        jmiDpsAdjReplaced = new JMenuItem("Sustitución de notas de crédito");
        jmiDpsAdjSendPendingEmail = new JMenuItem("Notas de crédito por enviar por correo-e");
        jmiDpsAdjSentEmail = new JMenuItem("Notas de crédito enviadas por correo-e");
        jmiDpsAdjSendPendingWs = new JMenuItem("Notas de crédito por enviar por web-service");
        jmiDpsAdjApprovedWs = new JMenuItem("Notas de crédito aceptadas por web-service");
        jmiDpsAdjRejectWs = new JMenuItem("Notas de crédito rechazadas por web-service");
        jmDpsAdj.add(jmiDpsAdjDoc);
        jmDpsAdj.addSeparator();
        jmDpsAdj.add(jmiDpsAdjAnnuled);
        jmDpsAdj.add(jmiDpsAdjReissued);
        jmDpsAdj.add(jmiDpsAdjReplaced);
        jmDpsAdj.addSeparator();
        jmDpsAdj.add(jmiDpsAdjSendPendingEmail);
        jmDpsAdj.add(jmiDpsAdjSentEmail);
        jmDpsAdj.addSeparator();
        jmDpsAdj.add(jmiDpsAdjSendPendingWs);
        jmDpsAdj.add(jmiDpsAdjApprovedWs);
        jmDpsAdj.add(jmiDpsAdjRejectWs);

        jmStkDvy = new JMenu("Surtidos");
        jmiStkDvyPend = new JMenuItem("Ventas por surtir");
        jmiStkDvyPendEntry = new JMenuItem("Ventas por surtir a detalle");
        jmiStkDvySupplied = new JMenuItem("Ventas surtidas");
        jmiStkDvySuppliedEntry = new JMenuItem("Ventas surtidas a detalle");
        jmiStkDvyDiog = new JMenuItem("Documentos de surtidos de ventas");
        jmiStkDvyStatsConsumption = new JMenuItem("Estadísticas de consumo de ventas");
        jmStkDvy.add(jmiStkDvyPend);
        jmStkDvy.add(jmiStkDvyPendEntry);
        jmStkDvy.addSeparator();
        jmStkDvy.add(jmiStkDvySupplied);
        jmStkDvy.add(jmiStkDvySuppliedEntry);
        jmStkDvy.addSeparator();
        jmStkDvy.add(jmiStkDvyDiog);
        jmStkDvy.addSeparator();
        jmStkDvy.add(jmiStkDvyStatsConsumption);

        jmStkRet = new JMenu("Devoluciones");
        jmiStkRetPending = new JMenuItem("Ventas por devolver");
        jmiStkRetPendingEntry = new JMenuItem("Ventas por devolver a detalle");
        jmiStkRetReturned = new JMenuItem("Ventas devueltas");
        jmiStkRetReturnedEntry = new JMenuItem("Ventas devueltas a detalle");
        jmiStkRetDiog = new JMenuItem("Documentos de devoluciones de ventas");
        jmStkRet.add(jmiStkRetPending);
        jmStkRet.add(jmiStkRetPendingEntry);
        jmStkRet.addSeparator();
        jmStkRet.add(jmiStkRetReturned);
        jmStkRet.add(jmiStkRetReturnedEntry);
        jmStkRet.addSeparator();
        jmStkRet.add(jmiStkRetDiog);

        jmRep = new JMenu("Reportes");
        jmRepStats = new JMenu("Consultas de estadísticas de ventas");
        jmiRepTrnGlobal = new JMenuItem("Ventas globales");
        jmiRepTrnByMonth = new JMenuItem("Ventas globales por mes");
        jmiRepTrnByItemGeneric = new JMenuItem("Ventas por ítem genérico");
        jmiRepTrnByItemGenericBizPartner = new JMenuItem("Ventas por ítem genérico-cliente");
        jmiRepTrnByItem = new JMenuItem("Ventas por ítem");
        jmiRepTrnByItemBizPartner = new JMenuItem("Ventas por ítem-cliente");
        jmiRepTrnByBizPartner = new JMenuItem("Ventas por cliente");
        jmiRepTrnByBizPartnerItem = new JMenuItem("Ventas por cliente-ítem");
        jmiRepTrnByBizPartnerType = new JMenuItem("Ventas por tipo de cliente");
        jmiRepTrnByBizPartnerTypeBizPartner = new JMenuItem("Ventas por tipo de cliente-cliente");
        jmiRepTrnDpsByItemBizPartner = new JMenuItem("Documentos de ventas por ítem-cliente");
        jmRepBackorder = new JMenu("Consultas de backorder de ventas");
        jmiRepBackorderContract = new JMenuItem("Backorder de contratos");
        jmiRepBackorderContractByItem = new JMenuItem("Backorder de contratos por ítem");
        jmiRepBackorderContractByItemBizPartner = new JMenuItem("Backorder de contratos por ítem-cliente");
        jmiRepBackorderContractByItemBizPartnerBra = new JMenuItem("Backorder de contratos por ítem-cliente sucursal");
        jmiRepBackorderOrder = new JMenuItem("Backorder de pedidos");
        jmiRepBackorderOrderByItem = new JMenuItem("Backorder de pedidos por ítem");
        jmiRepBackorderOrderByItemBizPartner = new JMenuItem("Backorder de pedidos por ítem-cliente");
        jmiRepBackorderOrderByItemBizPartnerBra = new JMenuItem("Backorder de pedidos por ítem-cliente sucursal");
        jmiRepBizPartnerBalanceAgingView = new JMenuItem("Consulta de antigüedad de saldos de clientes");
        jmiRepBizPartnerBalance = new JMenuItem("Reporte de saldos clientes...");
        jmiRepBizPartnerBalanceDps = new JMenuItem("Reporte de saldos clientes por documento...");
        jmiRepBizPartnerBalanceAging = new JMenuItem("Reporte de antigüedad de saldos de clientes...");
        jmiRepBizPartnerBalanceCollection = new JMenuItem("Reporte de pagos esperados...");
        jmiRepBizPartnerBalanceCollectionDps = new JMenuItem("Reporte de pagos esperados por documento...");
        jmiRepAccountStatements = new JMenuItem("Estados de cuenta de clientes...");
        jmiRepAccountStatementsDps = new JMenuItem("Estados de cuenta de clientes por documento...");
        jmiRepBizPartnerJournal = new JMenuItem("Reporte auxiliar de movimientos contables de clientes...");
        jmiRepBizPartnerAccountingMoves = new JMenuItem("Movimientos contables de clientes...");
        jmiRepBizPartnerAccountingMovesDays = new JMenuItem("Movimientos contables de clientes con días de pago...");
        jmiRepDpsList = new JMenuItem("Listado de facturas por período...");
        jmiRepDpsBizPartner = new JMenuItem("Reporte de facturas de clientes...");
        jmiRepDpsWithBalance = new JMenuItem("Reporte de facturas con saldo de clientes...");
        jmiRepTrn = new JMenuItem("Reporte de ventas netas...");
        jmiRepTrnConcept = new JMenuItem("Reporte de ventas netas por concepto...");
        jmiRepTrnLocality = new JMenuItem("Reporte de ventas netas por zona geográfica...");
        jmiRepTrnComparative = new JMenuItem("Reporte comparativo de ventas netas...");
        jmiRepTrnDpsDetailBizPartner = new JMenuItem("Reporte detallado de ventas por cliente...");
        jmiRepTrnNetTotal = new JMenuItem("Relación de ventas netas por período...");
        jmiRepTrnNetAnalytic = new JMenuItem("Relación de ventas, devoluciones y descuentos por período...");
        jmiRepTrnFileCsv = new JMenuItem("Archivo CSV de ventas netas por período...");
        jmiRepTrnJournal = new JMenuItem("Reporte de diario de ventas...");
        jmiRepTrnItemUnitaryPrice = new JMenuItem("Reporte de precios unitarios de ventas...");
        jsRepTrn = new JPopupMenu.Separator();
        jmiRepTrnContractStatus = new JMenuItem("Reporte de estatus de contratos de ventas...");
        jmiRepTrnContractBackorderStock = new JMenuItem("Reporte de backorder contratos de ventas vs. existencias...");
        jsRepContract = new JPopupMenu.Separator();
        jmiRepTrnShipmentItem = new JMenuItem("Reporte de embarque y surtido de ítems...");
        jsRepTrnShipment = new JPopupMenu.Separator();
        jmiRepMoneyIn = new JMenuItem("Reporte de documentos e ingresos del ejercicio...");

        jmRepStats.add(jmiRepTrnGlobal);
        jmRepStats.add(jmiRepTrnByMonth);
        jmRepStats.add(jmiRepTrnByItemGeneric);
        jmRepStats.add(jmiRepTrnByItemGenericBizPartner);
        jmRepStats.add(jmiRepTrnByItem);
        jmRepStats.add(jmiRepTrnByItemBizPartner);
        jmRepStats.add(jmiRepTrnByBizPartner);
        jmRepStats.add(jmiRepTrnByBizPartnerItem);
        jmRepStats.add(jmiRepTrnByBizPartnerType);
        jmRepStats.add(jmiRepTrnByBizPartnerTypeBizPartner);
        jmRepStats.addSeparator();
        jmRepStats.add(jmiRepTrnDpsByItemBizPartner);
        jmRepBackorder.add(jmiRepBackorderContract);
        jmRepBackorder.add(jmiRepBackorderContractByItem);
        jmRepBackorder.add(jmiRepBackorderContractByItemBizPartner);
        jmRepBackorder.add(jmiRepBackorderContractByItemBizPartnerBra);
        jmRepBackorder.addSeparator();
        jmRepBackorder.add(jmiRepBackorderOrder);
        jmRepBackorder.add(jmiRepBackorderOrderByItem);
        jmRepBackorder.add(jmiRepBackorderOrderByItemBizPartner);
        jmRepBackorder.add(jmiRepBackorderOrderByItemBizPartnerBra);

        jmRep.add(jmRepStats);
        jmRep.add(jmRepBackorder);
        jmRep.add(jmiRepBizPartnerBalanceAgingView);
        jmRep.addSeparator();
        jmRep.add(jmiRepBizPartnerBalance);
        jmRep.add(jmiRepBizPartnerBalanceDps);
        jmRep.add(jmiRepBizPartnerBalanceAging);
        jmRep.addSeparator();
        jmRep.add(jmiRepBizPartnerBalanceCollection);
        jmRep.add(jmiRepBizPartnerBalanceCollectionDps);
        jmRep.addSeparator();
        /*
        jmRep.add(jmiRepAccountStatements);
        jmRep.add(jmiRepAccountStatementsDps);
        jmRep.add(jmiRepBizPartnerJournal);
        jmRep.addSeparator();
        */
        jmRep.add(jmiRepBizPartnerAccountingMoves);
        jmRep.add(jmiRepBizPartnerAccountingMovesDays);
        jmRep.addSeparator();
        jmRep.add(jmiRepDpsList);
        jmRep.add(jmiRepDpsBizPartner);
        jmRep.add(jmiRepDpsWithBalance);
        jmRep.addSeparator();
        jmRep.add(jmiRepTrn);
        jmRep.add(jmiRepTrnConcept);
        jmRep.add(jmiRepTrnLocality);
        jmRep.add(jmiRepTrnComparative);
        jmRep.add(jmiRepTrnDpsDetailBizPartner);
        jmRep.addSeparator();
        jmRep.add(jmiRepTrnNetTotal);
        jmRep.add(jmiRepTrnNetAnalytic);
        jmRep.addSeparator();
        jmRep.add(jmiRepTrnFileCsv);
        jmRep.addSeparator();
        jmRep.add(jmiRepTrnJournal);
        jmRep.add(jmiRepTrnItemUnitaryPrice);
        jmRep.add(jsRepTrn); // separator
        jmRep.add(jmiRepTrnContractStatus);
        jmRep.add(jmiRepTrnContractBackorderStock);
        jmRep.add(jsRepContract); // separator
        jmRep.add(jmiRepTrnShipmentItem);
        jmRep.add(jsRepTrnShipment); // separator
        jmRep.add(jmiRepMoneyIn);

        moDialogRepDpsList = new SDialogRepDpsList(miClient);
        moDialogRepDpsBizPartner = new SDialogRepDpsBizPartner(miClient);
        moDialogRepDpsWithBalance = new SDialogRepDpsWithBalance(miClient);
        moDialogRepSalesPurchases = new SDialogRepSalesPurchases(miClient);
        moDialogRepSalesPurchasesByConcept = new SDialogRepSalesPurchasesByConcept(miClient);
        moDialogRepSalesPurchasesByLocality = new SDialogRepSalesPurchasesByLocality(miClient);
        moDialogRepSalesPurchasesComparative = new SDialogRepSalesPurchasesComparative(miClient, SDataConstantsSys.TRNS_CT_DPS_SAL);
        moDialogRepSalesPurchasesDetailByBizPartner = new SDialogRepSalesPurchasesDetailByBizPartner(miClient);
        moDialogRepSalesPurchasesNet = new SDialogRepSalesPurchasesNet(miClient);
        moDialogRepSalesPurchasesFileCsv = new SDialogRepSalesPurchasesFileCsv(miClient, SDataConstantsSys.TRNS_CT_DPS_SAL);
        moDialogRepSalesPurchasesJournal = new SDialogRepSalesPurchasesJournal(miClient);
        moDialogRepSalesPurchasesItemUnitaryPrice = new SDialogRepSalesPurchasesPriceUnitary(miClient);
        moDialogRepContractStock = new SDialogRepContractStock(miClient);
        moDialogRepDpsShipmentItem = new SDialogRepDpsShipmentItem(miClient);
        moDialogRepDpsMoves = new SDialogRepDpsMoves(miClient);

        jmiCatDpsDncDocumentNumberSeries.addActionListener(this);
        jmiCatDiogDncDocumentNumberSeries.addActionListener(this);
        jmiCatBizPartnerBlocking.addActionListener(this);
        jmiCatCfgCostCenterItem.addActionListener(this);
        jmiCatCfdiStampAvailable.addActionListener(this);
        jmiCatCfdiStampSign.addActionListener(this);
        jmiCatCfdiStampSignPending.addActionListener(this);
        jmiCatCfdiSendingLog.addActionListener(this);
        jmiEstimates.addActionListener(this);
        jmiEstimatesLinkPend.addActionListener(this);
        jmiEstimatesLinked.addActionListener(this);
        jmiEstimatesLinkPendEntry.addActionListener(this);
        jmiEstimatesLinkedEntry.addActionListener(this);
        jmiEstimatesLinks.addActionListener(this);
        jmiContracts.addActionListener(this);
        jmiContractsLinkPend.addActionListener(this);
        jmiContractsLinked.addActionListener(this);
        jmiContractsLinkPendEntry.addActionListener(this);
        jmiContractsLinkedEntry.addActionListener(this);
        jmiContractsLinks.addActionListener(this);
        jmiContractsLinkPendEntryPrice.addActionListener(this);
        jmiContractsLinkedEntryPrice.addActionListener(this);
        jmiContractsSendMail.addActionListener(this);
        jmiOrders.addActionListener(this);
        jmiOrdersLinkPend.addActionListener(this);
        jmiOrdersLinked.addActionListener(this);
        jmiOrdersLinkPendEntry.addActionListener(this);
        jmiOrdersLinkedEntry.addActionListener(this);
        jmiOrdersLinksSource.addActionListener(this);
        jmiOrdersLinksDestiny.addActionListener(this);
        jmiOrdersAutorizedPending.addActionListener(this);
        jmiOrdersAutorizedAutorized.addActionListener(this);
        jmiOrdersAutorizedRejected.addActionListener(this);
        jmiOrdersPriceHistory.addActionListener(this);
        jmiOrdersPrice.addActionListener(this);
        jmiDpsDoc.addActionListener(this);
        jmiDpsEntry.addActionListener(this);
        jmiDpsAnnuled.addActionListener(this);
        jmiDpsReissued.addActionListener(this);
        jmiDpsReplaced.addActionListener(this);
        jmiDpsLinksDestiny.addActionListener(this);
        jmiDpsLinksTrace.addActionListener(this);
        jmiDpsAutorizedPending.addActionListener(this);
        jmiDpsAutorizedAutorized.addActionListener(this);
        jmiDpsAutorizedReject.addActionListener(this);
        jmiDpsAuditPending.addActionListener(this);
        jmiDpsAudited.addActionListener(this);
        jmiDpsSendPendingEmail.addActionListener(this);
        jmiDpsSentEmail.addActionListener(this);
        jmiDpsSendPendingWs.addActionListener(this);
        jmiDpsApprovedWs.addActionListener(this);
        jmiDpsRejectWs.addActionListener(this);
        jmiDpsPriceHistory.addActionListener(this);
        jmiDpsPrice.addActionListener(this);
        jmiDpsAdjDoc.addActionListener(this);
        jmiDpsAdjAnnuled.addActionListener(this);
        jmiDpsAdjReissued.addActionListener(this);
        jmiDpsAdjReplaced.addActionListener(this);
        jmiDpsAdjSendPendingEmail.addActionListener(this);
        jmiDpsAdjSentEmail.addActionListener(this);
        jmiDpsAdjSendPendingWs.addActionListener(this);
        jmiDpsAdjApprovedWs.addActionListener(this);
        jmiDpsAdjRejectWs.addActionListener(this);
        jmiStkDvyPend.addActionListener(this);
        jmiStkDvySupplied.addActionListener(this);
        jmiStkDvyPendEntry.addActionListener(this);
        jmiStkDvySuppliedEntry.addActionListener(this);
        jmiStkDvyDiog.addActionListener(this);
        jmiStkDvyStatsConsumption.addActionListener(this);
        jmiStkRetPending.addActionListener(this);
        jmiStkRetPendingEntry.addActionListener(this);
        jmiStkRetReturned.addActionListener(this);
        jmiStkRetReturnedEntry.addActionListener(this);
        jmiStkRetDiog.addActionListener(this);
        jmiRepTrnGlobal.addActionListener(this);
        jmiRepTrnByMonth.addActionListener(this);
        jmiRepTrnByItemGeneric.addActionListener(this);
        jmiRepTrnByItemGenericBizPartner.addActionListener(this);
        jmiRepTrnByItem.addActionListener(this);
        jmiRepTrnByItemBizPartner.addActionListener(this);
        jmiRepTrnByBizPartner.addActionListener(this);
        jmiRepTrnByBizPartnerItem.addActionListener(this);
        jmiRepTrnByBizPartnerType.addActionListener(this);
        jmiRepTrnByBizPartnerTypeBizPartner.addActionListener(this);
        jmiRepTrnDpsByItemBizPartner.addActionListener(this);
        jmiRepBackorderContract.addActionListener(this);
        jmiRepBackorderContractByItem.addActionListener(this);
        jmiRepBackorderContractByItemBizPartner.addActionListener(this);
        jmiRepBackorderContractByItemBizPartnerBra.addActionListener(this);
        jmiRepBackorderOrder.addActionListener(this);
        jmiRepBackorderOrderByItem.addActionListener(this);
        jmiRepBackorderOrderByItemBizPartner.addActionListener(this);
        jmiRepBackorderOrderByItemBizPartnerBra.addActionListener(this);
        jmiRepBizPartnerBalanceAgingView.addActionListener(this);
        jmiRepBizPartnerBalance.addActionListener(this);
        jmiRepBizPartnerBalanceDps.addActionListener(this);
        jmiRepBizPartnerBalanceAging.addActionListener(this);
        jmiRepBizPartnerBalanceCollection.addActionListener(this);
        jmiRepBizPartnerBalanceCollectionDps.addActionListener(this);
        jmiRepAccountStatements.addActionListener(this);
        jmiRepAccountStatementsDps.addActionListener(this);
        jmiRepBizPartnerJournal.addActionListener(this);
        jmiRepBizPartnerAccountingMoves.addActionListener(this);
        jmiRepBizPartnerAccountingMovesDays.addActionListener(this);
        jmiRepDpsList.addActionListener(this);
        jmiRepDpsBizPartner.addActionListener(this);
        jmiRepDpsWithBalance.addActionListener(this);
        jmiRepTrn.addActionListener(this);
        jmiRepTrnConcept.addActionListener(this);
        jmiRepTrnLocality.addActionListener(this);
        jmiRepTrnComparative.addActionListener(this);
        jmiRepTrnDpsDetailBizPartner.addActionListener(this);
        jmiRepTrnNetTotal.addActionListener(this);
        jmiRepTrnNetAnalytic.addActionListener(this);
        jmiRepTrnFileCsv.addActionListener(this);
        jmiRepTrnJournal.addActionListener(this);
        jmiRepTrnItemUnitaryPrice.addActionListener(this);
        jmiRepTrnContractStatus.addActionListener(this);
        jmiRepTrnContractBackorderStock.addActionListener(this);
        jmiRepTrnShipmentItem.addActionListener(this);
        jmiRepMoneyIn.addActionListener(this);

        hasRightDnsDps = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DPS_DNS).HasRight;
        hasRightDnsDiog = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_DIOG_CFG).HasRight;
        hasRightDocEstimate = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DOC_EST).HasRight;
        hasRightDocOrder = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DOC_ORD).HasRight;
        hasRightDocOrderAuthorize = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DOC_ORD_AUTHORN).HasRight ||
                miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DOC_ORD_REJECT_OWN).HasRight;
        hasRightDocTransaction = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DOC_TRN).HasRight;
        hasRightDocTransactionAdjust = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DOC_TRN_ADJ).HasRight;
        hasRightBizPartnerBlocking = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_BP_BLOCK).HasRight;
        hasRightInventoryIn = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_IN_SAL).HasRight ||
                miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DIOG_IN).HasRight;
        hasRightInventoryOut = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_OUT_SAL).HasRight ||
                miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DIOG_OUT).HasRight;
        hasRightReports = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_REP).HasRight;
        hasRightItemConfig =
                hasRightDocOrder && miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DOC_ORD).Level == SUtilConsts.LEV_MANAGER ||
                hasRightDocTransaction && miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DOC_TRN).Level == SUtilConsts.LEV_MANAGER;
        levelRightDocOrder = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DOC_ORD).Level;
        levelRightDocTransaction = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DOC_TRN).Level;

        jmCat.setEnabled(hasRightDnsDps || hasRightDnsDiog || hasRightBizPartnerBlocking || hasRightItemConfig);
        jmiCatDpsDncDocumentNumberSeries.setEnabled(hasRightDnsDps);
        jmiCatDiogDncDocumentNumberSeries.setEnabled(hasRightDnsDiog);
        jmiCatBizPartnerBlocking.setEnabled(hasRightBizPartnerBlocking);
        jmCatCfg.setEnabled(hasRightItemConfig);

        jmEst.setEnabled(hasRightDocEstimate);

        jmCon.setEnabled(hasRightDocEstimate);

        jmOrd.setEnabled(hasRightDocOrder || hasRightDocOrderAuthorize);
        jmiOrdersPriceHistory.setEnabled(hasRightDocOrder && levelRightDocOrder >= SUtilConsts.LEV_AUTHOR);
        jmiOrdersPrice.setEnabled(hasRightDocOrder && levelRightDocOrder >= SUtilConsts.LEV_AUTHOR);

        jmDps.setEnabled(hasRightDocTransaction);
        jmiDpsDoc.setEnabled(hasRightDocTransaction);
        jmiDpsEntry.setEnabled(hasRightDocTransaction);
        jmiDpsAnnuled.setEnabled(false);
        jmiDpsReissued.setEnabled(false);
        jmiDpsReplaced.setEnabled(false);
        jmiDpsLinksDestiny.setEnabled(hasRightDocTransaction);
        jmiDpsLinksTrace.setEnabled(hasRightDocTransaction);
        jmiDpsAuditPending.setEnabled(hasRightDocTransaction && levelRightDocTransaction == SUtilConsts.LEV_MANAGER);
        jmiDpsAudited.setEnabled(hasRightDocTransaction && levelRightDocTransaction == SUtilConsts.LEV_MANAGER);
        jmiDpsSendPendingEmail.setEnabled(hasRightDocTransaction && levelRightDocTransaction >= SUtilConsts.LEV_AUTHOR);
        jmiDpsSentEmail.setEnabled(hasRightDocTransaction && levelRightDocTransaction >= SUtilConsts.LEV_AUTHOR);
        jmiDpsSendPendingWs.setEnabled(hasRightDocTransaction && levelRightDocTransaction >= SUtilConsts.LEV_AUTHOR);
        jmiDpsApprovedWs.setEnabled(hasRightDocTransaction && levelRightDocTransaction >= SUtilConsts.LEV_AUTHOR);
        jmiDpsRejectWs.setEnabled(hasRightDocTransaction && levelRightDocTransaction >= SUtilConsts.LEV_AUTHOR);
        jmiDpsPriceHistory.setEnabled(hasRightDocTransaction && levelRightDocTransaction >= SUtilConsts.LEV_AUTHOR);
        jmiDpsPrice.setEnabled(hasRightDocTransaction && levelRightDocTransaction >= SUtilConsts.LEV_AUTHOR);

        jmDpsAdj.setEnabled(hasRightDocTransactionAdjust);
        jmiDpsAdjDoc.setEnabled(hasRightDocTransactionAdjust);
        jmiDpsAdjAnnuled.setEnabled(false);
        jmiDpsAdjReissued.setEnabled(false);
        jmiDpsAdjReplaced.setEnabled(false);
        jmiDpsAdjSendPendingEmail.setEnabled(hasRightDocTransactionAdjust);
        jmiDpsAdjSentEmail.setEnabled(hasRightDocTransactionAdjust);
        jmiDpsAdjSendPendingWs.setEnabled(hasRightDocTransactionAdjust);
        jmiDpsAdjApprovedWs.setEnabled(hasRightDocTransactionAdjust);
        jmiDpsAdjRejectWs.setEnabled(hasRightDocTransactionAdjust);

        jmStkDvy.setEnabled(hasRightInventoryOut);
        jmiStkDvyPend.setEnabled(hasRightInventoryOut);
        jmiStkDvySupplied.setEnabled(hasRightInventoryOut);
        jmiStkDvyPendEntry.setEnabled(hasRightInventoryOut);
        jmiStkDvySuppliedEntry.setEnabled(hasRightInventoryOut);
        jmiStkDvyDiog.setEnabled(hasRightInventoryOut);
        jmiStkDvyStatsConsumption.setEnabled(hasRightInventoryOut);

        jmStkRet.setEnabled(hasRightInventoryIn);
        jmiStkRetPending.setEnabled(hasRightInventoryIn);
        jmiStkRetReturned.setEnabled(hasRightInventoryIn);
        jmiStkRetPendingEntry.setEnabled(hasRightInventoryIn);
        jmiStkRetReturnedEntry.setEnabled(hasRightInventoryIn);
        jmiStkRetDiog.setEnabled(hasRightInventoryIn);

        jmRep.setEnabled(hasRightReports);
        
        // GUI configuration:
        
        if (((erp.SClient) miClient).getCfgProcesor() != null) {
            SCfgModule module = new SCfgModule("" + mnModuleType);
            SCfgMenu menu = null;
            SCfgMenuSection section = null;
            
            menu = new SCfgMenu(jmEst, "" + SDataConstants.MOD_SAL_EST);
            module.getChildMenus().add(menu);
            
            menu = new SCfgMenu(jmCon, "" + SDataConstants.MOD_SAL_CON);
            module.getChildMenus().add(menu);
            
            menu = new SCfgMenu(jmStkDvy, "" + SDataConstants.MOD_SAL_DVY);
            module.getChildMenus().add(menu);
            
            menu = new SCfgMenu(jmStkRet, "" + SDataConstants.MOD_SAL_RET);
            module.getChildMenus().add(menu);
            
            menu = new SCfgMenu(jmRep, "" + SDataConstants.MOD_SAL_REP);
            section = new SCfgMenuSection("" + SDataConstants.MOD_SAL_REP_CON);
            section.getChildItems().add(new SCfgMenuSectionItem(jmiRepTrnContractStatus, "" + SDataConstants.TRNS_ST_DPS));
            section.getChildItems().add(new SCfgMenuSectionItem(jmiRepTrnContractBackorderStock, "" + SDataConstants.TRN_STK));
            section.setChildSeparator(new SCfgMenuSectionSeparator(jsRepTrn)); // previous separator
            menu.getChildSections().add(section);
            section = new SCfgMenuSection("" + SDataConstants.MOD_SAL_REP_SHI);
            section.getChildItems().add(new SCfgMenuSectionItem(jmiRepTrnShipmentItem, "" + SModConsts.LOG_SHIP));
            section.setChildSeparator(new SCfgMenuSectionSeparator(jsRepContract)); // previous separator
            menu.getChildSections().add(section);
            section = new SCfgMenuSection("" + SDataConstants.MOD_SAL_REP_MIN);
            section.getChildItems().add(new SCfgMenuSectionItem(jmiRepMoneyIn, "" + SDataConstants.TRN_DPS));
            section.setChildSeparator(new SCfgMenuSectionSeparator(jsRepTrnShipment)); // previous separator
            menu.getChildSections().add(section);
            module.getChildMenus().add(menu);
            
            ((erp.SClient) miClient).getCfgProcesor().processModule(module);
        }
    }

    private java.lang.String getViewTitle(int type) {
        String viewTitle = "" ;

        switch (type) {
            case SDataConstantsSys.TRNX_DPS_SAL_ORD_AUT_PEND:
                viewTitle = "VTA - " + SDataConstantsSys.getDpsTypeNamePlr(SDataConstantsSys.TRNX_TP_DPS_ORD) + " x autorizar";
                break;
            case SDataConstantsSys.TRNX_DPS_SAL_ORD_AUT_AUT:
                viewTitle = "VTA - " + SDataConstantsSys.getDpsTypeNamePlr(SDataConstantsSys.TRNX_TP_DPS_ORD) + " autorizad@s";
                break;
            case SDataConstantsSys.TRNX_DPS_SAL_ORD_AUT_REJ:
                viewTitle = "VTA - " + SDataConstantsSys.getDpsTypeNamePlr(SDataConstantsSys.TRNX_TP_DPS_ORD) + " rechazad@s";
                break;
            case SDataConstantsSys.TRNX_DPS_SAL_DOC_AUT_PEND:
                viewTitle = "VTA - " + SDataConstantsSys.getDpsTypeNamePlr(SDataConstantsSys.TRNX_TP_DPS_DOC) + " x autorizar";
                break;
            case SDataConstantsSys.TRNX_DPS_SAL_DOC_AUT_AUT:
                viewTitle = "VTA - " + SDataConstantsSys.getDpsTypeNamePlr(SDataConstantsSys.TRNX_TP_DPS_DOC) + " autorizad@s";
                break;
            case SDataConstantsSys.TRNX_DPS_SAL_DOC_AUT_REJ:
                viewTitle = "VTA - " + SDataConstantsSys.getDpsTypeNamePlr(SDataConstantsSys.TRNX_TP_DPS_DOC) + " rechazad@s";
                break;
            case SDataConstantsSys.TRNX_SAL_TOT:
                viewTitle = "Ventas globales";
                break;
            case SDataConstantsSys.TRNX_SAL_TOT_MONTH:
                viewTitle = "Ventas globales x mes";
                break;
            case SDataConstantsSys.TRNX_SAL_TOT_BY_BP:
                viewTitle = "Ventas x cliente";
                break;
            case SDataConstantsSys.TRNX_SAL_TOT_BY_BP_ITEM:
                viewTitle = "Ventas x cliente-ítem";
                break;
            case SDataConstantsSys.TRNX_SAL_TOT_BY_IGEN:
                viewTitle = "Ventas x ítem genérico";
                break;
            case SDataConstantsSys.TRNX_SAL_TOT_BY_IGEN_BP:
                viewTitle = "Ventas x ítem genérico-cliente";
                break;
            case SDataConstantsSys.TRNX_SAL_TOT_BY_ITEM:
                viewTitle = "Ventas x ítem";
                break;
            case SDataConstantsSys.TRNX_SAL_TOT_BY_ITEM_BP:
                viewTitle = "Ventas x ítem-cliente";
                break;
            case SDataConstantsSys.TRNX_SAL_TOT_BY_TP_BP:
                viewTitle = "Ventas x tipo cliente";
                break;
            case SDataConstantsSys.TRNX_SAL_TOT_BY_TP_BP_BP:
                viewTitle = "Ventas x tipo cliente-cliente";
                break;
           case SDataConstantsSys.TRNX_SAL_DPS_BY_ITEM_BP_FIL:
                viewTitle = "Ventas docs. x ítem-cliente";
                break;
            default:
        }

        return viewTitle;
    }

    private int showForm(int formType, int auxType, java.lang.Object pk, boolean isCopy) {
        int result = SLibConstants.UNDEFINED;

        try {
            clearFormMembers();
            setFrameWaitCursor();

            switch (formType) {
                case SDataConstants.TRN_DNC_DPS_DNS:
                    if (moFormDncDocumentNumberSeriesDps == null) {
                        moFormDncDocumentNumberSeriesDps = new SFormDncDocumentNumberSeries(miClient, formType);
                    }
                    if (pk != null) {
                        moRegistry = new SDataDpsDncDocumentNumberSeries();
                    }
                    miForm = moFormDncDocumentNumberSeriesDps;
                    miForm.setValue(SDataConstantsSys.VALUE_IS_PUR, false);
                    break;
                case SDataConstants.TRN_DNC_DIOG_DNS:
                    if (moFormDncDocumentNumberSeriesDiog == null) {
                        moFormDncDocumentNumberSeriesDiog = new SFormDncDocumentNumberSeries(miClient, formType);
                    }
                    if (pk != null) {
                        moRegistry = new SDataDiogDncDocumentNumberSeries();
                    }
                    miForm = moFormDncDocumentNumberSeriesDiog;
                    miForm.setValue(SDataConstantsSys.VALUE_IS_PUR, false);
                    break;
                case SDataConstants.TRN_BP_BLOCK:
                    if (moFormBizPartnerBlocking == null) {
                        moFormBizPartnerBlocking = new SFormBizPartnerBlocking(miClient);
                    }
                    miForm = moFormBizPartnerBlocking;
                    if (pk != null) {
                        moRegistry = new SDataBizPartnerBlocking();
                    }
                    miForm.setValue(1, SDataConstantsSys.BPSS_CT_BP_CUS);
                    break;
                case SDataConstants.TRN_DPS:
                    if (moFormDps == null) {
                        moFormDps = new SFormDps(miClient, SDataConstantsSys.TRNS_CT_DPS_SAL);
                    }
                    miForm = moFormDps;
                    if (pk != null) {
                        moRegistry = new SDataDps();
                    }
                    break;
                case SDataConstants.TRNX_DPS_RO:
                    if (moFormDpsRo == null) {
                        moFormDpsRo = new SFormDps(miClient, SDataConstantsSys.TRNS_CT_DPS_SAL);
                    }
                    miForm = moFormDpsRo;
                    if (pk != null) {
                        moRegistry = new SDataDps();
                    }
                    break;
                case SDataConstants.TRN_DSM:
                    if (moFormDsm == null) {
                        moFormDsm = new SFormDsm(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataDsm();
                    }
                    miForm = moFormDsm;
                    miForm.setValue(1,  auxType);
                    break;
                case SDataConstants.FIN_CC_ITEM:
                    if (moFormCostCenterItem == null) {
                        moFormCostCenterItem = new SFormCostCenterItem(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataCostCenterItem();
                    }
                    miForm = moFormCostCenterItem;
                    break;
                case SDataConstants.TRN_SIGN:
                    if (moFormStamp == null) {
                        moFormStamp = new SFormStamp(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataSign();
                    }
                    miForm = moFormStamp;
                    break;
                default:
                    throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_FORM);
            }

            // Additional configuration, if applies:

            switch (formType) {
                case SDataConstants.TRN_DPS:
                    miForm.setValue(SDataConstants.USRS_TP_LEV, mnCurrentUserPrivilegeLevel);

                    if (moFormComplement != null) {
                        if (moFormComplement instanceof int[]) {
                            // Complement has document type:

                            miForm.setValue(SLibConstants.VALUE_TYPE, moFormComplement);
                            miForm.setValue(SLibConstants.VALUE_STATUS, false);     // editable status
                            miForm.setValue(SLibConstants.VALUE_CURRENCY_LOCAL, false);     // convert local currency DPS
                        }
                        else if (moFormComplement instanceof Object[]) {
                            // Complement has document type and a reference document to import entries:

                            miForm.setValue(SLibConstants.VALUE_TYPE, ((Object[]) moFormComplement)[0]);    // int[]

                            if (((Object[]) moFormComplement).length >= 2) {
                                if (((Object[]) moFormComplement)[1] instanceof Boolean) {
                                    miForm.setValue(SLibConstants.VALUE_STATUS, ((Object[]) moFormComplement)[1]);
                                }
                                else if (((Object[]) moFormComplement)[1] instanceof SDataDps) {
                                    if (SLibUtilities.compareKeys(((Object[]) moFormComplement)[0], SDataConstantsSys.TRNU_TP_DPS_SAL_ORD)) {
                                        miForm.setValue(SDataConstantsSys.TRNX_TP_DPS_EST, ((Object[]) moFormComplement)[1]);
                                    }
                                    else if (SLibUtilities.compareKeys(((Object[]) moFormComplement)[0], SDataConstantsSys.TRNU_TP_DPS_SAL_INV)) {
                                        miForm.setValue(SDataConstantsSys.TRNX_TP_DPS_ORD, ((Object[]) moFormComplement)[1]);
                                    }
                                    else if (SLibUtilities.compareKeys(((Object[]) moFormComplement)[0], SDataConstantsSys.TRNU_TP_DPS_SAL_CN)) {
                                        miForm.setValue(SDataConstantsSys.TRNX_TP_DPS_DOC, ((Object[]) moFormComplement)[1]);
                                    }
                                }
                            }

                            if (((Object[]) moFormComplement).length >= 3 && ((Object[]) moFormComplement)[2] != null) {
                                miForm.setValue(SDataConstants.TRNS_STP_DPS_ADJ, ((Object[]) moFormComplement)[2]);
                            }

                            if (((Object[]) moFormComplement).length >= 4) {
                                miForm.setValue(SLibConstants.VALUE_CURRENCY_LOCAL, ((Object[]) moFormComplement)[3]);
                            }
                        }
                    }
                    break;

                case SDataConstants.TRNX_DPS_RO:
                    miForm.setValue(SLibConstants.VALUE_TYPE, moFormComplement); // int[] document type
                    miForm.setValue(SLibConstants.VALUE_STATUS, true); // editable status
                    break;

                default:
            }

            result = processForm(pk, isCopy);
            clearFormComplement();

            if (moRegistry != null) {
                switch (formType) {
                    case SDataConstants.TRN_DPS:
                        SDataDps dps = null;

                        if (((SDataDps) moRegistry).getAuxIsNeedCfd()) {
                            try {
                                dps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moRegistry.getPrimaryKey(), SLibConstants.EXEC_MODE_VERBOSE); // get brand new information stored in DBMS (e.g. edition timestamp)

                                SCfdUtils.computeCfd(miClient, dps, miClient.getSessionXXX().getParamsCompany().getFkXmlTypeId());
                            }
                            catch (java.lang.Exception e) {
                                throw new Exception("Ha ocurrido una excepción al generar el CFD: " + e);
                            }
                        }
                        break;

                    default:
                }
            }
        }
        catch (java.lang.Exception e) {
            SLibUtilities.renderException(this, e);
        }
        finally {
            restoreFrameCursor();
        }

        return result;
    }

    @Override
    public void showView(int viewType) {
        showView(viewType, 0, 0);
    }

    @Override
    public void showView(int viewType, int auxType) {
        showView(viewType, auxType, 0);
    }

    @Override
    public void showView(int viewType, int auxType01, int auxType02) {
        java.lang.String sViewTitle = "";
        java.lang.Class oViewClass = null;

        try {
            setFrameWaitCursor();

            switch (viewType) {
                case SDataConstants.TRN_DNC_DPS_DNS:
                case SDataConstants.TRN_DNC_DIOG_DNS:
                    oViewClass = erp.mtrn.view.SViewDncDocumentNumberSeries.class;
                    switch(viewType) {
                        case SDataConstants.TRN_DNC_DPS_DNS:
                            sViewTitle = "Folios docs. ventas";
                            break;
                        case SDataConstants.TRN_DNC_DIOG_DNS:
                            sViewTitle = "Folios docs. mercancías";
                            break;
                        default:
                            throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_VIEW);
                    }
                    auxType02 = auxType01;
                    auxType01 = viewType;
                    break;

                case SDataConstants.TRN_BP_BLOCK:
                    oViewClass = erp.mtrn.view.SViewBizPartnerBlocking.class;
                    sViewTitle = "Bloqueo clientes";
                    break;

                case SDataConstants.FIN_CC_ITEM:
                    oViewClass = erp.mfin.view.SViewCostCenterItem.class;
                    sViewTitle = "Config. centros costo ítems";
                    break;

                case SDataConstants.TRN_DPS:
                    oViewClass = erp.mtrn.view.SViewDps.class;
                    switch (auxType02) {
                        case SDataConstantsSys.TRNX_TP_DPS_EST_EST:
                        case SDataConstantsSys.TRNX_TP_DPS_EST_CON:
                        case SDataConstantsSys.TRNX_TP_DPS_ORD:
                        case SDataConstantsSys.TRNX_TP_DPS_DOC:
                        case SDataConstantsSys.TRNX_TP_DPS_ADJ:
                            sViewTitle = "VTA - " + SDataConstantsSys.getDpsTypeNamePlr(auxType02);
                            break;
                        case SDataConstantsSys.TRNX_TP_DPS_DOC + SDataConstantsSys.TRNX_DPS_DOC_ANNUL:
                            sViewTitle = "VTA - " + SDataConstantsSys.getDpsTypeNamePlr(SDataConstantsSys.TRNX_TP_DPS_DOC) + " anulación";
                            break;
                        case SDataConstantsSys.TRNX_TP_DPS_DOC + SDataConstantsSys.TRNX_DPS_DOC_RISS:
                            sViewTitle = "VTA - " + SDataConstantsSys.getDpsTypeNamePlr(SDataConstantsSys.TRNX_TP_DPS_DOC) + " reimpresión";
                            break;
                        case SDataConstantsSys.TRNX_TP_DPS_DOC + SDataConstantsSys.TRNX_DPS_DOC_REPL:
                            sViewTitle = "VTA - " + SDataConstantsSys.getDpsTypeNamePlr(SDataConstantsSys.TRNX_TP_DPS_DOC) + " sustitución";
                            break;
                        case SDataConstantsSys.TRNX_TP_DPS_ADJ + SDataConstantsSys.TRNX_DPS_DOC_ANNUL:
                            sViewTitle = "VTA - " + SDataConstantsSys.getDpsTypeNamePlr(SDataConstantsSys.TRNX_TP_DPS_ADJ) + " anulación";
                            break;
                        case SDataConstantsSys.TRNX_TP_DPS_ADJ + SDataConstantsSys.TRNX_DPS_DOC_RISS:
                            sViewTitle = "VTA - " + SDataConstantsSys.getDpsTypeNamePlr(SDataConstantsSys.TRNX_TP_DPS_ADJ) + " reimpresión";
                            break;
                        case SDataConstantsSys.TRNX_TP_DPS_ADJ + SDataConstantsSys.TRNX_DPS_DOC_REPL:
                            sViewTitle = "VTA - " + SDataConstantsSys.getDpsTypeNamePlr(SDataConstantsSys.TRNX_TP_DPS_ADJ) + " sustitución";
                            break;
                        default:
                            throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_VIEW);
                    }
                    break;

                case SDataConstants.TRNX_DPS_LINK_PEND:
                    oViewClass = erp.mtrn.view.SViewDpsLink.class;
                    sViewTitle = "VTA - " + SDataConstantsSys.getDpsTypeNamePlr(auxType02) + " x procesar";
                    break;

                case SDataConstants.TRNX_DPS_LINK_PEND_ETY:
                    oViewClass = erp.mtrn.view.SViewDpsLink.class;
                    sViewTitle = "VTA - " + SDataConstantsSys.getDpsTypeNamePlr(auxType02) + " x procesar (detalle)";
                    break;

                case SDataConstants.TRNX_DPS_LINKED:
                    oViewClass = erp.mtrn.view.SViewDpsLink.class;
                    sViewTitle = "VTA - " + SDataConstantsSys.getDpsTypeNamePlr(auxType02) + " procesad@s";
                    break;

                case SDataConstants.TRNX_DPS_LINKED_ETY:
                    oViewClass = erp.mtrn.view.SViewDpsLink.class;
                    sViewTitle = "VTA - " + SDataConstantsSys.getDpsTypeNamePlr(auxType02) + " procesad@s (detalle)";
                    break;

                case SDataConstants.TRNX_DPS_LINKS:
                    oViewClass = erp.mtrn.view.SViewDpsLinksQuery.class;
                    sViewTitle = "VTA - Vínculos " + SDataConstantsSys.getLinkNamePlr(auxType02).toLowerCase();
                    break;

                case SDataConstants.TRNX_DPS_LINKS_TRACE:
                    oViewClass = erp.mtrn.view.SViewDpsLinksQueryTrace.class;
                    sViewTitle = "VTA - Rastreo vínculos " + SDataConstantsSys.getDpsTypeNamePlr(auxType01).toLowerCase();
                    break;

                case SDataConstants.TRNX_DPS_AUTHORIZE_PEND:
                    oViewClass = erp.mtrn.view.SViewDpsPendAuthorized.class;
                    sViewTitle = getViewTitle(auxType01);
                    break;

                case SDataConstants.TRNX_DPS_AUDIT_PEND:
                    oViewClass = erp.mtrn.view.SViewDpsAudit.class;
                    sViewTitle = "VTA - " + SDataConstantsSys.getDpsTypeNamePlr(auxType02) + " x auditar";
                    break;

                case SDataConstants.TRNX_DPS_AUDITED:
                    oViewClass = erp.mtrn.view.SViewDpsAudit.class;
                    sViewTitle = "VTA - " + SDataConstantsSys.getDpsTypeNamePlr(auxType02) + " auditad@s";
                    break;

                case SDataConstants.TRNX_DPS_SEND_PEND:
                    oViewClass = erp.mtrn.view.SViewDpsSend.class;
                    sViewTitle = "VTA - " + SDataConstantsSys.getDpsTypeNamePlr(auxType02) + " x enviar x correo-e";
                    break;

                case SDataConstants.TRNX_DPS_SENT:
                    oViewClass = erp.mtrn.view.SViewDpsSend.class;
                    sViewTitle = "VTA - " + SDataConstantsSys.getDpsTypeNamePlr(auxType02) + " enviad@s x correo-e";
                    break;

                case SDataConstants.TRNX_PRICE_HIST:
                    oViewClass = erp.mtrn.view.SViewPriceHistory.class;
                    sViewTitle = "VTA - Historial precios";
                    break;

                case SDataConstants.TRNX_DPS_BACKORDER:
                    switch (auxType01) {
                        case SDataConstantsSys.TRNX_SAL_BACKORDER_CON:
                        case SDataConstantsSys.TRNX_SAL_BACKORDER_ORD:
                            oViewClass = erp.mtrn.view.SViewBackorder.class;
                            if (auxType02 == SDataConstantsSys.TRNS_CL_DPS_SAL_EST[1]) {
                                sViewTitle = "VTA - BO contratos";
                            }
                            else {
                                sViewTitle = "VTA - BO pedidos";
                            }
                            break;

                        case SDataConstantsSys.TRNX_SAL_BACKORDER_CON_ITEM:
                        case SDataConstantsSys.TRNX_SAL_BACKORDER_ORD_ITEM:
                            oViewClass = erp.mtrn.view.SViewBackorder.class;
                            if (auxType02 == SDataConstantsSys.TRNS_CL_DPS_SAL_EST[1]) {
                                sViewTitle = "VTA - BO contratos x ítem";
                            }
                            else {
                                sViewTitle = "VTA - BO pedidos x ítem";
                            }
                            break;

                        case SDataConstantsSys.TRNX_SAL_BACKORDER_CON_ITEM_BP:
                        case SDataConstantsSys.TRNX_SAL_BACKORDER_ORD_ITEM_BP:
                            oViewClass = erp.mtrn.view.SViewBackorder.class;
                            if (auxType02 == SDataConstantsSys.TRNS_CL_DPS_SAL_EST[1]) {
                                sViewTitle = "VTA - BO contratos x ítem-cliente";
                            }
                            else {
                                sViewTitle = "VTA - BO pedidos x ítem-cliente";
                            }
                            break;
                            
                        case SDataConstantsSys.TRNX_SAL_BACKORDER_CON_ITEM_BP_BRA:
                        case SDataConstantsSys.TRNX_SAL_BACKORDER_ORD_ITEM_BP_BRA:
                            oViewClass = erp.mtrn.view.SViewBackorder.class;
                            if (auxType02 == SDataConstantsSys.TRNS_CL_DPS_SAL_EST[1]) {
                                sViewTitle = "VTA - BO contratos x ítem-cliente suc.";
                            }
                            else {
                                sViewTitle = "VTA - BO pedidos x ítem-cliente suc.";
                            }
                            break;
                            
                        default:
                    }
                    break;

                case SDataConstants.TRNX_DPS_BAL_AGING:
                    oViewClass = erp.mtrn.view.SViewDpsBalanceAging.class;
                    sViewTitle = "Antigüedad saldos clientes";
                    break;

                case SDataConstants.TRNX_DPS_PAY_PEND:
                    oViewClass = erp.mtrn.view.SViewDpsPay.class;
                    sViewTitle = "Cuentas x cobrar";
                    break;

                case SDataConstants.TRNX_DPS_PAYED:
                    oViewClass = erp.mtrn.view.SViewDpsPay.class;
                    sViewTitle = "Cuentas cobradas";
                    break;

                case SDataConstants.TRN_DSM:
                    oViewClass = erp.mtrn.view.SViewDsm.class;
                    sViewTitle = "Movimientos clientes";
                    break;

                case SDataConstants.TRNX_DPS_QRY:
                    switch (auxType01) {
                        case SDataConstantsSys.TRNX_SAL_TOT:
                            oViewClass = erp.mtrn.view.SViewQueryGlobal.class;
                            sViewTitle = getViewTitle(auxType01);
                            break;
                        case SDataConstantsSys.TRNX_SAL_TOT_MONTH:
                            oViewClass = erp.mtrn.view.SViewQueryGlobalByMonth.class;
                            sViewTitle = getViewTitle(auxType01);
                            break;
                        case SDataConstantsSys.TRNX_SAL_TOT_BY_BP:
                        case SDataConstantsSys.TRNX_SAL_TOT_BY_BP_ITEM:
                        case SDataConstantsSys.TRNX_SAL_TOT_BY_IGEN:
                        case SDataConstantsSys.TRNX_SAL_TOT_BY_IGEN_BP:
                        case SDataConstantsSys.TRNX_SAL_TOT_BY_ITEM:
                        case SDataConstantsSys.TRNX_SAL_TOT_BY_ITEM_BP:
                        case SDataConstantsSys.TRNX_SAL_TOT_BY_TP_BP:
                        case SDataConstantsSys.TRNX_SAL_TOT_BY_TP_BP_BP:
                            oViewClass = erp.mtrn.view.SViewQueryTotal.class;
                            sViewTitle = getViewTitle(auxType01);
                            break;
                       case SDataConstantsSys.TRNX_SAL_DPS_BY_ITEM_BP_ALL:
                       case SDataConstantsSys.TRNX_SAL_DPS_BY_ITEM_BP_FIL:
                            oViewClass = erp.mtrn.view.SViewQueryDpsByItemBizPartner.class;

                            if (auxType01 == SDataConstantsSys.TRNX_SAL_DPS_BY_ITEM_BP_ALL) {
                                sViewTitle = "VTA - Facturas (detalle)";
                            }
                            else {
                                sViewTitle = getViewTitle(auxType01);
                            }
                            break;
                        default:
                            throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_VIEW);
                    }
                    break;

                case SDataConstants.TRN_CFD:
                    oViewClass = erp.mtrn.view.SViewCfdXml.class;
                    sViewTitle = "VTA - CFDI";
                    
                    switch (auxType01) {
                        case SDataConstants.TRNX_STAMP_SIGN:
                            sViewTitle += " timbrados";
                            break;
                        case SDataConstants.TRNX_STAMP_SIGN_PEND:
                            sViewTitle += " x timbrar";
                            break;
                        default:
                            throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_VIEW);
                    }
                    break;
                    
                case SDataConstants.TRN_CFD_SND_LOG:
                    oViewClass = erp.mtrn.view.SViewCfdSendingLog.class;
                    sViewTitle = "VTA - bitácora envíos CFDI";
                    break;

                case SDataConstants.MKT_PLIST_ITEM:
                    oViewClass = erp.mmkt.view.SViewPriceListItem.class;
                    sViewTitle = "VTA - Precios de ítems";
                    break;

                default:
                    throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_VIEW);
            }

            processView(oViewClass, sViewTitle, viewType, auxType01, auxType02);
        }
        catch (java.lang.Exception e) {
            SLibUtilities.renderException(this, e);
        }
        finally {
            restoreFrameCursor();
        }
    }

    @Override
    public int showForm(int formType, java.lang.Object pk) {
        return showForm(formType, SDataConstants.UNDEFINED, pk, false);
    }

    @Override
    public int showForm(int formType, int auxType, Object pk) {
        return showForm(formType, auxType, pk, false);
    }

    @Override
    public int showFormForCopy(int formType, java.lang.Object pk) {
        return showForm(formType, SDataConstants.UNDEFINED, pk, true);
    }

    @Override
    public erp.lib.form.SFormOptionPickerInterface getOptionPicker(int optionType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int annulRegistry(int registryType, java.lang.Object pk, sa.lib.gui.SGuiParams params) {
        int result = SLibConstants.UNDEFINED;
        boolean annul = true;
        String error = "";
        SServerRequest request = null;
        SServerResponse response = null;

        try {
            switch (registryType) {
                case SDataConstants.TRN_DPS:
                    moRegistry = (SDataDps) SDataUtilities.readRegistry(miClient, registryType, pk, SLibConstants.EXEC_MODE_VERBOSE);

                    if (moRegistry == null) {
                        annul = false;
                    }
                    else {
                        if (((SDataDps) moRegistry).getDbmsDataCfd() != null) {
                            if (((SDataDps) moRegistry).getDbmsDataCfd().getFkDpsYearId_n() != SLibConstants.UNDEFINED) {
                                if (((SDataDps) moRegistry).getDbmsDataCfd().getFkXmlTypeId() == SDataConstantsSys.TRNS_TP_XML_CFDI && ((SDataDps) moRegistry).getDbmsDataCfd().getFkXmlStatusId() == SDataConstantsSys.TRNS_ST_DPS_EMITED) {
                                    // Check if registry can be annuled:

                                    request = new SServerRequest(SServerConstants.REQ_DB_CAN_ANNUL);
                                    request.setPacket(moRegistry);
                                    response = miClient.getSessionXXX().request(request);

                                    if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
                                        error = response.getMessage();
                                    }
                                    else {
                                        result = response.getResultType();

                                        if (result != SLibConstants.DB_CAN_ANNUL_YES) {
                                            error = SLibConstants.MSG_ERR_DB_REG_ANNUL_CAN + (response.getMessage().length() == 0 ? "" : "\n" + response.getMessage());
                                        }
                                        /*
                                        else if (SCfdUtils.isNeedStamps(miClient, ((SDataDps) moRegistry).getDbmsDataCfd(), SDbConsts.ACTION_ANNUL) && SCfdUtils.getStampsAvailable(miClient, ((SDataDps) moRegistry).getDbmsDataCfd(), ((SDataDps) moRegistry).getDate()) <= 0) {
                                            error = "No existen timbres disponibles.";
                                        }
                                        */
                                        else {
                                            if ((Boolean) params.getParamsMap().get(SGuiConsts.PARAM_REQ_DOC)) {
                                                // XXX jbarajas 03/02/2016 sign and sending CFDI
                                                //SCfdUtils.cancelCfdi(miClient, ((SDataDps) moRegistry).getDbmsDataCfd(), SLibConstants.UNDEFINED, (Date) params.getParamsMap().get(SGuiConsts.PARAM_DATE), (Boolean) params.getParamsMap().get(SGuiConsts.PARAM_REQ_DOC));
                                                SCfdUtils.cancelAndSendCfdi(miClient, ((SDataDps) moRegistry).getDbmsDataCfd(), SLibConstants.UNDEFINED, (Date) params.getParamsMap().get(SGuiConsts.PARAM_DATE), (Boolean) params.getParamsMap().get(SGuiConsts.PARAM_REQ_DOC), true);
                                                result = SLibConstants.DB_ACTION_ANNUL_OK;
                                                annul = false;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (!error.isEmpty()) {
                        throw new Exception(error);
                    }

                    break;

                default:
                    throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
            }

            if (annul) {
                result = processActionAnnul(pk, true);
            }
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }

        return result;
    }

    @Override
    public int deleteRegistry(int registryType, java.lang.Object pk) {
        int result = SLibConstants.UNDEFINED;

        try {
            switch (registryType) {
                case SDataConstants.TRN_DPS:
                    moRegistry = new SDataDps();
                    break;
                default:
                    throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
            }

            result = processActionDelete(pk, true);
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }

        return result;
    }

    @Override
    public javax.swing.JMenu[] getMenues() {
        return new JMenu[] { jmCat, jmEst, jmCon, jmOrd, jmDps, jmDpsAdj, jmStkDvy, jmStkRet, jmRep };
    }

    @Override
    public javax.swing.JMenu[] getMenuesForModule(int moduleType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (e.getSource() instanceof javax.swing.JMenuItem) {
            javax.swing.JMenuItem item = (javax.swing.JMenuItem) e.getSource();

            if (item == jmiCatDpsDncDocumentNumberSeries) {
                showView(SDataConstants.TRN_DNC_DPS_DNS, SDataConstantsSys.TRNS_CT_DPS_SAL);
            }
            else if (item == jmiCatDiogDncDocumentNumberSeries) {
                showView(SDataConstants.TRN_DNC_DIOG_DNS, SDataConstantsSys.TRNS_CT_IOG_OUT);
            }
            else if (item == jmiCatBizPartnerBlocking) {
                showView(SDataConstants.TRN_BP_BLOCK, SDataConstantsSys.BPSS_CT_BP_CUS);
            }
            else if (item == jmiCatCfgCostCenterItem) {
                showView(SDataConstants.FIN_CC_ITEM);
            }
            else if (item == jmiCatCfdiStampAvailable) {
                miClient.getGuiModule(SDataConstants.MOD_CFG).showView(SDataConstants.TRNX_STAMP_AVL);
            }
            else if (item == jmiCatCfdiStampSign) {
                showView(SDataConstants.TRN_CFD, SDataConstants.TRNX_STAMP_SIGN, SCfdConsts.CFD_TYPE_DPS);
            }
            else if (item == jmiCatCfdiStampSignPending) {
                showView(SDataConstants.TRN_CFD, SDataConstants.TRNX_STAMP_SIGN_PEND, SCfdConsts.CFD_TYPE_DPS);
            }
            else if (item == jmiCatCfdiSendingLog) {
                showView(SDataConstants.TRN_CFD_SND_LOG, SCfdConsts.CFD_TYPE_DPS);
            }
            else if (item == jmiEstimates) {
                showView(SDataConstants.TRN_DPS, SDataConstantsSys.TRNS_CT_DPS_SAL, SDataConstantsSys.TRNX_TP_DPS_EST_EST);
            }
            else if (item == jmiEstimatesLinkPend) {
                showView(SDataConstants.TRNX_DPS_LINK_PEND, SDataConstantsSys.TRNS_CT_DPS_SAL, SDataConstantsSys.TRNX_TP_DPS_EST_EST);
            }
            else if (item == jmiEstimatesLinkPendEntry) {
                showView(SDataConstants.TRNX_DPS_LINK_PEND_ETY, SDataConstantsSys.TRNS_CT_DPS_SAL, SDataConstantsSys.TRNX_TP_DPS_EST_EST);
            }
            else if (item == jmiEstimatesLinked) {
                showView(SDataConstants.TRNX_DPS_LINKED, SDataConstantsSys.TRNS_CT_DPS_SAL, SDataConstantsSys.TRNX_TP_DPS_EST_EST);
            }
            else if (item == jmiEstimatesLinkedEntry) {
                showView(SDataConstants.TRNX_DPS_LINKED_ETY, SDataConstantsSys.TRNS_CT_DPS_SAL, SDataConstantsSys.TRNX_TP_DPS_EST_EST);
            }
            else if (item == jmiEstimatesLinks) {
                showView(SDataConstants.TRNX_DPS_LINKS, SDataConstantsSys.TRNS_CT_DPS_SAL, SDataConstantsSys.TRNX_LINK_EST_EST_SRC);
            }
            else if (item == jmiContracts) {
                showView(SDataConstants.TRN_DPS, SDataConstantsSys.TRNS_CT_DPS_SAL, SDataConstantsSys.TRNX_TP_DPS_EST_CON);
            }
            else if (item == jmiContractsLinkPend) {
                showView(SDataConstants.TRNX_DPS_LINK_PEND, SDataConstantsSys.TRNS_CT_DPS_SAL, SDataConstantsSys.TRNX_TP_DPS_EST_CON);
            }
            else if (item == jmiContractsLinkPendEntry) {
                showView(SDataConstants.TRNX_DPS_LINK_PEND_ETY, SDataConstantsSys.TRNS_CT_DPS_SAL, SDataConstantsSys.TRNX_TP_DPS_EST_CON);
            }
            else if (item == jmiContractsLinked) {
                showView(SDataConstants.TRNX_DPS_LINKED, SDataConstantsSys.TRNS_CT_DPS_SAL, SDataConstantsSys.TRNX_TP_DPS_EST_CON);
            }
            else if (item == jmiContractsLinkedEntry) {
                showView(SDataConstants.TRNX_DPS_LINKED_ETY, SDataConstantsSys.TRNS_CT_DPS_SAL, SDataConstantsSys.TRNX_TP_DPS_EST_CON);
            }
            else if (item == jmiContractsLinks) {
                showView(SDataConstants.TRNX_DPS_LINKS, SDataConstantsSys.TRNS_CT_DPS_SAL, SDataConstantsSys.TRNX_LINK_EST_CON_SRC);
            }
            else if (item == jmiContractsLinkPendEntryPrice) {
                miClient.getSession().showView(SModConsts.TRN_DPS_ETY_PRC, SModConsts.MOD_TRN_SAL_N, new SGuiParams(SModConsts.VIEW_ST_PEND));
            }
            else if (item == jmiContractsLinkedEntryPrice) {
                miClient.getSession().showView(SModConsts.TRN_DPS_ETY_PRC, SModConsts.MOD_TRN_SAL_N, new SGuiParams(SModConsts.VIEW_ST_DONE));
            }
            else if (item == jmiContractsSendMail) {
                new SDialogSendMailContract(miClient.getSession().getClient(), miClient, "Envío de contratos por email").setVisible(true);
            }
            else if (item == jmiOrders) {
                showView(SDataConstants.TRN_DPS, SDataConstantsSys.TRNS_CT_DPS_SAL, SDataConstantsSys.TRNX_TP_DPS_ORD);
            }
            else if (item == jmiOrdersLinkPend) {
                showView(SDataConstants.TRNX_DPS_LINK_PEND, SDataConstantsSys.TRNS_CT_DPS_SAL, SDataConstantsSys.TRNX_TP_DPS_ORD);
            }
            else if (item == jmiOrdersLinkPendEntry) {
                showView(SDataConstants.TRNX_DPS_LINK_PEND_ETY, SDataConstantsSys.TRNS_CT_DPS_SAL, SDataConstantsSys.TRNX_TP_DPS_ORD);
            }
            else if (item == jmiOrdersLinked) {
                showView(SDataConstants.TRNX_DPS_LINKED, SDataConstantsSys.TRNS_CT_DPS_SAL, SDataConstantsSys.TRNX_TP_DPS_ORD);
            }
            else if (item == jmiOrdersLinkedEntry) {
                showView(SDataConstants.TRNX_DPS_LINKED_ETY, SDataConstantsSys.TRNS_CT_DPS_SAL, SDataConstantsSys.TRNX_TP_DPS_ORD);
            }
            else if (item == jmiOrdersLinksSource) {
                showView(SDataConstants.TRNX_DPS_LINKS, SDataConstantsSys.TRNS_CT_DPS_SAL, SDataConstantsSys.TRNX_LINK_ORD_SRC);
            }
            else if (item == jmiOrdersLinksDestiny) {
                showView(SDataConstants.TRNX_DPS_LINKS, SDataConstantsSys.TRNS_CT_DPS_SAL, SDataConstantsSys.TRNX_LINK_ORD_DES);
            }
            else if (item == jmiOrdersAutorizedPending) {
                showView(SDataConstants.TRNX_DPS_AUTHORIZE_PEND, SDataConstantsSys.TRNX_DPS_SAL_ORD_AUT_PEND);
            }
            else if (item == jmiOrdersAutorizedAutorized) {
                showView(SDataConstants.TRNX_DPS_AUTHORIZE_PEND, SDataConstantsSys.TRNX_DPS_SAL_ORD_AUT_AUT);
            }
            else if (item == jmiOrdersAutorizedRejected) {
                showView(SDataConstants.TRNX_DPS_AUTHORIZE_PEND, SDataConstantsSys.TRNX_DPS_SAL_ORD_AUT_REJ);
            }
            else if (item == jmiOrdersPrice) {
                showView(SDataConstants.MKT_PLIST_ITEM, SDataConstantsSys.TRNS_CT_DPS_SAL);
            }
            else if (item == jmiDpsDoc) {
                showView(SDataConstants.TRN_DPS, SDataConstantsSys.TRNS_CT_DPS_SAL, SDataConstantsSys.TRNX_TP_DPS_DOC);
            }
            else if (item == jmiDpsEntry) {
               showView(SDataConstants.TRNX_DPS_QRY, SDataConstantsSys.TRNX_SAL_DPS_BY_ITEM_BP_ALL);
            }
            else if (item == jmiDpsAnnuled) {
                showView(SDataConstants.TRN_DPS, SDataConstantsSys.TRNS_CT_DPS_SAL, SDataConstantsSys.TRNX_TP_DPS_DOC + SDataConstantsSys.TRNX_DPS_DOC_ANNUL);
            }
            else if (item == jmiDpsReissued) {
                showView(SDataConstants.TRN_DPS, SDataConstantsSys.TRNS_CT_DPS_SAL, SDataConstantsSys.TRNX_TP_DPS_DOC + SDataConstantsSys.TRNX_DPS_DOC_RISS);
            }
            else if (item == jmiDpsReplaced) {
                showView(SDataConstants.TRN_DPS, SDataConstantsSys.TRNS_CT_DPS_SAL, SDataConstantsSys.TRNX_TP_DPS_DOC + SDataConstantsSys.TRNX_DPS_DOC_REPL);
            }
            else if (item == jmiDpsLinksDestiny) {
                showView(SDataConstants.TRNX_DPS_LINKS, SDataConstantsSys.TRNS_CT_DPS_SAL, SDataConstantsSys.TRNX_LINK_DOC_DES);
            }
            else if (item == jmiDpsLinksTrace) {
                showView(SDataConstants.TRNX_DPS_LINKS_TRACE, SDataConstantsSys.TRNS_CT_DPS_SAL);
            }
            else if (item == jmiDpsAutorizedPending) {
                showView(SDataConstants.TRNX_DPS_AUTHORIZE_PEND, SDataConstantsSys.TRNX_DPS_SAL_DOC_AUT_PEND);
            }
            else if (item == jmiDpsAutorizedAutorized) {
                showView(SDataConstants.TRNX_DPS_AUTHORIZE_PEND, SDataConstantsSys.TRNX_DPS_SAL_DOC_AUT_AUT);
            }
            else if (item == jmiDpsAutorizedReject) {
                showView(SDataConstants.TRNX_DPS_AUTHORIZE_PEND, SDataConstantsSys.TRNX_DPS_SAL_DOC_AUT_REJ);
            }
            else if (item == jmiDpsAuditPending) {
                showView(SDataConstants.TRNX_DPS_AUDIT_PEND, SDataConstantsSys.TRNS_CT_DPS_SAL, SDataConstantsSys.TRNX_TP_DPS_DOC);
            }
            else if (item == jmiDpsAudited) {
                showView(SDataConstants.TRNX_DPS_AUDITED, SDataConstantsSys.TRNS_CT_DPS_SAL, SDataConstantsSys.TRNX_TP_DPS_DOC);
            }
            else if (item == jmiDpsSendPendingEmail) {
                showView(SDataConstants.TRNX_DPS_SEND_PEND, SDataConstantsSys.TRNS_CT_DPS_SAL, SDataConstantsSys.TRNX_TP_DPS_DOC);
            }
            else if (item == jmiDpsSentEmail) {
                showView(SDataConstants.TRNX_DPS_SENT, SDataConstantsSys.TRNS_CT_DPS_SAL, SDataConstantsSys.TRNX_TP_DPS_DOC);
            }
            else if (item == jmiDpsSendPendingWs) {
                miClient.getSession().showView(SModConsts.TRN_CFD, SModSysConsts.TRNS_ST_XML_DVY_PENDING, new SGuiParams(SDataConstantsSys.TRNX_TP_DPS_DOC));
            }
            else if (item == jmiDpsApprovedWs) {
                miClient.getSession().showView(SModConsts.TRN_CFD, SModSysConsts.TRNS_ST_XML_DVY_APPROVED, new SGuiParams(SDataConstantsSys.TRNX_TP_DPS_DOC));
            }
            else if (item == jmiDpsRejectWs) {
                miClient.getSession().showView(SModConsts.TRN_CFD, SModSysConsts.TRNS_ST_XML_DVY_REJECT, new SGuiParams(SDataConstantsSys.TRNX_TP_DPS_DOC));
            }
            else if (item == jmiDpsPriceHistory || item == jmiOrdersPriceHistory) {
                showView(SDataConstants.TRNX_PRICE_HIST, SDataConstantsSys.TRNS_CT_DPS_SAL);
            }
            else if (item == jmiDpsAdjDoc) {
                showView(SDataConstants.TRN_DPS, SDataConstantsSys.TRNS_CT_DPS_SAL, SDataConstantsSys.TRNX_TP_DPS_ADJ);
            }
            else if (item == jmiDpsAdjAnnuled) {
                showView(SDataConstants.TRN_DPS, SDataConstantsSys.TRNS_CT_DPS_SAL, SDataConstantsSys.TRNX_TP_DPS_ADJ + SDataConstantsSys.TRNX_DPS_DOC_ANNUL);
            }
            else if (item == jmiDpsAdjReissued) {
                showView(SDataConstants.TRN_DPS, SDataConstantsSys.TRNS_CT_DPS_SAL, SDataConstantsSys.TRNX_TP_DPS_ADJ + SDataConstantsSys.TRNX_DPS_DOC_RISS);
            }
            else if (item == jmiDpsAdjReplaced) {
                showView(SDataConstants.TRN_DPS, SDataConstantsSys.TRNS_CT_DPS_SAL, SDataConstantsSys.TRNX_TP_DPS_ADJ + SDataConstantsSys.TRNX_DPS_DOC_REPL);
            }
            else if (item == jmiDpsAdjSendPendingEmail) {
                showView(SDataConstants.TRNX_DPS_SEND_PEND, SDataConstantsSys.TRNS_CT_DPS_SAL, SDataConstantsSys.TRNX_TP_DPS_ADJ);
            }
            else if (item == jmiDpsAdjSentEmail) {
                showView(SDataConstants.TRNX_DPS_SENT, SDataConstantsSys.TRNS_CT_DPS_SAL, SDataConstantsSys.TRNX_TP_DPS_ADJ);
            }
            else if (item == jmiDpsAdjSendPendingWs) {
                miClient.getSession().showView(SModConsts.TRN_CFD, SModSysConsts.TRNS_ST_XML_DVY_PENDING, new SGuiParams(SDataConstantsSys.TRNX_TP_DPS_ADJ));
            }
            else if (item == jmiDpsAdjApprovedWs) {
                miClient.getSession().showView(SModConsts.TRN_CFD, SModSysConsts.TRNS_ST_XML_DVY_APPROVED, new SGuiParams(SDataConstantsSys.TRNX_TP_DPS_ADJ));
            }
            else if (item == jmiDpsAdjRejectWs) {
                miClient.getSession().showView(SModConsts.TRN_CFD, SModSysConsts.TRNS_ST_XML_DVY_REJECT, new SGuiParams(SDataConstantsSys.TRNX_TP_DPS_ADJ));
            }
            else if (item == jmiStkDvyPend) {
                miClient.getGuiModule(SDataConstants.MOD_INV).showView(SDataConstants.TRNX_DPS_SUPPLY_PEND, SDataConstantsSys.TRNS_CL_DPS_SAL_DOC[0], SDataConstantsSys.TRNS_CL_DPS_SAL_DOC[1]);
            }
            else if (item == jmiStkDvyPendEntry) {
                miClient.getGuiModule(SDataConstants.MOD_INV).showView(SDataConstants.TRNX_DPS_SUPPLY_PEND_ETY, SDataConstantsSys.TRNS_CL_DPS_SAL_DOC[0], SDataConstantsSys.TRNS_CL_DPS_SAL_DOC[1]);
            }
            else if (item == jmiStkDvySupplied) {
                miClient.getGuiModule(SDataConstants.MOD_INV).showView(SDataConstants.TRNX_DPS_SUPPLIED, SDataConstantsSys.TRNS_CL_DPS_SAL_DOC[0], SDataConstantsSys.TRNS_CL_DPS_SAL_DOC[1]);
            }
            else if (item == jmiStkDvySuppliedEntry) {
                miClient.getGuiModule(SDataConstants.MOD_INV).showView(SDataConstants.TRNX_DPS_SUPPLIED_ETY, SDataConstantsSys.TRNS_CL_DPS_SAL_DOC[0], SDataConstantsSys.TRNS_CL_DPS_SAL_DOC[1]);
            }
            else if (item == jmiStkDvyDiog) {
                miClient.getGuiModule(SDataConstants.MOD_INV).showView(SDataConstants.TRN_DIOG, SDataConstantsSys.TRNS_CL_IOG_OUT_SAL[0], SDataConstantsSys.TRNS_CL_IOG_OUT_SAL[1]);
            }
            else if (item == jmiStkDvyStatsConsumption) {
                miClient.getGuiModule(SDataConstants.MOD_INV).showView(SDataConstants.TRNX_STK_COMSUME, SDataConstantsSys.TRNS_CT_DPS_SAL);
            }
            else if (item == jmiStkRetPending) {
                miClient.getGuiModule(SDataConstants.MOD_INV).showView(SDataConstants.TRNX_DPS_RETURN_PEND, SDataConstantsSys.TRNS_CL_DPS_SAL_ADJ[0], SDataConstantsSys.TRNS_CL_DPS_SAL_ADJ[1]);
            }
            else if (item == jmiStkRetPendingEntry) {
                miClient.getGuiModule(SDataConstants.MOD_INV).showView(SDataConstants.TRNX_DPS_RETURN_PEND_ETY, SDataConstantsSys.TRNS_CL_DPS_SAL_ADJ[0], SDataConstantsSys.TRNS_CL_DPS_SAL_ADJ[1]);
            }
            else if (item == jmiStkRetReturned) {
                miClient.getGuiModule(SDataConstants.MOD_INV).showView(SDataConstants.TRNX_DPS_RETURNED, SDataConstantsSys.TRNS_CL_DPS_SAL_ADJ[0], SDataConstantsSys.TRNS_CL_DPS_SAL_ADJ[1]);
            }
            else if (item == jmiStkRetReturnedEntry) {
                miClient.getGuiModule(SDataConstants.MOD_INV).showView(SDataConstants.TRNX_DPS_RETURNED_ETY, SDataConstantsSys.TRNS_CL_DPS_SAL_ADJ[0], SDataConstantsSys.TRNS_CL_DPS_SAL_ADJ[1]);
            }
            else if (item == jmiStkRetDiog) {
                miClient.getGuiModule(SDataConstants.MOD_INV).showView(SDataConstants.TRN_DIOG, SDataConstantsSys.TRNS_CL_IOG_IN_SAL[0], SDataConstantsSys.TRNS_CL_IOG_IN_SAL[1]);
            }
            else if (item == jmiRepTrnGlobal) {
                showView(SDataConstants.TRNX_DPS_QRY, SDataConstantsSys.TRNX_SAL_TOT);
            }
            else if (item == jmiRepTrnByMonth) {
                showView(SDataConstants.TRNX_DPS_QRY, SDataConstantsSys.TRNX_SAL_TOT_MONTH);
            }
            else if (item == jmiRepTrnByItemGeneric) {
                showView(SDataConstants.TRNX_DPS_QRY, SDataConstantsSys.TRNX_SAL_TOT_BY_IGEN);
            }
            else if (item == jmiRepTrnByItemGenericBizPartner) {
                showView(SDataConstants.TRNX_DPS_QRY, SDataConstantsSys.TRNX_SAL_TOT_BY_IGEN_BP);
            }
            else if (item == jmiRepTrnByItem) {
                showView(SDataConstants.TRNX_DPS_QRY, SDataConstantsSys.TRNX_SAL_TOT_BY_ITEM);
            }
            else if (item == jmiRepTrnByItemBizPartner) {
                showView(SDataConstants.TRNX_DPS_QRY, SDataConstantsSys.TRNX_SAL_TOT_BY_ITEM_BP);
            }
            else if (item == jmiRepTrnByBizPartner) {
                showView(SDataConstants.TRNX_DPS_QRY, SDataConstantsSys.TRNX_SAL_TOT_BY_BP);
            }
            else if (item == jmiRepTrnByBizPartnerItem) {
                showView(SDataConstants.TRNX_DPS_QRY, SDataConstantsSys.TRNX_SAL_TOT_BY_BP_ITEM);
            }
            else if (item == jmiRepTrnByBizPartnerType) {
                showView(SDataConstants.TRNX_DPS_QRY, SDataConstantsSys.TRNX_SAL_TOT_BY_TP_BP);
            }
            else if (item == jmiRepTrnByBizPartnerTypeBizPartner) {
                showView(SDataConstants.TRNX_DPS_QRY, SDataConstantsSys.TRNX_SAL_TOT_BY_TP_BP_BP);
            }
            else if (item == jmiRepTrnDpsByItemBizPartner) {
               showView(SDataConstants.TRNX_DPS_QRY, SDataConstantsSys.TRNX_SAL_DPS_BY_ITEM_BP_FIL);
            }
            else if (item == jmiRepBackorderContract) {
                showView(SDataConstants.TRNX_DPS_BACKORDER, SDataConstantsSys.TRNX_SAL_BACKORDER_CON, SDataConstantsSys.TRNS_CL_DPS_SAL_EST[1]);
            }
            else if (item == jmiRepBackorderContractByItem) {
                showView(SDataConstants.TRNX_DPS_BACKORDER, SDataConstantsSys.TRNX_SAL_BACKORDER_CON_ITEM, SDataConstantsSys.TRNS_CL_DPS_SAL_EST[1]);
            }
            else if (item == jmiRepBackorderContractByItemBizPartner) {
                showView(SDataConstants.TRNX_DPS_BACKORDER, SDataConstantsSys.TRNX_SAL_BACKORDER_CON_ITEM_BP, SDataConstantsSys.TRNS_CL_DPS_SAL_EST[1]);
            }
            else if (item == jmiRepBackorderContractByItemBizPartnerBra) {
                showView(SDataConstants.TRNX_DPS_BACKORDER, SDataConstantsSys.TRNX_SAL_BACKORDER_CON_ITEM_BP_BRA, SDataConstantsSys.TRNS_CL_DPS_SAL_EST[1]);
            }
            else if (item == jmiRepBackorderOrder) {
                showView(SDataConstants.TRNX_DPS_BACKORDER, SDataConstantsSys.TRNX_SAL_BACKORDER_ORD, SDataConstantsSys.TRNS_CL_DPS_SAL_ORD[1]);
            }
            else if (item == jmiRepBackorderOrderByItem) {
                showView(SDataConstants.TRNX_DPS_BACKORDER, SDataConstantsSys.TRNX_SAL_BACKORDER_ORD_ITEM, SDataConstantsSys.TRNS_CL_DPS_SAL_ORD[1]);
            }
            else if (item == jmiRepBackorderOrderByItemBizPartner) {
                showView(SDataConstants.TRNX_DPS_BACKORDER, SDataConstantsSys.TRNX_SAL_BACKORDER_ORD_ITEM_BP, SDataConstantsSys.TRNS_CL_DPS_SAL_ORD[1]);
            }
            else if (item == jmiRepBackorderOrderByItemBizPartnerBra) {
                showView(SDataConstants.TRNX_DPS_BACKORDER, SDataConstantsSys.TRNX_SAL_BACKORDER_ORD_ITEM_BP_BRA, SDataConstantsSys.TRNS_CL_DPS_SAL_ORD[1]);
            }
            else if (item == jmiRepBizPartnerBalanceAgingView) {
                showView(SDataConstants.TRNX_DPS_BAL_AGING, SDataConstantsSys.TRNS_CT_DPS_SAL);
            }
            else if (item == jmiRepBizPartnerBalance) {
                new SDialogRepBizPartnerBalance(miClient, SDataConstantsSys.BPSS_CT_BP_CUS).setVisible(true);
            }
            else if (item == jmiRepBizPartnerBalanceDps) {
                new SDialogRepBizPartnerBalanceDps(miClient, SDataConstantsSys.BPSS_CT_BP_CUS).setVisible(true);
            }
            else if (item == jmiRepBizPartnerBalanceAging) {
                new SDialogRepBizPartnerBalanceAging(miClient, SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS).setVisible(true);
            }
            else if (item == jmiRepBizPartnerBalanceCollection) {
                new SDialogRepBizPartnerBalanceDpsCollection(miClient, SDataConstantsSys.BPSS_CT_BP_CUS, SDataConstantsSys.REP_FIN_BPS_BAL_COLL).setVisible(true);
            }
            else if (item == jmiRepBizPartnerBalanceCollectionDps) {
                new SDialogRepBizPartnerBalanceDpsCollection(miClient, SDataConstantsSys.BPSS_CT_BP_CUS, SDataConstantsSys.REP_FIN_BPS_BAL_COLL_DPS).setVisible(true);
            }
            else if (item == jmiRepAccountStatements) {
                new SDialogRepBizPartnerStatements(miClient, SDataConstantsSys.BPSS_CT_BP_CUS, false).setVisible(true);
            }
            else if (item == jmiRepAccountStatementsDps) {
                new SDialogRepBizPartnerStatements(miClient, SDataConstantsSys.BPSS_CT_BP_CUS, true).setVisible(true);
            }
            else if (item == jmiRepBizPartnerJournal) {
                new SDialogRepBizPartnerJournal(miClient, SDataConstantsSys.BPSS_CT_BP_CUS).setVisible(true);
            }
            else if (item == jmiRepBizPartnerAccountingMoves) {
                new SDialogRepBizPartnerAccountingMoves(miClient, SDataConstantsSys.BPSS_CT_BP_CUS).setVisible(true);
            }
            else if (item == jmiRepBizPartnerAccountingMovesDays) {
                new SDialogRepBizPartnerAccountingMoves(miClient, SDataConstantsSys.BPSS_CT_BP_CUS, true).setVisible(true);
            }
            else if (item == jmiRepDpsList) {
                moDialogRepDpsList.setParamIsSupplier(false);
                moDialogRepDpsList.formRefreshCatalogues();
                moDialogRepDpsList.formReset();
                moDialogRepDpsList.setFormVisible(true);
            }
            else if (item == jmiRepDpsBizPartner) {
                moDialogRepDpsBizPartner.setParamIsSupplier(false);
                moDialogRepDpsBizPartner.formRefreshCatalogues();
                moDialogRepDpsBizPartner.formReset();
                moDialogRepDpsBizPartner.setFormVisible(true);
            }
            else if (item == jmiRepDpsWithBalance) {
                moDialogRepDpsWithBalance.setParamIsSupplier(false);
                moDialogRepDpsWithBalance.formRefreshCatalogues();
                moDialogRepDpsWithBalance.formReset();
                moDialogRepDpsWithBalance.setFormVisible(true);
            }
            else if (item == jmiRepTrn) {
                moDialogRepSalesPurchases.setParamIsSupplier(false);
                moDialogRepSalesPurchases.formRefreshCatalogues();
                moDialogRepSalesPurchases.formReset();
                moDialogRepSalesPurchases.setFormVisible(true);
            }
            else if (item == jmiRepTrnConcept) {
                moDialogRepSalesPurchasesByConcept.formRefreshCatalogues();
                moDialogRepSalesPurchasesByConcept.formReset();
                moDialogRepSalesPurchasesByConcept.setParamIsSupplier(false);
                moDialogRepSalesPurchasesByConcept.setFormVisible(true);
            }
            else if (item == jmiRepTrnLocality) {
                moDialogRepSalesPurchasesByLocality.formRefreshCatalogues();
                moDialogRepSalesPurchasesByLocality.formReset();
                moDialogRepSalesPurchasesByLocality.setParamIsSupplier(false);
                moDialogRepSalesPurchasesByLocality.setFormVisible(true);
            }
            else if (item == jmiRepTrnComparative) {
                moDialogRepSalesPurchasesComparative.formReset();
                moDialogRepSalesPurchasesComparative.setFormVisible(true);
            }
            else if (item == jmiRepTrnDpsDetailBizPartner) {
                moDialogRepSalesPurchasesDetailByBizPartner.formRefreshCatalogues();
                moDialogRepSalesPurchasesDetailByBizPartner.formReset();
                moDialogRepSalesPurchasesDetailByBizPartner.setParamIsSupplier(false);
                moDialogRepSalesPurchasesDetailByBizPartner.setFormVisible(true);
            }
            else if (item == jmiRepTrnNetTotal) {
                moDialogRepSalesPurchasesNet.formRefreshCatalogues();
                moDialogRepSalesPurchasesNet.formReset();
                moDialogRepSalesPurchasesNet.setParamIsSupplier(false);
                moDialogRepSalesPurchasesNet.setParamIsNet(true);
                moDialogRepSalesPurchasesNet.setFormVisible(true);
            }
            else if (item == jmiRepTrnNetAnalytic) {
                moDialogRepSalesPurchasesNet.formRefreshCatalogues();
                moDialogRepSalesPurchasesNet.formReset();
                moDialogRepSalesPurchasesNet.setParamIsSupplier(false);
                moDialogRepSalesPurchasesNet.setParamIsNet(false);
                moDialogRepSalesPurchasesNet.setFormVisible(true);
            }
            else if (item == jmiRepTrnFileCsv) {
                moDialogRepSalesPurchasesFileCsv.formReset();
                moDialogRepSalesPurchasesFileCsv.setFormVisible(true);
            }
            else if (item == jmiRepTrnJournal) {
                moDialogRepSalesPurchasesJournal.formRefreshCatalogues();
                moDialogRepSalesPurchasesJournal.formReset();
                moDialogRepSalesPurchasesJournal.setParamIsSupplier(false);
                moDialogRepSalesPurchasesJournal.setFormVisible(true);
            }
            else if (item == jmiRepTrnItemUnitaryPrice) {
                moDialogRepSalesPurchasesItemUnitaryPrice.formRefreshCatalogues();
                moDialogRepSalesPurchasesItemUnitaryPrice.formReset();
                moDialogRepSalesPurchasesItemUnitaryPrice.setParamIsSupplier(false);
                moDialogRepSalesPurchasesItemUnitaryPrice.setFormVisible(true);
            }
            else if (item == jmiRepTrnContractStatus) {
                new SDialogRepContractStatus(miClient.getSession().getClient(), SDataConstantsSys.BPSS_CT_BP_CUS, "Reporte de estatus de contratos").setVisible(true);
            }
            else if (item == jmiRepTrnContractBackorderStock) {
                moDialogRepContractStock.formRefreshCatalogues();
                moDialogRepContractStock.formReset();
                moDialogRepContractStock.setParamIsSupplier(false);
                moDialogRepContractStock.setFormVisible(true);
            }
            else if (item == jmiRepTrnShipmentItem) {
                moDialogRepDpsShipmentItem.formRefreshCatalogues();
                moDialogRepDpsShipmentItem.formReset();
                moDialogRepDpsShipmentItem.setFormVisible(true);
            }
            else if (item == jmiRepMoneyIn) {
                moDialogRepDpsMoves.formRefreshCatalogues();
                moDialogRepDpsMoves.formReset();
                moDialogRepDpsMoves.setParamIsSupplier(false);
                moDialogRepDpsMoves.setFormVisible(true);
            }
        }
    }
}
