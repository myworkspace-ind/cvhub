 function splitSentences() {
    const elements = document.querySelectorAll('.sentence-split');
    elements.forEach(element => {
        const text = element.textContent;
        const sentences = text.match(/[^\.!\?:]+[\.!\?:]+(?:\s|$)/g) || [text];
        const parentUl = element.parentElement;
        sentences.forEach(sentence => {
            const trimmedSentence = sentence.trim();
            if (trimmedSentence) {
                const li = document.createElement('li');
                li.textContent = trimmedSentence;
                parentUl.insertBefore(li, element);
            }
        });
        parentUl.removeChild(element);
    });
}

document.addEventListener('DOMContentLoaded', splitSentences);

 function toggleSalaryInput() {
 	var salaryOption = document.getElementById('salaryOption').value;
 	var salaryInput = document.getElementById('salary');
 	salaryInput.style.display = salaryOption === '1' ? 'block' : 'none';
 	salaryInput.required = salaryOption === '1';
 }

 // Initialize the salary input visibility
 toggleSalaryInput();