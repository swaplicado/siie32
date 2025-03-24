package erp.mfin.data.diot.ver2;

import cfd.DCfdConsts;
import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.mbps.data.SDataBizPartner;
import erp.mfin.data.SDataAccount;
import erp.mfin.data.SDataTax;
import erp.mfin.data.diot.SDiotAccount;
import erp.mfin.data.diot.SDiotAccountingTxn;
import erp.mfin.data.diot.SDiotConsts;
import erp.mfin.data.diot.SDiotUtils;
import erp.mod.SModSysConsts;
import erp.mtrn.data.SDataDps;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;

/**
 * For DIOT layout valid since 2025-01-01.
 * @author Sergio Flores
 */
public class SDiotLayout {
    
    protected static final double AMOUNT_DIFF_ALLOWANCE = 0.1;
    protected static final double FIXED_VAT_THIRD_TAXPAYER = 0.01; // fixed pre-arranged subtotal of VAT of third-taxpayer
    protected static final DecimalFormat FormatRecordNumber = new DecimalFormat(SLibUtils.textRepeat("0", SDataConstantsSys.NUM_LEN_FIN_REC));
    
    protected SClientInterface miClient;
    protected Date mtStart;
    protected Date mtEnd;
    protected ArrayList<String> maRequredFiscalIds;
    
    protected HashMap<String, SDataAccount> moAccountsMap; // key: number of account
    protected HashMap<String, SDataAccount> moLedgerAccountsMap; // key: number of ledger account
    protected HashMap<String, SDataTax> moVatsMap; // key: 'basic tax ID' + "-" + 'tax ID'
    protected HashMap<Integer, SDataBizPartner> moBizPartnersMap; // key: ID of business partner
    protected HashSet<String> moDuplicatedOccasionalFiscalIdsMap; // set of occasional fiscal IDs that also are referrenced directly or indirectly into accounting
    protected String[] masConfigDiotAccountCodes;
    protected int[] manConfigDefaultVatKey;
    protected int[] manConfigTaxRegionsIgnored;
    protected int mnThisCompanyId;
    protected String msThisCompanyFiscalId;
    
    public SDiotLayout(erp.client.SClientInterface client, Date start, Date end, ArrayList<String> requredFiscalIds) throws Exception {
        miClient = client;
        mtStart = start;
        mtEnd = end;
        maRequredFiscalIds = requredFiscalIds;
        
        moAccountsMap = new HashMap<>();
        moLedgerAccountsMap = new HashMap<>();
        moVatsMap = new HashMap<>();
        moBizPartnersMap = new HashMap<>();
        masConfigDiotAccountCodes = SDiotUtils.getDiotAccounts(miClient.getSession().getStatement());
        manConfigDefaultVatKey = SDiotUtils.getDiotVatDefaultKey(miClient.getSession().getStatement());
        manConfigTaxRegionsIgnored = SDiotUtils.getDiotTaxRegionsIgnored(miClient.getSession().getStatement());
        mnThisCompanyId = miClient.getSession().getConfigCompany().getCompanyId();
        msThisCompanyFiscalId = miClient.getSessionXXX().getCompany().getDbmsDataCompany().getFiscalId();
    }
    
    private boolean isThisCompany(final int bizPartnerId, final String fiscalId) {
        return bizPartnerId == mnThisCompanyId || fiscalId.equals(msThisCompanyFiscalId);
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
    
    private SDataAccount getLedgerAccount(final String accountCodeUser) {
        return moLedgerAccountsMap.get(accountCodeUser);
    }
    
    private void addLedgerAccount(final SDataAccount account) throws Exception {
        if (account == null) {
            throw new Exception("Se debe proporcionar una cuenta contable de mayor.");
        }
        
        if (getLedgerAccount(account.getPkAccountIdXXX()) == null) {
            moLedgerAccountsMap.put(account.getPkAccountIdXXX(), account);
        }
    }
    
    private SDataTax getVat(final int[] vatKey) {
        return moVatsMap.get(SLibUtils.textKey(vatKey));
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
    
    private Object[] createRecordKey(final ResultSet resultSet) throws Exception {
        return new Object[] { 
            resultSet.getInt("re.id_year"),
            resultSet.getInt("re.id_per"),
            resultSet.getInt("re.id_bkc"),
            resultSet.getString("re.id_tp_rec"),
            resultSet.getInt("re.id_num")
        };
    }
    
    private Object[] createCustomRecordEntryKey(final ResultSet resultSet) throws Exception {
        return new Object[] { 
            SLibUtils.DecimalFormatCalendarYear.format(resultSet.getInt("re.id_year")),
            SLibUtils.DecimalFormatCalendarMonth.format(resultSet.getInt("re.id_per")),
            resultSet.getInt("re.id_bkc"),
            resultSet.getString("re.id_tp_rec"),
            FormatRecordNumber.format(resultSet.getInt("re.id_num")),
            resultSet.getInt("re.sort_pos")
        };
    }
    
    private String composeCustomRecordEntry(final ResultSet resultSet, final SDataAccount account, final SDataBizPartner bizPartner, final String occasionalFiscalId) throws Exception {
        String thirdParty;
        
        if (bizPartner != null) {
            thirdParty = bizPartner.getBizPartnerCommercial() + " (" + (bizPartner.isDomestic(miClient) ? bizPartner.getFiscalId() : bizPartner.getFiscalFrgId()) + ")";
        }
        else if (!occasionalFiscalId.isEmpty()) {
            thirdParty = occasionalFiscalId;
        }
        else {
            thirdParty = SDiotConsts.NAME_THIRD_GLOBAL;
        }
        
        return "Ren. pól. " + 
                SLibUtils.DecimalFormatCalendarYear.format(resultSet.getInt("re.id_year")) + "-" + 
                SLibUtils.DecimalFormatCalendarMonth.format(resultSet.getInt("re.id_per")) + "-" + 
                resultSet.getInt("re.id_bkc") + "-" + 
                resultSet.getString("re.id_tp_rec") + "-" + 
                FormatRecordNumber.format(resultSet.getInt("re.id_num")) + " " +
                resultSet.getInt("re.sort_pos") +
                (account == null ? "" : "/ cta. ctb. '" +  account.getPkAccountIdXXX()) + "'"+
                "/ prov. '" + thirdParty + "'";
    }
    
    private void obtainDuplicatedOccasionalFiscalIds() throws Exception {
        String sql;
        ResultSet resultSet;
        HashSet<String> occasionalFiscalIds = new HashSet<>();
        HashSet<String> referencedFiscalIds = new HashSet<>();
        
        // prepare filter for configured DIOT accounts:
        
        String sqlDiotAccounts = "";
        
        for (String accountCode : masConfigDiotAccountCodes) {
            sqlDiotAccounts += (!sqlDiotAccounts.isEmpty() ? " OR " : "") + "re.fid_acc LIKE '" + accountCode + "%'";
        }
        
        // get occasional fiscal IDs from required period (those ones that are not in a DIOT account, or, if they are, when there is not any referenced business partner):
        
        sql = "SELECT DISTINCT re.occ_fiscal_id " +
                "FROM fin_rec AS r " +
                "INNER JOIN fin_rec_ety AS re ON re.id_year = r.id_year AND re.id_per = r.id_per AND re.id_bkc = r.id_bkc AND re.id_tp_rec = r.id_tp_rec AND re.id_num = r.id_num " +
                "WHERE NOT r.b_del AND NOT re.b_del " +
                "AND r.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(mtStart) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(mtEnd) + "' " +
                "AND re.occ_fiscal_id <> '' AND (" +
                "(NOT (" + sqlDiotAccounts + ") AND re.fid_tax_bas_n = " + manConfigDefaultVatKey[0] + ") OR " + // only VAT entries
                "((" + sqlDiotAccounts + ") AND re.fid_bp_nr IS NULL AND re.fid_dps_year_n IS NULL AND re.fid_dps_doc_n IS NULL AND re.fid_cfd_n IS NULL)) " +
                "ORDER BY re.occ_fiscal_id;";
        resultSet = miClient.getSession().getStatement().executeQuery(sql);
        while (resultSet.next()) {
            occasionalFiscalIds.add(resultSet.getString(1));
        }
        
        // get fiscal IDs from directly referenced business partners:
        
        sql = "SELECT b.fiscal_id " + // business partners without DPS
                "FROM fin_rec AS r " +
                "INNER JOIN fin_rec_ety AS re ON re.id_year = r.id_year AND re.id_per = r.id_per AND re.id_bkc = r.id_bkc AND re.id_tp_rec = r.id_tp_rec AND re.id_num = r.id_num " +
                "INNER JOIN erp.bpsu_bp AS b ON b.id_bp = re.fid_bp_nr " +
                "WHERE NOT r.b_del AND NOT re.b_del " +
                "AND r.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(mtStart) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(mtEnd) + "' " +
                "AND (" + sqlDiotAccounts + ") AND re.fid_dps_year_n IS NULL AND re.fid_dps_doc_n IS NULL " +
                "UNION " +
                "SELECT b.fiscal_id " + // direct business partners with DPS
                "FROM fin_rec AS r " +
                "INNER JOIN fin_rec_ety AS re ON re.id_year = r.id_year AND re.id_per = r.id_per AND re.id_bkc = r.id_bkc AND re.id_tp_rec = r.id_tp_rec AND re.id_num = r.id_num " +
                "INNER JOIN trn_dps AS d ON d.id_year = re.fid_dps_year_n AND d.id_doc = re.fid_dps_doc_n " +
                "INNER JOIN trn_dps_ety AS de ON de.id_year = d.id_year AND de.id_doc = d.id_doc AND de.fid_third_tax_n IS NULL " +
                "INNER JOIN erp.bpsu_bp AS b ON b.id_bp = d.fid_bp_r " +
                "WHERE NOT r.b_del AND NOT re.b_del " +
                "AND r.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(mtStart) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(mtEnd) + "' " +
                "AND (" + sqlDiotAccounts + ") AND d.fid_ct_dps = " + SModSysConsts.TRNS_CT_DPS_PUR + " " +
                "UNION " +
                "SELECT b.fiscal_id " + // indirect business partners with DPS
                "FROM fin_rec AS r " +
                "INNER JOIN fin_rec_ety AS re ON re.id_year = r.id_year AND re.id_per = r.id_per AND re.id_bkc = r.id_bkc AND re.id_tp_rec = r.id_tp_rec AND re.id_num = r.id_num " +
                "INNER JOIN trn_dps AS d ON d.id_year = re.fid_dps_year_n AND d.id_doc = re.fid_dps_doc_n " +
                "INNER JOIN trn_dps_ety AS de ON de.id_year = d.id_year AND de.id_doc = d.id_doc AND de.fid_third_tax_n IS NOT NULL " +
                "INNER JOIN erp.bpsu_bp AS b ON b.id_bp = de.fid_third_tax_n " +
                "WHERE NOT r.b_del AND NOT re.b_del " +
                "AND r.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(mtStart) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(mtEnd) + "' " +
                "AND (" + sqlDiotAccounts + ") AND d.fid_ct_dps = " + SModSysConsts.TRNS_CT_DPS_PUR + " " +
                "UNION " +
                "SELECT c.fid_fact_bank_n " + // factoring business partners with CFD
                "FROM fin_rec AS r " +
                "INNER JOIN fin_rec_ety AS re ON re.id_year = r.id_year AND re.id_per = r.id_per AND re.id_bkc = r.id_bkc AND re.id_tp_rec = r.id_tp_rec AND re.id_num = r.id_num " +
                "INNER JOIN trn_cfd AS c ON c.id_cfd = re.fid_cfd_n " +
                "WHERE NOT r.b_del AND NOT re.b_del " +
                "AND r.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(mtStart) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(mtEnd) + "' " +
                "AND (" + sqlDiotAccounts + ") AND c.fid_fact_bank_n IS NOT NULL " +
                "ORDER BY fiscal_id;";
        
        resultSet = miClient.getSession().getStatement().executeQuery(sql);
        while (resultSet.next()) {
            referencedFiscalIds.add(resultSet.getString(1));
        }
        
        // identify occasional fiscal IDs found as well as referenced IDs:
        
        moDuplicatedOccasionalFiscalIdsMap = new HashSet<>();
        
        for (String fiscalId : occasionalFiscalIds) {
            if (referencedFiscalIds.contains(fiscalId)) {
                moDuplicatedOccasionalFiscalIdsMap.add(fiscalId);
            }
        }
    }
    
    /**
     * Get DIOT layout in requested format.
     * @param format Requested format: PIPE separated values or CSV.
     * @param excludeTercerosTotallyZero Exclude terceros totally in zero.
     * @param generateDetailedInfo Generate DIOT detailed information. Works only for CSV format.
     * @return <code>String</code> array: index 0: DIOT layout; index 1: DIOT detailed information when requested, otherwise an empty string is provided.
     * @throws Exception 
     */
    @SuppressWarnings("deprecation")
    public String[] getLayout(final int format, final boolean excludeTercerosTotallyZero, final boolean generateDetailedInfo) throws Exception {
        int accounts = 0;
        int entries = 0;
        int entriesWithoutVat = 0;
        int entriesWithoutCatalogBizPartner = 0;
        int entriesForThisCompany = 0; // there should not be any entry for this company!
        int entriesVatZeroObsolete = 0;
        int entriesVatZeroUndefined = 0;
        int entriesVatNonZeroObsolete = 0;
        int entriesVatNonZeroUndefined = 0;
        int entriesVatAdjustsNonZeroObsolete = 0;
        int entriesVatAdjustsNonZeroUndefined = 0;
        int year = SLibTimeUtils.digestYear(mtStart)[0];
        double totalDebit = 0;
        double totalCredit = 0;
        boolean collectDetailedInfo = format == SDiotConsts.FORMAT_CSV && generateDetailedInfo;
        String warning = "";
        String warnings = "";
        String requiredFiscalIds = "";
        Statement statement = miClient.getSession().getStatement().getConnection().createStatement();
        Statement statementAux = miClient.getSession().getStatement().getConnection().createStatement();
        HashMap<SDataTax, Double> vatSettlementsMap = new HashMap<>();
        HashMap<SDataTax, Double> vatWithheldsMap = new HashMap<>();
        HashMap<SDataTax, Double> vatWithheldSettlementsMap = new HashMap<>();
        HashMap<String, SDiotTercero> tercerosMap = new HashMap<>(); // key: 'business partner ID' + "-" + 'tipo de operación DIOT'
        HashMap<String, AccountTotal> accountTotalsMap = new HashMap<>(); // key: number of account
        ArrayList<String> layoutSkippedRecordEntries = new ArrayList<>();
        ArrayList<DiotEntry> layoutDiotEntries = new ArrayList<>();
        
        moAccountsMap.clear();
        moLedgerAccountsMap.clear();
        moVatsMap.clear();
        moBizPartnersMap.clear();
        
        // obtain occasional fiscal IDs found as well as referenced IDs:

        if (maRequredFiscalIds.isEmpty()) {
            // process all fiscal ID:
            obtainDuplicatedOccasionalFiscalIds();
        }
        else {
            // process only required fiscal ID:
            for (String fiscalId : maRequredFiscalIds) {
                requiredFiscalIds += (requiredFiscalIds.isEmpty() ? "" : ", ") + "'" + fiscalId + "'";
            }
        }
        
        // add default VAT to map of taxes:
        
        addVat((SDataTax) SDataUtilities.readRegistry(miClient, SDataConstants.FINU_TAX, manConfigDefaultVatKey, SLibConstants.EXEC_MODE_VERBOSE));
        
        /*
         * 1. Iterate through all DIOT accounts set up in company's configuration:
         */
        
        ArrayList<SDiotAccount> diotAccounts = new ArrayList<>();
        
        for (String accountCode : masConfigDiotAccountCodes) {
            diotAccounts.add(new SDiotAccount(accountCode, true));
        }
        
        diotAccounts.add(new SDiotAccount("", false)); // special element to trigger search in non configuration parameter accounts
        
        for (SDiotAccount diotAccount : diotAccounts) {
            String sql = "";
            String section = "";
            double subtotalCalcAcum = 0;
            double subtotalCalcAcumDiffPos = 0; // positive
            double subtotalCalcAcumDiffNeg = 0; // negative
            
            if (diotAccount.IsConfigParamAccount) {
                // prepare query to scan accounts, one at a time, set up by configuration:
                
                sql = "SELECT re.id_year, re.id_per, re.id_bkc, re.id_tp_rec, re.id_num, re.sort_pos, re.id_ety, "
                        + "re.debit, re.credit, re.fid_acc, re.fid_tax_bas_n, re.fid_tax_n, "
                        + "re.fid_dps_year_n, re.fid_dps_doc_n, re.fid_cfd_n, re.fid_bp_nr, re.occ_fiscal_id, re.usr_id, re.fid_bkk_year_n, re.fid_bkk_num_n "
                        + "FROM fin_rec AS r "
                        + "INNER JOIN fin_rec_ety AS re ON r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num "
                        + (maRequredFiscalIds.isEmpty() ? "" : "LEFT OUTER JOIN erp.bpsu_bp AS b ON b.id_bp = re.fid_bp_nr ")
                        + "WHERE NOT r.b_del AND NOT re.b_del "
                        + "AND r.id_year = " + year + " AND r.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(mtStart) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(mtEnd) + "' "
                        + "AND re.fid_acc LIKE '" + diotAccount.AccountCode + "%' "

                /////  TESTING SOURCE CODE BLOCK - START ///////////////////////

                //        + "AND re.fid_bp_nr IN (1570, 6700) "
                //        + "AND re.occ_fiscal_id = 'XEXX010101000' "

                /////  TESTING SOURCE CODE BLOCK - END /////////////////////////

                        + (maRequredFiscalIds.isEmpty() ? "" : "AND (re.occ_fiscal_id IN (" + requiredFiscalIds + ") OR b.fiscal_id IN (" + requiredFiscalIds + ")) ")
                        + "ORDER BY re.fid_acc, re.id_year, re.id_per, re.id_bkc, re.id_tp_rec, re.id_num, re.sort_pos, re.id_ety;"; // same ordering for both configured and not configured accounts
                
                System.out.println();
                System.out.println(SLibUtils.textRepeat("=", 80));
                System.out.println("*** " + (section = "CUENTA CONTABLE CONFIGURADA PARA LA DIOT: '" + diotAccount.AccountCode + "' (" + (++accounts + " de " + masConfigDiotAccountCodes.length) + ")") + " ***");
                System.out.println(SLibUtils.textRepeat("=", 80));
            }
            else {
                // prepare filter for configured DIOT accounts:

                String sqlDiotAccounts = "";

                for (String accountCode : masConfigDiotAccountCodes) {
                    sqlDiotAccounts += (!sqlDiotAccounts.isEmpty() ? " OR " : "") + "re.fid_acc LIKE '" + accountCode + "%'";
                }
                
                // prepare query to scan other purchases and expenses accounts with explicitly tax input:
                
                sql = "SELECT re.id_year, re.id_per, re.id_bkc, re.id_tp_rec, re.id_num, re.sort_pos, re.id_ety, "
                        + "re.debit, re.credit, re.fid_acc, re.fid_tax_bas_n, re.fid_tax_n, "
                        + "re.fid_dps_year_n, re.fid_dps_doc_n, re.fid_cfd_n, re.fid_bp_nr, re.occ_fiscal_id, re.usr_id, re.fid_bkk_year_n, re.fid_bkk_num_n "
                        + "FROM fin_rec AS r "
                        + "INNER JOIN fin_rec_ety AS re ON r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num "
                        + "INNER JOIN fin_acc AS a ON re.fid_acc = a.id_acc "
                        + "INNER JOIN fin_acc AS al ON f_acc_std_ldg(a.code) = al.code "
                        + (maRequredFiscalIds.isEmpty() ? "" : "LEFT OUTER JOIN erp.bpsu_bp AS b ON b.id_bp = re.fid_bp_nr ")
                        + "WHERE NOT r.b_del AND NOT re.b_del "
                        + "AND r.id_year = " + year + " AND r.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(mtStart) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(mtEnd) + "' "
                        + "AND NOT (" + sqlDiotAccounts + ") AND re.fid_tax_bas_n = " + manConfigDefaultVatKey[0] + " AND (" // only VAT entries
                        + "(re.occ_fiscal_id <> '') OR "
                        + "(al.fid_tp_acc_sys IN (" + SDataConstantsSys.FINS_TP_ACC_SYS_PUR + ", " + SDataConstantsSys.FINS_TP_ACC_SYS_PUR_ADJ + "))) "

                /////  TESTING SOURCE CODE BLOCK - START ///////////////////////

                //        + "AND re.fid_bp_nr IN (1570, 6700) "
                //        + "AND re.occ_fiscal_id = 'XEXX010101000' "

                /////  TESTING SOURCE CODE BLOCK - END /////////////////////////

                        + (maRequredFiscalIds.isEmpty() ? "" : "AND (re.occ_fiscal_id IN (" + requiredFiscalIds + ") OR b.fiscal_id IN (" + requiredFiscalIds + ")) ")
                        + "ORDER BY re.fid_acc, re.id_year, re.id_per, re.id_bkc, re.id_tp_rec, re.id_num, re.sort_pos, re.id_ety;"; // same ordering for both configured and not configured accounts
                
                System.out.println();
                System.out.println(SLibUtils.textRepeat("=", 80));
                System.out.println("*** " + (section = "MOVIMIENTOS CONTABLES ADICIONALES PARA LA DIOT") + " ***");
                System.out.println(SLibUtils.textRepeat("=", 80));
            }
            
            try (ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    /*
                     * 1.1. Count total journal voucher entries processed.
                     */

                    entries++;
                    
                    /*
                     * 1.2. Start processing of current journal voucher entry.
                     */

                    double entryDebit = resultSet.getDouble("re.debit");
                    double entryCredit = resultSet.getDouble("re.credit");
                    double vatAmount = SLibUtils.roundAmount(Math.abs(entryDebit + entryCredit)); // VAT amount
                    String occasionalFiscalId = resultSet.getString("re.occ_fiscal_id");
                    ArrayList<DiotEntry> diotEntries = new ArrayList<>();

                    /*
                     * 1.3. Get account and ledger account of current journal voucher entry.
                     */

                    SDataAccount account = getAccount(resultSet.getString("re.fid_acc"));

                    if (account == null) {
                        account = (SDataAccount) SDataUtilities.readRegistry(miClient, SDataConstants.FIN_ACC, new Object[] { resultSet.getString("re.fid_acc") }, SLibConstants.EXEC_MODE_VERBOSE);
                        addAccount(account);
                    }

                    SDataAccount ledgerAccount = getLedgerAccount(account.getDbmsPkLedgerAccountIdXXX());

                    if (ledgerAccount == null) {
                        if (account.getLevel() == 1) {
                            ledgerAccount = account; // input account is a ledger account:
                        }
                        else {
                            ledgerAccount = (SDataAccount) SDataUtilities.readRegistry(miClient, SDataConstants.FIN_ACC, new Object[] { account.getDbmsPkLedgerAccountIdXXX() }, SLibConstants.EXEC_MODE_VERBOSE);
                        }

                        addLedgerAccount(ledgerAccount);
                    }

                    /*
                     * 1.4. Identify the typoe of VAT of current journal voucher entry.
                     */

                    int[] vatKey = null;

                    if (resultSet.getInt("re.fid_tax_bas_n") != 0 && resultSet.getInt("re.fid_tax_n") != 0) {
                        vatKey = new int[] { resultSet.getInt("re.fid_tax_bas_n"), resultSet.getInt("re.fid_tax_n") };
                    }
                    else {
                        entriesWithoutVat++;
                        vatKey = manConfigDefaultVatKey;
                    }

                    SDataTax vat = getVat(vatKey);

                    if (vat == null) {
                        vat = (SDataTax) SDataUtilities.readRegistry(miClient, SDataConstants.FINU_TAX, vatKey, SLibConstants.EXEC_MODE_VERBOSE);
                        addVat(vat);
                    }

                    /*
                     * 1.5. Identify the third parties (known in DIOT layout as "el tercero") of current journal voucher entry.
                     */

                    SDataBizPartner entryBizPartner = null;
                    boolean isEntryBizPartnerFromDps = false;
                    
                    // 1.5.1. Get DPS, if any, and get business partners indirectly from this DPS:

                    SDataDps dps = null;

                    if (resultSet.getInt("re.fid_dps_year_n") != 0 && resultSet.getInt("re.fid_dps_doc_n") != 0) {
                        dps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, new int[] { resultSet.getInt("re.fid_dps_year_n"), resultSet.getInt("re.fid_dps_doc_n") }, SLibConstants.EXEC_MODE_VERBOSE);

                        SDataBizPartner dpsBizPartner = getBizPartner(dps.getFkBizPartnerId_r());

                        if (dpsBizPartner == null) {
                            dpsBizPartner = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, new int[] { dps.getFkBizPartnerId_r() }, SLibConstants.EXEC_MODE_VERBOSE);
                            addBizPartner(dpsBizPartner);
                        }

                        // check if are there special settings (i.e., tax regions to be ignored or third taxpayers):

                        double dpsSubtotal = 0;
                        double dpsVatAmount = 0;
                        ArrayList<DpsVatSetting> dpsVatSettings = new ArrayList<>();

                        sql = "SELECT de.fid_tax_reg, de.fid_third_tax_n, SUM(de.stot_r) AS _sum_stot, SUM(det.tax) AS _sum_tax "
                                + "FROM trn_dps_ety AS de "
                                + "INNER JOIN trn_dps_ety_tax AS det ON det.id_year = de.id_year AND det.id_doc = de.id_doc AND det.id_ety = de.id_ety "
                                + "WHERE NOT de.b_del "
                                + "AND de.id_year = " + dps.getPkYearId() + " AND de.id_doc = " + dps.getPkDocId() + " "
                                + "AND det.id_tax_bas = " + vatKey[0] + " AND det.id_tax = " + vatKey[1] + " "
                                + "GROUP BY de.fid_tax_reg, de.fid_third_tax_n "
                                + "ORDER BY de.fid_tax_reg, de.fid_third_tax_n;";

                        try (ResultSet resultSetAux = statementAux.executeQuery(sql)) {
                            while (resultSetAux.next()) {
                                dpsVatSettings.add(new DpsVatSetting(
                                        resultSetAux.getInt("de.fid_tax_reg"),
                                        resultSetAux.getInt("de.fid_third_tax_n"),
                                        resultSetAux.getDouble("_sum_stot"),
                                        resultSetAux.getDouble("_sum_tax")));

                                dpsSubtotal = SLibUtils.roundAmount(dpsSubtotal + resultSetAux.getDouble("_sum_stot"));
                                dpsVatAmount = SLibUtils.roundAmount(dpsVatAmount + resultSetAux.getDouble("_sum_tax"));
                            }
                        }
                        
                        if (dpsSubtotal == 0) {
                            warning = "" + entries + ".- " + composeCustomRecordEntry(resultSet, account, dpsBizPartner, "") + ":\n"
                                    + " En el documento '" + dps.getDpsNumber() + "' la totalidad del importe gravado con IVA '" + vat.getTax() + "', tipo IVA '" + vat.getVatType() + "', será excluido de la DIOT porque es cero:\n"
                                    + " - subtotal: $" + SLibUtils.getDecimalFormatAmount().format(dpsSubtotal) + ";\n"
                                    + " - importe IVA: $" + SLibUtils.getDecimalFormatAmount().format(dpsVatAmount) + ".";
                            warnings += "\"" + warning.replaceAll("\n", " ") + "\"\n";
                            System.out.println(warning);
                            
                            if (collectDetailedInfo) {
                                layoutSkippedRecordEntries.add(warning.replaceAll("\n", " "));
                            }
                            
                            continue; // skip current journal voucher entry, must be ignored!
                        }

                        // process tax regions to be ignored, when applicable:
                        
                        double dpsSubtotalIgnored = 0;

                        if (manConfigTaxRegionsIgnored != null && manConfigTaxRegionsIgnored.length > 0) {
                            for (int taxRegionIgnored : manConfigTaxRegionsIgnored) {
                                for (DpsVatSetting dpsVatSetting : dpsVatSettings) {
                                    if (dpsVatSetting.TaxRegionId == taxRegionIgnored) {
                                        // sum subtotal to be ignored:
                                        
                                        dpsVatSetting.IsIgnored = true;
                                        dpsSubtotalIgnored = SLibUtils.roundAmount(dpsSubtotalIgnored + dpsVatSetting.Subtotal);
                                    }
                                }
                            }

                            if (SLibUtils.compareAmount(dpsSubtotal, dpsSubtotalIgnored)) {
                                warning = "" + entries + ".- " + composeCustomRecordEntry(resultSet, account, dpsBizPartner, "") + ":\n"
                                        + " En el documento '" + dps.getDpsNumber() + "' la totalidad del importe gravado con IVA '" + vat.getTax() + "', tipo IVA '" + vat.getVatType() + "', será excluido de la DIOT por su(s) región(es) de impuestos:\n"
                                        + " - subtotal: $" + SLibUtils.getDecimalFormatAmount().format(dpsSubtotal) + ";\n"
                                        + " - importe IVA: $" + SLibUtils.getDecimalFormatAmount().format(dpsVatAmount) + ".";
                                warnings += "\"" + warning.replaceAll("\n", " ") + "\"\n";
                                System.out.println(warning);
                                
                                if (collectDetailedInfo) {
                                    layoutSkippedRecordEntries.add(warning.replaceAll("\n", " "));
                                }
                                
                                continue; // skip current journal voucher entry, must be ignored!
                            }
                        }

                        // process third taxpayers, when applicable:
                        
                        double dpsSubtotalThirdTaxpayers = 0;

                        for (DpsVatSetting dpsVatSetting : dpsVatSettings) {
                            if (!dpsVatSetting.IsIgnored) {
                                if (dpsVatSetting.ThirdTaxpayerId != 0 && dpsVatSetting.ThirdTaxpayerId != dpsBizPartner.getPkBizPartnerId()) {
                                    // sum subtotal of third parties:
                                    
                                    dpsVatSetting.IsThirdTaxpayer = true;
                                    dpsSubtotalThirdTaxpayers = SLibUtils.roundAmount(dpsSubtotalThirdTaxpayers + dpsVatSetting.Subtotal);
                                    
                                    // process current third taxpayer:
                                    
                                    SDataBizPartner thirdTaxpayer = getBizPartner(dpsVatSetting.ThirdTaxpayerId);

                                    if (thirdTaxpayer == null) {
                                        thirdTaxpayer = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, new int[] { dpsVatSetting.ThirdTaxpayerId }, SLibConstants.EXEC_MODE_VERBOSE);
                                        addBizPartner(thirdTaxpayer);
                                    }

                                    double thirdTaxpayerRatio = 0;
                                    boolean isFixedVatThirdTaxpayer = false;

                                    if (entryDebit == 0 && entryCredit == 0) {
                                        // third-taxpayer ratio based on DPS subtotal:

                                        if (dpsVatSetting.Subtotal > 0 && dpsSubtotal > 0) {
                                            thirdTaxpayerRatio = dpsVatSetting.Subtotal / dpsSubtotal;
                                        }
                                    }
                                    else {
                                        // third-taxpayer ratio based on DPS VAT amount:

                                        if (dpsVatSetting.Tax > 0 && dpsVatAmount > 0) {
                                            thirdTaxpayerRatio = dpsVatSetting.Tax / dpsVatAmount;
                                        }

                                        isFixedVatThirdTaxpayer = SLibUtils.compareAmount(dpsVatSetting.Subtotal, FIXED_VAT_THIRD_TAXPAYER) && dpsVatSetting.Tax > 0 && 
                                                !vat.getVatType().equals(SDiotConsts.VAT_TYPE_EXEMPT) && !vat.getVatType().equals(SDiotConsts.VAT_TYPE_RATE_0);
                                    }
                                    
                                    DiotEntry diotEntry = new DiotEntry(createCustomRecordEntryKey(resultSet), thirdTaxpayer, "", account, vat, 
                                            SLibUtils.roundAmount(entryDebit * thirdTaxpayerRatio), SLibUtils.roundAmount(entryCredit * thirdTaxpayerRatio), true, isFixedVatThirdTaxpayer, thirdTaxpayerRatio);
                                    diotEntry.XtaDpsNumber = dps.getDpsNumber();
                                    
                                    diotEntries.add(diotEntry);
                                }
                            }
                        }
                        
                        // process third party of DPS:
                        
                        double debits = 0;
                        double credits = 0;
                        
                        for (DiotEntry diotEntry : diotEntries) {
                            debits = SLibUtils.roundAmount(debits + diotEntry.Debit);
                            credits = SLibUtils.roundAmount(credits + diotEntry.Credit);
                        }
                        
                        if (diotEntries.isEmpty() || (Math.abs(debits) < Math.abs(entryDebit) || Math.abs(credits) < Math.abs(entryCredit))) {
                            DiotEntry diotEntry = new DiotEntry(createCustomRecordEntryKey(resultSet), dpsBizPartner, "", account, vat, 
                                SLibUtils.roundAmount(entryDebit - debits), SLibUtils.roundAmount(entryCredit - credits), false, false, 0);
                            diotEntry.XtaDpsNumber = dps.getDpsNumber();
                            
                            diotEntries.add(diotEntry);
                        }
                        
                        entryBizPartner = dpsBizPartner; // business partner from DPS taken as the very best, even if there are other third parties
                        isEntryBizPartnerFromDps = true;
                    }

                    // 1.5.2. Check if there is a CFD, and get business partner (factoring bank) indirectly from this CFD, if available:

                    String cfdUuid = "";
                    
                    if (entryBizPartner == null && resultSet.getInt("re.fid_cfd_n") != 0) {
                        sql = "SELECT fid_fact_bank_n, uuid "
                                + "FROM trn_cfd "
                                + "WHERE id_cfd = " + resultSet.getInt("re.fid_cfd_n") + ";";
                        
                        try (ResultSet resultSetAux = statementAux.executeQuery(sql)) {
                            if (resultSetAux.next() && resultSetAux.getInt("fid_fact_bank_n") != 0) {
                                cfdUuid = resultSetAux.getString("uuid");
                                entryBizPartner = getBizPartner(resultSetAux.getInt("fid_fact_bank_n"));

                                if (entryBizPartner == null) {
                                    entryBizPartner = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, new int[] { resultSetAux.getInt("fid_fact_bank_n") }, SLibConstants.EXEC_MODE_VERBOSE);
                                    addBizPartner(entryBizPartner);
                                }
                            }
                        }
                    }
                    
                    // 1.5.3. Get business partner directly from the current journal voucher entry, if available:
                    

                    if (entryBizPartner == null && resultSet.getInt("re.fid_bp_nr") != 0) {
                        entryBizPartner = getBizPartner(resultSet.getInt("re.fid_bp_nr"));

                        if (entryBizPartner == null) {
                            entryBizPartner = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, new int[] { resultSet.getInt("re.fid_bp_nr") }, SLibConstants.EXEC_MODE_VERBOSE);
                            addBizPartner(entryBizPartner);
                        }
                    }

                    if (!isEntryBizPartnerFromDps) {
                        DiotEntry diotEntry = new DiotEntry(createCustomRecordEntryKey(resultSet), entryBizPartner, occasionalFiscalId, account, vat, 
                                entryDebit, entryCredit, false, false, 0);
                        diotEntry.XtaCfdUuid = cfdUuid;
                        
                        diotEntries.add(diotEntry);
                    }
                    
                    /*
                     * 1.6. Sum amount of current DIOT transcation.
                     */

                    totalDebit = SLibUtils.roundAmount(totalDebit + entryDebit);
                    totalCredit = SLibUtils.roundAmount(totalCredit + entryCredit);

                    // add to account totals:

                    AccountTotal accountTotal = accountTotalsMap.get(account.getPkAccountIdXXX());

                    if (accountTotal == null) {
                        accountTotal = new AccountTotal(entryDebit, entryCredit);
                        accountTotalsMap.put(account.getPkAccountIdXXX(), accountTotal);
                    }
                    else {
                        accountTotal.add(entryDebit, entryCredit);
                    }

                    boolean isAccountForDebits = SLibUtils.belongsTo(ledgerAccount.getAccountClassKey(), new int[][] { SDataConstantsSys.FINS_CL_ACC_ASSET, SDataConstantsSys.FINS_CL_ACC_RES_DBT });
                    boolean isAccountForCredits = SLibUtils.belongsTo(ledgerAccount.getAccountClassKey(), new int[][] { SDataConstantsSys.FINS_CL_ACC_LIABTY, SDataConstantsSys.FINS_CL_ACC_RES_CDT });
                    
                    /*
                     * 1.7. Proces DIOT entries of current journal voucher entry.
                     */
                    
                    if (collectDetailedInfo) {
                        layoutDiotEntries.addAll(diotEntries); // preserve all DIOT entries
                    }
                    
                    for (DiotEntry diotEntry : diotEntries) {
                        double debit = diotEntry.Debit;
                        double credit = diotEntry.Credit;
                        SDataBizPartner bizPartner = diotEntry.BizPartner;
                        
                        // process DIOT Tercero current business partner:

                        String terceroClave;

                        if (bizPartner != null) {
                            terceroClave = bizPartner.getDiotTerceroClave(); // "standard tercero"
                            
                            if (bizPartner.isCurrentCompany(miClient)) {
                                entriesForThisCompany++;
                            }
                        }
                        else {
                            entriesWithoutCatalogBizPartner++;
                            
                            if (occasionalFiscalId.isEmpty()) {
                                terceroClave = SDiotTercero.GLOBAL_CLAVE; // "global tercero"
                            }
                            else {
                                terceroClave = SDiotTercero.composeOccasionalClave(occasionalFiscalId); // "occasional tercero"
                            }
                        }

                        SDiotTercero tercero = tercerosMap.get(terceroClave);

                        if (tercero == null) {
                            if (!occasionalFiscalId.isEmpty()) {
                                tercero = new SDiotTercero(miClient, occasionalFiscalId);
                            }
                            else {
                                tercero = new SDiotTercero(miClient, bizPartner);
                            }

                            tercerosMap.put(tercero.Clave, tercero);
                        }

                        if (isAccountForDebits) {
                            // VAT creditable:

                            if (debit > 0 || credit < 0 || (debit == 0 && credit == 0)) {
                                double paymentAmount = 0;
                                double paymentRatio = 0;
                                double txnAmount = 0;
                                SDiotAccountingTxn diotAccountingTxn = null;

                                if (diotAccount.IsConfigParamAccount) {
                                    // extract net total and VAT debits and credits:

                                    diotAccountingTxn = new SDiotAccountingTxn(
                                            statementAux, 
                                            resultSet.getInt("re.usr_id"), 
                                            createRecordKey(resultSet), 
                                            new int[] { resultSet.getInt("re.fid_bkk_year_n"), resultSet.getInt("re.fid_bkk_num_n") },
                                            dps == null ? null : (int[]) dps.getPrimaryKey()
                                    );

                                    paymentAmount = diotAccountingTxn.getPaymentAmount();
                                    paymentRatio = dps == null || dps.getTotal_r() == 0 ? 1.0 : paymentAmount / dps.getTotal_r();
                                    txnAmount = SLibUtils.roundAmount(diotAccountingTxn.getEntryDpsSubtotal(SDataConstantsSys.FINS_TP_SYS_MOV_TAX_DBT, (int[]) vat.getPrimaryKey()) * paymentRatio);
                                }
                                else {
                                    txnAmount = vatAmount;
                                }

                                // VAT creditable of payments of purchases:

                                if ((debit == 0 && credit == 0) || (!diotAccount.IsConfigParamAccount && vatAmount > 0 && SLibUtils.belongsTo(vat.getVatType(), new String[] { SDiotConsts.VAT_TYPE_EXEMPT, SDiotConsts.VAT_TYPE_RATE_0 }))) {
                                    switch (vat.getVatType()) {
                                        case SDiotConsts.VAT_TYPE_EXEMPT:
                                            if (tercero.IsDomestic) {
                                                tercero.DatPagosExentNac = SLibUtils.roundAmount(tercero.DatPagosExentNac + txnAmount);
                                            }
                                            else {
                                                tercero.DatPagosExentInt = SLibUtils.roundAmount(tercero.DatPagosExentInt + txnAmount);
                                            }
                                            break;

                                        case SDiotConsts.VAT_TYPE_RATE_0:
                                        case SDiotConsts.VAT_TYPE_GENERAL:      // VAT was manipulated to be zero
                                        case SDiotConsts.VAT_TYPE_BORDER_N:     // VAT was manipulated to be zero
                                        case SDiotConsts.VAT_TYPE_BORDER_S:     // VAT was manipulated to be zero
                                            tercero.DatPagosTasa0Nac = SLibUtils.roundAmount(tercero.DatPagosTasa0Nac + txnAmount);
                                            break;
                                            
                                        case SDiotConsts.VAT_TYPE_BORDER:       // VAT was manipulated to be zero
                                        case SDiotConsts.VAT_TYPE_BORDER_NORTH: // VAT was manipulated to be zero
                                            entriesVatZeroObsolete++;

                                            warning = "" + entries + ".- " + composeCustomRecordEntry(resultSet, account, bizPartner, occasionalFiscalId) + ":\n"
                                                    + " TIPO IVA OBSOLETO EN '" + vat.getTax() + "', tipo IVA '" + vat.getVatType() + "':\n"
                                                    + " - importe IVA: $" + SLibUtils.getDecimalFormatAmount().format(vatAmount) + ";\n"
                                                    + " - total neto real: $" + SLibUtils.getDecimalFormatAmount().format(paymentAmount) + ".";
                                            warnings += "\"" + warning.replaceAll("\n", " ") + "\"\n";
                                            System.out.println(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION + "\n" + warning);
                                            break;

                                        default:
                                            entriesVatZeroUndefined++;

                                            warning = "" + entries + ".- " + composeCustomRecordEntry(resultSet, account, bizPartner, occasionalFiscalId) + ":\n"
                                                    + " TIPO IVA DESCONOCIDO EN '" + vat.getTax() + "', tipo IVA '" + vat.getVatType() + "':\n"
                                                    + " - importe IVA: $" + SLibUtils.getDecimalFormatAmount().format(vatAmount) + ";\n"
                                                    + " - total neto real: $" + SLibUtils.getDecimalFormatAmount().format(paymentAmount) + ".";
                                            warnings += "\"" + warning.replaceAll("\n", " ") + "\"\n";
                                            System.out.println(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION + "\n" + warning);
                                    }
                                }
                                else {
                                    // VAT corresponds to supplier:

                                    double subtotalCalc = SLibUtils.roundAmount(vat.getPercentage() == 0 ? 0 : vatAmount / vat.getPercentage());
                                    double subtotalCalcDiff = SLibUtils.roundAmount(txnAmount - subtotalCalc);

                                    if (subtotalCalcDiff < -AMOUNT_DIFF_ALLOWANCE) {
                                        // suspicious situation: calculated net-total is greater than the real one:

                                        subtotalCalcAcumDiffNeg = SLibUtils.roundAmount(subtotalCalcAcumDiffNeg + subtotalCalcDiff);

                                        warning = "" + entries + ".- " + composeCustomRecordEntry(resultSet, account, bizPartner, occasionalFiscalId) + ":\n"
                                                + " Discrepancia NO corregible en impuesto '" + vat.getTax() + "', tipo IVA '" + vat.getVatType() + "':\n"
                                                + " - importe IVA: $" + SLibUtils.getDecimalFormatAmount().format(vatAmount) + ";\n"
                                                + " - total neto real: $" + SLibUtils.getDecimalFormatAmount().format(paymentAmount) + ";\n"
                                                + " - subtotal real: $" + SLibUtils.getDecimalFormatAmount().format(txnAmount) + " < "
                                                + "subtotal calculado: $" + SLibUtils.getDecimalFormatAmount().format(subtotalCalc) + " = "
                                                + "diferencia: $" + SLibUtils.getDecimalFormatAmount().format(subtotalCalcDiff) + "."
                                                + (diotEntry.IsFixedVatThirdTaxpayer ? " Puede tratarse meramente del IVA de un tercero causante." : "");
                                        warnings += "\"" + warning.replaceAll("\n", " ") + "\"\n";
                                        System.out.println(warning);
                                    }
                                    else if (subtotalCalcDiff > AMOUNT_DIFF_ALLOWANCE) {
                                        // manageable situation: calculated net-total is less than the real one:

                                        subtotalCalcAcumDiffPos = SLibUtils.roundAmount(subtotalCalcAcumDiffPos + subtotalCalcDiff);

                                        warning = "" + entries + ".- " + composeCustomRecordEntry(resultSet, account, bizPartner, occasionalFiscalId) + ":\n"
                                                + " Discrepancia SÍ corregible en impuesto '" + vat.getTax() + "', tipo IVA '" + vat.getVatType() + "':\n"
                                                + " - importe IVA: $" + SLibUtils.getDecimalFormatAmount().format(vatAmount) + ";\n"
                                                + " - total neto real: $" + SLibUtils.getDecimalFormatAmount().format(paymentAmount) + ";\n"
                                                + "subtotal real: $" + SLibUtils.getDecimalFormatAmount().format(txnAmount) + " > "
                                                + "subtotal calculado: $" + SLibUtils.getDecimalFormatAmount().format(subtotalCalc) + " = "
                                                + "diferencia: $" + SLibUtils.getDecimalFormatAmount().format(subtotalCalcDiff) + ".";
                                        warnings += "\"" + warning.replaceAll("\n", " ") + "\"\n";
                                        System.out.println(warning);

                                        if (tercero.IsDomestic) {
                                            tercero.DatPagosExentNac = SLibUtils.roundAmount(tercero.DatPagosExentNac + subtotalCalcDiff);
                                        }
                                        else {
                                            tercero.DatPagosExentInt = SLibUtils.roundAmount(tercero.DatPagosExentInt + subtotalCalcDiff);
                                        }
                                    }
                                    else if (subtotalCalcDiff != 0) {
                                        // adjust calculated subtotal due to difference between real and calculated net-total:
                                        subtotalCalc = SLibUtils.roundAmount(subtotalCalc + subtotalCalcDiff);
                                    }

                                    subtotalCalcAcum = SLibUtils.roundAmount(subtotalCalcAcum + subtotalCalc);
                                    System.out.println("debit: " + SLibUtils.getDecimalFormatAmount().format(debit) + "; "
                                            + "subtotal calc.: " + SLibUtils.getDecimalFormatAmount().format(subtotalCalc) + "; "
                                            + "subtotal calc. acum.: " + SLibUtils.getDecimalFormatAmount().format(subtotalCalcAcum));

                                    switch (vat.getVatType()) {
                                        case SDiotConsts.VAT_TYPE_GENERAL:
                                            if (tercero.IsDomestic) {
                                                tercero.ValTasaGralNacPagos = SLibUtils.roundAmount(tercero.ValTasaGralNacPagos + subtotalCalc);
                                            }
                                            else {
                                                tercero.ValTasaGralIntProdsPagos = SLibUtils.roundAmount(tercero.ValTasaGralIntProdsPagos + subtotalCalc);
                                            }
                                            break;

                                        case SDiotConsts.VAT_TYPE_BORDER_N:
                                            tercero.ValFrontNtePagos = SLibUtils.roundAmount(tercero.ValFrontNtePagos + subtotalCalc);
                                            break;

                                        case SDiotConsts.VAT_TYPE_BORDER_S:
                                            tercero.ValFrontSurPagos = SLibUtils.roundAmount(tercero.ValFrontSurPagos + subtotalCalc);
                                            break;

                                        case SDiotConsts.VAT_TYPE_BORDER:
                                        case SDiotConsts.VAT_TYPE_BORDER_NORTH:
                                            entriesVatNonZeroObsolete++;

                                            warning = "" + entries + ".- " + composeCustomRecordEntry(resultSet, account, bizPartner, occasionalFiscalId) + ":\n"
                                                    + " TIPO IVA OBSOLETO EN '" + vat.getTax() + "', tipo IVA '" + vat.getVatType() + "':\n"
                                                    + " - importe IVA: $" + SLibUtils.getDecimalFormatAmount().format(vatAmount) + ";\n"
                                                    + " - total neto real: $" + SLibUtils.getDecimalFormatAmount().format(paymentAmount) + ".";
                                            warnings += "\"" + warning.replaceAll("\n", " ") + "\"\n";
                                            System.out.println(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION + "\n" + warning);
                                            break;

                                        default:
                                            entriesVatNonZeroUndefined++;

                                            warning = "" + entries + ".- " + composeCustomRecordEntry(resultSet, account, bizPartner, occasionalFiscalId) + ":\n"
                                                    + " TIPO IVA DESCONOCIDO EN '" + vat.getTax() + "', tipo IVA '" + vat.getVatType() + "':\n"
                                                    + " - importe IVA: $" + SLibUtils.getDecimalFormatAmount().format(vatAmount) + ";\n"
                                                    + " - total neto real: $" + SLibUtils.getDecimalFormatAmount().format(paymentAmount) + ".";
                                            warnings += "\"" + warning.replaceAll("\n", " ") + "\"\n";
                                            System.out.println(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION + "\n" + warning);
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
                                    
                                    double subtotalCalc = SLibUtils.roundAmount(vat.getPercentage() == 0 ? 0 : vatAmount / vat.getPercentage());
                                    
                                    switch (vat.getVatType()) {
                                        case SDiotConsts.VAT_TYPE_GENERAL:
                                            if (tercero.IsDomestic) {
                                                tercero.ValTasaGralNacReembs = SLibUtils.roundAmount(tercero.ValTasaGralNacReembs + subtotalCalc);
                                            }
                                            else {
                                                tercero.ValTasaGralIntProdsReembs = SLibUtils.roundAmount(tercero.ValTasaGralIntProdsReembs + subtotalCalc);
                                            }
                                            break;

                                        case SDiotConsts.VAT_TYPE_BORDER_N:
                                            tercero.ValFrontNteReembs = SLibUtils.roundAmount(tercero.ValFrontNteReembs + subtotalCalc);
                                            break;

                                        case SDiotConsts.VAT_TYPE_BORDER_S:
                                            tercero.ValFrontSurReembs = SLibUtils.roundAmount(tercero.ValFrontSurReembs + subtotalCalc);
                                            break;

                                        case SDiotConsts.VAT_TYPE_BORDER:
                                        case SDiotConsts.VAT_TYPE_BORDER_NORTH:
                                            entriesVatAdjustsNonZeroObsolete++;

                                            warning = "" + entries + ".- " + composeCustomRecordEntry(resultSet, account, bizPartner, occasionalFiscalId) + ":\n"
                                                    + " TIPO IVA OBSOLETO EN AJUSTES EN '" + vat.getTax() + "', tipo IVA '" + vat.getVatType() + "':\n"
                                                    + " - importe IVA de ajustes: $" + SLibUtils.getDecimalFormatAmount().format(vatAmount) + ".";
                                            warnings += "\"" + warning.replaceAll("\n", " ") + "\"\n";
                                            System.out.println(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION + "\n" + warning);
                                            break;

                                        default:
                                            entriesVatAdjustsNonZeroUndefined++;

                                            warning = "" + entries + ".- " + composeCustomRecordEntry(resultSet, account, bizPartner, occasionalFiscalId) + ":\n"
                                                    + " TIPO IVA DESCONOCIDO EN AJUSTES EN '" + vat.getTax() + "', tipo IVA '" + vat.getVatType() + "':\n"
                                                    + " - importe IVA de ajustes: $" + SLibUtils.getDecimalFormatAmount().format(vatAmount) + ".";
                                            warnings += "\"" + warning.replaceAll("\n", " ") + "\"\n";
                                            System.out.println(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION + "\n" + warning);
                                    }
                                }
                            }
                        }
                        else if (isAccountForCredits) {
                            // VAT creditable withheld:

                            if (debit < 0 || credit > 0 || (debit == 0 && credit == 0)) {
                                // VAT creditable withheld of payments of purchases:

                                tercero.DatIvaRetenido = SLibUtils.roundAmount(tercero.DatIvaRetenido + vatAmount);

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

                                    tercero.DatIvaRetenido = SLibUtils.roundAmount(tercero.DatIvaRetenido - vatAmount);

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
                            throw new Exception("" + entries + ".- " + composeCustomRecordEntry(resultSet, account, bizPartner, occasionalFiscalId) + ":\n"
                                    + " No pudo determinarse si la cuenta contable de mayor " + ledgerAccount.getCode() + ", '" + ledgerAccount.getAccount() + "', es de débito o de crédito.");
                        }
                    }
                }
            }
            
            // print out subtotals for current account:
            
            SDiotTercero terceroSubtotal = new SDiotTercero(); // "empty" tercero
            
            for (SDiotTercero tercero : tercerosMap.values()) {
                terceroSubtotal.addTercero(tercero);
            }
            
            System.out.println(SLibUtils.textRepeat("-", 80));
            System.out.println("RESUMEN: " + section);
            System.out.println("SUBTOTALES:");
            System.out.println(terceroSubtotal.toString());
            System.out.println("Diferencia total (-) en cálculo de subtotal: " + SLibUtils.getDecimalFormatAmount().format(subtotalCalcAcumDiffNeg));
            System.out.println("Diferencia total (+) en cálculo de subtotal: " + SLibUtils.getDecimalFormatAmount().format(subtotalCalcAcumDiffPos));
            
            for (String account : accountTotalsMap.keySet()) {
                AccountTotal accountTotal = accountTotalsMap.get(account);
                System.out.println("Cuenta: " + account + "; "
                        + "debe: " + SLibUtils.getDecimalFormatAmount().format(accountTotal.Debits) + "; "
                        + "haber: " + SLibUtils.getDecimalFormatAmount().format(accountTotal.Credits) + "; "
                        + "saldo: " + SLibUtils.getDecimalFormatAmount().format(accountTotal.Debits - accountTotal.Credits));
            }
            
            System.out.println(SLibUtils.textRepeat("-", 80));
        }
        
        /*
         * 2. Compute DIOT layout.
         */

        // prepare DIOT layout third parties:
        
        ArrayList<SDiotTercero> terceros = new ArrayList<>();
        
        if (maRequredFiscalIds.isEmpty()) {
            // processing all fiscal ID, may be duplicated ones:
            HashMap<String, SDiotTercero> duplicatedTercerosMap = new HashMap<>();

            for (SDiotTercero tercero : tercerosMap.values()) {
                if (!moDuplicatedOccasionalFiscalIdsMap.contains(tercero.TerRfc)) {
                    terceros.add(tercero);
                }
                else {
                    if (!duplicatedTercerosMap.containsKey(tercero.getComparableKey())) {
                        duplicatedTercerosMap.put(tercero.getComparableKey(), tercero);
                    }
                    else {
                        SDiotTercero duplicatedTercero = duplicatedTercerosMap.get(tercero.getComparableKey());
                        duplicatedTercero.addTercero(tercero);
                    }
                }
            }

            if (!duplicatedTercerosMap.isEmpty()) {
                terceros.addAll(duplicatedTercerosMap.values());
            }
        }
        else {
            // processing only required fiscal ID, cannot be duplicated ones:
            terceros.addAll(tercerosMap.values());
        }
        
        Collections.sort(terceros);
        
        // generate layout:

        String layout = "";
        int tercerosTotallyZero = 0;
        SDiotTercero terceroGlobal = null;
        SDiotTercero terceroTotal = null;
        
        if (format == SDiotConsts.FORMAT_CSV) {
            terceroTotal = new SDiotTercero();
            
            layout += "\"" + miClient.getSessionXXX().getCurrentCompanyName() + "\"\n";
            layout += "\"DIOT (del " + SLibUtils.DateFormatDate.format(mtStart) + " al " + SLibUtils.DateFormatDate.format(mtEnd) + ")\"\n";
            layout += requiredFiscalIds.isEmpty() ? "" : ("\"RFC filtrados: " + requiredFiscalIds.replaceAll("'", "") + "\"\n");
            layout += SDiotTercero.getLayoutCsvHeadings() + "\n";
        }
        
        // third parties:
        
        for (SDiotTercero tercero : terceros) {
            if (format == SDiotConsts.FORMAT_CSV) {
                terceroTotal.addTercero(tercero);
            }
            
            if (tercero.isTotallyZero()) {
                tercerosTotallyZero++;
            }
            
            if (tercero.IsGlobal) {
                if (terceroGlobal == null) {
                    terceroGlobal = tercero;
                }
                else {
                    throw new Exception("El tercero con RFC: '" + tercero.TerRfc + "' (" + tercero.getComparableKey() + ") no puede ser el tercero 'Global', puesto que ya existe uno.");
                }
            }
            else {
                if (!excludeTercerosTotallyZero || !tercero.isTotallyZero()) {
                    layout += tercero.getLayoutRow(format) + "\n";
                }
            }
        }
        
        // global third party:
        
        if (terceroGlobal != null && (!excludeTercerosTotallyZero || !terceroGlobal.isTotallyZero())) {
            layout += terceroGlobal.getLayoutRow(format) + "\n";
        }
        
        // layout summary:
        
        if (format == SDiotConsts.FORMAT_CSV) {
            DecimalFormat amountFormat = new DecimalFormat("#0.00");
            
            layout += "\n";
            layout += "\"TOTALES:\"\n";
            layout += terceroTotal.getLayoutRow(format) + "\n";
            
            layout += "\n";
            layout += "\"Totales por cuenta contable:\"\n";
            layout += "\"Cuenta contable\",\"Debe\",\"Haber\",\"Saldo\"\n";
            for (String account : accountTotalsMap.keySet()) {
                AccountTotal accountTotal = accountTotalsMap.get(account);
                layout += "\"'" + account + "\"," + 
                        accountTotal.Debits + "," + 
                        accountTotal.Credits + "," + 
                        (accountTotal.Debits - accountTotal.Credits) + "\n";
            }
            layout += "\"Total:\"," + 
                    totalDebit + "," + 
                    totalCredit + "," + 
                    (totalDebit - totalCredit) + "\n";
            
            SDataTax vatDefault = getVat(manConfigDefaultVatKey);
            
            layout += "\n";
            layout += "\"RESUMEN:\"\n";
            layout += "\"renglones pólizas contables procesados:\"," + entries + "\n";
            layout += "\"renglones pólizas contables sin impuesto:\"," + entriesWithoutVat + ",\"(asignadas el impuesto predeterminado para DIOT: " + vatDefault.getTax() + ")\"\n";
            layout += "\"renglones pólizas contables sin asociado de negocios de catálogo:\"," + entriesWithoutCatalogBizPartner + ",\"(asignados a " + SDiotConsts.NAME_THIRD_GLOBAL + ")\"\n";
            layout += "\"renglones pólizas contables de la empresa '" + miClient.getSessionXXX().getCurrentCompanyName() + "':\"," + entriesForThisCompany + ",\"(incluidos en este layout)\"\n";
            layout += "\"renglones pólizas contables con impuesto igual a cero, pero de tipo IVA obsoleto:\"," + entriesVatZeroObsolete + ",\"(excluidos de este layout)\"\n";
            layout += "\"renglones pólizas contables con impuesto igual a cero, pero de tipo IVA desconocido:\"," + entriesVatZeroUndefined + ",\"(excluidos de este layout)\"\n";
            layout += "\"renglones pólizas contables con impuesto distinto a cero, pero de tipo IVA obsoleto:\"," + entriesVatNonZeroObsolete + ",\"(excluidos de este layout)\"\n";
            layout += "\"renglones pólizas contables con impuesto distinto a cero, pero de tipo IVA desconocido:\"," + entriesVatNonZeroUndefined + ",\"(excluidos de este layout)\"\n";
            layout += "\"renglones pólizas contables con impuesto distinto a cero, pero de tipo IVA de ajustes obsoleto:\"," + entriesVatAdjustsNonZeroObsolete + ",\"(excluidos de este layout)\"\n";
            layout += "\"renglones pólizas contables con impuesto distinto a cero, pero de tipo IVA de ajustes desconocido:\"," + entriesVatAdjustsNonZeroUndefined + ",\"(excluidos de este layout)\"\n";
            layout += "\"terceros totalmente en cero:\"," + tercerosTotallyZero + ",\"" + (excludeTercerosTotallyZero ? "(excluídos de este layout)" : "(incluidos en este layout)") + "\"\n";

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
        
        // DIOT detailed information:
        
        String detailedInfo = "";
        
        if (collectDetailedInfo) {
            detailedInfo += "\"" + miClient.getSessionXXX().getCurrentCompanyName() + "\"\n";
            detailedInfo += "\"MOVIMIENTOS CONTABLES DE LA DIOT (del " + SLibUtils.DateFormatDate.format(mtStart) + " al " + SLibUtils.DateFormatDate.format(mtEnd) + ")\"\n";
            detailedInfo += requiredFiscalIds.isEmpty() ? "" : ("\"RFC filtrados: " + requiredFiscalIds.replaceAll("'", "") + "\"\n");
            
            if (layoutDiotEntries.isEmpty()) {
                detailedInfo += "\"(No hay movimientos contables de la DIOT.)\"\n";
            }
            else {
                Collections.sort(layoutDiotEntries);
                
                detailedInfo += layoutDiotEntries.get(0).getCsvHeading() + "\n";

                for (DiotEntry entry : layoutDiotEntries) {
                    detailedInfo += entry.getAsCsv() + "\n";
                }
            }
            
            detailedInfo += "\n";
            detailedInfo += "\"MOVIMIENTOS CONTABLES OMITIDOS DE LA DIOT:\"\n";
            
            if (layoutSkippedRecordEntries.isEmpty()) {
                detailedInfo += "\"(No hay movimientos contables omitidos de la DIOT.)\"\n";
            }
            else {
                for (String string : layoutSkippedRecordEntries) {
                    detailedInfo += "\"" + string + "\"\n";
                }
            }
        }
        
        String[] strings;
        
        if (format == SDiotConsts.FORMAT_CSV) {
            strings = new String[] { SLibUtils.textToAscii(layout), SLibUtils.textToAscii(detailedInfo) };
        }
        else {
            strings = new String[] { layout, detailedInfo };
        }
        
        return strings;
    }
    
    private class DpsVatSetting {
        
        public int TaxRegionId;
        public int ThirdTaxpayerId;
        public double Subtotal;
        public double Tax;
        
        public boolean IsIgnored;
        public boolean IsThirdTaxpayer;
        
        public DpsVatSetting(final int taxRegionId, final int thirdTaxpayerId, final double subtotal, final double tax) {
            TaxRegionId = taxRegionId;
            ThirdTaxpayerId = thirdTaxpayerId;
            Subtotal = subtotal;
            Tax = tax;
            
            IsIgnored = false;
            IsThirdTaxpayer = false;
        }
    }
    
    private class DiotEntry implements Comparable<DiotEntry> {
        
        public Object[] EntryKey; // year, period, BKC, record type, record number, entry position
        public SDataBizPartner BizPartner;
        public String OccasionalFiscalId;
        public SDataAccount Account;
        public SDataTax Vat;
        public double Debit;
        public double Credit;
        public boolean IsThirdTaxpayer;
        public boolean IsFixedVatThirdTaxpayer;
        public double ThirdTaxpayerRatio;
        
        public boolean SkipFromLayout;
        
        public String XtaDpsNumber;
        public String XtaCfdUuid;
        
        public DiotEntry(final Object[] entryKey, final SDataBizPartner bizPartner, final String occasionalFiscalId, final SDataAccount account, final SDataTax vat, 
                final double debit, final double credit, final boolean isThirdTaxpayer, final boolean isFixedVatThirdTaxpayer, final double thirdTaxpayerRatio) {
            EntryKey = entryKey;
            BizPartner = bizPartner;
            OccasionalFiscalId = occasionalFiscalId;
            Account = account;
            Vat = vat;
            Debit = debit;
            Credit = credit;
            IsThirdTaxpayer = isThirdTaxpayer;
            IsFixedVatThirdTaxpayer = isFixedVatThirdTaxpayer;
            ThirdTaxpayerRatio = thirdTaxpayerRatio;
            
            SkipFromLayout = isThisCompany(BizPartner != null ? BizPartner.getPkBizPartnerId() : 0, OccasionalFiscalId);
            
            XtaDpsNumber = "";
            XtaCfdUuid = "";
        }
        
        /**
         * Gets comparable key as a <code>String</code> of four segments, either in CSV ready or standard format:
         * 1. RFC
         * 2. Foreign fiscal ID
         * 3. Business partner's PK
         * 4. Fiscal registry entry's PK
         * @param asCsvFormat CSV ready format.
         * @return 
         */
        public String getComparableKey(final boolean asCsvFormat) {
            String comparableKey = "";
            String separator = asCsvFormat ? "," : "-";
            String quote = asCsvFormat ? "\"" : "";
            String empty = quote + quote;
            
            comparableKey += quote + Account.getPkAccountIdXXX() + quote
                    + separator;
            
            if (BizPartner != null) {
                if (BizPartner.isDomestic(miClient)) {
                    comparableKey += quote + BizPartner.getFiscalId() + quote
                            + separator + empty;
                }
                else {
                    comparableKey += quote + DCfdConsts.RFC_GEN_INT + quote
                            + separator + quote + BizPartner.getFiscalFrgId() + quote;
                }
                
                comparableKey += separator + BizPartner.getPkBizPartnerId()
                        + separator;
            }
            else if (!OccasionalFiscalId.isEmpty()) {
                comparableKey += quote + OccasionalFiscalId + quote
                        + separator + empty
                        + separator + "0"
                        + separator;
            }
            else {
                comparableKey += quote + (asCsvFormat ? "(" + SDiotConsts.NAME_THIRD_GLOBAL + ")": SDiotTercero.GLOBAL_RFC) + quote
                        + separator + empty
                        + separator + "0"
                        + separator;
            }
            
            comparableKey += quote + SLibUtils.textKey(EntryKey) + quote;
            
            return comparableKey;
        }
        
        public String getCsvHeading() {
            return
                    "\"Cuenta contable\"," +
                    "\"RFC\"," +
                    "\"ID fiscal\"," +
                    "\"ID asociado negocios\"," +
                    "\"Renglón póliza contable\"," +
                    "\"Asociado negocios\"," +
                    "\"Es tercero causante\"," +
                    "\"Impuesto\"," +
                    "\"Tipo IVA\"," +
                    "\"Debe\"," +
                    "\"Haber\"," +
                    "\"Documento\"," +
                    "\"UUID CFDI\"";
        }
        
        public String getAsCsv() {
            return
                    getComparableKey(true) + "," +
                    "\"" + (BizPartner != null ? BizPartner.getBizPartnerCommercial() : "") + "\"," +
                    "\"" + (IsThirdTaxpayer ? "1" : "0") + "\"," +
                    "\"" + Vat.getTax() + "\"," +
                    "\"" + Vat.getVatType() + "\"," +
                    Debit + "," +
                    Credit + "," +
                    "\"" + "'" + XtaDpsNumber + "\"," + // add apostroph to prevent large document numbers from being shown in scientific notation in spreadsheet
                    "\"" + XtaCfdUuid + "\"";
        }

        @Override
        public int compareTo(DiotEntry other) {
            return this.getComparableKey(false).compareTo(other.getComparableKey(false));
        }
    }
    
    private class AccountTotal {
        
        public double Debits;
        public double Credits;
        
        public AccountTotal(final double debits, final double credits) {
            Debits = debits;
            Credits = credits;
        }
        
        public void add(final double debits, final double credits) {
            Debits = SLibUtils.roundAmount(Debits + debits);
            Credits = SLibUtils.roundAmount(Credits + credits);
        }
    }
}
