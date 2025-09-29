/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp;

import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Sergio Flores
 */
public abstract class SFileUtilities {
    
    public static final String CSV = "csv";
    public static final String CSV_DESCRIP = "Archivo CSV (*." + CSV + ")";
    
    public static final String EXP = "exp";
    public static final String EXP_DESCRIP = "Archivo EXP (*." + EXP + ")";
    
    public static final String PDF = "pdf";
    public static final String PDF_DESCRIP = "Archivo PDF (*." + PDF + ")";
    
    public static final String XML = "xml";
    public static final String XML_DESCRIP = "Archivo XML (*." + XML + ")";
    
    public static final String XLSX = "xlsx";
    public static final String XLSX_DESCRIP = "Archivo MS Excel (*." + XLSX + ")";
    
    public static final String ZIP = "zip";
    public static final String ZIP_DESCRIP = "Archivo ZIP (*." + ZIP + ")";
    
    public static final String IMG_JPG = "jpg";
    public static final String IMG_JPEG = "jpeg";
    public static final String IMG_PNG = "png";
    public static final String IMG_GIF = "gif";
    public static final String IMG_BMP = "bmp";
    
    public static final String IMGS_JPG_PNG = "jpg_png";
    public static final String IMGS_JPG_PNG_DESCRIP = "Archivo imagen JPG, PNG (*." + IMG_JPG + ", *." + IMG_JPEG + ", *." + IMG_PNG + ")";
    
    public static final String IMGS_ALL = "all";
    public static final String IMGS_ALL_DESCRIP = "Archivo imagen JPG, PNG, GIF y BMP (*." + IMG_JPG + ", *." + IMG_JPEG + ", *." + IMG_PNG + ", *." + IMG_GIF + ", *." + IMG_BMP + ")";

    /**
     * Create file name extension filter for given extension.
     * @param extension Supported options: CSV_EXT, PDF_EXT, XML_EXT, EXP_EXT, EXCEL_EXT, IMAGES_EXT.
     * @return File name extension filter for given extension.
     */
    public static FileNameExtensionFilter createFileNameExtensionFilter(final String extension) {
        FileNameExtensionFilter filter = null;
        switch (extension) {
            case CSV:
                filter = new FileNameExtensionFilter(CSV_DESCRIP, extension);
                break;
            case EXP:
                filter = new FileNameExtensionFilter(EXP_DESCRIP, extension);
                break;
            case PDF:
                filter = new FileNameExtensionFilter(PDF_DESCRIP, extension);
                break;
            case XML:
                filter = new FileNameExtensionFilter(XML_DESCRIP, extension);
                break;
            case XLSX:
                filter = new FileNameExtensionFilter(XLSX_DESCRIP, extension);
                break;
            case ZIP:
                filter = new FileNameExtensionFilter(ZIP_DESCRIP, extension);
                break;
            case IMGS_JPG_PNG:
                filter = new FileNameExtensionFilter(IMGS_JPG_PNG_DESCRIP, IMG_JPG, IMG_JPEG, IMG_PNG);
                break;
            case IMGS_ALL:
                filter = new FileNameExtensionFilter(IMGS_ALL_DESCRIP, IMG_JPG, IMG_JPEG, IMG_PNG, IMG_GIF, IMG_BMP);
                break;
            default:
        // nothing
        }
        return filter;
    }
}
