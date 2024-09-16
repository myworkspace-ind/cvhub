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
            switch(sortBy) {
                case 'sort1': // Ngày đăng
                    return compareDates(b, a, '.modified-date');
                case 'sort2': // Ngày cập nhật
                    return compareDates(b, a, '.modified-date');
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
        const dateA = new Date(a.querySelector(selector).textContent);
        const dateB = new Date(b.querySelector(selector).textContent);
        return dateA - dateB;
    }

    function compareExperience(a, b) {
        const expA = parseInt(a.querySelector('.experience').textContent);
        const expB = parseInt(b.querySelector('.experience').textContent);
        return expA - expB;
    }

    function compareSalary(a, b) {
        const salaryA = parseSalary(a.querySelector('.text-success').textContent);
        const salaryB = parseSalary(b.querySelector('.text-success').textContent);
        return salaryA - salaryB;
    }

    function parseSalary(salaryString) {
        if (salaryString === 'Thỏa thuận') return 0;
        return parseInt(salaryString.replace('$', '').replace(',', ''));
    }
});