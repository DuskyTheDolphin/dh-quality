# DH Quality

A small client-side NeoForge mod for Minecraft 1.21.1 that runs Distant Horizons at vertical quality **HIGH**, with a
config switch to turn that off.

At Distant Horizons' default vertical quality, far cliffs and steep slopes in the LODs show as coloured stripes. At
HIGH they read as mostly solid faces, and in our tests the frame rate didn't measurably change. Players rarely find
that setting, and a modpack can't change it for existing players: `DistantHorizons.toml` is a file pack updates usually
leave alone, so a new value there only reaches fresh installs.

DH Quality sets the value through Distant Horizons' own API at every game start instead. It is an in-memory override:
your `DistantHorizons.toml` is never written. Turn the option off, or remove the jar, and your own setting is back.
While it is on, Distant Horizons' menu can't change this one setting.

**Unofficial:** this is not a Distant Horizons team project and isn't affiliated with or endorsed by them. Please
report problems with this mod here, not to Distant Horizons.

Designed by DuskyTheDolphin, written with AI assistance.

## Install

Client only. Servers don't need it.

- Minecraft 1.21.1
- NeoForge 21.1 or newer (built against 21.1.250)
- Distant Horizons (optional; built and tested against 3.3.3). Without it the mod does nothing.

Release test for 1.0.1: a NeoForge 21.1.250 client with only Distant Horizons 3.3.3 and this mod installed; it reported
vertical quality HIGH.

## Config

`config/forgeward_dhquality-client.toml`, also in Mods > DH Quality > Config:

| Key | Default | Meaning |
|---|---|---|
| `forceVerticalHigh` | `true` | Run Distant Horizons at vertical quality HIGH. Off: your own setting in DH's menu applies. Takes effect at once; DH rebuilds its LODs. |

## Log

Once Distant Horizons' API is ready, shortly after the game starts:

```
[forgeward_dhquality] 1.0.1 ready: forceVerticalHigh true, vertical quality now HIGH (player's file: MEDIUM, API accepted true)
```

Without Distant Horizons: `[forgeward_dhquality] 1.0.1 ready: Distant Horizons absent, nothing to do`.

## Building

`build.ps1` compiles with `javac` against NeoForge 21.1.250's named client jars (it regenerates them into
`clientlibs/` with the server's NeoForge installer if missing), a server's `libraries/`, and the Distant Horizons jar
in that server's `mods/` (compile only). Set `SERVER` and `JDK`. Mod id `forgeward_dhquality`.

## License

MIT, see [LICENSE](LICENSE).
