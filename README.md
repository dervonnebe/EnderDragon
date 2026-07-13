# EnderDragon Plugin

[![Release Workflow](https://github.com/dervonnebe/EnderDragon/actions/workflows/release.yml/badge.svg)](https://github.com/dervonnebe/EnderDragon/actions/workflows/release.yml)

*[🇩🇪 Deutsche Version hier lesen](README_de.md)*

A modern PaperMC plugin (1.21+) that tracks statistics when defeating the Ender Dragon and allows for custom drop chances for Dragon Eggs, Dragon Heads, and Elytras.

## Features
- **Custom Drop Chances**: Configure drop rates for Dragon Egg, Dragon Head, and Elytra when the Ender Dragon dies.
- **Statistics Tracking**: Tracks how many dragons were killed and how many items have dropped globally.
- **Interactive GUI**: A beautiful GUI powered by Kyori Adventure MiniMessage to view all statistics.
- **Multi-Language**: Supports both English and German out of the box (editable via config & language files).

## Commands & Permissions
- `/dragon stats` - Opens the statistics GUI (Permission: `enderdragon.stats` - default: true)
- `/dragon setchance <egg|head|elytra> <chance>` - Set drop chances in-game (Permission: `enderdragon.admin`)
- `/dragon resetstats` - Reset all global statistics (Permission: `enderdragon.admin`)
- `/dragon reload` - Reload the configuration and language files (Permission: `enderdragon.admin`)

## Configuration
The plugin generates a `config.yml` where you can set the `language` (`en` or `de`) and base `chances`. 
Text colors and translations are fully customizable using MiniMessage format in the `messages_en.yml` and `messages_de.yml` files.

## Installation
1. Download the latest release from the [Releases page](../../releases).
2. Drop the `.jar` file into your server's `plugins` folder.
3. Restart the server.

## License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
