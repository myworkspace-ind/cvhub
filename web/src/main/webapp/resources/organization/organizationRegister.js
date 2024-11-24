function handleEmployerFormSubmit(event) {
			event.preventDefault();

			const progressBar = document.querySelector('.progress-bar');
			progressBar.style.width = '100%';
			progressBar.setAttribute('aria-valuenow', '100');

			const requirementsTab = new bootstrap.Tab(document.querySelector('#pills-requirements-tab'));
			requirementsTab.show();

			return false;
		}

		document.querySelectorAll('button[data-bs-toggle="pill"]').forEach(button => {
			button.addEventListener('shown.bs.tab', function (event) {
				const progressBar = document.querySelector('.progress-bar');
				if (event.target.id === 'pills-requirements-tab') {
					progressBar.style.width = '100%';
					progressBar.setAttribute('aria-valuenow', '100');
				} else {
					progressBar.style.width = '50%';
					progressBar.setAttribute('aria-valuenow', '50');
				}
			});
		});
function appendOrganizationTitle() {
    // Lấy giá trị từ Form 1
   const orgTitle = document.querySelector('input[name="title"]').value;

    // Tạo một trường ẩn trong Form 2 để gửi kèm
    const hiddenInput = document.createElement('input');
    hiddenInput.type = 'hidden';
    hiddenInput.name = 'organizationTitle';
    hiddenInput.value = orgTitle;

    // Thêm trường ẩn vào Form 2
    document.getElementById('form2').appendChild(hiddenInput);
    
    return true; // Cho phép gửi form
}