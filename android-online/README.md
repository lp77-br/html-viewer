# 📱 HTML Viewer - ONLINE

[![License](https://img.shields.io/badge/LICENSE-OBRIGATÓRIO--LER-orange?style=for-the-badge)](LICENSE)
[![Download APK](https://img.shields.io/badge/DOWNLOAD-APK--TESTE-green?style=for-the-badge)](https://github.com/lp77-br/html-viewer/raw/refs/heads/main/android-online/HTMLViewer_Online.apk)

> [!IMPORTANT]
> **TERMOS DE USO:** Ao utilizar, modificar ou distribuir este código, você declara estar ciente dos termos contidos na **[LICENSE](LICENSE)** deste projeto. Nós apoiamos o Open Source, desde que os créditos originais sejam minimamente mantidos.

A versão **ONLINE** é a ferramenta mais completa da nossa linha de editores. Ela combina o poder do editor multi-abas com um sistema avançado de extração de código, permitindo importar e estudar o HTML de qualquer site da web em tempo real.

## ✨ Diferenciais desta Versão
* **Importador de URL (Extractor):** Motor integrado que processa links externos e extrai o DOM completo (HTML) diretamente para o editor.
* **Sincronização Dinâmica:** Botão de captura inteligente que identifica mudanças no código da página carregada no preview.
* **Editor Multi-Abas:** Ambiente profissional com suporte a arquivos HTML, CSS e JavaScript separados.
* **Navegação Integrada:** WebView configurada para suportar Mixed Content (HTTP/HTTPS) e carregamento de bibliotecas via Web/CDN.

## 🛠️ Especificações Técnicas
| Componentes | Detalhes |
|:--- |:--- |
| **Linguagem** | Java 1.8 + HTML5/CSS3/JS |
| **Editor Base** | CodeMirror (via Web/CDN) |
| **Bibliotecas** | AppCompat & Design |
| **Motor de Build** | D8 |
| **Compatibilidade** | Android 5.0+ (API 21) |

## 🚀 Como Compilar
Para reconstruir este projeto no **Sketchware Pro** ou **Android Studio**:

1.  **Pasta Assets:** Esta versão utiliza carregamento remoto para as bibliotecas do editor, dependendo obrigatoriamente apenas do arquivo na pasta [assets/](assets/):
    * `index.html` (Interface principal e lógica de extração).
2.  **Bibliotecas:** Certifique-se de que as bibliotecas `AppCompat` e `Design` estão ativadas no seu ambiente de desenvolvimento.
3.  **Configurações:** É essencial manter as permissões de `JavaScriptEnabled`, `DomStorageEnabled` e as configurações de `MixedContentMode` ativos.
4.  **Permissões:** A permissão de `INTERNET` deve estar presente no seu `AndroidManifest.xml` para o funcionamento do extrator e carregamento do editor.

## 📥 Download do APK
O APK de demonstração está disponível no link abaixo:
👉 **[Baixar HTMLViewer_Online.apk](https://github.com/lp77-br/html-viewer/raw/refs/heads/main/android-online/HTMLViewer_Online.apk)**

> **Nota:** Versão de testes assinada com `testkey`. O Android exibirá o alerta de segurança padrão. Basta ignorar e prosseguir.
