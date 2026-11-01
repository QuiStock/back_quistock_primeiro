//entrada
const interacao = document.getElementById("interacao");
const opcao1 = document.getElementById("escolha1");
const opcao2 = document.getElementById("escolha2");
const fala = document.getElementById("box2");
const fefo = document.querySelector('.fefo');
const fefoImagem = document.querySelector(".fefox");
const fefoNormal = new Image();
fefoNormal.src = "landing-assets/login-assets/fefo-normal.png";

function despedirFefo() {
    interacao.classList.add("fadeout");
    if (fefoNormal.complete && fefoNormal.naturalWidth) {
        fefoImagem.src = fefoNormal.src;
    }
    fefoImagem.classList.add("fefoSaindo");
}

setTimeout(function(){
    interacao.classList.add("fadein");
});

opcao1.addEventListener("click", function(){

    if(fala.textContent == "Já tem cadastro?"){

        fala.textContent = "Gostaria de fazer um?";
        opcao1.textContent = "Sim";
        opcao2.textContent = "Não, obrigado";

    } else if(fala.textContent == "Gostaria de fazer um?"){

        fala.textContent = "Entre em contato conosco!";
        opcao1.textContent = "Entrar em contato";
        opcao2.textContent = "Voltar";

    } else if(fala.textContent == "Entre em contato conosco!"){

        window.location.href = "mailto:quistockinterdisciplinar@gmail.com";

    }

    interacao.classList.remove("fadein");
    void interacao.offsetWidth;
    interacao.classList.add("fadein");

});

opcao2.addEventListener("click", function(){

    if(fala.textContent == "Já tem cadastro?"){

        despedirFefo();

    } else if(fala.textContent == "Gostaria de fazer um?"){

        despedirFefo();

    } else if(fala.textContent == "Entre em contato conosco!"){

        fala.textContent = "Gostaria de fazer um?";
        opcao1.textContent = "Sim";
        opcao2.textContent = "Não";

        interacao.classList.remove("fadeout");
        void interacao.offsetWidth;
        interacao.classList.add("fadein");

    }

})
