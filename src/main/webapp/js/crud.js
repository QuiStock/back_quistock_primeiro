/*
 * crud.js - comportamento ÚNICO de todas as telas de lista.
 *   - clicar numa linha a seleciona (só ganha a borda roxa);
 *   - clicar fora da lista desfaz a seleção;
 *   - Editar / Remover sem nada selecionado mostram um aviso;
 *   - Adicionar / Editar abrem o popup com o formulário;
 *   - Remover abre o popup de confirmação.
 * Quem grava no banco é sempre o Servlet; aqui só controlamos a tela.
 */
(function () {
    'use strict';

    const popup = document.getElementById('popup');
    if (!popup) {
        return; // página sem CRUD (ex.: Início)
    }

    const formSalvar = document.getElementById('formSalvar');
    const formRemover = document.getElementById('formRemover');
    const titulo = document.getElementById('popupTitulo');
    const singular = popup.dataset.singular;

    const tabela = document.getElementById('tabela');
    const aviso = document.getElementById('aviso');
    const btnAdicionar = document.getElementById('btnAdicionar');
    const btnEditar = document.getElementById('btnEditar');
    const btnRemover = document.getElementById('btnRemover');

    let selecionada = null;
    let timerAviso = null;

    // mostra a mensagem por 3 segundos
    function mostrarAviso(texto) {
        if (!aviso) {
            return;
        }
        aviso.textContent = texto;
        aviso.hidden = false;
        clearTimeout(timerAviso);
        timerAviso = setTimeout(function () {
            aviso.hidden = true;
        }, 3000);
    }

    function selecionar(linha) {
        if (selecionada) {
            selecionada.classList.remove('selecionada');
        }
        selecionada = linha;
        selecionada.classList.add('selecionada');
    }

    function desselecionar() {
        if (selecionada) {
            selecionada.classList.remove('selecionada');
        }
        selecionada = null;
    }

    // mostra só um dos dois formulários dentro do popup e abre ele
    function abrir(form) {
        formSalvar.hidden = (form !== formSalvar);
        formRemover.hidden = (form !== formRemover);
        popup.showModal();
    }

    // ---- seleção: clicar numa linha ----
    if (tabela) {
        tabela.addEventListener('click', function (e) {
            const linha = e.target.closest('.linha');
            if (linha) {
                selecionar(linha);
            }
        });
    }

    // ---- desseleção: clicar fora da lista ----
    // Não desfaz ao clicar numa linha, nos botões da barra (Editar/Remover) nem no popup.
    document.addEventListener('click', function (e) {
        if (!selecionada) {
            return;
        }
        if (e.target.closest('.linha, #botoes, dialog')) {
            return;
        }
        desselecionar();
    });

    // ---- Adicionar ----
    if (btnAdicionar) {
        btnAdicionar.addEventListener('click', function () {
            formSalvar.reset();
            formSalvar.elements['id'].value = '';
            titulo.textContent = 'Adicionar ' + singular;
            abrir(formSalvar);
        });
    }

    // ---- Editar: precisa de uma linha selecionada ----
    if (btnEditar) {
        btnEditar.addEventListener('click', function () {
            if (!selecionada) {
                mostrarAviso('Selecione um registro na lista para editar.');
                return;
            }
            formSalvar.reset();
            formSalvar.elements['id'].value = selecionada.dataset.id;
            selecionada.querySelectorAll('[data-campo]').forEach(function (celula) {
                const campo = formSalvar.elements[celula.dataset.campo];
                if (campo) {
                    campo.value = celula.textContent;
                }
            });
            titulo.textContent = 'Editar ' + singular;
            abrir(formSalvar);
        });
    }

    // ---- Remover: precisa de uma linha selecionada ----
    if (btnRemover) {
        btnRemover.addEventListener('click', function () {
            if (!selecionada) {
                mostrarAviso('Selecione um registro na lista para remover.');
                return;
            }
            formRemover.elements['id'].value = selecionada.dataset.id;
            abrir(formRemover);
        });
    }

    // ---- fechar o popup: botões "Cancelar" e clique no fundo escuro ----
    popup.querySelectorAll('[data-fechar]').forEach(function (botao) {
        botao.addEventListener('click', function () {
            popup.close();
        });
    });

    popup.addEventListener('click', function (e) {
        if (e.target === popup) {
            popup.close();
        }
    });
    // ---- mensagem de erro do servidor: some sozinha ----
    const erro = document.querySelector('.alerta:not(#aviso)');
    if (erro) {
        // tira "?erro=..." do endereço, para o F5 não trazer a mensagem de volta
        const url = new URL(window.location.href);
        url.searchParams.delete('erro');
        window.history.replaceState({}, '', url.pathname + url.search);

        // some depois de 3 segundos (3000 milissegundos)
        setTimeout(function () {
            erro.hidden = true;
        }, 5000);
    }
})();
