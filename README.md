# Project

Tim, Sandy, Matthias, Morten, Linus

## Thema: Cards against Humanity

### Regeln

Jeder Spieler bekommt zehn weiße Karten.
Ein zufälliger Spieler beginnt als Zar. Dieser liest die erste schwarze Karte laut vor, mit Lücken.
Alle anderen Spieler wählen aus ihren zehn weißen Karten ihren Favoriten. 
Sobald alle Spieler ihre Karte abgegeben haben, werden sie nacheinander aufgedeckt und in ihrer Kombination mit der schwarzen Karte vorgelesen. Die, die der Zar am witzigsten findet, kann er auswählen, der Spieler hinter dieser Karte erhält einen Punkt. Nächste Runde beginnt mit dem nächsten Spieler als Zar in der Reihe.

### Aufteilung

- Game Server: Tim
- Scrum Master: Sandy
- GUI & Client: Matthias
- Card Assistant to the Manager: Linus

### Technische Herangehensweise:

- Kanban: Trello, Sandy erstellt die Tasks
- VCS: Git via Github, ein Repo mit client/ und server/
    - Tim Branch
    - Matthias Branch
    - Master Branch
- Client-Host-Communication: Port 8761 via custom protocol -> plain data


Erste Schritt: Ping zwischen Client und Host.

#### Statistik

- Gewinner
- Punkte
- Wins
