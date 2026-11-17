package com.forgeward.dhquality;

import com.mojang.logging.LogUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

/**
 * DH Quality: runs Distant Horizons at vertical quality HIGH, with a way out. DH's LOD cliffs at MEDIUM show as coloured
 * stripes; HIGH makes them mostly solid, at no measurable frame cost in our tests. DistantHorizons.toml is a file a
 * modpack update usually preserves, so a pack change there reaches only fresh installs. This sets the value through
 * DH's API each game start instead: an in-memory override, never written to the player's file, so removing the jar or
 * turning off `forceVerticalHigh` hands the value back. While it is on, DH's own menu can't change this one setting.
 */
@Mod(value = DhQuality.MOD_ID, dist = Dist.CLIENT)
public final class DhQuality {
    public static final String MOD_ID = "forgeward_dhquality";
    static final Logger LOG = LogUtils.getLogger();
    static final String VERSION = "1.0.1";

    static final ModConfigSpec SPEC;
    static final ModConfigSpec.BooleanValue FORCE;

    static {
        ModConfigSpec.Builder b = new ModConfigSpec.Builder();
        FORCE = b.comment("Run Distant Horizons at vertical quality HIGH (solid LOD cliffs instead of stripes). Off: your own setting in DH's menu applies. Takes effect at once; DH rebuilds its LODs.")
                .define("forceVerticalHigh", true);
        SPEC = b.build();
    }

    private static boolean applied, wanted, reported;

    public DhQuality(IEventBus modBus, ModContainer container) {
        container.registerConfig(ModConfig.Type.CLIENT, SPEC);
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        modBus.addListener(ModConfigEvent.Reloading.class, e -> { if (e.getConfig().getSpec() == SPEC) applied = false; });
        NeoForge.EVENT_BUS.addListener(ClientTickEvent.Post.class, e -> tick());
    }

    /** DH's API appears some time after construction, so this retries each tick until it is there. */
    private static void tick() {
        if (applied || !SPEC.isLoaded()) return;
        if (!ModList.get().isLoaded("distanthorizons")) {
            applied = true;
            LOG.info("[forgeward_dhquality] {} ready: Distant Horizons absent, nothing to do", VERSION);
            return;
        }
        wanted = FORCE.get();
        String result;
        try { result = DhBridge.apply(wanted); }
        catch (Throwable t) {
            applied = true;
            LOG.warn("[forgeward_dhquality] {} FAILED: {}", VERSION, t.toString());
            return;
        }
        if (result == null) return;   // not ready yet
        applied = true;
        LOG.info("[forgeward_dhquality] {} {}: forceVerticalHigh {}, {}", VERSION, reported ? "changed" : "ready", wanted, result);
        reported = true;
    }
}
