/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.utils;

import java.io.File;

/**
 *
 * @author Edwin Carmona
 */
public class SPDFUtils {
    
    /**
     * Obtiene el directorio temporal para almacenar archivos antes de subir a GCS.
     * Si no existe, lo crea.
     * Nota: Este método asume que el sistema operativo tiene configurado un directorio temporal válido.
     * 
     * @return File representando el directorio temporal local para esta aplicación.
     */
    public static File getTemporalDir() {
        String sysTempDir = System.getProperty("java.io.tmpdir");
        File localTempDir = new File(sysTempDir + (sysTempDir.endsWith("\\") ? "" : "\\") + "OCS_TEMP");
        if (!localTempDir.exists()) {
            boolean ok = localTempDir.mkdirs();
            if (!ok) {
                throw new RuntimeException("Failed to create directory: " + localTempDir.getAbsolutePath());
            }
        }

        return localTempDir;
    }
    
    /**
     * Se comenta este código para posible utilización futura en comprobantes de pago
     * Edwin Carmona 2026-04-28
     */
//    public static File getPDFPage(String inputPath, int pageNumber) throws IOException {
//        File outputFolder = SPDFUtils.getTemporalDir();
//        if (!outputFolder.exists()) outputFolder.mkdirs();
//
//        try (PDDocument document = PDDocument.load(new File(inputPath))) {
//            if (pageNumber < 1 || pageNumber > document.getNumberOfPages()) {
//                throw new IllegalArgumentException("Page number out of range. Total pages: " + document.getNumberOfPages());
//            }
//
//            PDPage page = document.getPage(pageNumber - 1);
//            PDDocument singlePageDoc = new PDDocument();
//            singlePageDoc.addPage(page);
//
//            File outputFile = new File(outputFolder, "page_" + pageNumber + ".pdf");
//            singlePageDoc.save(outputFile);
//            singlePageDoc.close();
//
//            return outputFile;   
//        }
//    }
//    
//    public static void getMetadata(String filePath) throws IOException {
//        try (PDDocument document = PDDocument.load(new File(filePath))) {
//            PDDocumentInformation info = document.getDocumentInformation();
//
//            System.out.println("Título:  " + info.getTitle());
//            System.out.println("Autor:   " + info.getAuthor());
//            System.out.println("Páginas: " + document.getNumberOfPages());
//            System.out.println("Creado:  " + info.getCreationDate().getTime());
//        }
//    }
//    
//    public static int searchWord(String filePath, String wordToSearch) throws IOException {
//        try (PDDocument document = PDDocument.load(new File(filePath))) {
//            PDFTextStripper stripper = new PDFTextStripper();
//            int totalPaginas = document.getNumberOfPages();
//            int inPage = 0;
//            for (int i = 1; i <= totalPaginas; i++) {
//                stripper.setStartPage(i);
//                stripper.setEndPage(i);
//
//                String texto = stripper.getText(document);
//
//                if (texto.toLowerCase().contains(wordToSearch.toLowerCase())) {
//                    System.out.println("Encontrado en página " + i);
//                    inPage = i;
//                    break;
//                }
//            }
//            
//            return inPage;
//        }
//    }
}
