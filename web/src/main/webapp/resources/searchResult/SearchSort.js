document.addEventListener('DOMContentLoaded', function() {
        const sortOptions = document.querySelectorAll('input[name="sort"]');
        const jobList = document.querySelector('.job-list');

        sortOptions.forEach(option => {
            option.addEventListener('change', function() {
                const sortBy = this.id;
                sortJobs(sortBy);
            });
        });

        function sortJobs(sortBy) {
            const jobs = Array.from(jobList.children);

            jobs.sort((a, b) => {
                switch (sortBy) {
                    case 'sort1': // Ngày đăng
                    case 'sort2': // Ngày cập nhật
                        return compareDates(b, a, '.update-tag');
                    case 'sort3': // Kinh nghiệm
                        return compareExperience(a, b);
                    case 'sort4': // Lương cao đến thấp
                        return compareSalary(b, a);
                    default:
                        return 0;
                }
            });

            jobs.forEach(job => jobList.appendChild(job));
        }

        function compareDates(a, b, selector) {
            const dateA = parseDateFromUpdateTag(a.querySelector(selector).textContent);
            const dateB = parseDateFromUpdateTag(b.querySelector(selector).textContent);
            return dateA - dateB;
        }

        function parseDateFromUpdateTag(updateText) {
            const match = updateText.match(/(\d+)\s+tuần/);
            const weeks = match ? parseInt(match[1]) : 0;
            const currentDate = new Date();
            return new Date(currentDate.setDate(currentDate.getDate() - (weeks * 7)));
        }

        function compareExperience(a, b) {
            const expA = parseExperience(a.querySelector('.location-tag').textContent);
            const expB = parseExperience(b.querySelector('.location-tag').textContent);
            return expA - expB;
        }

        function parseExperience(expString) {
            const match = expString.match(/(\d+)/);
            return match ? parseInt(match[1]) : 0; // Trả về số năm, hoặc 0 nếu không tìm thấy
        }

        function compareSalary(a, b) {
            const salaryA = parseSalary(a.querySelector('.salary').textContent);
            const salaryB = parseSalary(b.querySelector('.salary').textContent);
            return salaryA - salaryB;
        }

        function parseSalary(salaryString) {
            if (salaryString === 'Thỏa thuận') return 0;
            return parseInt(salaryString.replace(/[^\d]/g, '')) || 0; // Trả về 0 nếu không phải số
        }
    });