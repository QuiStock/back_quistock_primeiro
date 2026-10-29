<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>404</title>

    <style>
        :root {
            --branco: #E7CEEB;
            --violeta: #7F3AEF;
            --limao: #89B751;
            --lavana: #AF36DF;
            --petroleo: #11061F;

            /* cores variaveis */
            --limao-claro: #D7F6B1;
            --verde-escuro: #659529;
            --fundo: #FAF5FB;
            --branco2: #FEFCFF;
            --titulo-roxo: #8720B0;
        }

        * {
            font-family: "Poppins", sans-serif;
            font-style: normal;
            text-decoration: none;
            margin: 0;
            padding: 0;
        }

        body {
            height: 100vh;
            height: 100dvh;
            width: 100%;

            display: flex;
            flex-direction: column;
            justify-content: flex-end;
            align-items: center;
            gap: 2.5rem;

            padding-bottom: 2rem;
            box-sizing: border-box;
        }

        .container {
            display: flex;
            flex-direction: row;
            align-items: center;
            position: absolute;
            gap: 5rem;
            bottom: 15%;
        }

        .container > h1 {
            font-size: clamp(5rem, 40vw, 100rem);
            color: var(--violeta);
        }

        .container > div {
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            gap: 2.5rem;

            aspect-ratio: 1 / 1;
            width: auto;
            height: 60vh;

            border: 15px solid var(--limao);
            border-radius: 50%;
        }

        .container > div .eye {
            display: flex;
            flex-direction: row;
            gap: 5rem;
        }

        .container > div .eye h1 {
            font-size: clamp(1rem, 8vw + 1rem, 70rem);
            color: var(--limao);
        }

        .container > div p {
            font-size: clamp(1rem, 1vw + .5rem, 3rem);
            color: var(--limao);
        }

        body > p {
            font-size: clamp(1rem, 1vw + .5rem, 5rem);
        }

        button {
            width: 15vw;
            height: 8vh;

            border-radius: 50px;
            border: none;

            background-color: var(--violeta);
            color: var(--branco2);

            font-size: clamp(.75rem, 1rem + .5rem, 1.5rem);

            cursor: pointer;
            transition: 300ms;
        }

        button:hover {
            background-color: var(--titulo-roxo);
            transform: scale(1.01);
        }

        button:active {
            transform: scale(0.98);
        }

        /* Responsividade para telas menores que a tela web */
        @media (max-width: 1024px) {
            body {
                height: auto;
                min-height: 100vh;
                min-height: 100dvh;
                justify-content: center;
                gap: 2rem;
                padding: 2rem 1rem;
            }

            .container {
                position: relative;
                bottom: auto;
                gap: clamp(1rem, 4vw, 3rem);
                max-width: 100%;
            }

            .container > h1 {
                font-size: clamp(4rem, 18vw, 9rem);
                line-height: 1;
            }

            .container > div {
                width: clamp(5.5rem, 22vw, 12rem);
                height: clamp(5.5rem, 22vw, 12rem);
                max-height: 40vh;
                border-width: clamp(6px, 1.5vw, 12px);
                gap: clamp(0.35rem, 1.5vw, 1rem);
            }

            .container > div .eye {
                gap: clamp(0.75rem, 3.5vw, 2.5rem);
            }

            .container > div .eye h1 {
                font-size: clamp(1.25rem, 4.5vw, 2.75rem);
                line-height: 1;
            }

            .container > div p {
                font-size: clamp(0.45rem, 1.2vw, 0.85rem);
                text-align: center;
                white-space: nowrap;
            }

            body > p {
                text-align: center;
                font-size: clamp(1rem, 2.5vw, 1.5rem);
                max-width: 90%;
            }

            button {
                width: clamp(140px, 25vw, 180px);
                height: auto;
                min-height: 48px;
                padding: 0.7rem 1.5rem;
                font-size: clamp(0.9rem, 2vw, 1.1rem);
            }
        }

        @media (max-width: 480px) {
            body {
                gap: 1.5rem;
            }

            .container {
                gap: clamp(0.5rem, 2.5vw, 1rem);
            }

            .container > h1 {
                font-size: clamp(3.2rem, 20vw, 4.5rem);
            }

            .container > div {
                width: clamp(4.5rem, 25vw, 6rem);
                height: clamp(4.5rem, 25vw, 6rem);
                border-width: 5px;
                gap: 0.25rem;
            }

            .container > div .eye {
                gap: clamp(0.5rem, 2vw, 1rem);
            }

            .container > div .eye h1 {
                font-size: clamp(1rem, 4vw, 1.4rem);
            }

            .container > div p {
                font-size: clamp(0.35rem, 1.4vw, 0.5rem);
            }

            button {
                width: clamp(140px, 45vw, 180px);
                min-height: 46px;
            }
        }

        @media (max-height: 520px) {
            body {
                justify-content: center;
                gap: 1rem;
                padding: 1rem;
                padding-bottom: 1rem;
            }

            .container {
                position: relative;
                bottom: auto;
                gap: 1rem;
            }

            .container > h1 {
                font-size: clamp(2.5rem, 18vh, 5rem);
            }

            .container > div {
                width: clamp(3.2rem, 22vh, 6.5rem);
                height: clamp(3.2rem, 22vh, 6.5rem);
                border-width: 4px;
                gap: 0.2rem;
            }

            .container > div .eye {
                gap: 0.75rem;
            }

            .container > div .eye h1 {
                font-size: clamp(0.85rem, 6vh, 1.5rem);
            }

            .container > div p {
                font-size: clamp(0.32rem, 2.2vh, 0.55rem);
            }

            body > p {
                font-size: clamp(0.85rem, 4vh, 1.1rem);
            }

            button {
                width: clamp(120px, 20vw, 160px);
                height: auto;
                min-height: 38px;
                padding: 0.4rem 1.2rem;
                font-size: clamp(0.85rem, 3.5vh, 1rem);
            }
        }
    </style>
</head>
<body>

<div class="container">

    <h1>4</h1>
    <div>
        <div class="eye">
            <h1>X</h1>
            <h1>X</h1>
        </div>

        <p>PAGE NOT FOUND</p>
    </div>

    <h1>4</h1>

</div>

<p>Desculpe, não foi possível encontrar esta página.</p>

<button>Voltar</button>

<script src="../script/error404.js"></script>
</body>
</html>