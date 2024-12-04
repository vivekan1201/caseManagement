$(document).ready(function() {
    var animating = false; // Flag to prevent multiple animations
    var current_fs, next_fs, previous_fs; // Variables to hold fieldset elements

    // For the "next" button
    $(".next").click(function() {
        if (animating) return false; // Prevent multiple clicks
        animating = true;

        current_fs = $(this).parent();  // Get current fieldset
        next_fs = $(this).parent().next(); // Get next fieldset

        // Activate next step on progressbar
        $("#progressbar li").eq($("fieldset").index(next_fs)).addClass("active");

        // Show the next fieldset with animation
        next_fs.show();
        current_fs.animate({ opacity: 0 }, {
            step: function(now, mx) {
                // Define animation effects
                scale = 1 - (1 - now) * 0.2;
                left = (now * 50) + "%";
                opacity = 1 - now;
                current_fs.css({
                    'transform': 'scale(' + scale + ')',
                    'position': 'absolute'
                });
                next_fs.css({ 'left': left, 'opacity': opacity });
            },
            duration: 800,
            complete: function() {
                current_fs.hide();
                animating = false;
            },
            easing: 'swing'  // Use a valid easing function
        });
    });

    // For the "previous" button
    $(".previous").click(function() {
        if (animating) return false; // Prevent multiple clicks
        animating = true;

        current_fs = $(this).parent();  // Get current fieldset
        previous_fs = $(this).parent().prev(); // Get previous fieldset

        // Deactivate current step on progressbar
        $("#progressbar li").eq($("fieldset").index(current_fs)).removeClass("active");

        // Show the previous fieldset with animation
        previous_fs.show();
        current_fs.animate({ opacity: 0 }, {
            step: function(now, mx) {
                // Define animation effects
                scale = 0.8 + (1 - now) * 0.2;
                left = ((1 - now) * 50) + "%";
                opacity = 1 - now;
                current_fs.css({
                    'left': left,
                    'opacity': opacity
                });
                previous_fs.css({
                    'transform': 'scale(' + scale + ')',
                    'opacity': opacity
                });
            },
            duration: 800,
            complete: function() {
                current_fs.hide();
                animating = false;
            },
            easing: 'swing'  // Use a valid easing function
        });
    });

    // Initialize progress bar or other parts of the form if needed
    // Example: Initialize the first step as active
    $("#progressbar li").eq(0).addClass("active");
});

/*--------------------------------------------------------------------------------*/
//submit Logic
let username = '';

    fetch('http://localhost:8080/api/getUsername')
    .then(response => response.text()) // Get the response as text
    .then(data => {
        username = data;
        // Update the username displayed on the page
    //   document.querySelector('.username').textContent = `Logged in as: ${username}`;
    })
    .catch(error => {
        console.error('Error fetching username:', error);
        document.querySelector('.username').textContent = 'Logged in as: Guest'; // Fallback if error occurs
    });

    document.getElementById('msform').addEventListener('submit', function(event) {
        event.preventDefault(); // Prevent the default form submission

        const formData = new FormData(this); // Gather form data

        // Add the username as 'staff' to the form data
        formData.append('staff', username);

        // Send POST request
        fetch('http://localhost:8080/api/submitTask', {
            method: 'POST',
            body: formData
        })
        .then(response => {
            if (response.ok) {
                alert('Mail sent successfully!');
                this.reset(); // Reset the form after successful submission
            } else {
                throw new Error('Failed to send mail.');
            }
        })
        .catch(error => {
            alert(error.message); // Show error message
        });
    });

    // URL of the API endpoint
    const apiURL = "http://localhost:8080/api/custom-services";

    // Function to populate dropdown
    async function populateDropdown() {
        try {
            // Fetch data from API
            const response = await fetch(apiURL);

            // Check if the response is OK
            if (!response.ok) {
                throw new Error("Failed to fetch services");
            }

            // Parse the JSON response
            const services = await response.json();

            // Get the dropdown element
            const dropdown = document.getElementById("service");

            // Clear existing options (if any)
            dropdown.innerHTML = `
                <option value="" disabled selected>Select a Service Type</option>
            `;

            // Populate dropdown with services
            services.forEach(service => {
                const option = document.createElement("option");
                option.value = service.serviceName; // Use serviceName as value
                option.textContent = service.serviceName; // Display serviceName
                dropdown.appendChild(option);
            });
        } catch (error) {
            console.error("Error populating dropdown:", error);
        }
    }

    // Call the function on page load
    document.addEventListener("DOMContentLoaded", populateDropdown);
