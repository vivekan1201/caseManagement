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
});



