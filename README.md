# Codex Naturalis (Bachelor Thesis Project)

This repository contains the digital Java implementation of the board game *Codex Naturalis*, originally published by Cranio Creations.

The project was developed as part of our Bachelor thesis in Computer Science at Politecnico di Milano (Prof. Cugola section), with the goal of designing and implementing a fully playable online version of the game.

## Key Features

- Multiplayer support via both **Socket** and **RMI** protocols
- **Graphical User Interface (GUI)** and **Command Line Interface (CLI)**
- **Server-client architecture**
- In-game **chat functionality**
- Full game logic implementation based on official *Codex Naturalis* rules


## Table of Contents
1. [Overview](#Overview)
2. [Authors](#Authors)
3. [Development State](#Development-State)
4. [Installation](#Installation)
5. [Running the game](#Running-the-game)
6. [Rules](#Rules)
7. [Requirements](#Requirements)
8. [License](#License)
## Overview
This repository contains the digital java implementation of the game **Codex Naturalis**, published by **Cranio Games**.

## Authors
Politecnico di Miliano - Prof. Cugola Section - **Group GC38** 
-  **Matilde Restelli**   ([@matirestelli](https://github.com/matirestelli)) _matilde.restelli@mail.polimi.it_
-  **Marco Scianna** ([@Marcoscianna](https://github.com/Marcoscianna)) _marco.scianna@mail.polimi.it_
-  **Niccolò Salvi** ([@NiccoloSalvi](https://github.com/NiccoloSalvi)) _niccolo1.salvi@mail.polimi.it_
- **Alessio Villa** ([@alessiovilla](https://github.com/alessiovilla)) _alessio3.villa@mail.polimi.it_

## Development State

| Functionality   | State          |
|-----------------|----------------|
| Chat            | :green_circle: |
| Multi Game      | :green_circle: |
| RMI             | :green_circle: |
| TCP             | :green_circle: |
| CLI             | :green_circle: |
| GUI             | :green_circle: |
## Installation
In order to run Codex Naturalis, you can either clone this repository
```
git clone https://github.com/NiccoloSalvi/ing-sw-2024-restelli-salvi-scianna-villa.git
```
or just download the jar file.
## Running the game
The game has 3 different jars:

- One for those who want to play via socket
- One for those who want to play via RMI
- One for the server

### Run as a Server
To run the server jar file, use the command
```
java -jar server-jar-with-dependencies.jar
```
from the command line in the jar's folder. The socket service is hosted on 12345 port and RMI service on 1099

### Run as a Socket Client
To run the socket client jar file, use the command
```
java -jar Socketclient-jar-with-dependencies.jar 192.168.1.18
```
from the command line in the jar's folder, where 192.168.1.18 is the server IP address.

### Run as RMI Client
To run the socket client jar file, use the command
```
java -jar RMIclient-jar-with-dependencies.jar 192.168.1.18
```
from the command line in the jar's folder, where 192.168.1.18 is the server IP address.

Both server and client app can run on Unix systems (macOS, only CLI) or Windows system (on Powershell, cmd and WSL).

## License
All rights to **Codex Naturalis** are owned by [Cranio Creations](https://www.craniocreations.it/),  which provided the graphical resources to be used for educational purposes only.
