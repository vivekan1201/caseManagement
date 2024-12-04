const maxHeight = 400;

$(function () {
    // Populate Start Date Dropdown
    const currentYear = new Date().getFullYear();
    const months = [
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    ];

    const startDateMenu = $("#startDateMenu");
    months.forEach(month => {
        const monthItem = $("<li><a href='#'>" + month + " " + currentYear + "</a></li>");
        monthItem.on("click", function (e) {
            e.preventDefault();
            $("#startDateDropdown > a").text($(this).text());
        });
        startDateMenu.append(monthItem);
    });

    // Fetch and populate Service Dropdown from API
    const serviceDropdown = $(".dropdown .drop:nth-child(1) .sub_menu");
    fetch('http://localhost:8080/api/custom-services')
        .then(response => response.json())
        .then(data => {
            data.forEach(service => {
                const serviceItem = $("<li><a href='#'>" + service.serviceName + "</a></li>");
                serviceItem.on("click", function (e) {
                    e.preventDefault();
                    $(".dropdown .drop:nth-child(1) > a").text(service.serviceName);
                });
                serviceDropdown.append(serviceItem);
            });
        })
        .catch(error => console.error('Error fetching services:', error));

    // Fetch and populate Region Dropdown from API
    const regionDropdown = $(".dropdown .drop:nth-child(3) .sub_menu");
    fetch('http://localhost:8080/api/regions')
        .then(response => response.json())
        .then(data => {
            data.forEach(region => {
                // Accessing the 'name' field from each region object
                const regionName = region.name;
                const regionItem = $("<li><a href='#'>" + regionName + "</a></li>");
                regionItem.on("click", function (e) {
                    e.preventDefault();
                    $(".dropdown .drop:nth-child(3) > a").text(regionName);
                });
                regionDropdown.append(regionItem);
            });
        })
        .catch(error => console.error('Error fetching regions:', error));

    // Add click functionality to all dropdowns
    $(".dropdown .sub_menu li a").on("click", function (e) {
        e.preventDefault();
        const selectedValue = $(this).text();
        $(this).closest(".drop").find("> a").text(selectedValue);
    });

    // Hover functionality for dropdowns
    $(".dropdown > li").hover(function () {
        const $container = $(this),
            $list = $container.find("ul"),
            $anchor = $container.find("a"),
            height = $list.height() * 1.1,
            multiplier = height / maxHeight;

        $container.data("origHeight", $container.height());
        $anchor.addClass("hover");
        $list.show().css({ paddingTop: $container.data("origHeight") });

        if (multiplier > 1) {
            $container
                .css({ height: maxHeight, overflow: "hidden" })
                .mousemove(function (e) {
                    const offset = $container.offset();
                    const relativeY =
                        (e.pageY - offset.top) * multiplier -
                        $container.data("origHeight") * multiplier;
                    if (relativeY > $container.data("origHeight")) {
                        $list.css("top", -relativeY + $container.data("origHeight"));
                    }
                });
        }
    }, function () {
        const $el = $(this);
        $el
            .height($el.data("origHeight"))
            .find("ul")
            .css({ top: 0 })
            .hide()
            .end()
            .find("a")
            .removeClass("hover");
    });

    // Handle Filter Button Click
    $("#filter-button").on("click", function () {
        // Fetch the staff name first
        fetch("http://localhost:8080/api/getUsername")
            .then(response => response.text())  // Expecting plain text as the response
            .then(staff => {
                // Get the selected filter values
                const service = $(".dropdown .drop:nth-child(1) > a").text();
                const region = $(".dropdown .drop:nth-child(3) > a").text();
                const startDate = $("#startDateDropdown > a").text();

                // Fetch filtered clients based on selected filters and staff
                fetch(`http://localhost:8080/api/filterClients?service=${service}&region=${region}&startDate=${startDate}&staff=${staff}`)
                    .then(response => response.json())
                    .then(data => {
                        const clientTableBody = $("#client-table-body");
                        clientTableBody.empty();  // Clear any existing rows

                        // Populate the table with filtered clients
                        data.forEach(client => {
                            const row = $("<tr></tr>");
                            row.append(`<td>${client.firstName} ${client.lastName}</td>`);
                            row.append(`<td>${client.email}</td>`);
                            row.append(`<td>${client.service}</td>`);
                            row.append(`<td>${client.address}</td>`);
                            row.append(`<td><button class="btn btn-info">View Details</button></td>`);
                            clientTableBody.append(row);
                        });
                    })
                    .catch(error => console.error('Error fetching filtered clients:', error));
            })
            .catch(error => console.error('Error fetching staff username:', error));
    });
});

$(function () {
    // Add click functionality to the "View Details" button dynamically
    $(document).on("click", ".btn-info", function (e) {
        const row = $(this).closest("tr");
        const name = row.find("td:nth-child(1)").text();  // Assuming the name is in the first column
        const email = row.find("td:nth-child(2)").text(); // Assuming the email is in the second column
        const phone = row.find("td:nth-child(3)").text(); // Assuming the phone is in the third column
        const address = row.find("td:nth-child(4)").text(); // Assuming the address is in the fourth column

        // Set the popup content dynamically
        const popupBody = $("#popup-body");
        popupBody.html(`
            <p><strong>Name:</strong> ${name}</p>
            <p><strong>Email:</strong> ${email}</p>
            <p><strong>Phone:</strong> ${phone}</p>
            <p><strong>Address:</strong> ${address}</p>
        `);

        // Show the popup
        $(".popup-overlay").fadeIn();
    });

    // Close popup functionality
    $(".popup-close").on("click", function () {
        $(".popup-overlay").fadeOut();
    });

    // Close popup when clicking outside of the popup
    $(".popup-overlay").on("click", function (e) {
        if ($(e.target).is(".popup-overlay")) {
            $(".popup-overlay").fadeOut();
        }
    });
})
