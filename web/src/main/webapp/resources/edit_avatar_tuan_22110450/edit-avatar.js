// Lấy các phần tử từ DOM
const uploadBtn = document.getElementById('upload-btn'); // Nút mở input file
const imageInput = document.getElementById('imageInput'); // Input file ẩn
const avatar = document.getElementById('avatar'); // Ảnh đại diện
const cropperModal = new bootstrap.Modal(document.getElementById('cropperModal')); // Modal chỉnh sửa ảnh
const cropperImage = document.getElementById('cropperImage'); // Ảnh hiển thị trong modal
const zoomRange = document.getElementById('zoom-range'); // Thanh zoom
const saveBtn = document.getElementById('save-btn'); // Nút lưu ảnh

let cropper; // Biến để lưu đối tượng Cropper

// Khi nhấn nút upload, mở input file
uploadBtn.addEventListener('click', () => {
	imageInput.click();
});

// Khi người dùng chọn file ảnh
imageInput.addEventListener('change', (e) => {
	const file = e.target.files[0];
	if (file) {
		const reader = new FileReader();
		reader.onload = (event) => {
			cropperImage.src = event.target.result; // Gán ảnh vào modal
			cropperModal.show(); // Hiển thị modal
		};
		reader.readAsDataURL(file); // Đọc file ảnh
	}
});

// Khởi tạo Cropper khi modal được mở
document.getElementById('cropperModal').addEventListener('shown.bs.modal', () => {
	if (cropper) cropper.destroy(); // Hủy cropper cũ nếu có
	cropper = new Cropper(cropperImage, {
		aspectRatio: 1, // Khung crop hình vuông
		viewMode: 1,
		dragMode: 'move',
		zoomable: true,
		movable: true,
		background: false,
	});

	// Điều chỉnh zoom bằng thanh trượt
	zoomRange.addEventListener('input', (e) => {
		cropper.zoomTo(e.target.value);
	});
});

// Khi nhấn nút "Lưu"
saveBtn.addEventListener('click', () => {
	const canvas = cropper.getCroppedCanvas({
		width: 200,
		height: 200,
	});
	//            avatar.src = canvas.toDataURL();// Gán ảnh mới vào avatar
	//            alert('Ảnh đã được lưu thành công!');
	// Chuyển canvas thành Blob (ảnh định dạng PNG)
	
	canvas.toBlob((blob) => {
		// Tạo đối tượng File từ Blob
		const file = new File([blob], "avatar.png", { type: "image/png" });

		// Sử dụng DataTransfer để tạo FileList cho input file
		const dataTransfer = new DataTransfer();
		dataTransfer.items.add(file);

		// Gắn FileList vào input ẩn
		const fileInput = document.getElementById('fileInput');
		fileInput.files = dataTransfer.files;

		// Gửi form sau khi gắn file vào input
		fileInput.form.submit();
	}, 'image/png'); // Định dạng ảnh là PNG
	
//	cropperModal.hide();// Đóng modal
});