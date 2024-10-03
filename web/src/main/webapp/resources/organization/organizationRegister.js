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