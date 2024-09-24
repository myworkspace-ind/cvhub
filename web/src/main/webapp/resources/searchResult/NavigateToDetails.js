$(function () {
  $('.job-card').on('click', function(e) {
    // Kiểm tra xem click có phải là trên phần tử thuộc về form organization không
    if (!$(e.target).closest('.job-organization').length) {
      // Nếu không phải, submit form job details
      $(this).submit();
    }
  });

  $('.job-organization').on('click', function(e) {
    e.preventDefault(); // Ngăn chặn hành vi mặc định
    e.stopPropagation(); // Ngăn chặn sự kiện lan truyền lên job-card
    $(this).submit(); // Submit form organization
  });
});