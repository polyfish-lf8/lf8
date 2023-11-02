# Starter für das LF08 Projekt

Erstellen Sie einen Fork dieses Projektes auf Github. Wählen Sie einen Namen und passen Sie diesen auch in der pom.xml in Zeile 12, 14 und 15 an.

## Requirements
* Docker https://docs.docker.com/get-docker/
* Docker compose (bei Windows und Mac schon in Docker enthalten) https://docs.docker.com/compose/install/

## Endpunkt
```
http://localhost:8089
```
## Swagger
```
http://localhost:8089/swagger
```


# Postgres
### Terminal öffnen
für alles gilt, im Terminal im Ordner docker/local sein
```bash
cd docker/local
```
### Postgres starten
```bash
docker compose up
```
Achtung: Der Docker-Container läuft dauerhaft! Wenn er nicht mehr benötigt wird, sollten Sie ihn stoppen.

### Postgres stoppen
```bash
docker compose down
```

### Postgres Datenbank wipen, z.B. bei Problemen
```bash
docker compose down
docker volume rm local_lf8_starter_postgres_data
docker compose up
```

### Intellij-Ansicht für Postgres Datenbank einrichten
```bash
1. Lasse den Docker-Container mit der PostgreSQL-Datenbank laufen
2. im Ordner resources die Datei application.properties öffnen und die URL der Datenbank kopieren
3. rechts im Fenster den Reiter Database öffnen
4. In der Database-Symbolleiste auf das Datenbanksymbol mit dem Schlüssel klicken
5. auf das Pluszeichen klicken
6. Datasource from URL auswählen
7. URL der DB einfügen und PostgreSQL-Treiber auswählen, mit OK bestätigen
8. Username lf8_starter und Passwort secret eintragen (siehe application.properties), mit Apply bestätigen
9. im Reiter Schemas alle Häkchen entfernen und lediglich vor lf8_starter_db und public Häkchen setzen
10. mit Apply und ok bestätigen 
```
# Keycloak

### Keycloak Token
1. Auf der Projektebene [GetBearerToken.http](./GetBearerToken.http) öffnen.
2. Neben der Request auf den grünen Pfeil drücken
3. Aus dem Reponse das access_token kopieren

# Für Herr Schwerk
Uns sind beim Testen keine Fehler aufgefallen. Wir haben 30 Tests und diese sind alle Erfolgreich durchlaufen. Sollten sie Fehler bei den Tests bekommen,
versuchen sie erst die Beispieldaten in der SampleProjectDataCreator.java zu deaktivieren. Der Grund dafür, dass wir keine Relations verwendet haben, ist weil,
wir dadurch einen Loop erzeugt haben und am Ende eine 100 KB Antwort vom Server erhalten haben, welche nur ein Projekt und ein Mitarbeiter beihielt.

Auch haben wir keinen Fehler dafür eingebaut, wenn ein Nutzer versuchen sollte, einen Mitarbeiter aus einem Projekt zu entfernen, der in diesem Projekt nicht eingetragen ist.
Dies liegt daran, dass so der Nutzer direkt einmal die neuen Daten kriegt und sich so der Client einfach aktualisiert.

Als extra Feature haben wir einen Cache für die API Verbindung zum Employee Service eingebaut. Dieser Speichert die Daten so lange, wie sie laut dem Server gültig sind.
Sollten sie diesen Cache nicht verwenden wollen, müssen sie lediglich das Argument '--disable-cache' an das Programm, beim Start, übergeben.