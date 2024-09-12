
    document.querySelectorAll('input[name="sort"]').forEach((radio) => {
        radio.addEventListener('change', function() {
            const sortBy = this.id; // Lấy id của radio đã chọn
            sortJobs(sortBy);
        });
    });

    function sortJobs(sortBy) {
        const jobList = Array.from(document.querySelectorAll('.job-card'));
        
        jobList.sort((a, b) => {
            let aValue, bValue;

            switch (sortBy) {
                case 'sort1': // Ngày đăng
                    aValue = new Date(a.getAttribute('data-created-date'));
                    bValue = new Date(b.getAttribute('data-created-date'));
                    return bValue - aValue; // Sắp xếp giảm dần
                case 'sort2': // Ngày cập nhật
                    aValue = new Date(a.getAttribute('data-modified-date'));
                    bValue = new Date(b.getAttribute('data-modified-date'));
                    return bValue - aValue; // Sắp xếp giảm dần
                case 'sort3': // Kinh nghiệm
                    aValue = parseInt(a.getAttribute('data-experience'));
                    bValue = parseInt(b.getAttribute('data-experience'));
                    return aValue - bValue; // Sắp xếp tăng dần
                case 'sort4': // Lương cao đến thấp
                    aValue = parseFloat(a.getAttribute('data-salary'));
                    bValue = parseFloat(b.getAttribute('data-salary'));
                    return bValue - aValue; // Sắp xếp giảm dần
                default:
                    return 0;
            }
        });

        const jobContainer = document.querySelector('.job-list');
        jobContainer.innerHTML = ''; // Xóa danh sách hiện tại

        jobList.forEach(job => {
            jobContainer.appendChild(job); // Thêm lại các job đã sắp xếp
        });
    }
