/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mfin.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import erp.SParamsApp;
import erp.data.SDataConstantsSys;
import erp.mod.SModSysConsts;
import erp.mod.hrs.link.db.SConfigException;
import erp.mod.hrs.link.db.SShareDB;
import erp.mod.hrs.link.pub.SShareData;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import sa.lib.SLibTimeUtils;
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
        ArrayList<String> bankNbDays = new ArrayList<>();
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
        Date nextDay = SLibTimeUtils.addDate(date, 0, 0, 1);
        
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(nextDay);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        if (bankNbDays.size() > 0) {
            for (Object day : bankNbDays) {
                String bankNbDay = day.toString();
                if (!SLibUtils.DbmsDateFormatDate.format(nextDay).equals(bankNbDay) && dayOfWeek != 7 && dayOfWeek != 1) {
                    isbankBussDay = true;
                } 
                else {
                    isbankBussDay = false;
                    break;
                }
            }
        } else {
            isbankBussDay = dayOfWeek != 7 && dayOfWeek != 1;
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
        
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        
        if (bankNbDays.size() > 0) {
            for (Object day : bankNbDays) {
                String bankNbDay = day.toString();
                if (!SLibUtils.DbmsDateFormatDate.format(date).equals(bankNbDay) && dayOfWeek != 7 && dayOfWeek != 1) {
                    bankBussDay = true;
                } 
                else {
                    bankBussDay = false;
                    break;
                }
            }
        } else {
            bankBussDay = dayOfWeek != 7 && dayOfWeek != 1;
        }
        
        return bankBussDay;
    }
    
    /**
     * Revisa si el dia que se le manda es un dia habil bancario.
     *
     * @param date
     * @param bankNbDays
     * @return
     */
    public static boolean isDayBankBussDay(Date date, ArrayList bankNbDays) {
        boolean bankBussDay = true;
        
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        
        if (bankNbDays.size() > 0) {
            for (Object day : bankNbDays) {
                String bankNbDay = day.toString();
                if (!SLibUtils.DbmsDateFormatDate.format(date).equals(bankNbDay) && dayOfWeek != 7 && dayOfWeek != 1) {
                    bankBussDay = true;
                } 
                else {
                    bankBussDay = false;
                    break;
                }
            }
        } 
        else {
            bankBussDay = dayOfWeek != 7 && dayOfWeek != 1;
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
    public static ArrayList<String> setExchangeRateDays(int usd_xrt_policy, ArrayList calendar) throws Exception {
        Date date = new Date();
        ArrayList<String> exchangeRateDays = new ArrayList<>();
        boolean bankBussDay = false;
        boolean canSetDay = false;
        
        if (usd_xrt_policy == SDataConstantsSys.USD_XRT_POLICY_INFORMAL) {
            while (!bankBussDay) {
                if (isNextDayBankBussDay(date, calendar)) {
                    date = SLibTimeUtils.addDate(date, 0, 0, 1);
                    if (exchangeRateDays.isEmpty()) {
                        exchangeRateDays.add(SLibUtils.DbmsDateFormatDate.format(date));
                    } 
                    else {
                        bankBussDay = true;
                    }
                } 
                else {
                    date = SLibTimeUtils.addDate(date, 0, 0, 1);
                    if (!exchangeRateDays.isEmpty()) {
                        exchangeRateDays.add(SLibUtils.DbmsDateFormatDate.format(date));
                    }
                }
            }
        } 
        else if (usd_xrt_policy == SDataConstantsSys.USD_XRT_POLICY_BANXICO) {
            while(!bankBussDay){
                date = SLibTimeUtils.addDate(date, 0, 0, 1);
                if(isDayBankBussDay(date, calendar)){
                    date = SLibTimeUtils.addDate(date, 0, 0, 1);
                    while(!bankBussDay){
                        if(isDayBankBussDay(date, calendar)){
                            exchangeRateDays.add(SLibUtils.DbmsDateFormatDate.format(date));
                            bankBussDay = true;
                            break;
                        }
                        else{
                            exchangeRateDays.add(SLibUtils.DbmsDateFormatDate.format(date));
                            date = SLibTimeUtils.addDate(date, 0, 0, 1);
                        }
                    }
                }
            }
        } 
        else {
            throw new Exception("La politica de tipo de cambio no es reconocida");
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
                try{
                    Logger logger = Logger.getLogger("logs/logsExchangeRate_");
                    logger.setUseParentHandlers(false);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy_MM");
                    FileHandler fh;
                    fh = new FileHandler("logs/logsExchangeRate_" + format.format(Calendar.getInstance().getTime()) + ".log", true);
                    logger.addHandler(fh);
                    SimpleFormatter formatter = new SimpleFormatter() {
                            private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            
                            public String format() {
                                return String.format("%s \n", dateFormat.format(Calendar.getInstance().getTime()));
                            }
                    };
                    fh.setFormatter(formatter);
                    String message = "Saved value: " + valueExchangeRate + " at " + exchangeRateDay;
                    logger.info(message);
                    fh.close();
                } catch (SecurityException ex) {
                    Logger.getLogger(SSetExchangeRate.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public static void main(String[] args) throws ParseException, SQLException, ClassNotFoundException, JsonProcessingException, SConfigException {
        SShareData sd = new SShareData();
        SShareDB sdb = new SShareDB();
        sd.setJsonConn("{\"dbHost\":\"192.168.1.233\",\"dbName\":\"erp\",\"dbPort\":\"3306\",\"dbUser\":\"root\",\"dbPass\":\"msroot\",\"dbMainId\":\"1211\"}");
        String incidents = "{\"year\":\"2023\",\"num\":\"20\",\"type_pay\":\"2\",\"rows\":[{\"id_emp\":\"3774\",\"company_id\":\"2852\"},{\"id_emp\":\"3505\",\"company_id\":\"1211\"}]}";
        String json = "{\"year\":2023,\"num\":20,\"type_pay\":2,\"rows\":[{\"id_emp\":4675,\"company_id\":2852}]}";
        String prueba ="[{\"id_company\":1,\"external_id\":2852,\"company_db_name\":\"erp_aeth\",\"last_sync_date\":\"2023-01-01 17:01:42\"},{\"id_company\":2,\"external_id\":1,\"company_db_name\":\"erp_th\",\"last_sync_date\":null},{\"id_company\":3,\"external_id\":1211,\"company_db_name\":\"erp_otsa\",\"last_sync_date\":\"2023-12-05 17:01:42\"},{\"id_company\":4,\"external_id\":1603,\"company_db_name\":\"erp_sasa\",\"last_sync_date\":\"2023-12-05 17:01:42\"},{\"id_company\":5,\"external_id\":2217,\"company_db_name\":\"erp_amesa\",\"last_sync_date\":\"2023-12-05 17:01:42\"}]";
        String jsonString = "[{\"id_company\":1,\"external_id\":2852,\"company_db_name\":\"erp_aeth\",\"last_sync_date\":\"2023-12-05 17:01:42\"},{\"id_company\":2,\"external_id\":1,\"company_db_name\":\"erp_th\",\"last_sync_date\":null},{\"id_company\":3,\"external_id\":1211,\"company_db_name\":\"erp_otsa\",\"last_sync_date\":\"2023-12-05 17:01:42\"},{\"id_company\":4,\"external_id\":1603,\"company_db_name\":\"erp_sasa\",\"last_sync_date\":\"2023-12-05 17:01:42\"},{\"id_company\":5,\"external_id\":2217,\"company_db_name\":\"erp_amesa\",\"last_sync_date\":\"2023-12-05 17:01:42\"}]";
        String fecha = "2024-01-03 13:45:34";
//      String res = sd.getEmployeesSiieData();
        String jsonS = "[{\"company\":2852,\"lEmployees\":[5811,5952]},{\"company\":1211,\"lEmployees\":[5059]}]";
        String per = "5059";
        //String jsonText = "{\"id_bp\":5059,\"lastname1\":\"AVILES\",\"lastname2\":\"GOMEZ\",\"names\":\"ADRIAN ALEJANDRO\",\"rfc\":null,\"id_bpb\":\"5071\",\"id_add\":\"1\",\"id_con\":\"1\",\"fk_cl_cat_sex\":1,\"fk_tp_cat_sex\":2,\"fk_cl_cat_blo\":2,\"fk_tp_cat_blo\":3,\"fk_cl_cat_mar\":3,\"fk_tp_cat_mar\":2,\"fk_cl_cat_edu\":4,\"fk_tp_cat_edu\":6,\"email_01\":\"adrian.alejandro.aviles@gmail.com\",\"email_02\":\"adrian.aviles@swaplicado.com.mx\",\"tel_num_01\":\"4525223239\",\"tel_num_02\":null,\"emergs_tel_num\":\"4522090000\",\"emergs_con\":\"maria magdalena\",\"street\":\"JOAQUIN AMARO 42\",\"street_num_ext\":\"773\",\"street_num_int\":null,\"neighborhood\":\"GUSTAVO DIAZ ORDAZ\",\"locality\":\"MORELIA\",\"state\":16,\"zip_code\":\"58057\",\"county\":\"MORELIA\",\"mate\":\"prueba :p\",\"mate_dt_bir_n\":\"10-10-1995\",\"fk_cl_cat_sex_mate\":1,\"fk_tp_cat_sex_mate\":3,\"benefs\":\"madre 70% hermano 30%\",\"son_1\":\"hijo2\",\"son_dt_bir_1_n\":\"10-10-1995\",\"fk_cl_cat_sex_son_1\":1,\"fk_tp_cat_sex_son_1\":3,\"son_2\":\"\",\"son_dt_bir_2_n\":\"\",\"fk_cl_cat_sex_son_2\":\"\",\"fk_tp_cat_sex_son_2\":\"\",\"son_3\":\"\",\"son_dt_bir_3_n\":\"\",\"fk_cl_cat_sex_son_3\":\"\",\"fk_tp_cat_sex_son_3\":\"\",\"son_4\":\"\",\"son_dt_bir_4_n\":\"\",\"fk_cl_cat_sex_son_4\":\"\",\"fk_tp_cat_sex_son_4\":\"\",\"son_5\":\"\",\"son_dt_bir_5_n\":\"\",\"fk_cl_cat_sex_son_5\":\"\",\"fk_tp_cat_sex_son_5\":\"\"}";
        //String jsonText ="{\"id_bp\":5059,\"id_add\":1,\"id_con\":1,\"id_bpb\":5071,\"lastname1\":\"AVILES\",\"lastname2\":\"GOMEZ\",\"names\":\"ADRIAN ALEJANDRO\",\"rfc\":\"AIGA951010GN9\",\"fk_cl_cat_sex\":1,\"fk_tp_cat_sex\":2,\"fk_cl_cat_blo\":2,\"fk_tp_cat_blo\":1,\"fk_cl_cat_mar\":3,\"fk_tp_cat_mar\":2,\"fk_cl_cat_edu\":4,\"fk_tp_cat_edu\":6,\"email_01\":\"aviles@gmail.com\",\"email_02\":\"ADRIASWAP@gmail.com\",\"tel_num_01\":\"\",\"tel_num_02\":\"45200000\",\"emergs_tel_num\":\"4522090000\",\"emergs_con\":\"maria - madre\",\"street\":\"\",\"street_num_ext\":\"10\",\"street_num_int\":\"1\",\"neighborhood\":\"una col de gdl\",\"locality\":\"Guadalajara\",\"state\":\"14\",\"zip_code\":\"50000\",\"county\":\"Guadalajara\",\"mate\":\"no hay :,c\",\"mate_dt_bir_n\":\"1995-10-10\",\"fk_cl_cat_sex_mate\":1,\"fk_tp_cat_sex_mate\":3,\"benefs\":\"asklfjsokdfsojjf\",\"son_1\":\"hijo1\",\"son_dt_bir_1_n\":\"2000-10-10\",\"fk_cl_cat_sex_son_1\":1,\"fk_tp_cat_sex_son_1\":2,\"son_2\":\"hijo2\",\"son_dt_bir_2_n\":\"2001-10-10\",\"fk_cl_cat_sex_son_2\":1,\"fk_tp_cat_sex_son_2\":3,\"son_3\":\"\",\"son_dt_bir_3_n\":\"\",\"fk_cl_cat_sex_son_3\":1,\"fk_tp_cat_sex_son_3\":1,\"son_4\":\"\",\"son_dt_bir_4_n\":\"\",\"fk_cl_cat_sex_son_4\":1,\"fk_tp_cat_sex_son_4\":1,\"son_5\":\"\",\"son_dt_bir_5_n\":\"\",\"fk_cl_cat_sex_son_5\":1,\"fk_tp_cat_sex_son_5\":1}";
        String jsonText = "{\"id_bp\":5059,\"id_add\":1,\"id_con\":1,\"id_bpb\":5071,\"lastname1\":\"AVILES\",\"lastname2\":\"GOMEZ\",\"names\":\"ADRIAN ALEJANDRO\",\"rfc\":\"AIGA951010GN9\",\"fk_cl_cat_sex\":1,\"fk_tp_cat_sex\":2,\"fk_cl_cat_blo\":2,\"fk_tp_cat_blo\":3,\"fk_cl_cat_mar\":3,\"fk_tp_cat_mar\":6,\"fk_cl_cat_edu\":4,\"fk_tp_cat_edu\":7,\"email_01\":\"aviles@gmail.com\",\"email_02\":\"sw@gmail.com\",\"tel_num_01\":\"4525223239\",\"tel_num_02\":\"4520000\",\"tel_ext_02\":\"151\",\"emergs_tel_num\":\"452090000\",\"emergs_con\":\"maria magdalena\",\"fk_tp_cat_kin_emergs\":21,\"fk_cl_cat_kin_emergs\":5,\"street\":\"una calle de gdl\",\"street_num_ext\":\"1\",\"street_num_int\":\"0\",\"neighborhood\":\"una col de gdl\",\"locality\":\"gdl\",\"state\":\"14\",\"zip_code\":\"50000\",\"county\":\"gdl\",\"reference\":\"una referencia\",\"mate\":\"no hay, no existe, :p\",\"mate_dt_bir_n\":\"1995-10-10\",\"fk_cl_cat_sex_mate\":1,\"fk_tp_cat_sex_mate\":3,\"benefs\":\"madre-hermano\",\"son_1\":\"hijo1\",\"son_dt_bir_1_n\":\"2000-10-10\",\"fk_cl_cat_sex_son_1\":1,\"fk_tp_cat_sex_son_1\":2,\"son_2\":\"hijo2\",\"son_dt_bir_2_n\":\"2001-10-10\",\"fk_cl_cat_sex_son_2\":1,\"fk_tp_cat_sex_son_2\":3,\"son_3\":\"\",\"son_dt_bir_3_n\":\"\",\"fk_cl_cat_sex_son_3\":1,\"fk_tp_cat_sex_son_3\":1,\"son_4\":\"\",\"son_dt_bir_4_n\":\"\",\"fk_cl_cat_sex_son_4\":1,\"fk_tp_cat_sex_son_4\":1,\"son_5\":\"\",\"son_dt_bir_5_n\":\"\",\"fk_cl_cat_sex_son_5\":1,\"fk_tp_cat_sex_son_5\":1}";
//sd.getPersonalInfo(per);
        String jsonAdrian = "{\"id_bp\":\"5059\",\"id_add\":\"1\",\"id_con\":\"1\",\"id_bpb\":\"5071\",\"lastname1\":\"áñILES\",\"lastname2\":\"GOMEZ\",\"names\":\"ADRIAN ALEJANDRO\",\"rfc\":\"AIGA951010GN9\",\"fk_cl_cat_sex\":\"1\",\"fk_tp_cat_sex\":\"2\",\"fk_cl_cat_blo\":\"2\",\"fk_tp_cat_blo\":\"1\",\"fk_cl_cat_mar\":\"3\",\"fk_tp_cat_mar\":\"2\",\"fk_cl_cat_edu\":\"4\",\"fk_tp_cat_edu\":\"6\",\"email_01\":\"adrian.alejandro.aviles@gmail.com\",\"email_02\":\"adrian.aviles@swaplicado.com.mx\",\"tel_num_01\":\"4525223239\",\"tel_num_02\":\"4434712128\",\"tel_ext_02\":\"\",\"emergs_tel_num\":\"4522090000\",\"emergs_con\":\"Maria Magdalena\",\"fk_tp_cat_kin_emergs\":\"21\",\"fk_cl_cat_kin_emergs\":\"5\",\"street\":\"ANDRES QUINTANA ROO\",\"street_num_ext\":\"773\",\"street_num_int\":\"\",\"neighborhood\":\"JUAREZ\",\"locality\":\"MORELIA\",\"fid_sta_n\":\"16\",\"zip_code\":\"58010\",\"county\":\"MORELIA\",\"reference\":\"A UN COSTADO DE SORIANA CENTRO\",\"mate\":\"césar ñ\",\"mate_dt_bir_n\":\"\",\"fk_cl_cat_sex_mate\":\"1\",\"fk_tp_cat_sex_mate\":\"1\",\"benefs\":\"Madre - 70% hermano 30%\",\"son_1\":\"\",\"son_dt_bir_1_n\":\"\",\"fk_cl_cat_sex_son_1\":\"1\",\"fk_tp_cat_sex_son_1\":\"1\",\"son_2\":\"\",\"son_dt_bir_2_n\":\"\",\"fk_cl_cat_sex_son_2\":\"1\",\"fk_tp_cat_sex_son_2\":\"1\",\"son_3\":\"\",\"son_dt_bir_3_n\":\"\",\"fk_cl_cat_sex_son_3\":\"1\",\"fk_tp_cat_sex_son_3\":\"1\",\"son_4\":\"\",\"son_dt_bir_4_n\":\"\",\"fk_cl_cat_sex_son_4\":\"1\",\"fk_tp_cat_sex_son_4\":\"1\",\"son_5\":\"\",\"son_dt_bir_5_n\":\"\",\"fk_cl_cat_sex_son_5\":\"1\",\"fk_tp_cat_sex_son_5\":\"1\"}";
        String base64EncodedJson = "eyJpZF9icCI6NTA1OSwiaWRfYWRkIjoxLCJpZF9jb24iOjEsImlkX2JwYiI6NTA3MSwibGFzdG5hbWUxIjoiQVZJTEVTIiwibGFzdG5hbWUyIjoiR09NRVoiLCJuYW1lcyI6IkFEUklBTiBBTEVKQU5EUk8iLCJyZmMiOiJBSUdBOTUxMDEwR045IiwiZmtfY2xfY2F0X3NleCI6MSwiZmtfdHBfY2F0X3NleCI6MiwiZmtfY2xfY2F0X2JsbyI6MiwiZmtfdHBfY2F0X2JsbyI6MSwiZmtfY2xfY2F0X21hciI6MywiZmtfdHBfY2F0X21hciI6MiwiZmtfY2xfY2F0X2VkdSI6NCwiZmtfdHBfY2F0X2VkdSI6NiwiZW1haWxfMDEiOiJhZHJpYW4uYWxlamFuZHJvLmF2aWxlc0BnbWFpbC5jb20iLCJlbWFpbF8wMiI6ImFkcmlhbi5hdmlsZXNAc3dhcGxpY2Fkby5jb20ubXgiLCJ0ZWxfbnVtXzAxIjoiNDUyNTIyMzIzOSIsInRlbF9udW1fMDIiOiI0NDM0NzEyMTI4IiwidGVsX2V4dF8wMiI6IiIsImVtZXJnc190ZWxfbnVtIjoiNDUyMjA5MDAwMCIsImVtZXJnc19jb24iOiJNYXJpYSBNYWdkYWxlbmEiLCJma190cF9jYXRfa2luX2VtZXJncyI6MjEsImZrX2NsX2NhdF9raW5fZW1lcmdzIjo1LCJzdHJlZXQiOiJBTkRSRVMgUVVJTlRBTkEgUk9PIiwic3RyZWV0X251bV9leHQiOiI3NzMiLCJzdHJlZXRfbnVtX2ludCI6IiIsIm5laWdoYm9yaG9vZCI6IkpVQVJFWiIsImxvY2FsaXR5IjoiTU9SRUxJQSIsImZpZF9zdGFfbiI6MTYsInppcF9jb2RlIjoiNTgwMTAiLCJjb3VudHkiOiJNT1JFTElBIiwicmVmZXJlbmNlIjoiQSBVTiBDT1NUQURPIERFIFNPUklBTkEgQ0VOVFJPIiwibWF0ZSI6IkFkcmnDocOxIiwibWF0ZV9kdF9iaXJfbiI6IiIsImZrX2NsX2NhdF9zZXhfbWF0ZSI6MSwiZmtfdHBfY2F0X3NleF9tYXRlIjoxLCJiZW5lZnMiOiJNYWRyZSAtIDcwJSBoZXJtYW5vIDMwJSIsInNvbl8xIjoiIiwic29uX2R0X2Jpcl8xX24iOiIiLCJma19jbF9jYXRfc2V4X3Nvbl8xIjoxLCJma190cF9jYXRfc2V4X3Nvbl8xIjoxLCJzb25fMiI6IiIsInNvbl9kdF9iaXJfMl9uIjoiIiwiZmtfY2xfY2F0X3NleF9zb25fMiI6MSwiZmtfdHBfY2F0X3NleF9zb25fMiI6MSwic29uXzMiOiIiLCJzb25fZHRfYmlyXzNfbiI6IiIsImZrX2NsX2NhdF9zZXhfc29uXzMiOjEsImZrX3RwX2NhdF9zZXhfc29uXzMiOjEsInNvbl80IjoiIiwic29uX2R0X2Jpcl80X24iOiIiLCJma19jbF9jYXRfc2V4X3Nvbl80IjoxLCJma190cF9jYXRfc2V4X3Nvbl80IjoxLCJzb25fNSI6IiIsInNvbl9kdF9iaXJfNV9uIjoiIiwiZmtfY2xfY2F0X3NleF9zb25fNSI6MSwiZmtfdHBfY2F0X3NleF9zb25fNSI6MX0=";
        //sdb.insertPersonalInfo(base64EncodedJson);
        sdb.getPersonalInfo(per);
    }

}
