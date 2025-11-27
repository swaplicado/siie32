/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mfin.utils;

import erp.data.SDataConstantsSys;
import erp.mcfg.data.SCfgUtils;
import erp.mod.SModSysConsts;
import java.io.FileWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.xml.SXmlUtils;

/**
 * Exchange Rates Handler.
 * To obtain exchange rates from RSS official sources, and register them in all company databases.
 * USD exchange rate policy will be extended to EUR exchange rate as well.
 * 
 * This class is pretended to be used in CLI context.
 * 
 * @author Sergio Flores
 */
public class SXrtsHandler {
    
    private static final String RSS_XRT_USD = "https://www.banxico.org.mx/rsscb/rss?BMXC_canal=fix&BMXC_idioma=es"; // as of nov. 2025
    private static final String RSS_XRT_EUR = "https://www.banxico.org.mx/rsscb/rss?BMXC_canal=euro&BMXC_idioma=es"; // as of nov. 2025
    private static final String LOG_FILE_NAME = "logs\\xrts_";
    private static final SimpleDateFormat FormatPeriod = new SimpleDateFormat("yyyy_MM");
    private static final SimpleDateFormat FormatDatetime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
    
    private final static int TEXT_LINE_LENGTH = 80;
    
    protected Connection miConnection;
    protected Date mtToday;
    protected ArrayList<Date> maBankNonBizDates;
    protected ArrayList<String> maErpCompanyDbs;
    protected ArrayList<String> maErpCompanyDbsXrtUsdPolicyBanxico;
    protected ArrayList<String> maErpCompanyDbsXrtUsdPolicyInformal;
    
    public SXrtsHandler(final Connection connection) throws Exception {
        miConnection = connection;
        mtToday = SLibTimeUtils.convertToDateOnly(new Date());
    }
    
    private Xrt createXrtFromRss(final int currency) throws MalformedURLException, ParserConfigurationException, SAXException, Exception {
        URL feedSource;
        
        switch (currency) {
            case SModSysConsts.CFGU_CUR_USD:
                feedSource = new URL(RSS_XRT_USD);
                break;
            case SModSysConsts.CFGU_CUR_EUR:
                feedSource = new URL(RSS_XRT_EUR);
                break;
            default:
                throw new UnsupportedOperationException(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\n(Moneda: " + currency + ".)");
        }
        
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(feedSource.openStream());
        Node nodeDate = SXmlUtils.extractElements(doc, "dc:date").item(0);
        Node nodeXrt = SXmlUtils.extractElements(doc, "cb:value").item(0);
        
        Date date = SLibUtils.IsoFormatDatetime.parse(nodeDate.getTextContent());
        double xrt = SLibUtils.parseDouble(nodeXrt.getTextContent());
        
        return new Xrt(date, xrt);
    }

    protected void readBankNonBizDates() throws Exception {
        maBankNonBizDates = new ArrayList<>();
        
        try (Statement statement = miConnection.createStatement()) {
            String sql = "SELECT nb_day "
                    + "FROM erp.finu_bank_nb_day "
                    + "WHERE NOT b_del AND nb_day >= " + "'" + SLibUtils.DbmsDateFormatDate.format(mtToday) + "'" + " "
                    + "ORDER BY nb_day, name, id_bank_nb_day;";
            ResultSet resultSet = statement.executeQuery(sql);
            
            while (resultSet.next()) {
                maBankNonBizDates.add(resultSet.getDate("nb_day"));
            }
        }
    }
    
    protected void readErpCompanyDbs() throws Exception {
        maErpCompanyDbs = new ArrayList<>();
        maErpCompanyDbsXrtUsdPolicyBanxico = new ArrayList<>();
        maErpCompanyDbsXrtUsdPolicyInformal = new ArrayList<>();
        
        try (Statement statement = miConnection.createStatement()) {
            String sql = "SELECT bd "
                    + "FROM erp.cfgu_co "
                    + "WHERE NOT b_del ORDER BY id_co;";
            ResultSet resultSet = statement.executeQuery(sql);
            
            while (resultSet.next()) {
                maErpCompanyDbs.add(resultSet.getString("bd"));
            }
            
            for (String db : maErpCompanyDbs) {
                int policy = SLibUtils.parseInt(SCfgUtils.getParamValue(statement, SDataConstantsSys.CFG_PARAM_FIN_USD_XRT_POLICY, db));
                
                switch (policy) {
                    case SDataConstantsSys.USD_XRT_POLICY_BANXICO:
                        maErpCompanyDbsXrtUsdPolicyBanxico.add(db);
                        break;
                    case SDataConstantsSys.USD_XRT_POLICY_INFORMAL:
                        maErpCompanyDbsXrtUsdPolicyInformal.add(db);
                        break;
                    default:
                        throw new UnsupportedOperationException(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\n(Política de tipo de cambio USD: " + policy + " en BD '" + db + "'.)");
                }
            }
        }
    }
    
    protected boolean isBankBizDay(final Date date) {
        boolean isBizDay = true;
        
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        
        if (dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY) {
            isBizDay = false;
        }
        else {
            for (Date nonBizDate : maBankNonBizDates) {
                if (SLibTimeUtils.isSameDate(date, nonBizDate)) {
                    isBizDay = false;
                    break;
                }
            }
        }
        
        return isBizDay;
    }
    
    protected boolean existsXrt(final Statement statement, final String database, final int currency, final Date date) throws Exception {
        int count = 0;
        String sql = "SELECT COUNT(*) "
                + "FROM " + database + ".fin_exc_rate "
                + "WHERE NOT b_del AND id_cur = " + currency + " AND id_dt = '" + SLibUtils.DbmsDateFormatDate.format(date) + "';";
        
        try (ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        }
        
        return count > 0;
    }
    
    protected void insertXrt(final Statement statement, final String database, final int currency, final Date date, final double xrt, final FileWriter logWriter) throws Exception {
        boolean inserted = false;
        String logLine = "Procesing date " + SLibUtils.DateFormatDate.format(date) + " in '" + database + "': registry ";
        
        if (!existsXrt(statement, database, currency, date)) {
            String sql = "INSERT INTO " + database + ".fin_exc_rate VALUES ("
                    + currency + ", '" + SLibUtils.DbmsDateFormatDate.format(date) + "', " + SLibUtils.getDecimalFormatExchangeRate().format(xrt) + ", "
                    + "0, " + SDataConstantsSys.USRX_USER_NA + ", " + SDataConstantsSys.USRX_USER_NA + ", " + SDataConstantsSys.USRX_USER_NA + ", NOW(), NOW(), NOW());";

            statement.execute(sql);
            inserted = true;
        }
        
        appendLogLine(logWriter, logLine + (inserted ? "inserted!" : "not inserted."), new Date());
    }
    
    protected void appendLogLine(final FileWriter logWriter, final String line, final Date date) throws Exception {
        logWriter.write("\n" + (date == null ? "" : "[" + FormatDatetime.format(date) + "] ") + line);
    }
    
    public void handleXrts() throws Exception {
        System.out.println("Procesando tipos de cambio para " + SLibUtils.GuiDateFormat.format(mtToday) + " ...");
        
        // read non-business dates and ERP-company databases:
        
        readBankNonBizDates();
        readErpCompanyDbs();
        
        // handle exchange rates for required currencies:
        
        String logFile = LOG_FILE_NAME + FormatPeriod.format(mtToday) + ".log";
        System.out.println("Bitácora de procesamiento: " + logFile + ".");
        
        try (Statement statement = miConnection.createStatement(); FileWriter logWriter = new FileWriter(logFile, true)) {
            int[] currencies = new int[] { SModSysConsts.CFGU_CUR_USD, SModSysConsts.CFGU_CUR_EUR };
            
            HashMap<Integer, String> currencyNames = new HashMap<>();
            currencyNames.put(SModSysConsts.CFGU_CUR_USD, "USD");
            currencyNames.put(SModSysConsts.CFGU_CUR_EUR, "EUR");

            for (int currency : currencies) {
                appendLogLine(logWriter, SLibUtils.textRepeat("=", TEXT_LINE_LENGTH), null);
                appendLogLine(logWriter, "START OF PROCESSING EXCHANGE RATE '" + currencyNames.get(currency) + "'...", new Date());
                
                Xrt xrt = createXrtFromRss(currency);
                
                appendLogLine(logWriter, "RSS date: " + SLibUtils.IsoFormatDate.format(xrt.Date), null);
                appendLogLine(logWriter, "RSS exchange rate: " + SLibUtils.getDecimalFormatExchangeRate().format(xrt.Xrt), null);
                
                // read available exchange rate from RSS:

                // look up for next bank business day after date of read exchange rate:
                Date nextBizDay = SLibTimeUtils.addDate(xrt.Date, 0, 0, 1);
                while (!isBankBizDay(nextBizDay)) {
                    nextBizDay = SLibTimeUtils.addDate(nextBizDay, 0, 0, 1);
                }

                // create exchange rates for companies with Banxico policy:
                for (String db : maErpCompanyDbsXrtUsdPolicyBanxico) {
                    Date nextDay = SLibTimeUtils.addDate(nextBizDay, 0, 0, 1);
                    boolean isFirstNextDayNonBizDay = !isBankBizDay(nextDay);
                    
                    do {
                        insertXrt(statement, db, currency, nextDay, xrt.Xrt, logWriter);

                        nextDay = SLibTimeUtils.addDate(nextDay, 0, 0, 1);
                    } while (isFirstNextDayNonBizDay && !isBankBizDay(nextDay));
                    
                    if (isFirstNextDayNonBizDay) {
                        insertXrt(statement, db, currency, nextDay, xrt.Xrt, logWriter);
                    }
                }

                // create exchange rates for companies with informal policy:
                for (String db : maErpCompanyDbsXrtUsdPolicyInformal) {
                    Date nextDay = SLibTimeUtils.convertToDateOnly(nextBizDay);
                    
                    do {
                        insertXrt(statement, db, currency, nextDay, xrt.Xrt, logWriter);

                        nextDay = SLibTimeUtils.addDate(nextDay, 0, 0, 1);
                    } while (!isBankBizDay(nextDay));
                }
                
                appendLogLine(logWriter, "END OF PROCESSING EXCHANGE RATE '" + currencyNames.get(currency) + "'!\n", new Date());
            }
        }
    }
    
    private class Xrt {
        
        public Date Date;
        public double Xrt;
        
        public Xrt(final Date date, final double xrt) {
            Date = date;
            Xrt = xrt;
        }
    }
}
