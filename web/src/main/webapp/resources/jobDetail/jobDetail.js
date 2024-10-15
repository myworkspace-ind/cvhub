document.querySelectorAll('textarea').forEach(textarea => {
  textarea.addEventListener('input', function() {
    // Thay thế nhiều dấu xuống dòng liên tiếp bằng một dấu xuống dòng
    this.value = this.value.replace(/\n{3,}/g, '\n\n');
    
    // Thay thế dấu xuống dòng bằng ký tự đặc biệt (ví dụ: |)
    this.value = this.value.replace(/\n/g, '|');
  });
});

document.querySelector('form').addEventListener('submit', function(e) {
  document.querySelectorAll('textarea').forEach(textarea => {
    // Loại bỏ ký tự | ở đầu và cuối
    textarea.value = textarea.value.replace(/^\|+|\|+$/g, '');
  });
});