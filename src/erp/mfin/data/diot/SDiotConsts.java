package erp.mfin.data.diot;

import java.util.HashMap;

/**
 *
 * @author Sergio Flores
 */
public abstract class SDiotConsts {

    // layout formats:
    
    public static final int FORMAT_PIPE = 1; // layout of values separated with pipe
    public static final int FORMAT_CSV = 2;  // layout of values separated with comma
    
    // DIOT types of operation:

    public static final String OPER_UNDEF = "00";
    public static final String OPER_GOODS = "02"; // since DIOT 2025
    public static final String OPER_SERVS = "03";
    public static final String OPER_RENTS = "06";
    public static final String OPER_IMP_INT = "07"; // since DIOT 2025
    public static final String OPER_IMP_NAC = "08"; // since DIOT 2025
    public static final String OPER_OTHER = "85";

    public static final HashMap<String, String> Operations = new HashMap<>();

    static {
        Operations.put(OPER_GOODS, SDiotConsts.OPER_GOODS + " - Enajenación de bienes [nac./ext.]");
        Operations.put(OPER_SERVS, SDiotConsts.OPER_SERVS + " - Prestación de Servicios Profesionales [nac./ext.]");
        Operations.put(OPER_RENTS, SDiotConsts.OPER_RENTS + " - Uso o goce temporal de bienes [nac.]");
        Operations.put(OPER_IMP_INT, SDiotConsts.OPER_IMP_INT + " - Importación de bienes o servicios [ext.]");
        Operations.put(OPER_IMP_NAC, SDiotConsts.OPER_IMP_NAC + " - Importación por transferencia virtual [nac.]");
        Operations.put(OPER_OTHER, SDiotConsts.OPER_OTHER + " - Otros [nac.]");
    } 
    
    // DIOT types of third parties:

    public static final String THIRD_UNDEF = "00";
    public static final String THIRD_DOMESTIC = "04";
    public static final String THIRD_INTERNAT = "05";
    public static final String THIRD_GLOBAL = "15";
    
    // DIOT types of tax effects:

    public static final String TAX_EFFECT_YES = "01"; // since DIOT 2025
    public static final String TAX_EFFECT_NO = "02";  // since DIOT 2025

    // DIOT types of VAT:

    public static final String VAT_TYPE_EXEMPT = "Exent";
    public static final String VAT_TYPE_RATE_0 = "TCero";
    public static final String VAT_TYPE_GENERAL = "TGral";
    @Deprecated
    public static final String VAT_TYPE_BORDER = "TFron";
    public static final String VAT_TYPE_BORDER_NORTH = "FronN";
    public static final String VAT_TYPE_BORDER_SOUTH = "FronS";
    
    public static final HashMap<String, String> Vats = new HashMap<>();
    
    static {
        Vats.put(VAT_TYPE_EXEMPT, VAT_TYPE_EXEMPT + " - IVA exento");
        Vats.put(VAT_TYPE_RATE_0, VAT_TYPE_RATE_0 + " - IVA tasa 0%");
        Vats.put(VAT_TYPE_GENERAL, VAT_TYPE_GENERAL + " - IVA tasa general");
        Vats.put(VAT_TYPE_BORDER, VAT_TYPE_BORDER + " - IVA región fronteriza (obsoleta)");
        Vats.put(VAT_TYPE_BORDER_NORTH, VAT_TYPE_BORDER_NORTH + " - IVA región fronteriza norte");
        Vats.put(VAT_TYPE_BORDER_SOUTH, VAT_TYPE_BORDER_SOUTH + " - IVA región fronteriza sur");
    }
    
    // other DIOT constants:

    public static final String THIRD_GLOBAL_NAME = "Proveedor Global";
    
    public static final String NA = "NA";
}
