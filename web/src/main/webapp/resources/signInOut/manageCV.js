function setPrimaryCV(cvId) {
    fetch(`/cv/${cvId}/setPrimary`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(response => {
        if (response.ok) {
            alert('Đã đặt làm CV chính');
            location.reload();
        }
    });
} 