document.addEventListener('DOMContentLoaded', function() {
    const profileDropdown = document.querySelector('.profile-dropdown');
    const dropdownToggle = profileDropdown.querySelector('.avatar-placeholder');
    const dropdownMenu = profileDropdown.querySelector('.dropdown-menu');

    if (!profileDropdown || !dropdownToggle || !dropdownMenu) {
        console.error('Required elements not found');
        return;
    }

    dropdownToggle.addEventListener('click', function(e) {
        e.stopPropagation();

        // Toggle hiển thị dropdown
        dropdownMenu.classList.toggle('show'); // Sử dụng class 'show' để kiểm soát hiển thị
    });

    // Đóng dropdown khi click ra ngoài
    document.addEventListener('click', function(e) {
        if (!profileDropdown.contains(e.target)) {
            dropdownMenu.classList.remove('show');
        }
    });
});