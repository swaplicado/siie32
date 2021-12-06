/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mfin.data;

import erp.SParamsApp;
import erp.data.SDataConstantsSys;
import erp.mod.SModSysConsts;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbDatabase;
import sa.lib.xml.SXmlUtils;

/**
 *
 * @author Adrián Avilés
 */
public class SSetExchangeRate {
    
    private static final int ID_PARAM_XRT_POLICY = 31;

    /**
     * Obtiene el la poltica de tipo de cambio del dolar, las politicas son: 1
     * para politica de banxico y 2 para politica informal usada por la empresa.
     *
     * @param connection
     * @return
     * @throws SQLException
     */
    public static int getExchangeRatePolicy(Connection connection) throws SQLException {
        int exchangeRatePolicy = SDataConstantsSys.USD_XRT_POLICY_BANXICO;

        Statement statement = connection.createStatement();
        String sql = "SELECT param_value FROM cfg_param WHERE id_param = " + ID_PARAM_XRT_POLICY;
        ResultSet resultSet = statement.executeQuery(sql);
        if (resultSet.next()) {
            exchangeRatePolicy = Integer.parseInt(resultSet.getString("param_value"));
        }
        return exchangeRatePolicy;
    }

    /**
     * Obtiene las fechas de los proximos dias inhabiles bancarios a partir de la
     * fecha actual. regresa un ArrayList con las fechas obtenidas.
     *
     * @param connection
     * @return
     * @throws SQLException
     */
    public static ArrayList readBankNbDay(Connection connection) throws SQLException {
        ArrayList bankNbDays = new ArrayList();
        Date date = new Date();
        
        Statement statement = connection.createStatement();
        String sql = "SELECT nb_day FROM finu_bank_nb_day WHERE nb_day >= " + "'" + SLibUtils.DbmsDateFormatDate.format(date) + "'" + " AND NOT b_del";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            bankNbDays.add(resultSet.getString(1));
        }
        return bankNbDays;
    }

    /**
     * Revisa si el dia siguiente de la fecha ingresada es un dia habil
     * bancario.
     *
     * @param date
     * @param bankNbDays
     * @return
     */
    public static boolean isNextDayBankBussDay(Date date, ArrayList bankNbDays) {
        boolean isbankBussDay = true;
        Date nextDay = new Date();
        nextDay.setDate(date.getDate() + 1);

        for (Object day : bankNbDays) {
            String bankNbDay = day.toString();
            if (!SLibUtils.DbmsDateFormatDate.format(nextDay).equals(bankNbDay) && nextDay.getDay() != 6 && nextDay.getDay() != 0) {
                isbankBussDay = true;
            } 
            else {
                isbankBussDay = false;
                break;
            }
        }
        return isbankBussDay;
    }

    /**
     * Revisa si el dia actual es un dia habil bancario.
     *
     * @param bankNbDays
     * @return
     */
    public static boolean isActualDayBankBussDay(ArrayList bankNbDays) {
        boolean bankBussDay = true;

        Date date = new Date();

        for (Object day : bankNbDays) {
            String bankNbDay = day.toString();
            if (!SLibUtils.DbmsDateFormatDate.format(date).equals(bankNbDay) && date.getDay() != 6 && date.getDay() != 0) {
                bankBussDay = true;
            } 
            else {
                bankBussDay = false;
                break;
            }
        }
        return bankBussDay;
    }

    /**
     * Lee el xml del tipo de cambio de la pagina oficial de banxico:
     * (https://www.banxico.org.mx/rsscb/rss?BMXC_canal=fix&BMXC_idioma=es) , y
     * obtiene el valor del tipo de cambio.
     *
     * @return
     * @throws MalformedURLException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws Exception
     */
    public static double readXmlExchangeRate() throws MalformedURLException, ParserConfigurationException, SAXException, Exception {
        Double valueExchangeRate = null;
        URL feedSource = new URL("https://www.banxico.org.mx/rsscb/rss?BMXC_canal=fix&BMXC_idioma=es");
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(feedSource.openStream());
        Node node = SXmlUtils.extractElements(doc, "cb:value").item(0);
        valueExchangeRate = Double.parseDouble(node.getTextContent());
        return valueExchangeRate;
    }

    /**
     * Pone en un ArrayList todas las fechas en las cuales se utilizara el tipo
     * de cambio obtenido de banxico, dependiendo de la poltica de tipo de
     * cambio obtenida.
     *
     * @param usd_xrt_policy
     * @param calendar
     * @return
     * @throws Exception
     */
    public static ArrayList setExchangeRateDays(int usd_xrt_policy, ArrayList calendar) throws Exception {
        Date date = new Date();
        ArrayList exchangeRateDays = new ArrayList();
        boolean bankBussDay = false;
        boolean canSetDay = false;

        while (!bankBussDay) {
            if (isNextDayBankBussDay(date, calendar)) {

                if (usd_xrt_policy == SDataConstantsSys.USD_XRT_POLICY_INFORMAL) {
                    date.setDate(date.getDate() + 1);
                    canSetDay = true;
                } 
                else if (usd_xrt_policy == SDataConstantsSys.USD_XRT_POLICY_BANXICO) {
                    date.setDate(date.getDate() + 1);
                    canSetDay = false;
                    usd_xrt_policy = SDataConstantsSys.USD_XRT_POLICY_INFORMAL;
                } 
                else {
                    throw new Exception("La politica de tipo de cambio no es reconocida");
                }

                if (canSetDay) {
                    if (exchangeRateDays.isEmpty()) {
                        exchangeRateDays.add(SLibUtils.DbmsDateFormatDate.format(date));
                    } 
                    else {
                        bankBussDay = true;
                    }
                }

            } 
            else {
                date.setDate(date.getDate() + 1);
                if (!exchangeRateDays.isEmpty()) {
                    exchangeRateDays.add(SLibUtils.DbmsDateFormatDate.format(date));
                }
            }
        }
        return exchangeRateDays;
    }

    /**
     * Comprueba si no existe un valor de tipo de cambio en la fecha ingresada
     *
     * @param connection
     * @param day
     * @return
     * @throws SQLException
     */
    public static boolean isSetExchangeRate(Connection connection, String day) throws SQLException {
        String sql = "SELECT exc_rate FROM fin_exc_rate WHERE id_dt = " + '"' + day + '"';
        boolean isSetExchangeRate = false;
        try (ResultSet resultSet = connection.createStatement().executeQuery(sql)) {
            if (resultSet.next()) {
                isSetExchangeRate = true;
            }
        }
        return isSetExchangeRate;
    }

    /**
     * Guarda en la base de datos el tipo de cambio y la fechas en las que se
     * usará;
     *
     * @param connection
     * @param valueExchangeRate
     * @param exchangeRateDays
     * @throws ParseException
     * @throws Exception
     */
    public static void saveExchangeRate(Connection connection, Double valueExchangeRate, ArrayList<String> exchangeRateDays) throws ParseException, Exception {
        SDataExchangeRate exchangeRate = new SDataExchangeRate();

        exchangeRate.setPkCurrencyId(SModSysConsts.CFGU_CUR_USD);
        exchangeRate.setExchangeRate(valueExchangeRate);
        exchangeRate.setIsDeleted(false);
        exchangeRate.setFkUserNewId(1);
        exchangeRate.setFkUserEditId(1);
        exchangeRate.setFkUserDeleteId(1);
        exchangeRate.setUserNewTs(new Date());
        exchangeRate.setUserEditTs(new Date());
        exchangeRate.setUserDeleteTs(new Date());
        for (String exchangeRateDay : exchangeRateDays) {
            if (!isSetExchangeRate(connection, exchangeRateDay)) {
                Date newDate = SLibUtils.DbmsDateFormatDate.parse(exchangeRateDay);
                exchangeRate.setPkDateId(newDate);
                exchangeRate.save(connection);
                System.out.println("Saved value: " + valueExchangeRate + " at " + exchangeRateDay);
            }
        }
    }

    public static void main(String[] args) {
        try {
            SParamsApp paramsApp = new SParamsApp();

            if (!paramsApp.read()) {
                throw new Exception(erp.SClient.ERR_PARAMS_APP_READING);
            }

            //conect to erp database
            SDbDatabase dbErp = new SDbDatabase(SDbConsts.DBMS_MYSQL);
            int result = dbErp.connect(paramsApp.getDatabaseHostClt(), paramsApp.getDatabasePortClt(),
                    paramsApp.getDatabaseName(), paramsApp.getDatabaseUser(), paramsApp.getDatabasePswd());

            if (result != SDbConsts.CONNECTION_OK) {
                throw new Exception(SDbConsts.ERR_MSG_DB_CONNECTION);
            }

            ArrayList bankNbDays = readBankNbDay(dbErp.getConnection());

            if (isActualDayBankBussDay(bankNbDays)) {
                // total databases:

                ArrayList companiesDb = new ArrayList();

                String sql = "SELECT bd "
                        + "FROM erp.cfgu_co ;";

                try (ResultSet resultSet = dbErp.getConnection().createStatement().executeQuery(sql)) {
                    while (resultSet.next()) {
                        String bd = resultSet.getString("bd");
                        companiesDb.add(bd);
                    }
                }

                Double valueExchangeRate = readXmlExchangeRate();

                for (Object companiesDb1 : companiesDb) {
                    // connect to company database:

                    SDbDatabase dbCompany = new SDbDatabase(SDbConsts.DBMS_MYSQL);
                    result = dbCompany.connect(paramsApp.getDatabaseHostClt(), paramsApp.getDatabasePortClt(), companiesDb1.toString(), paramsApp.getDatabaseUser(), paramsApp.getDatabasePswd());
                    if (result == SDbConsts.CONNECTION_OK) {
                        int exchangeRatePolicy = getExchangeRatePolicy(dbCompany.getConnection());
                        ArrayList exchangeRateDays = setExchangeRateDays(exchangeRatePolicy, bankNbDays);
                        saveExchangeRate(dbCompany.getConnection(), valueExchangeRate, exchangeRateDays);
                    }
                }
            }

        } 
        catch (Exception e) {
            System.err.println(e);
        }
    }

}
