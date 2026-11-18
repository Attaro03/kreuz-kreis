# Kreuz & Kreis verstehen

Starte beim ersten Spielzug in Abschnitt 2. Lies die dort verlinkten Methoden im Editor mit. Danach kannst du die Spielregeln in Abschnitt 4 mit Papier und Stift nachspielen. Die Zeilenangaben führen jeweils zur Stelle im Repository.

## 1. DAS GROSSE GANZE

**Kreuz & Kreis** ist ein Java-Spiel für zwei Personen an einem Bildschirm. Auf einem Brett mit vier Zeilen und vier Spalten setzt zuerst Kreuz, dann wechseln sich beide ab. Wer mit dem eigenen Zug drei eigene Zeichen direkt hintereinander setzt, verliert. Ist das Brett voll und der letzte Zug erzeugt keine Verlustreihe, endet die Runde unentschieden. [`Game.java`, Z. 7–16](../src/main/java/de/kreuzkreis/Game.java#L7-L16), [Z. 102–125](../src/main/java/de/kreuzkreis/Game.java#L102-L125)

### Die Klassen der Anwendung

Eine **Klasse** beschreibt, welche Daten und Methoden zu einem Teil des Programms gehören. Ein **Objekt** ist eine konkrete Ausführung dieser Beschreibung im Speicher. Ein `Game`-Objekt enthält etwa den Stand einer bestimmten Runde. **Swing** ist die Java-Bibliothek für Fenster, Schaltflächen und andere Teile der grafischen Oberfläche. [`Game.java`, Z. 65–71](../src/main/java/de/kreuzkreis/Game.java#L65-L71), [`Main.java`, Z. 23–39](../src/main/java/de/kreuzkreis/Main.java#L23-L39)

| Klasse und Datei | Aufgabe |
| --- | --- |
| [`Main.java`, Z. 23–50](../src/main/java/de/kreuzkreis/Main.java#L23-L50) | Startet die Anwendung, lädt die Schriften und öffnet das Fenster mit einem `AppPanel`. |
| [`Game.java`, Z. 14–166](../src/main/java/de/kreuzkreis/Game.java#L14-L166) | Speichert Brett, Zugzahl, aktuellen Spieler und Rundenergebnis. Prüft Züge und setzt die Runde zurück. Die inneren Typen `Player`, `State`, `Cell` und `Triple` beschreiben Spieler, Rundenzustände, Feldkoordinaten und Verlustreihen. |
| [`AppPanel.java`, Z. 16–49](../src/main/java/de/kreuzkreis/ui/AppPanel.java#L16-L49) | Baut die Gesamtansicht auf. Gibt dasselbe `Game`-Objekt an Brett und Seitenleiste weiter und verbindet beide mit den Aktionen für Zug und Neustart. |
| [`BoardPanel.java`, Z. 60–131](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L60-L131), [Z. 201–225](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L201-L225), [Z. 272–290](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L272-L290) | Nimmt Maus- und Tastatureingaben für das Brett entgegen, übergibt Feldkoordinaten an `Game` und zeichnet den Spielstand. |
| [`SidePanel.java`, Z. 29–57](../src/main/java/de/kreuzkreis/ui/SidePanel.java#L29-L57), [Z. 118–158](../src/main/java/de/kreuzkreis/ui/SidePanel.java#L118-L158) | Enthält den Neustartknopf und zeichnet Titel, Regeln und Status. Liest aus `Game`, wer am Zug ist oder wie die Runde ausgegangen ist. |
| [`PaperButton.java`, Z. 23–92](../src/main/java/de/kreuzkreis/ui/PaperButton.java#L23-L92) | Zeichnet einen Swing-Knopf im Kartonstil, mit eigener Darstellung beim Drücken, beim Überfahren mit der Maus und bei Tastaturfokus. |
| [`PaperShapes.java`, Z. 21–80](../src/main/java/de/kreuzkreis/ui/PaperShapes.java#L21-L80) | Erstellt abgerundete Formen und Papierkanten und zeichnet Schatten und Papierflächen. |
| [`PaperTexture.java`, Z. 19–77](../src/main/java/de/kreuzkreis/ui/PaperTexture.java#L19-L77) | Erzeugt bei Bedarf eine kleine Bildkachel für das Papierkorn und verwendet sie beim Füllen von Flächen wieder. |
| [`Palette.java`, Z. 15–52](../src/main/java/de/kreuzkreis/ui/Palette.java#L15-L52) | Hält Farben an einer zentralen Stelle und liefert die Farbe zu einem Spieler. |
| [`Fonts.java`, Z. 24–53](../src/main/java/de/kreuzkreis/ui/Fonts.java#L24-L53) | Lädt die mitgelieferten Chillax-Schriften aus `src/main/resources/fonts/`. Bei einem Ladefehler fordert Java `Segoe UI` als Ersatzschrift an. |
| [`AppIcon.java`, Z. 18–38](../src/main/java/de/kreuzkreis/ui/AppIcon.java#L18-L38) | Zeichnet das kleine Kreuz-und-Kreis-Bild für den Fensterrahmen. |

### So hängen sie zusammen

```text
Main öffnet das JFrame mit AppPanel
              AppPanel legt ein Game an und verbindet die Ansichten
                 ├── SidePanel (links) liest Game und enthält PaperButton
                 └── BoardPanel (Mitte) liest Game und ruft Game.play auf
```

Das **Spielmodell** `Game` hält den aktuellen Zustand und entscheidet nach den Spielregeln. `BoardPanel` übersetzt eine Eingabe in Zeile und Spalte. Die beiden Ansichten lesen anschließend dasselbe Modell und zeichnen ihre jeweilige Sicht darauf. `Game` kommt mit den Sprachmitteln aus Abschnitt 3 und Hilfsmitteln aus `java.util` aus. Deshalb kann `GameTest` Züge ohne ein Fenster prüfen. So kannst du bei einem Fehler zuerst klären, ob bereits der gespeicherte Spielstand falsch ist oder ob die Anzeige ihn falsch darstellt. [Importe in `Game.java`, Z. 1–5](../src/main/java/de/kreuzkreis/Game.java#L1-L5), [Verdrahtung in `AppPanel.java`, Z. 22–42](../src/main/java/de/kreuzkreis/ui/AppPanel.java#L22-L42), [direkter Modelltest in `GameTest.java`, Z. 57–69](../src/test/java/de/kreuzkreis/GameTest.java#L57-L69)

### Tests, Start und Vorschauen

| Datei | Wofür du sie gebrauchen kannst |
| --- | --- |
| [`GameTest.java`, Z. 18–38](../src/test/java/de/kreuzkreis/GameTest.java#L18-L38) | Ruft das Modell direkt auf und prüft unter anderem den Spielerwechsel, ungültige Züge, alle vier Richtungen, Spielende und Neustart. |
| [`UiTest.java`, Z. 37–58](../src/test/java/de/kreuzkreis/UiTest.java#L37-L58), [Z. 217–224](../src/test/java/de/kreuzkreis/UiTest.java#L217-L224) | Prüft die Verbindung zwischen Swing und Modell ohne sichtbares Fenster. Beispielsweise erzeugt `click` ein Mausereignis und ruft den registrierten Empfänger auf. |
| [`Preview.java`, Z. 28–43](../src/test/java/de/kreuzkreis/Preview.java#L28-L43), [Z. 104–122](../src/test/java/de/kreuzkreis/Preview.java#L104-L122) | Zeichnet die echte Ansicht in PNG-Bilder für Start, laufendes Spiel, Niederlage, Unentschieden und eine kompakte Fenstergröße. |
| [`build.bat`, Z. 5–20](../build.bat#L5-L20), [Z. 24–44](../build.bat#L24-L44), [`run.bat`, Z. 5–13](../run.bat#L5-L13) | Der Build übersetzt den Code für Java 17, kopiert die Schriften und packt alles in `KreuzKreis.jar`, ein startbares Java-Archiv. `run.bat` baut und startet sie. |
| [`test.bat`, Z. 11–20](../test.bat#L11-L20), [`preview.bat`, Z. 11–14](../preview.bat#L11-L14) | Starten die beiden Testprogramme beziehungsweise die Vorschauerzeugung. |
| [`README.md`, Z. 10–55](../README.md#L10-L55), [`DESIGN.md`, Z. 34–58](../DESIGN.md#L34-L58) | Dokumentieren Start, Bedienung und Regeln beziehungsweise das Aussehen. |

## 2. ABLAUF EINES SPIELZUGS

Drücke in einer neuen Runde die linke Maustaste im Feld oben links. Der Zug beginnt bereits beim **Drücken** der Taste. Der UI-Test `clickPlacesSymbol` stellt diesen Einstieg nach. [Mauseinstieg in `BoardPanel.java`, Z. 75–110](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L75-L110), [`UiTest.java`, Z. 61–73](../src/test/java/de/kreuzkreis/UiTest.java#L61-L73), [Z. 217–224](../src/test/java/de/kreuzkreis/UiTest.java#L217-L224)

| Schritt | Was passiert und warum? |
| --- | --- |
| **1. Swing meldet den Tastendruck.** | Die erste eigene Methode auf diesem Weg ist `mousePressed(MouseEvent event)` im `MouseAdapter` von `BoardPanel`. Der Adapter ist ein Empfänger für Mausereignisse. `mousePressed` prüft zuerst, ob es die linke Maustaste war, und reicht die Klickposition an `cellAt` weiter. [`BoardPanel.java`, Z. 75–110](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L75-L110) |
| **2. `cellAt` bestimmt das Feld.** | `geometry()` berechnet Lage und Größe des Rasters anhand der aktuellen Größe der Brettansicht. `cellAt` verwirft Punkte außerhalb des Rasters. Für `x` und `y` zieht die Methode den Ursprung des Rasters ab, teilt durch die Zellgröße und wandelt die Ergebnisse in ganze Zahlen um. Nach der Indexprüfung entsteht hier `Cell(0, 0)`. Dieselbe Geometrie dient beim Zeichnen, damit ein sichtbares Feld auch sein eigenes Klickziel ist. [`BoardPanel.java`, Z. 258–279](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L258-L279), [Z. 486–510](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L486-L510) |
| **3. Die Ansicht lässt den Zugversuch zu.** | Bei einer gültigen Position und `RUNNING` fordert `mousePressed` den Tastaturfokus für das Brett an, speichert das angeklickte Feld als Fokusposition und blendet den Ring aus. Fokus bedeutet hier, dass das Brett Tastendrücke empfangen kann. Bei einem belegten Feld fordert die Methode nur das Neuzeichnen an und kehrt zurück. Für das freie Feld ruft sie `attemptPlay(cell)` auf. Diese gemeinsame Methode für Maus und Tastatur prüft die Belegung und ruft `game.play(0, 0)` auf. [`BoardPanel.java`, Z. 92–106](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L92-L106), [Z. 201–214](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L201-L214) |
| **4. `Game.play` prüft und schreibt.** | `state.isOver()` erkennt ein bereits beendetes Spiel. `isFree` prüft mit `isInside` erst die Koordinaten und dann, ob das Feld `null`, also leer, ist. Nur beim erlaubten Zug liest `play` den aktuellen Spieler in `mover`, schreibt `KREUZ` nach `board[0][0]` und erhöht `moveCount` von `0` auf `1`. Die Prüfung im Modell schützt auch direkte Aufrufe wie in `GameTest`. [`Game.java`, Z. 43–45](../src/main/java/de/kreuzkreis/Game.java#L43-L45), [Z. 77–79](../src/main/java/de/kreuzkreis/Game.java#L77-L79), [Z. 102–109](../src/main/java/de/kreuzkreis/Game.java#L102-L109), [Z. 165–167](../src/main/java/de/kreuzkreis/Game.java#L165-L167) |
| **5. `Game.play` entscheidet über den Ausgang.** | `findTriplesThrough(0, 0, mover)` sucht neue Dreierfolgen durch das gesetzte Feld. Beim ersten Zug bleibt die Liste leer und der Zähler liegt unter `16`. Deshalb setzt `mover.other()` den nächsten Spieler auf `KREIS`. Erzeugt ein späterer Zug mindestens eine Dreierfolge, speichert `play` die Koordinaten in `losingTriples` und setzt `KREUZ_LOST` oder `KREIS_LOST`. Nur bei leerer Ergebnisliste und vollem Brett setzt es `DRAW`. Beide Endfälle verlassen die Methode vor dem Spielerwechsel. `true` bedeutet in allen drei Fällen, dass das Zeichen gesetzt wurde. [`Game.java`, Z. 32–34](../src/main/java/de/kreuzkreis/Game.java#L32-L34), [Z. 111–125](../src/main/java/de/kreuzkreis/Game.java#L111-L125), [Z. 138–162](../src/main/java/de/kreuzkreis/Game.java#L138-L162) |
| **6. `attemptPlay` meldet den Erfolg.** | Nach `true` blendet `attemptPlay` bei Spielende den Fokusring aus, entfernt die Vorschau unter der Maus und aktualisiert den Mauszeiger. `onMove.run()` ruft `AppPanel.afterMove()` auf, weil `AppPanel` diese Aktion beim Aufbau übergeben hat. `afterMove` ruft `sidePanel.refresh()` auf und fordert ein Neuzeichnen an. `refresh` setzt den passenden Knopftext und fordert ebenfalls Neuzeichnen an. Am Ende ruft auch das Brett `repaint()` auf. [`BoardPanel.java`, Z. 214–224](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L214-L224), [`AppPanel.java`, Z. 34–43](../src/main/java/de/kreuzkreis/ui/AppPanel.java#L34-L43), [`SidePanel.java`, Z. 53–57](../src/main/java/de/kreuzkreis/ui/SidePanel.java#L53-L57) |
| **7. Swing zeichnet den neuen Zustand.** | `repaint()` fordert das Neuzeichnen an. Swing ruft dafür `paintComponent` auf. `BoardPanel.paintSymbols` liest mit `game.playerAt` die Felder und zeichnet oben links ein Kreuz. `SidePanel.paintComponent` liest `RUNNING` und `KREIS` und zeigt „Kreis ist am Zug.“. Bei einer Niederlage zeichnet das Brett die gespeicherten Verlustreihen und „Drei!“. Bei `DRAW` erscheint „Unentschieden“ auf dem Brett. [`BoardPanel.java`, Z. 272–290](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L272-L290), [Z. 353–390](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L353-L390), [Z. 423–429](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L423-L429), [`SidePanel.java`, Z. 118–158](../src/main/java/de/kreuzkreis/ui/SidePanel.java#L118-L158) |

Spiele für einen Verlustzug die Folge aus `horizontalTripleLoses` nach. Kreuz setzt zuerst auf `(0,0)`, Kreis auf `(1,1)`, Kreuz auf `(0,1)`, Kreis auf `(1,2)` und Kreuz auf `(0,2)`. Beim letzten Zug steht das dritte Kreuz auf dem Brett, der Status lautet „Kreuz verliert.“ und der Knopf „Nochmal spielen“. Ein weiterer Klick kann keinen Zug auslösen. So kannst du im Editor verfolgen, wo die Anzeige vom normalen Zug zum Ergebnis wechselt. [`GameTest.java`, Z. 92–107](../src/test/java/de/kreuzkreis/GameTest.java#L92-L107), [`BoardPanel.java`, Z. 92–106](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L92-L106), [`SidePanel.java`, Z. 53–57](../src/main/java/de/kreuzkreis/ui/SidePanel.java#L53-L57), [Z. 133–148](../src/main/java/de/kreuzkreis/ui/SidePanel.java#L133-L148)

## 3. DIE WICHTIGSTEN JAVA-KONZEPTE

### Objekt, Referenz und Konstruktor

```java
public AppPanel() {
    this(new Game());
}
```

`AppPanel()` ist ein **Konstruktor**, also der Code, der ein neues Objekt vorbereitet. `new Game()` erzeugt den Spielstand. `this(...)` ruft einen anderen Konstruktor derselben Klasse auf. `AppPanel(Game game)` gibt die **Referenz**, also den Verweis auf dieses Objekt, an beide Ansichten weiter. Das ist nötig, damit Brett und Seitenleiste denselben Zug sehen. Tests und Vorschauen können über den zweiten Konstruktor ein vorbereitetes Spiel übergeben. [`AppPanel.java`, Z. 22–38](../src/main/java/de/kreuzkreis/ui/AppPanel.java#L22-L38), [`Preview.java`, Z. 104–107](../src/test/java/de/kreuzkreis/Preview.java#L104-L107)

### Zweidimensionales Array und gekapselter Zustand

```java
private final Player[][] board = new Player[SIZE][SIZE];
private Player currentPlayer = Player.KREUZ;
```

`Player[][]` ist ein Array aus Zeilen, die wiederum Arrays aus Feldern sind. `private` hält den direkten Zugriff innerhalb der Klasse. `final` bedeutet bei `board`, dass die Variable auf dasselbe Array verweist. Seine Felder können weiterhin gesetzt oder geleert werden. So fragt die Ansicht über `playerAt` ab, während `Game` die Änderungen über `play` und `reset` kontrolliert. Diese Trennung heißt **Kapselung**. [`Game.java`, Z. 65–79](../src/main/java/de/kreuzkreis/Game.java#L65-L79), [Z. 102–135](../src/main/java/de/kreuzkreis/Game.java#L102-L135)

### `enum` und bedingter Ausdruck

```java
public Player other() {
    return this == KREUZ ? KREIS : KREUZ;
}
```

Ein **`enum`** ist ein Typ mit einer festen Auswahl an Werten, hier `KREUZ` und `KREIS`. `this` bezeichnet den Wert, auf dem die Methode aufgerufen wurde. Der Ausdruck `Bedingung ? WertWennWahr : WertWennFalsch` liefert hier jeweils den anderen Spieler. So bleibt die Zuordnung an einer Stelle, und `play` kann den Wechsel mit `mover.other()` ausdrücken. [`Game.java`, Z. 18–35](../src/main/java/de/kreuzkreis/Game.java#L18-L35), [Z. 124–125](../src/main/java/de/kreuzkreis/Game.java#L124-L125)

### `boolean`, Methodenparameter und Kurzschlussprüfung

```java
public boolean isFree(int row, int col) {
    return isInside(row, col) && board[row][col] == null;
}
```

Die **Parameter** `row` und `col` sind die Werte, die der Aufrufer übergibt. Der **Rückgabewert** ist ein `boolean`, also `true` oder `false`. `&&` wertet die rechte Seite nur aus, wenn links `true` herauskommt. Dadurch liefert etwa `isFree(-1, 0)` sicher `false`, weil die Methode bei einem ungültigen Index gar nicht auf das Array zugreift. [`Game.java`, Z. 77–79](../src/main/java/de/kreuzkreis/Game.java#L77-L79), [Z. 165–167](../src/main/java/de/kreuzkreis/Game.java#L165-L167)

### `record` und typisierte Liste

```java
public record Cell(int row, int col) {
}

public record Triple(Cell first, Cell second, Cell third) {
    public List<Cell> cells() {
        return List.of(first, second, third);
    }
}
```

Ein **`record`** bündelt zusammengehörige Werte. Java erzeugt für `Cell` den Konstruktor `Cell(int row, int col)` und die Lesemethoden `row()` und `col()`. Ein `Triple` bündelt drei solcher Koordinaten. `List<Cell>` bedeutet Liste mit Elementen vom Typ `Cell`. `cells()` liefert die drei Koordinaten als Liste, damit der Test die ganze Folge in ihrer Reihenfolge vergleichen kann. Beim Zeichnen verwendet das Brett die automatisch erzeugten `first()` und `third()` für die Endpunkte der Markierung. [`Game.java`, Z. 56–63](../src/main/java/de/kreuzkreis/Game.java#L56-L63), [`GameTest.java`, Z. 265–269](../src/test/java/de/kreuzkreis/GameTest.java#L265-L269), [`BoardPanel.java`, Z. 353–368](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L353-L368)

### Schleifen und `continue`

```java
for (int row = 0; row < Game.SIZE; row++) {
    for (int col = 0; col < Game.SIZE; col++) {
        Player player = game.playerAt(row, col);
        if (player == null) {
            continue;
        }
```

Die äußere Schleife geht Zeile für Zeile durch das Brett. Die innere beginnt in jeder Zeile wieder bei Spalte `0`. `continue` springt zum nächsten Durchlauf der inneren Schleife. Das ist hier nötig, weil nur belegte Felder ein Symbol bekommen. Das Beispiel zeigt den Anfang von `paintSymbols`. [`BoardPanel.java`, Z. 376–390](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L376-L390)

### `switch` als Ausdruck

```java
return switch (this) {
    case KREUZ_LOST -> Player.KREUZ;
    case KREIS_LOST -> Player.KREIS;
    default -> null;
};
```

In `State.loser()` wählt `switch` anhand des aktuellen Zustands einen Wert für `return`. `default` deckt hier `RUNNING` und `DRAW` ab. `null` bedeutet in diesem Zusammenhang, dass es keinen Verlierer gibt. So kann die Seitenleiste aus einem Verlustzustand den passenden Namen und das Symbol bestimmen. [`Game.java`, Z. 37–53](../src/main/java/de/kreuzkreis/Game.java#L37-L53), [`SidePanel.java`, Z. 118–129](../src/main/java/de/kreuzkreis/ui/SidePanel.java#L118-L129)

### Methodenreferenz, Lambda und `Runnable`

```java
boardPanel = new BoardPanel(game, this::afterMove);
sidePanel = new SidePanel(game, this::resetGame);
```

`this::afterMove` ist eine **Methodenreferenz**. Sie gibt eine Methode zum späteren Aufrufen weiter. `Runnable` ist hier der Typ für eine solche Aktion ohne Parameter und Rückgabewert, die über `run()` startet. Ein **Lambda** ist eine kurze Schreibweise für eine übergebene Aktion. In `bind("LEFT", () -> moveFocus(0, -1));` bedeutet `()` ohne Parameter und der Pfeil leitet die Aktion ein. Damit kann dieselbe `bind`-Methode verschiedene Tasten an die passenden Aktionen binden, während die Brettansicht nach einem erfolgreichen Zug die Gesamtansicht benachrichtigen kann. [`AppPanel.java`, Z. 34–35](../src/main/java/de/kreuzkreis/ui/AppPanel.java#L34-L35), [`BoardPanel.java`, Z. 48–49](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L48-L49), [Z. 162–187](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L162-L187), [Z. 218–223](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L218-L223)

### Vererbung und Überschreiben

```java
@Override
protected void paintComponent(Graphics graphics) {
    super.paintComponent(graphics);
```

`AppPanel` übernimmt mit `extends JPanel` das Verhalten einer Swing-Fläche. **Überschreiben** bedeutet, eine geerbte Methode mit eigener Ausführung zu versehen. `@Override` lässt Java prüfen, ob die Methode tatsächlich eine geerbte Methode überschreibt. `super.paintComponent(graphics)` führt zuerst die Swing-Grundarbeit aus. Anschließend zeichnet `AppPanel` den eigenen Papierhintergrund. So greift Swing beim Neuzeichnen auf den Projektcode zurück. [`AppPanel.java`, Z. 16–20](../src/main/java/de/kreuzkreis/ui/AppPanel.java#L16-L20), [Z. 52–58](../src/main/java/de/kreuzkreis/ui/AppPanel.java#L52-L58)

## 4. SPIELLOGIK

### Was das Modell speichert

`board[row][col]` liest erst die Zeile und darin die Spalte. Beide **Indizes**, also Positionen im Array, reichen von `0` bis `3`. `(0, 0)` ist oben links und `(3, 3)` unten rechts. Beim Anlegen des Arrays sind alle Felder `null`. `currentPlayer` startet mit `KREUZ`, `state` mit `RUNNING`, `losingTriples` mit einer leeren Liste und `moveCount` mit `0`. Der Zähler entspricht der Anzahl belegter Felder, da jeder erlaubte Zug genau ein freies Feld belegt. [`Game.java`, Z. 65–79](../src/main/java/de/kreuzkreis/Game.java#L65-L79), [Z. 102–109](../src/main/java/de/kreuzkreis/Game.java#L102-L109), [Z. 128–135](../src/main/java/de/kreuzkreis/Game.java#L128-L135)

### Wann ein Zug gilt und wer danach dran ist

| Situation | Ergebnis in `Game.play` |
| --- | --- |
| Auf einem leeren Brett setzt Kreuz auf `(0,0)`. | Das Feld enthält `KREUZ`, der Zähler steht auf `1`, `state` bleibt `RUNNING` und `currentPlayer` wird `KREIS`. [`GameTest.java`, Z. 57–69](../src/test/java/de/kreuzkreis/GameTest.java#L57-L69) |
| Kreis versucht direkt danach `(0,0)`. | `false`. Das erste Kreuz bleibt stehen, `moveCount` bleibt `1` und Kreis bleibt am Zug. [`GameTest.java`, Z. 71–79](../src/test/java/de/kreuzkreis/GameTest.java#L71-L79) |
| Ein Aufruf übergibt `(-1,0)` oder `(4,0)`. | `false`, weil die Position außerhalb des Arrays liegt. [`GameTest.java`, Z. 81–90](../src/test/java/de/kreuzkreis/GameTest.java#L81-L90) |
| Eine beendete Runde bekommt noch einen Zugversuch. | `false`, auch bei einem freien Feld. `state.isOver()` reicht zur Ablehnung. [`Game.java`, Z. 102–105](../src/main/java/de/kreuzkreis/Game.java#L102-L105), [`GameTest.java`, Z. 100–106](../src/test/java/de/kreuzkreis/GameTest.java#L100-L106) |

`isFree` beantwortet nur die Frage nach Position und Belegung. Erst `play` verbindet diese Prüfung mit dem Rundenzustand. Bei einer Niederlage oder einem Unentschieden behält `currentPlayer` den Wert des letzten Setzenden, weil `play` vorher zurückkehrt. Für die Ergebnisanzeige zählt `state`. [`Game.java`, Z. 77–83](../src/main/java/de/kreuzkreis/Game.java#L77-L83), [Z. 102–125](../src/main/java/de/kreuzkreis/Game.java#L102-L125), [`SidePanel.java`, Z. 118–129](../src/main/java/de/kreuzkreis/ui/SidePanel.java#L118-L129)

### Wie die Dreiersuche funktioniert

Nach einem Zug kann eine neue Verlustreihe nur dort entstehen, wo sich das Brett geändert hat. Deshalb sucht `findTriplesThrough` nur Dreierabschnitte, die das gerade gesetzte Feld enthalten. Die Richtungen stehen als Paare `(dr, dc)` für eine Änderung der Zeile und der Spalte pro Schritt im Code. [`Game.java`, Z. 65](../src/main/java/de/kreuzkreis/Game.java#L65), [Z. 107–116](../src/main/java/de/kreuzkreis/Game.java#L107-L116), [Z. 138–158](../src/main/java/de/kreuzkreis/Game.java#L138-L158)

| `(dr, dc)` | Bewegung pro Schritt | Beispiel für drei Felder |
| --- | --- | --- |
| `(0, 1)` | Nach rechts | `(0,0) → (0,1) → (0,2)` |
| `(1, 0)` | Nach unten | `(0,0) → (1,0) → (2,0)` |
| `(1, 1)` | Schräg nach rechts unten | `(0,1) → (1,2) → (2,3)` |
| `(1, -1)` | Schräg nach links unten | `(0,3) → (1,2) → (2,1)` |

Der **Offset** ist die Verschiebung des Startfelds entlang der Richtung, gerechnet vom neuen Stein aus. Mit `-2`, `-1` und `0` kann das neue Zeichen an dritter, zweiter und erster Stelle des Dreierabschnitts liegen. Von jedem Startpunkt aus liegen Mitte und Ende einen beziehungsweise zwei Schritte weiter. Liegen Anfang und Ende im Brett, liegt auch die Mitte darin. Dann vergleicht `findTriplesThrough` alle drei Felder mit dem gerade gesetzten Spieler. Bei einem Treffer hängt es ein `Triple` an die Liste und prüft weiter. So bleiben auch mehrere gleichzeitig entstandene Verlustreihen erhalten. [`Game.java`, Z. 138–162](../src/main/java/de/kreuzkreis/Game.java#L138-L162)

Bei der waagerechten Suche für den neuen Stein `(0,2)` sieht das so aus. [Berechnung in `Game.java`, Z. 141–154](../src/main/java/de/kreuzkreis/Game.java#L141-L154)

| Offset | Drei untersuchte Positionen | Auswirkung |
| --- | --- | --- |
| `-2` | `(0,0)`, `(0,1)`, `(0,2)` | Alle Feldpositionen sind gültig. |
| `-1` | `(0,1)`, `(0,2)`, `(0,3)` | Alle Feldpositionen sind gültig. |
| `0` | `(0,2)`, `(0,3)`, `(0,4)` | Der Endpunkt liegt außerhalb. `continue` überspringt diesen Abschnitt vor dem Arrayzugriff. |

### Was als Niederlage oder Unentschieden zählt

In den Beispielen steht `X` für Kreuz, `O` für Kreis und `.` für ein freies Feld. Diese Schreibweise dient dazu, den Spielstand auf Papier nachzuvollziehen. Im Java-Array stehen dafür `Player.KREUZ`, `Player.KREIS` und `null`. [`Game.java`, Z. 18–20](../src/main/java/de/kreuzkreis/Game.java#L18-L20), [Z. 65–79](../src/main/java/de/kreuzkreis/Game.java#L65-L79)

| Ausschnitt | Ergebnis |
| --- | --- |
| `X X . .` → `X X X .` | Kreuz verliert, weil es auf `(0,2)` die erste Dreierfolge in Zeile `0` schließt. Das dritte Kreuz bleibt auf dem Brett und zählt als Zug. [`GameTest.java`, Z. 92–107](../src/test/java/de/kreuzkreis/GameTest.java#L92-L107) |
| `X X O X` | Die beiden Dreierabschnitte der Zeile enthalten jeweils ein `O`, also entsteht in dieser Zeile keine Verlustfolge. [`GameTest.java`, Z. 209–221](../src/test/java/de/kreuzkreis/GameTest.java#L209-L221) |
| `X X . X` → `X X X X` | Setzt Kreuz auf `(0,2)`, entstehen zugleich die Abschnitte `0–2` und `1–3` der Zeile. Beide werden gespeichert. [`GameTest.java`, Z. 223–241](../src/test/java/de/kreuzkreis/GameTest.java#L223-L241) |

Ein volles Brett aus `fullBoardWithoutTripleIsDraw` sieht so aus. Der letzte Kreis steht auf `(3,3)`. Er erzeugt keine Dreierfolge, und `moveCount == SIZE * SIZE` ergibt `true`. Damit setzt `Game.play` den Zustand `DRAW`. [`GameTest.java`, Z. 151–175](../src/test/java/de/kreuzkreis/GameTest.java#L151-L175), [`Game.java`, Z. 111–125](../src/main/java/de/kreuzkreis/Game.java#L111-L125)

```text
O X O X
X X O O
O O X X
X X O O
```

Auch der 16. Zug kann eine Niederlage auslösen. In `lastMoveCanCompleteTwoTriples` setzt Kreis auf das letzte freie Feld `(3,2)`. Der Zug schließt eine waagerechte und eine senkrechte Dreierfolge. Weil `play` zuerst die Dreierprüfung auswertet und bei einem Treffer sofort zurückkehrt, lautet das Ergebnis `KREIS_LOST`. [`GameTest.java`, Z. 178–207](../src/test/java/de/kreuzkreis/GameTest.java#L178-L207), [`Game.java`, Z. 111–122](../src/main/java/de/kreuzkreis/Game.java#L111-L122)

```text
Vor dem letzten Zug    Nach dem letzten Zug
X O X O                 X O X O
X X O X                 X X O X
O X O X                 O X O X
X O . O                 X O O O
```

## 5. GRAFISCHE OBERFLÄCHE

- **Fenster und Anordnung.** `JFrame` ist das Fenster. `JPanel` ist eine Fläche darin, die weitere Komponenten aufnehmen oder selbst zeichnen kann. `AppPanel` legt mit `BorderLayout` fest, wo die beiden Teilflächen liegen. `WEST` setzt die Seitenleiste links, `CENTER` gibt dem Brett den übrigen Platz. `SwingUtilities.invokeLater` plant den Aufbau auf dem Swing-Ereignisthread, also dem Ausführungsstrang, auf dem Swing seine Oberfläche und Eingaben verarbeitet. [`Main.java`, Z. 23–50](../src/main/java/de/kreuzkreis/Main.java#L23-L50), [`AppPanel.java`, Z. 26–38](../src/main/java/de/kreuzkreis/ui/AppPanel.java#L26-L38)
- **Brett und Zeichnen.** Ein einziges `BoardPanel` zeichnet das Raster und alle 16 Felder. `Graphics2D` ist sein Zeichenwerkzeug für Linien, Formen und Schrift. `paintComponent` legt die Reihenfolge fest. Das Raster kommt vor den Verlustmarkierungen und diese vor den Symbolen, damit die Zeichen über der Markierung sichtbar bleiben. Für das durchscheinende Vorschausymbol unter der Maus liest das Brett den aktuellen Spieler aus `Game`. [`BoardPanel.java`, Z. 272–290](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L272-L290), [Z. 337–405](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L337-L405)
- **Tastatur.** Hat das Brett den Fokus, verschieben die Pfeile `focusRow` und `focusCol`. `clamp` begrenzt beide Werte auf `0` bis `3`. Enter oder Leertaste ruft `activateFocus` auf. Diese Methode reicht das ausgewählte Feld an dieselbe `attemptPlay`-Methode wie die Maus weiter. Dadurch brauchen Tastatur und Maus nur einen gemeinsamen Weg zur Spielregelprüfung. [`BoardPanel.java`, Z. 162–225](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L162-L225)
- **Status und Neustart.** `SidePanel` zeichnet seine Texte selbst. Sein `PaperButton` erbt durch `extends JButton` die Knopffunktion von Swing und zeichnet sein Aussehen selbst. Beim Auslösen des Knopfs meldet der registrierte `ActionListener`, also der Empfänger für Knopfaktionen, das Ereignis an `onReset`. Das ist `AppPanel.resetGame`. Es leert über `Game.reset()` den bestehenden Spielstand und aktualisiert beide Ansichten. Weil beide dieselbe Referenz behalten, können sie sofort die leere Runde lesen. [`SidePanel.java`, Z. 37–57](../src/main/java/de/kreuzkreis/ui/SidePanel.java#L37-L57), [`PaperButton.java`, Z. 23–55](../src/main/java/de/kreuzkreis/ui/PaperButton.java#L23-L55), [`AppPanel.java`, Z. 34–49](../src/main/java/de/kreuzkreis/ui/AppPanel.java#L34-L49), [`Game.java`, Z. 128–135](../src/main/java/de/kreuzkreis/Game.java#L128-L135)

### Wo du bei einem Fehler oder einer kleinen Änderung anfängst

| Beobachtung oder Aufgabe | Einstieg im Code und Grund |
| --- | --- |
| Beim Klick landet das Zeichen im Nachbarfeld. | Verfolge `mousePressed → cellAt → geometry` und prüfe `Point` sowie die berechnete `Cell`. `UiTest.compactHitTesting` prüft bei kompakter Größe die Mitte jedes Feldes an einem frischen Brett. [`BoardPanel.java`, Z. 88–105](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L88-L105), [Z. 258–269](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L258-L269), [`UiTest.java`, Z. 182–193](../src/test/java/de/kreuzkreis/UiTest.java#L182-L193) |
| Die Runde endet zu früh oder zu spät. | Untersuche in `Game.play` nach `findTriplesThrough` die Werte von `triples`, `moveCount` und `state`. Die Modelltests für Richtungen, Lücken und den letzten Zug zeigen, welche Ergebnisse zu erwarten sind. [`Game.java`, Z. 102–125](../src/main/java/de/kreuzkreis/Game.java#L102-L125), [`GameTest.java`, Z. 92–240](../src/test/java/de/kreuzkreis/GameTest.java#L92-L240) |
| Du möchtest den Text des Neustartknopfs ändern. | Lies die erste Beschriftung im `SidePanel`-Konstruktor und den Textwechsel in `SidePanel.refresh`. `UiTest.buttonTextAfterLoss` prüft den Wortlaut für die Niederlage. [`SidePanel.java`, Z. 37–57](../src/main/java/de/kreuzkreis/ui/SidePanel.java#L37-L57), [`UiTest.java`, Z. 133–144](../src/test/java/de/kreuzkreis/UiTest.java#L133-L144) |
| Du möchtest die Farbe von Kreuz ändern. | Schau in `Palette`. `KREUZ` und `FOCUS` verwenden beide `BLUE`. An diesen Zuweisungen siehst du, warum eine Änderung von `BLUE` auch den Fokusring beeinflusst. [`Palette.java`, Z. 28–52](../src/main/java/de/kreuzkreis/ui/Palette.java#L28-L52), [`BoardPanel.java`, Z. 407–420](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L407-L420) |

## 6. METHODENÜBERSICHT

`void` bedeutet, dass eine Methode keinen Wert zurückgibt. Konstruktoren haben keinen Rückgabetyp. „Swing“ in der letzten Spalte bedeutet, dass das Bibliotheksverhalten die Methode auslöst, etwa nach einem Ereignis oder beim Zeichnen. Die Zeilennummer neben dem Methodennamen führt zur Definition.

### Start und Spielmodell

| Klasse und Methode | Parameter | Rückgabewert | Aufgabe | Aufrufende Methode oder Ereignis |
| --- | --- | --- | --- | --- |
| [`Main.main`, Z. 23](../src/main/java/de/kreuzkreis/Main.java#L23) | `String[] args` | `void` | Plant den Fensteraufbau und zeigt das Spiel an. | Java beim Start der JAR-Datei |
| [`Game.Player.Player`, Z. 24](../src/main/java/de/kreuzkreis/Game.java#L24) | `String displayName` | Konstruktor | Speichert den lesbaren Namen. | Anlegen der beiden `enum`-Werte |
| [`Game.Player.displayName`, Z. 28](../src/main/java/de/kreuzkreis/Game.java#L28) | – | `String` | Liefert „Kreuz“ oder „Kreis“. | `SidePanel.paintComponent`, `SidePanel.statusText` |
| [`Game.Player.other`, Z. 32](../src/main/java/de/kreuzkreis/Game.java#L32) | – | `Player` | Liefert den jeweils anderen Spieler. | `Game.play` |
| [`Game.State.isOver`, Z. 43](../src/main/java/de/kreuzkreis/Game.java#L43) | – | `boolean` | Erkennt einen beendeten Spielzustand. | `Game.play`, `SidePanel.refresh` und `paintComponent`, `BoardPanel.paintSticker` |
| [`Game.State.loser`, Z. 47](../src/main/java/de/kreuzkreis/Game.java#L47) | – | `Player` oder `null` | Ordnet einem Verlustzustand den Verlierer zu. | `SidePanel.paintComponent`, `SidePanel.statusText`, `GameTest.lastMoveCanCompleteTwoTriples` |
| [`Game.Triple.cells`, Z. 60](../src/main/java/de/kreuzkreis/Game.java#L60) | – | `List<Cell>` | Gibt die drei Felder in Reihenfolge zurück. | `GameTest.assertTriple` |
| [`Game.playerAt`, Z. 73](../src/main/java/de/kreuzkreis/Game.java#L73) | `int row, int col` | `Player` oder `null` | Liest das Zeichen. Gibt auch für ungültige Koordinaten `null` zurück. | `BoardPanel.paintSymbols`, Tests |
| [`Game.isFree`, Z. 77](../src/main/java/de/kreuzkreis/Game.java#L77) | `int row, int col` | `boolean` | Prüft, ob ein Feld innerhalb des Bretts und leer ist. | `Game.play`, mehrere Eingabe- und Zeichenmethoden in `BoardPanel`, Tests |
| [`Game.getCurrentPlayer`, Z. 81](../src/main/java/de/kreuzkreis/Game.java#L81) | – | `Player` | Liest den aktuellen Spielerwert. | `BoardPanel.paintHoverGhost`, `SidePanel.paintComponent` und `statusText`, Tests |
| [`Game.getState`, Z. 85](../src/main/java/de/kreuzkreis/Game.java#L85) | – | `State` | Liest den Rundenzustand. | `BoardPanel`, `SidePanel`, Tests und `Preview` |
| [`Game.getLosingTriples`, Z. 89](../src/main/java/de/kreuzkreis/Game.java#L89) | – | `List<Triple>` | Liefert gespeicherte Verlustreihen oder eine leere Liste. | `BoardPanel.paintLosingLines`, `GameTest` |
| [`Game.getMoveCount`, Z. 93](../src/main/java/de/kreuzkreis/Game.java#L93) | – | `int` | Liest die Anzahl erlaubter, ausgeführter Züge. | `GameTest`, `UiTest` |
| [`Game.play`, Z. 102](../src/main/java/de/kreuzkreis/Game.java#L102) | `int row, int col` | `boolean` | Prüft und setzt den Zug, bestimmt Ergebnis oder Spielerwechsel. | `BoardPanel.attemptPlay`, `GameTest`, `UiTest.play`, `Preview.play` |
| [`Game.reset`, Z. 128](../src/main/java/de/kreuzkreis/Game.java#L128) | – | `void` | Leert das Brett und stellt den Startzustand her. | `AppPanel.resetGame`, `GameTest.resetClearsFinishedGame` |
| [`Game.findTriplesThrough`, Z. 138](../src/main/java/de/kreuzkreis/Game.java#L138) | `int row, int col, Player player` | `List<Triple>` | Sammelt alle Dreierfolgen des Spielers durch das neue Feld. | `Game.play` |
| [`Game.isInside`, Z. 165](../src/main/java/de/kreuzkreis/Game.java#L165) | `int row, int col` | `boolean` | Prüft die Arraygrenzen. | `Game.playerAt`, `Game.isFree`, `Game.findTriplesThrough` |

`new Game()` nutzt den automatisch verfügbaren Konstruktor ohne Parameter. Bei den `record`-Typen erzeugt Java die Konstruktoren und die Lesemethoden für die einzelnen Werte. [`Game.java`, Z. 14](../src/main/java/de/kreuzkreis/Game.java#L14), [Z. 56–71](../src/main/java/de/kreuzkreis/Game.java#L56-L71), [`BoardPanel.java`, Z. 500–511](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L500-L511)

### Zusammensetzung, Status und Neustart

| Klasse und Methode | Parameter | Rückgabewert | Aufgabe | Aufrufende Methode oder Ereignis |
| --- | --- | --- | --- | --- |
| [`AppPanel.AppPanel`, Z. 22](../src/main/java/de/kreuzkreis/ui/AppPanel.java#L22) | – | Konstruktor | Erzeugt eine neue Runde und ruft den zweiten Konstruktor auf. | `Main.main` beim Fensteraufbau |
| [`AppPanel.AppPanel`, Z. 26](../src/main/java/de/kreuzkreis/ui/AppPanel.java#L26) | `Game game` | Konstruktor | Baut die Ansichten mit derselben Runde auf. | `AppPanel()`, `UiTest.panel`, `Preview.render` |
| [`AppPanel.afterMove`, Z. 40](../src/main/java/de/kreuzkreis/ui/AppPanel.java#L40) | – | `void` | Aktualisiert die Seitenleiste und meldet die Gesamtfläche zum Neuzeichnen an. | `BoardPanel.attemptPlay` über `onMove` |
| [`AppPanel.resetGame`, Z. 45](../src/main/java/de/kreuzkreis/ui/AppPanel.java#L45) | – | `void` | Setzt das Modell zurück und aktualisiert beide Ansichten. | Knopfaktion in `SidePanel` über `onReset` |
| [`AppPanel.paintComponent`, Z. 53](../src/main/java/de/kreuzkreis/ui/AppPanel.java#L53) | `Graphics graphics` | `void` | Zeichnet den Hintergrund. | Swing beim Zeichnen |
| [`AppPanel.getGame`, Z. 60](../src/main/java/de/kreuzkreis/ui/AppPanel.java#L60) | – | `Game` | Gibt das gemeinsame Modell heraus. | `UiTest` |
| [`AppPanel.getBoardPanel`, Z. 64](../src/main/java/de/kreuzkreis/ui/AppPanel.java#L64) | – | `BoardPanel` | Gibt die Brettansicht heraus. | `UiTest` |
| [`AppPanel.getSidePanel`, Z. 68](../src/main/java/de/kreuzkreis/ui/AppPanel.java#L68) | – | `SidePanel` | Gibt die Seitenleiste heraus. | `UiTest` |
| [`SidePanel.SidePanel`, Z. 37](../src/main/java/de/kreuzkreis/ui/SidePanel.java#L37) | `Game game, Runnable onReset` | Konstruktor | Erstellt den Knopf, registriert seine Aktion und setzt seine Startbeschriftung passend zum Modell. | `AppPanel(Game)` |
| [`SidePanel.refresh`, Z. 53](../src/main/java/de/kreuzkreis/ui/SidePanel.java#L53) | – | `void` | Setzt den passenden Knopftext und fordert Neuzeichnen an. | `SidePanel(Game, Runnable)`, `AppPanel.afterMove`, `AppPanel.resetGame` |
| [`SidePanel.getResetButton`, Z. 59](../src/main/java/de/kreuzkreis/ui/SidePanel.java#L59) | – | `PaperButton` | Gibt den Knopf heraus. | `UiTest` |
| [`SidePanel.doLayout`, Z. 64](../src/main/java/de/kreuzkreis/ui/SidePanel.java#L64) | – | `void` | Legt Position und Größe des Knopfs fest. | Swing bei der Anordnung, `UiTest.layout`, `Preview.layout` |
| [`SidePanel.paintComponent`, Z. 74](../src/main/java/de/kreuzkreis/ui/SidePanel.java#L74) | `Graphics graphics` | `void` | Zeichnet Titel, Regeltext, Status und Fußzeile. | Swing beim Zeichnen |
| [`SidePanel.fitStatusFont`, Z. 168](../src/main/java/de/kreuzkreis/ui/SidePanel.java#L168) | `Graphics2D g, int width` | `Font` | Sucht eine Schriftgröße für den Statustext. | `SidePanel.paintComponent` |
| [`SidePanel.statusText`, Z. 184](../src/main/java/de/kreuzkreis/ui/SidePanel.java#L184) | – | `String` | Liefert den Statustext für die Breitenmessung. | `SidePanel.fitStatusFont` |
| [`SidePanel.drawStatusSymbol`, Z. 195](../src/main/java/de/kreuzkreis/ui/SidePanel.java#L195) | `Graphics2D g, Player player, double centerX, double centerY, double radius` | `void` | Zeichnet ein Kreuz- oder Kreissymbol neben dem Status. | `SidePanel.paintComponent` |
| [`SidePanel.drawLines`, Z. 210](../src/main/java/de/kreuzkreis/ui/SidePanel.java#L210) | `Graphics2D g, List<String> lines, int x, int top, int lineHeight, Font font, Color color` | `void` | Zeichnet die zuvor umgebrochenen Textzeilen. | `SidePanel.paintComponent` |
| [`SidePanel.wrap`, Z. 222](../src/main/java/de/kreuzkreis/ui/SidePanel.java#L222) | `String text, FontMetrics metrics, int maxWidth` | `List<String>` | Teilt den Regeltext anhand gemessener Breiten in Zeilen. | `SidePanel.paintComponent` |

### Brett, Eingabe und Zeichnen

| Klasse und Methode | Parameter | Rückgabewert | Aufgabe | Aufrufende Methode oder Ereignis |
| --- | --- | --- | --- | --- |
| [`BoardPanel.BoardPanel`, Z. 60](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L60) | `Game game, Runnable onMove` | Konstruktor | Verbindet das Brett mit dem Modell und registriert die Eingaben. | `AppPanel(Game)` |
| [`BoardPanel`-Empfänger `mousePressed`, Z. 88](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L88) | `MouseEvent event` | `void` | Filtert den Tastendruck, ermittelt das Feld und startet den Zugversuch. | Swing beim Drücken einer Maustaste, `UiTest.click` |
| [`BoardPanel`-Empfänger `mouseMoved`, Z. 77](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L77) | `MouseEvent event` | `void` | Reicht die Mausposition an `handleMouseMove` weiter. | Swing bei Mausbewegung |
| [`BoardPanel`-Empfänger `mouseExited`, Z. 82](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L82) | `MouseEvent event` | `void` | Entfernt die Vorschau und aktualisiert den Mauszeiger. | Swing beim Verlassen der Brettkomponente |
| [`BoardPanel`-Empfänger `focusGained`, Z. 114](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L114) | `FocusEvent event` | `void` | Setzt die Sichtbarkeit des Rings anhand der Ursache des Fokuswechsels. | Swing beim Fokusgewinn, `UiTest.fireFocus` |
| [`BoardPanel`-Empfänger `focusLost`, Z. 122](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L122) | `FocusEvent event` | `void` | Blendet den Ring aus und fordert Neuzeichnen an. | Swing beim Fokusverlust, `UiTest.fireFocus` |
| [`BoardPanel.refresh`, Z. 134](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L134) | – | `void` | Entfernt die Mausvorschau, aktualisiert den Zeiger und meldet das Brett zum Zeichnen an. | `AppPanel.resetGame` |
| [`BoardPanel.isFocusIndicatorVisible`, Z. 144](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L144) | – | `boolean` | Gibt den gespeicherten Wert `showFocus` zurück. | `UiTest.focusRingOnTraversal`, `UiTest.occupiedClickClearsFocusRing` |
| [`BoardPanel.cellCenter`, Z. 152](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L152) | `int row, int col` | `Point` | Liefert die gerundete Feldmitte. Bei ungültigen Koordinaten löst es einen Fehler aus. | `UiTest.click` |
| [`BoardPanel.installKeyBindings`, Z. 162](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L162) | – | `void` | Ordnet Pfeile, Enter und Leertaste den Aktionen zu. | `BoardPanel(Game, Runnable)` |
| [`BoardPanel.isTraversal`, Z. 171](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L171) | `FocusEvent.Cause cause` | `boolean` | Erkennt die im Code aufgeführten Ursachen der Fokusnavigation. | `focusGained` |
| [`BoardPanel.bind`, Z. 179](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L179) | `String keyStroke, Runnable action` | `void` | Hinterlegt eine Taste und die dazugehörige Aktion in Swing. | `BoardPanel.installKeyBindings` |
| [`BoardPanel`-Tastenaktion `actionPerformed`, Z. 184](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L184) | `ActionEvent event` | `void` | Ruft die von `bind` gespeicherte Aktion auf. | Swing bei einer gebundenen Taste, `UiTest` über die Aktionsliste |
| [`BoardPanel.moveFocus`, Z. 190](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L190) | `int deltaRow, int deltaCol` | `void` | Verschiebt das ausgewählte Feld innerhalb der Brettgrenzen. | Pfeiltasten-Aktionen aus `installKeyBindings` |
| [`BoardPanel.clamp`, Z. 197](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L197) | `int value` | `int` | Begrenzt einen Index auf `0` bis `SIZE - 1`. | `BoardPanel.moveFocus` |
| [`BoardPanel.activateFocus`, Z. 201](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L201) | – | `void` | Versucht bei laufender Runde einen Zug auf dem ausgewählten Feld. | Enter- oder Leertasten-Aktion |
| [`BoardPanel.attemptPlay`, Z. 209](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L209) | `Cell cell` | `void` | Ruft für ein freies Feld `Game.play` auf und meldet Erfolg an `onMove`. | `mousePressed`, `BoardPanel.activateFocus` |
| [`BoardPanel.handleMouseMove`, Z. 227](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L227) | `Point point` | `void` | Ermittelt das Feld unter der Maus und steuert die Vorschau. | `mouseMoved` |
| [`BoardPanel.setHover`, Z. 239](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L239) | `int row, int col` | `void` | Speichert das Vorschaufeld. `(-1,-1)` entfernt die Vorschau. | `mouseExited`, `BoardPanel.refresh`, `attemptPlay`, `handleMouseMove` |
| [`BoardPanel.updateCursor`, Z. 248](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L248) | – | `void` | Wählt Hand oder Standardzeiger anhand der Spielbarkeit. | `mouseExited`, `BoardPanel.refresh`, `attemptPlay`, `handleMouseMove` |
| [`BoardPanel.cellAt`, Z. 258](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L258) | `Point point` | `Cell` oder `null` | Übersetzt die Position in Zeile und Spalte oder verwirft sie. | `mousePressed`, `BoardPanel.handleMouseMove` |
| [`BoardPanel.paintComponent`, Z. 272](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L272) | `Graphics graphics` | `void` | Zeichnet das gesamte Brett in festgelegter Reihenfolge. | Swing beim Zeichnen |
| [`BoardPanel.paintMat`, Z. 293](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L293) | `Graphics2D g, Geometry geometry` | `void` | Zeichnet die leicht gedrehte Papierunterlage. | `BoardPanel.paintComponent` |
| [`BoardPanel.paintSheet`, Z. 311](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L311) | `Graphics2D g, Geometry geometry` | `void` | Zeichnet das obere Papierblatt. | `BoardPanel.paintComponent` |
| [`BoardPanel.paintHoverFill`, Z. 323](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L323) | `Graphics2D g, Geometry geometry` | `void` | Hinterlegt das freie Feld unter der Maus farbig. | `BoardPanel.paintComponent` |
| [`BoardPanel.paintGrid`, Z. 337](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L337) | `Graphics2D g, Geometry geometry` | `void` | Zeichnet Rasterlinien und Außenrahmen. | `BoardPanel.paintComponent` |
| [`BoardPanel.paintLosingLines`, Z. 353](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L353) | `Graphics2D g, Geometry geometry` | `void` | Markiert jede vom Modell gespeicherte Verlustreihe. | `BoardPanel.paintComponent` |
| [`BoardPanel.paintSymbols`, Z. 376](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L376) | `Graphics2D g, Geometry geometry` | `void` | Zeichnet die belegten Felder anhand des Modells. | `BoardPanel.paintComponent` |
| [`BoardPanel.paintHoverGhost`, Z. 393](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L393) | `Graphics2D g, Geometry geometry` | `void` | Zeichnet das durchscheinende Vorschausymbol. | `BoardPanel.paintComponent` |
| [`BoardPanel.paintFocus`, Z. 407](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L407) | `Graphics2D g, Geometry geometry` | `void` | Zeichnet den Fokusring, wenn er in einer laufenden Runde sichtbar sein soll. | `BoardPanel.paintComponent` |
| [`BoardPanel.paintSticker`, Z. 423](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L423) | `Graphics2D g, Geometry geometry` | `void` | Zeichnet bei Spielende den Ergebnisaufkleber. | `BoardPanel.paintComponent` |
| [`BoardPanel.drawSymbol`, Z. 456](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L456) | `Graphics2D g, Player player, double centerX, double centerY, double radius, Color color, float opacity` | `void` | Zeichnet Kreuz oder Kreis an der angegebenen Stelle. | `BoardPanel.paintSymbols`, `BoardPanel.paintHoverGhost` |
| [`BoardPanel.centerX`, Z. 478](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L478), [`centerY`, Z. 482](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L482) | `Geometry geometry`, dazu `int col` bzw. `int row` | je `double` | Rechnen Spalte oder Zeile in die passende Bildkoordinate der Feldmitte um. | `BoardPanel.cellCenter`, `paintLosingLines`, `paintSymbols`, `paintHoverGhost` |
| [`BoardPanel.geometry`, Z. 486](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L486) | – | `Geometry` | Berechnet Blatt, Raster und Feldgröße aus der Komponentengröße. | `BoardPanel.cellAt`, `cellCenter`, `paintComponent` |
| [`BoardPanel.Geometry.gridBounds`, Z. 509](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L509) | – | `Rectangle2D` | Liefert die rechteckige Fläche des Rasters. | `BoardPanel.cellAt` |

### Gestaltung und Schriften

| Klasse und Methode | Parameter | Rückgabewert | Aufgabe | Aufrufende Methode oder Ereignis |
| --- | --- | --- | --- | --- |
| [`PaperButton.PaperButton`, Z. 29](../src/main/java/de/kreuzkreis/ui/PaperButton.java#L29) | `String text` | Konstruktor | Richtet den Knopf und seine Schrift ein. Bei Zustandsänderungen fordert er Neuzeichnen an. | `SidePanel` beim Anlegen des Neustartknopfs |
| [`PaperButton.paintComponent`, Z. 48](../src/main/java/de/kreuzkreis/ui/PaperButton.java#L48) | `Graphics graphics` | `void` | Zeichnet Kartonfläche, Text und bei Fokus den Ring. | Swing beim Zeichnen |
| [`Palette.alpha`, Z. 47](../src/main/java/de/kreuzkreis/ui/Palette.java#L47) | `Color color, int alpha` | `Color` | Erzeugt eine Farbe mit geänderter Deckkraft. | Zeichenmethoden, etwa `BoardPanel.paintGrid` |
| [`Palette.playerColor`, Z. 51](../src/main/java/de/kreuzkreis/ui/Palette.java#L51) | `Player player` | `Color` | Ordnet Kreuz und Kreis ihre Farben zu. | `BoardPanel.paintSymbols`, `BoardPanel.paintHoverGhost`, `SidePanel.drawStatusSymbol` |
| [`PaperShapes.rounded`, Z. 21](../src/main/java/de/kreuzkreis/ui/PaperShapes.java#L21) | `double x, double y, double width, double height, double radius` | `Shape` | Erzeugt ein abgerundetes Rechteck. | Zeichenmethoden, etwa `PaperButton.paintComponent` |
| [`PaperShapes.tornSheet`, Z. 29](../src/main/java/de/kreuzkreis/ui/PaperShapes.java#L29) | `double x, double y, double width, double height, long seed` | `Path2D` | Berechnet den Umriss eines Blatts. `seed` ist der Startwert für die Zufallsfolge und hält die Risskante beim Neuzeichnen stabil. | `BoardPanel.paintMat`, `BoardPanel.paintSheet` |
| [`PaperShapes.shadow`, Z. 60](../src/main/java/de/kreuzkreis/ui/PaperShapes.java#L60) | `Graphics2D g, Shape shape, int layers` | `void` | Zeichnet den Schatten in mehreren versetzten Schichten. | `BoardPanel.paintMat`, `BoardPanel.paintSheet` |
| [`PaperShapes.paper`, Z. 72](../src/main/java/de/kreuzkreis/ui/PaperShapes.java#L72) | `Graphics2D g, Shape shape, Color base` | `void` | Füllt eine Form mit Papierkorn und zeichnet den Rand. | `BoardPanel.paintMat`, `BoardPanel.paintSheet` |
| [`PaperShapes.rotate`, Z. 79](../src/main/java/de/kreuzkreis/ui/PaperShapes.java#L79) | `Graphics2D g, double radians, double centerX, double centerY` | `void` | Dreht die Zeichenfläche um einen Mittelpunkt. | `BoardPanel.paintMat` |
| [`PaperTexture.grain`, Z. 25](../src/main/java/de/kreuzkreis/ui/PaperTexture.java#L25) | – | `TexturePaint` | Liefert die wiederholbare Körnung und erzeugt sie beim ersten Bedarf. | `PaperTexture.fill`, `PaperTexture.fillBackground` |
| [`PaperTexture.fill`, Z. 32](../src/main/java/de/kreuzkreis/ui/PaperTexture.java#L32) | `Graphics2D g, Shape shape, Color base` | `void` | Füllt die Form mit Grundfarbe und Körnung. | `PaperShapes.paper`, `AppIcon.create`, `BoardPanel.paintSticker`, `PaperButton.paintComponent` |
| [`PaperTexture.fillBackground`, Z. 40](../src/main/java/de/kreuzkreis/ui/PaperTexture.java#L40) | `Graphics2D g, int width, int height, Color base` | `void` | Füllt den rechteckigen Hintergrund mit Grundfarbe und Körnung. | `AppPanel.paintComponent` |
| [`PaperTexture.createGrain`, Z. 48](../src/main/java/de/kreuzkreis/ui/PaperTexture.java#L48) | – | `TexturePaint` | Zeichnet Punkte und Fasern in eine Bildkachel. | `PaperTexture.grain` |
| [`Fonts.load`, Z. 24](../src/main/java/de/kreuzkreis/ui/Fonts.java#L24) | – | `void` | Lädt beide Schriftvarianten beim ersten Aufruf. | `Main.main`, `Fonts.ui`, `Fonts.display`, `UiTest.main`, `Preview.main` |
| [`Fonts.loadFont`, Z. 33](../src/main/java/de/kreuzkreis/ui/Fonts.java#L33) | `String resource, int fallbackStyle` | `Font` | Lädt eine Schriftdatei oder fordert eine Ersatzschrift an. | `Fonts.load` |
| [`Fonts.ui`, Z. 45](../src/main/java/de/kreuzkreis/ui/Fonts.java#L45), [`Fonts.display`, Z. 50](../src/main/java/de/kreuzkreis/ui/Fonts.java#L50) | je `float size` | je `Font` | Liefert die gewünschte Größe aus Medium für Oberflächentexte beziehungsweise Semibold mit engerem Buchstabenabstand für große Texte. | `SidePanel`, `PaperButton` (`ui`), `BoardPanel.paintSticker` (`display`) |
| [`AppIcon.create`, Z. 18](../src/main/java/de/kreuzkreis/ui/AppIcon.java#L18) | – | `BufferedImage` | Gibt das gezeichnete 64×64-Fenstersymbol zurück. | `Main.main` beim Fensteraufbau |

### Tests und Vorschauen zum Nachlesen

Die Testmethoden arbeiten meist nach demselben Muster. Sie bauen einen Spielstand auf, führen einen Zug oder eine Eingabe aus und prüfen das Ergebnis. `assert…`-Methoden melden über `AssertionError`, wenn ein erwarteter Wert vom tatsächlichen Wert abweicht. [`GameTest.java`, Z. 92–107](../src/test/java/de/kreuzkreis/GameTest.java#L92-L107), [Z. 265–315](../src/test/java/de/kreuzkreis/GameTest.java#L265-L315), [`UiTest.java`, Z. 61–73](../src/test/java/de/kreuzkreis/UiTest.java#L61-L73), [Z. 241–288](../src/test/java/de/kreuzkreis/UiTest.java#L241-L288)

| Klasse und Methode | Parameter | Rückgabewert | Aufgabe | Aufrufende Methode oder Ereignis |
| --- | --- | --- | --- | --- |
| [`GameTest.main`, Z. 18](../src/test/java/de/kreuzkreis/GameTest.java#L18), [`UiTest.main`, Z. 37](../src/test/java/de/kreuzkreis/UiTest.java#L37) | je `String[] args` | je `void` | Startet und zählt Regeltests beziehungsweise Swing-Tests. | `test.bat` |
| [`GameTest.newGameStartsEmpty`, Z. 41](../src/test/java/de/kreuzkreis/GameTest.java#L41) bis [`outOfBoundsRejected`, Z. 81](../src/test/java/de/kreuzkreis/GameTest.java#L81) | – | je `void` | Prüfen Startzustand, Spielerwechsel, belegte Felder und Brettgrenzen. | `GameTest.main` über `run` |
| [`GameTest.horizontalTripleLoses`, Z. 92](../src/test/java/de/kreuzkreis/GameTest.java#L92) bis [`diagonalDownLeftLoses`, Z. 137](../src/test/java/de/kreuzkreis/GameTest.java#L137) | – | je `void` | Prüfen Verlustreihen in vier Richtungen. | `GameTest.main` über `run` |
| [`GameTest.fullBoardWithoutTripleIsDraw`, Z. 151](../src/test/java/de/kreuzkreis/GameTest.java#L151), [`lastMoveCanCompleteTwoTriples`, Z. 178](../src/test/java/de/kreuzkreis/GameTest.java#L178) | – | je `void` | Prüfen ein volles Brett mit und ohne Verlust. | `GameTest.main` über `run` |
| [`GameTest.gapInRowDoesNotLose`, Z. 209](../src/test/java/de/kreuzkreis/GameTest.java#L209), [`gapInLineCompletesTwoTriples`, Z. 223](../src/test/java/de/kreuzkreis/GameTest.java#L223), [`resetClearsFinishedGame`, Z. 243](../src/test/java/de/kreuzkreis/GameTest.java#L243) | – | je `void` | Prüfen unterbrochene Reihen, sich überlappende Reihen und Neustart. | `GameTest.main` über `run` |
| [`GameTest.playOk`, Z. 272](../src/test/java/de/kreuzkreis/GameTest.java#L272), [`UiTest.play`, Z. 226](../src/test/java/de/kreuzkreis/UiTest.java#L226), [`Preview.play`, Z. 93](../src/test/java/de/kreuzkreis/Preview.java#L93) | je `Game game, int row, int col` | je `void` | Führen einen Modellzug aus und melden einen Fehler, wenn er abgelehnt wird. | Testfälle und Vorschau-Spielstände |
| [`UiTest.clickPlacesSymbol`, Z. 61](../src/test/java/de/kreuzkreis/UiTest.java#L61) bis [`buttonTextAfterLoss`, Z. 133](../src/test/java/de/kreuzkreis/UiTest.java#L133) | – | je `void` | Prüfen das Setzen mit Maus und Tastatur, ignorierte Klicks, Neustart und Knopftext. | `UiTest.main` über `run` |
| [`UiTest.focusRingOnTraversal`, Z. 146](../src/test/java/de/kreuzkreis/UiTest.java#L146) bis [`compactHitTesting`, Z. 182](../src/test/java/de/kreuzkreis/UiTest.java#L182) | – | je `void` | Prüfen den Fokus, die Tastenbelegung und die Zuordnung der Klickpositionen. | `UiTest.main` über `run` |
| [`UiTest.fireFocus`, Z. 195](../src/test/java/de/kreuzkreis/UiTest.java#L195) | `BoardPanel board, int id, FocusEvent.Cause cause` | `void` | Ruft die Fokus-Empfänger mit einem Testereignis auf. | `UiTest.focusRingOnTraversal` |
| [`UiTest.panel`, Z. 206 und 210](../src/test/java/de/kreuzkreis/UiTest.java#L206-L214) | `Game game`, optional `int width, int height` | `AppPanel` | Baut eine Testansicht und ordnet ihre Komponenten an. | UI-Testfälle, etwa `clickPlacesSymbol` und `compactHitTesting` |
| [`UiTest.click`, Z. 217](../src/test/java/de/kreuzkreis/UiTest.java#L217) | `BoardPanel board, int row, int col` | `void` | Simuliert den linken Tastendruck in der Feldmitte. | UI-Testfälle, etwa `clickPlacesSymbol` |
| [`GameTest.run`, Z. 307](../src/test/java/de/kreuzkreis/GameTest.java#L307), [`UiTest.run`, Z. 277](../src/test/java/de/kreuzkreis/UiTest.java#L277) | je `String name, Runnable test` | je `void` | Führt einen Test aus und zählt Erfolg oder Fehler. | `main` der jeweiligen Testklasse |
| [`Preview.main`, Z. 28](../src/test/java/de/kreuzkreis/Preview.java#L28) | `String[] args` | `void` | Startet die fünf Bildexporte. | `preview.bat` |
| [`Preview.midGame`, Z. 46](../src/test/java/de/kreuzkreis/Preview.java#L46), [`lostGame`, Z. 57](../src/test/java/de/kreuzkreis/Preview.java#L57), [`drawGame`, Z. 76](../src/test/java/de/kreuzkreis/Preview.java#L76) | – | je `Game` | Spielen die Züge für laufende Runde, Niederlage und Unentschieden nach. | `Preview.main` |
| [`Preview.render`, Z. 100 und 104](../src/test/java/de/kreuzkreis/Preview.java#L100-L104) | `String path, Game game`, optional `int width, int height` | `void` | Zeichnet das `AppPanel` in ein Bild und speichert es als PNG. | `Preview.main`, die Variante mit zwei Parametern ruft die mit vier auf |
| [`Preview.layout`, Z. 125](../src/test/java/de/kreuzkreis/Preview.java#L125), [`UiTest.layout`, Z. 232](../src/test/java/de/kreuzkreis/UiTest.java#L232) | je `Component component` | je `void` | Ordnet eine Komponente und dann mit derselben Methode ihre enthaltenen Komponenten an. | `Preview.render` beziehungsweise `UiTest.panel`, danach die jeweilige Methode selbst |

## 7. SELBSTTEST

Beantworte die Fragen zuerst ohne Editor. Bei den letzten Fragen lohnt es sich, Zwischenergebnisse oder Aufrufketten auf Papier zu notieren.

1. Wer beginnt eine neue Runde, wie viele Felder hat das Brett und wodurch verliert jemand?
2. Welche Klasse speichert die Regeln und welche beiden Ansichten lesen den Spielstand?
3. Was bedeuten `row`, `col` und `null` bei `board[row][col]`? Welche Koordinaten hat das Feld unten rechts?
4. Was steht nach dem ersten erfolgreichen Zug auf `(0,0)` im Array? Wie lauten Zugzahl, aktueller Spieler und Zustand?
5. Was liefert `Game.play(0, 0)`, wenn auf dem Feld schon das erste Kreuz steht? Was passiert mit dem Spieler und dem Zähler?
6. Welche eigene Methode empfängt den Druck der linken Maustaste auf dem Brett, und welche Methode macht daraus eine Feldkoordinate?
7. Warum ist ein direkter Aufruf von `game.play(-1, 0)` sicher? Erkläre die Reihenfolge der Prüfungen bis zum Arrayzugriff.
8. Welche Methode teilt sich der Enter-Tastendruck mit der Maus? Was passiert nur bei einem erfolgreichen `Game.play`?
9. Was ist der Unterschied zwischen `repaint()` und `paintComponent`? Welcher Teil des Programms liefert beim Zeichnen die Feldinhalte?
10. Warum ist `X X O X` in einer Zeile keine Verlustfolge, obwohl dort drei Kreuze stehen?
11. Prüfe den waagerechten Fall für einen neuen Stein auf `(0,2)`. Welche Starts und Enden ergeben sich für `-2`, `-1` und `0`? Welches Paar fällt heraus?
12. Welche zwei Dreierfolgen entstehen, wenn Kreuz in `X X . X` die Lücke in Zeile `0` füllt? Woher kennt die Oberfläche ihre Positionen?
13. Der 16. Zug von Kreis schließt eine Dreierfolge. Welchen Wert geben `Game.play`, `getState()` und `getCurrentPlayer()` danach jeweils zurück? Begründe die Reihenfolge.
14. Wie führt ein Klick auf „Nochmal spielen“ zurück zur leeren Runde? Verfolge den Weg bis zu den beiden Ansichten.
15. Das Zeichen und die gelbe Verlustmarkierung erscheinen korrekt, links bleibt aber nach dem Verlust der Knopftext „Neue Runde“. Welche Methoden und welche Zustandsabfrage prüfst du, um den Anzeigefehler einzugrenzen?

### Antworten zum Selbsttest

1. Kreuz beginnt. Das Brett hat `4 × 4 = 16` Felder. Wer mit dem eigenen Zug drei eigene Zeichen direkt waagerecht, senkrecht oder diagonal verbindet, verliert. [`Game.java`, Z. 7–16](../src/main/java/de/kreuzkreis/Game.java#L7-L16), [Z. 65–71](../src/main/java/de/kreuzkreis/Game.java#L65-L71)
2. `Game` hält die Daten und prüft die Regeln. `AppPanel` gibt dasselbe Objekt an `BoardPanel` und `SidePanel`. [`Game.java`, Z. 65–71](../src/main/java/de/kreuzkreis/Game.java#L65-L71), [Z. 102–125](../src/main/java/de/kreuzkreis/Game.java#L102-L125), [`AppPanel.java`, Z. 26–38](../src/main/java/de/kreuzkreis/ui/AppPanel.java#L26-L38)
3. `row` ist der Zeilenindex, `col` der Spaltenindex. `null` in einer Zelle bedeutet frei. Unten rechts liegt `(3,3)`, weil beide Indizes bei `0` beginnen. [`Game.java`, Z. 67–79](../src/main/java/de/kreuzkreis/Game.java#L67-L79), [Z. 165–167](../src/main/java/de/kreuzkreis/Game.java#L165-L167)
4. `board[0][0]` enthält `Player.KREUZ`. `moveCount` ist `1`, `currentPlayer` ist `Player.KREIS` und `state` bleibt `RUNNING`. [`Game.java`, Z. 102–125](../src/main/java/de/kreuzkreis/Game.java#L102-L125), [`GameTest.java`, Z. 57–69](../src/test/java/de/kreuzkreis/GameTest.java#L57-L69)
5. `play` liefert `false` und verlässt die Methode vor dem Schreiben und Zählen. Der Zähler bleibt `1` und Kreis bleibt am Zug. [`Game.java`, Z. 102–109](../src/main/java/de/kreuzkreis/Game.java#L102-L109), [`GameTest.java`, Z. 71–79](../src/test/java/de/kreuzkreis/GameTest.java#L71-L79)
6. `mousePressed` im Mausadapter von `BoardPanel` prüft die Taste und gibt `event.getPoint()` an `cellAt`. `cellAt` rechnet die Position in Zeile und Spalte um. [`BoardPanel.java`, Z. 88–106](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L88-L106), [Z. 258–269](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L258-L269)
7. Bei beendeter Runde kehrt `play` schon wegen `state.isOver()` zurück. Bei laufender Runde ruft es `isFree(-1, 0)` auf. `isInside` liefert `false`, und `&&` überspringt den Zugriff auf `board[-1][0]`. `play` gibt `false` zurück. [`Game.java`, Z. 77–79](../src/main/java/de/kreuzkreis/Game.java#L77-L79), [Z. 102–109](../src/main/java/de/kreuzkreis/Game.java#L102-L109), [Z. 165–167](../src/main/java/de/kreuzkreis/Game.java#L165-L167)
8. `activateFocus` für Enter und `mousePressed` für die Maus führen zu `attemptPlay`. Nach einem erfolgreichen Modellzug entfernt diese Methode die Mausvorschau, aktualisiert den Zeiger, ruft die übergebene Aktion `onMove` auf und fordert Neuzeichnen an. Bei Ende blendet sie zudem den Fokusring aus. [`BoardPanel.java`, Z. 96–106](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L96-L106), [Z. 162–169](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L162-L169), [Z. 201–225](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L201-L225)
9. `repaint()` meldet Zeichenbedarf an. Beim Zeichnen führt Swing `paintComponent` aus. Im Brett liest `paintSymbols` die Feldinhalte über `Game.playerAt` aus dem gemeinsamen Modell. [`BoardPanel.java`, Z. 214–224](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L214-L224), [Z. 272–290](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L272-L290), [Z. 376–390](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L376-L390)
10. Die beiden Abschnitte `X X O` und `X O X` enthalten jeweils `O`. Die Vergleiche verlangen, dass jedes der drei aufeinanderfolgenden Felder das Zeichen des Setzenden enthält. [`Game.java`, Z. 144–158](../src/main/java/de/kreuzkreis/Game.java#L144-L158), [`GameTest.java`, Z. 209–221](../src/test/java/de/kreuzkreis/GameTest.java#L209-L221)
11. `-2` ergibt `(0,0)` bis `(0,2)`, `-1` ergibt `(0,1)` bis `(0,3)`, `0` ergibt `(0,2)` bis `(0,4)`. Das letzte Paar scheitert an der Spalte `4`. [`Game.java`, Z. 65](../src/main/java/de/kreuzkreis/Game.java#L65), [Z. 141–151](../src/main/java/de/kreuzkreis/Game.java#L141-L151), [Z. 165–167](../src/main/java/de/kreuzkreis/Game.java#L165-L167)
12. Es entstehen `(0,0)–(0,1)–(0,2)` und `(0,1)–(0,2)–(0,3)`. `Game.play` speichert beide als `Triple`. `BoardPanel.paintLosingLines` liest die Liste und zeichnet zwischen den Mittelpunkten des ersten und dritten Feldes jeder Folge. [`GameTest.java`, Z. 223–241](../src/test/java/de/kreuzkreis/GameTest.java#L223-L241), [`Game.java`, Z. 111–116](../src/main/java/de/kreuzkreis/Game.java#L111-L116), [`BoardPanel.java`, Z. 353–373](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L353-L373)
13. `play` liefert `true`, `getState()` liefert `KREIS_LOST` und `getCurrentPlayer()` liefert `KREIS`. Der 16. Zug wird gesetzt und gezählt. Die Methode kehrt wegen des Treffers vor der Unentschiedenprüfung und vor dem Spielerwechsel zurück. [`Game.java`, Z. 81–95](../src/main/java/de/kreuzkreis/Game.java#L81-L95), [Z. 107–125](../src/main/java/de/kreuzkreis/Game.java#L107-L125), [`GameTest.java`, Z. 193–206](../src/test/java/de/kreuzkreis/GameTest.java#L193-L206)
14. Der Knopf löst den `ActionListener` in `SidePanel` aus. `onReset.run()` führt `AppPanel.resetGame` aus. `Game.reset()` leert die Felder und setzt Spieler, Zustand, Verlustliste und Zähler zurück. Danach ruft `AppPanel` beide `refresh()`-Methoden und `repaint()` auf. [`SidePanel.java`, Z. 43–57](../src/main/java/de/kreuzkreis/ui/SidePanel.java#L43-L57), [`AppPanel.java`, Z. 34–49](../src/main/java/de/kreuzkreis/ui/AppPanel.java#L34-L49), [`Game.java`, Z. 128–135](../src/main/java/de/kreuzkreis/Game.java#L128-L135), [`BoardPanel.java`, Z. 134–138](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L134-L138)
15. Prüfe nach `Game.play` zuerst den Zustandswert. Dann verfolge `BoardPanel.attemptPlay → onMove.run() → AppPanel.afterMove → SidePanel.refresh`. Bei `getState().isOver() == true` muss `refresh` den Text „Nochmal spielen“ setzen. So lässt sich prüfen, ob das Ergebnis korrekt vorliegt, die Aktion aufgerufen wird und der Knopf die richtige Beschriftung erhält. [`BoardPanel.java`, Z. 214–224](../src/main/java/de/kreuzkreis/ui/BoardPanel.java#L214-L224), [`AppPanel.java`, Z. 34–43](../src/main/java/de/kreuzkreis/ui/AppPanel.java#L34-L43), [`SidePanel.java`, Z. 53–57](../src/main/java/de/kreuzkreis/ui/SidePanel.java#L53-L57), [`UiTest.java`, Z. 133–144](../src/test/java/de/kreuzkreis/UiTest.java#L133-L144)
