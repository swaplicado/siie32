/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.server;

import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.table.STableCellRendererBoolean;
import erp.lib.table.STableCellRendererDate;
import erp.lib.table.STableCellRendererDefault;
import erp.lib.table.STableCellRendererDefaultColor;
import erp.lib.table.STableCellRendererIcon;
import erp.lib.table.STableCellRendererNumber;
import erp.lib.table.STableCellRendererStyle;
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Locale;

/**
 *
 * @author Sergio Flores
 */
public class SFormatters implements java.io.Serializable {

    public static final int RENDERER_DEFAULT = 101;
    public static final int RENDERER_BOOLEAN = 102;
    public static final int RENDERER_SIMPLE_INTEGER = 103;
    public static final int RENDERER_NUMBER_LONG = 104;
    public static final int RENDERER_NUMBER_DOUBLE = 105;
    public static final int RENDERER_YEAR = 106;
    public static final int RENDERER_MONTH = 107;
    public static final int RENDERER_RECORD_NUMBER = 108;
    public static final int RENDERER_STYLE = 109;
    public static final int RENDERER_ICON = 110;
    
    public static final int RENDERER_VALUE = 201;
    public static final int RENDERER_VALUE_UNITARY = 202;
    public static final int RENDERER_VALUE_UNITARY_FIXED4 = 203;
    public static final int RENDERER_EXCHANGE_RATE = 204;
    public static final int RENDERER_PERCENTAGE = 205;
    public static final int RENDERER_DISCOUNT = 206;
    public static final int RENDERER_CURRENCY = 207;
    public static final int RENDERER_CURRENCY_LOCAL = 208;
    public static final int RENDERER_QUANTITY = 209;
    public static final int RENDERER_UNITS_CONTAINED = 210;
    public static final int RENDERER_UNITS_VIRTUAL = 211;
    public static final int RENDERER_NET_CONTENT = 212;
    public static final int RENDERER_LENGTH = 213;
    public static final int RENDERER_SURFACE = 214;
    public static final int RENDERER_VOLUME = 215;
    public static final int RENDERER_MASS = 216;
    public static final int RENDERER_WEIGTH_GROSS = 217;
    public static final int RENDERER_WEIGHT_DELIVERY = 218;
    
    public static final int RENDERER_BASE_EQUIVALENCE = 301;
    
    public static final int RENDERER_DATE = 401;
    public static final int RENDERER_DATETIME = 402;
    public static final int RENDERER_TIME = 403;
    
    public static final int RENDERER_DEFAULT_COLOR_BLUEDARK = 501;

    private java.text.DecimalFormat moSimpleIntegerFormat;
    private java.text.DecimalFormat moNumberLongFormat;
    private java.text.DecimalFormat moNumberDoubleFormat;
    private java.text.DecimalFormat moYearFormat;
    private java.text.DecimalFormat moMonthFormat;
    private java.text.DecimalFormat moRecordNumberFormat;

    private java.text.DecimalFormat moDecimalsValueFormat;
    private java.text.DecimalFormat moDecimalsValueUnitaryFormat;
    private java.text.DecimalFormat moDecimalsValueUnitaryFormatFixed4;
    private java.text.DecimalFormat moDecimalsExchangeRateFormat;
    private java.text.DecimalFormat moDecimalsPercentageFormat;
    private java.text.DecimalFormat moDecimalsDiscountFormat;
    private java.text.DecimalFormat moDecimalsCurrencyFormat;
    private java.text.DecimalFormat moDecimalsCurrencyLocalFormat;
    private java.text.DecimalFormat moDecimalsQuantityFormat;
    private java.text.DecimalFormat moDecimalsUnitsContainedFormat;
    private java.text.DecimalFormat moDecimalsUnitsVirtualFormat;
    private java.text.DecimalFormat moDecimalsNetContentFormat;
    private java.text.DecimalFormat moDecimalsLengthFormat;
    private java.text.DecimalFormat moDecimalsSurfaceFormat;
    private java.text.DecimalFormat moDecimalsVolumeFormat;
    private java.text.DecimalFormat moDecimalsMassFormat;
    private java.text.DecimalFormat moDecimalsWeigthGrossFormat;
    private java.text.DecimalFormat moDecimalsWeightDeliveryFormat;

    private java.text.DecimalFormat moDecimalsBaseEquivalenceFormat;

    private java.text.SimpleDateFormat moDateFormat;
    private java.text.SimpleDateFormat moDateYearFormat;
    private java.text.SimpleDateFormat moDateYearMonthFormat;
    private java.text.SimpleDateFormat moDateTextFormat;

    private java.text.SimpleDateFormat moDatetimeFormat;
    private java.text.SimpleDateFormat moDatetimeZoneFormat;

    private java.text.SimpleDateFormat moTimeFormat;
    private java.text.SimpleDateFormat moTimeZoneFormat;

    private java.text.SimpleDateFormat moFileNameDatetimeFormat;

    private java.text.SimpleDateFormat moCsvDateFormat;
    private java.text.SimpleDateFormat moCsvDatetimeFormat;
    private java.text.SimpleDateFormat moCsvTimeFormat;

    private java.text.SimpleDateFormat moDbmsDateFormat;
    private java.text.SimpleDateFormat moDbmsDatetimeFormat;
    private java.text.SimpleDateFormat moDbmsTimeFormat;

    private erp.lib.table.STableCellRendererDefault moTableCellRendererDefault;
    private erp.lib.table.STableCellRendererBoolean moTableCellRendererBoolean;
    private erp.lib.table.STableCellRendererNumber moTableCellRendererSimpleInteger;
    private erp.lib.table.STableCellRendererNumber moTableCellRendererNumberLong;
    private erp.lib.table.STableCellRendererNumber moTableCellRendererNumberDouble;
    private erp.lib.table.STableCellRendererNumber moTableCellRendererYear;
    private erp.lib.table.STableCellRendererNumber moTableCellRendererMonth;
    private erp.lib.table.STableCellRendererNumber moTableCellRendererRecordNumber;
    private erp.lib.table.STableCellRendererStyle moTableCellRendererStyle;
    private erp.lib.table.STableCellRendererIcon moTableCellRendererIcon;

    private erp.lib.table.STableCellRendererNumber moTableCellRendererValue;
    private erp.lib.table.STableCellRendererNumber moTableCellRendererValueUnitary;
    private erp.lib.table.STableCellRendererNumber moTableCellRendererValueUnitaryFixed4;
    private erp.lib.table.STableCellRendererNumber moTableCellRendererExchangeRate;
    private erp.lib.table.STableCellRendererNumber moTableCellRendererPercentage;
    private erp.lib.table.STableCellRendererNumber moTableCellRendererDiscount;
    private erp.lib.table.STableCellRendererNumber moTableCellRendererCurrency;
    private erp.lib.table.STableCellRendererNumber moTableCellRendererCurrencyLocal;
    private erp.lib.table.STableCellRendererNumber moTableCellRendererQuantity;
    private erp.lib.table.STableCellRendererNumber moTableCellRendererUnitsContained;
    private erp.lib.table.STableCellRendererNumber moTableCellRendererUnitsVirtual;
    private erp.lib.table.STableCellRendererNumber moTableCellRendererNetContent;
    private erp.lib.table.STableCellRendererNumber moTableCellRendererLength;
    private erp.lib.table.STableCellRendererNumber moTableCellRendererSurface;
    private erp.lib.table.STableCellRendererNumber moTableCellRendererVolume;
    private erp.lib.table.STableCellRendererNumber moTableCellRendererMass;
    private erp.lib.table.STableCellRendererNumber moTableCellRendererWeigthGross;
    private erp.lib.table.STableCellRendererNumber moTableCellRendererWeightDelivery;

    private erp.lib.table.STableCellRendererNumber moTableCellRendererBaseEquivalence;

    private erp.lib.table.STableCellRendererDate moTableCellRendererDate;
    private erp.lib.table.STableCellRendererDate moTableCellRendererDatetime;
    private erp.lib.table.STableCellRendererDate moTableCellRendererTime;

    private erp.lib.table.STableCellRendererDefaultColor moTableCellRendererDefaultColorBlueDark;

    private void createTableCellRenderers() {
        moTableCellRendererDefault = new STableCellRendererDefault();
        moTableCellRendererBoolean = new STableCellRendererBoolean();
        moTableCellRendererSimpleInteger = new STableCellRendererNumber(moSimpleIntegerFormat);
        moTableCellRendererNumberLong = new STableCellRendererNumber(moNumberLongFormat);
        moTableCellRendererNumberDouble = new STableCellRendererNumber(moNumberDoubleFormat);
        moTableCellRendererYear = new STableCellRendererNumber(moYearFormat);
        moTableCellRendererMonth = new STableCellRendererNumber(moMonthFormat);
        moTableCellRendererRecordNumber = new STableCellRendererNumber(moRecordNumberFormat);
        moTableCellRendererStyle = new STableCellRendererStyle();
        moTableCellRendererIcon = new STableCellRendererIcon();

        moTableCellRendererValue = new STableCellRendererNumber(moDecimalsValueFormat);
        moTableCellRendererValueUnitary = new STableCellRendererNumber(moDecimalsValueUnitaryFormat);
        moTableCellRendererValueUnitaryFixed4 = new STableCellRendererNumber(moDecimalsValueUnitaryFormatFixed4);
        moTableCellRendererExchangeRate = new STableCellRendererNumber(moDecimalsExchangeRateFormat);
        moTableCellRendererPercentage = new STableCellRendererNumber(moDecimalsPercentageFormat);
        moTableCellRendererDiscount = new STableCellRendererNumber(moDecimalsDiscountFormat);
        moTableCellRendererCurrency = new STableCellRendererNumber(moDecimalsCurrencyFormat);
        moTableCellRendererCurrencyLocal = new STableCellRendererNumber(moDecimalsCurrencyLocalFormat);
        moTableCellRendererQuantity = new STableCellRendererNumber(moDecimalsQuantityFormat);
        moTableCellRendererUnitsContained = new STableCellRendererNumber(moDecimalsUnitsContainedFormat);
        moTableCellRendererUnitsVirtual = new STableCellRendererNumber(moDecimalsUnitsVirtualFormat);
        moTableCellRendererNetContent = new STableCellRendererNumber(moDecimalsNetContentFormat);
        moTableCellRendererLength = new STableCellRendererNumber(moDecimalsLengthFormat);
        moTableCellRendererSurface = new STableCellRendererNumber(moDecimalsSurfaceFormat);
        moTableCellRendererVolume = new STableCellRendererNumber(moDecimalsVolumeFormat);
        moTableCellRendererMass = new STableCellRendererNumber(moDecimalsMassFormat);
        moTableCellRendererWeigthGross = new STableCellRendererNumber(moDecimalsWeigthGrossFormat);
        moTableCellRendererWeightDelivery = new STableCellRendererNumber(moDecimalsWeightDeliveryFormat);

        moTableCellRendererBaseEquivalence = new STableCellRendererNumber(moDecimalsBaseEquivalenceFormat);

        moTableCellRendererDate = new STableCellRendererDate(moDateFormat);
        moTableCellRendererDatetime = new STableCellRendererDate(moDatetimeFormat);
        moTableCellRendererTime = new STableCellRendererDate(moTimeFormat);

        moTableCellRendererDefaultColorBlueDark = new STableCellRendererDefaultColor(Color.BLUE.darker());
    }

    public SFormatters() {
        moSimpleIntegerFormat = new DecimalFormat("#0");
        moNumberLongFormat = new DecimalFormat("#,##0");
        moNumberDoubleFormat = new DecimalFormat("#,##0.00");
        moYearFormat = new DecimalFormat("0000");
        moMonthFormat = new DecimalFormat("00");
        moRecordNumberFormat = new DecimalFormat(SLibUtilities.textRepeat("0", SDataConstantsSys.NUM_LEN_FIN_REC));

        moDecimalsValueFormat = new DecimalFormat("#,##0");
        moDecimalsValueUnitaryFormat = new DecimalFormat("#,##0");
        moDecimalsValueUnitaryFormatFixed4 = new DecimalFormat("#,##0.0000");
        moDecimalsExchangeRateFormat = new DecimalFormat("#,##0");
        moDecimalsPercentageFormat = new DecimalFormat("#,##0");
        moDecimalsDiscountFormat = new DecimalFormat("#,##0");
        moDecimalsCurrencyFormat = new DecimalFormat("$ #,##0");
        moDecimalsCurrencyLocalFormat = new DecimalFormat("$ #,##0");

        moDecimalsQuantityFormat = new DecimalFormat("#,##0");
        moDecimalsUnitsContainedFormat = new DecimalFormat("#,##0");
        moDecimalsUnitsVirtualFormat = new DecimalFormat("#,##0");
        moDecimalsNetContentFormat = new DecimalFormat("#,##0");
        moDecimalsLengthFormat = new DecimalFormat("#,##0");
        moDecimalsSurfaceFormat = new DecimalFormat("#,##0");
        moDecimalsVolumeFormat = new DecimalFormat("#,##0");
        moDecimalsMassFormat = new DecimalFormat("#,##0");
        moDecimalsWeigthGrossFormat = new DecimalFormat("#,##0");
        moDecimalsWeightDeliveryFormat = new DecimalFormat("#,##0");

        moDecimalsBaseEquivalenceFormat = new DecimalFormat("#,##0." + SLibUtilities.textRepeat("0", 12));

        moDateFormat = new java.text.SimpleDateFormat("dd/MM/yyyy");
        moDateTextFormat = new java.text.SimpleDateFormat("dd MMMM yyyy", new Locale("es_MX")); // XXX improve this, it should not be a fixed setting! (sflores, 2015-12-17)

        moDateYearFormat = new java.text.SimpleDateFormat("yyyy");

        moDateYearMonthFormat = new java.text.SimpleDateFormat("MM/yyyy");

        moDatetimeFormat = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        moDatetimeZoneFormat = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss Z");

        moTimeFormat = new java.text.SimpleDateFormat("HH:mm:ss");
        moTimeZoneFormat = new java.text.SimpleDateFormat("HH:mm:ss Z");

        moFileNameDatetimeFormat = new java.text.SimpleDateFormat("yyyyMMdd HHmmss");

        moCsvDateFormat = new java.text.SimpleDateFormat("yyyy/MM/dd");
        moCsvDatetimeFormat = new java.text.SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        moCsvTimeFormat = new java.text.SimpleDateFormat("hh:mm:ss");

        moDbmsDateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
        moDbmsDatetimeFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        moDbmsTimeFormat = new java.text.SimpleDateFormat("HH:mm:ss");

        createTableCellRenderers();
    }

    public SFormatters(erp.mcfg.data.SDataParamsErp params) {
        moSimpleIntegerFormat = new DecimalFormat("#0");
        moNumberLongFormat = new DecimalFormat("#,##0");
        moNumberDoubleFormat = new DecimalFormat("#,##0.00");
        moYearFormat = new DecimalFormat("0000");
        moMonthFormat = new DecimalFormat("00");
        moRecordNumberFormat = new DecimalFormat(SLibUtilities.textRepeat("0", SDataConstantsSys.NUM_LEN_FIN_REC));

        moDecimalsValueFormat = new DecimalFormat("#,##0" + (params.getDecimalsValue() == 0 ? "" : "." + SLibUtilities.textRepeat("0", params.getDecimalsValue())));
        moDecimalsValueUnitaryFormat = new DecimalFormat("#,##0" + (params.getDecimalsValueUnitary() == 0 ? "" : "." + SLibUtilities.textRepeat("0", params.getDecimalsValueUnitary())));
        moDecimalsValueUnitaryFormatFixed4 = new DecimalFormat("#,##0.0000");
        moDecimalsExchangeRateFormat = new DecimalFormat("#,##0" + (params.getDecimalsExchangeRate() == 0 ? "" : "." + SLibUtilities.textRepeat("0", params.getDecimalsExchangeRate())));
        moDecimalsPercentageFormat = new DecimalFormat("#,##0" + (params.getDecimalsPercentage() == 0 ? "" : "." + SLibUtilities.textRepeat("0", params.getDecimalsPercentage())) + "%");
        moDecimalsDiscountFormat = new DecimalFormat("#,##0" + (params.getDecimalsDiscount() == 0 ? "" : "." + SLibUtilities.textRepeat("0", params.getDecimalsDiscount())));
        moDecimalsCurrencyFormat = new DecimalFormat("$ #,##0" + (params.getDecimalsValue() == 0 ? "" : "." + SLibUtilities.textRepeat("0", params.getDecimalsValue())));
        moDecimalsCurrencyLocalFormat = new DecimalFormat("$ #,##0" + (params.getDecimalsValue() == 0 ? "" : "." + SLibUtilities.textRepeat("0", params.getDecimalsValue())) + " '" + params.getDbmsDataCurrency().getKey() + "'");

        moDecimalsQuantityFormat = new DecimalFormat("#,##0" + (params.getDecimalsQuantity() == 0 ? "" : "." + SLibUtilities.textRepeat("0", params.getDecimalsQuantity())));
        moDecimalsUnitsContainedFormat = new DecimalFormat("#,##0" + (params.getDecimalsUnitsContained() == 0 ? "" : "." + SLibUtilities.textRepeat("0", params.getDecimalsUnitsContained())));
        moDecimalsUnitsVirtualFormat = new DecimalFormat("#,##0" + (params.getDecimalsUnitsVirtual() == 0 ? "" : "." + SLibUtilities.textRepeat("0", params.getDecimalsUnitsVirtual())));
        moDecimalsNetContentFormat = new DecimalFormat("#,##0" + (params.getDecimalsNetContent() == 0 ? "" : "." + SLibUtilities.textRepeat("0", params.getDecimalsNetContent())));
        moDecimalsLengthFormat = new DecimalFormat("#,##0" + (params.getDecimalsLength() == 0 ? "" : "." + SLibUtilities.textRepeat("0", params.getDecimalsLength())));
        moDecimalsSurfaceFormat = new DecimalFormat("#,##0" + (params.getDecimalsSurface() == 0 ? "" : "." + SLibUtilities.textRepeat("0", params.getDecimalsSurface())));
        moDecimalsVolumeFormat = new DecimalFormat("#,##0" + (params.getDecimalsVolume() == 0 ? "" : "." + SLibUtilities.textRepeat("0", params.getDecimalsVolume())));
        moDecimalsMassFormat = new DecimalFormat("#,##0" + (params.getDecimalsMass() == 0 ? "" : "." + SLibUtilities.textRepeat("0", params.getDecimalsMass())));
        moDecimalsWeigthGrossFormat = new DecimalFormat("#,##0" + (params.getDecimalsWeigthGross() == 0 ? "" : "." + SLibUtilities.textRepeat("0", params.getDecimalsWeigthGross())));
        moDecimalsWeightDeliveryFormat = new DecimalFormat("#,##0" + (params.getDecimalsWeightDelivery() == 0 ? "" : "." + SLibUtilities.textRepeat("0", params.getDecimalsWeightDelivery())));

        moDecimalsBaseEquivalenceFormat = new DecimalFormat("#,##0." + SLibUtilities.textRepeat("0", 12));

        switch (params.getFkFormatDateTypeId()) {
            case SDataConstantsSys.CFGS_TP_FMT_D_YYYY_MM_DD:
                moDateFormat = new java.text.SimpleDateFormat("yyyy/MM/dd");
                moDateTextFormat = new java.text.SimpleDateFormat("yyyy MMMM dd");
                break;
            case SDataConstantsSys.CFGS_TP_FMT_D_DD_MM_YYYY:
                moDateFormat = new java.text.SimpleDateFormat("dd/MM/yyyy");
                moDateTextFormat = new java.text.SimpleDateFormat("dd MMMM yyyy");
                break;
            case SDataConstantsSys.CFGS_TP_FMT_D_MM_DD_YYYY:
                moDateFormat = new java.text.SimpleDateFormat("MM/dd/yyyy");
                moDateTextFormat = new java.text.SimpleDateFormat("MMMM dd yyyy");
                break;
            default:
                break;
        }

        moDateYearFormat = new java.text.SimpleDateFormat("yyyy");

        switch (params.getFkFormatDateTypeId()) {
            case SDataConstantsSys.CFGS_TP_FMT_D_YYYY_MM_DD:
                moDateYearMonthFormat = new java.text.SimpleDateFormat("yyyy/MM");
                break;
            case SDataConstantsSys.CFGS_TP_FMT_D_DD_MM_YYYY:
            case SDataConstantsSys.CFGS_TP_FMT_D_MM_DD_YYYY:
                moDateYearMonthFormat = new java.text.SimpleDateFormat("MM/yyyy");
                break;
            default:
                break;
        }

        switch (params.getFkFormatDatetimeTypeId()) {
            case SDataConstantsSys.CFGS_TP_FMT_DT_YYYY_MM_DD_HH_MM:
                moDatetimeFormat = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm");
                moDatetimeZoneFormat = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm Z");
                break;
            case SDataConstantsSys.CFGS_TP_FMT_DT_YYYY_MM_DD_HH_MM_AP:
                moDatetimeFormat = new java.text.SimpleDateFormat("yyyy/MM/dd hh:mm a");
                moDatetimeZoneFormat = new java.text.SimpleDateFormat("yyyy/MM/dd hh:mm a Z");
                break;
            case SDataConstantsSys.CFGS_TP_FMT_DT_YYYY_MM_DD_HH_MM_SS:
                moDatetimeFormat = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                moDatetimeZoneFormat = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss Z");
                break;
            case SDataConstantsSys.CFGS_TP_FMT_DT_YYYY_MM_DD_HH_MM_SS_AP:
                moDatetimeFormat = new java.text.SimpleDateFormat("yyyy/MM/dd hh:mm:ss a");
                moDatetimeZoneFormat = new java.text.SimpleDateFormat("yyyy/MM/dd hh:mm:ss a Z");
                break;
            case SDataConstantsSys.CFGS_TP_FMT_DT_DD_MM_YYYY_HH_MM:
                moDatetimeFormat = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
                moDatetimeZoneFormat = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm Z");
                break;
            case SDataConstantsSys.CFGS_TP_FMT_DT_DD_MM_YYYY_HH_MM_AP:
                moDatetimeFormat = new java.text.SimpleDateFormat("dd/MM/yyyy hh:mm a");
                moDatetimeZoneFormat = new java.text.SimpleDateFormat("dd/MM/yyyy hh:mm a Z");
                break;
            case SDataConstantsSys.CFGS_TP_FMT_DT_DD_MM_YYYY_HH_MM_SS:
                moDatetimeFormat = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                moDatetimeZoneFormat = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss Z");
                break;
            case SDataConstantsSys.CFGS_TP_FMT_DT_DD_MM_YYYY_HH_MM_SS_AP:
                moDatetimeFormat = new java.text.SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
                moDatetimeZoneFormat = new java.text.SimpleDateFormat("dd/MM/yyyy hh:mm:ss a Z");
                break;
            case SDataConstantsSys.CFGS_TP_FMT_DT_MM_DD_YYYY_HH_MM:
                moDatetimeFormat = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm");
                moDatetimeZoneFormat = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm Z");
                break;
            case SDataConstantsSys.CFGS_TP_FMT_DT_MM_DD_YYYY_HH_MM_AP:
                moDatetimeFormat = new java.text.SimpleDateFormat("MM/dd/yyyy hh:mm a");
                moDatetimeZoneFormat = new java.text.SimpleDateFormat("MM/dd/yyyy hh:mm a Z");
                break;
            case SDataConstantsSys.CFGS_TP_FMT_DT_MM_DD_YYYY_HH_MM_SS:
                moDatetimeFormat = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                moDatetimeZoneFormat = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss Z");
                break;
            case SDataConstantsSys.CFGS_TP_FMT_DT_MM_DD_YYYY_HH_MM_SS_AP:
                moDatetimeFormat = new java.text.SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
                moDatetimeZoneFormat = new java.text.SimpleDateFormat("MM/dd/yyyy hh:mm:ss a Z");
                break;
            default:
                break;
        }

        switch (params.getFkFormatTimeTypeId()) {
            case SDataConstantsSys.CFGS_TP_FMT_T_HH_MM:
                moTimeFormat = new java.text.SimpleDateFormat("HH:mm");
                moTimeZoneFormat = new java.text.SimpleDateFormat("HH:mm Z");
                break;
            case SDataConstantsSys.CFGS_TP_FMT_T_HH_MM_AP:
                moTimeFormat = new java.text.SimpleDateFormat("hh:mm a");
                moTimeZoneFormat = new java.text.SimpleDateFormat("hh:mm a Z");
                break;
            case SDataConstantsSys.CFGS_TP_FMT_T_HH_MM_SS:
                moTimeFormat = new java.text.SimpleDateFormat("HH:mm:ss");
                moTimeZoneFormat = new java.text.SimpleDateFormat("HH:mm:ss Z");
                break;
            case SDataConstantsSys.CFGS_TP_FMT_T_HH_MM_SS_AP:
                moTimeFormat = new java.text.SimpleDateFormat("hh:mm:ss a");
                moTimeZoneFormat = new java.text.SimpleDateFormat("hh:mm:ss a Z");
                break;
            default:
                break;
        }

        moFileNameDatetimeFormat = new java.text.SimpleDateFormat("yyyyMMdd HHmmss");

        moCsvDateFormat = new java.text.SimpleDateFormat("yyyy/MM/dd");
        moCsvDatetimeFormat = new java.text.SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        moCsvTimeFormat = new java.text.SimpleDateFormat("hh:mm:ss");

        switch (params.getFkDbmsTypeId()) {
            case SLibConstants.DBMS_MY_SQL:
                moDbmsDateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
                moDbmsDatetimeFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                moDbmsTimeFormat = new java.text.SimpleDateFormat("HH:mm:ss");
                break;
            case SLibConstants.DBMS_SQL_SERVER_2000:
            case SLibConstants.DBMS_SQL_SERVER_2005:
                moDbmsDateFormat = null;
                moDbmsDatetimeFormat = null;
                moDbmsTimeFormat = null;
                break;
            default:
                break;
        }

        createTableCellRenderers();
    }

    public java.text.DecimalFormat getSimpleIntegerFormat() { return moSimpleIntegerFormat; }
    public java.text.DecimalFormat getNumberLongFormat() { return moNumberLongFormat; }
    public java.text.DecimalFormat getNumberDoubleFormat() { return moNumberDoubleFormat; }
    public java.text.DecimalFormat getYearFormat() { return moYearFormat; }
    public java.text.DecimalFormat getMonthFormat() { return moMonthFormat; }
    public java.text.DecimalFormat getRecordNumberFormat() { return moRecordNumberFormat; }

    public java.text.DecimalFormat getDecimalsValueFormat() { return moDecimalsValueFormat; }
    public java.text.DecimalFormat getDecimalsValueUnitaryFormat() { return moDecimalsValueUnitaryFormat; }
    public java.text.DecimalFormat getDecimalsValueUnitaryFormatFixed4() { return moDecimalsValueUnitaryFormatFixed4; }
    public java.text.DecimalFormat getDecimalsExchangeRateFormat() { return moDecimalsExchangeRateFormat; }
    public java.text.DecimalFormat getDecimalsPercentageFormat() { return moDecimalsPercentageFormat; }
    public java.text.DecimalFormat getDecimalsDiscountFormat() { return moDecimalsDiscountFormat; }
    public java.text.DecimalFormat getDecimalsCurrencyFormat() { return moDecimalsCurrencyFormat; }
    public java.text.DecimalFormat getDecimalsCurrencyLocalFormat() { return moDecimalsCurrencyLocalFormat; }
    public java.text.DecimalFormat getDecimalsQuantityFormat() { return moDecimalsQuantityFormat; }
    public java.text.DecimalFormat getDecimalsUnitsContainedFormat() { return moDecimalsUnitsContainedFormat; }
    public java.text.DecimalFormat getDecimalsUnitsVirtualFormat() { return moDecimalsUnitsVirtualFormat; }
    public java.text.DecimalFormat getDecimalsNetContentFormat() { return moDecimalsNetContentFormat; }
    public java.text.DecimalFormat getDecimalsLengthFormat() { return moDecimalsLengthFormat; }
    public java.text.DecimalFormat getDecimalsSurfaceFormat() { return moDecimalsSurfaceFormat; }
    public java.text.DecimalFormat getDecimalsVolumeFormat() { return moDecimalsVolumeFormat; }
    public java.text.DecimalFormat getDecimalsMassFormat() { return moDecimalsMassFormat; }
    public java.text.DecimalFormat getDecimalsWeigthGrossFormat() { return moDecimalsWeigthGrossFormat; }
    public java.text.DecimalFormat getDecimalsWeightDeliveryFormat() { return moDecimalsWeightDeliveryFormat; }

    public java.text.DecimalFormat getDecimalsBaseEquivalenceFormat() { return moDecimalsBaseEquivalenceFormat; }

    public java.text.SimpleDateFormat getDateFormat() { return moDateFormat; }
    public java.text.SimpleDateFormat getDateYearFormat() { return moDateYearFormat; }
    public java.text.SimpleDateFormat getDateYearMonthFormat() { return moDateYearMonthFormat; }
    public java.text.SimpleDateFormat getDateTextFormat() { return moDateTextFormat; }

    public java.text.SimpleDateFormat getDatetimeFormat() { return moDatetimeFormat; }
    public java.text.SimpleDateFormat getDatetimeZoneFormat() { return moDatetimeZoneFormat; }

    public java.text.SimpleDateFormat getTimeFormat() { return moTimeFormat; }
    public java.text.SimpleDateFormat getTimeZoneFormat() { return moTimeZoneFormat; }

    public java.text.SimpleDateFormat getFileNameDatetimeFormat() { return moFileNameDatetimeFormat; }

    public java.text.SimpleDateFormat getCsvDateFormat() { return moCsvDateFormat; }
    public java.text.SimpleDateFormat getCsvDatetimeFormat() { return moCsvDatetimeFormat; }
    public java.text.SimpleDateFormat getCsvTimeFormat() { return moCsvTimeFormat; }

    public java.text.SimpleDateFormat getDbmsDateFormat() { return moDbmsDateFormat; }
    public java.text.SimpleDateFormat getDbmsDatetimeFormat() { return moDbmsDatetimeFormat; }
    public java.text.SimpleDateFormat getDbmsTimeFormat() { return moDbmsTimeFormat; }

    public erp.lib.table.STableCellRendererDefault getTableCellRendererDefault() { return moTableCellRendererDefault; }
    public erp.lib.table.STableCellRendererBoolean getTableCellRendererBoolean() { return moTableCellRendererBoolean; }
    public erp.lib.table.STableCellRendererNumber getTableCellRendererSimpleInteger() { return moTableCellRendererSimpleInteger; }
    public erp.lib.table.STableCellRendererNumber getTableCellRendererNumberLong() { return moTableCellRendererNumberLong; }
    public erp.lib.table.STableCellRendererNumber getTableCellRendererNumberDouble() { return moTableCellRendererNumberDouble; }
    public erp.lib.table.STableCellRendererNumber getTableCellRendererYear() { return moTableCellRendererYear; }
    public erp.lib.table.STableCellRendererNumber getTableCellRendererMonth() { return moTableCellRendererMonth; }
    public erp.lib.table.STableCellRendererNumber getTableCellRendererRecordNumber() { return moTableCellRendererRecordNumber; }
    public erp.lib.table.STableCellRendererStyle getTableCellRendererStyle() { return moTableCellRendererStyle; }
    public erp.lib.table.STableCellRendererIcon getTableCellRendererIcon() { return moTableCellRendererIcon; }

    public erp.lib.table.STableCellRendererNumber getTableCellRendererValue() { return moTableCellRendererValue; }
    public erp.lib.table.STableCellRendererNumber getTableCellRendererValueUnitary() { return moTableCellRendererValueUnitary; }
    public erp.lib.table.STableCellRendererNumber getTableCellRendererValueUnitaryFixed4() { return moTableCellRendererValueUnitaryFixed4; }
    public erp.lib.table.STableCellRendererNumber getTableCellRendererExchangeRate() { return moTableCellRendererExchangeRate; }
    public erp.lib.table.STableCellRendererNumber getTableCellRendererPercentage() { return moTableCellRendererPercentage; }
    public erp.lib.table.STableCellRendererNumber getTableCellRendererDiscount() { return moTableCellRendererDiscount; }
    public erp.lib.table.STableCellRendererNumber getTableCellRendererCurrency() { return moTableCellRendererCurrency; }
    public erp.lib.table.STableCellRendererNumber getTableCellRendererCurrencyLocal() { return moTableCellRendererCurrencyLocal; }
    public erp.lib.table.STableCellRendererNumber getTableCellRendererQuantity() { return moTableCellRendererQuantity; }
    public erp.lib.table.STableCellRendererNumber getTableCellRendererUnitsContained() { return moTableCellRendererUnitsContained; }
    public erp.lib.table.STableCellRendererNumber getTableCellRendererUnitsVirtual() { return moTableCellRendererUnitsVirtual; }
    public erp.lib.table.STableCellRendererNumber getTableCellRendererNetContent() { return moTableCellRendererNetContent; }
    public erp.lib.table.STableCellRendererNumber getTableCellRendererLength() { return moTableCellRendererLength; }
    public erp.lib.table.STableCellRendererNumber getTableCellRendererSurface() { return moTableCellRendererSurface; }
    public erp.lib.table.STableCellRendererNumber getTableCellRendererVolume() { return moTableCellRendererVolume; }
    public erp.lib.table.STableCellRendererNumber getTableCellRendererMass() { return moTableCellRendererMass; }
    public erp.lib.table.STableCellRendererNumber getTableCellRendererWeigthGross() { return moTableCellRendererWeigthGross; }
    public erp.lib.table.STableCellRendererNumber getTableCellRendererWeightDelivery() { return moTableCellRendererWeightDelivery; }

    public erp.lib.table.STableCellRendererNumber getTableCellRendererBaseEquivalence() { return moTableCellRendererBaseEquivalence; }

    public erp.lib.table.STableCellRendererDate getTableCellRendererDate() { return moTableCellRendererDate; }
    public erp.lib.table.STableCellRendererDate getTableCellRendererDatetime() { return moTableCellRendererDatetime; }
    public erp.lib.table.STableCellRendererDate getTableCellRendererTime() { return moTableCellRendererTime; }

    public erp.lib.table.STableCellRendererDefaultColor getTableCellRendererDefaultColorBlueDark() { return moTableCellRendererDefaultColorBlueDark; };

    /**
     * Create again server side created table cell renderers, in order to use client specific format.
     */
    public void redefineTableCellRenderers() {
        moTableCellRendererDefault = new STableCellRendererDefault();
        moTableCellRendererBoolean = new STableCellRendererBoolean();
        moTableCellRendererSimpleInteger = new STableCellRendererNumber(moSimpleIntegerFormat);
        moTableCellRendererNumberLong = new STableCellRendererNumber(moNumberLongFormat);
        moTableCellRendererNumberDouble = new STableCellRendererNumber(moNumberDoubleFormat);
        moTableCellRendererStyle = new STableCellRendererStyle();
        moTableCellRendererIcon = new STableCellRendererIcon();

        moTableCellRendererDefaultColorBlueDark = new STableCellRendererDefaultColor(Color.BLUE.darker());
    }
    
    public javax.swing.table.DefaultTableCellRenderer getCellRenderer(final int cellRendererType) {
        javax.swing.table.DefaultTableCellRenderer cellRenderer = null;
        
        switch(cellRendererType) {
            case RENDERER_SIMPLE_INTEGER:
                cellRenderer = moTableCellRendererSimpleInteger;
                break;
            case RENDERER_NUMBER_LONG:
                cellRenderer = moTableCellRendererNumberLong;
                break;
            case RENDERER_NUMBER_DOUBLE:
                cellRenderer = moTableCellRendererNumberDouble;
                break;
            case RENDERER_YEAR:
                cellRenderer = moTableCellRendererYear;
                break;
            case RENDERER_MONTH:
                cellRenderer = moTableCellRendererMonth;
                break;
            case RENDERER_RECORD_NUMBER:
                cellRenderer = moTableCellRendererRecordNumber;
                break;
            case RENDERER_STYLE:
                cellRenderer = moTableCellRendererStyle;
                break;
            case RENDERER_ICON:
                cellRenderer = moTableCellRendererIcon;
                break;
            case RENDERER_VALUE:
                cellRenderer = moTableCellRendererValue;
                break;
            case RENDERER_VALUE_UNITARY:
                cellRenderer = moTableCellRendererValueUnitary;
                break;
            case RENDERER_VALUE_UNITARY_FIXED4:
                cellRenderer = moTableCellRendererValueUnitaryFixed4;
                break;
            case RENDERER_EXCHANGE_RATE:
                cellRenderer = moTableCellRendererExchangeRate;
                break;
            case RENDERER_PERCENTAGE:
                cellRenderer = moTableCellRendererPercentage;
                break;
            case RENDERER_DISCOUNT:
                cellRenderer = moTableCellRendererDiscount;
                break;
            case RENDERER_CURRENCY:
                cellRenderer = moTableCellRendererCurrency;
                break;
            case RENDERER_CURRENCY_LOCAL:
                cellRenderer = moTableCellRendererCurrencyLocal;
                break;
            case RENDERER_QUANTITY:
                cellRenderer = moTableCellRendererQuantity;
                break;
            case RENDERER_UNITS_CONTAINED:
                cellRenderer = moTableCellRendererUnitsContained;
                break;
            case RENDERER_UNITS_VIRTUAL:
                cellRenderer = moTableCellRendererUnitsVirtual;
                break;
            case RENDERER_NET_CONTENT:
                cellRenderer = moTableCellRendererNetContent;
                break;
            case RENDERER_LENGTH:
                cellRenderer = moTableCellRendererLength;
                break;
            case RENDERER_SURFACE:
                cellRenderer = moTableCellRendererSurface;
                break;
            case RENDERER_VOLUME:
                cellRenderer = moTableCellRendererVolume;
                break;
            case RENDERER_MASS:
                cellRenderer = moTableCellRendererMass;
                break;
            case RENDERER_WEIGTH_GROSS:
                cellRenderer = moTableCellRendererWeigthGross;
                break;
            case RENDERER_WEIGHT_DELIVERY:
                cellRenderer = moTableCellRendererWeightDelivery;
                break;
            case RENDERER_BASE_EQUIVALENCE:
                cellRenderer = moTableCellRendererBaseEquivalence;
                break;
            case RENDERER_DATE:
                cellRenderer = moTableCellRendererDate;
                break;
            case RENDERER_DATETIME:
                cellRenderer = moTableCellRendererDatetime;
                break;
            case RENDERER_TIME:
                cellRenderer = moTableCellRendererTime;
                break;
            case RENDERER_DEFAULT_COLOR_BLUEDARK:
                cellRenderer = moTableCellRendererDefaultColorBlueDark;
                break;
            default:
        }
        
        return cellRenderer;
    }
    
    public int getCellRendererType(javax.swing.table.TableCellRenderer cellRenderer) {
        int cellRendererType = 0;
        
        if (cellRenderer == moTableCellRendererSimpleInteger) {
            cellRendererType = RENDERER_SIMPLE_INTEGER;
        }
        else if (cellRenderer == moTableCellRendererNumberLong) {
            cellRendererType = RENDERER_NUMBER_LONG;
        }
        else if (cellRenderer == moTableCellRendererNumberDouble) {
            cellRendererType = RENDERER_NUMBER_DOUBLE;
        }
        else if (cellRenderer == moTableCellRendererYear) {
            cellRendererType = RENDERER_YEAR;
        }
        else if (cellRenderer == moTableCellRendererMonth) {
            cellRendererType = RENDERER_MONTH;
        }
        else if (cellRenderer == moTableCellRendererRecordNumber) {
            cellRendererType = RENDERER_RECORD_NUMBER;
        }
        else if (cellRenderer == moTableCellRendererStyle) {
            cellRendererType = RENDERER_STYLE;
        }
        else if (cellRenderer == moTableCellRendererIcon) {
            cellRendererType = RENDERER_ICON;
        }
        else if (cellRenderer == moTableCellRendererValue) {
            cellRendererType = RENDERER_VALUE;
        }
        else if (cellRenderer == moTableCellRendererValueUnitary) {
            cellRendererType = RENDERER_VALUE_UNITARY;
        }
        else if (cellRenderer == moTableCellRendererValueUnitaryFixed4) {
            cellRendererType = RENDERER_VALUE_UNITARY_FIXED4;
        }
        else if (cellRenderer == moTableCellRendererExchangeRate) {
            cellRendererType = RENDERER_EXCHANGE_RATE;
        }
        else if (cellRenderer == moTableCellRendererPercentage) {
            cellRendererType = RENDERER_PERCENTAGE;
        }
        else if (cellRenderer == moTableCellRendererDiscount) {
            cellRendererType = RENDERER_DISCOUNT;
        }
        else if (cellRenderer == moTableCellRendererCurrency) {
            cellRendererType = RENDERER_CURRENCY;
        }
        else if (cellRenderer == moTableCellRendererCurrencyLocal) {
            cellRendererType = RENDERER_CURRENCY_LOCAL;
        }
        else if (cellRenderer == moTableCellRendererQuantity) {
            cellRendererType = RENDERER_QUANTITY;
        }
        else if (cellRenderer == moTableCellRendererUnitsContained) {
            cellRendererType = RENDERER_UNITS_CONTAINED;
        }
        else if (cellRenderer == moTableCellRendererUnitsVirtual) {
            cellRendererType = RENDERER_UNITS_VIRTUAL;
        }
        else if (cellRenderer == moTableCellRendererNetContent) {
            cellRendererType = RENDERER_NET_CONTENT;
        }
        else if (cellRenderer == moTableCellRendererLength) {
            cellRendererType = RENDERER_LENGTH;
        }
        else if (cellRenderer == moTableCellRendererSurface) {
            cellRendererType = RENDERER_SURFACE;
        }
        else if (cellRenderer == moTableCellRendererVolume) {
            cellRendererType = RENDERER_VOLUME;
        }
        else if (cellRenderer == moTableCellRendererMass) {
            cellRendererType = RENDERER_MASS;
        }
        else if (cellRenderer == moTableCellRendererWeigthGross) {
            cellRendererType = RENDERER_WEIGTH_GROSS;
        }
        else if (cellRenderer == moTableCellRendererWeightDelivery) {
            cellRendererType = RENDERER_WEIGHT_DELIVERY;
        }
        else if (cellRenderer == moTableCellRendererBaseEquivalence) {
            cellRendererType = RENDERER_BASE_EQUIVALENCE;
        }
        else if (cellRenderer == moTableCellRendererDate) {
            cellRendererType = RENDERER_DATE;
        }
        else if (cellRenderer == moTableCellRendererDatetime) {
            cellRendererType = RENDERER_DATETIME;
        }
        else if (cellRenderer == moTableCellRendererTime) {
            cellRendererType = RENDERER_TIME;
        }
        else if (cellRenderer == moTableCellRendererDefaultColorBlueDark) {
            cellRendererType = RENDERER_DEFAULT_COLOR_BLUEDARK;
        }
        
        return cellRendererType;
    }
}
