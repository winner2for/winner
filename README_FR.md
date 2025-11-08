# AccessibilityService (Kotlin) — Intégration avec l'Agent Termux (FR)

Ce paquet contient :
- Un exemple d'**AccessibilityService** Android en Kotlin (source) qui envoie des requêtes HTTP locales au serveur Termux (`http://127.0.0.1:3000`) pour exécuter des commandes.
- Instructions pour compiler l'APK, l'installer, et activer le service Accessibility.
- Code d'exemple pour la sécurité (filtrage des commandes) et journalisation.

## Prérequis
- Android Studio (pour compiler l'APK)
- Un appareil Android (ou émulateur) avec **Termux** installé et l'agent (node server.js) en écoute sur le téléphone (`node server.js`)
- Sur l'appareil, activer l'accessibilité pour l'application (Paramètres > Accessibilité > votre app > activer)

## Structure (fichiers principaux)
- `app/src/main/AndroidManifest.xml`
- `app/src/main/java/com/example/autocontrol/MainActivity.kt`
- `app/src/main/java/com/example/autocontrol/MyAccessibilityService.kt`
- `app/src/main/res/xml/accessibility_service_config.xml`
- `app/build.gradle` (module)
- `settings.gradle`
- `gradle.properties`
- `build.gradle` (root - minimal)
- `keystore_instructions.txt` (signer apk)

## Comportement
1. Le service écoute les événements Accessibility (ex: fenêtre changée, notification).
2. Quand un événement pertinent est détecté, le service peut envoyer une requête POST locale :
   `POST http://127.0.0.1:3000/api/command` avec payload JSON `{ "cmd": "input keyevent 26", "reason": "from-accessibility" }`
3. Le serveur Termux (agent) exécute la commande, retourne la sortie et l'app peut afficher une notification ou un toast de confirmation.

## Sécurité recommandée
- Configure l'agent Termux pour refuser les commandes non dans la whitelist (`config.json`).
- Dans le code Android, n'envoyer que des commandes pré-autoriséess (ex: mapping interne).
- Définir un mot de passe local ou utiliser un jeton partagé (simple secret) dans l'entête HTTP `X-Agent-Secret` pour authentifier l'app.
- Teste en mode debug avant d'activer des actions sensibles.

## Construction rapide
1. Ouvrir Android Studio, sélectionner "Import project" et choisir ce dossier.
2. Construire l'APK (Build > Build Bundle(s) / APK(s) > Build APK(s)).
3. Signer l'APK (voir `keystore_instructions.txt`) ou utiliser "Run" pour installer en debug.
4. Installer l'APK sur le téléphone et activer le service dans Paramètres > Accessibilité.
5. Lancer Termux + `node server.js` (agent).

## Notes
- L'exemple est conçu pour la sécurité : l'app n'exécute pas de commandes sensibles par elle-même; elle envoie une requête au serveur local qui applique la whitelist.
- Je fournis le code source — tu dois compiler l'APK toi-même dans Android Studio (je ne fournis pas d'APK signé ici).
