/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.lib.table;

import java.awt.Color;

/**
 *
 * @author Sergio Flores
 */
public abstract class STableConstants {

    public static final int UNDEFINED = 0;

    public static final int WIDTH_DATE = 65;
    public static final int WIDTH_DATE_TIME = 110;
    public static final int WIDTH_BOOLEAN = 50;
    public static final int WIDTH_BOOLEAN_2X = 75;
    public static final int WIDTH_BOOLEAN_3X = 100;
    public static final int WIDTH_YEAR = 40;
    public static final int WIDTH_YEAR_PERIOD = 50;
    public static final int WIDTH_PERIOD = 30;
    public static final int WIDTH_RECORD_TYPE = 30;
    public static final int WIDTH_RECORD_NUM = 65;
    public static final int WIDTH_ACCOUNT_ID = 100;
    public static final int WIDTH_ACCOUNT = 200;
    public static final int WIDTH_USER = 100;
    public static final int WIDTH_VALUE = 75;
    public static final int WIDTH_VALUE_2X = 100;
    public static final int WIDTH_VALUE_UNITARY = 100;
    public static final int WIDTH_EXCHANGE_RATE = 75;
    public static final int WIDTH_PERCENTAGE = 75;
    public static final int WIDTH_DISCOUNT = 75;
    public static final int WIDTH_QUANTITY = 100;
    public static final int WIDTH_QUANTITY_2X = 135;
    public static final int WIDTH_UNIT_SYMBOL = 35;
    public static final int WIDTH_CURRENCY_KEY = 35;
    public static final int WIDTH_ITEM_KEY = 80;
    public static final int WIDTH_ITEM = 100;
    public static final int WIDTH_ITEM_2X = 200;
    public static final int WIDTH_ITEM_3X = 300;
    public static final int WIDTH_NUM_TINYINT = 25;
    public static final int WIDTH_NUM_SMALLINT = 50;
    public static final int WIDTH_NUM_INTEGER = 75;
    public static final int WIDTH_CODE_COB = 35;
    public static final int WIDTH_CODE_COB_ENT = 45;
    public static final int WIDTH_CODE_DOC = 35;
    public static final int WIDTH_DOC_NUM = 75;
    public static final int WIDTH_DOC_NUM_REF = 50;
    public static final int WIDTH_ICON = 16;

    public static final int STATUS_OFF = 0;
    public static final int STATUS_ON = 1;

    public static final int SETTING_FILTER_DELETED = 1;
    public static final int SETTING_FILTER_PERIOD = 2;
    public static final int SETTING_FILTER_USER = 3;
    public static final int SETTING_FILTER_USER_NEW = 4;
    public static final int SETTING_FILTER_USER_EDIT = 5;
    public static final int SETTING_FILTER_USER_DELETE = 6;
    public static final int SETTING_FILTER_BPS_BIZ_PARTNER_TYPE = 7;
    public static final int SETTING_FILTER_BPS_BIZ_PARTNER = 8;
    public static final int SETTING_FILTER_ITM_ITEM_FAMILY = 9;
    public static final int SETTING_FILTER_ITM_ITEM_GROUP = 10;
    public static final int SETTING_FILTER_ITM_ITEM_GENERIC = 11;
    public static final int SETTING_FILTER_ITM_ITEM = 12;
    public static final int SETTING_FILTER_ITM_BRAND = 13;
    public static final int SETTING_FILTER_ITM_MANUFACTURER = 14;
    public static final int SETTING_FILTER_ITM_ELEMENT = 15;
    public static final int SETTING_FILTER_FIN_REC_TYPE = 16;
    public static final int SETTING_FILTER_YEAR = 17;
    public static final int SETTING_FILTER_ACCOUNT = 18;

    public static final int SETTING_OPTION_TAXES = 101;
    public static final int SETTING_OPTION_CURRENCY_LOCAL = 102;
    public static final int SETTING_OPTION_DATE_TYPE = 103;
    public static final int SETTING_OPTION_SWITCH = 109;

    public static final int REFRESH_MODE_RESET = 1;
    public static final int REFRESH_MODE_RELOAD = 2;

    public static final int ICON_NULL = 1000;
    public static final int ICON_ST_ANNUL = 1001;
    public static final int ICON_ST_THUMBS_DOWN = 1002;
    public static final int ICON_ST_THUMBS_UP = 1003;
    public static final int ICON_ST_WAIT = 1004;
    public static final int ICON_WARN = 1051;
    public static final int ICON_XML = 1101;
    public static final int ICON_XML_PEND = 1102;
    public static final int ICON_XML_SIGN = 1103;
    public static final int ICON_XML_CANC = 1106;
    public static final int ICON_XML_CANC_PDF = 1107;
    public static final int ICON_XML_CANC_XML = 1108;
    public static final int ICON_MFG_ST = 1200;
    public static final int ICON_MFG_ST_01 = 1201;
    public static final int ICON_MFG_ST_02 = 1202;
    public static final int ICON_MFG_ST_03 = 1203;
    public static final int ICON_MFG_ST_04 = 1204;
    public static final int ICON_MFG_ST_05 = 1205;
    public static final int ICON_MFG_ST_06 = 1206;
    public static final int ICON_VIEW_LIG_GRE = 1207;
    public static final int ICON_VIEW_LIG_RED = 1208;
    public static final int ICON_VIEW_LIG_WHI = 1209;
    public static final int ICON_VIEW_LIG_YEL = 1210;

    public static final int STYLE_BOLD = 1;
    public static final int STYLE_ITALIC = 2;
    public static final int STYLE_DELETE = 3;

    public static final int VIEW_STATUS_ALL = 1;
    public static final int VIEW_STATUS_ALIVE = 2;

    //public static final java.awt.Color FOREGROUND_EDIT = new Color(31, 73, 125);
    public static final java.awt.Color FOREGROUND_EDIT = new Color(0, 0, 255);
    public static final java.awt.Color FOREGROUND_EDIT_NEG = new Color(255, 0, 0);
    public static final java.awt.Color FOREGROUND_READ = new Color(0, 0, 0);
    public static final java.awt.Color FOREGROUND_READ_NEG = new Color(255, 0, 0);

    public static final java.awt.Color BACKGROUND_PLAIN_EDIT = new Color(255, 255, 255);
    public static final java.awt.Color BACKGROUND_PLAIN_READ = new Color(255, 255, 255);
    public static final java.awt.Color BACKGROUND_SELECT_EDIT = new Color(149, 179, 215);
    public static final java.awt.Color BACKGROUND_SELECT_EDIT_FOCUS = new Color(184, 204, 228);
    public static final java.awt.Color BACKGROUND_SELECT_READ = new Color(149, 179, 215);
    public static final java.awt.Color BACKGROUND_SELECT_READ_FOCUS = new Color(184, 204, 228);

    public static final java.lang.String FIELD_IS_EDITABLE = "f_is_editable";
    public static final java.lang.String FIELD_STYLE = "f_style";
    public static final java.lang.String MSG_WAR_REGISTRY_NO_EDITABLE = "Este registro no se puede modificar.";
    public static final java.lang.String MSG_ERR_TABLE_TAB_SQL_QUERY_EMPTY = "La sentencia de consulta está vacía.";
}
