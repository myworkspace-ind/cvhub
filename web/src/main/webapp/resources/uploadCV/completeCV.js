function parseAndFillCV(cvText) {
    // Hàm tiện ích để tìm giá trị sau một chuỗi nhất định
    function findValue(text, key) {
        const index = text.indexOf(key);
        if (index !== -1) {
            const start = index + key.length;
            const end = text.indexOf('\n', start);
            return text.substring(start, end !== -1 ? end : undefined).trim();
        }
        return '';
    }

    // Cập nhật các trường trong HTML
    document.querySelector('.header h1').textContent = findValue(cvText, 'Total ') || '';
    document.querySelector('.contact-info span:nth-of-type(1)').textContent = findValue(cvText, 'Phone\n') || '';
    document.querySelector('.contact-info span:nth-of-type(2)').textContent = findValue(cvText, 'Email\n') || '';
    document.querySelector('.contact-info span:nth-of-type(3)').textContent = findValue(cvText, 'Address\n') || '';
    document.querySelector('.contact-info span:nth-of-type(4)').textContent = findValue(cvText, 'Dob\n') || '';
    document.querySelector('.contact-info span:nth-of-type(5)').textContent = findValue(cvText, 'Gender\n') || '';

    // Xử lý phần Education
    const educationSection = document.querySelector('.section:nth-of-type(1)');
    const educationText = findValue(cvText, 'Education\n');
    const educationParts = educationText.split('\n');
    educationSection.querySelector('p:nth-of-type(1)').textContent = educationParts[0] || '';
    educationSection.querySelector('p:nth-of-type(2)').textContent = educationParts[1] || '';
    educationSection.querySelector('p:nth-of-type(3)').textContent = findValue(cvText, 'GPA:') || '';

    // Xử lý phần Skills
    const skillsSection = document.querySelector('.section:nth-of-type(2)');
    const skillsText = findValue(cvText, 'Skills\n');
    const skills = skillsText.split('\n');
    skills.forEach((skill, index) => {
        if (skillsSection.querySelector(`p:nth-of-type(${index + 1})`)) {
            skillsSection.querySelector(`p:nth-of-type(${index + 1})`).textContent = skill.trim();
        }
    });

    // Xử lý phần Certifications
    const certSection = document.querySelector('.section:nth-of-type(3)');
    const certText = findValue(cvText, 'Certifications\n');
    const certs = certText.split('\n');
    certs.forEach((cert, index) => {
        if (certSection.querySelector(`p:nth-of-type(${index + 1})`)) {
            certSection.querySelector(`p:nth-of-type(${index + 1})`).textContent = cert.trim();
        }
    });

    // Xử lý phần Projects
    const projectSection = document.querySelector('.section:nth-of-type(4) .project');
    projectSection.querySelector('h3').textContent = findValue(cvText, '"') || '';
    projectSection.querySelector('p:nth-of-type(1)').textContent = findValue(cvText, 'E-Commerce Website\n') || '';
    projectSection.querySelector('p:nth-of-type(2)').textContent = findValue(cvText, 'Team size:') || '';
    projectSection.querySelector('p:nth-of-type(3)').textContent = findValue(cvText, 'MY POSITION:') || '';
    projectSection.querySelector('p:nth-of-type(4)').textContent = cvText.substring(cvText.indexOf('Website selling'), cvText.indexOf('TECHNOLOGY DESCRIPTION')).trim();
    projectSection.querySelector('p:nth-of-type(5)').textContent = findValue(cvText, 'Front end:') || '';
    projectSection.querySelector('p:nth-of-type(6)').textContent = findValue(cvText, 'Back end:') || '';
    projectSection.querySelector('p:nth-of-type(7)').textContent = findValue(cvText, 'SQL:') || '';
}