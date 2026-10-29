<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>408</title>
    <link rel="stylesheet" href="../css/error400.css">

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

        img {
            position: relative;
            width: 85%;
            bottom: 5%;
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

            img {
                position: static;
                bottom: auto;
                width: clamp(260px, 75vw, 650px);
                max-width: 90%;
                max-height: 45vh;
                object-fit: contain;
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

            img {
                width: 92%;
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

            img {
                position: static;
                bottom: auto;
                max-height: 38vh;
                width: auto;
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

<img src="../assets/error-assets/400.svg" alt="400">

<p>Bad request</p>

<button>Voltar</button>

<script src="../script/error404.js"></script>
</body>
</html>