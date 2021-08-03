package erp.mfin.data.diot;

/**
 *
 * @author Sergio Flores
 */
public abstract class SDiotConsts {
    
    public static final String OPER_UNDEF = "00";
    public static final String OPER_SERVICES = "03";
    public static final String OPER_LEASING = "06";
    public static final String OPER_OTHER = "85";
    
    public static final String THIRD_UNDEF = "00";
    public static final String THIRD_DOMESTIC = "04";
    public static final String THIRD_INTERNATIONAL = "05";
    public static final String THIRD_GLOBAL = "15";
    
    public static final String THIRD_GLOBAL_NAME = "Proveedor Global";
    
    public static final String NA = "NA";
    
    /** VAT type: Exempt. */
    public static final String VAT_TYPE_EXEMPT = "Exent";
    /** VAT type: Zero rate. */
    public static final String VAT_TYPE_RATE_0 = "TCero";
    /** VAT type: General rate. */
    public static final String VAT_TYPE_GENERAL = "TGral";
    /** VAT type: Border rate. */
    public static final String VAT_TYPE_BORDER = "TFron";
    /** VAT type: Northern border incentive rate. */
    public static final String VAT_TYPE_BORDER_NORTH_INC = "EFron";
}
