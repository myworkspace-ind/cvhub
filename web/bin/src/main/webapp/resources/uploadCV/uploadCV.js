function updateFileName() {
            var input = document.getElementById('file-upload');
            var fileNameDisplay = document.getElementById('file-name');
            if (input.files && input.files[0]) {
                fileNameDisplay.textContent = 'File đã chọn: ' + input.files[0].name;
            } else {
                fileNameDisplay.textContent = '';
            }
        }
        