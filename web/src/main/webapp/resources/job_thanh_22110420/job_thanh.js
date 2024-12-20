// Initialize DataTable
$(document).ready(function () {
    $('#jobTable').DataTable({
        paging: true, // Enable pagination
        searching: true, // Enable search bar
        ordering: true, // Enable column sorting
        info: true, // Display table information
        pageLength: 5, // Default rows per page
        lengthMenu: [5, 10, 25, 50], // Rows per page options
        responsive: true, // Make the table responsive
        language: {
            search: "Search Jobs:", // Customize search label
            paginate: {
                previous: "Previous",
                next: "Next"
            },
            info: "Showing _START_ to _END_ of _TOTAL_ jobs",
            lengthMenu: "Show _MENU_ jobs"
        }
    });

    // Add hover effect to rows
    $('#jobTable tbody').on('mouseenter', 'tr', function () {
        $(this).css('background-color', '#d6e9f8');
    });

    $('#jobTable tbody').on('mouseleave', 'tr', function () {
        $(this).css('background-color', '');
    });

    // Apply button click event
    $('.apply-btn').on('click', function () {
        const jobTitle = $(this).closest('tr').find('td:nth-child(2)').text();
        alert(`You have applied for the job: ${jobTitle}`);
    });
});
