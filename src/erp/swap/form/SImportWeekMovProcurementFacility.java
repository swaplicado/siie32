/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.swap.form;

import com.fasterxml.jackson.databind.JsonNode;
import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.mbps.data.SDataBizPartner;
import erp.mfin.data.SDataAccount;
import erp.mfin.data.SDataAccountCash;
import erp.mfin.data.SDataCostCenter;
import java.io.Serializable;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import sa.lib.grid.SGridRow;

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

    public SImportWeekMovProcurementFacility() {
        Id = 0;
        Movement_date = null;
        Concept =  "";
        Reference = "";
        Debe = 0;
        Haber = 0;
        oCurrency = null;
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
    }
    
    @SuppressWarnings("deprecation")
    public SImportWeekMovProcurementFacility(final JsonNode docNode, final Statement statement, SClientInterface miClient, SImportProcurementFacility procurementFacility) throws ParseException {
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
}
