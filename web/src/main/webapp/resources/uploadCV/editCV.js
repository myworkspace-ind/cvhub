document.addEventListener('DOMContentLoaded', function() {
    var profilePictureContainer = document.querySelector('.profile-picture-container');
    var profilePictureInput = document.getElementById('profile-picture-input');
    var profilePicture = document.getElementById('profile-picture');
    var cvForm = document.getElementById('cv-form');

    if (profilePictureContainer && profilePictureInput && profilePicture) {
        profilePictureContainer.addEventListener('click', function() {
            profilePictureInput.click();
        });

        profilePictureInput.addEventListener('change', function(event) {
            const file = event.target.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function(e) {
                    profilePicture.src = e.target.result;
                };
                reader.readAsDataURL(file);
            }
        });
    } else {
        console.warn('Một hoặc nhiều phần tử cần thiết không tồn tại trong DOM.');
    }

    if (cvForm) {
        cvForm.addEventListener('submit', function(e) {
            var logoFile = profilePictureInput ? profilePictureInput.files[0] : null;
        });
    } else {
        console.warn('Form CV không tồn tại trong DOM.');
    }
});