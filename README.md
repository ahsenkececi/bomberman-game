# 💣 Online Multiplayer Bomberman Game

A Java-based multiplayer Bomberman game built to demonstrate the practical application of **8 design patterns** in a real-world, real-time game architecture. Supports both local (same-keyboard) and online multiplayer via socket programming.

> Developed as a Design Patterns course project (CENG 326) at İzmir Katip Çelebi University.

## Features

- Classic Bomberman mechanics: bomb placement, four-directional explosions, breakable/hard/unbreakable walls
- **Local multiplayer** — two players on one keyboard (WASD + Space vs Arrow Keys + Enter)
- **Online multiplayer** — client-server architecture over TCP sockets, host acts as authoritative server
- Three enemy AI types, including an **A\* pathfinding** enemy
- Stackable power-up system (speed boost, bomb power, bomb count, shield, ghost)
- Persistent user accounts, game stats, and a MySQL-backed leaderboard
- Three selectable visual themes (Desert, Forest, City)

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Build tool | Maven |
| GUI | Java Swing |
| Networking | Java Sockets (`java.net`) |
| Database | MySQL (JDBC) |
| IDE | IntelliJ IDEA |

## Architecture

The project follows an **MVC** architecture with a clear separation between game logic (`models`), rendering (`views`), and input/flow control (`controllers`). Data access is isolated behind a **Repository** layer so business logic never touches SQL directly.

```
src/main/java/com/bomberman/
├── models/       # Game entities (Player, Enemy, Bomb, Map...)
├── views/        # Swing UI components
├── controllers/  # Game flow & input handling
├── factory/      # Enemy / PowerUp / Wall factories
├── strategy/     # Enemy AI behaviors (Static, Chasing, A* pathfinding)
├── observer/     # Game event broadcasting (GameEventManager)
├── decorator/    # Stackable player power-ups
├── state/        # Game state machine (Menu, Playing, Paused, GameOver)
├── repository/   # Database access layer
├── database/     # DB connection manager
├── network/      # Client-server multiplayer
└── theme/        # Visual theme management
```

## Design Patterns

| Pattern | Where | Why |
|---|---|---|
| **Singleton** | `GameManager`, `DatabaseManager`, `GameStateManager` | Single consistent global state, one DB connection pool |
| **Factory** | `EnemyFactory`, `PowerUpFactory`, `WallFactory` | Centralized, extensible object creation (Open/Closed Principle) |
| **Decorator** | `PlayerDecorator` + `SpeedBoostDecorator`, `BombPowerDecorator`, `BombCountDecorator` | Stack power-up effects on the player at runtime without subclass explosion |
| **Strategy** | `IEnemyBehavior` + `StaticBehavior`, `ChasingBehavior`, `IntelligentBehavior` (A\*) | Swap enemy AI at runtime, isolate pathfinding logic |
| **Observer** | `GameEventManager` (subject) → `Player` (observer) | Decoupled event broadcasting (e.g. enemy death → score update) |
| **State** | `IGameState` + `MenuState`, `PlayingState`, `PausedState`, `GameOverState` | Replace conditional game-flow logic with clean state transitions |
| **Repository** | `UserRepository`, `GameStatsRepository`, `LeaderboardRepository` | Isolate SQL/JDBC from business logic |
| **MVC** | `models/`, `views/`, `controllers/` | Overall architectural separation of concerns |

## Getting Started

### Prerequisites
- JDK 17+
- Maven
- MySQL Server

### Setup
```bash
git clone https://github.com/ahsenkececi/bombermanGame.git
cd bombermanGame
```

Create a `database.properties` file (see `.gitignore` — this file is intentionally excluded from version control) with your local MySQL credentials, then run the schema setup for the `users`, `game_stats`, and `leaderboard` tables.

### Run
```bash
mvn clean install
mvn exec:java
```

## Network Architecture

- **GameServer** listens on port `8888` and acts as the authoritative host
- **GameClient** connects, sends only input commands (not state), and receives validated state updates
- Event-based message types: `PLAYER_CONNECTED`, `GAME_START`, `PLAYER_MOVE`, `BOMB_PLACED`, `BOMB_EXPLODED`, `POWERUP_COLLECTED`, `PLAYER_DIED`, `GAME_OVER`

## Author

**Ahsen Keçeci** — [github.com/ahsenkececi](https://github.com/ahsenkececi)
