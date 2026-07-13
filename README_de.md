# EnderDragon Plugin

[![Release Workflow](https://github.com/dervonnebe/EnderDragon/actions/workflows/release.yml/badge.svg)](https://github.com/dervonnebe/EnderDragon/actions/workflows/release.yml)

*[🇬🇧 Read English version here](README.md)*

Ein modernes PaperMC Plugin (1.21+), welches Statistiken beim Besiegen des Enderdrachen aufzeichnet und einstellbare Drop-Chancen für Dracheneier, Drachenköpfe und Elytren bietet.

## Features
- **Einstellbare Drop-Chancen**: Konfiguriere die Wahrscheinlichkeit, mit der ein Drachenei, Drachenkopf oder eine Elytra nach dem Tod des Enderdrachen droppt.
- **Statistiken**: Zählt automatisch, wie viele Drachen getötet wurden und wie viele Items insgesamt gedroppt sind.
- **Interaktives GUI**: Ein modernes Menü (basierend auf Kyori Adventure MiniMessage) zur Ansicht der Server-Statistiken.
- **Mehrsprachig**: Unterstützt Deutsch und Englisch (anpassbar über die Konfigurations- und Sprachdateien).

## Befehle & Berechtigungen
- `/dragon stats` - Öffnet das Statistik-Menü (Permission: `enderdragon.stats` - Standard: true)
- `/dragon setchance <egg|head|elytra> <chance>` - Setzt die Drop-Chance ingame (Permission: `enderdragon.admin`)
- `/dragon resetstats` - Setzt alle globalen Statistiken zurück (Permission: `enderdragon.admin`)
- `/dragon reload` - Lädt die Konfiguration und Sprachdateien neu (Permission: `enderdragon.admin`)

## Konfiguration
Das Plugin generiert eine `config.yml`, in der die `language` (`de` oder `en`) sowie die Basis-Wahrscheinlichkeiten (`chances`) eingestellt werden können.
Farben und Texte lassen sich vollständig über das MiniMessage-Format in den Dateien `messages_de.yml` und `messages_en.yml` anpassen.

## Installation
1. Lade das neueste Release von der [Releases Seite](../../releases) herunter.
2. Ziehe die `.jar` Datei in den `plugins` Ordner deines Servers.
3. Starte den Server neu.

## Lizenz
Dieses Projekt ist unter der MIT-Lizenz lizenziert. Siehe die Datei [LICENSE](LICENSE) für weitere Details.
