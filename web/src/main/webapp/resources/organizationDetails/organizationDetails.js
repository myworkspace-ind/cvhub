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
             function toggleSalaryInput() {
    const salaryOption = document.getElementById('salaryOption');
    const salaryInput = document.getElementById('salary');

    if (salaryOption.value === "0") {
        salaryInput.value = 0; // Gán giá trị là 0
        salaryInput.style.display = 'none'; // Ẩn trường nhập lương
    } else if (salaryOption.value === "1") {
        salaryInput.style.display = 'block'; // Hiển thị trường nhập lương
        salaryInput.value = ''; // Xóa giá trị trước đó
    } else {
        salaryInput.style.display = 'none'; // Ẩn trường nhập lương
        salaryInput.value = ''; // Xóa giá trị nếu không chọn
    }
}