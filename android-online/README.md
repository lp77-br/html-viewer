# 📱 HTML Viewer - ONLINE

[![License](https://img.shields.io/badge/LICENSE-OBRIGATÓRIO--LER-orange?style=for-the-badge)](LICENSE)
[![Download APK](https://img.shields.io/badge/DOWNLOAD-APK--TESTE-green?style=for-the-badge)](https://github.com/lp77-br/html-viewer/raw/refs/heads/main/android-online/HTMLViewer_Online.apk)

> [!IMPORTANT]
> **TERMOS DE USO:** Ao utilizar, modificar ou distribuir este código, você declara estar ciente dos termos contidos na **[LICENSE](LICENSE)** deste projeto. Nós apoiamos o Open Source, desde que os créditos originais sejam minimamente mantidos.

A versão **ONLINE** é a edição mais avançada dos nossos editores. Ela introduz um motor de extração de código e uma interface dinâmica que gerencia estados de conexão, garantindo que o ambiente de desenvolvimento esteja sempre sincronizado com as bibliotecas mais recentes.

## ✨ Diferenciais desta Versão
* **Gerenciamento de Estados:** Inclui telas nativas de *Loading* (Sincronização) e *Offline* (Aviso de Conexão) para garantir que as dependências do editor sejam carregadas corretamente.
* **Importador de URL (Extractor):** Sistema que utiliza uma engine oculta para processar links externos e injetar o HTML resultante diretamente no editor.
* **Sincronização Inteligente:** Botão de captura que identifica e "puxa" o código-fonte gerado dinamicamente no preview.
* **Editor Multi-Abas:** Ambiente completo com suporte a HTML, CSS e JavaScript em abas independentes.

## 🛠️ Especificações Técnicas
| Componentes | Detalhes |
|:--- |:--- |
| **Linguagem** | Java 1.8 + HTML5/CSS3/JS |
| **Editor Base** | CodeMirror 5.65 (via CDNJS) |
| **Bibliotecas** | AppCompat & Design |
| **Motor de Build** | D8 |
| **Compatibilidade** | Android 5.0+ (API 21) |

## 🚀 Como Compilar
Para reconstruir este projeto no **Sketchware Pro** ou **Android Studio**:

1.  **Pasta Assets:** Esta versão é ultra-compacta e depende apenas do arquivo na pasta [assets/](assets/):
    * `index.html` (Contém toda a lógica de interface, estilos e scripts de extração).
2.  **Dependências Externas:** O editor carrega o CodeMirror e o JS-Beautify via Cloudflare (CDN). Certifique-se de que o dispositivo possui acesso à internet no primeiro carregamento.
3.  **Configurações:** Mantenha as bibliotecas `AppCompat` e `Design` ativas e configure a WebView para suportar `MixedContentMode` (necessário para o extrator).
4.  **Permissões:** É obrigatório o uso da permissão `INTERNET` no seu manifesto.

## 📥 Download do APK
O APK de demonstração está disponível no link abaixo:
👉 **[Baixar HTMLViewer_Online.apk](https://github.com/lp77-br/html-viewer/raw/refs/heads/main/android-online/HTMLViewer_Online.apk)**

> **Nota:** Versão de testes assinada com `testkey`. O Android exibirá o alerta de segurança padrão durante a instalação.
