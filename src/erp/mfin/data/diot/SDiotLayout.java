package erp.mfin.data.diot;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.mbps.data.SDataBizPartner;
import erp.mfin.data.SDataAccount;
import erp.mfin.data.SDataTax;
import erp.mtrn.data.SDataDps;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import sa.lib.SLibUtils;

/**
 *
 * @author Sergio Flores, Isabel Servín
 */
public class SDiotLayout {
    
    public static final int FORMAT_PIPE = 1; // pipe separated values
    public static final int FORMAT_CSV = 2; // comma separated values
    public static final double AMOUNT_DIFF_ALLOWANCE = 0.1;
    
    protected SClientInterface miClient;
    protected Date mtStart;
    protected Date mtEnd;
    protected HashMap<String, SDataAccount> moAccountsMap; // key: number of account
    protected HashMap<String, SDataAccount> moMajorAccountsMap; // key: number of major account
    protected HashMap<String, SDataTax> moVatsMap; // key: 'basic tax ID' + "-" + 'tax ID'
    protected HashMap<Integer, SDataBizPartner> moBizPartnersMap; // key: ID of business partner
    protected HashSet<String> moRepeatedFiscalId;
    protected String[] masDiotAccountsCodes;
    protected int[] manVatDefaultKey;
    
    public SDiotLayout(erp.client.SClientInterface client, Date start, Date end) throws Exception {
        miClient = client;
        mtStart = start;
        mtEnd = end;
        moAccountsMap = new HashMap<>();
        moMajorAccountsMap = new HashMap<>();
        moVatsMap = new HashMap<>();
        moBizPartnersMap = new HashMap<>();
        masDiotAccountsCodes = SDiotUtils.getDiotAccounts(miClient.getSession().getStatement());
        manVatDefaultKey = SDiotUtils.getDiotVatDefaultPk(miClient.getSession().getStatement());
    }
    
    private SDataAccount getAccount(final String accountCodeUser) {
        return moAccountsMap.get(accountCodeUser);
    }
    
    private void addAccount(final SDataAccount account) throws Exception {
        if (account == null) {
            throw new Exception("Se debe proporcionar una cuenta contable.");
        }
        
        if (getAccount(account.getPkAccountIdXXX()) == null) {
            moAccountsMap.put(account.getPkAccountIdXXX(), account);
        }
    }
    
    private SDataAccount getMajorAccount(final String accountCodeUser) {
        return moMajorAccountsMap.get(accountCodeUser);
    }
    
    private void addMajorAccount(final SDataAccount account) throws Exception {
        if (account == null) {
            throw new Exception("Se debe proporcionar una cuenta contable de mayor.");
        }
        
        if (getMajorAccount(account.getPkAccountIdXXX()) == null) {
            moMajorAccountsMap.put(account.getPkAccountIdXXX(), account);
        }
    }
    
    private SDataTax getVat(final int[] vatPk) {
        return moVatsMap.get(SLibUtils.textKey(vatPk));
    }
    
    private void addVat(final SDataTax tax) throws Exception {
        if (tax == null) {
            throw new Exception("Se debe proporcionar un impuesto.");
        }
        
        if (getVat((int[]) tax.getPrimaryKey()) == null) {
            if (tax.getVatType().isEmpty()) {
                throw new Exception(SDataTax.ERR_MSG_VAT_TYPE + "'" + tax.getTax() + "'.");
            }
            moVatsMap.put(SLibUtils.textKey((int[]) tax.getPrimaryKey()), tax);
        }
    }
    
    private SDataBizPartner getBizPartner(final int bizPartnerId) {
        return moBizPartnersMap.get(bizPartnerId);
    }
    
    private void addBizPartner(final SDataBizPartner bizPartner) throws Exception {
        if (bizPartner == null) {
            throw new Exception("Se debe proporcionar un asociado de negocios.");
        }
        
        if (getBizPartner(bizPartner.getPkBizPartnerId()) == null) {
            moBizPartnersMap.put(bizPartner.getPkBizPartnerId(), bizPartner);
        }
    }
    
    private Object[] createFinRecordKey(final ResultSet resultSet, final String tableAlias) throws Exception {
        return new Object[] { 
            resultSet.getInt(tableAlias + ".id_year"),
            resultSet.getInt(tableAlias + ".id_per"),
            resultSet.getInt(tableAlias + ".id_bkc"),
            resultSet.getString(tableAlias + ".id_tp_rec"),
            resultSet.getInt(tableAlias + ".id_num")
        };
    }
    
    private String composeFinRecordEntry(final ResultSet resultSet, final String tableAlias, final SDataAccount account, final SDataBizPartner bizPartner) throws Exception {
        return "Ren. pól. " + 
                resultSet.getInt(tableAlias + ".id_year") + "-" + 
                resultSet.getInt(tableAlias + ".id_per") + "-" + 
                resultSet.getInt(tableAlias + ".id_bkc") + "-" + 
                resultSet.getString(tableAlias + ".id_tp_rec") + "-" + 
                resultSet.getInt(tableAlias + ".id_num") + " " +
                resultSet.getInt(tableAlias + ".sort_pos") +
                (account == null ? "" : "/ cta. ctb. " +  account.getPkAccountIdXXX()) +
                "/ prov. " + (bizPartner == null ? SDiotConsts.THIRD_GLOBAL_NAME.toUpperCase() : bizPartner.getBizPartnerCommercial() + " " +
                "(" + (bizPartner.isDomestic(miClient) ? bizPartner.getFiscalId() : bizPartner.getFiscalFrgId()) + ")");
    }
    
    private void obtainRepeatedFiscalId() throws Exception {
        moRepeatedFiscalId = new HashSet<>();
        HashSet<String> occFiscalIds = new HashSet<>();
        HashSet<String> taxFiscalIds = new HashSet<>();
        ResultSet resultSet;
        
        String sql = "SELECT DISTINCT re.occ_fiscal_id " +
            "FROM " +
            "fin_rec AS r " +
            "INNER JOIN fin_rec_ety AS re ON re.id_year=r.id_year AND re.id_per=r.id_per AND re.id_bkc=r.id_bkc AND re.id_tp_rec=r.id_tp_rec AND re.id_num=r.id_num " +
            "WHERE " +
            "NOT r.b_del AND NOT re.b_del AND " +
            "r.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(mtStart) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(mtEnd) + "' AND " +
            "re.occ_fiscal_id <> '' " +
            "ORDER BY re.occ_fiscal_id;";
        resultSet = miClient.getSession().getStatement().executeQuery(sql);
        while (resultSet.next()) {
            occFiscalIds.add(resultSet.getString(1));
        }
        
        sql = "SELECT DISTINCT b.fiscal_id " +
            "FROM " +
            "fin_rec AS r " +
            "INNER JOIN fin_rec_ety AS re ON re.id_year=r.id_year AND re.id_per=r.id_per AND re.id_bkc=r.id_bkc AND re.id_tp_rec=r.id_tp_rec AND re.id_num=r.id_num " +
            "INNER JOIN trn_dps AS d ON d.id_year=re.fid_dps_year_n AND d.id_doc=re.fid_dps_doc_n " +
            "INNER JOIN erp.bpsu_bp AS b ON b.id_bp=d.fid_bp_r " +
            "WHERE " +
            "NOT r.b_del AND NOT re.b_del AND " +
            "r.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(mtStart) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(mtEnd) + "' AND " +
            "d.fid_ct_dps=1 AND re.fid_acc = '1160-0002-0000' " +
            "ORDER BY b.fiscal_id;";
        resultSet = miClient.getSession().getStatement().executeQuery(sql);
        while (resultSet.next()) {
            taxFiscalIds.add(resultSet.getString(1));
        }
        
        occFiscalIds.stream().filter((occFiscalId) -> (taxFiscalIds.contains(occFiscalId))).forEach((occFiscalId) -> {
            moRepeatedFiscalId.add(occFiscalId);
        });
    }
    
    /**
     * Get DIOT layout in requested format.
     * @param format Requested format: PIPE separated values or CSV.
     * @param excludeTotallyZero Exclude suppliers totally in zero.
     * @return DIOT layout as <code>String</code>.
     * @throws Exception 
     */
    public String getLayout(final int format, final boolean excludeTotallyZero) throws Exception {
        int entries = 0;
        int entriesWithoutTax = 0;
        int entriesWithoutBizPartner = 0;
        int entriesForCompany = 0;
        int entriesVatZeroUndefined = 0;
        int entriesVatNonZeroUndefined = 0;
        double debit = 0;
        double credit = 0;
        double debitTotal = 0;
        double creditTotal = 0;
        String warnings = "";
        Statement statement = miClient.getSession().getStatement().getConnection().createStatement();
        Statement statementAux = miClient.getSession().getStatement().getConnection().createStatement();
        HashMap<SDataTax, Double> vatSettlementsMap = new HashMap<>();
        HashMap<SDataTax, Double> vatWithheldsMap = new HashMap<>();
        HashMap<SDataTax, Double> vatWithheldSettlementsMap = new HashMap<>();
        HashMap<String, SDiotTercero> tercerosMap = new HashMap<>(); // key: 'business partner ID' + "-" + 'tipo de operación DIOT'
        HashMap<String, JournalEntry> accountJournalEntryTotalsMap = new HashMap<>(); // key: number of account
        
        moAccountsMap.clear();
        moMajorAccountsMap.clear();
        moVatsMap.clear();
        moBizPartnersMap.clear();
        
        // Obtener los RFC ocasionales capturados directamente en los renglones de pólizas contables, y que tienen movimientos de IVA acreditable:
        
        obtainRepeatedFiscalId();
        
        // add default VAT to map of taxes:
        
        addVat((SDataTax) SDataUtilities.readRegistry(miClient, SDataConstants.FINU_TAX, manVatDefaultKey, SLibConstants.EXEC_MODE_VERBOSE));
        
        // iterate through all DIOT accounts set up in company's configuration:
        
        ArrayList<SDiotAccount> diotAccounts = new ArrayList<>();
        
        for (String diotAccountCode : masDiotAccountsCodes) {
            diotAccounts.add(new SDiotAccount(diotAccountCode, true));
        }
        
        diotAccounts.add(new SDiotAccount("", false)); // special element to trigger search in non configuration parameter accounts
        
        for (SDiotAccount diotAccount : diotAccounts) {
            String sql = "";
            String section = "";
            double netSubtotalCalcAcum = 0;
            double netSubtotalCalcDiffAcumPos = 0; // positive
            double netSubtotalCalcDiffAcumNeg = 0; // negative
            
            if (diotAccount.IsConfigParamAccount) {
                // scan account set up in company's configuration:
                
                sql = "SELECT re.* "
                        + "FROM fin_rec AS r "
                        + "INNER JOIN fin_rec_ety AS re ON "
                        + "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num "
                        + "WHERE NOT r.b_del AND NOT re.b_del AND "
                        + "r.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(mtStart) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(mtEnd) + "' AND "
                        + "re.fid_acc LIKE '" + diotAccount.AccountCode + "%' "
                        + "ORDER BY r.dt, re.id_year, re.id_per, re.id_bkc, re.id_tp_rec, re.id_num, re.id_ety;";
                
                System.out.println();
                System.out.println(SLibUtils.textRepeat("=", 80));
                System.out.println("*** " + (section = "CUENTA CONTABLE: [" + diotAccount.AccountCode + "]") + " ***");
                System.out.println(SLibUtils.textRepeat("=", 80));
            }
            else {
                // scan other purchases and expenses accounts with explicitly tax input:
                
                sql = "SELECT re.* "
                        + "FROM fin_rec AS r "
                        + "INNER JOIN fin_rec_ety AS re ON "
                        + "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num "
                        + "INNER JOIN fin_acc AS a ON "
                        + "re.fid_acc = a.id_acc "
                        + "INNER JOIN fin_acc AS al ON "
                        + "f_acc_std_ldg(a.code) = al.code "
                        + "WHERE NOT r.b_del AND NOT re.b_del AND "
                        + "r.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(mtStart) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(mtEnd) + "' AND "
                        + "al.fid_tp_acc_sys IN (" + SDataConstantsSys.FINS_TP_ACC_SYS_PUR + ", " + SDataConstantsSys.FINS_TP_ACC_SYS_PUR_ADJ + ") AND "
                        + "(re.fid_tax_bas_n IS NOT NULL AND re.fid_tax_bas_n = " + manVatDefaultKey[0] + ") " // exclude taxes that are not VAT
                        + "ORDER BY r.dt, re.id_year, re.id_per, re.id_bkc, re.id_tp_rec, re.id_num, re.id_ety;";
                
                System.out.println();
                System.out.println(SLibUtils.textRepeat("=", 80));
                System.out.println("*** " + (section = "MOVIMIENTOS CONTABLES ADICIONALES") + " ***");
                System.out.println(SLibUtils.textRepeat("=", 80));
            }
            
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                entries++;
                debit = resultSet.getDouble("re.debit");
                credit = resultSet.getDouble("re.credit");
                debitTotal = SLibUtils.roundAmount(debitTotal + debit);
                creditTotal = SLibUtils.roundAmount(creditTotal + credit);
                
                // get account:
                
                SDataAccount account = getAccount(resultSet.getString("re.fid_acc"));
                
                if (account == null) {
                    account = (SDataAccount) SDataUtilities.readRegistry(miClient, SDataConstants.FIN_ACC, new Object[] { resultSet.getString("re.fid_acc") }, SLibConstants.EXEC_MODE_VERBOSE);
                    addAccount(account);
                }
                
                SDataAccount majorAccount = getMajorAccount(account.getDbmsPkAccountMajorIdXXX());
                
                if (majorAccount == null) {
                    if (account.getLevel() == 1) {
                        majorAccount = account; // input account is a ledger account:
                    }
                    else {
                        majorAccount = (SDataAccount) SDataUtilities.readRegistry(miClient, SDataConstants.FIN_ACC, new Object[] { account.getDbmsPkAccountMajorIdXXX() }, SLibConstants.EXEC_MODE_VERBOSE);
                    }
                    
                    addMajorAccount(majorAccount);
                }
                
                // add to account totals:
                
                JournalEntry journalEntry = accountJournalEntryTotalsMap.get(account.getPkAccountIdXXX());
                
                if (journalEntry == null) {
                    journalEntry = new JournalEntry(debit, credit);
                    accountJournalEntryTotalsMap.put(account.getPkAccountIdXXX(), journalEntry);
                }
                else {
                    journalEntry.add(debit, credit);
                }
                
                // get VAT:
                
                int[] vatPk = null;
                
                if (resultSet.getInt("re.fid_tax_bas_n") != 0 && resultSet.getInt("re.fid_tax_n") != 0) {
                    vatPk = new int[] { resultSet.getInt("re.fid_tax_bas_n"), resultSet.getInt("re.fid_tax_n") };
                }
                else {
                    entriesWithoutTax++;
                    vatPk = manVatDefaultKey;
                }
                
                SDataTax vat = getVat(vatPk);
                
                if (vat == null) {
                    vat = (SDataTax) SDataUtilities.readRegistry(miClient, SDataConstants.FINU_TAX, vatPk, SLibConstants.EXEC_MODE_VERBOSE);
                    addVat(vat);
                }
                
                // get business partner directly through ID, if any:
                
                SDataDps dps = null;
                SDataBizPartner bizPartner = null;
                
                if (resultSet.getInt("re.fid_bp_nr") != 0) {
                    bizPartner = getBizPartner(resultSet.getInt("re.fid_bp_nr"));
                    
                    if (bizPartner == null) {
                        bizPartner = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, new int[] { resultSet.getInt("re.fid_bp_nr") }, SLibConstants.EXEC_MODE_VERBOSE);
                        addBizPartner(bizPartner);
                    }
                }
                
                // get DPS, if any, and, if needed, business partner indirectly through DPS:
                
                if (resultSet.getInt("re.fid_dps_year_n") != 0 && resultSet.getInt("re.fid_dps_doc_n") != 0) {
                    dps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, new int[] { resultSet.getInt("re.fid_dps_year_n"), resultSet.getInt("re.fid_dps_doc_n") }, SLibConstants.EXEC_MODE_VERBOSE);
                }
                
                if (bizPartner == null && dps != null) {
                    bizPartner = getBizPartner(dps.getFkBizPartnerId_r());

                    if (bizPartner == null) {
                        bizPartner = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, new int[] { dps.getFkBizPartnerId_r() }, SLibConstants.EXEC_MODE_VERBOSE);
                        addBizPartner(bizPartner);
                    }
                }
                
                // get business partner (factoring bank) indirectly through CFD, if any:
                
                if (bizPartner == null && resultSet.getInt("re.fid_cfd_n") != 0) {
                    sql = "SELECT fid_fact_bank_n FROM trn_cfd WHERE id_cfd = " + resultSet.getInt("re.fid_cfd_n") + ";";
                    ResultSet resultSetAux = statementAux.executeQuery(sql);
                    if (resultSetAux.next()) {
                        bizPartner = getBizPartner(resultSetAux.getInt(1));
                        
                        if (bizPartner == null) {
                            bizPartner = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, new int[] { resultSetAux.getInt(1) }, SLibConstants.EXEC_MODE_VERBOSE);
                            addBizPartner(bizPartner);
                        }
                    }
                }
                
                // process out business partner:
                
                boolean isCompany = false;
                
                if (bizPartner == null) {
                    entriesWithoutBizPartner++;
                }
                else {
                    isCompany = SDiotTercero.checkIsCompany(miClient, bizPartner.getPkBizPartnerId());
                    if (isCompany) {
                        entriesForCompany++;
                    }
                }
                
                // get DIOT Tercero:
                
                String terceroClave;
                String occasionalFiscalId = resultSet.getString("re.occ_fiscal_id");
                
                if (bizPartner == null) {
                    if (!occasionalFiscalId.isEmpty()) {
                        terceroClave = SDiotTercero.composeOccasionalClave(occasionalFiscalId);
                    }
                    else {
                        terceroClave = SDiotTercero.GLOBAL_CLAVE; // business partner is undefined, the global third
                    }
                }
                else {
                    if (isCompany) {
                        terceroClave = SDiotTercero.GLOBAL_CLAVE; // when business partner in current entry is the company itself, then third is treated as the global one
                    }
                    else {
                        terceroClave = bizPartner.getDiotTerceroClave();
                    }
                }
                
                SDiotTercero tercero = tercerosMap.get(terceroClave);
                
                if (tercero == null) {
                    if (!occasionalFiscalId.isEmpty()) {
                        tercero = new SDiotTercero(occasionalFiscalId);
                    }
                    else {
                        tercero = new SDiotTercero(miClient, bizPartner);
                    }
                    tercerosMap.put(tercero.getClave(), tercero);
                }
                
                String warning;
                double vatAmount = SLibUtils.roundAmount(Math.abs(debit + credit)); // VAT amout of payment
                boolean isDebit = SLibUtils.belongsTo(majorAccount.getAccountClassKey(), new int[][] { SDataConstantsSys.FINS_CL_ACC_ASSET, SDataConstantsSys.FINS_CL_ACC_RES_DBT });
                boolean isCredit = SLibUtils.belongsTo(majorAccount.getAccountClassKey(), new int[][] { SDataConstantsSys.FINS_CL_ACC_LIABTY, SDataConstantsSys.FINS_CL_ACC_RES_CDT });
                
                if (isDebit) {
                    // VAT creditable:
                    
                    if (debit > 0 || credit < 0 || (debit == 0 && credit == 0)) {
                        double paymentAmount = 0;
                        double paymentRatio = 0;
                        double transactionAmount = 0;
                        SDiotAccountingTxn diotAccountingTxn = null;
                        
                        if (diotAccount.IsConfigParamAccount) {
                            // extract net total and VAT debits and credits:

                            diotAccountingTxn = new SDiotAccountingTxn(
                                    statementAux, 
                                    resultSet.getInt("re.usr_id"), 
                                    createFinRecordKey(resultSet, "re"), 
                                    new int[] { resultSet.getInt("re.fid_bkk_year_n"), resultSet.getInt("re.fid_bkk_num_n") },
                                    dps
                            );

                            paymentAmount = diotAccountingTxn.getPaymentAmount();
                            paymentRatio = dps == null || dps.getTotal_r() == 0 ? 1.0 : paymentAmount / dps.getTotal_r();
                            transactionAmount = SLibUtils.roundAmount(diotAccountingTxn.getEntryDpsSubtotal(SDataConstantsSys.FINS_TP_SYS_MOV_TAX_DBT, (int[]) vat.getPrimaryKey()) * paymentRatio);
                        }
                        else {
                            transactionAmount = vatAmount;
                        }
                        
                        // VAT creditable of payments of purchases:
                        
                        if ((debit == 0 && credit == 0) || (!diotAccount.IsConfigParamAccount && vatAmount > 0 && SLibUtils.belongsTo(vat.getVatType(), new String[] { SDiotConsts.VAT_TYPE_EXEMPT, SDiotConsts.VAT_TYPE_RATE_0 }))) {
                            switch (vat.getVatType()) {
                                case SDiotConsts.VAT_TYPE_EXEMPT:
                                    if (tercero.IsDomestic) {
                                        tercero.ValorPagosNacIvaExento = SLibUtils.roundAmount(tercero.ValorPagosNacIvaExento + transactionAmount);
                                    }
                                    else {
                                        tercero.ValorPagosImpIvaExento = SLibUtils.roundAmount(tercero.ValorPagosImpIvaExento + transactionAmount);
                                    }
                                    break;

                                case SDiotConsts.VAT_TYPE_RATE_0:
                                case SDiotConsts.VAT_TYPE_GENERAL:          // VAT deliberately manipulated to be zero
                                case SDiotConsts.VAT_TYPE_BORDER:           // VAT deliberately manipulated to be zero
                                case SDiotConsts.VAT_TYPE_BORDER_NORTH_INC: // VAT deliberately manipulated to be zero
                                    tercero.ValorPagosNacIva0 = SLibUtils.roundAmount(tercero.ValorPagosNacIva0 + transactionAmount);
                                    break;

                                default:
                                    entriesVatZeroUndefined++;
                                    
                                    warning = "" + entries + ".- " + composeFinRecordEntry(resultSet, "re", account, bizPartner) + ":\n"
                                            + " TIPO IVA DESCONOCIDO EN '" + vat.getTax() + "', tipo IVA '" + vat.getVatType() + "':\n"
                                            + " importe IVA: $" + SLibUtils.getDecimalFormatAmount().format(vatAmount) + ";\n"
                                            + " total neto real: $" + SLibUtils.getDecimalFormatAmount().format(paymentAmount) + "."; 
                                    warnings += "\"" + warning.replaceAll("\n", " ") + "\"\n";
                                    System.out.println(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION + "\n" + warning);
                            }
                        }
                        else {
                            // check if current VAT is not asigned explicitly to other third tax causings:
                            
                            ArrayList<SDiotTercero> tercerosToProcess = new ArrayList<>();
                            HashSet<Integer> causingIds = diotAccountingTxn != null ? diotAccountingTxn.getThirdTaxCausings((int[]) vat.getPrimaryKey()) : new HashSet<>();
                            
                            for (Integer causingId : causingIds) {
                                SDataBizPartner bizPartnerCausing = getBizPartner(causingId);

                                if (bizPartnerCausing == null) {
                                    bizPartnerCausing = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, new int[] { causingId }, SLibConstants.EXEC_MODE_VERBOSE);
                                    addBizPartner(bizPartnerCausing);
                                }
                                
                                SDiotTercero terceroCausing = tercerosMap.get(bizPartnerCausing.getDiotTerceroClave());
                                
                                if (terceroCausing == null) {
                                    terceroCausing = new SDiotTercero(miClient, bizPartnerCausing);
                                    tercerosMap.put(terceroCausing.getClave(), terceroCausing);
                                }
                                
                                tercerosToProcess.add(terceroCausing);
                            }
                            
                            // process DIOT terceros, including the directly current one:
                            
                            tercerosToProcess.add(tercero);
                            
                            double vatProcessed = 0;
                            double terceroExempt = 0;
                            
                            for (SDiotTercero terceroToProcess : tercerosToProcess) {
                                if (vatProcessed >= vatAmount) {
                                    if (terceroExempt != 0) {
                                        if (terceroToProcess.IsDomestic) {
                                            terceroToProcess.ValorPagosNacIvaExento = SLibUtils.roundAmount(terceroToProcess.ValorPagosNacIvaExento + terceroExempt);
                                        }
                                        else {
                                            terceroToProcess.ValorPagosImpIvaExento = SLibUtils.roundAmount(terceroToProcess.ValorPagosImpIvaExento + terceroExempt);
                                        }
                                    }
                                    break; // no more VAT to be processed
                                }
                                else {
                                    double vatToProcess;

                                    if (terceroToProcess == tercero) {
                                        // VAT corresponds to supplier:
                                        vatToProcess = SLibUtils.roundAmount(vatAmount - vatProcessed);
                                    }
                                    else {
                                        // VAT corresponds to a third tax causing:
                                        vatToProcess = SLibUtils.roundAmount(diotAccountingTxn.getThirdTax(terceroToProcess.BizPartnerId, (int[]) vat.getPrimaryKey()) * paymentRatio);
                                        terceroExempt = SLibUtils.roundAmount(diotAccountingTxn.getThirdTaxSubtotal(terceroToProcess.BizPartnerId, (int[]) vat.getPrimaryKey()) * paymentRatio);
                                    }

                                    vatProcessed = SLibUtils.roundAmount(vatProcessed + vatToProcess);
                                    
                                    double netSubtotalCalc = SLibUtils.roundAmount(vat.getPercentage() == 0 ? 0 : vatToProcess / vat.getPercentage());

                                    if (terceroToProcess == tercero) {
                                        double netSubtotalCalcDiff = SLibUtils.roundAmount(transactionAmount - netSubtotalCalc);

                                        if (netSubtotalCalcDiff < -AMOUNT_DIFF_ALLOWANCE) {
                                            // suspicious situation: calculated net-total is greater than the real one:
                                            
                                            netSubtotalCalcDiffAcumNeg = SLibUtils.roundAmount(netSubtotalCalcDiffAcumNeg + netSubtotalCalcDiff);

                                            warning = "" + entries + ".- " + composeFinRecordEntry(resultSet, "re", account, bizPartner) + ":\n"
                                                    + " Discrepancia NO corregible en impuesto '" + vat.getTax() + "', tipo IVA '" + vat.getVatType() + "':\n"
                                                    + " importe IVA: $" + SLibUtils.getDecimalFormatAmount().format(vatAmount) + "; "
                                                    + "IVA a procesar: $" + SLibUtils.getDecimalFormatAmount().format(vatToProcess) + "; "
                                                    + "IVA remanente: $" + SLibUtils.getDecimalFormatAmount().format(vatAmount - vatProcessed) + ";\n"
                                                    + " total neto real: $" + SLibUtils.getDecimalFormatAmount().format(paymentAmount) + "; "
                                                    + "subtotal real: $" + SLibUtils.getDecimalFormatAmount().format(transactionAmount) + " < "
                                                    + "subtotal calculado: $" + SLibUtils.getDecimalFormatAmount().format(netSubtotalCalc) + " = "
                                                    + "diferencia: $" + SLibUtils.getDecimalFormatAmount().format(netSubtotalCalcDiff) + "."; 
                                            warnings += "\"" + warning.replaceAll("\n", " ") + "\"\n";
                                            System.out.println(warning);
                                        }
                                        else if (netSubtotalCalcDiff > AMOUNT_DIFF_ALLOWANCE) {
                                            // manageable situation: calculated net-total is less than the real one:
                                            
                                            netSubtotalCalcDiffAcumPos = SLibUtils.roundAmount(netSubtotalCalcDiffAcumPos + netSubtotalCalcDiff);

                                            warning = "" + entries + ".- " + composeFinRecordEntry(resultSet, "re", account, bizPartner) + ":\n"
                                                    + " Discrepancia SÍ corregible en impuesto '" + vat.getTax() + "', tipo IVA '" + vat.getVatType() + "':\n"
                                                    + " importe IVA: $" + SLibUtils.getDecimalFormatAmount().format(vatAmount) + "; "
                                                    + "IVA a procesar: $" + SLibUtils.getDecimalFormatAmount().format(vatToProcess) + "; "
                                                    + "IVA remanente: $" + SLibUtils.getDecimalFormatAmount().format(vatAmount - vatProcessed) + ":\n"
                                                    + " total neto real: $" + SLibUtils.getDecimalFormatAmount().format(paymentAmount) + "; "
                                                    + "subtotal real: $" + SLibUtils.getDecimalFormatAmount().format(transactionAmount) + " > "
                                                    + "subtotal calculado: $" + SLibUtils.getDecimalFormatAmount().format(netSubtotalCalc) + " = "
                                                    + "diferencia: $" + SLibUtils.getDecimalFormatAmount().format(netSubtotalCalcDiff) + "."; 
                                            warnings += "\"" + warning.replaceAll("\n", " ") + "\"\n";
                                            System.out.println(warning);

                                            if (terceroToProcess.IsDomestic) {
                                                terceroToProcess.ValorPagosNacIvaExento = SLibUtils.roundAmount(terceroToProcess.ValorPagosNacIvaExento + netSubtotalCalcDiff);
                                            }
                                            else {
                                                terceroToProcess.ValorPagosImpIvaExento = SLibUtils.roundAmount(terceroToProcess.ValorPagosImpIvaExento + netSubtotalCalcDiff);
                                            }
                                        }
                                        else if (netSubtotalCalcDiff != 0) {
                                            // adjust calculated subtotal due to difference between real and calculated net-total:
                                            netSubtotalCalc = SLibUtils.roundAmount(netSubtotalCalc + netSubtotalCalcDiff);
                                        }
                                    }

                                    netSubtotalCalcAcum = SLibUtils.roundAmount(netSubtotalCalcAcum + netSubtotalCalc);
                                    System.out.println("debit: " + SLibUtils.getDecimalFormatAmount().format(debit) + "; "
                                            + "subtotal calc.: " + SLibUtils.getDecimalFormatAmount().format(netSubtotalCalc) + "; "
                                            + "subtotal calc. acum.: " + SLibUtils.getDecimalFormatAmount().format(netSubtotalCalcAcum));
                                    
                                    switch (vat.getVatType()) {
                                        case SDiotConsts.VAT_TYPE_GENERAL:
                                            if (terceroToProcess.IsDomestic) {
                                                terceroToProcess.ValorPagosNacIva1516 = SLibUtils.roundAmount(terceroToProcess.ValorPagosNacIva1516 + netSubtotalCalc);
                                            }
                                            else {
                                                terceroToProcess.ValorPagosImpIva1516 = SLibUtils.roundAmount(terceroToProcess.ValorPagosImpIva1516 + netSubtotalCalc);
                                            }
                                            break;

                                        case SDiotConsts.VAT_TYPE_BORDER:
                                            if (terceroToProcess.IsDomestic) {
                                                terceroToProcess.ValorPagosNacIva1011 = SLibUtils.roundAmount(terceroToProcess.ValorPagosNacIva1011 + netSubtotalCalc);
                                            }
                                            else {
                                                terceroToProcess.ValorPagosImpIva1011 = SLibUtils.roundAmount(terceroToProcess.ValorPagosImpIva1011 + netSubtotalCalc);
                                            }
                                            break;

                                        case SDiotConsts.VAT_TYPE_BORDER_NORTH_INC:
                                            terceroToProcess.ValorPagosNacIvaEstFront = SLibUtils.roundAmount(terceroToProcess.ValorPagosNacIvaEstFront + netSubtotalCalc);
                                            break;

                                        default:
                                            entriesVatNonZeroUndefined++;

                                            warning = "" + entries + ".- " + composeFinRecordEntry(resultSet, "re", account, bizPartner) + ":\n"
                                                    + " TIPO IVA DESCONOCIDO EN '" + vat.getTax() + "', tipo IVA '" + vat.getVatType() + "':\n"
                                                    + " importe IVA: $" + SLibUtils.getDecimalFormatAmount().format(vatAmount) + "; "
                                                    + "IVA a procesar: $" + SLibUtils.getDecimalFormatAmount().format(vatToProcess) + "; "
                                                    + "IVA remanente: $" + SLibUtils.getDecimalFormatAmount().format(vatAmount - vatProcessed) + ":\n"
                                                    + " total neto real: $" + SLibUtils.getDecimalFormatAmount().format(paymentAmount) + "."; 
                                            warnings += "\"" + warning.replaceAll("\n", " ") + "\"\n";
                                            System.out.println(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION + "\n" + warning);
                                    }
                                }
                            }
                        }
                    }
                    else if (debit < 0 || credit > 0) {
                        // VAT creditable of purchases adjustments & VAT creditable settlements:
                        
                        if (tercero.IsGlobal) {
                            // VAT creditable settlement:
                            
                            Double settlement = vatSettlementsMap.get(vat);
                            
                            if (settlement == null) {
                                vatSettlementsMap.put(vat, vatAmount);
                            }
                            else {
                                vatSettlementsMap.put(vat, SLibUtils.roundAmount(settlement + vatAmount));
                            }
                        }
                        else {
                            // VAT creditable of purchases adjustment:
                            
                            tercero.IvaNotasCréditoCompras = SLibUtils.roundAmount(tercero.IvaNotasCréditoCompras + vatAmount);
                        }
                    }
                }
                else if (isCredit) {
                    // VAT creditable withheld:
                    
                    if (debit < 0 || credit > 0 || (debit == 0 && credit == 0)) {
                        // VAT creditable withheld of payments of purchases:
                        
                        tercero.IvaRetenido = SLibUtils.roundAmount(tercero.IvaRetenido + vatAmount);
                        
                        // preserve withheld:
                        
                        Double withheld = vatWithheldsMap.get(vat);
                        
                        if (withheld == null) {
                            vatWithheldsMap.put(vat, vatAmount);
                        }
                        else {
                            vatWithheldsMap.put(vat, SLibUtils.roundAmount(withheld + vatAmount));
                        }
                    }
                    else if (debit > 0 || credit < 0) {
                        // VAT creditable withheld of purchases adjustments & VAT creditable withheld settlements:
                        
                        if (tercero.IsGlobal) {
                            // VAT creditable withheld settlement:
                            
                            Double withheldSettlement = vatWithheldSettlementsMap.get(vat);
                            
                            if (withheldSettlement == null) {
                                vatWithheldSettlementsMap.put(vat, vatAmount);
                            }
                            else {
                                vatWithheldSettlementsMap.put(vat, SLibUtils.roundAmount(withheldSettlement + vatAmount));
                            }
                        }
                        else {
                            // VAT creditable withheld of purchases adjustment:
                            
                            tercero.IvaRetenido = SLibUtils.roundAmount(tercero.IvaRetenido - vatAmount);
                            
                            // preserve withheld adjustment:
                        
                            Double withheld = vatWithheldsMap.get(vat);

                            if (withheld == null) {
                                vatWithheldsMap.put(vat, vatAmount);
                            }
                            else {
                                vatWithheldsMap.put(vat, SLibUtils.roundAmount(withheld - vatAmount));
                            }
                        }
                    }
                }
                else {
                    throw new Exception("" + entries + ".- " + composeFinRecordEntry(resultSet, "re", account, bizPartner) + ":\n"
                            + " No pudo determinarse si la cuenta contable de mayor " + majorAccount.getCode() + ", '" + majorAccount.getAccount() + "', es de débito o de crédito.");
                }
                
                // identify business partner by CFDI:
                // identify business partner by ID of business partner:
                // process non identifiable business partner:
            }
            
            // print out subtotal:
            
            SDiotTercero subtotal = new SDiotTercero();
            for (SDiotTercero tercero : tercerosMap.values()) {
                subtotal.addTercero(tercero);
            }
            
            System.out.println(SLibUtils.textRepeat("-", 80));
            System.out.println("RESUMEN: " + section);
            System.out.println("SUBTOTALES:");
            System.out.println(subtotal.toString());
            System.out.println("Diferencia total (-) en cálculo de subtotal: " + SLibUtils.getDecimalFormatAmount().format(netSubtotalCalcDiffAcumNeg));
            System.out.println("Diferencia total (+) en cálculo de subtotal: " + SLibUtils.getDecimalFormatAmount().format(netSubtotalCalcDiffAcumPos));
            
            for (String account : accountJournalEntryTotalsMap.keySet()) {
                JournalEntry journalEntry = accountJournalEntryTotalsMap.get(account);
                System.out.println("Cuenta: " + account + "; "
                        + "debe: " + SLibUtils.getDecimalFormatAmount().format(journalEntry.Debit) + "; "
                        + "haber: " + SLibUtils.getDecimalFormatAmount().format(journalEntry.Credit) + "; "
                        + "saldo: " + SLibUtils.getDecimalFormatAmount().format(journalEntry.Debit - journalEntry.Credit));
            }
            
            System.out.println(SLibUtils.textRepeat("-", 80));
        }
        
        // compose layout:

        String layout = "";
        int totallyZero = 0;
        SDiotTercero terceroTotal = null;
        SDiotTercero terceroCompany = null;
        
        // Contendrá los registros de terceros cuyo RFC no esta repetido.
        HashSet<SDiotTercero> terceros = new HashSet<>();
        
        // Contendrá los registros te terceros cuyo RFC esta repetido, es decir que aparecen en los ocasionales agregados a las pólizas y los que tienen movimientos de IVA acreditable
        HashMap<String, SDiotTercero> tercerosRepetidos = new HashMap<>();
        
        tercerosMap.values().stream().forEach((map) -> {
            if (moRepeatedFiscalId.contains(map.Rfc)){
                if(tercerosRepetidos.containsKey(map.Rfc)) {
                    SDiotTercero aux = tercerosRepetidos.get(map.Rfc);
                    aux.ValorPagosImpIva1011 += map.ValorPagosImpIva1011;
                    aux.ValorPagosImpIva1516 += map.ValorPagosImpIva1516;
                    aux.ValorPagosImpIvaExento += map.ValorPagosImpIvaExento;
                    aux.ValorPagosNacIva0 += map.ValorPagosNacIva0;
                    aux.ValorPagosNacIva10 += map.ValorPagosNacIva10;
                    aux.ValorPagosNacIva1011 += map.ValorPagosNacIva1011;
                    aux.ValorPagosNacIva15 += map.ValorPagosNacIva15;
                    aux.ValorPagosNacIva1516 += map.ValorPagosNacIva1516;
                    aux.ValorPagosNacIvaEstFront += map.ValorPagosNacIvaEstFront;
                    aux.ValorPagosNacIvaExento += map.ValorPagosNacIvaExento;
                }
                else {
                    tercerosRepetidos.put(map.Rfc, map);
                }
            }
            else {
                terceros.add(map);
            }
        });
        // Se agregan los repetidos con los demas registros para procesarse.
        tercerosRepetidos.values().stream().forEach((terceroRepetido) -> {
            terceros.add(terceroRepetido);
        });
        
        if (format == FORMAT_CSV) {
            terceroTotal = new SDiotTercero();
            
            layout += "\"" + miClient.getSessionXXX().getCurrentCompanyName() + "\"\n";
            layout += "\"Layout DIOT\"\n";
            layout += "\"Del " + SLibUtils.DateFormatDate.format(mtStart) + " al " + SLibUtils.DateFormatDate.format(mtEnd) + "\"\n";
            layout += SDiotTercero.getLayoutRowHeadings() + "\n";
        }
        
        // suppliers:
        
        for (SDiotTercero tercero : terceros) {
            if (format == FORMAT_CSV) {
                terceroTotal.addTercero(tercero);
            }
            
            if (tercero.isTotallyZero()) {
                totallyZero++;
            }
            
            if (tercero.IsGlobal) {
                if (terceroCompany == null) {
                    terceroCompany = tercero;
                }
                else {
                    throw new Exception("Al menos existen dos terceros que corresponden a esta empresa (" + tercero.Rfc + ").");
                }
            }
            else {
                if (!excludeTotallyZero || !tercero.isTotallyZero()) {
                    layout += tercero.getLayoutRow(format) + "\n";
                }
            }
        }
        
        // company itself:
        
        if (terceroCompany != null) {
            layout += terceroCompany.getLayoutRow(format) + "\n";
        }
        
        if (format == FORMAT_CSV) {
            DecimalFormat amountFormat = new DecimalFormat("#0.00");
            
            layout += "\n";
            layout += "\"Totales:\"\n";
            layout += terceroTotal.getLayoutRow(format) + "\n";
            
            layout += "\n";
            layout += "\"Totales por cuenta contable:\"\n";
            layout += "\"Cuenta contable\",\"Debe\",\"Haber\",\"Saldo\"\n";
            for (String account : accountJournalEntryTotalsMap.keySet()) {
                JournalEntry journalEntry = accountJournalEntryTotalsMap.get(account);
                layout += "\"'" + account + "\"," + 
                        journalEntry.Debit + "," + 
                        journalEntry.Credit + "," + 
                        (journalEntry.Debit - journalEntry.Credit) + "\n";
            }
            layout += "\"Total:\"," + 
                    debitTotal + "," + 
                    creditTotal + "," + 
                    (debitTotal - creditTotal) + "\n";
            
            SDataTax vatDefault = getVat(manVatDefaultKey);
            
            layout += "\n";
            layout += "\"Resumen:\"\n";
            layout += "\"renglones pólizas contables procesados:\"," + entries + "\n";
            layout += "\"renglones pólizas contables sin impuesto:\"," + entriesWithoutTax + ",\"(asignadas el impuesto predeterminado para DIOT: " + vatDefault.getTax() + ")\"\n";
            layout += "\"renglones pólizas contables sin asociado de negocios:\"," + entriesWithoutBizPartner + ",\"(asignados a " + SDiotConsts.THIRD_GLOBAL_NAME + ")\"\n";
            layout += "\"renglones pólizas contables de la empresa '" + miClient.getSessionXXX().getCurrentCompanyName() + "':\"," + entriesForCompany + ",\"(incluidos en este layout)\"\n";
            layout += "\"renglones pólizas contables con impuesto igual a cero, pero de tipo IVA desconocido:\"," + entriesVatZeroUndefined + ",\"(excluidos de este layout)\"\n";
            layout += "\"renglones pólizas contables con impuesto distinto a cero, pero de tipo IVA desconocido:\"," + entriesVatNonZeroUndefined + ",\"(excluidos de este layout)\"\n";
            layout += "\"terceros totalmente en cero:\"," + totallyZero + ",\"" + (excludeTotallyZero ? "(excluídos de este layout)" : "(incluidos en este layout)") + "\"\n";

            layout += "\n";
            layout += "\"Liquidación de impuestos:\"\n";
            for (SDataTax vat : vatSettlementsMap.keySet()) {
                Double amount = vatSettlementsMap.get(vat);
                layout += "\"impuesto:\",\"" + vat.getTax() + "\",\"tasa:\"," + SLibUtils.getDecimalFormatPercentageTax().format(vat.getPercentage()) + ",\"monto:\"," + amountFormat.format(amount) + "\n";
            }

            layout += "\n";
            layout += "\"Retención de impuestos:\"\n";
            for (SDataTax vat : vatWithheldsMap.keySet()) {
                Double amount = vatWithheldsMap.get(vat);
                layout += "\"impuesto:\",\"" + vat.getTax() + "\",\"tasa:\"," + SLibUtils.getDecimalFormatPercentageTax().format(vat.getPercentage()) + ",\"monto:\"," + amountFormat.format(amount) + "\n";
            }

            layout += "\n";
            layout += "\"Liquidación de retención de impuestos:\"\n";
            for (SDataTax vat : vatWithheldSettlementsMap.keySet()) {
                Double amount = vatWithheldSettlementsMap.get(vat);
                layout += "\"impuesto:\",\"" + vat.getTax() + "\",\"tasa:\"," + SLibUtils.getDecimalFormatPercentageTax().format(vat.getPercentage()) + ",\"monto:\"," + amountFormat.format(amount) + "\n";
            }

            layout += "\n";
            layout += "\"Excepciones:\"\n";
            layout += warnings.isEmpty() ? "\"¡No hay excepciones!\"\n" : warnings; 
        }
        
        return SLibUtils.textToAscii(layout);
    }
    
    public class JournalEntry {
        public double Debit;
        public double Credit;
        
        public JournalEntry(final double debit, final double credit) {
            Debit = debit;
            Credit = credit;
        }
        
        public void add(final double debit, final double credit) {
            Debit = SLibUtils.roundAmount(Debit + debit);
            Credit = SLibUtils.roundAmount(Credit + credit);
        }
    }
}
