document.getElementById('jobForm').addEventListener('submit', function(event) {
  event.preventDefault();
  
  // Collect form data
  var formData = new FormData(this);
  
  // Send POST request
  fetch('/registerJob', {
    method: 'POST',
    body: formData
  })
  .then(response => response.json())
  .then(data => {
    // Handle success
    $('#addJobModal').modal('hide');
    // Redirect to organization page
    window.location.href = '/organization?id=' + data.organizationId;
  })
  .catch(error => {
    // Handle error
    console.error('Error:', error);
    alert('Có lỗi xảy ra khi đăng ký công việc: ' + error.message);
  });
});