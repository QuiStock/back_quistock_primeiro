// observer genérico (fade in / fade out ao entrar e sair da tela)
const observer = new IntersectionObserver((entries) => {
    entries.forEach((entry) => {
        if (entry.isIntersecting) {
            entry.target.classList.add("show");
        } 
    });
}, {
    threshold: 0.51,
    rootMargin: "30px"
});

// hero
const heroTitle = document.querySelector("#hero h1");
const heroP = document.querySelector("#hero p");
const heroBtn = document.querySelector("#hero button");

observer.observe(heroTitle);
observer.observe(heroP);
observer.observe(heroBtn);

// beneficios
const benefTitle = document.querySelector("#beneficios h1");
const benefBox1 = document.querySelector("#beneficios #box-1");
const benefBox2 = document.querySelector("#beneficios #box-2");
const benefBox3 = document.querySelector("#beneficios #box-3");
const benefBox4 = document.querySelector("#beneficios #box-4");
const benefBox5 = document.querySelector("#beneficios #box-5");

observer.observe(benefTitle);
observer.observe(benefBox1);
observer.observe(benefBox2);
observer.observe(benefBox3);
observer.observe(benefBox4);
observer.observe(benefBox5);

// quem somos
const equipeTitle = document.querySelector("#quem-somos h1");
const greetingCard = document.querySelector("#quem-somos .greeting-card");
const equipeCards = document.querySelectorAll("#quem-somos .card");
const contatoAll = document.querySelector("#contato");

observer.observe(equipeTitle);
observer.observe(greetingCard);
equipeCards.forEach((card) => {
    observer.observe(card);
});
observer.observe(contatoAll);


// ===== PLATAFORMA (cards empilhados com efeito de cone) =====

const platCard1 = document.querySelector(".cards_plataforma:nth-child(1)");
const platCards = document.querySelectorAll(".cards_plataforma");
const topOffsets = [100, 150, 200, 250, 300]; // precisa bater com o "top" de cada #c1..#c5 no CSS
const shrinkStep = 0.03;   // quanto cada card empilhado acima encolhe os de baixo
const minScale = 0.75;     // limite mínimo de encolhimento (card mais no fundo)

// observer dedicado só pro fade-in dos cards, sem remover o "show" depois (evita flicker)
const platObserver = new IntersectionObserver((entries) => {
    entries.forEach((entry) => {
        if (entry.isIntersecting) {
            entry.target.classList.add("show");
            platObserver.unobserve(entry.target);
        }
    });
}, {
    threshold: 0.1
});

platObserver.observe(platCard1)

// posição "de parada" de cada card, calculada uma única vez (antes de qualquer scale ser aplicado)
let stopPoints = [];

function calculateStopPoints() {
    stopPoints = Array.from(platCards).map((card, index) => {
        const rect = card.getBoundingClientRect();
        return rect.top + window.scrollY - topOffsets[index];
    });
}

function updateCardsScale() {
    const lastIndex = platCards.length - 1;

    // progresso de cada card, calculado ao longo de TODA a distância desde que
    // o card anterior grudou até este grudar — em vez de um range fixo em px
    const progress = Array.from(platCards).map((card, index) => {
        if (index === 0) return 0;
        const rangeForThisCard = stopPoints[index] - stopPoints[index - 1];
        const distance = stopPoints[index] - window.scrollY;
        return 1 - Math.min(Math.max(distance / rangeForThisCard, 0), 1);
    });

    // progresso extra de scroll depois que o último card já grudou,
    // usando a mesma distância do último "gap" como referência
    const lastRange = stopPoints[lastIndex] - stopPoints[lastIndex - 1];
    const distanceAfterLast = window.scrollY - stopPoints[lastIndex];
    const progressAfterLast = Math.min(Math.max(distanceAfterLast / lastRange, 0), 1);

    platCards.forEach((card, index) => {
        let totalShrink = 0;

        // soma o progresso de todos os cards que vêm depois (cobrindo este)
        for (let j = index + 1; j < platCards.length; j++) {
            totalShrink += progress[j] * shrinkStep;
        }

        // o último card também encolhe, baseado no scroll extra depois dele
        if (index === lastIndex) {
            totalShrink += progressAfterLast * shrinkStep;
        }

        const scale = Math.max(1 - totalShrink, minScale);
        card.style.transform = `scale(${scale})`;
    });
}

calculateStopPoints();
updateCardsScale();

window.addEventListener("scroll", () => {
    requestAnimationFrame(updateCardsScale);
});

window.addEventListener("resize", () => {
    calculateStopPoints();
    updateCardsScale();
});