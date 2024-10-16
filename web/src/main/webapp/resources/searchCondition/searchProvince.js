document.addEventListener('DOMContentLoaded', function() {
        const locationSelect = document.getElementById('locationSelect');
        const jobRoleSelect = document.getElementById('jobRoleSelect');
        async function fetchLocations() {
            try {
                const response = await fetch('https://provinces.open-api.vn/api/?depth=1');
                const locations = await response.json();
                
                locations.forEach(location => {
                    const option = new Option(`${location.name} (${location.division_type})`, location.code);
                    locationSelect.add(option);
                });
            } catch (error) {
                console.error('Error fetching locations:', error);
            }
        }

        async function fetchJobRoles() {
    try {
        const response = await fetch('/cvhub-web/job-roles');
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const jobRoles = await response.json(); // Sử dụng phương thức .json() để phân tích cú pháp
        jobRoles.forEach(role => {
            const option = new Option(role.title, role.id);
            jobRoleSelect.add(option);
        });
    } catch (error) {
        console.error('Error loading job roles:', error);
        jobRoleSelect.innerHTML = '<option value="">Error loading job roles</option>';
    }
}
		fetchJobRoles();
        fetchLocations();
    
});