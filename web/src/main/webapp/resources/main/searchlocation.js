document.addEventListener('DOMContentLoaded', function() {
    const locationSelect = document.getElementById('locationSelect'); // Đảm bảo bạn đã có phần tử này trong HTML

    async function fetchLocations() {
        try {
            // Fetch data từ file JSON hoặc API
            const response = await fetch(_ctx + 'resources/master-data/provinces.json');
            const locations = await response.json();
            
            // Thêm các tùy chọn vào dropdown locationSelect
            locations.forEach(location => {
                const option = new Option(`${location.name} (${location.division_type})`, location.name);
                locationSelect.add(option);
            });
        } catch (error) {
            console.error('Error fetching locations:', error);
        }
    }

    // Gọi hàm fetchLocations khi DOM đã sẵn sàng
    fetchLocations();
});
