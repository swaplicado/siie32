/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mfin.form;

import erp.mfin.data.SDataAccount;

/**
 *
 * @author Adrian Aviles
 */
public class SFinRecordEntry {

    public String AccountId;
    public String Concept;
    public double Debit;
    public double Credit;
    public double ExchangeRate;
    public double ExchangeRateSystem;
    public double DebitCy;
    public double CreditCy;
    public int CurId;
    public boolean IsExchangeDifference;
    public String CostCenter;
    public int BizPartnerId;
    public String OccasionalFiscalId;
    public boolean IsForegn;
    public String Reference;
    public boolean IsReferenceTax;
    public int[] TaxKey;
    public int[] EntityKey;
    public int ItemKey;
    public int ItemAuxKey;
    public double Quantity;
    public int UnitId;
    public int[] DpsKey;
    public int[] DpsKeyAdj;
    public int Year;
    public int CheckIndex;
    public boolean IsTaxCash;

    public boolean IsSystem;
    public boolean IsDeleted;

    public SDataAccount Account;
    public SDataAccount AccountMajor;

    public boolean IsCheckAppliying;
    public boolean IsBizPartnerRequired;
    public boolean IsCurrentAccountDiogAccount;
    public boolean IsTaxRequired;
    public boolean IsRequiredEntity;
    public boolean IsRequiredYear;
    public boolean MissingFieldsBizPartnerAndOccasionalFiscalId;
    public boolean MissingFieldItem;
    public boolean FilledFieldItemAux;
    public boolean FilledFieldUnits;

    public SFinRecordEntry() {
        AccountId = "";
        Concept = "";
        Debit = 0.0;
        Credit = 0.0;
        ExchangeRate = 0.0;
        ExchangeRateSystem = 0.0;
        DebitCy = 0.0;
        CreditCy = 0.0;
        CurId = 0;
        IsExchangeDifference = false;
        CostCenter = "";
        BizPartnerId = 0;
        OccasionalFiscalId = "";
        IsForegn = false;
        Reference = "";
        IsReferenceTax = false;
        TaxKey = null;
        EntityKey = null;
        ItemKey = 0;
        ItemAuxKey = 0;
        Quantity = 0.0;
        UnitId = 0;
        DpsKey = null;
        DpsKeyAdj = null;
        Year = 0;
        CheckIndex = 0;
        IsTaxCash = false;

        IsSystem = false;
        IsDeleted = false;

        Account = null;
        AccountMajor = null;

        IsCheckAppliying = false;
        IsBizPartnerRequired = false;
        IsCurrentAccountDiogAccount = false;
        IsTaxRequired = false;
        IsRequiredEntity = false;
        IsRequiredYear = false;
        MissingFieldsBizPartnerAndOccasionalFiscalId = false;
        MissingFieldItem = false;
        FilledFieldItemAux = false;
        FilledFieldUnits = false;
    }
}
