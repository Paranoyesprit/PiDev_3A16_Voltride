package Controllers;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

import java.io.FileNotFoundException;
import java.util.List;

public class PDFGenerator {
    // Méthode pour générer un fichier PDF à partir des détails fournis
    public static void generatePDF(String fileName, List<String> labels, String title) throws FileNotFoundException {
        // Création du writer PDF
        PdfWriter pdfWriter = new PdfWriter(fileName);
        // Création du document PDF
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDocument);

        // Définition de la taille de page par défaut
        pdfDocument.setDefaultPageSize(PageSize.A4);

        // Ajout du titre
        Paragraph titleParagraph = new Paragraph(title)
                .setFontSize(18)
                .setTextAlignment(TextAlignment.CENTER)
                .setBold();
        document.add(titleParagraph);

        // Ajout d'une ligne vide
        document.add(new Paragraph());

        // Création du tableau
        float[] columnWidths = {3, 4}; // Largeurs des colonnes
        Table table = new Table(UnitValue.createPercentArray(columnWidths))
                .useAllAvailableWidth();

        // Ajout des en-têtes de tableau
        table.addCell(createHeaderCell("Détails")); // En-tête de la colonne des détails
        table.addCell(createHeaderCell("Valeur")); // En-tête de la colonne des valeurs

        // Ajout des détails au tableau
        for (String label : labels) {
            String[] parts = label.split(": ");
            if (parts.length == 2) {
                table.addCell(createCell(parts[0])); // Ajout du détail
                table.addCell(createCell(parts[1])); // Ajout de la valeur
            }
        }

        // Ajout du tableau au document
        document.add(table);

        // Fermeture du document
        document.close();
    }

    // Méthode pour créer une cellule d'en-tête avec un style spécifique
    private static Paragraph createHeaderCell(String content) {
        return new Paragraph(content)
                .setTextAlignment(TextAlignment.CENTER)
                .setBold();
    }

    // Méthode pour créer une cellule avec un style spécifique
    private static Paragraph createCell(String content) {
        return new Paragraph(content)
                .setTextAlignment(TextAlignment.LEFT);
    }

}
