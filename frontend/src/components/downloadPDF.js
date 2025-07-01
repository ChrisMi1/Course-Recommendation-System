import jsPDF from 'jspdf';
import RobotoRegular from '../assets/fonts/Roboto-Regular';
import logo from '../assets/logo.png';

export function generatePDF(recommendations) {
  const doc = new jsPDF();
  doc.addFileToVFS("Roboto-Regular.ttf", RobotoRegular);
  doc.addFont("Roboto-Regular.ttf", "Roboto", "normal");
  doc.setFont("Roboto");

  const pageWidth = doc.internal.pageSize.getWidth();
  const pageHeight = doc.internal.pageSize.getHeight();

  let y = 20;

  const drawLogoBackground = () => {
    doc.addImage(logo, 'PNG', (pageWidth - 100) / 2, (pageHeight - 100) / 2, 100, 100, '', 'FAST', 0.1);
  };

  drawLogoBackground(); // First page background

  doc.setFontSize(18);
  doc.text("Προτεινόμενα Μαθήματα", 10, y);
  y += 10;

  const sections = [
    {
      title: 'Επιλεγόμενα Μαθήματα',
      filter: c => !c.mandatory && !c.prerequest,
    },
    {
      title: 'Βασικής Ροής Μαθήματα',
      filter: c => c.mandatory,
    },
    {
      title: 'Προαπαιτούμενα Μαθήματα',
      filter: c => c.prerequest,
    }
  ];

  sections.forEach(section => {
    const courses = recommendations.filter(section.filter);
    if (courses.length === 0) return;

    if (y > 260) {
      doc.addPage();
      drawLogoBackground();
      y = 20;
    }

    doc.setFontSize(15);
    doc.text(section.title, 10, y);
    y += 8;

    courses.forEach(course => {
      if (y > 270) {
        doc.addPage();
        drawLogoBackground();
        y = 20;
      }

      doc.setFontSize(13);
      doc.text(`• ${course.name}`, 12, y);
      y += 5;

      doc.setFontSize(10);
      doc.text(`URL: ${course.url}`, 14, y);
      y += 6;
    });
  });

  doc.save('recommended-lessons.pdf');
}
