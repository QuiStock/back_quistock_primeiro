(function () {

    function criarLoading() {
        let loading = document.getElementById("loading");

        if (loading) {
            return loading;
        }

        //cria a tela de carregamento caso a pagina nao tenha colocado o html
        loading = document.createElement("div");
        loading.id = "loading";
        loading.className = "loading hidden";
        loading.setAttribute("aria-hidden", "true");
        loading.innerHTML = '<div class="chaotic-orbit"></div>';
        document.body.appendChild(loading);

        return loading;
    }

    //mostra o carregamento enquanto a proxima acao acontece
    window.mostrarLoading = function () {
        const loading = criarLoading();

        loading.classList.remove("hidden");
        loading.setAttribute("aria-hidden", "false");
    };

    //esconde o carregamento quando a pagina termina de abrir
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

        //mostra o carregamento em qualquer formulario de crud enviado
        document.addEventListener("submit", function (event) {
            if (!event.defaultPrevented) {
                window.mostrarLoading();
            }
        });

        //mostra o carregamento quando for trocar de tela
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

    //esconde o loading quando o navegador termina de voltar ou abrir a pagina
    window.addEventListener("pageshow", window.esconderLoading);
    window.addEventListener("load", window.esconderLoading);
})();
