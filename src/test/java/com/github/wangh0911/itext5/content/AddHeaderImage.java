package com.github.wangh0911.itext5.content;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AddHeaderImage {
    final static File RESULT_FOLDER = new File("target/test-outputs", "content");

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        RESULT_FOLDER.mkdirs();
    }

    /**
     * <a href="http://stackoverflow.com/questions/35035356/itext-add-an-image-on-a-header-with-an-absolute-position">
     * IText : Add an image on a header with an absolute position
     * </a>
     * <p>
     * This test demonstrates how to add an image a a fixed position on a page.
     * </p>
     */
    @Test
    public void testAddHeaderImageFixed() throws IOException, DocumentException {
        try ( FileOutputStream stream = new FileOutputStream(new File(RESULT_FOLDER, "headerImage.pdf"))){
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, stream);
            writer.setPageEvent(new PdfPageEventHelper(){
                Image imgSoc = null;

                @Override
                public void onOpenDocument(PdfWriter writer, Document document){
                    try (InputStream imageStream = getClass().getResourceAsStream("/images/header.jpg ")){
                        imgSoc = Image.getInstance(IOUtils.toByteArray(imageStream));
                        imgSoc.scaleToFit(100,100);
                        imgSoc.setAbsolutePosition(200, 200);
                    }catch (BadElementException | IOException e){
                        e.printStackTrace();
                    }
                    super.onOpenDocument(writer, document);
                }

                @Override
                public void onEndPage(PdfWriter writer, Document document){
                    try{
                        writer.getDirectContent().addImage(imgSoc);
                    }catch (DocumentException e) {
                        e.printStackTrace();
                    }
                }
            });
            document.open();
            
            document.add(new Paragraph("PAGE 1"));
            document.newPage();
            document.add(new Paragraph("PAGE 2"));
            
            document.close();
        }
    }
}
