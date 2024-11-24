document.addEventListener('DOMContentLoaded', function() {
    const trigger = document.querySelector('.profile-trigger');
    const dropdown = document.getElementById('profileDropdown');
    
    if(trigger && dropdown) {
        trigger.addEventListener('click', function(event) {
            event.stopPropagation();
            dropdown.classList.toggle('show');
        });
    }
    
    document.addEventListener('click', function(event) {
        if (dropdown && !dropdown.contains(event.target) && !trigger.contains(event.target)) {
            dropdown.classList.remove('show');
        }
    });
});