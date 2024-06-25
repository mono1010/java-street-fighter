# java-street-fighter

[![Javadoc](https://img.shields.io/badge/JavaDoc-Online-green)](https://mono1010.github.io/java-street-fighter/)

## EPIC

Street Fighter ist ein 2D 1v1 Kampfspiel bei welchem zwei Gegner gegeneinander antreten.
Damit der Spieler gewinnen kann muss er zwei Runden gewinnen.

Eine Runde wird beendet sobald einer der Spieler kein Leben mehr hat.
Die Health wird von beiden Spielern angezeigt.

Die Map ist so gross wie das Game Fenster.
Es gibt keine Möglichkeit mit der Map zu interagieren.

Mit dem Keyboard wird der Spieler gesteuert und kann Aktionen ausführen wie schlagen oder springen.
Es werden nur einfache Schläge implementiert werden.

Bei dem Start einer Runde werden die Spieler Links und Rechts platziert und ein Countdown startet welcher auf 0 zählt.
Rechts befindet sich immer der NPC.

## Installation

Offiziell wird MacOS Sonoma unterstützt, Windows sollte jedoch auch funktionieren.

### *NIX

1. `mvn` installieren
2. Die Datei [run.bash](./run.bash) ausführen mit dem Path zu dem assets Ordner für den Fighter.
    Beispiel: `./run.bash "GAME_ROOT_FOLDER/src/main/resources/assets/fighter2/"`

## Dokumentation

### Bedienung

- Das Menü kann mit der Escape Taste geöffnet werden
- Alle Keybinds können im Menü unter Settings gefunden werden

### Code

Die Code Dokumentation kann mit Javadoc generiert werden oder [direkt online angeschaut werden](https://mono1010.github.io/java-street-fighter/).

Die Dokumentation kann mit den folgenden Befehlen generiert werden:
1. Im Terminal in den Game Root Ordner gehen und `mvn javadoc:javadoc` ausführen.
2. Dann mit einem Browser das `index.html` öffnen welches sich in `target/site/apidocs/index.html` befindet.

## Testumgebung

Getestet wurde das Spiel auf MacOS Sonoma.