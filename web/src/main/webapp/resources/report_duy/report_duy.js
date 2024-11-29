document.addEventListener('DOMContentLoaded', function () {
    // Lấy các phần tử cần dùng
    const reportButton = document.getElementById('report');
    const dropdownMenu = document.querySelector('.dropdown-menu');

    // Bật/tắt hiển thị menu khi click vào nút Report
    reportButton.addEventListener('click', function (event) {
        event.preventDefault(); // Ngăn chặn hành vi mặc định
        // Toggle menu visibility
        dropdownMenu.style.display = dropdownMenu.style.display === 'block' ? 'none' : 'block';
    });

    // Đóng menu khi click ra ngoài
    document.addEventListener('click', function (event) {
        if (!reportButton.contains(event.target) && !dropdownMenu.contains(event.target)) {
            dropdownMenu.style.display = 'none';
        }
    });
});