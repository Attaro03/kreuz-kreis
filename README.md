# Kreuz & Kreis

Ein lokales Spiel für zwei Personen an einem Bildschirm: Auf einem 4 × 4 großen Brett setzen Kreuz und Kreis abwechselnd ihre Zeichen. Wer drei eigene Zeichen unmittelbar waagerecht, senkrecht oder diagonal verbindet, verliert. Ist das Brett ohne Verlustreihe voll, endet die Runde unentschieden.

## Voraussetzungen

Ein JDK ab Version 17 mit `java`, `javac` und `jar` im PATH sowie eine grafische Desktop-Umgebung. Weitere Bibliotheken sind nicht erforderlich.

## Start unter Windows

Im Projektordner `run.bat` ausführen. Das Skript baut und startet das Spiel. Alternativ `build.bat` ausführen und anschließend `java -jar KreuzKreis.jar` starten. Vor einem erneuten Build das Spiel schließen.

## Start unter Linux und macOS

```sh
mkdir -p out
find src/main/java -name '*.java' > sources.txt
javac --release 17 -encoding UTF-8 -d out @sources.txt
cp -R src/main/resources/. out/
jar --create --file KreuzKreis.jar --main-class de.kreuzkreis.Main -C out .
java -jar KreuzKreis.jar
```

## Bedienung

Ein freies Feld anklicken oder mit Tab zum Spielfeld wechseln, mit den Pfeiltasten navigieren und mit Enter oder Leertaste setzen. „Neue Runde“ setzt das Spiel zurück.

## Tests

Unter Windows `test.bat` ausführen. Das Skript baut das Projekt und führt Modell- und Swing-Tests aus; die Swing-Tests laufen ohne sichtbares Fenster.

## Projektstruktur

- `src/main/java/de/kreuzkreis/`: Startpunkt und Spielmodell
- `src/main/java/de/kreuzkreis/ui/`: Swing-Oberfläche
- `src/main/resources/fonts/`: Schriftdateien
- `src/test/java/de/kreuzkreis/`: Modell- und Oberflächentests
- `build.bat`, `run.bat`, `test.bat`: Windows-Build, Start und Tests
