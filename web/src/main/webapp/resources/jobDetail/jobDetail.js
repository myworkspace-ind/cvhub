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
document.addEventListener('DOMContentLoaded', function() {
    document.querySelectorAll('.job-organization').forEach(function(form) {
        form.addEventListener('submit', function(e) {
            e.preventDefault();
            e.stopPropagation();
            this.submit();
        });
    });
});
 document.addEventListener('DOMContentLoaded', function() {
            const toggleButton = document.getElementById('toggleJobForm');
            const jobForm = document.getElementById('jobPostForm');
            const overlay = document.getElementById('formOverlay');

            function showForm() {
                jobForm.style.display = 'block';
                overlay.style.display = 'block';
                toggleButton.textContent = '- Ẩn form thêm công việc';
            }

            function hideForm() {
                jobForm.style.display = 'none';
                overlay.style.display = 'none';
                toggleButton.textContent = '+ Thêm thông tin tuyển dụng';
            }

            toggleButton.addEventListener('click', function() {
                if (jobForm.style.display === 'none' || jobForm.style.display === '') {
                    showForm();
                } else {
                    hideForm();
                }
            });

            // Close form when clicking outside
            overlay.addEventListener('click', hideForm);

            // Prevent form from closing when clicking inside it
            jobForm.addEventListener('click', function(event) {
                event.stopPropagation();
            });
             });