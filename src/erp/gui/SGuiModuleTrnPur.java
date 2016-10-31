/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.gui;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataRepConstants;
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
import erp.mfin.form.SDialogRepBizPartnerJournal;
import erp.mfin.form.SDialogRepBizPartnerStatement;
import erp.mfin.form.SFormCostCenterItem;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.bps.db.SBpsUtils;
import erp.mod.trn.form.SDialogRepContractStatus;
import erp.mtrn.data.SDataBizPartnerBlocking;
import erp.mtrn.data.SDataDiogDncDocumentNumberSeries;
import erp.mtrn.data.SDataDps;
import erp.mtrn.data.SDataDpsDncDocumentNumberSeries;
import erp.mtrn.form.SDialogRepBizPartnerBalanceAging;
import erp.mtrn.form.SDialogRepContractStock;
import erp.mtrn.form.SDialogRepDpsBizPartner;
import erp.mtrn.form.SDialogRepDpsList;
import erp.mtrn.form.SDialogRepDpsWithBalance;
import erp.mtrn.form.SDialogRepPurchasesUnitaryCost;
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
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import sa.gui.util.SUtilConsts;
import sa.lib.gui.SGuiParams;

/**
 *
 * @author Sergio Flores
 */
public class SGuiModuleTrnPur extends erp.lib.gui.SGuiModule implements java.awt.event.ActionListener {

    private javax.swing.JMenu jmCat;
    private javax.swing.JMenuItem jmiCatDpsDncDocumentNumberSeries;
    private javax.swing.JMenuItem jmiCatDiogDncDocumentNumberSeries;
    private javax.swing.JMenuItem jmiCatBizPartnerBlocking;
    private javax.swing.JMenu jmCatCfg;
    private javax.swing.JMenuItem jmiCatCfgCostCenterItem;
    private javax.swing.JMenuItem jmiCatSendingDpsLog;
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
    private javax.swing.JMenu jmOrd;
    private javax.swing.JMenuItem jmiOrders;
    private javax.swing.JMenuItem jmiOrdersLinkPend;
    private javax.swing.JMenuItem jmiOrdersLinkPendEntry;
    private javax.swing.JMenuItem jmiOrdersLinked;
    private javax.swing.JMenuItem jmiOrdersLinkedEntry;
    private javax.swing.JMenuItem jmiOrdersLinksSource;
    private javax.swing.JMenuItem jmiOrdersLinksDestiny;
    private javax.swing.JMenuItem jmiOrdersAutPending;
    private javax.swing.JMenuItem jmiOrdersAutAutorized;
    private javax.swing.JMenuItem jmiOrdersAutRejected;
    private javax.swing.JMenuItem jmiOrdersPrice;
    private javax.swing.JMenuItem jmiOrdersPriceHist;
    private javax.swing.JMenuItem jmiOrdersFunctionalArea;
    private javax.swing.JMenuItem jmiOrdersUsr;
    private javax.swing.JMenuItem jmiOrdersMailPending;
    private javax.swing.JMenuItem jmiOrdersMailSent;
    private javax.swing.JMenu jmDps;
    private javax.swing.JMenuItem jmiDpsDoc;
    private javax.swing.JMenuItem jmiDpsEntry;
    private javax.swing.JMenuItem jmiDpsEntryRef;
    private javax.swing.JMenuItem jmiDpsLinksDestiny;
    private javax.swing.JMenuItem jmiDpsLinksTrace;
    private javax.swing.JMenuItem jmiDpsAutPending;
    private javax.swing.JMenuItem jmiDpsAutAutorized;
    private javax.swing.JMenuItem jmiDpsAutReject;
    private javax.swing.JMenuItem jmiDpsAudPending;
    private javax.swing.JMenuItem jmiDpsAudAudited;
    private javax.swing.JMenuItem jmiDpsAnnulled;
    private javax.swing.JMenuItem jmiDpsPrice;
    private javax.swing.JMenuItem jmiDpsPriceHist;
    private javax.swing.JMenu jmDpsAdj;
    private javax.swing.JMenuItem jmiDpsAdjDoc;
    private javax.swing.JMenuItem jmiDpsAdjDocAnn;
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
    private javax.swing.JMenu jmRepQueries;
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
    private javax.swing.JMenuItem jmiQryBizPartnerBalance;
    private javax.swing.JMenuItem jmiQryBizPartnerAccountsAging;
    private javax.swing.JMenuItem jmiRepBizPartnerBalance;
    private javax.swing.JMenuItem jmiRepBizPartnerBalanceDps;
    private javax.swing.JMenuItem jmiRepBizPartnerBalanceAging;
    private javax.swing.JMenuItem jmiRepAccountStatements;
    private javax.swing.JMenuItem jmiRepBizPartnerAccountingMoves;
    private javax.swing.JMenuItem jmiRepBizPartnerJournal;
    private javax.swing.JMenuItem jmiRepBizPartnerBalanceCollection;
    private javax.swing.JMenuItem jmiRepBizPartnerBalanceCollectionDps;
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
    private javax.swing.JMenuItem jmiRepTrnUnitaryCosts;

    private erp.mtrn.form.SFormDps moFormDps;
    private erp.mtrn.form.SFormDps moFormDpsRo;
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
    private erp.mtrn.form.SDialogRepPurchasesUnitaryCost moDialogRepPurchasesUnitaryCost;

    public SGuiModuleTrnPur(erp.client.SClientInterface client) {
        super(client, SDataConstants.MOD_PUR);
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
        jmiCatDpsDncDocumentNumberSeries = new JMenuItem("Folios de docs. de compras");
        jmiCatDiogDncDocumentNumberSeries = new JMenuItem("Folios de docs. de inventarios");
        jmiCatBizPartnerBlocking = new JMenuItem("Bloqueo de proveedores");
        jmCatCfg = new JMenu("Contabilización automática");
        jmiCatCfgCostCenterItem = new JMenuItem("Configuración de centros de costo vs. ítems");
        jmiCatSendingDpsLog = new JMenuItem("Bitácora de envíos de docs.");
        jmCatCfg.add(jmiCatCfgCostCenterItem);
        jmCat.add(jmiCatDpsDncDocumentNumberSeries);
        jmCat.add(jmiCatDiogDncDocumentNumberSeries);
        jmCat.addSeparator();
        jmCat.add(jmiCatBizPartnerBlocking);
        jmCat.addSeparator();
        jmCat.add(jmCatCfg);
        jmCat.addSeparator();
        jmCat.add(jmiCatSendingDpsLog);
        
        jmEst = new JMenu("Cotizaciones");
        jmiEstimates = new JMenuItem("Cotizaciones de compras");
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
        jmiContracts = new JMenuItem("Contratos de compras");
        jmiContractsLinkPend = new JMenuItem("Contratos por procesar");
        jmiContractsLinkPendEntry = new JMenuItem("Contratos por procesar a detalle");
        jmiContractsLinked = new JMenuItem("Contratos procesados");
        jmiContractsLinkedEntry = new JMenuItem("Contratos procesados a detalle");
        jmiContractsLinks = new JMenuItem("Vínculos de contratos como origen");
        jmiContractsLinkPendEntryPrice = new JMenuItem("Entregas mensuales de contratos por procesar");
        jmiContractsLinkedEntryPrice = new JMenuItem("Entregas mensuales de contratos procesados");
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

        jmOrd = new JMenu("Pedidos");
        jmiOrders = new JMenuItem("Pedidos de compras");
        jmiOrdersLinkPend = new JMenuItem("Pedidos por procesar");
        jmiOrdersLinkPendEntry = new JMenuItem("Pedidos por procesar a detalle");
        jmiOrdersLinked = new JMenuItem("Pedidos procesados");
        jmiOrdersLinkedEntry = new JMenuItem("Pedidos procesados a detalle");
        jmiOrdersLinksSource = new JMenuItem("Vínculos de pedidos como origen");
        jmiOrdersLinksDestiny = new JMenuItem("Vínculos de pedidos como destino");
        jmiOrdersAutPending = new JMenuItem("Pedidos por autorizar");
        jmiOrdersAutAutorized = new JMenuItem("Pedidos autorizados");
        jmiOrdersAutRejected = new JMenuItem("Pedidos rechazados");
        jmiOrdersPrice = new JMenuItem("Precios de compras");
        jmiOrdersPriceHist = new JMenuItem("Historial de precios de compras");
        jmiOrdersFunctionalArea = new JMenuItem("Control de límite máximo mensual por área funcional");
        jmiOrdersUsr = new JMenuItem("Control de límite máximo mensual por usuario");
        jmiOrdersMailPending = new JMenuItem("Pedidos por enviar por correo-e");
        jmiOrdersMailSent = new JMenuItem("Pedidos enviados por correo-e");
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
        jmOrd.add(jmiOrdersAutPending);
        jmOrd.add(jmiOrdersAutAutorized);
        jmOrd.add(jmiOrdersAutRejected);
        jmOrd.addSeparator();
        jmOrd.add(jmiOrdersPrice);
        jmOrd.add(jmiOrdersPriceHist);
        jmOrd.addSeparator();
        jmOrd.add(jmiOrdersFunctionalArea);
        jmOrd.add(jmiOrdersUsr);
        jmOrd.addSeparator();
        jmOrd.add(jmiOrdersMailPending);
        jmOrd.add(jmiOrdersMailSent);

        jmDps = new JMenu("Facturas");
        jmiDpsDoc = new JMenuItem("Facturas de compras");
        jmiDpsEntry = new JMenuItem("Facturas de compras a detalle");
        jmiDpsEntryRef = new JMenuItem("Referencias de partidas de facturas");
        jmiDpsLinksDestiny = new JMenuItem("Vínculos de facturas como destino");
        jmiDpsLinksTrace = new JMenuItem("Rastreo de vínculos de facturas");
        jmiDpsAutPending = new JMenuItem("Facturas por autorizar");
        jmiDpsAutAutorized = new JMenuItem("Facturas autorizadas");
        jmiDpsAutReject = new JMenuItem("Facturas rechazadas");
        jmiDpsAudPending = new JMenuItem("Facturas por auditar");
        jmiDpsAudAudited = new JMenuItem("Facturas auditadas");
        jmiDpsAnnulled = new JMenuItem("Facturas anuladas");
        jmiDpsPrice = new JMenuItem("Precios de compras");
        jmiDpsPriceHist = new JMenuItem("Historial de precios de compras");
        jmDps.add(jmiDpsDoc);
        jmDps.add(jmiDpsEntry);
        jmDps.add(jmiDpsEntryRef);
        jmDps.addSeparator();
        jmDps.add(jmiDpsLinksDestiny);
        jmDps.add(jmiDpsLinksTrace);
        jmDps.addSeparator();
        jmDps.add(jmiDpsAutPending);
        jmDps.add(jmiDpsAutAutorized);
        jmDps.add(jmiDpsAutReject);
        jmDps.addSeparator();
        jmDps.add(jmiDpsAudPending);
        jmDps.add(jmiDpsAudAudited);
        jmDps.addSeparator();
        jmDps.add(jmiDpsAnnulled);
        jmDps.addSeparator();
        jmDps.add(jmiDpsPrice);
        jmDps.add(jmiDpsPriceHist);

        jmDpsAdj = new JMenu("Notas crédito");
        jmiDpsAdjDoc = new JMenuItem("Notas de crédito de compras");
        jmiDpsAdjDocAnn = new JMenuItem("Notas de crédito anuladas");
        jmDpsAdj.add(jmiDpsAdjDoc);
        jmDpsAdj.addSeparator();
        jmDpsAdj.add(jmiDpsAdjDocAnn);

        jmStkDvy = new JMenu("Surtidos");
        jmiStkDvyPend = new JMenuItem("Compras por surtir");
        jmiStkDvyPendEntry = new JMenuItem("Compras por surtir a detalle");
        jmiStkDvySupplied = new JMenuItem("Compras surtidas");
        jmiStkDvySuppliedEntry = new JMenuItem("Compras surtidas a detalle");
        jmiStkDvyDiog = new JMenuItem("Documentos de surtidos de compras");
        jmiStkDvyStatsConsumption = new JMenuItem("Estadísticas de consumo de compras");
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
        jmiStkRetPending = new JMenuItem("Compras por devolver");
        jmiStkRetPendingEntry = new JMenuItem("Compras por devolver a detalle");
        jmiStkRetReturned = new JMenuItem("Compras devueltas");
        jmiStkRetReturnedEntry = new JMenuItem("Compras devueltas a detalle");
        jmiStkRetDiog = new JMenuItem("Documentos de devoluciones de compras");
        jmStkRet.add(jmiStkRetPending);
        jmStkRet.add(jmiStkRetPendingEntry);
        jmStkRet.addSeparator();
        jmStkRet.add(jmiStkRetReturned);
        jmStkRet.add(jmiStkRetReturnedEntry);
        jmStkRet.addSeparator();
        jmStkRet.add(jmiStkRetDiog);

        jmRep = new JMenu("Reportes");
        jmRepStats = new JMenu("Consultas de estadísticas de compras");
        jmRepQueries = new JMenu("Consultas de saldos de proveedores");
        jmiRepTrnGlobal = new JMenuItem("Compras globales");
        jmiRepTrnByMonth = new JMenuItem("Compras globales por mes");
        jmiRepTrnByItemGeneric = new JMenuItem("Compras por ítem genérico");
        jmiRepTrnByItemGenericBizPartner = new JMenuItem("Compras por ítem genérico-proveedor");
        jmiRepTrnByItem = new JMenuItem("Compras por ítem");
        jmiRepTrnByItemBizPartner = new JMenuItem("Compras por ítem-proveedor");
        jmiRepTrnByBizPartner = new JMenuItem("Compras por proveedor");
        jmiRepTrnByBizPartnerItem = new JMenuItem("Compras por proveedor-ítem");
        jmiRepTrnByBizPartnerType = new JMenuItem("Compras por tipo de proveedor");
        jmiRepTrnByBizPartnerTypeBizPartner = new JMenuItem("Compras por tipo de proveedor-proveedor");
        jmiRepTrnDpsByItemBizPartner = new JMenuItem("Documentos de compras por ítem-proveedor");
        jmRepBackorder = new JMenu("Consultas de backorder de compras");
        jmiRepBackorderContract = new JMenuItem("Backorder de contratos");
        jmiRepBackorderContractByItem = new JMenuItem("Backorder de contratos por ítem");
        jmiRepBackorderContractByItemBizPartner = new JMenuItem("Backorder de contratos por ítem-proveedor");
        jmiRepBackorderContractByItemBizPartnerBra = new JMenuItem("Backorder de contratos por ítem-proveedor sucursal");
        jmiRepBackorderOrder = new JMenuItem("Backorder de pedidos");
        jmiRepBackorderOrderByItem = new JMenuItem("Backorder de pedidos por ítem");
        jmiRepBackorderOrderByItemBizPartner = new JMenuItem("Backorder de pedidos por ítem-proveedor");
        jmiRepBackorderOrderByItemBizPartnerBra = new JMenuItem("Backorder de pedidos por ítem-proveedor sucursal");
        jmiQryBizPartnerBalance = new JMenuItem("Consulta de saldos de proveedores");
        jmiQryBizPartnerAccountsAging = new JMenuItem("Consulta de antigüedad de saldos de proveedores");
        jmiRepBizPartnerBalance = new JMenuItem("Saldos proveedores...");
        jmiRepBizPartnerBalanceDps = new JMenuItem("Saldos proveedores por documento...");
        jmiRepBizPartnerBalanceAging = new JMenuItem("Antigüedad de saldos de proveedores...");
        jmiRepAccountStatements = new JMenuItem("Estados de cuenta de proveedores...");
        jmiRepBizPartnerAccountingMoves = new JMenuItem("Movimientos contables de proveedores por documento...");
        jmiRepBizPartnerJournal = new JMenuItem("Reporte auxiliar de movimientos contables de proveedores...");
        jmiRepBizPartnerBalanceCollection = new JMenuItem("Reporte de cobranza esperada...");
        jmiRepBizPartnerBalanceCollectionDps = new JMenuItem("Reporte de cobranza esperada por documento...");
        jmiRepDpsList = new JMenuItem("Listado de facturas por período...");
        jmiRepDpsBizPartner = new JMenuItem("Reporte de facturas de proveedores...");
        jmiRepDpsWithBalance = new JMenuItem("Reporte de facturas con saldo de proveedores...");
        jmiRepTrn = new JMenuItem("Reporte de compras netas...");
        jmiRepTrnConcept = new JMenuItem("Reporte de compras netas por concepto...");
        jmiRepTrnLocality = new JMenuItem("Reporte de compras netas por zona geográfica...");
        jmiRepTrnComparative = new JMenuItem("Reporte comparativo de compras netas...");
        jmiRepTrnDpsDetailBizPartner = new JMenuItem("Reporte detallado de compras por proveedor...");
        jmiRepTrnNetTotal = new JMenuItem("Relación de compras netas por período...");
        jmiRepTrnNetAnalytic = new JMenuItem("Relación de compras, devoluciones y descuentos por período...");
        jmiRepTrnFileCsv = new JMenuItem("Archivo CSV de compras netas por período...");
        jmiRepTrnJournal = new JMenuItem("Reporte de diario de compras...");
        jmiRepTrnItemUnitaryPrice = new JMenuItem("Reporte de precios unitarios de compras...");
        jsRepTrn = new JPopupMenu.Separator();
        jmiRepTrnContractStatus = new JMenuItem("Reporte de estatus de contratos de compras...");
        jmiRepTrnContractBackorderStock = new JMenuItem("Reporte de backorder contratos de compras vs. existencias...");
        jsRepContract = new JPopupMenu.Separator();
        jmiRepTrnUnitaryCosts = new JMenuItem("Reporte de costos unitarios de compras...");
        
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
        jmRepQueries.add(jmiQryBizPartnerBalance);
        jmRepQueries.add(jmiQryBizPartnerAccountsAging);

        jmRep.add(jmRepStats);
        jmRep.add(jmRepBackorder);
        jmRep.add(jmRepQueries);
        jmRep.addSeparator();
        jmRep.add(jmiRepBizPartnerBalance);
        jmRep.add(jmiRepBizPartnerBalanceDps);
        jmRep.add(jmiRepBizPartnerBalanceAging);
        jmRep.add(jmiRepAccountStatements);
        jmRep.add(jmiRepBizPartnerAccountingMoves);
        jmRep.add(jmiRepBizPartnerJournal); // XXX needs to be checked prior to launch (sflores, 2016-03-14)
        //jmRep.addSeparator();
        //jmRep.add(jmiRepBizPartnerBalanceCollection); // report does not match with module, just for consistency (sflores, 2016-03-14)
        //jmRep.add(jmiRepBizPartnerBalanceCollectionDps); // report does not match with module, just for consistency (sflores, 2016-03-14)
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
        jmRep.add(jmiRepTrnUnitaryCosts);
        
        moDialogRepDpsList = new SDialogRepDpsList(miClient);
        moDialogRepDpsBizPartner = new SDialogRepDpsBizPartner(miClient);
        moDialogRepDpsWithBalance = new SDialogRepDpsWithBalance(miClient);
        moDialogRepSalesPurchases = new SDialogRepSalesPurchases(miClient);
        moDialogRepSalesPurchasesByConcept = new SDialogRepSalesPurchasesByConcept(miClient);
        moDialogRepSalesPurchasesByLocality = new SDialogRepSalesPurchasesByLocality(miClient);
        moDialogRepSalesPurchasesComparative = new SDialogRepSalesPurchasesComparative(miClient, SDataConstantsSys.TRNS_CT_DPS_PUR);
        moDialogRepSalesPurchasesDetailByBizPartner = new SDialogRepSalesPurchasesDetailByBizPartner(miClient);
        moDialogRepSalesPurchasesNet = new SDialogRepSalesPurchasesNet(miClient);
        moDialogRepSalesPurchasesFileCsv = new SDialogRepSalesPurchasesFileCsv(miClient, SDataConstantsSys.TRNS_CT_DPS_PUR);
        moDialogRepSalesPurchasesJournal = new SDialogRepSalesPurchasesJournal(miClient);
        moDialogRepSalesPurchasesItemUnitaryPrice = new SDialogRepSalesPurchasesPriceUnitary(miClient);
        moDialogRepContractStock = new SDialogRepContractStock(miClient);
        moDialogRepPurchasesUnitaryCost = new SDialogRepPurchasesUnitaryCost(miClient);

        jmiCatDpsDncDocumentNumberSeries.addActionListener(this);
        jmiCatDiogDncDocumentNumberSeries.addActionListener(this);
        jmiCatBizPartnerBlocking.addActionListener(this);
        jmiCatCfgCostCenterItem.addActionListener(this);
        jmiCatSendingDpsLog.addActionListener(this);
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
        jmiOrders.addActionListener(this);
        jmiOrdersLinkPend.addActionListener(this);
        jmiOrdersLinked.addActionListener(this);
        jmiOrdersLinkPendEntry.addActionListener(this);
        jmiOrdersLinkedEntry.addActionListener(this);
        jmiOrdersLinksSource.addActionListener(this);
        jmiOrdersLinksDestiny.addActionListener(this);
        jmiOrdersAutPending.addActionListener(this);
        jmiOrdersAutAutorized.addActionListener(this);
        jmiOrdersAutRejected.addActionListener(this);
        jmiOrdersPrice.addActionListener(this);
        jmiOrdersPriceHist.addActionListener(this);
        jmiOrdersFunctionalArea.addActionListener(this);
        jmiOrdersUsr.addActionListener(this);
        jmiOrdersMailPending.addActionListener(this);
        jmiOrdersMailSent.addActionListener(this);
        jmiDpsDoc.addActionListener(this);
        jmiDpsEntry.addActionListener(this);
        jmiDpsEntryRef.addActionListener(this);
        jmiDpsLinksDestiny.addActionListener(this);
        jmiDpsLinksTrace.addActionListener(this);
        jmiDpsAutPending.addActionListener(this);
        jmiDpsAutAutorized.addActionListener(this);
        jmiDpsAutReject.addActionListener(this);
        jmiDpsAudPending.addActionListener(this);
        jmiDpsAudAudited.addActionListener(this);
        jmiDpsAnnulled.addActionListener(this);
        jmiDpsPrice.addActionListener(this);
        jmiDpsPriceHist.addActionListener(this);
        jmiDpsAdjDoc.addActionListener(this);
        jmiDpsAdjDocAnn.addActionListener(this);
        jmiStkDvyPend.addActionListener(this);
        jmiStkDvyPendEntry.addActionListener(this);
        jmiStkDvySupplied.addActionListener(this);
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
        jmiQryBizPartnerBalance.addActionListener(this);
        jmiQryBizPartnerAccountsAging.addActionListener(this);
        jmiRepBizPartnerBalance.addActionListener(this);
        jmiRepBizPartnerBalanceDps.addActionListener(this);
        jmiRepBizPartnerBalanceAging.addActionListener(this);
        jmiRepBizPartnerBalanceCollection.addActionListener(this);
        jmiRepBizPartnerBalanceCollectionDps.addActionListener(this);
        jmiRepAccountStatements.addActionListener(this);
        jmiRepBizPartnerJournal.addActionListener(this);
        jmiRepBizPartnerAccountingMoves.addActionListener(this);
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
        jmiRepTrnUnitaryCosts.addActionListener(this);

        hasRightDnsDps = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DPS_DNS).HasRight;
        hasRightDnsDiog = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_DIOG_CFG).HasRight;
        hasRightDocEstimate = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_EST).HasRight;
        hasRightDocOrder = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_ORD).HasRight;
        hasRightDocOrderAuthorize = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_ORD_AUTHORN).HasRight ||
                miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_ORD_REJECT_OWN).HasRight;
        hasRightDocTransaction = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_TRN).HasRight;
        hasRightDocTransactionAdjust = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_TRN_ADJ).HasRight;
        hasRightBizPartnerBlocking = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_BP_BLOCK).HasRight;
        hasRightInventoryIn = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_IN_PUR).HasRight ||
                miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DIOG_IN).HasRight;
        hasRightInventoryOut = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_OUT_PUR).HasRight ||
                miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DIOG_OUT).HasRight;
        hasRightReports = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_REP).HasRight;
        hasRightItemConfig =
                hasRightDocOrder && miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_ORD).Level == SUtilConsts.LEV_MANAGER ||
                hasRightDocTransaction && miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_TRN).Level == SUtilConsts.LEV_MANAGER;
        levelRightDocOrder = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_ORD).Level;
        levelRightDocTransaction = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_TRN).Level;

        jmCat.setEnabled(hasRightDnsDps || hasRightDnsDiog || hasRightBizPartnerBlocking || hasRightItemConfig);
        jmiCatDpsDncDocumentNumberSeries.setEnabled(hasRightDnsDps);
        jmiCatDiogDncDocumentNumberSeries.setEnabled(hasRightDnsDiog);
        jmiCatBizPartnerBlocking.setEnabled(hasRightBizPartnerBlocking);
        jmCatCfg.setEnabled(hasRightItemConfig);
        jmiCatSendingDpsLog.setEnabled(hasRightDocOrder);
        jmEst.setEnabled(hasRightDocEstimate);

        jmCon.setEnabled(hasRightDocEstimate);

        jmOrd.setEnabled(hasRightDocOrder || hasRightDocOrderAuthorize);
        jmiOrdersPrice.setEnabled(hasRightDocOrder && levelRightDocOrder >= SUtilConsts.LEV_AUTHOR);
        jmiOrdersPriceHist.setEnabled(hasRightDocOrder && levelRightDocOrder >= SUtilConsts.LEV_AUTHOR);
        jmiOrdersFunctionalArea.setEnabled(hasRightDocTransaction && levelRightDocTransaction == SUtilConsts.LEV_MANAGER);
        jmiOrdersUsr.setEnabled(hasRightDocTransaction && levelRightDocTransaction == SUtilConsts.LEV_MANAGER);
        jmiOrdersMailPending.setEnabled(hasRightDocOrder);
        jmiOrdersMailSent.setEnabled(hasRightDocOrder);

        jmDps.setEnabled(hasRightDocTransaction);
        jmiDpsDoc.setEnabled(hasRightDocTransaction);
        jmiDpsEntry.setEnabled(hasRightDocTransaction);
        jmiDpsEntryRef.setEnabled(hasRightDocTransaction);
        jmiDpsLinksDestiny.setEnabled(hasRightDocTransaction);
        jmiDpsLinksTrace.setEnabled(hasRightDocTransaction);
        jmiDpsAudPending.setEnabled(hasRightDocTransaction && levelRightDocTransaction == SUtilConsts.LEV_MANAGER);
        jmiDpsAudAudited.setEnabled(hasRightDocTransaction && levelRightDocTransaction == SUtilConsts.LEV_MANAGER);
        jmiDpsAnnulled.setEnabled(hasRightDocTransaction && levelRightDocTransaction == SUtilConsts.LEV_MANAGER);
        jmiDpsPrice.setEnabled(hasRightDocTransaction && levelRightDocTransaction >= SUtilConsts.LEV_AUTHOR);
        jmiDpsPriceHist.setEnabled(hasRightDocTransaction && levelRightDocTransaction >= SUtilConsts.LEV_AUTHOR);

        jmDpsAdj.setEnabled(hasRightDocTransactionAdjust);
        jmiDpsAdjDoc.setEnabled(hasRightDocTransactionAdjust);
        jmiDpsAdjDocAnn.setEnabled(hasRightDocTransactionAdjust);

        jmStkDvy.setEnabled(hasRightInventoryIn);
        jmiStkDvyPend.setEnabled(hasRightInventoryIn);
        jmiStkDvyPendEntry.setEnabled(hasRightInventoryIn);
        jmiStkDvySupplied.setEnabled(hasRightInventoryIn);
        jmiStkDvySuppliedEntry.setEnabled(hasRightInventoryIn);
        jmiStkDvyDiog.setEnabled(hasRightInventoryIn);
        jmiStkDvyStatsConsumption.setEnabled(hasRightInventoryIn);

        jmStkRet.setEnabled(hasRightInventoryOut);
        jmiStkRetPending.setEnabled(hasRightInventoryOut);
        jmiStkRetPendingEntry.setEnabled(hasRightInventoryOut);
        jmiStkRetReturned.setEnabled(hasRightInventoryOut);
        jmiStkRetReturnedEntry.setEnabled(hasRightInventoryOut);
        jmiStkRetDiog.setEnabled(hasRightInventoryOut);

        jmRep.setEnabled(hasRightReports);
        
        // GUI configuration:
        
        if (((erp.SClient) miClient).getCfgProcesor() != null) {
            SCfgModule module = new SCfgModule("" + mnModuleType);
            SCfgMenu menu = null;
            SCfgMenuSection section = null;
            
            menu = new SCfgMenu(jmEst, "" + SDataConstants.MOD_PUR_EST);
            module.getChildMenus().add(menu);
            
            menu = new SCfgMenu(jmCon, "" + SDataConstants.MOD_PUR_CON);
            module.getChildMenus().add(menu);
            
            menu = new SCfgMenu(jmStkDvy, "" + SDataConstants.MOD_PUR_DVY);
            module.getChildMenus().add(menu);
            
            menu = new SCfgMenu(jmStkRet, "" + SDataConstants.MOD_PUR_RET);
            module.getChildMenus().add(menu);
            
            menu = new SCfgMenu(jmRep, "" + SDataConstants.MOD_PUR_REP);
            section = new SCfgMenuSection("" + SDataConstants.MOD_PUR_REP_CON);
            section.getChildItems().add(new SCfgMenuSectionItem(jmiRepTrnContractStatus, "" + SDataConstants.TRNS_ST_DPS));
            section.getChildItems().add(new SCfgMenuSectionItem(jmiRepTrnContractBackorderStock, "" + SDataConstants.TRN_STK));
            section.setChildSeparator(new SCfgMenuSectionSeparator(jsRepTrn)); // previous separator
            menu.getChildSections().add(section);
            section = new SCfgMenuSection("" + SDataConstants.MOD_PUR_REP_CST);
            section.getChildItems().add(new SCfgMenuSectionItem(jmiRepTrnUnitaryCosts, "" + SModConsts.LOG_SHIP));
            section.setChildSeparator(new SCfgMenuSectionSeparator(jsRepContract)); // previous separator
            menu.getChildSections().add(section);
            module.getChildMenus().add(menu);
            
            ((erp.SClient) miClient).getCfgProcesor().processModule(module);
        }
    }

    private java.lang.String getViewTitle(int type) {
        String viewTitle = "" ;

        switch (type) {
            case SDataConstantsSys.TRNX_DPS_PUR_ORD_AUT_PEND:
                viewTitle = "CPA - " + SDataConstantsSys.getDpsTypeNamePlr(SDataConstantsSys.TRNX_TP_DPS_ORD) + " x autorizar";
                break;
            case SDataConstantsSys.TRNX_DPS_PUR_ORD_AUT_AUT:
                viewTitle = "CPA - " + SDataConstantsSys.getDpsTypeNamePlr(SDataConstantsSys.TRNX_TP_DPS_ORD) + " autorizad@s";
                break;
            case SDataConstantsSys.TRNX_DPS_PUR_ORD_AUT_REJ:
                viewTitle = "CPA - " + SDataConstantsSys.getDpsTypeNamePlr(SDataConstantsSys.TRNX_TP_DPS_ORD) + " rechazad@s";
                break;
            case SDataConstantsSys.TRNX_DPS_PUR_DOC_AUT_PEND:
                viewTitle = "CPA - " + SDataConstantsSys.getDpsTypeNamePlr(SDataConstantsSys.TRNX_TP_DPS_DOC) + " x autorizar";
                break;
            case SDataConstantsSys.TRNX_DPS_PUR_DOC_AUT_AUT:
                viewTitle = "CPA - " + SDataConstantsSys.getDpsTypeNamePlr(SDataConstantsSys.TRNX_TP_DPS_DOC) + " autorizad@s";
                break;
            case SDataConstantsSys.TRNX_DPS_PUR_DOC_AUT_REJ:
                viewTitle = "CPA - " + SDataConstantsSys.getDpsTypeNamePlr(SDataConstantsSys.TRNX_TP_DPS_DOC) + " rechazad@s";
                break;
            case SDataConstantsSys.TRNX_PUR_TOT:
                viewTitle = "Compras globales";
                break;
            case SDataConstantsSys.TRNX_PUR_TOT_MONTH:
                viewTitle = "Compras globales x mes";
                break;
            case SDataConstantsSys.TRNX_PUR_TOT_BY_BP:
                viewTitle = "Compras x proveedor";
                break;
            case SDataConstantsSys.TRNX_PUR_TOT_BY_BP_ITEM:
                viewTitle = "Compras x proveedor-ítem";
                break;
            case SDataConstantsSys.TRNX_PUR_TOT_BY_IGEN:
                viewTitle = "Compras x ítem genérico";
                break;
            case SDataConstantsSys.TRNX_PUR_TOT_BY_IGEN_BP:
                viewTitle = "Compras x ítem genérico-proveedor";
                break;
            case SDataConstantsSys.TRNX_PUR_TOT_BY_ITEM:
                viewTitle = "Compras x ítem";
                break;
            case SDataConstantsSys.TRNX_PUR_TOT_BY_ITEM_BP:
                viewTitle = "Compras x ítem-proveedor";
                break;
            case SDataConstantsSys.TRNX_PUR_TOT_BY_TP_BP:
                viewTitle = "Compras x tipo proveedor";
                break;
            case SDataConstantsSys.TRNX_PUR_TOT_BY_TP_BP_BP:
                viewTitle = "Compras x tipo proveedor-proveedor";
                break;
           case SDataConstantsSys.TRNX_PUR_DPS_BY_ITEM_BP_FIL:
                viewTitle = "Compras docs. x ítem-proveedor";
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
                    miForm.setValue(SDataConstantsSys.VALUE_IS_PUR, true);
                    break;
                case SDataConstants.TRN_DNC_DIOG_DNS:
                    if (moFormDncDocumentNumberSeriesDiog == null) {
                        moFormDncDocumentNumberSeriesDiog = new SFormDncDocumentNumberSeries(miClient, formType);
                    }
                    if (pk != null) {
                        moRegistry = new SDataDiogDncDocumentNumberSeries();
                    }
                    miForm = moFormDncDocumentNumberSeriesDiog;
                    miForm.setValue(SDataConstantsSys.VALUE_IS_PUR, true);
                    break;
                case SDataConstants.TRN_BP_BLOCK:
                    if (moFormBizPartnerBlocking == null) {
                        moFormBizPartnerBlocking = new SFormBizPartnerBlocking(miClient);
                    }
                    miForm = moFormBizPartnerBlocking;
                    if (pk != null) {
                        moRegistry = new SDataBizPartnerBlocking();
                    }
                    miForm.setValue(1, SDataConstantsSys.BPSS_CT_BP_SUP);
                    break;
                case SDataConstants.TRN_DPS:
                    if (moFormDps == null) {
                        moFormDps = new SFormDps(miClient, SDataConstantsSys.TRNS_CT_DPS_PUR);
                    }
                    miForm = moFormDps;
                    if (pk != null) {
                        moRegistry = new SDataDps();
                    }
                    break;
                case SDataConstants.TRNX_DPS_RO:
                    if (moFormDpsRo == null) {
                        moFormDpsRo = new SFormDps(miClient, SDataConstantsSys.TRNS_CT_DPS_PUR);
                    }
                    miForm = moFormDpsRo;
                    if (pk != null) {
                        moRegistry = new SDataDps();
                    }
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
                                    if (SLibUtilities.compareKeys(((Object[]) moFormComplement)[0], SDataConstantsSys.TRNU_TP_DPS_PUR_ORD)) {
                                        miForm.setValue(SDataConstantsSys.TRNX_TP_DPS_EST, ((Object[]) moFormComplement)[1]);
                                    }
                                    else if (SLibUtilities.compareKeys(((Object[]) moFormComplement)[0], SDataConstantsSys.TRNU_TP_DPS_PUR_INV)) {
                                        miForm.setValue(SDataConstantsSys.TRNX_TP_DPS_ORD, ((Object[]) moFormComplement)[1]);
                                    }
                                    else if (SLibUtilities.compareKeys(((Object[]) moFormComplement)[0], SDataConstantsSys.TRNU_TP_DPS_PUR_CN)) {
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
                    miForm.setValue(SLibConstants.VALUE_TYPE, moFormComplement);    // int[] document type
                    miForm.setValue(SLibConstants.VALUE_STATUS, true);              // editable status
                    break;

                default:
            }

            result = processForm(pk, isCopy);
            clearFormComplement();
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
                            sViewTitle = "Folios docs. compras";
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

                case SDataConstants.TRN_DPS_SND_LOG:
                    oViewClass = erp.mtrn.view.SViewDpsSendingLog.class;
                    sViewTitle = "CPA - bitácora envíos docs.";
                    break;
                
                case SDataConstants.TRN_BP_BLOCK:
                    oViewClass = erp.mtrn.view.SViewBizPartnerBlocking.class;
                    sViewTitle = "Bloqueo proveedores";
                    break;

                case SDataConstants.FIN_CC_ITEM:
                    oViewClass = erp.mfin.view.SViewCostCenterItem.class;
                    sViewTitle = "Config. centros costo ítems";
                    break;
                    
                case SDataConstants.TRN_DPS:
                    oViewClass = erp.mtrn.view.SViewDps.class;
                    sViewTitle = "CPA - " + SDataConstantsSys.getDpsTypeNamePlr(auxType02);
                    break;

                case SDataConstants.TRNX_DPS_ETY_REF:
                    oViewClass = erp.mtrn.view.SViewDpsEntryReference.class;
                    sViewTitle = "CPA - " + SDataConstantsSys.getDpsTypeNamePlr(auxType02) + " (referencias part.)";
                    break;

                case SDataConstants.TRNX_DPS_LINK_PEND:
                    oViewClass = erp.mtrn.view.SViewDpsLink.class;
                    sViewTitle = "CPA - " + SDataConstantsSys.getDpsTypeNamePlr(auxType02) + " x procesar";
                    break;

                case SDataConstants.TRNX_DPS_LINK_PEND_ETY:
                    oViewClass = erp.mtrn.view.SViewDpsLink.class;
                    sViewTitle = "CPA - " + SDataConstantsSys.getDpsTypeNamePlr(auxType02) + " x procesar (detalle)";
                    break;

                case SDataConstants.TRNX_DPS_LINKED:
                    oViewClass = erp.mtrn.view.SViewDpsLink.class;
                    sViewTitle = "CPA - " + SDataConstantsSys.getDpsTypeNamePlr(auxType02) + " procesd@s";
                    break;

                case SDataConstants.TRNX_DPS_LINKED_ETY:
                    oViewClass = erp.mtrn.view.SViewDpsLink.class;
                    sViewTitle = "CPA - " + SDataConstantsSys.getDpsTypeNamePlr(auxType02) + " procesd@s (detalle)";
                    break;

                case SDataConstants.TRNX_DPS_LINKS:
                    oViewClass = erp.mtrn.view.SViewDpsLinksQuery.class;
                    sViewTitle = "CPA - Vínculos " + SDataConstantsSys.getLinkNamePlr(auxType02).toLowerCase();
                    break;

                case SDataConstants.TRNX_DPS_LINKS_TRACE:
                    oViewClass = erp.mtrn.view.SViewDpsLinksQueryTrace.class;
                    sViewTitle = "CPA - Rastreo vínculos " + SDataConstantsSys.getDpsTypeNamePlr(auxType01).toLowerCase();
                    break;

                case SDataConstants.TRNX_DPS_AUTHORIZE_PEND:
                    oViewClass = erp.mtrn.view.SViewDpsPendAuthorized.class;
                    sViewTitle = getViewTitle(auxType01);
                    break;

                case SDataConstants.TRNX_DPS_AUDIT_PEND:
                    oViewClass = erp.mtrn.view.SViewDpsAudit.class;
                    sViewTitle = "CPA - " + SDataConstantsSys.getDpsTypeNamePlr(auxType02) + " x auditar";
                    break;

                case SDataConstants.TRNX_DPS_AUDITED:
                    oViewClass = erp.mtrn.view.SViewDpsAudit.class;
                    sViewTitle = "CPA - " + SDataConstantsSys.getDpsTypeNamePlr(auxType02) + " auditad@s";
                    break;
                
                case SDataConstants.TRNU_TP_DPS_ANN:
                    oViewClass = erp.mtrn.view.SViewDpsAudit.class;
                    sViewTitle = "CPA - " + SDataConstantsSys.getDpsTypeNamePlr(auxType02) + " anulad@s";
                    break;

                case SDataConstants.TRNX_DPS_SEND_PEND:
                    oViewClass = erp.mtrn.view.SViewDpsSend.class;
                    sViewTitle = "CPA - " + SDataConstantsSys.getDpsTypeNamePlr(auxType02) + " x enviar x correo-e";
                    break;

                case SDataConstants.TRNX_DPS_SENT:
                    oViewClass = erp.mtrn.view.SViewDpsSend.class;
                    sViewTitle = "CPA - " + SDataConstantsSys.getDpsTypeNamePlr(auxType02) + " enviad@s x correo-e";
                    break;

                case SDataConstants.TRNX_PRICE_HIST:
                    oViewClass = erp.mtrn.view.SViewPriceHistory.class;
                    sViewTitle = "CPA - Historial precios";
                    break;
                    
                case SDataConstants.MKT_PLIST_ITEM:
                    oViewClass = erp.mmkt.view.SViewPriceListItem.class;
                    sViewTitle = "CPA - Precios de ítems";
                    break;

                case SDataConstants.TRNX_DPS_BACKORDER:
                    switch (auxType01) {
                        case SDataConstantsSys.TRNX_PUR_BACKORDER_CON:
                        case SDataConstantsSys.TRNX_PUR_BACKORDER_ORD:
                            oViewClass = erp.mtrn.view.SViewBackorder.class;
                            if (auxType02 == SDataConstantsSys.TRNS_CL_DPS_PUR_EST[1]) {
                                sViewTitle = "CPA - BO contratos";
                            }
                            else {
                                sViewTitle = "CPA - BO pedidos";
                            }
                            break;

                        case SDataConstantsSys.TRNX_PUR_BACKORDER_CON_ITEM:
                        case SDataConstantsSys.TRNX_PUR_BACKORDER_ORD_ITEM:
                            oViewClass = erp.mtrn.view.SViewBackorder.class;
                            if (auxType02 == SDataConstantsSys.TRNS_CL_DPS_PUR_EST[1]) {
                                sViewTitle = "CPA - BO contratos x ítem";
                            }
                            else {
                                sViewTitle = "CPA - BO pedidos x ítem";
                            }
                            break;

                        case SDataConstantsSys.TRNX_PUR_BACKORDER_CON_ITEM_BP:
                        case SDataConstantsSys.TRNX_PUR_BACKORDER_ORD_ITEM_BP:
                            oViewClass = erp.mtrn.view.SViewBackorder.class;
                            if (auxType02 == SDataConstantsSys.TRNS_CL_DPS_PUR_EST[1]) {
                                sViewTitle = "CPA - BO contratos x ítem-cliente";
                            }
                            else {
                                sViewTitle = "CPA - BO pedidos x ítem-cliente";
                            }
                            break;
                            
                        case SDataConstantsSys.TRNX_PUR_BACKORDER_CON_ITEM_BP_BRA:
                        case SDataConstantsSys.TRNX_PUR_BACKORDER_ORD_ITEM_BP_BRA:
                            oViewClass = erp.mtrn.view.SViewBackorder.class;
                            if (auxType02 == SDataConstantsSys.TRNS_CL_DPS_PUR_EST[1]) {
                                sViewTitle = "CPA - BO contratos x ítem-cliente suc.";
                            }
                            else {
                                sViewTitle = "CPA - BO pedidos x ítem-cliente suc.";
                            }
                            break;
                            
                        default:
                    }
                    break;

                case SDataConstants.TRNX_DPS_BAL_AGING:
                    oViewClass = erp.mtrn.view.SViewDpsBalanceAging.class;
                    sViewTitle = "Antigüedad saldos proveedores";
                    break;

                case SDataConstants.TRNX_DPS_PAY_PEND:
                    oViewClass = erp.mtrn.view.SViewDpsPay.class;
                    sViewTitle = "Cuentas x pagar";
                    break;

                case SDataConstants.TRNX_DPS_PAYED:
                    oViewClass = erp.mtrn.view.SViewDpsPay.class;
                    sViewTitle = "Cuentas pagadas";
                    break;

                case SDataConstants.TRNX_DPS_QRY:
                    switch (auxType01) {
                        case SDataConstantsSys.TRNX_PUR_TOT:
                            oViewClass = erp.mtrn.view.SViewQueryGlobal.class;
                            sViewTitle = getViewTitle(auxType01);
                            break;
                        case SDataConstantsSys.TRNX_PUR_TOT_MONTH:
                            oViewClass = erp.mtrn.view.SViewQueryGlobalByMonth.class;
                            sViewTitle = getViewTitle(auxType01);
                            break;
                        case SDataConstantsSys.TRNX_PUR_TOT_BY_BP:
                        case SDataConstantsSys.TRNX_PUR_TOT_BY_BP_ITEM:
                        case SDataConstantsSys.TRNX_PUR_TOT_BY_IGEN:
                        case SDataConstantsSys.TRNX_PUR_TOT_BY_IGEN_BP:
                        case SDataConstantsSys.TRNX_PUR_TOT_BY_ITEM:
                        case SDataConstantsSys.TRNX_PUR_TOT_BY_ITEM_BP:
                        case SDataConstantsSys.TRNX_PUR_TOT_BY_TP_BP:
                        case SDataConstantsSys.TRNX_PUR_TOT_BY_TP_BP_BP:
                            oViewClass = erp.mtrn.view.SViewQueryTotal.class;
                            sViewTitle = getViewTitle(auxType01);
                            break;
                       case SDataConstantsSys.TRNX_PUR_DPS_BY_ITEM_BP_ALL:
                       case SDataConstantsSys.TRNX_PUR_DPS_BY_ITEM_BP_FIL:
                            oViewClass = erp.mtrn.view.SViewQueryDpsByItemBizPartner.class;
                            
                            if (auxType01 == SDataConstantsSys.TRNX_PUR_DPS_BY_ITEM_BP_ALL) {
                                sViewTitle = "CPA - Facturas (detalle)";
                            }
                            else {
                                sViewTitle = getViewTitle(auxType01);
                            }
                            break;
                        default:
                            throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_VIEW);
                    }
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

        try {
            switch (registryType) {
                case SDataConstants.TRN_DPS:
                    moRegistry = new SDataDps();
                    break;
                default:
                    throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
            }

            result = processActionAnnul(pk, true);
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
                showView(SDataConstants.TRN_DNC_DPS_DNS, SDataConstantsSys.TRNS_CT_DPS_PUR);
            }
            else if (item == jmiCatDiogDncDocumentNumberSeries) {
                showView(SDataConstants.TRN_DNC_DIOG_DNS, SDataConstantsSys.TRNS_CT_IOG_IN);
            }
            else if (item == jmiCatBizPartnerBlocking) {
                showView(SDataConstants.TRN_BP_BLOCK, SDataConstantsSys.BPSS_CT_BP_SUP);
            }
            else if (item == jmiCatCfgCostCenterItem) {
                showView(SDataConstants.FIN_CC_ITEM);
            }
            else if (item == jmiCatSendingDpsLog) {
                showView(SDataConstants.TRN_DPS_SND_LOG, SDataConstantsSys.TRNS_CT_DPS_PUR);
            }
            else if (item == jmiEstimates) {
                showView(SDataConstants.TRN_DPS, SDataConstantsSys.TRNS_CT_DPS_PUR, SDataConstantsSys.TRNX_TP_DPS_EST_EST);
            }
            else if (item == jmiEstimatesLinkPend) {
                showView(SDataConstants.TRNX_DPS_LINK_PEND, SDataConstantsSys.TRNS_CT_DPS_PUR, SDataConstantsSys.TRNX_TP_DPS_EST_EST);
            }
            else if (item == jmiEstimatesLinkPendEntry) {
                showView(SDataConstants.TRNX_DPS_LINK_PEND_ETY, SDataConstantsSys.TRNS_CT_DPS_PUR, SDataConstantsSys.TRNX_TP_DPS_EST_EST);
            }
            else if (item == jmiEstimatesLinked) {
                showView(SDataConstants.TRNX_DPS_LINKED, SDataConstantsSys.TRNS_CT_DPS_PUR, SDataConstantsSys.TRNX_TP_DPS_EST_EST);
            }
            else if (item == jmiEstimatesLinkedEntry) {
                showView(SDataConstants.TRNX_DPS_LINKED_ETY, SDataConstantsSys.TRNS_CT_DPS_PUR, SDataConstantsSys.TRNX_TP_DPS_EST_EST);
            }
            else if (item == jmiEstimatesLinks) {
                showView(SDataConstants.TRNX_DPS_LINKS, SDataConstantsSys.TRNS_CT_DPS_PUR, SDataConstantsSys.TRNX_LINK_EST_EST_SRC);
            }
            else if (item == jmiContracts) {
                showView(SDataConstants.TRN_DPS, SDataConstantsSys.TRNS_CT_DPS_PUR, SDataConstantsSys.TRNX_TP_DPS_EST_CON);
            }
            else if (item == jmiContractsLinkPend) {
                showView(SDataConstants.TRNX_DPS_LINK_PEND, SDataConstantsSys.TRNS_CT_DPS_PUR, SDataConstantsSys.TRNX_TP_DPS_EST_CON);
            }
            else if (item == jmiContractsLinkPendEntry) {
                showView(SDataConstants.TRNX_DPS_LINK_PEND_ETY, SDataConstantsSys.TRNS_CT_DPS_PUR, SDataConstantsSys.TRNX_TP_DPS_EST_CON);
            }
            else if (item == jmiContractsLinked) {
                showView(SDataConstants.TRNX_DPS_LINKED, SDataConstantsSys.TRNS_CT_DPS_PUR, SDataConstantsSys.TRNX_TP_DPS_EST_CON);
            }
            else if (item == jmiContractsLinkedEntry) {
                showView(SDataConstants.TRNX_DPS_LINKED_ETY, SDataConstantsSys.TRNS_CT_DPS_PUR, SDataConstantsSys.TRNX_TP_DPS_EST_CON);
            }
            else if (item == jmiContractsLinks) {
                showView(SDataConstants.TRNX_DPS_LINKS, SDataConstantsSys.TRNS_CT_DPS_PUR, SDataConstantsSys.TRNX_LINK_EST_CON_SRC);
            }
            else if (item == jmiContractsLinkPendEntryPrice) {
                miClient.getSession().showView(SModConsts.TRN_DPS_ETY_PRC, SModConsts.MOD_TRN_PUR_N, new SGuiParams(SModConsts.VIEW_ST_PEND));
            }
            else if (item == jmiContractsLinkedEntryPrice) {
                miClient.getSession().showView(SModConsts.TRN_DPS_ETY_PRC, SModConsts.MOD_TRN_PUR_N, new SGuiParams(SModConsts.VIEW_ST_DONE));
            }
            else if (item == jmiOrders) {
                showView(SDataConstants.TRN_DPS, SDataConstantsSys.TRNS_CT_DPS_PUR, SDataConstantsSys.TRNX_TP_DPS_ORD);
            }
            else if (item == jmiOrdersLinkPend) {
                showView(SDataConstants.TRNX_DPS_LINK_PEND, SDataConstantsSys.TRNS_CT_DPS_PUR, SDataConstantsSys.TRNX_TP_DPS_ORD);
            }
            else if (item == jmiOrdersLinkPendEntry) {
                showView(SDataConstants.TRNX_DPS_LINK_PEND_ETY, SDataConstantsSys.TRNS_CT_DPS_PUR, SDataConstantsSys.TRNX_TP_DPS_ORD);
            }
            else if (item == jmiOrdersLinked) {
                showView(SDataConstants.TRNX_DPS_LINKED, SDataConstantsSys.TRNS_CT_DPS_PUR, SDataConstantsSys.TRNX_TP_DPS_ORD);
            }
            else if (item == jmiOrdersLinkedEntry) {
                showView(SDataConstants.TRNX_DPS_LINKED_ETY, SDataConstantsSys.TRNS_CT_DPS_PUR, SDataConstantsSys.TRNX_TP_DPS_ORD);
            }
            else if (item == jmiOrdersLinksSource) {
                showView(SDataConstants.TRNX_DPS_LINKS, SDataConstantsSys.TRNS_CT_DPS_PUR, SDataConstantsSys.TRNX_LINK_ORD_SRC);
            }
            else if (item == jmiOrdersLinksDestiny) {
                showView(SDataConstants.TRNX_DPS_LINKS, SDataConstantsSys.TRNS_CT_DPS_PUR, SDataConstantsSys.TRNX_LINK_ORD_DES);
            }
            else if (item == jmiOrdersAutPending) {
                showView(SDataConstants.TRNX_DPS_AUTHORIZE_PEND, SDataConstantsSys.TRNX_DPS_PUR_ORD_AUT_PEND);
            }
            else if (item == jmiOrdersAutAutorized) {
                showView(SDataConstants.TRNX_DPS_AUTHORIZE_PEND, SDataConstantsSys.TRNX_DPS_PUR_ORD_AUT_AUT);
            }
            else if (item == jmiOrdersAutRejected) {
                showView(SDataConstants.TRNX_DPS_AUTHORIZE_PEND, SDataConstantsSys.TRNX_DPS_PUR_ORD_AUT_REJ);
            }
            else if (item == jmiOrdersMailPending) {
                showView(SDataConstants.TRNX_DPS_SEND_PEND, SDataConstantsSys.TRNS_CT_DPS_PUR, SDataConstantsSys.TRNX_TP_DPS_ORD);
            }
            else if (item == jmiOrdersMailSent) {
                showView(SDataConstants.TRNX_DPS_SENT, SDataConstantsSys.TRNS_CT_DPS_PUR, SDataConstantsSys.TRNX_TP_DPS_ORD);
            }
            else if (item == jmiDpsDoc) {
                showView(SDataConstants.TRN_DPS, SDataConstantsSys.TRNS_CT_DPS_PUR, SDataConstantsSys.TRNX_TP_DPS_DOC);
            }
            else if (item == jmiDpsEntry) {
                showView(SDataConstants.TRNX_DPS_QRY, SDataConstantsSys.TRNX_PUR_DPS_BY_ITEM_BP_ALL);
            }
            else if (item == jmiDpsEntryRef) {
                showView(SDataConstants.TRNX_DPS_ETY_REF, SDataConstantsSys.TRNS_CT_DPS_PUR, SDataConstantsSys.TRNX_TP_DPS_DOC);
            }
            else if (item == jmiDpsLinksDestiny) {
                showView(SDataConstants.TRNX_DPS_LINKS, SDataConstantsSys.TRNS_CT_DPS_PUR, SDataConstantsSys.TRNX_LINK_DOC_DES);
            }
            else if (item == jmiDpsLinksTrace) {
                showView(SDataConstants.TRNX_DPS_LINKS_TRACE, SDataConstantsSys.TRNS_CT_DPS_PUR);
            }
            else if (item == jmiDpsAutPending) {
                showView(SDataConstants.TRNX_DPS_AUTHORIZE_PEND, SDataConstantsSys.TRNX_DPS_PUR_DOC_AUT_PEND);
            }
            else if (item == jmiDpsAutAutorized) {
                showView(SDataConstants.TRNX_DPS_AUTHORIZE_PEND, SDataConstantsSys.TRNX_DPS_PUR_DOC_AUT_AUT);
            }
            else if (item == jmiDpsAutReject) {
                showView(SDataConstants.TRNX_DPS_AUTHORIZE_PEND, SDataConstantsSys.TRNX_DPS_PUR_DOC_AUT_REJ);
            }
            else if (item == jmiDpsAudPending) {
                showView(SDataConstants.TRNX_DPS_AUDIT_PEND, SDataConstantsSys.TRNS_CT_DPS_PUR, SDataConstantsSys.TRNX_TP_DPS_DOC);
            }
            else if (item == jmiDpsAudAudited) {
                showView(SDataConstants.TRNX_DPS_AUDITED, SDataConstantsSys.TRNS_CT_DPS_PUR, SDataConstantsSys.TRNX_TP_DPS_DOC);
            }
            else if (item == jmiDpsAnnulled) {
                showView(SDataConstants.TRNU_TP_DPS_ANN, SDataConstantsSys.TRNS_CT_DPS_PUR, SDataConstantsSys.TRNX_TP_DPS_DOC);
            }
            else if (item == jmiDpsPrice || item == jmiOrdersPrice) {
                showView(SDataConstants.MKT_PLIST_ITEM, SDataConstantsSys.TRNS_CT_DPS_PUR);
            }
            else if (item == jmiDpsPriceHist || item == jmiOrdersPriceHist) {
                showView(SDataConstants.TRNX_PRICE_HIST, SDataConstantsSys.TRNS_CT_DPS_PUR);
            }
            else if (item == jmiOrdersFunctionalArea) {
                 miClient.getSession().showView(SModConsts.TRNX_ORD_LIM_MAX, SModConsts.CFGU_FUNC, new SGuiParams(SModSysConsts.TRNS_CT_DPS_PUR));
            }
            else if (item == jmiOrdersUsr) {
                 miClient.getSession().showView(SModConsts.TRNX_ORD_LIM_MAX, SModConsts.USRU_USR, new SGuiParams(SModSysConsts.TRNS_CT_DPS_PUR));
            }
            else if (item == jmiDpsAdjDoc) {
                showView(SDataConstants.TRN_DPS, SDataConstantsSys.TRNS_CT_DPS_PUR, SDataConstantsSys.TRNX_TP_DPS_ADJ);
            }
            else if (item == jmiDpsAdjDocAnn) {
                showView(SDataConstants.TRNU_TP_DPS_ANN, SDataConstantsSys.TRNS_CT_DPS_PUR, SDataConstantsSys.TRNX_TP_DPS_ADJ);
            }
            else if (item == jmiStkDvyPend) {
                miClient.getGuiModule(SDataConstants.MOD_INV).showView(SDataConstants.TRNX_DPS_SUPPLY_PEND, SDataConstantsSys.TRNS_CL_DPS_PUR_DOC[0], SDataConstantsSys.TRNS_CL_DPS_PUR_DOC[1]);
            }
            else if (item == jmiStkDvyPendEntry) {
                miClient.getGuiModule(SDataConstants.MOD_INV).showView(SDataConstants.TRNX_DPS_SUPPLY_PEND_ETY, SDataConstantsSys.TRNS_CL_DPS_PUR_DOC[0], SDataConstantsSys.TRNS_CL_DPS_PUR_DOC[1]);
            }
            else if (item == jmiStkDvySupplied) {
                miClient.getGuiModule(SDataConstants.MOD_INV).showView(SDataConstants.TRNX_DPS_SUPPLIED, SDataConstantsSys.TRNS_CL_DPS_PUR_DOC[0], SDataConstantsSys.TRNS_CL_DPS_PUR_DOC[1]);
            }
            else if (item == jmiStkDvySuppliedEntry) {
                miClient.getGuiModule(SDataConstants.MOD_INV).showView(SDataConstants.TRNX_DPS_SUPPLIED_ETY, SDataConstantsSys.TRNS_CL_DPS_PUR_DOC[0], SDataConstantsSys.TRNS_CL_DPS_PUR_DOC[1]);
            }
            else if (item == jmiStkDvyDiog) {
                miClient.getGuiModule(SDataConstants.MOD_INV).showView(SDataConstants.TRN_DIOG, SDataConstantsSys.TRNS_CL_IOG_IN_PUR[0], SDataConstantsSys.TRNS_CL_IOG_IN_PUR[1]);
            }
            else if (item == jmiStkDvyStatsConsumption) {
                miClient.getGuiModule(SDataConstants.MOD_INV).showView(SDataConstants.TRNX_STK_COMSUME, SDataConstantsSys.TRNS_CT_DPS_PUR);
            }
            else if (item == jmiStkRetPending) {
                miClient.getGuiModule(SDataConstants.MOD_INV).showView(SDataConstants.TRNX_DPS_RETURN_PEND, SDataConstantsSys.TRNS_CL_DPS_PUR_ADJ[0], SDataConstantsSys.TRNS_CL_DPS_PUR_ADJ[1]);
            }
            else if (item == jmiStkRetPendingEntry) {
                miClient.getGuiModule(SDataConstants.MOD_INV).showView(SDataConstants.TRNX_DPS_RETURN_PEND_ETY, SDataConstantsSys.TRNS_CL_DPS_PUR_ADJ[0], SDataConstantsSys.TRNS_CL_DPS_PUR_ADJ[1]);
            }
            else if (item == jmiStkRetReturned) {
                miClient.getGuiModule(SDataConstants.MOD_INV).showView(SDataConstants.TRNX_DPS_RETURNED, SDataConstantsSys.TRNS_CL_DPS_PUR_ADJ[0], SDataConstantsSys.TRNS_CL_DPS_PUR_ADJ[1]);
            }
            else if (item == jmiStkRetReturnedEntry) {
                miClient.getGuiModule(SDataConstants.MOD_INV).showView(SDataConstants.TRNX_DPS_RETURNED_ETY, SDataConstantsSys.TRNS_CL_DPS_PUR_ADJ[0], SDataConstantsSys.TRNS_CL_DPS_PUR_ADJ[1]);
            }
            else if (item == jmiStkRetDiog) {
                miClient.getGuiModule(SDataConstants.MOD_INV).showView(SDataConstants.TRN_DIOG, SDataConstantsSys.TRNS_CL_IOG_OUT_PUR[0], SDataConstantsSys.TRNS_CL_IOG_OUT_PUR[1]);
            }
            else if (item == jmiRepTrnGlobal) {
                showView(SDataConstants.TRNX_DPS_QRY, SDataConstantsSys.TRNX_PUR_TOT);
            }
            else if (item == jmiRepTrnByMonth) {
                showView(SDataConstants.TRNX_DPS_QRY, SDataConstantsSys.TRNX_PUR_TOT_MONTH);
            }
            else if (item == jmiRepTrnByItemGeneric) {
                showView(SDataConstants.TRNX_DPS_QRY, SDataConstantsSys.TRNX_PUR_TOT_BY_IGEN);
            }
            else if (item == jmiRepTrnByItemGenericBizPartner) {
                showView(SDataConstants.TRNX_DPS_QRY, SDataConstantsSys.TRNX_PUR_TOT_BY_IGEN_BP);
            }
            else if (item == jmiRepTrnByItem) {
                showView(SDataConstants.TRNX_DPS_QRY, SDataConstantsSys.TRNX_PUR_TOT_BY_ITEM);
            }
            else if (item == jmiRepTrnByItemBizPartner) {
                showView(SDataConstants.TRNX_DPS_QRY, SDataConstantsSys.TRNX_PUR_TOT_BY_ITEM_BP);
            }
            else if (item == jmiRepTrnByBizPartner) {
                showView(SDataConstants.TRNX_DPS_QRY, SDataConstantsSys.TRNX_PUR_TOT_BY_BP);
            }
            else if (item == jmiRepTrnByBizPartnerItem) {
                showView(SDataConstants.TRNX_DPS_QRY, SDataConstantsSys.TRNX_PUR_TOT_BY_BP_ITEM);
            }
            else if (item == jmiRepTrnByBizPartnerType) {
                showView(SDataConstants.TRNX_DPS_QRY, SDataConstantsSys.TRNX_PUR_TOT_BY_TP_BP);
            }
            else if (item == jmiRepTrnByBizPartnerTypeBizPartner) {
                showView(SDataConstants.TRNX_DPS_QRY, SDataConstantsSys.TRNX_PUR_TOT_BY_TP_BP_BP);
            }
            else if (item == jmiRepTrnDpsByItemBizPartner) {
               showView(SDataConstants.TRNX_DPS_QRY, SDataConstantsSys.TRNX_PUR_DPS_BY_ITEM_BP_FIL);
            }
            else if (item == jmiRepBackorderContract) {
                showView(SDataConstants.TRNX_DPS_BACKORDER, SDataConstantsSys.TRNX_PUR_BACKORDER_CON, SDataConstantsSys.TRNS_CL_DPS_PUR_EST[1]);
            }
            else if (item == jmiRepBackorderContractByItem) {
                showView(SDataConstants.TRNX_DPS_BACKORDER, SDataConstantsSys.TRNX_PUR_BACKORDER_CON_ITEM, SDataConstantsSys.TRNS_CL_DPS_PUR_EST[1]);
            }
            else if (item == jmiRepBackorderContractByItemBizPartner) {
                showView(SDataConstants.TRNX_DPS_BACKORDER, SDataConstantsSys.TRNX_PUR_BACKORDER_CON_ITEM_BP, SDataConstantsSys.TRNS_CL_DPS_PUR_EST[1]);
            }
            else if (item == jmiRepBackorderContractByItemBizPartnerBra) {
                showView(SDataConstants.TRNX_DPS_BACKORDER, SDataConstantsSys.TRNX_PUR_BACKORDER_CON_ITEM_BP_BRA, SDataConstantsSys.TRNS_CL_DPS_PUR_EST[1]);
            }
            else if (item == jmiRepBackorderOrder) {
                showView(SDataConstants.TRNX_DPS_BACKORDER, SDataConstantsSys.TRNX_PUR_BACKORDER_ORD, SDataConstantsSys.TRNS_CL_DPS_PUR_ORD[1]);
            }
            else if (item == jmiRepBackorderOrderByItem) {
                showView(SDataConstants.TRNX_DPS_BACKORDER, SDataConstantsSys.TRNX_PUR_BACKORDER_ORD_ITEM, SDataConstantsSys.TRNS_CL_DPS_PUR_ORD[1]);
            }
            else if (item == jmiRepBackorderOrderByItemBizPartner) {
                showView(SDataConstants.TRNX_DPS_BACKORDER, SDataConstantsSys.TRNX_PUR_BACKORDER_ORD_ITEM_BP, SDataConstantsSys.TRNS_CL_DPS_PUR_ORD[1]);
            }
            else if (item == jmiRepBackorderOrderByItemBizPartnerBra) {
                showView(SDataConstants.TRNX_DPS_BACKORDER, SDataConstantsSys.TRNX_PUR_BACKORDER_ORD_ITEM_BP_BRA, SDataConstantsSys.TRNS_CL_DPS_PUR_ORD[1]);
            }
            else if (item == jmiQryBizPartnerBalance) {
                miClient.getGuiModule(SDataConstants.MOD_FIN).showView(SDataConstants.FINX_ACCOUNTING, SDataConstantsSys.FINS_TP_ACC_SYS_SUP);
            }
            else if (item == jmiQryBizPartnerAccountsAging) {
                showView(SDataConstants.TRNX_DPS_BAL_AGING, SDataConstantsSys.TRNS_CT_DPS_PUR);
            }
            else if (item == jmiRepBizPartnerBalance) {
                new SDialogRepBizPartnerBalance(miClient, SDataConstantsSys.BPSS_CT_BP_SUP).setVisible(true);
            }
            else if (item == jmiRepBizPartnerBalanceDps) {
                new SDialogRepBizPartnerBalanceDps(miClient, SDataConstantsSys.BPSS_CT_BP_SUP).setVisible(true);
            }
            else if (item == jmiRepBizPartnerBalanceAging) {
                new SDialogRepBizPartnerBalanceAging(miClient, SDataRepConstants.REP_ACC_AGI + " " + SBpsUtils.getBizPartnerCategoryName(SModSysConsts.BPSS_CT_BP_SUP, SUtilConsts.NUM_PLR).toLowerCase(), SDataConstantsSys.BPSS_CT_BP_SUP).setVisible(true);
            }
            else if (item == jmiRepAccountStatements) {
                new SDialogRepBizPartnerStatement(miClient, SDataRepConstants.REP_STA + " " + SBpsUtils.getBizPartnerCategoryName(SModSysConsts.BPSS_CT_BP_SUP, SUtilConsts.NUM_PLR).toLowerCase(), SDataConstantsSys.BPSS_CT_BP_SUP).setVisible(true);
            }
            else if (item == jmiRepBizPartnerJournal) {
                new SDialogRepBizPartnerJournal(miClient, SDataConstantsSys.BPSS_CT_BP_SUP).setVisible(true);
            }
            else if (item == jmiRepBizPartnerAccountingMoves) {
                new SDialogRepBizPartnerAccountingMoves(miClient, SDataConstantsSys.BPSS_CT_BP_SUP).setVisible(true);
            }
            else if (item == jmiRepDpsList) {
                moDialogRepDpsList.setParamIsSupplier(true);
                moDialogRepDpsList.formRefreshCatalogues();
                moDialogRepDpsList.formReset();
                moDialogRepDpsList.setFormVisible(true);
            }
            else if (item == jmiRepDpsBizPartner) {
                moDialogRepDpsBizPartner.setParamIsSupplier(true);
                moDialogRepDpsBizPartner.formRefreshCatalogues();
                moDialogRepDpsBizPartner.formReset();
                moDialogRepDpsBizPartner.setFormVisible(true);
            }
            else if (item == jmiRepDpsWithBalance) {
                moDialogRepDpsWithBalance.setParamIsSupplier(true);
                moDialogRepDpsWithBalance.formRefreshCatalogues();
                moDialogRepDpsWithBalance.formReset();
                moDialogRepDpsWithBalance.setFormVisible(true);
            }
            else if (item == jmiRepTrn) {
                moDialogRepSalesPurchases.setParamIsSupplier(true);
                moDialogRepSalesPurchases.formRefreshCatalogues();
                moDialogRepSalesPurchases.formReset();
                moDialogRepSalesPurchases.setFormVisible(true);
            }
            else if (item == jmiRepTrnConcept) {
                moDialogRepSalesPurchasesByConcept.formRefreshCatalogues();
                moDialogRepSalesPurchasesByConcept.formReset();
                moDialogRepSalesPurchasesByConcept.setParamIsSupplier(true);
                moDialogRepSalesPurchasesByConcept.setFormVisible(true);
            }
            else if (item == jmiRepTrnLocality) {
                moDialogRepSalesPurchasesByLocality.formRefreshCatalogues();
                moDialogRepSalesPurchasesByLocality.formReset();
                moDialogRepSalesPurchasesByLocality.setParamIsSupplier(true);
                moDialogRepSalesPurchasesByLocality.setFormVisible(true);
            }
            else if (item == jmiRepTrnComparative) {
                moDialogRepSalesPurchasesComparative.formReset();
                moDialogRepSalesPurchasesComparative.setFormVisible(true);
            }
            else if (item == jmiRepTrnDpsDetailBizPartner) {
                moDialogRepSalesPurchasesDetailByBizPartner.formRefreshCatalogues();
                moDialogRepSalesPurchasesDetailByBizPartner.formReset();
                moDialogRepSalesPurchasesDetailByBizPartner.setParamIsSupplier(true);
                moDialogRepSalesPurchasesDetailByBizPartner.setFormVisible(true);
            }
            else if (item == jmiRepTrnNetTotal) {
                moDialogRepSalesPurchasesNet.formRefreshCatalogues();
                moDialogRepSalesPurchasesNet.formReset();
                moDialogRepSalesPurchasesNet.setParamIsSupplier(true);
                moDialogRepSalesPurchasesNet.setParamIsNet(true);
                moDialogRepSalesPurchasesNet.setFormVisible(true);
            }
            else if (item == jmiRepTrnNetAnalytic) {
                moDialogRepSalesPurchasesNet.formRefreshCatalogues();
                moDialogRepSalesPurchasesNet.formReset();
                moDialogRepSalesPurchasesNet.setParamIsSupplier(true);
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
                moDialogRepSalesPurchasesJournal.setParamIsSupplier(true);
                moDialogRepSalesPurchasesJournal.setFormVisible(true);
            }
            else if (item == jmiRepTrnItemUnitaryPrice) {
                moDialogRepSalesPurchasesItemUnitaryPrice.formRefreshCatalogues();
                moDialogRepSalesPurchasesItemUnitaryPrice.formReset();
                moDialogRepSalesPurchasesItemUnitaryPrice.setParamIsSupplier(true);
                moDialogRepSalesPurchasesItemUnitaryPrice.setFormVisible(true);
            }
            else if (item == jmiRepTrnContractStatus) {
                new SDialogRepContractStatus(miClient.getSession().getClient(), SDataConstantsSys.BPSS_CT_BP_SUP, "Reporte de estatus de contratos").setVisible(true);
            }
            else if (item == jmiRepTrnContractBackorderStock) {
                moDialogRepContractStock.formRefreshCatalogues();
                moDialogRepContractStock.formReset();
                moDialogRepContractStock.setParamIsSupplier(true);
                moDialogRepContractStock.setFormVisible(true);
            }
            else if (item == jmiRepTrnUnitaryCosts) {
                moDialogRepPurchasesUnitaryCost.formRefreshCatalogues();
                moDialogRepPurchasesUnitaryCost.formReset();
                moDialogRepPurchasesUnitaryCost.setFormVisible(true);
            }
        }
    }
}