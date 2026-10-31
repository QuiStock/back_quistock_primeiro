$ErrorActionPreference = 'Stop'
$projectRoot = Split-Path -Parent $PSScriptRoot
$sourcePath = Join-Path $projectRoot 'src/main/webapp/index.html'
$landing = [System.IO.File]::ReadAllText($sourcePath)

# Keep GitHub Pages aligned with the landing shipped in the Java application.
$landing = $landing.Replace('href="css/', 'href="src/main/webapp/css/')
$landing = $landing.Replace('src="assets/', 'src="src/main/webapp/assets/')
$landing = $landing.Replace('src="script/', 'src="src/main/webapp/script/')
$landing = $landing.Replace('src="landing-assets/', 'src="src/main/webapp/landing-assets/')
$landing = $landing.Replace('href="landing-assets/', 'href="src/main/webapp/landing-assets/')
$landing = $landing.Replace('url(landing-assets/', 'url(src/main/webapp/landing-assets/')
$landing = $landing.Replace('login.jsp', '#acesso')
$landing = $landing.Replace('data-loading-navigation ', '')
$accessNotice = @'
    <section id="acesso" aria-labelledby="acesso-titulo" style="padding:3rem 8%;background:#f4effc;color:#281144;font-family:Arial,sans-serif;line-height:1.7">
        <h2 id="acesso-titulo">Acesso ao sistema</h2>
        <p>Esta é a apresentação pública do QuiStock. A área de gestão utiliza uma aplicação Java com banco de dados e precisa de um servidor próprio para funcionar.</p>
        <p>Para executar a aplicação e acessar os cadastros, consulte o <a href="https://github.com/QuiStock/back_quistock_primeiro#como-executar">guia de instalação do QuiStock</a>.</p>
    </section>
'@
$landing = $landing.Replace('</main>', ($accessNotice + "`n    </main>"))
[System.IO.File]::WriteAllText((Join-Path $projectRoot 'index.html'), $landing, [System.Text.UTF8Encoding]::new($false))
Write-Output 'Static landing exported to index.html.'
