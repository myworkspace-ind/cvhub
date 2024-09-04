 document.addEventListener('DOMContentLoaded', function() {
        const locationSelect = document.getElementById('locationSelect');
        const locationSearch = document.getElementById('locationSearch');
        async function fetchLocations() {
            try {
                const response = await fetch('https://provinces.open-api.vn/api/?depth=1');
                const locations = await response.json();

                while (locationSelect.options.length > 1) {
                    locationSelect.remove(1);
                }

                locations.forEach(location => {
                    const optionText = `${location.name} (${location.division_type})`;
                    const option = new Option(optionText, location.code);
                    locationSelect.add(option);
                });
            } catch (error) {
                console.error('Error fetching locations:', error);
            }
        }

        fetchLocations();

        locationSearch.addEventListener('input', function() {
            const filterValue = this.value.toLowerCase();
            Array.from(locationSelect.options).forEach(option => {
                const optionText = option.text.toLowerCase();
                if (optionText.includes(filterValue) || option.value === '') {
                    option.style.display = 'block';
                } else {
                    option.style.display = 'none';
                }
            });
        });
    });