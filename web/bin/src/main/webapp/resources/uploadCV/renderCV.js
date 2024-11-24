// Đợi và load thư viện cần thiết
async function loadLibraries() {
    // Load jsPDF
    await new Promise((resolve, reject) => {
        const script = document.createElement('script');
        script.src = 'https://cdnjs.cloudflare.com/ajax/libs/jspdf/2.5.1/jspdf.umd.min.js';
        script.onload = resolve;
        script.onerror = reject;
        document.head.appendChild(script);
    });

    // Load html2canvas
    await new Promise((resolve, reject) => {
        const script = document.createElement('script');
        script.src = 'https://cdnjs.cloudflare.com/ajax/libs/html2canvas/1.4.1/html2canvas.min.js';
        script.onload = resolve;
        script.onerror = reject;
        document.head.appendChild(script);
    });
}

async function generatePDF() {
    const button = document.getElementById('download-btn');
    const spinner = button.querySelector('.loading-spinner');
    const buttonText = button.querySelector('span');
    const pdfIcon = button.querySelector('.pdf-icon');

    try {
        // Hiển thị loading
        spinner.style.display = 'block';
        buttonText.style.display = 'none';
        pdfIcon.style.display = 'none';
        button.disabled = true;

        // Lấy element cần in
        const element = document.getElementById('cv-container');
        
        // Convert HTML sang Canvas
        const canvas = await html2canvas(element, {
            scale: 2,
            useCORS: true,
            allowTaint: true,
            logging: true,
            backgroundColor: '#ffffff'
        });

        // Khởi tạo jsPDF
        const { jsPDF } = window.jspdf;
        const doc = new jsPDF({
            orientation: 'portrait',
            unit: 'mm',
            format: 'a4',
            compress: true
        });

        // Tính toán tỷ lệ để fit vào A4
        const imgWidth = 210; // A4 width in mm
        const imgHeight = (canvas.height * imgWidth) / canvas.width;
        
        // Add canvas vào PDF
        const imgData = canvas.toDataURL('image/jpeg', 1.0);
        doc.addImage(imgData, 'JPEG', 0, 0, imgWidth, imgHeight);

        // Tải PDF
        doc.save('CV.pdf');

    } catch (error) {
        console.error('Error generating PDF:', error);
        alert('Có lỗi xảy ra khi tạo PDF: ' + error.message);
    } finally {
        // Reset button state
        spinner.style.display = 'none';
        buttonText.style.display = 'inline';
        pdfIcon.style.display = 'inline';
        button.disabled = false;
    }
}

// Initialize khi document ready
document.addEventListener('DOMContentLoaded', async function() {
    try {
        // Load các thư viện cần thiết
        await loadLibraries();
        
        // Add event listener cho nút download
        const downloadBtn = document.getElementById('download-btn');
        if (downloadBtn) {
            downloadBtn.addEventListener('click', generatePDF);
        }
    } catch (error) {
        console.error('Error initializing:', error);
    }
});