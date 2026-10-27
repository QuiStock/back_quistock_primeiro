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

            if (!link || event.defaultPrevented || link.hasAttribute("data-modal")) {
                return;
            }

            const href = link.getAttribute("href");

            if (!href || href.startsWith("#") || href.startsWith("javascript:") || link.target === "_blank") {
                return;
            }

            window.mostrarLoading();
        });
    });

    //esconde o loading quando o navegador termina de voltar ou abrir a pagina
    window.addEventListener("pageshow", window.esconderLoading);
    window.addEventListener("load", window.esconderLoading);
})();
