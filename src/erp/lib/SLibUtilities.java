/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.lib;

import erp.lib.form.SFormField;
import erp.lib.form.SFormValidation;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sa.lib.SLibTimeUtils;

/**
 *
 * @author Sergio Flores
 */
public abstract class SLibUtilities {

    private static final double MAX_VALUE = 999999999.99;    // 999,999,999.99

    // Numbers in Spanish:

    private static final java.lang.String MONEY_SPA_ZERO = "CERO";
    private static final java.lang.String MONEY_SPA_HUNDRED = "CIEN";
    private static final java.lang.String MONEY_SPA_THOUSAND_SNG = "MIL";
    private static final java.lang.String MONEY_SPA_THOUSAND_PLR = "MIL";
    private static final java.lang.String MONEY_SPA_MILLION_SNG = "MILLÓN";
    private static final java.lang.String MONEY_SPA_MILLION_PLR = "MILLONES";

    private static final java.lang.String[] mastrSpaUnits00 = {
    "UN", "DOS", "TRES", "CUATRO", "CINCO", "SEIS", "SIETE", "OCHO", "NUEVE"};

    private static final java.lang.String[] mastrSpaUnits10 = {
    "ONCE", "DOCE", "TRECE", "CATORCE", "QUINCE", "DIECISÉIS", "DIECISIETE", "DIECIOCHO", "DIECINUEVE"};

    private static final java.lang.String[] mastrSpaUnits20 = {
    "VEINTIÚN", "VEINTIDÓS", "VEINTITRÉS", "VEINTICUATRO", "VEINTICINCO", "VEINTISÉIS", "VEINTISIETE", "VEINTIOCHO", "VEINTINUEVE"};

    private static final java.lang.String[] mastrSpaTens = {
    "DIEZ", "VEINTE", "TREINTA", "CUARENTA", "CINCUENTA", "SESENTA", "SETENTA", "OCHENTA", "NOVENTA"};

    private static final java.lang.String[] mastrSpaHundreds = {
    "CIENTO", "DOSCIENTOS", "TRESCIENTOS", "CUATROCIENTOS", "QUINIENTOS", "SEISCIENTOS", "SETECIENTOS", "OCHOCIENTOS", "NOVECIENTOS"};

    // Numbers in English:

    private static final java.lang.String MONEY_ENG_ZERO = "ZERO";
    private static final java.lang.String MONEY_ENG_HUNDRED = "HUNDRED";
    private static final java.lang.String MONEY_ENG_THOUSAND = "THOUSAND";
    private static final java.lang.String MONEY_ENG_MILLION = "MILLION";

    private static final java.lang.String[] mastrEngUnits00 = {
    "ONE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE"};

    private static final java.lang.String[] mastrEngUnits10 = {
    "ELEVEN", "TWELVE", "THIRTEEN", "FOURTEEN", "FIFTEEN", "SIXTEEN", "SEVENTEEN", "EIGHTTEEN", "NINETEEN"};

    private static final java.lang.String[] mastrEngTens = {
    "TEN", "TWENTY", "THIRTY", "FORTY", "FIFTY", "SIXTY", "SEVENTY", "EIGHTY", "NINETY"};

    public static boolean compareKeys(int[] panKeyA, int[] panKeyB) {
        if (panKeyA == null && panKeyB != null || panKeyA != null && panKeyB == null) {
            return false;
        }
        else if (panKeyA.length == panKeyB.length) {
            for (int i = 0; i < panKeyA.length; i++) {
                if (panKeyA[i] != panKeyB[i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static boolean compareKeys(java.lang.Object paoKeyA, java.lang.Object paoKeyB) {
        Object[] aoKeyA = null;
        Object[] aoKeyB = null;

        if (paoKeyA == null && paoKeyB != null || paoKeyA != null && paoKeyB == null) {
            return false;
        }
        if (paoKeyA.getClass() == int[].class && paoKeyB.getClass() == int[].class) {
            return compareKeys((int[]) paoKeyA, (int[]) paoKeyB);
        }
        else if (paoKeyA.getClass() == Object[].class && paoKeyB.getClass() == Object[].class ) {
            aoKeyA = (Object[]) paoKeyA;
            aoKeyB = (Object[]) paoKeyB;

            if (aoKeyA.length == aoKeyB.length) {
                for (int i = 0; i < aoKeyA.length; i++) {
                    if (aoKeyA[i] instanceof Number && aoKeyB[i] instanceof Number) {
                        if (((Number) aoKeyA[i]).doubleValue() != ((Number) aoKeyB[i]).doubleValue()) {
                            return false;
                        }
                    }
                    else if (aoKeyA[i] instanceof String && aoKeyB[i] instanceof String) {
                        if (((String) aoKeyA[i]).compareTo((String) aoKeyB[i]) != 0) {
                            return false;
                        }
                    }
                    else if (aoKeyA[i] instanceof java.util.Date && aoKeyB[i] instanceof java.util.Date) {
                        if (((java.util.Date) aoKeyA[i]).compareTo((java.util.Date) aoKeyB[i]) != 0) {
                            return false;
                        }
                    }
                    else if (aoKeyA[i] != aoKeyB[i]) {
                        return false;
                    }
                }
                return true;
            }
        }

        return false;
    }

    public static boolean belongsTo(int val, int[] valArray) {
        boolean belongs = false;

        for (int curVal : valArray) {
            if (val == curVal) {
                belongs = true;
                break;
            }
        }

        return belongs;
    }

    public static boolean belongsTo(int[] key, int[][] keyArray) {
        boolean belongs = false;

        for (int[] curKey : keyArray) {
            if (compareKeys(key, curKey)) {
                belongs = true;
                break;
            }
        }

        return belongs;
    }

    public static SFormValidation validateDateRange(final SFormField fieldDateStart, final SFormField fieldDateEnd) {
        Date dateStart = fieldDateStart.getDate();
        Date dateEnd = fieldDateEnd.getDate();
        SFormValidation validation = new SFormValidation();

        if (dateStart == null) {
            validation.setMessage("No se ha especificado un valor para la fecha inicial.");
            validation.setComponent(fieldDateStart.getComponent());
        }
        else if (dateEnd == null) {
            validation.setMessage("No se ha especificado un valor para la fecha final.");
            validation.setComponent(fieldDateEnd.getComponent());
        }
        else if (SLibTimeUtils.digestYear(dateStart)[0] != SLibTimeUtils.digestYear(dateEnd)[0]) {
            validation.setMessage("El año de las fechas inicial y final debe ser el mismo.");
            validation.setComponent(fieldDateStart.getComponent());
        }
        else if (dateEnd.before(dateStart)) {
            validation.setMessage("La fecha inicial debe ser anterior a la fecha final.");
            validation.setComponent(fieldDateStart.getComponent());
        }

        return validation;
    }

    public static int parseInt(java.lang.String text) {
        int value = 0;

        try {
            value = java.lang.Integer.parseInt(text.trim().replaceAll(",", "").replaceAll("%", ""));
        }
        catch (java.lang.NumberFormatException e) { }

        return value;
    }

    public static long parseLong(java.lang.String text) {
        long value = 0;

        try {
            value = java.lang.Long.parseLong(text.trim().replaceAll(",", "").replaceAll("%", ""));
        }
        catch (java.lang.NumberFormatException e) { }

        return value;
    }

    public static float parseFloat(java.lang.String text) {
        float value = 0;

        try {
            value = java.lang.Float.parseFloat(text.trim().replaceAll(",", "").replaceAll("%", ""));
        }
        catch (java.lang.NumberFormatException e) { }

        return value;
    }

    public static double parseDouble(java.lang.String text) {
        double value = 0;

        try {
            value = java.lang.Double.parseDouble(text.trim().replaceAll(",", "").replaceAll("%", ""));
        }
        catch (java.lang.NumberFormatException e) { }

        return value;
    }

    public static double round(double value, int decimals) {
        return Math.round(value * Math.pow(10, decimals)) / Math.pow(10, decimals);
    }

    public static java.lang.String textRepeat(java.lang.String text, int times) {
        java.lang.String repeatedText = "";

        for (int i = 0; i < times; i++) {
            repeatedText += text;
        }

        return repeatedText;
    }

    public static java.lang.String textTrim(java.lang.String text) {
        java.lang.String trimmedText = text.trim();

        while(trimmedText.indexOf("  ") != -1) {
            trimmedText = trimmedText.replaceAll("  ", " ");
        }

        return trimmedText;
    }

    public static java.lang.String textLeft(java.lang.String text, int count) {
        return text.length() <= count ? text : text.substring(0, count);
    }

    public static java.lang.String textRight(java.lang.String text, int count) {
        return text.length() <= count ? text : text.substring(text.length() - count, text.length());
    }

    public static java.lang.String textToAscii(java.lang.String text) {
        String ascii = textTrim(text);

        ascii = ascii.replaceAll("á", "a");
        ascii = ascii.replaceAll("é", "e");
        ascii = ascii.replaceAll("í", "i");
        ascii = ascii.replaceAll("ó", "o");
        ascii = ascii.replaceAll("ú", "u");
        ascii = ascii.replaceAll("Á", "A");
        ascii = ascii.replaceAll("É", "E");
        ascii = ascii.replaceAll("Í", "I");
        ascii = ascii.replaceAll("Ó", "O");
        ascii = ascii.replaceAll("Ú", "U");
        ascii = ascii.replaceAll("ä", "a");
        ascii = ascii.replaceAll("ë", "e");
        ascii = ascii.replaceAll("ï", "i");
        ascii = ascii.replaceAll("ö", "o");
        ascii = ascii.replaceAll("ü", "u");
        ascii = ascii.replaceAll("Ä", "A");
        ascii = ascii.replaceAll("Ë", "E");
        ascii = ascii.replaceAll("Ï", "I");
        ascii = ascii.replaceAll("Ö", "O");
        ascii = ascii.replaceAll("Ü", "U");
        ascii = ascii.replaceAll("à", "a");
        ascii = ascii.replaceAll("è", "e");
        ascii = ascii.replaceAll("ì", "i");
        ascii = ascii.replaceAll("ò", "o");
        ascii = ascii.replaceAll("ù", "u");
        ascii = ascii.replaceAll("À", "A");
        ascii = ascii.replaceAll("È", "E");
        ascii = ascii.replaceAll("Ì", "I");
        ascii = ascii.replaceAll("Ò", "O");
        ascii = ascii.replaceAll("Ù", "U");
        ascii = ascii.replaceAll("â", "a");
        ascii = ascii.replaceAll("ê", "e");
        ascii = ascii.replaceAll("î", "i");
        ascii = ascii.replaceAll("ô", "o");
        ascii = ascii.replaceAll("û", "u");
        ascii = ascii.replaceAll("Â", "A");
        ascii = ascii.replaceAll("Ê", "E");
        ascii = ascii.replaceAll("Î", "I");
        ascii = ascii.replaceAll("Ô", "O");
        ascii = ascii.replaceAll("Û", "U");
        ascii = ascii.replaceAll("ñ", "n");
        ascii = ascii.replaceAll("Ñ", "N");

        return ascii;
    }

    public static String textToAlphanumeric(java.lang.String text) {
        Pattern pattern = null;
        Matcher matcher = null;
        StringBuffer stringBuffer = null;

        String alphanumeric = textToAscii(text);

        pattern = Pattern.compile("[^A-Za-z0-9\\s]+");
        matcher = pattern.matcher(alphanumeric);
        stringBuffer = new StringBuffer();

        if (matcher.find()) {
            do {
                matcher.appendReplacement(stringBuffer, "");
            }
            while (matcher.find());
        }

        matcher.appendTail(stringBuffer);

        alphanumeric = stringBuffer.toString();

        return alphanumeric;
    }

    public static String textProperCase(final java.lang.String text) {
        boolean spaceFound = true;
        char[] charArray = textTrim(text).toLowerCase().toCharArray();

        for (int i = 0; i < charArray.length; i++) {
            if (spaceFound) {
                charArray[i] = ("" + charArray[i]).toUpperCase().toCharArray()[0];
                spaceFound = false;
            }
            else if (charArray[i] == ' ') {
                spaceFound = true;
            }
        }

        return new String(charArray);
    }

    public static String[] textExplode(final java.lang.String text, final String separator) {
        int pos = 0;
        int index = 0;
        Vector<String> strings = new Vector<String>();

        do {
            index = separator.length() == 0 ? -1 : text.indexOf(separator, pos);

            if (index == -1) {
                strings.add(text.substring(pos));
            }
            else {
                strings.add(text.substring(pos, index));
                pos = index + 1;
            }
        } while (index != -1);

        return strings.toArray(new String[strings.size()]);
    }

    public static javax.swing.ImageIcon convertBlobToImageIcon(java.sql.Blob blob) throws java.sql.SQLException, java.io.IOException {
        int i = 0;
        int bytesRead = 0;
        int bytesReadTotal = 0;
        byte[] buffer = new byte[1024];
        byte[] bufferImageIcon = new byte[1024 * 1024];
        InputStream is = blob.getBinaryStream();

        while ((bytesRead = is.read(buffer)) != -1) {
            for (i = 0; i < bytesRead; i++) {
                bufferImageIcon[bytesReadTotal + i] = buffer[i];
            }
            bytesReadTotal += bytesRead;
        }

        return new ImageIcon(bufferImageIcon);
    }

    public static byte[] convertBlobToBytes(java.sql.Blob blob) throws java.sql.SQLException, java.io.IOException {
        InputStream is = blob.getBinaryStream();
        DataInputStream dis = new DataInputStream(is);
        byte[] bytes = new byte[dis.available()];

        dis.readFully(bytes);
        dis.close();

        return bytes;
    }

    public static void printOutException(java.lang.String object, java.lang.Exception exception) {
        System.err.println("[" + object + "] " + exception);
    }

    public static void printOutException(java.lang.Object object, java.lang.Exception exception) {
        System.err.println("[" + object.getClass().getName() + "] " + exception);
    }

    public static void renderException(java.lang.String object, java.lang.Exception exception) {
        printOutException(object, exception);
        //JOptionPane.showMessageDialog(null, "[" + object + "] " + exception, "Excepción", JOptionPane.WARNING_MESSAGE);
        JOptionPane.showMessageDialog(null, (exception.getMessage() == null ? exception : exception.getMessage()) +
                "\n\n[" + exception.getClass().getName() + " en " + object + "]", "Excepción", JOptionPane.WARNING_MESSAGE);
    }

    public static void renderException(java.lang.Object object, java.lang.Exception exception) {
        printOutException(object, exception);
        //JOptionPane.showMessageDialog(null, "[" + object.getClass().getName() + "] " + exception, "Excepción", JOptionPane.WARNING_MESSAGE);
        JOptionPane.showMessageDialog(null, (exception.getMessage() == null ? exception : exception.getMessage()) +
                "\n\n[" + exception.getClass().getName() + " en " + object.getClass().getName() + "]", "Excepción", JOptionPane.WARNING_MESSAGE);
    }
    
    public static void requestComponentFocus(final javax.swing.JComponent component){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                component.requestFocusInWindow();
            }
        });
    }

    /** Translates a value to its text representation. */
    private static java.lang.String translateValueToTexHundreds(int value, int languaje) {
        int integer;
        int remaining;
        String sText = "";
        String sBlank = "";

        switch (languaje) {
            case SLibConstants.LAN_SPANISH:
                if (value == 100) {
                    sText = MONEY_SPA_HUNDRED;
                }
                else {
                    remaining = value;

                    // Hundreds:

                    integer = value / 100;
                    if (integer > 0) {
                        sText += mastrSpaHundreds[integer - 1];
                        remaining -= integer * 100;
                        sBlank = " ";
                    }

                    // Tenths:

                    if (remaining >= 11 && remaining <= 19) {
                        sText += sBlank + mastrSpaUnits10[remaining - 10 - 1];
                    }
                    else if (remaining >= 21 && remaining <= 29) {
                        sText += sBlank + mastrSpaUnits20[remaining - 20 - 1];
                    }
                    else {
                        integer = remaining / 10;
                        if (integer > 0) {
                            sText += sBlank + mastrSpaTens[integer - 1];
                            remaining -= integer * 10;
                            sBlank = " Y ";
                        }

                        integer = remaining;
                        if (integer > 0) {
                            sText += sBlank + mastrSpaUnits00[integer - 1];
                        }
                    }
                }
                break;

            case SLibConstants.LAN_ENGLISH:
                remaining = value;

                // Hundreds:

                integer = value / 100;
                if (integer > 0) {
                    sText += mastrEngUnits00[integer - 1] + " " + MONEY_ENG_HUNDRED;
                    remaining -= integer * 100;
                    sBlank = " ";
                }

                // Tenths:

                if (remaining >= 11 && remaining <= 19) {
                    sText += sBlank + mastrEngUnits10[remaining - 10 - 1];
                }
                else {
                    integer = remaining / 10;
                    if (integer > 0) {
                        sText += sBlank + mastrEngTens[integer - 1];
                        remaining -= integer * 10;
                        sBlank = "-";
                    }

                    integer = remaining;
                    if (integer > 0) {
                        sText += sBlank + mastrEngUnits00[integer - 1];
                    }
                }
                break;

            default:
        }

        return sText;
    }

    /** Translates a value to its text representation. */
    public static java.lang.String translateValueToText(double value, int decs, int languaje,
            java.lang.String curSingular, java.lang.String curPlural, java.lang.String curPrefix, java.lang.String curSuffix) {

        int integer;
        double remaining;
        String sDecs = "";
        String sText = "";
        String sBlank = "";
        DecimalFormat formatDecs = new DecimalFormat("." + SLibUtilities.textRepeat("0", decs));

        if (value > MAX_VALUE) {
            sText = "(ERROR: cantidad mayor a " + MAX_VALUE + ")";
        }
        else {
            switch (languaje) {
                case SLibConstants.LAN_SPANISH:
                    remaining = value;
                    sText = "(";

                    // Millions:

                    integer = (int) (remaining / 1000000.0);
                    if (integer > 0) {
                        sText += translateValueToTexHundreds(integer, languaje) + " " + (integer == 1 ? MONEY_SPA_MILLION_SNG : MONEY_SPA_MILLION_PLR);
                        remaining -= integer * 1000000.0;
                        sBlank = " ";
                    }

                    // Thousands:

                    integer = (int) (remaining / 1000.0);
                    if (integer > 0) {
                        sText += sBlank + translateValueToTexHundreds(integer, languaje) + " " + (integer == 1 ? MONEY_SPA_THOUSAND_SNG : MONEY_SPA_THOUSAND_PLR);
                        remaining -= integer * 1000.0;
                        sBlank = " ";
                    }

                    // Units:

                    integer = (int) (remaining);
                    if (integer > 0) {
                        sText += sBlank + translateValueToTexHundreds(integer, languaje);
                    }

                    // Currency:

                    if ((int) value == 0) {
                        sText += MONEY_SPA_ZERO + " " + curPlural;
                    }
                    else if ((int) value == 1) {
                        sText += " " + curSingular;
                    }
                    else {
                        sText += " " + curPlural;
                    }

                    sDecs = formatDecs.format(value);
                    sText += " " + sDecs.substring(sDecs.lastIndexOf(".") + 1) + "/1" + SLibUtilities.textRepeat("0", decs) + " " + curSuffix + ")";
                    break;

                case SLibConstants.LAN_ENGLISH:
                    remaining = value;
                    sText = "(";

                    // Millions:

                    integer = (int) (remaining / 1000000.0);
                    if (integer > 0) {
                        sText += translateValueToTexHundreds(integer, languaje) + " " + MONEY_ENG_MILLION;
                        remaining -= integer * 1000000.0;
                        sBlank = " ";
                    }

                    // Thousands:

                    integer = (int) (remaining / 1000.0);
                    if (integer > 0) {
                        sText += sBlank + translateValueToTexHundreds(integer, languaje) + " " + MONEY_ENG_THOUSAND;
                        remaining -= integer * 1000.0;
                        sBlank = " ";
                    }

                    // Units:

                    integer = (int) (remaining);
                    if (integer > 0) {
                        sText += sBlank + translateValueToTexHundreds(integer, languaje);
                    }

                    // Currency:

                    if ((int) value == 0) {
                        sText += MONEY_ENG_ZERO + " " + curPlural;
                    }
                    else if ((int) value == 1) {
                        sText += " " + curSingular;
                    }
                    else {
                        sText += " " + curPlural;
                    }

                    sDecs = formatDecs.format(value);
                    sText += " " + sDecs.substring(sDecs.lastIndexOf(".") + 1) + "/1" + SLibUtilities.textRepeat("0", decs) + " " + curSuffix + ")";
                    break;

                default:
                    sText = "(ERROR: idioma no soportado)";
            }
        }

        return sText;
    }

    /** Translates units to its text representation. */
    public static java.lang.String translateUnitsToText(double units, int decs, int languaje,
            java.lang.String unitSingular, java.lang.String unitPlural) {

        int integer = 0;
        double remaining = 0;
        double remainingDecs = 0;
        String sDecs = "";
        String sText = "";
        String sBlank = "";
        DecimalFormat formatDecs = new DecimalFormat("." + SLibUtilities.textRepeat("0", decs));

        if (units > MAX_VALUE) {
            sText = "(ERROR: cantidad mayor a " + MAX_VALUE + ")";
        }
        else {
            switch (languaje) {
                case SLibConstants.LAN_SPANISH:
                    /*
                     * 1. Integer units.
                     */

                    remaining = units;
                    sText = "(";

                    // Millions:

                    integer = (int) (remaining / 1000000.0);
                    if (integer > 0) {
                        sText += translateValueToTexHundreds(integer, languaje) + " " + (integer == 1 ? MONEY_SPA_MILLION_SNG : MONEY_SPA_MILLION_PLR);
                        remaining -= integer * 1000000.0;
                        sBlank = " ";
                    }

                    // Thousands:

                    integer = (int) (remaining / 1000.0);
                    if (integer > 0) {
                        sText += sBlank + translateValueToTexHundreds(integer, languaje) + " " + (integer == 1 ? MONEY_SPA_THOUSAND_SNG : MONEY_SPA_THOUSAND_PLR);
                        remaining -= integer * 1000.0;
                        sBlank = " ";
                    }

                    // Units:

                    integer = (int) (remaining);
                    if (integer > 0) {
                        sText += sBlank + translateValueToTexHundreds(integer, languaje);
                    }

                    /*
                     * 2. Decimal units.
                     */

                    if ((int) units == 0 && units != 0d) {
                        sText += MONEY_SPA_ZERO;
                    }

                    sDecs = formatDecs.format(units);
                    sDecs = sDecs.substring(sDecs.lastIndexOf(".") + 1);
                    remaining = parseDouble(sDecs);
                    remainingDecs = remaining;

                    while ((int) remainingDecs >= 10 && (int) remainingDecs % 10 == 0) {
                        remainingDecs /= 10;
                    }

                    if (remaining > 0) {
                        sText += " PUNTO ";

                        for (int i = 0; i < sDecs.length() - ("" + ((int) remaining)).length(); i++) {
                            sText += MONEY_SPA_ZERO + " ";
                        }

                        remaining = remainingDecs;

                        // Millions:

                        integer = (int) (remaining / 1000000.0);
                        if (integer > 0) {
                            sText += translateValueToTexHundreds(integer, languaje) + " " + (integer == 1 ? MONEY_SPA_MILLION_SNG : MONEY_SPA_MILLION_PLR);
                            remaining -= integer * 1000000.0;
                            sBlank = " ";
                        }

                        // Thousands:

                        integer = (int) (remaining / 1000.0);
                        if (integer > 0) {
                            sText += sBlank + translateValueToTexHundreds(integer, languaje) + " " + (integer == 1 ? MONEY_SPA_THOUSAND_SNG : MONEY_SPA_THOUSAND_PLR);
                            remaining -= integer * 1000.0;
                            sBlank = " ";
                        }

                        // Units:

                        integer = (int) (remaining);
                        if (integer > 0) {
                            sText += sBlank + translateValueToTexHundreds(integer, languaje);
                        }
                    }

                    // Final text:

                    if (units == 0d) {
                        sText += MONEY_SPA_ZERO + " " + unitPlural;
                    }
                    else if (units == 1d) {
                        sText += " " + unitSingular;
                    }
                    else {
                        sText += " " + unitPlural;
                    }

                    sText += ")";
                    break;

                case SLibConstants.LAN_ENGLISH:
                    /*
                     * 1. Integer units.
                     */

                    remaining = units;
                    sText = "(";

                    // Millions:

                    integer = (int) (remaining / 1000000.0);
                    if (integer > 0) {
                        sText += translateValueToTexHundreds(integer, languaje) + " " + MONEY_ENG_MILLION;
                        remaining -= integer * 1000000.0;
                        sBlank = " ";
                    }

                    // Thousands:

                    integer = (int) (remaining / 1000.0);
                    if (integer > 0) {
                        sText += sBlank + translateValueToTexHundreds(integer, languaje) + " " + MONEY_ENG_THOUSAND;
                        remaining -= integer * 1000.0;
                        sBlank = " ";
                    }

                    // Units:

                    integer = (int) (remaining);
                    if (integer > 0) {
                        sText += sBlank + translateValueToTexHundreds(integer, languaje);
                    }

                    /*
                     * 2. Decimal units.
                     */

                    if ((int) units == 0 && units != 0d) {
                        sText += MONEY_ENG_ZERO;
                    }

                    sDecs = formatDecs.format(units);
                    sDecs = sDecs.substring(sDecs.lastIndexOf(".") + 1);
                    remaining = parseDouble(sDecs);
                    remainingDecs = remaining;

                    while ((int) remainingDecs >= 10 && (int) remainingDecs % 10 == 0) {
                        remainingDecs /= 10;
                    }

                    if (remaining > 0) {
                        sText += " DOT ";

                        for (int i = 0; i < sDecs.length() - ("" + ((int) remaining)).length(); i++) {
                            sText += MONEY_ENG_ZERO + " ";
                        }

                        remaining = remainingDecs;

                        // Millions:

                        integer = (int) (remaining / 1000000.0);
                        if (integer > 0) {
                            sText += translateValueToTexHundreds(integer, languaje) + " " + MONEY_ENG_MILLION;
                            remaining -= integer * 1000000.0;
                            sBlank = " ";
                        }

                        // Thousands:

                        integer = (int) (remaining / 1000.0);
                        if (integer > 0) {
                            sText += sBlank + translateValueToTexHundreds(integer, languaje) + " " + MONEY_ENG_THOUSAND;
                            remaining -= integer * 1000.0;
                            sBlank = " ";
                        }

                        // Units:

                        integer = (int) (remaining);
                        if (integer > 0) {
                            sText += sBlank + translateValueToTexHundreds(integer, languaje);
                        }
                    }

                    // Final text:

                    if (units == 0d) {
                        sText += MONEY_ENG_ZERO + " " + unitPlural;
                    }
                    else if (units == 1d) {
                        sText += " " + unitSingular;
                    }
                    else {
                        sText += " " + unitPlural;
                    }

                    sText += ")";
                    break;

                default:
                    sText = "(ERROR: idioma no soportado)";
            }
        }

        return sText;
    }

    public static java.util.ArrayList<org.w3c.dom.Node> obtainChildElements(final org.w3c.dom.Node node, final java.lang.String childElementName) throws java.lang.Exception {
        NodeList nodeList = node.getChildNodes();
        ArrayList<Node> children = new ArrayList<Node>();

        if (nodeList == null) {
            throw new Exception("Supplied XML node '" + node.getNodeName() + "' does not have any child elements!");
        }
        else {
            for (int i = 0; i < nodeList.getLength(); i++) {
                if (nodeList.item(i).getNodeName().compareTo(childElementName) == 0) {
                    children.add(nodeList.item(i));
                }
            }

            if (children.isEmpty()) {
                throw new Exception("XML child elements '" + childElementName + "' not found in XML node '" + node.getNodeName() + "'!");
            }
        }

        return children;
    }

    public static boolean hasChildElement(final org.w3c.dom.Node node, final java.lang.String childElementName) {
        boolean found = false;
        NodeList nodeList = node.getChildNodes();

        if (nodeList != null) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                if (nodeList.item(i).getNodeName().compareTo(childElementName) == 0) {
                    found = true;
                    break;
                }
            }
        }

        return found;
    }

    public static java.lang.String obtainAttributeValue(org.w3c.dom.NamedNodeMap namedNodeMap, java.lang.String attributeName, boolean isMandatory) throws java.lang.Exception {
        String value = "";
        Node node = namedNodeMap.getNamedItem(attributeName);

        if (node == null) {
            if (isMandatory) {
                throw new Exception("XML element attribute '" + attributeName + "' not found!");
            }
        }
        else {
            value = node.getNodeValue();
        }

        return value;
    }

   public static String validateEmail(String sEmail) {
        String sError = "";
        try {
            new InternetAddress(sEmail).validate();

        } catch (AddressException e) {
            sError = "La cuenta de correo-e '" + sEmail + "', es inválido:\n" + e.getMessage();
        }

        return sError;
    }

    public static void launchFile(final java.lang.String filePath) {
        try {
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler \"" + filePath + "\"");
        }
        catch (IOException e) {
            renderException(SLibUtilities.class.getName(), e);
        }
        catch (Exception e) {
            renderException(SLibUtilities.class.getName(), e);
        }
    }

    public static void launch(final java.lang.String command) {
        try {
            Runtime.getRuntime().exec(command);
        }
        catch (IOException e) {
            renderException(SLibUtilities.class.getName(), e);
        }
        catch (Exception e) {
            renderException(SLibUtilities.class.getName(), e);
        }
    }

    public static void launchCalculator() {
        launch("calc.exe");
    }
}
