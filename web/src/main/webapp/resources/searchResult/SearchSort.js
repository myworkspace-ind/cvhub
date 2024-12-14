document.addEventListener('DOMContentLoaded', function() {
        const sortOptions = document.querySelectorAll('input[name="sort"]');
        const jobList = document.querySelector('.job-list');

        const sortOrderElement = document.getElementById('sortOrder');
        
        // Hàm cập nhật thông báo sắp xếp khi switch thay đổi
        sortOrderElement.addEventListener('change', function() {
            const sortSwitch = document.getElementById('sortOrder');
            const sortLabel = document.getElementById('sortLabel');
            var sortOrder = 'asc';

            if (sortSwitch.checked) {
                sortLabel.textContent = 'Sắp xếp tăng dần';
                sortOrder = 'asc';
            } else {
                sortLabel.textContent = 'Sắp xếp giảm dần';
                sortOrder = 'desc';
            }

            // lấy thông tin sort hiện tại
            const sortBy = document.querySelector('input[name="sort"]:checked').id;
            sortJobs(sortBy, sortOrder);
        });

        sortOptions.forEach(option => {
            option.addEventListener('change', function() {
                // lấy thông tin sort hiện tại
                const sortBy = this.id;

                // lấy thông tin sortOrder
                const sortOrderElement = document.getElementById('sortOrder');
                var sortOrder = 'asc';
                if (sortOrderElement.checked) {
                    sortOrder = 'asc';
                } else {
                    sortOrder = 'desc';
                }
                sortJobs(sortBy, sortOrder);
            });
        });

        function sortJobs(sortBy, sortOrder) {
            
            const jobs = Array.from(jobList.children);

            jobs.sort((a, b) => {
                switch (sortBy) {
                    case 'sort1': // Ngày đăng
                    case 'sort2': // Ngày cập nhật
                        return compareDates(b, a, '.update-tag', sortOrder);
                    case 'sort3': // Kinh nghiệm
                        return compareExperience(b, a, sortOrder);
                    case 'sort4': // Lương cao đến thấp
                        return compareSalary(b, a, sortOrder);
                    default:
                        return 0;
                }
            });

            jobs.forEach(job => jobList.appendChild(job));

            console.log(jobs, sortBy, sortOrder);
        }

        function compareDates(a, b, selector, sortOrder) {
            const textDateA = a.querySelector(selector).textContent;
            const textDateB = b.querySelector(selector).textContent;

            const dateA = new Date(textDateA);
            const dateB = new Date(textDateB);

            if (sortOrder === 'asc') {
                return dateB - dateA;
            }
            else {
                return dateA - dateB;
            }
        }

        function parseDateFromUpdateTag(updateText) {
            const match = updateText.match(/(\d+)\s+tuần/);
            const weeks = match ? parseInt(match[1]) : 0;
            const currentDate = new Date();
            return new Date(currentDate.setDate(currentDate.getDate() - (weeks * 7)));
        }

        function compareExperience(a, b, sortOrder) {
            const expA = parseExperience(a.querySelector('.location-tag').textContent);
            const expB = parseExperience(b.querySelector('.location-tag').textContent);

            if (sortOrder === 'asc') {
                return expB - expA;
            }
            else {
                return expA - expB;
            }
        }

        function parseExperience(expString) {
            const match = expString.match(/(\d+)/);
            return match ? parseInt(match[1]) : 0; // Trả về số năm, hoặc 0 nếu không tìm thấy
        }

        function compareSalary(a, b, sortOrder) {
            const salaryA = parseSalary(a.querySelector('.salary').textContent);
            const salaryB = parseSalary(b.querySelector('.salary').textContent);

            if (sortOrder === 'asc') {
                return salaryB - salaryA;
            }
            else {
                return salaryA - salaryB;
            }
        }

        function parseSalary(salaryString) {
            if (salaryString === 'Thỏa thuận') return 0;
            return parseInt(salaryString.replace(/[^\d]/g, '')) || 0; // Trả về 0 nếu không phải số
        }
    });