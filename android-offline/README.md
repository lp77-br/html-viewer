# 📱 HTML Viewer - OFFLINE

[![License](https://img.shields.io/badge/LICENSE-OBRIGATÓRIO--LER-orange?style=for-the-badge)](LICENSE)
[![Download APK](https://img.shields.io/badge/DOWNLOAD-APK--TESTE-green?style=for-the-badge)](https://github.com/lp77-br/html-viewer/raw/refs/heads/main/android-offline/HTMLViewer_Offline.apk)

> [!IMPORTANT]
> **TERMOS DE USO:** Ao utilizar, modificar ou distribuir este código, você declara estar ciente dos termos contidos na **[LICENSE](LICENSE)** deste projeto. Nós apoiamos o Open Source, desde que os créditos originais sejam minimamente mantidos.

A versão **OFFLINE** é a nossa ferramenta para desenvolvedores que buscam privacidade total e recursos avançados. Diferente da versão Lite, esta oferece um ambiente de desenvolvimento completo (IDE-like) sem exigir conexão à internet.

## ✨ Diferenciais desta Versão
* **Editor Multi-Abas:** Interface profissional com abas separadas para HTML, CSS e JavaScript.
* **Privacidade:** Sem permissões de internet (`INTERNET_PERMISSION`). O código nunca sai do aparelho.
* **Persistência Local:** Uso de `LocalStorage` e `SharedPreferences` para salvamento automático do progresso.

## 🛠️ Especificações Técnicas
| Componentes | Detalhes |
|:--- |:--- |
| **Linguagem** | Java 1.8 + HTML5/CSS3/JS |
| **Editor Base** | CodeMirror |
| **Bibliotecas** | AppCompat & Design |
| **Motor de Build** | D8 |
| **Compatibilidade** | Android 5.0+ (API 21) |

## 🚀 Como Compilar
Para reconstruir este projeto no **Sketchware Pro** ou **Android Studio**:

1.  **Pasta Assets:** Esta versão depende obrigatoriamente dos arquivos na pasta [assets/](assets/):
    * `index.html` (Interface principal).
    * `codemirror.min.js`, `xml.min.js`, `javascript.min.js`, `css.min.js`, `htmlmixed.min.js`, `beautify-html.min.js`.
    * `codemirror.min.css` e `dracula.min.css`.
2.  **Bibliotecas:** Certifique-se de que as bibliotecas `AppCompat` e `Design` estão ativadas no seu ambiente de desenvolvimento.
3.  **Configurações:** Ative `JavaScriptEnabled` e `DomStorageEnabled` na sua WebView, conforme o `MainActivity.java`.
4.  **Permissões:** O manifesto não deve solicitar acesso à rede para manter a integridade da proposta offline.

## 📥 Download do APK
O APK de demonstração está disponível no link abaixo:
👉 **[Baixar HTMLViewer_Offline.apk](https://github.com/lp77-br/html-viewer/raw/refs/heads/main/android-offline/HTMLViewer_Offline.apk)**

> **Nota:** Versão de testes assinada com `testkey`. O Android exibirá o alerta de segurança padrão. Basta ignorar e prosseguir.
