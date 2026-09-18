/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.swap.form;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.mbps.data.SDataBizPartner;
import erp.mcfg.data.SCfgUtils;
import erp.mfin.data.SDataAccount;
import erp.mfin.data.SDataAccountCash;
import erp.mfin.data.SDataCostCenter;
import erp.mod.cfg.utils.SAuthJsonUtils;
import erp.swap.SSwapConsts;
import java.io.Serializable;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import sa.lib.SLibUtils;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Adrian Aviles
 */
public class SImportWeekMovProcurementFacility implements SGridRow, Serializable {
    private final int MOVEMENT_TYPE_INGRESO_ID = 1;
    private final int MOVEMENT_TYPE_EGRESO_ID = 2;
    private static final int AME_BP_ID = 2217;
    
    private final String REASIGNACIÓN_EFECTIVO_CODE = "DM004";
    private final String ASIGNACIÓN_EFECTIVO_CODE = "DM003";
    private final String ANTICIPO_PROVEEDOR_CODE = "DM002";
    private final String ABONO_A_PRESTAMO_CODE = "DM001";
    private final String AGUACATE_MADURO = "1104110111";
    
    private final int ACCOUNTING_TYPE_COMPRA_ID = 1;
    private final int ACCOUNTING_TYPE_GASTO_ID = 2;
    private final int ACCOUNTING_TYPE_EFECTIVO_ID = 4;
    
    private final int ACCOUNTING_SUBTYPE_COMPRA_ID = 4;
    private final int ACCOUNTING_SUBTYPE_GASTO_ID = 5;
    private final int ACCOUNTING_SUBTYPE_EFECTIVO_ID = 1;
    private final int ACCOUNTING_SUBTYPE_DEUDORES_ID = 2;
    private final int ACCOUNTING_SUBTYPE_ACREEDORES_ID = 3;
    
    private final String ACCOUNTING_ACCOUNT_IVA = "1160-0002-0000";
    private final String OCASSIONAL_FISCAL_ID = "XAXX010101000";
    private final int[] TAX_KEY = new int[]{1 , 14};
    
    private final int LIMIT_TO_IVA = 1;
            
    private final String[] months = {
            "ENERO", "FEBRERO", "MARZO", "ABRIL", 
            "MAYO", "JUNIO", "JULIO", "AGOSTO", 
            "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE"
        };
    
    public int Id;
    public Date Movement_date;
    public String Concept;
    public String Reference;
    public double Debe;
    public double Haber;
    public Currency oCurrency;
    public CostCenter Cost_center;
    public AccountingAccount Accounting_account;
    public String Fiscal_id;
    public String Erp_user;
    public double Unit_cost;
    public double Stock_in;
    public int Crates;
    public ProcurementFacility Facility;
    public MovementType Movement_type;
    public Item Item;
    public boolean isPurchaseExpense;
    public Item ItemPurchaseExpense;
    public Item ItemAuxPurchaseExpense;
    public boolean Is_adjustment;
    public boolean Is_invoiced;
    public SDataAccount oDataAccount;
    public SDataAccount oDataAccountMajor;
    public SDataCostCenter oDataCostCenter;
    public SDataBizPartner oDataBizPartner;
    public int mnSortingPosition;
    public int mnFacilitySeasonWeekId;
    public SDataAccountCash moDataAccountCash;
    public int AccountingTypeId;
    public String AccountingTypeName;
    public int AccountingSubTypeId;
    public String AccountingSubTypeName;
    public int MovementTypeId;
    public String MovementTypeName;
    public SImportProcurementFacility oProcurementFacility;
    
    //del portal ame
    public double ameIva;
    public double ameRetention;
    
    public double subtotal16;
    public double subtotal0;
    public double impuesto16;
    public double impuesto0;
    public double impuestoExcento;
    public double retention1;
    public double retention2;
    
    public SDataAccount oDataAccountIVA;
    public String OccasionalFiscalId;
    public int[] TaxKey;
    
    public SDataAccount accIva16;
    public SDataAccount accIva0;
    public SDataAccount accExcento;
    public SDataAccount accRetention1;
    public SDataAccount accretention2;
    
    public SDataAccount accLedgerIva16;
    public SDataAccount accLedgerIva0;
    public SDataAccount accLedgerExcento;
    public SDataAccount accLedgerRetention1;
    public SDataAccount accLedgerretention2;
    
    public int[] TaxKeyIva16;
    public int[] TaxKeyIva0;
    public int[] TaxKeyExcento;
    public int[] TaxKeyRetention1;
    
    public double taxableBase16;
    public double taxableBase0;
    public double taxableBaseExcento;
    
    public boolean isIva16;
    public boolean isIva0;
    public boolean isIvaExcento;
    public boolean isRetention1;
    public boolean isRetention2;
    
    public double totalSinIva;

    public SImportWeekMovProcurementFacility() {
        Id = 0;
        Movement_date = null;
        Concept =  "";
        Reference = "";
        Debe = 0;
        Haber = 0;
        oCurrency = new Currency();
        Cost_center = null;
        Accounting_account = null;
        Fiscal_id = "";
        Erp_user = "";
        Unit_cost = 0;
        Stock_in = 0;
        Crates = 0;
        Facility = null;
        Movement_type = null;
        Item = null;
        ItemPurchaseExpense = null;
        ItemAuxPurchaseExpense = null;
        Is_adjustment = false;
        Is_invoiced = false;
        oDataAccount = null;
        oDataCostCenter = null;
        oDataAccountMajor = null;
        oDataBizPartner = null;
        moDataAccountCash = null;
        AccountingTypeId = 0;
        AccountingTypeName = "";
        AccountingSubTypeId = 0;
        AccountingSubTypeName = "";
        MovementTypeId = 0;
        MovementTypeName = "";
        oProcurementFacility = null;
        isPurchaseExpense = false;
        
        ameIva = 0;
        ameRetention = 0;

        taxableBase16 = 0;
        taxableBase0 = 0;
        taxableBaseExcento = 0;

        subtotal16 = 0;
        subtotal0 = 0;
        impuesto16 = 0;
        impuesto0 = 0;
        impuestoExcento = 0;
        retention1 = 0;
        retention2 = 0;

        oDataAccountIVA = null;
        OccasionalFiscalId = "";
        TaxKey = new int[]{};

        accIva16 = null;
        accIva0 = null;
        accExcento = null;
        accRetention1 = null;
        accretention2 = null;
        
        accLedgerIva16 = null;
        accLedgerIva0 = null;
        accLedgerExcento = null;
        accLedgerRetention1 = null;
        accLedgerretention2 = null;
        
        isIva16 = false;
        isIva0 = false;
        isIvaExcento = false;
        isRetention1 = false;
        isRetention2 = false;
        
        oCurrency = new Currency();
        totalSinIva = 0;
    }
    
    public SImportWeekMovProcurementFacility(SImportWeekMovProcurementFacility otra) {
        this.Id = otra.Id;
        this.Movement_date = otra.Movement_date;
        this.Concept = otra.Concept;
        this.Reference = otra.Reference;
        this.Debe = otra.Debe;
        this.Haber = otra.Haber;
        this.oCurrency = otra.oCurrency;
        this.Cost_center = otra.Cost_center;
        this.Accounting_account = otra.Accounting_account;
        this.Fiscal_id = otra.Fiscal_id;
        this.Erp_user = otra.Erp_user;
        this.Unit_cost = otra.Unit_cost;
        this.Stock_in = otra.Stock_in;
        this.Crates = otra.Crates;
        this.Facility = otra.Facility;
        this.Movement_type = otra.Movement_type;
        this.Item = otra.Item;
        this.ItemPurchaseExpense = otra.ItemPurchaseExpense;
        this.ItemAuxPurchaseExpense = otra.ItemAuxPurchaseExpense;
        this.Is_adjustment = otra.Is_adjustment;
        this.Is_invoiced = otra.Is_invoiced;
        this.oDataAccount = otra.oDataAccount;
        this.oDataCostCenter = otra.oDataCostCenter;
        this.oDataAccountMajor = otra.oDataAccountMajor;
        this.oDataBizPartner = otra.oDataBizPartner;
        this.moDataAccountCash = otra.moDataAccountCash;
        this.AccountingTypeId = otra.AccountingTypeId;
        this.AccountingTypeName = otra.AccountingTypeName;
        this.AccountingSubTypeId = otra.AccountingSubTypeId;
        this.AccountingSubTypeName = otra.AccountingSubTypeName;
        this.MovementTypeId = otra.MovementTypeId;
        this.MovementTypeName = otra.MovementTypeName;
        this.oProcurementFacility = otra.oProcurementFacility;
        this.isPurchaseExpense = otra.isPurchaseExpense;

        this.ameIva = otra.ameIva;
        this.ameRetention = otra.ameRetention;

        this.taxableBase16 = otra.taxableBase16;
        this.taxableBase0 = otra.taxableBase0;
        this.taxableBaseExcento = otra.taxableBaseExcento;

        this.subtotal16 = otra.subtotal16;
        this.subtotal0 = otra.subtotal0;
        this.impuesto16 = otra.impuesto16;
        this.impuesto0 = otra.impuesto0;
        this.impuestoExcento = otra.impuestoExcento;
        this.retention1 = otra.retention1;
        this.retention2 = otra.retention2;

        this.oDataAccountIVA = otra.oDataAccountIVA;
        this.OccasionalFiscalId = otra.OccasionalFiscalId;

        // Array: crear copia nueva para que no compartan la misma referencia
        this.TaxKey = (otra.TaxKey != null) ? otra.TaxKey.clone() : new int[]{};

        this.accIva16 = otra.accIva16;
        this.accIva0 = otra.accIva0;
        this.accExcento = otra.accExcento;
        this.accRetention1 = otra.accRetention1;
        this.accretention2 = otra.accretention2;

        this.accLedgerIva16 = otra.accLedgerIva16;
        this.accLedgerIva0 = otra.accLedgerIva0;
        this.accLedgerExcento = otra.accLedgerExcento;
        this.accLedgerRetention1 = otra.accLedgerRetention1;
        this.accLedgerretention2 = otra.accLedgerretention2;

        this.isIva16 = otra.isIva16;
        this.isIva0 = otra.isIva0;
        this.isIvaExcento = otra.isIvaExcento;
        this.isRetention1 = otra.isRetention1;
        this.isRetention2 = otra.isRetention2;
        
        this.TaxKeyIva16 = (otra.TaxKeyIva16 != null) ? otra.TaxKeyIva16.clone() : new int[]{};
        this.TaxKeyIva0 = (otra.TaxKeyIva0 != null) ? otra.TaxKeyIva0.clone() : new int[]{};
        this.TaxKeyExcento = (otra.TaxKeyExcento != null) ? otra.TaxKeyExcento.clone() : new int[]{};
        this.TaxKeyRetention1 = (otra.TaxKeyRetention1 != null) ? otra.TaxKeyRetention1.clone() : new int[]{};

        this.taxableBase16 = otra.taxableBase16;
        this.taxableBase0 = otra.taxableBase0;
        this.taxableBaseExcento = otra.taxableBaseExcento;
        
        this.mnSortingPosition = otra.mnSortingPosition;
        this.mnFacilitySeasonWeekId = otra.mnFacilitySeasonWeekId;
    }
    
    @SuppressWarnings("deprecation")
    public SImportWeekMovProcurementFacility(final JsonNode docNode, final Statement statement, SClientInterface miClient, SImportProcurementFacility procurementFacility) throws ParseException, Exception {
        Id = docNode.get("id").asInt();
        String movement_date = docNode.get("movement_date").asText();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dMovementDate = sdf.parse(movement_date);
        Movement_date = dMovementDate;
        Concept = docNode.get("concept").isNull() ? "" : docNode.get("concept").asText();
        Reference = docNode.get("reference").isNull() ? "" : docNode.get("reference").asText();
        oCurrency = new Currency();
        
        oProcurementFacility = procurementFacility;

        SDataAccount oAccount = new SDataAccount();
        SDataAccount oAccountLedger = new SDataAccount();

        JsonNode accountingAccountNode = docNode.path("accounting_account");
        if (!accountingAccountNode.isEmpty()) {                    
            oAccount.read(new String[] { accountingAccountNode.get("code").asText() }, statement);
            oAccountLedger.read( new String[] { oAccount.getDbmsPkLedgerAccountIdXXX() }, statement);
        } else {
            oAccount.read( new String[] { "6000-0001-0000" }, statement);
            oAccountLedger.read( new String[] { oAccount.getDbmsPkLedgerAccountIdXXX() }, statement);
        }
        
        oDataAccount = oAccount;
        oDataAccountMajor = oAccountLedger;
        
        SDataAccount oAccountIVA = new SDataAccount();
        oAccountIVA.read( new String[] { ACCOUNTING_ACCOUNT_IVA }, statement);
        
        oDataAccountIVA = oAccountIVA;
        OccasionalFiscalId = OCASSIONAL_FISCAL_ID;
        TaxKey = TAX_KEY;

        SDataCostCenter costCenter = new SDataCostCenter();
        JsonNode costCenterNode = docNode.path("cost_center");
        if (!costCenterNode.isEmpty()) {                    
            costCenter.read( new String[] { costCenterNode.get("code").asText() }, statement);
        } else {
            costCenter.read( new String[] { "100-01-01-001" }, statement);
        }
        
        oDataCostCenter = costCenter;
        
        Unit_cost = docNode.get("unit_cost").asDouble();
        Stock_in = docNode.get("stock_in").asDouble();
        Crates = docNode.get("crates").asInt();
        
        JsonNode facilityNode = docNode.path("facility");
        if (!facilityNode.isEmpty()) {
            Facility = new ProcurementFacility(
                facilityNode.get("id").asInt(),
                facilityNode.get("code").isNull() ? "" : facilityNode.get("code").asText(), 
                facilityNode.get("name").isNull() ? "" : facilityNode.get("name").asText()
            );
        } else {
            Facility = new ProcurementFacility();
        }
        
        JsonNode movementTypeNode = docNode.path("movement_type");
        if (!movementTypeNode.isEmpty()) {
            Movement_type = new MovementType(
                movementTypeNode.get("id").asInt(),
                movementTypeNode.get("code").isNull() ? "" : movementTypeNode.get("code").asText(), 
                movementTypeNode.get("name").isNull() ? "" : movementTypeNode.get("name").asText()
            );
        } else {
            Movement_type = new MovementType();
        }
        
        JsonNode itemNode = docNode.path("item");
        if (!itemNode.isEmpty()) {
            Item = new Item(
                itemNode.get("erp_id").asInt(),
                itemNode.get("code").isNull() ? "" : itemNode.get("code").asText(), 
                itemNode.get("name").isNull() ? "" : itemNode.get("name").asText()
            );
        } else {
            Item = new Item();
        }
        
        JsonNode itemPurchaseNode = docNode.path("purchase_expense");
        if (!itemPurchaseNode.isEmpty()) {
            JsonNode itemPurchaseExpenseNode = itemPurchaseNode.path("base_item");
            JsonNode itemAuxPurchaseExpenseNode = itemPurchaseNode.path("aux_item");
            isPurchaseExpense = true;
            if (!itemPurchaseExpenseNode.isEmpty()) {
                ItemPurchaseExpense = new Item(
                    itemPurchaseExpenseNode.get("erp_id").asInt(),
                    itemPurchaseExpenseNode.get("code").isNull() ? "" : itemNode.get("code").asText(), 
                    itemPurchaseExpenseNode.get("name").isNull() ? "" : itemNode.get("name").asText()
                );
            } else {
                ItemPurchaseExpense = new Item();
            }
            
            if (!itemAuxPurchaseExpenseNode.isEmpty()) {
                ItemAuxPurchaseExpense = new Item(
                    itemAuxPurchaseExpenseNode.get("erp_id").asInt(),
                    itemAuxPurchaseExpenseNode.get("code").isNull() ? "" : itemNode.get("code").asText(), 
                    itemAuxPurchaseExpenseNode.get("name").isNull() ? "" : itemNode.get("name").asText()
                );
            } else {
                ItemAuxPurchaseExpense = new Item();
            }
            
        } else {
            ItemPurchaseExpense = new Item();
            ItemAuxPurchaseExpense = new Item();
            isPurchaseExpense = false;
        }
        
        Is_adjustment = docNode.get("is_adjustment").asBoolean();
        Is_invoiced = docNode.get("is_invoiced").asBoolean();
        
        JsonNode accounting_type = docNode.path("accounting_type");
        JsonNode accounting_subtype = docNode.path("accounting_subtype");
        
        AccountingTypeId = accounting_type.get("id").asInt();
        AccountingTypeName = accounting_type.get("name").asText();
        AccountingSubTypeId = accounting_subtype.get("id").asInt();
        AccountingSubTypeName = accounting_subtype.get("name").asText();
        
        Fiscal_id = docNode.get("fiscal_id").isNull() ? "" : docNode.get("fiscal_id").asText();
        Erp_user = docNode.get("export_updated_by").isNull() ? "" : docNode.get("export_updated_by").asText();
        
        int pkBp;
        if (docNode.get("erp_user").isNull() && AccountingTypeId == ACCOUNTING_TYPE_COMPRA_ID) {
            pkBp = AME_BP_ID;
        } else {
            pkBp = docNode.get("erp_user").asInt();
        }
        
        if (pkBp != 0) {
            SDataBizPartner bp = new SDataBizPartner();
            int res = bp.read(new int[] {pkBp}, statement);
            if (res == SLibConstants.DB_ACTION_READ_OK) {
                oDataBizPartner = bp;
            }
        }
        
        JsonNode accountCash = docNode.path("cash_holding");
        if (!accountCash.isEmpty()) {
            int[] pkAccountCash = new int[] { accountCash.get("id_cob_ext").asInt(), accountCash.get("id_ent_ext").asInt() };
            moDataAccountCash = (SDataAccountCash) SDataUtilities.readRegistry(miClient, SDataConstants.FIN_ACC_CASH, pkAccountCash, SLibConstants.EXEC_MODE_SILENT);
        }
        
        JsonNode movement_type = docNode.path("movement_type");
        
        MovementTypeId = movement_type.get("id").asInt();
        MovementTypeName = movement_type.get("name").asText();
        
        ArrayList<String> checkAccountTypeResult = checkAccountType();
        if ("debe".equals(checkAccountTypeResult.get(0))) {
            if (docNode.get("outcome").asDouble() != 0) {
                Debe = docNode.get("outcome").asDouble();
            }
            if (docNode.get("income").asDouble() != 0) {
                Debe = docNode.get("income").asDouble();
            }
        }
        if ("haber".equals(checkAccountTypeResult.get(0))) {
            if (docNode.get("income").asDouble() != 0) {
                Haber = docNode.get("income").asDouble();
            }
            if (docNode.get("outcome").asDouble() != 0) {
                Haber = docNode.get("outcome").asDouble();
            }
        }
        
        JsonNode invoice = docNode.path("invoice");
        if(!invoice.isEmpty()){
//            ameIva = invoice.get("iva").asInt();
//            ameRetention = invoice.get("retention").asInt();
        }

        ameIva = 136.93;
        ameRetention = 10;
        
        if(ameIva > 0.0) {
            calcularIva();
            readJsonConfig(miClient.getSession(), statement);
        }
        
        if (AccountingTypeId == ACCOUNTING_TYPE_COMPRA_ID && Item.Code.equals(AGUACATE_MADURO)) {
            Concept = "COMPRA DE FRUTA " + Stock_in + " KG " + Facility.Name + " " + oProcurementFacility.WeekMonthNumber;
        } else if (AccountingTypeId == ACCOUNTING_TYPE_GASTO_ID) {
            Concept = docNode.get("concept").isNull() ? "" : docNode.get("concept").asText();

            if (!Concept.matches("(?i).*DEL\\s+\\d+\\s+AL\\s+\\d+.*")) {
                Concept = docNode.get("concept").isNull() ? "" : docNode.get("concept").asText() + 
                        " DEL " + oProcurementFacility.StartDate.getDate() + " AL " + oProcurementFacility.EndDate.getDate() +
                        " DE " + months[oProcurementFacility.EndDate.getMonth()] + " " + Facility.Name + " " + oProcurementFacility.WeekMonthNumber;
            }
            
        } else {
            Concept = docNode.get("concept").isNull() ? "" : docNode.get("concept").asText();
        }
    }

    public int getId() { return Id; }
    public void setId(int Id) { this.Id = Id; }
    public Date getMovement_date() { return Movement_date; }
    public void setMovement_date(Date Movement_date) { this.Movement_date = Movement_date; }
    public String getConcept() { return Concept; }
    public void setConcept(String Concept) { this.Concept = Concept; }
    public String getReference() { return Reference; }
    public void setReference(String Reference) { this.Reference = Reference; }
    public double getDebe() { return Debe; }
    public void setDebe(double Debe) { this.Debe = Debe; }
    public double getHaber() { return Haber; }
    public void setHaber(double Haber) { this.Haber = Haber; }
    public Currency getCurrency() { return oCurrency; }
    public void setCurrency(int id, String code, String name) { this.oCurrency = new Currency(id, code, name); }
    public CostCenter getCost_center() { return Cost_center; }
    public void setCost_center(int id, String code, String name) { this.Cost_center = new CostCenter(id, code, name); }
    public AccountingAccount getAccounting_account() { return Accounting_account;    }
    public void setAccounting_account(int id, String code, String name) { this.Accounting_account = new AccountingAccount(id, code, name); }
    public String getFiscal_id() { return Fiscal_id; }
    public void setFiscal_id(String Fiscal_id) { this.Fiscal_id = Fiscal_id; }
    public String getErp_user() { return Erp_user; }
    public void setErp_user(String Erp_user) { this.Erp_user = Erp_user; }
    public double getUnit_cost() { return Unit_cost; }
    public void setUnit_cost(double Unit_cost) { this.Unit_cost = Unit_cost; }
    public double getStock_in() { return Stock_in; }
    public void setStock_in(double Stock_in) { this.Stock_in = Stock_in; }
    public int getCrates() { return Crates; }
    public void setCrates(int Crates) { this.Crates = Crates; }
    public ProcurementFacility getFacility() { return Facility; }
    public void setFacility(ProcurementFacility Facility) { this.Facility = Facility; }
    public MovementType getMovement_type() { return Movement_type; }
    public void setMovement_type(MovementType Movement_type) { this.Movement_type = Movement_type; }
    public Item getItem() { return Item; }
    public void setItem(int id, String code, String name) { this.Item = new Item(id, code, name); }
    public boolean isIs_adjustment() { return Is_adjustment; }
    public void setIs_adjustment(boolean Is_adjustment) { this.Is_adjustment = Is_adjustment; }
    public boolean isIs_invoiced() { return Is_invoiced; }
    public void setIs_invoiced(boolean Is_invoiced) { this.Is_invoiced = Is_invoiced; }
    public void setDataAccount(SDataAccount dataAccount) { this.oDataAccount = dataAccount; }
    public SDataAccount getDataAccount(){ return oDataAccount; }
    public void setDataAccountMajor(SDataAccount dataAccountMajor) { this.oDataAccountMajor = dataAccountMajor; }
    public SDataAccount getDataAccountMajor(){ return oDataAccountMajor; }
    public void setDataCostCenter(SDataCostCenter dataCostCenter) { this.oDataCostCenter = dataCostCenter; }
    public SDataCostCenter getDataCostCenter(){ return oDataCostCenter; }
    public void setDataBizPartner(SDataBizPartner bp) { this.oDataBizPartner = bp; }
    public SDataBizPartner getDataBizPartner(){ return oDataBizPartner; }
    public int getMnSortingPosition() { return mnSortingPosition; }
    public void setMnSortingPosition(int mnSortingPosition) { this.mnSortingPosition = mnSortingPosition; }
    public int getMnFacilitySeasonWeekId() { return mnFacilitySeasonWeekId; }
    public void setMnFacilitySeasonWeekId(int mnFacilitySeasonWeekId) { this.mnFacilitySeasonWeekId = mnFacilitySeasonWeekId; }
    public SDataAccountCash getDataAccountCash() { return moDataAccountCash; }
    public void setDataAccountCash(SDataAccountCash moDataAccountCash) { this.moDataAccountCash = moDataAccountCash; }
    public int getAccountingTypeId() { return AccountingTypeId; }
    public void setAccountingTypeId(int AccountingTypeId) { this.AccountingTypeId = AccountingTypeId; }
    public int getAccountingSubTypeId() { return AccountingSubTypeId; }
    public void setAccountingSubTypeId(int AccountingSubTypeId) { this.AccountingSubTypeId = AccountingSubTypeId; }
    public String getAccountingTypeName() { return AccountingTypeName; }
    public void setAccountingTypeName(String AccountingTypeName) { this.AccountingTypeName = AccountingTypeName; }
    public String getAccountingSubTypeName() { return AccountingSubTypeName; }
    public void setAccountingSubTypeName(String AccountingSubTypeName) { this.AccountingSubTypeName = AccountingSubTypeName; }
    public boolean getIsPurchaseExpense() { return this.isPurchaseExpense; }
    public void setIsPurchaseExpense(boolean isPurchaseExpense) { this.isPurchaseExpense = isPurchaseExpense; }
    public Item getItemPurchaseExpense() { return ItemPurchaseExpense; }
    public void setItemPurchaseExpense(int id, String code, String name) { this.ItemPurchaseExpense = new Item(id, code, name); }
    public Item getItemAuxPurchaseExpense() { return ItemAuxPurchaseExpense; }
    public void setItemAuxPurchaseExpense(int id, String code, String name) { this.ItemAuxPurchaseExpense = new Item(id, code, name); }
    public double getSubtotal16() { return subtotal16; }
    public void setSubtotal16(double subtotal16) { this.subtotal16 = subtotal16; }
    public double getSubtotal0() { return subtotal0; }
    public void setSubtotal0(double subtotal0) { this.subtotal0 = subtotal0; }
    public double getImpuesto16() { return impuesto16; }
    public void setImpuesto16(double impuesto16) { this.impuesto16 = impuesto16; }
    public SDataAccount getoDataAccountIVA() { return oDataAccountIVA; }
    public void setoDataAccountIVA(SDataAccount oDataAccountIVA) { this.oDataAccountIVA = oDataAccountIVA; }
    public String getOccasionalFiscalId() { return OccasionalFiscalId; }
    public void setOccasionalFiscalId(String OccasionalFiscalId) { this.OccasionalFiscalId = OccasionalFiscalId; }
    public int[] getTaxKey() { return TaxKey; }
    public void setTaxKey(int[] TaxKey) { this.TaxKey = TaxKey; }
    public SDataAccount getAccIva16() { return accIva16; }
    public void setAccIva16(SDataAccount accIva16) { this.accIva16 = accIva16; }
    public SDataAccount getAccIva0() { return accIva0; }
    public void setAccIva0(SDataAccount accIva0) { this.accIva0 = accIva0; }
    public SDataAccount getAccExcento() { return accExcento; }
    public void setAccExcento(SDataAccount accExcento) { this.accExcento = accExcento; }
    public double getImpuestoExcento() { return impuestoExcento; }
    public void setImpuestoExcento(double impuestoExcento) { this.impuestoExcento = impuestoExcento; }
    public SDataAccount getAccRetention1() { return accRetention1; }
    public void setAccRetention1(SDataAccount accRetention1) { this.accRetention1 = accRetention1; }
    public SDataAccount getAccLedgerIva16() { return accLedgerIva16; }
    public void setAccLedgerIva16(SDataAccount accLedgerIva16) { this.accLedgerIva16 = accLedgerIva16; }
    public SDataAccount getAccLedgerIva0() { return accLedgerIva0; }
    public void setAccLedgerIva0(SDataAccount accLedgerIva0) { this.accLedgerIva0 = accLedgerIva0; }
    public SDataAccount getAccLedgerExcento() { return accLedgerExcento; }
    public void setAccLedgerExcento(SDataAccount accLedgerExcento) { this.accLedgerExcento = accLedgerExcento; }
    public SDataAccount getAccLedgerRetention1() { return accLedgerRetention1; }
    public void setAccLedgerRetention1(SDataAccount accLedgerRetention1) { this.accLedgerRetention1 = accLedgerRetention1; }
    public SDataAccount getAccLedgerretention2() { return accLedgerretention2; }
    public void setAccLedgerretention2(SDataAccount accLedgerretention2) { this.accLedgerretention2 = accLedgerretention2; }
    public double getImpuesto0() { return impuesto0; }
    public void setImpuesto0(double impuesto0) { this.impuesto0 = impuesto0; }
    public double getRetention1() { return retention1; }
    public void setRetention1(double retention1) { this.retention1 = retention1; }
    public double getRetention2() { return retention2; }
    public void setRetention2(double retention2) { this.retention2 = retention2; }
    public double getTaxableBase16() { return taxableBase16; }
    public void setTaxableBase16(double taxableBase16) { this.taxableBase16 = taxableBase16; }
    public double getTaxableBase0() { return taxableBase0; }
    public void setTaxableBase0(double taxableBase0) { this.taxableBase0 = taxableBase0; }
    public double getTaxableBaseExcento() { return taxableBaseExcento; }
    public void setTaxableBaseExcento(double taxableBaseExcento) { this.taxableBaseExcento = taxableBaseExcento; }
    public void setIsIva16(boolean isIva16) { this.isIva16 = isIva16; }
    public boolean getIsIva16(){ return isIva16; }
    public void setIsIva0(boolean isIva0) { this.isIva0 = isIva0; }
    public boolean getIsIva0(){ return isIva0; }
    public void setIsIvaExcento(boolean isIvaExcento) { this.isIvaExcento = isIvaExcento; }
    public boolean getIsIvaExcento(){ return isIvaExcento; }
    public void setIsRetention1(boolean isRetention1) { this.isRetention1 = isRetention1; }
    public boolean getIsRetention1(){ return isRetention1; }
    public void setIsRetention2(boolean isRetention2) { this.isRetention2 = isRetention1; }
    public boolean getIsRetention2(){ return isRetention2; }
    public int[] getTaxKeyIva16() { return TaxKeyIva16; }
    public int[] getTaxKeyIva0() { return TaxKeyIva0; }
    public int[] getTaxKeyExcento() { return TaxKeyExcento; }
    public double getTotalSinIva() { return totalSinIva; }
    
    @Override
    public int[] getRowPrimaryKey() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getRowCode() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getRowName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isRowSystem() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isRowDeletable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isRowEdited() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setRowEdited(boolean edited) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;
        
        switch (col) {
            case 0:
                value = Movement_date;
                break;
            case 1:
                value = Concept;
                break;
            case 2:
                value = Reference;
                break;
            case 3:
                value = Debe;
                break;
            case 4:
                value = Haber;
                break;
            case 5:
                value = oCurrency.Name;
                break;
            case 6:
                if (oDataCostCenter != null) {
                    value = oDataCostCenter.getCode();
                } else {
                    value = "";
                }
                break;
            case 7:
                if (oDataCostCenter != null) {
                    value = oDataCostCenter.getCostCenter();
                } else {
                    value = "";
                }
                break;
            case 8:
                value = oDataAccount.getCode();
                break;
            case 9:
                value = oDataAccount.getAccount();
                break;
            case 10:
                value = Fiscal_id;
                break;
            case 11:
                value = Erp_user;
                break;
            case 12:
                value = Unit_cost;
                break;
            case 13:
                value = Stock_in;
                break;
            case 14:
                value = Crates;
                break;
            case 15:
                value = Movement_type.Name;
                break;
            case 16:
                value = Item.Code;
                break;
            case 17:
                value = Item.Name;
                break;
            default:
            // nothing
        }
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public static class Currency implements Serializable{
        public int Id;
        public String Code;
        public String Name;
        
        public Currency() {
            Id = 1;
            Code = "";
            Name = "PESOS MEXICANOS";
        }
        
        public Currency(final int id, final String code, final String name) {
            Id = id;
            Code = code;
            Name = name;
        }
    }
    
    /**
     * In-memory CostCenter.
     */
    public static class CostCenter implements Serializable {

        public int Id;
        public String Code;
        public String Name;

        public CostCenter() {
            Id = 0;
            Code = "";
            Name = "";
        }
        
        public CostCenter(final int id, final String code, final String name) {
            Id = id;
            Code = code;
            Name = name;
        }
    }
    
    /**
     * In-memory AccountingAccount.
     */
    public static class AccountingAccount implements Serializable {

        public int Id;
        public String Code;
        public String Name;

        public AccountingAccount() {
            Id = 0;
            Code = "";
            Name = "";
        }
        
        public AccountingAccount(final int id, final String code, final String name) {
            Id = id;
            Code = code;
            Name = name;
        }
    }
    
    /**
     * In-memory ProcurementFacility.
     */
    public static class ProcurementFacility implements Serializable {

        public int Id;
        public String Code;
        public String Name;

        public ProcurementFacility() {
            Id = 0;
            Code = "";
            Name = "";
        }
        
        public ProcurementFacility(final int id, final String code, final String name) {
            Id = id;
            Code = code;
            Name = name;
        }
    }
    
    /**
     * In-memory MovementType.
     */
    public static class MovementType implements Serializable {

        public int Id;
        public String Code;
        public String Name;

        public MovementType() {
            Id = 0;
            Code = "";
            Name = "";
        }
        
        public MovementType(final int id, final String code, final String name) {
            Id = id;
            Code = code;
            Name = name;
        }
    }
    
    /**
     * In-memory Item.
     */
    public static class Item implements Serializable {

        public int Id;
        public String Code;
        public String Name;

        public Item() {
            Id = 0;
            Code = "";
            Name = "";
        }
        
        public Item(final int id, final String code, final String name) {
            Id = id;
            Code = code;
            Name = name;
        }
    }
    
    public ArrayList<String> checkAccountType() {
        ArrayList<String> result = new ArrayList<>();
        String type = "";
        String counterpart = "";
        
        if (MOVEMENT_TYPE_EGRESO_ID == MovementTypeId && ACCOUNTING_TYPE_COMPRA_ID == AccountingTypeId && ACCOUNTING_SUBTYPE_COMPRA_ID == AccountingSubTypeId) {
            type = "debe";
            counterpart = "salida_caja_compras";
        }
        
        if (MOVEMENT_TYPE_EGRESO_ID == MovementTypeId && ACCOUNTING_TYPE_GASTO_ID == AccountingTypeId && ACCOUNTING_SUBTYPE_GASTO_ID == AccountingSubTypeId) {
            type = "debe";
            counterpart = "salida_caja_gastos";
        }
        
        if (MOVEMENT_TYPE_EGRESO_ID == MovementTypeId && ACCOUNTING_TYPE_EFECTIVO_ID == AccountingTypeId && ACCOUNTING_SUBTYPE_DEUDORES_ID == AccountingSubTypeId) {
            type = "debe";
            counterpart = "salida_caja_deudores";
        }
        
        if (MOVEMENT_TYPE_EGRESO_ID == MovementTypeId && ACCOUNTING_TYPE_EFECTIVO_ID == AccountingTypeId && ACCOUNTING_SUBTYPE_ACREEDORES_ID == AccountingSubTypeId) {
            type = "debe";
            counterpart = "salida_caja_acreedores";
        }
        
        if (MOVEMENT_TYPE_INGRESO_ID == MovementTypeId && ACCOUNTING_TYPE_EFECTIVO_ID == AccountingTypeId && ACCOUNTING_SUBTYPE_DEUDORES_ID == AccountingSubTypeId) {
            type = "haber";
            counterpart = "entrada_caja_deudores";
        }
        
        if (MOVEMENT_TYPE_INGRESO_ID == MovementTypeId && ACCOUNTING_TYPE_EFECTIVO_ID == AccountingTypeId && ACCOUNTING_SUBTYPE_ACREEDORES_ID == AccountingSubTypeId) {
            type = "haber";
            counterpart = "entrada_caja_acreedor";
        }
        
        if (ASIGNACIÓN_EFECTIVO_CODE.equals(Item.Code) && MovementTypeId == MOVEMENT_TYPE_INGRESO_ID) {
            type = "debe";
            counterpart = "caja_central";
        }
        
        if (REASIGNACIÓN_EFECTIVO_CODE.equals(Item.Code) && MovementTypeId == MOVEMENT_TYPE_EGRESO_ID) {
            type = "haber";
            counterpart = "caja_x";
        }
        
        result.add(type);
        result.add(counterpart);
        
        return result;
    }
    
//    public void calcularIva() {
//        double total = Debe != 0 ? Debe : Haber;
//        double sub16 = total / 1.16;
//        double montoConIva = sub16 + ameIva;
//        double sub0 = 0;
//        
//        if (SLibUtils.compareAmount(total, montoConIva)) {
//            subtotal0 = sub0;
//            subtotal16 = SLibUtils.roundAmount(sub16);
//            impuesto16 = ameIva;
//            taza16 = 0.16;
//        } else {
//            montoConIva = SLibUtils.roundAmount(sub16) + SLibUtils.roundAmount(ameIva);
//            sub0 = total - montoConIva;
//            subtotal0 = SLibUtils.roundAmount(sub0);
//            subtotal16 = SLibUtils.roundAmount(sub16);
//            impuesto16 = ameIva;
//            taza16 = 0.16;
//        }
//    }
    
    public void calcularIva() {
        double total = Debe;
        double sub16 = total / 1.16;
        double montoConIva = sub16 + ameIva;
        double sub0 = 0;
        double iva16Calculado = SLibUtils.roundAmount( (total / 1.16) * 0.16 );
        
        sub16 = total - (ameIva + sub0);
        taxableBase16 = total / 1.16;
        taxableBase0 = total  - taxableBase16 - ameIva;
        
        if (iva16Calculado > ameIva) {
            if ( (total - montoConIva) > LIMIT_TO_IVA ) {
                sub16 = ameIva / 0.16;
                sub0 = total - (ameIva + sub16);
            }
        }
        
        subtotal0 = SLibUtils.roundAmount(sub0);
        subtotal16 = SLibUtils.roundAmount(sub16);
        impuesto16 = ameIva;
        impuesto0 = subtotal0;
        
        totalSinIva = total - impuesto16 - impuesto0;
        retention1 = ameRetention;
    }
    
    public void readJsonConfig(SGuiSession session, Statement statement) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode config = mapper.readTree(SCfgUtils.getParamValue(session.getStatement(), SDataConstantsSys.CFG_PARAM_SWAP_SERVICES_AVO_CONFIG));
        ArrayList<String> arrPkAccTaxIva16 = SAuthJsonUtils.getValueOfElementAsTextArray(config, SSwapConsts.CFG_OBJ_AVO_SRV_CFG_TAX_TRANS_ACC, "1");
        ArrayList<String> arrPkAccTaxIva0 = SAuthJsonUtils.getValueOfElementAsTextArray(config, SSwapConsts.CFG_OBJ_AVO_SRV_CFG_TAX_TRANS_ACC, "2");
        ArrayList<String> arrPkAccTaxExc = SAuthJsonUtils.getValueOfElementAsTextArray(config, SSwapConsts.CFG_OBJ_AVO_SRV_CFG_TAX_TRANS_ACC, "3");
        ArrayList<String> arrPkAccRetention1 = SAuthJsonUtils.getValueOfElementAsTextArray(config, SSwapConsts.CFG_OBJ_AVO_SRV_CFG_TAX_RET_ACC, "1");
        
        TaxKeyIva16 = new int[] { Integer.valueOf(arrPkAccTaxIva16.get(0)), Integer.valueOf(arrPkAccTaxIva16.get(1)) };
        TaxKeyIva0 = new int[] { Integer.valueOf(arrPkAccTaxIva0.get(0)), Integer.valueOf(arrPkAccTaxIva0.get(1)) };
        TaxKeyExcento = new int[] { Integer.valueOf(arrPkAccTaxExc.get(0)), Integer.valueOf(arrPkAccTaxExc.get(1)) };
        TaxKeyRetention1 = new int[] { Integer.valueOf(arrPkAccRetention1.get(0)), Integer.valueOf(arrPkAccRetention1.get(1)) };
        
        SDataAccount oAccountIVA = new SDataAccount();
        oAccountIVA.read( new String[] { ACCOUNTING_ACCOUNT_IVA }, statement);
        accIva16 = oAccountIVA;
        
        SDataAccount oAccountLedgerIVA16 = new SDataAccount();
        oAccountLedgerIVA16.read( new String[] { oAccountIVA.getDbmsPkLedgerAccountIdXXX() }, statement);
        
        accLedgerIva16 = oAccountLedgerIVA16;
        
        SDataAccount oAccountRetention1 = new SDataAccount();
        oAccountRetention1.read( new String[] { "2140-0001-0028" }, statement);
        
        accRetention1 = oAccountRetention1;
        accretention2 = null;
        
        SDataAccount oAccountLedgerRetention1 = new SDataAccount();
        oAccountLedgerRetention1.read( new String[] { oAccountRetention1.getDbmsPkLedgerAccountIdXXX() }, statement);
        
        accLedgerRetention1 = oAccountLedgerRetention1;
        accLedgerretention2 = null;
    }
    
//    public void readJsonConfig(SGuiSession session, Statement statement) throws Exception {
//        ObjectMapper mapper = new ObjectMapper();
//        JsonNode config = mapper.readTree(SCfgUtils.getParamValue(session.getStatement(), SDataConstantsSys.CFG_PARAM_SWAP_SERVICES_AVO_CONFIG));
//        ArrayList<String> arrPkAccTaxIva16 = SAuthJsonUtils.getValueOfElementAsTextArray(config, SSwapConsts.CFG_OBJ_AVO_SRV_CFG_TAX_TRANS_ACC, "1");
//        ArrayList<String> arrPkAccTaxIva0 = SAuthJsonUtils.getValueOfElementAsTextArray(config, SSwapConsts.CFG_OBJ_AVO_SRV_CFG_TAX_TRANS_ACC, "2");
//        ArrayList<String> arrPkAccTaxExc = SAuthJsonUtils.getValueOfElementAsTextArray(config, SSwapConsts.CFG_OBJ_AVO_SRV_CFG_TAX_TRANS_ACC, "3");
//        ArrayList<String> arrPkAccRetention1 = SAuthJsonUtils.getValueOfElementAsTextArray(config, SSwapConsts.CFG_OBJ_AVO_SRV_CFG_TAX_RET_ACC, "1");
//        
//        SDataAccountTax oDataAccountTax16 = new SDataAccountTax();
//        String[] pkIva16 = {arrPkAccTaxIva16.get(0), arrPkAccTaxIva16.get(1), arrPkAccTaxIva16.get(2), arrPkAccTaxIva16.get(3)};
//        oDataAccountTax16.read(pkIva16, statement);
//        
//        SDataAccountTax oDataAccountTax0 = new SDataAccountTax();
//        String[] pkIva0 = {arrPkAccTaxIva0.get(0), arrPkAccTaxIva0.get(1), arrPkAccTaxIva0.get(2), arrPkAccTaxIva0.get(3)};
//        oDataAccountTax0.read(pkIva0, statement);
//        
//        SDataAccountTax oDataAccountTaxExc = new SDataAccountTax();
//        String[] pkIvaExc = {arrPkAccTaxExc.get(0), arrPkAccTaxExc.get(1), arrPkAccTaxExc.get(2), arrPkAccTaxExc.get(3)};
//        oDataAccountTaxExc.read(pkIvaExc, statement);
//        
//        SDataAccountTax oDataAccountRet = new SDataAccountTax();
//        String[] pkRet = {arrPkAccRetention1.get(0), arrPkAccRetention1.get(1), arrPkAccRetention1.get(2), arrPkAccRetention1.get(3)};
//        oDataAccountRet.read(pkRet, statement);
//        
//        SDataAccount oAccountIVA16 = new SDataAccount();
//        oAccountIVA16.read( new String[] { oDataAccountTax16.getFkAccountPaymentId() }, statement);
//        
//        SDataAccount oAccountLedgerIVA16 = new SDataAccount();
//        oAccountLedgerIVA16.read( new String[] { oAccountIVA16.getDbmsPkLedgerAccountIdXXX() }, statement);
//        
//        SDataAccount oAccountIVA0 = new SDataAccount();
//        oAccountIVA0.read( new String[] { oDataAccountTax0.getFkAccountPaymentId() }, statement);
//        
//        SDataAccount oAccountLedgerIVA0 = new SDataAccount();
//        oAccountLedgerIVA0.read( new String[] { oAccountIVA0.getDbmsPkLedgerAccountIdXXX() }, statement);
//        
//        SDataAccount oAccountExc = new SDataAccount();
//        oAccountExc.read( new String[] { oDataAccountTaxExc.getFkAccountPaymentId() }, statement);
//        
//        SDataAccount oAccountLedgerExc = new SDataAccount();
//        oAccountLedgerExc.read( new String[] { oAccountExc.getDbmsPkLedgerAccountIdXXX() }, statement);
//        
//        SDataAccount oAccountRetention1 = new SDataAccount();
//        oAccountRetention1.read( new String[] { oDataAccountRet.getFkAccountPaymentId() }, statement);
//        
//        SDataAccount oAccountLedgerRetention1 = new SDataAccount();
//        oAccountLedgerRetention1.read( new String[] { oAccountRetention1.getDbmsPkLedgerAccountIdXXX() }, statement);
//        
//        accIva16 = oAccountIVA16;
//        accIva0 = oAccountIVA0;
//        accExcento = oAccountExc;
//        accRetention1 = oAccountRetention1;
//        accretention2 = null;
//        
//        accLedgerIva16 = oAccountLedgerIVA16;
//        accLedgerIva0 = oAccountLedgerIVA0;
//        accLedgerExcento = oAccountLedgerExc;
//        accLedgerRetention1 = oAccountRetention1;
//        accLedgerretention2 = null;
//    }
}
