# java-street-fighter

[![Javadoc](https://img.shields.io/badge/JavaDoc-Online-green)](https://mono1010.github.io/java-street-fighter/)

## EPIC

Street Fighter ist ein 2D 1v1 Kampfspiel bei welchem zwei Gegner gegeneinander antreten.
Damit der Spieler gewinnen kann muss er zwei Runden gewinnen.

Eine Runde wird beendet sobald einer der Spieler kein Leben mehr hat.
Das Leben wird von beiden Spielern angezeigt.

Die Map ist so gross wie das Game Fenster.
Es gibt keine Möglichkeit mit der Map zu interagieren.

Mit dem Keyboard wird der Spieler gesteuert und kann Aktionen ausführen wie schlagen oder springen.
Es werden nur einfache Schläge implementiert werden.

Bei dem Start einer Runde werden die Spieler Links und Rechts platziert und ein Countdown startet welcher auf 0 zählt.
Rechts befindet sich immer der NPC.

## Entwicklung

Informationen zu der Entwicklung können im [documentation](./documentation/README.md) Ordner gefunden werden.

## Installation

Offiziell wird MacOS Sonoma unterstützt, Windows sollte jedoch auch funktionieren.

### Releases

Bereits kompilierte Versionen können unter [Releases](https://github.com/mono1010/java-street-fighter/releases) gefunden werden.
Für jedes Release gibt es zwei Jar Files.
- `street-fighter-release.jar`
   -  Release Version, beinhaltet das ganze Game
- `street-fighter-dev.jar`
    - Development build mit mehr Log Ausgaben und debug Informationen

### Kompilierung

Für die Kompilierung müssen Maven und Java 21 auf dem Gerät installiert sein.

**Release Build**

Im Terminal `mvn clean install -Prelease` ausführen.
Die kompilierte Jar Datei befindet sich dann unter "GAME_ROOT_FOLDER/target/street-fighter-x.x.x.jar".
Mit einem Doppelklick auf die Datei startet sich das Game.

**Development Build**

Mit der Datei [run.bash](./run.bash) wird ein development Build generiert.
- Optional kann als Argument der Filepath angegeben werden. Beispiel: `./run.bash "GAME_ROOT_FOLDER/src/main/resources/assets/fighter/"` Wenn nicht werden die Assets vom Resource Ordner benutzt.

Der development Build beinhaltet trace logs und debug Game Informationen.

## Dokumentation

### Bedienung

- Das Menü kann mit der Escape Taste geöffnet werden
- Alle Keybinds können im Menü unter Settings gefunden und verändert werden

### Code

Die Code Dokumentation kann mit Javadoc generiert werden oder [direkt online angeschaut werden](https://mono1010.github.io/java-street-fighter/).

Die Dokumentation kann mit den folgenden Befehlen generiert werden:
1. Im Terminal in den Game Root Ordner gehen und `mvn javadoc:javadoc` ausführen.
2. Dann mit einem Browser das `index.html` öffnen welches sich in `target/site/apidocs/index.html` befindet.

## Testumgebung

Getestet wurde das Spiel auf MacOS Sonoma.

## Assets

Alle Assets sind kostenlos auf https://itch.io/ verfügbar.

## Credits

- [luizmelo](https://luizmelo.itch.io) für die [Martial Hero 2](https://luizmelo.itch.io/martial-hero-2) und [Fantasy Warrior](https://luizmelo.itch.io/fantasy-warrior) Assets