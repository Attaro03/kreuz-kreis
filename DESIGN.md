# Designnotizen

## Herkunft

- Design-Skill: [Hallmark v2.0.0](https://github.com/Nutlope/hallmark)
- Theme: **Hum** (beim Deal 1/3, Genre `playful`)
- Anpassung: Hallmark prüft HTML/CSS. Für diese Java-Swing-Anwendung wurden die
  übertragbaren Regeln manuell angewendet: Farb- und Schrift-Tokens, Kontrast,
  sichtbarer Fokus, keine Animationen, keine Emojis, keine Gradienten, eine Idee
  pro Fläche. `sloplint` ist ein Browser-Werkzeug und läuft hier nicht.

## Material

| Token | Wert | Einsatz |
|---|---|---|
| `PAPER` | `#F7F5EC` | Grundfläche, Brettbogen |
| `PAPER_2` | `#F0EDE0` | Sekundärfläche |
| `CARD` | `#FCFBF6` | helle Karte |
| `FRINGE` | `#FEFCF8` | Papierkern an gerissenen Kanten |
| `INK` | `#12161C` | Text und Linien |
| `INK_2` | `#272C32` | Fließtext der Regel |
| `MUTED` | `#565B63` | Sekundärtext |
| `ACCENT` | `#F7D21A` | Pear-Highlighter und Primärknopf |
| `ACCENT_DEEP` | `#C9A000` | Unterkante des Knopfs |
| `KREUZ` | `#0064BE` | Kreuz, Fokusring |
| `KREIS` | `#E34B50` | Kreis, Verlust-Sticker |
| `SUGAR` | `#C6DFEF` | Papiermatte unter dem Brett |

Die Werte sind die sRGB-Entsprechungen der OKLCH-Tokens aus
`references/themes/hum.md`. Das Coral wurde gegenüber dem Theme leicht vertieft
(`#E34B50` statt `#FF5E63`), damit das Kreis-Symbol auf Creme mindestens
3:1 Kontrast erreicht.

## Typografie

- **Chillax Medium** für Fließtext, Regeln, Knopf und Footer.
- **Chillax Semibold** für Titel und Status, leicht negativ gesperrt.
- Titel 52 pt, Status 24 pt, Regel 15 pt, Footer 12,5 pt. Keine Schrift unter
  12,5 pt.
- Beide Schnitte liegen als TTF in `src/main/resources/fonts` und werden über
  die Classpath-Ressource geladen.

## Komposition

- **Linke Papierspalte (390 px):** Titel, Regel, Zugstatus, ein Karton-Knopf,
  Footer. Viel Weißraum; die Fläche gehört dem Brett.
- **Rechtes Brett:** ein quadratischer Creme-Bogen auf einer leicht gedrehten
  Sugar-Paper-Matte. Beide haben eine gerissene Unterkante mit hellem
  Papierkern. Papierkorn liegt über jeder Fläche.
- **Raster:** 4×4, dünne blau-schwarze Linien, keine Karten in Karten.
- **Signature:** Der Pear-Highlighter liegt exakt über der oder den
  Verlustdreierfolgen; der Sticker `Drei!` beziehungsweise `Unentschieden` sitzt
  an der oberen rechten Ecke des Bogens.
- **Symbole:** Kreuz in Blau, Kreis in Coral, beide mit runden Kappen. Form und
  Farbe unterscheiden die Spieler, nicht nur die Farbe.
- **Zustände:** Hover füllt ein freies Feld zart pear; Tastaturfokus zeigt einen
  blauen Ring; der Knopf senkt sich beim Drücken; nach dem Ende sind keine
  weiteren Züge möglich.

## Bewegung

Keine. Die Zustände ändern sich sofort. Damit gibt es nichts, was bei
reduzierter Bewegung abgeschaltet werden müsste.

## Bewusste Entscheidungen

- Der Ergebnis-Sticker ist der einzige laute Moment in Coral.
- Der Knopf ist pear, weil er die einzige Aktion außerhalb des Bretts ist.
- Die Statuszeile hebt beim Zug den Namen des aktiven Spielers mit Pear hervor.
- Beim Remis stehen zwei kleine Symbole (Kreuz und Kreis) neben dem Status.
- Der native Fensterrahmen bleibt erhalten; die Seite zeichnet kein
  nachgebautes Betriebssystemfenster.
