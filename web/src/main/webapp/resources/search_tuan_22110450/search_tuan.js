function toggleCheck(element) {
	const items = document.querySelectorAll('.box-type-keyword-below__list--item');

	// Kiểm tra nếu phần tử đang được chọn
	if (element.classList.contains('selected')) {
		// Nếu đã chọn thì bỏ chọn
		element.classList.remove('selected');
	} else {
		// Nếu chưa chọn thì bỏ chọn các phần tử khác và chọn phần tử hiện tại
		items.forEach(item => item.classList.remove('selected'));
		element.classList.add('selected');
	}
}

const filterCombobox = document.getElementById('jobRoleSelect');
const selectedFilters = document.getElementById('selected-filters');

const selectElement = document.getElementById('jobRoleSelect');

selectElement.addEventListener('change', function() {

	// Đặt lại về giá trị mặc định
	this.value = '0';
});


// Hàm để thêm filter đã chọn vào vùng display
function applyFilter(filterText, filterValue) {
	// Kiểm tra xem filter đã chọn chưa
	if (![...selectedFilters.children].some(tag => tag.dataset.value === filterValue)) {
		// Tạo phần tử mới cho filter đã chọn
		const filterTag = document.createElement('span');
		filterTag.classList.add('badge', 'rounded-pill', 'bg-primary', 'lh-1', 'position-relative', 'filter-tag', 'p-2', 'text-white');
		filterTag.dataset.value = filterValue;  // Lưu giá trị vào dataset
		filterTag.innerHTML =
			`${filterText}
                              <a class="o_delete text-white ps-1" onclick="removeFilter(this)" href="#" aria-label="Xóa"><i class="bi bi-x-lg align-text-center"></i></a>
                            `;
		selectedFilters.appendChild(filterTag);

		// Ẩn option đã chọn khỏi combobox
		const optionToHide = Array.from(filterCombobox.options).find(option => option.value === filterValue);
		if (optionToHide) {
			optionToHide.style.display = 'none';  // Ẩn option khỏi combobox
		}
	}
}

// Hàm để xóa filter khi nhấn vào nút xóa
function removeFilter(link) {
	const filterTag = link.closest('span');
	const filterValue = filterTag.dataset.value;
	filterTag.remove();

	// Hiển thị lại filter trong combobox khi xóa
	const optionToShow = Array.from(filterCombobox.options).find(option => option.value === filterValue);
	if (optionToShow) {
		optionToShow.style.display = 'block';  // Hiển thị lại option
	}
	
	console.log("Calling searchJobs");
	searchJobs(0);
}

let currentPage = 0; // Biến lưu số trang hiện tại
const pageSize = 5; // Số lượng công việc mỗi trang


//document.getElementById('searchButton').addEventListener('click', function() {
//	searchJobs(0); // Khi nhấn nút tìm kiếm, luôn bắt đầu từ trang đầu tiên
//});

// Hàm định dạng ngày theo kiểu "19 Oct, 2024"
function formatDate(dateString) {
	const date = new Date(dateString);
	const options = { day: 'numeric', month: 'short', year: 'numeric' };
	return date.toLocaleDateString('en-US', options);
}

function searchJobs(page) {
	const industry = document.getElementById('jobRoleSelect').value;
	console.log(industry);
	const keyword = document.getElementById('searchKeyword').value;
	const location = document.getElementById('locationSelect').value;
	const sort = document.getElementById('sort-advanced').value;

	// Lấy các giá trị data-value từ các phần tử filter-tag
	const filterTags = document.querySelectorAll('.filter-tag');
	let selectedIndustries = [];

	// Nếu không, lấy các giá trị data-value từ các phần tử filter-tag
	filterTags.forEach(tag => {
		selectedIndustries.push(tag.getAttribute('data-value'));
	});

//	if (selectedIndustries.length === 0) {
//		const jobRoleSelect = document.getElementById('jobRoleSelect');
//		const options = jobRoleSelect?.querySelectorAll('option') || [];
//		selectedIndustries = Array.from(options).map(option => option.value);
//	}

	console.log(selectedIndustries);

	// Tìm phần tử có class 'selected'
	const selectedItem = document.querySelector('.box-type-keyword-below__list--item.selected');
	let bool;
	if (selectedItem) {
		const keyword = selectedItem.getAttribute('data-type-keyword');
		bool = keyword === "2" ? 1 : 0;
	} else {
		console.log("Không có phần tử nào được chọn.");
		bool = 0; // Giá trị mặc định
	}

	const params = new URLSearchParams({
		keyword,
		location,
		//             industry,
		selectedIndustries: selectedIndustries.join(','),  // Chuyển thành chuỗi JSON
		sort,
		bool,
		page,
		size: pageSize
	});

	fetch(`/cvhub-web/search_job_sort_tuan?${params}`)
		.then(response => {
			if (!response.ok) {
				throw new Error('Failed to fetch data');
			}
			return response.json();
		})
		.then(data => {
			console.log('Dữ liệu nhận được từ server:', data); // In dữ liệu ra console

			const tableBody = document.querySelector('#job-table-body');
			tableBody.innerHTML = ''; // Xóa tất cả dữ liệu cũ trong bảng

			// Hiển thị danh sách công việc
			if (data.jobs && data.jobs.length > 0) {
				data.jobs.forEach(job => {
					const row = document.createElement('tr');

					// Định dạng ngày thành "19 Oct, 2024"
					const formattedDate = formatDate(job.createdDate);

					row.innerHTML = `
                            <td><img
                                src="/cvhub-web/images/${job.organization.logoID}"
                                alt="Logo" class="img-responsive"></td>
                            <td>${job.title}</td>
                            <td>${job.organization.title}</td>
                            <td>${job.location.name}</td>
                            <td>${job.jobRole.title}</td>
                            <td>${job.experience}</td>
                            <td>${job.salary}</td>
                            <td>${formattedDate}</td>
                            <td>
                                <a href="/cvhub-web/jobrequests/${job.id}" class="btn btn-primary">Chi tiết</a>
                            </td>
                        `;
					tableBody.appendChild(row);
				});
			} else {
				tableBody.innerHTML = '<tr><td colspan="8">Không có công việc nào phù hợp.</td></tr>';
			}

			// Cập nhật phân trang
			renderPagination(data.currentPage, data.totalPages);
		})
		.catch(error => {
			console.error('Error:', error);
			const tableBody = document.querySelector('#job-table-body');
			tableBody.innerHTML = '<tr><td colspan="8">Đã có lỗi xảy ra khi tải dữ liệu. Vui lòng thử lại sau.</td></tr>';
		});
}

function renderPagination(currentPage, totalPages) {
	const pagination = document.getElementById('pagination');
	pagination.innerHTML = ''; // Xóa nội dung phân trang cũ

	// Nút Previous
	if (currentPage > 0) {
		const prevButton = document.createElement('button');
		prevButton.textContent = 'Previous';
		prevButton.innerHTML = '<i class="bi bi-chevron-left"></i>'; // Icon
		prevButton.classList.add('btn', 'btn-outline-primary', 'page-item', 'page-link');
		prevButton.addEventListener('click', () => searchJobs(currentPage - 1));
		pagination.appendChild(prevButton);
	}

	// Hiển thị thông tin trang
	const pageInfo = document.createElement('span');
	pageInfo.textContent = ` Page ${currentPage + 1} of ${totalPages} `;
	pageInfo.classList.add('mx-2', 'page-info', 'page-item', 'fw-bold');
	pagination.appendChild(pageInfo);

	// Nút Next
	if (currentPage + 1 < totalPages) {
		const nextButton = document.createElement('button');
		nextButton.textContent = 'Next';
		nextButton.innerHTML = '<i class="bi bi-chevron-right"></i>'; // Icon
		nextButton.classList.add('btn', 'btn-outline-primary', 'page-item', 'page-link');
		nextButton.addEventListener('click', () => searchJobs(currentPage + 1));
		pagination.appendChild(nextButton);
	}
}

// Lấy phần tử riêng biệt
const searchName = document.getElementById('search-for-name');
toggleCheck(searchName)


// Lấy dữ liệu về location và job role
document.addEventListener('DOMContentLoaded', function() {
	const locationSelect = document.getElementById('locationSelect');
	const jobRoleSelect = document.getElementById('jobRoleSelect');
	async function fetchLocations() {
		try {
			/*const response = await fetch('https://provinces.open-api.vn/api/?depth=1');*/
			const response = await fetch(_ctx + 'resources/master-data/provinces.json');
			const locations = await response.json();

			locations.forEach(location => {
				const option = new Option(`${location.name} (${location.division_type})`, location.code);
				locationSelect.add(option);
			});
		} catch (error) {
			console.error('Error fetching locations:', error);
		}
	}

	async function fetchJobRoles() {
		try {
			const response = await fetch('/cvhub-web/job-roles');
			if (!response.ok) {
				throw new Error(`HTTP error! status: ${response.status}`);
			}
			const jobRoles = await response.json(); // Sử dụng phương thức .json() để phân tích cú pháp
			jobRoles.forEach(role => {
				const option = new Option(role.title, role.id);
				jobRoleSelect.add(option);
			});
		} catch (error) {
			console.error('Error loading job roles:', error);
			jobRoleSelect.innerHTML = '<option value="">Error loading job roles</option>';
		}
	}


	Promise.all([fetchJobRoles(), fetchLocations()])
		.then(() => {
			searchJobs(0); // Chỉ chạy khi dữ liệu đã được tải xong
		})
		.catch(error => {
			console.error('Error fetching job roles or locations:', error);
		});


});

