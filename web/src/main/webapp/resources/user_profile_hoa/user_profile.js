function getHocVan() {
    // Lấy dữ liệu từ Modal
    
    const schoolName = document.getElementById("schoolName").value.trim();
    const degree = document.getElementById("degree").options[document.getElementById("degree").selectedIndex].text;
    const startDate = document.getElementById("startDate").value;
    const endDate = document.getElementById("endDate").value;
	
	if (!schoolName)
		{alert("Vui lòng nhập tên trường!"); return;}
    if (!degree)
		{alert("Vui lòng chọn bằng cấp!"); return;}
    if (!startDate)
		{alert("Vui lòng nhập ngày bắt đầu!"); return;}
    if (!endDate)
		{alert("Vui lòng nhập ngày kết thúc!"); return;}

    // Truyền dữ liệu từ Modal lên Form chính
    document.getElementById("mainSchoolName").value = schoolName;
    document.getElementById("mainDegree").value = degree;
    document.getElementById("mainStartDate").value = startDate;
    document.getElementById("mainEndDate").value = endDate;


    const mainLearning = document.getElementById("main-learning");
	mainLearning.style.display = "block";
}
function getKiNang() {
	// Lấy dữ liệu từ Modal
	const skillName = document.getElementById("skillName").value.trim();
	const sliderValue = document.getElementById("slider").value.trim();

	// Kiểm tra xem có nhập đủ thông tin không
	if (!skillName) {
		alert("Vui lòng nhập tên kỹ năng!");
		return;
	}

	// Tạo phần tử kỹ năng mới
	const skillItem = document.createElement("div");
	skillItem.classList.add("skill-item", "border", "rounded", "p-2", "mb-2", "bg-white");
	skillItem.innerHTML = `
		<div class="row">
			<div class="col-md-6">
				<strong>Kỹ năng:</strong> ${skillName}
			</div>
			<div class="col-md-4">
				<strong>Thành thạo:</strong> ${sliderValue}/5
			</div>
			<div class="col-md-2 text-end">
				<button type="button" class="btn btn-sm btn-danger" onclick="deleteSkill(this)">Xóa</button>
			</div>
		</div>
	`;

	// Thêm kỹ năng mới vào danh sách
	const skillsList = document.getElementById("skills-list");
	skillsList.appendChild(skillItem);

	// Đóng Modal và xóa dữ liệu nhập vào

	/*const modal = bootstrap.Modal.getInstance(document.getElementById('skillModal'));
	modal.hide();*/
	document.getElementById("skillName").value = "";
	document.getElementById("slider").value = "3";
}

// Hàm xóa kỹ năng
function deleteSkill(button) {
	const skillItem = button.closest(".skill-item");
	skillItem.remove();
}

// Lưu danh sách ngoại ngữ
let languages = [];

// Hàm để lấy dữ liệu từ Modal và thêm vào danh sách
function getNgoaiNgu() {
    // Lấy giá trị từ các trường trong modal
    const languageName = document.getElementById("languageName").value.trim();
    const proficiency = document.getElementById("slider1").value.trim();
    const certification = document.getElementById("certi").value.trim();

    // Kiểm tra dữ liệu nhập
    if (!languageName) {
        alert("Vui lòng nhập tên ngoại ngữ!");
        return;
    }
    if (!certification) {
        alert("Vui lòng nhập bằng cấp!");
        return;
    }

    // Thêm ngoại ngữ vào danh sách
    languages.push({
        name: languageName,
        proficiency: parseInt(proficiency),
        certification: certification
    });

    // Hiển thị ngoại ngữ trong danh sách
    const languageItem = document.createElement("div");
    languageItem.classList.add("language-item", "border", "rounded", "p-2", "mb-2", "bg-white");
    languageItem.innerHTML = `
        <div class="row">
            <div class="col-md-4"><strong>Ngoại ngữ:</strong> ${languageName}</div>
            <div class="col-md-3"><strong>Thành thạo:</strong> ${proficiency}/5</div>
            <div class="col-md-3"><strong>Bằng cấp:</strong> ${certification}</div>
            <div class="col-md-2 text-end">
                <button type="button" class="btn btn-sm btn-danger" onclick="deleteLanguage(this, '${languageName}')">Xóa</button>
            </div>
        </div>
    `;

    const languagesList = document.getElementById("languages-list");
    languagesList.appendChild(languageItem);

    // Đóng Modal và reset các trường
	
    document.getElementById("languageName").value = "";
    document.getElementById("slider1").value = "3";
    document.getElementById("certi").value = "";
}

// Hàm để xóa một ngoại ngữ khỏi danh sách
function deleteLanguage(button, languageName) {
    // Xóa khỏi giao diện
    const languageItem = button.closest(".language-item");
    languageItem.remove();

    // Xóa khỏi danh sách
    languages = languages.filter(language => language.name !== languageName);
}

let certificates = [];

function getChungChi() {
    // Get input values from modal
    const name = document.getElementById('certificateName').value.trim();
    const auth = document.getElementById('certificateAuth').value.trim();
    const startDate = document.getElementById('cc-startDate').value.trim();
    const endDate = document.getElementById('cc-endDate').value.trim();

    // Add certificate to the array
    certificates.push({ name, auth, startDate, endDate });

    // Clear modal inputs
    document.getElementById('certificateName').value = '';
    document.getElementById('certificateAuth').value = '';
    document.getElementById('cc-startDate').value = '';
    document.getElementById('cc-endDate').value = '';

    // Close the modal

    // Update the certificate list
    updateCertificateList();
}

// Function to render the list of certificates
function updateCertificateList() {
    const certificateList = document.getElementById('certificateList');
    certificateList.innerHTML = ''; // Clear existing list

    certificates.forEach((certificate, index) => {
        const certificateItem = document.createElement("div");
        certificateItem.classList.add("certificate-item", "border", "rounded", "p-2", "mb-2", "bg-white");

        certificateItem.innerHTML = `
            <div class="row">
                <div class="col-md-4"><strong>Tên chứng chỉ:</strong> ${certificate.name}</div>
                <div class="col-md-3"><strong>Cấp bởi:</strong> ${certificate.auth}</div>
                <div class="col-md-3"><strong>Thời gian:</strong> ${certificate.startDate} - ${certificate.endDate}</div>
                <div class="col-md-2 text-end">
                    <button type="button" class="btn btn-sm btn-danger" onclick="removeCertificate(${index})">Xóa</button>
                </div>
            </div>
        `;

        certificateList.appendChild(certificateItem);
    });
}

// Function to remove a certificate
function removeCertificate(index) {
    certificates.splice(index, 1); // Remove the selected certificate
    updateCertificateList(); // Refresh the list
}

// Lưu danh sách kinh nghiệm
let experiences = [];

function getKNLV() {
    const company = document.getElementById('company').value.trim();
    const role = document.getElementById('position').value.trim();
    const workingDayFrom = document.getElementById('startDateWorking').value.trim();
    const workingDayTo = document.getElementById('endDateWorking').value.trim();
    const des = document.getElementById('description').value.trim();

    // Thêm vào danh sách kinh nghiệm
    experiences.push({ company, role, workingDayFrom, workingDayTo, des });
    
    // Clear modal inputs
    document.getElementById('company').value = '';
    document.getElementById('position').value = '';
    document.getElementById('startDateWorking').value = '';
    document.getElementById('endDateWorking').value = '';
    document.getElementById('description').value = '';


    // Update the experience list
    updateKNLVList();
}

function updateKNLVList() {
    const experiencesList = document.getElementById('experienceList');
    experiencesList.innerHTML = ''; // Clear existing list

    experiences.forEach((experience, index) => {
        // Hiển thị kinh nghiệm trong danh sách
        const experienceItem = document.createElement("div");
        experienceItem.classList.add("experience-item", "border", "rounded", "p-2", "mb-2", "bg-white");
        experienceItem.innerHTML = `
        	<div class="row mb-3">
            <div class="col-md-6">
                <strong>Công ty:</strong> ${experience.company}
            </div>
            <div class="col-md-6">
                <strong>Chức danh:</strong> ${experience.role}
            </div>
        </div>
        <div class="row mb-3">
            <div class="col-md-6">
                <strong>Từ:</strong> ${experience.workingDayFrom} <strong>Đến:</strong> ${experience.workingDayTo}
            </div>
            <div class="col-md-6">
            	<strong>Mô tả:</strong> 
            	<span class="description">${experience.des}</span>
        	</div>
        </div>
        <div class="row">
            <div class="col-md-12 text-end">
                <button type="button" class="btn btn-sm btn-danger" onclick="deleteKNLV(${index})">
                    Xóa
                </button>
            </div>
        </div>

        `;

        experiencesList.appendChild(experienceItem);
    });
}

function deleteKNLV(index) {
    experiences.splice(index, 1); // Remove the selected experience
    updateKNLVList(); // Refresh the list
}