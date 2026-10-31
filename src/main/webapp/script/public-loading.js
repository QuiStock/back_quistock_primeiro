// External apps and new tabs leave this page open, so clear the overlay shortly after.
document.addEventListener("click", function (event) {
    if (event.defaultPrevented || event.button !== 0 || event.ctrlKey || event.metaKey || event.shiftKey || event.altKey) return;

    const navigation = event.target.closest("[data-loading-navigation]");
    if (navigation) {
        window.mostrarLoading();
        return;
    }

    const external = event.target.closest("[data-loading-external], a[target='_blank'], a[href^='mailto:']");
    if (external && !external.closest("#interacao")) {
        window.mostrarLoading();
        window.setTimeout(window.esconderLoading, 700);
    }
}, true);
