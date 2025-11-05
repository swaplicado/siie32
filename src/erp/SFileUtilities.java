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
    
    public static final String csv = "csv";
    public static final String CSV_DESCRIP = "Archivo CSV (*." + csv + ")";
    
    public static final String exp = "exp";
    public static final String EXP_DESCRIP = "Archivo EXP (*." + exp + ")";
    
    public static final String pdf = "pdf";
    public static final String PDF_DESCRIP = "Archivo PDF (*." + pdf + ")";
    
    public static final String xml = "xml";
    public static final String XML_DESCRIP = "Archivo XML (*." + xml + ")";
    
    public static final String xlsx = "xlsx";
    public static final String XLSX_DESCRIP = "Archivo MS Excel (*." + xlsx + ")";
    
    public static final String zip = "zip";
    public static final String ZIP_DESCRIP = "Archivo ZIP (*." + zip + ")";
    
    public static final String IMG_jpg = "jpg";
    public static final String IMG_jpeg = "jpeg";
    public static final String IMG_png = "png";
    public static final String IMG_gif = "gif";
    public static final String IMG_bmp = "bmp";
    
    public static final String IMGS_jpg_png = "jpg_png";
    public static final String IMGS_JPG_PNG_DESCRIP = "Archivo imagen JPG, PNG (*." + IMG_jpg + ", *." + IMG_jpeg + ", *." + IMG_png + ")";
    
    public static final String IMGS_ALL = "all";
    public static final String IMGS_ALL_DESCRIP = "Archivo imagen JPG, PNG, GIF y BMP (*." + IMG_jpg + ", *." + IMG_jpeg + ", *." + IMG_png + ", *." + IMG_gif + ", *." + IMG_bmp + ")";

    /**
     * Create file name extension filter for given extension.
     * @param extension Supported options: csv, exp, pdf, xml, xlsx, zip, IMGS_jpg_png & IMGS_ALL.
     * @return File name extension filter for given extension.
     */
    public static FileNameExtensionFilter createFileNameExtensionFilter(final String extension) {
        FileNameExtensionFilter filter = null;
        
        switch (extension) {
            case csv:
                filter = new FileNameExtensionFilter(CSV_DESCRIP, extension);
                break;
            case exp:
                filter = new FileNameExtensionFilter(EXP_DESCRIP, extension);
                break;
            case pdf:
                filter = new FileNameExtensionFilter(PDF_DESCRIP, extension);
                break;
            case xml:
                filter = new FileNameExtensionFilter(XML_DESCRIP, extension);
                break;
            case xlsx:
                filter = new FileNameExtensionFilter(XLSX_DESCRIP, extension);
                break;
            case zip:
                filter = new FileNameExtensionFilter(ZIP_DESCRIP, extension);
                break;
            case IMGS_jpg_png:
                filter = new FileNameExtensionFilter(IMGS_JPG_PNG_DESCRIP, IMG_jpg, IMG_jpeg, IMG_png);
                break;
            case IMGS_ALL:
                filter = new FileNameExtensionFilter(IMGS_ALL_DESCRIP, IMG_jpg, IMG_jpeg, IMG_png, IMG_gif, IMG_bmp);
                break;
            default:
                // nothing
        }
        
        return filter;
    }
}
