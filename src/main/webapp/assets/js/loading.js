(function () {

    function criarLoading() {
        let loading = document.getElementById("loading");

        if (loading) {
            return loading;
        }

        // Add the overlay when the page does not include it.
        loading = document.createElement("div");
        loading.id = "loading";
        loading.className = "loading hidden";
        loading.setAttribute("aria-hidden", "true");
        loading.innerHTML = '<div class="chaotic-orbit"></div>';
        document.body.appendChild(loading);

        return loading;
    }

    // Show the overlay while navigation is in progress.
    window.mostrarLoading = function () {
        const loading = criarLoading();

        loading.classList.remove("hidden");
        loading.setAttribute("aria-hidden", "false");
    };

    // Hide it once the page is ready.
    window.esconderLoading = function () {
        const loading = document.getElementById("loading");

        if (!loading) {
            return;
        }

        loading.classList.add("hidden");
        loading.setAttribute("aria-hidden", "true");
    };

    document.addEventListener("DOMContentLoaded", function () {
        criarLoading();

        // Show the overlay after a valid CRUD form is submitted.
        document.addEventListener("submit", function (event) {
            if (!event.defaultPrevented) {
                const modal = event.target.closest("dialog[open], [popover]:popover-open");

                // Close the modal before showing the overlay.
                if (modal) {
                    if (modal.matches("dialog")) {
                        modal.close();
                    } else {
                        modal.hidePopover();
                    }
                }

                window.mostrarLoading();
            }
        });

        // Show the overlay when leaving the page.
        document.addEventListener("click", function (event) {
            const link = event.target.closest("a");

            if (!link || event.defaultPrevented || event.button !== 0 || event.ctrlKey || event.metaKey || event.shiftKey || event.altKey || link.hasAttribute("data-modal") || link.hasAttribute("download")) {
                return;
            }

            const href = link.getAttribute("href");

            if (!href || link.target && link.target !== "_self") {
                return;
            }

            const destino = new URL(link.href, window.location.href);
            if (destino.protocol !== "http:" && destino.protocol !== "https:") {
                return;
            }
            if (destino.href === window.location.href ||
                destino.pathname === window.location.pathname && destino.search === window.location.search && destino.hash) {
                return;
            }

            window.mostrarLoading();
        });
    });

    // Clear the overlay after back or forward navigation.
    window.addEventListener("pageshow", window.esconderLoading);
    window.addEventListener("load", window.esconderLoading);
})();
