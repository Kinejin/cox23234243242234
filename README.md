# AutoridadDeLaAvaricia Plugin

This repository contains the source of the Paper plugin **AutoridadDeLaAvaricia**. It provides a simplified recreation of Regulus Corneas' Authority of Greed. The plugin registers a command `/avaricia` for administrators to grant players the *Gen de la Bruja*. Upon using it, the player gains access to the *Corazón de la Avaricia* which toggles a special ability inventory with three skill items.

The implementation focuses on inventory switching, Lion's Heart invulnerability and a drop chance for the gene on death. Details of the ability effects can be expanded further.

## Building

Make sure you have Maven installed, then run:

```bash
mvn package
```

The compiled plugin JAR will be placed in the `target` directory.
