document.addEventListener('DOMContentLoaded', function() {
    console.log('Script loaded'); // Để debug
    
    const profileDropdown = document.querySelector('.profile-dropdown');
    const profileButton = document.querySelector('.profile-button');
    const dropdownMenu = document.querySelector('.dropdown-menu');
    
    if (!profileButton || !dropdownMenu) {
        console.error('Required elements not found'); // Để debug
        return;
    }
    
    let isOpen = false;
    
    profileButton.addEventListener('click', function(e) {
        console.log('Button clicked'); // Để debug
        e.preventDefault();
        e.stopPropagation();
        
        isOpen = !isOpen;
        dropdownMenu.style.display = isOpen ? 'block' : 'none';
    });
    
    // Đóng dropdown khi click ra ngoài
    document.addEventListener('click', function(e) {
        if (isOpen && !profileDropdown.contains(e.target)) {
            isOpen = false;
            dropdownMenu.style.display = 'none';
        }
    });
    
    // Đóng dropdown khi nhấn ESC
    document.addEventListener('keydown', function(e) {
        if (isOpen && e.key === 'Escape') {
            isOpen = false;
            dropdownMenu.style.display = 'none';
        }
    });
});