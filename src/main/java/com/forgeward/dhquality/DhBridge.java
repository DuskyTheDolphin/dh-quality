package com.forgeward.dhquality;

import com.seibel.distanthorizons.api.DhApi;
import com.seibel.distanthorizons.api.enums.config.EDhApiVerticalQuality;
import com.seibel.distanthorizons.api.interfaces.config.IDhApiConfigValue;

/** Loaded only when DH is present. Returns null while DH's API isn't ready. */
final class DhBridge {
    static String apply(boolean force) {
        if (DhApi.Delayed.configs == null) return null;
        IDhApiConfigValue<EDhApiVerticalQuality> v = DhApi.Delayed.configs.graphics().verticalQuality();
        EDhApiVerticalQuality file = v.getTrueValue();
        boolean ok = force ? v.setValue(EDhApiVerticalQuality.HIGH, "forgeward_dhquality") : v.clearValue();
        return "vertical quality now " + v.getValue() + " (player's file: " + file + ", API accepted " + ok + ")";
    }

    private DhBridge() {}
}
