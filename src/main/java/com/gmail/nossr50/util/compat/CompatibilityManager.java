package com.gmail.nossr50.util.compat;

import com.gmail.nossr50.locale.LocaleLoader;
import com.gmail.nossr50.mcMMO;
import com.gmail.nossr50.util.StringUtils;
import com.gmail.nossr50.util.compat.layers.attackcooldown.PlayerAttackCooldownExploitPreventionLayer;
import com.gmail.nossr50.util.compat.layers.persistentdata.AbstractPersistentDataLayer;
import com.gmail.nossr50.util.compat.layers.persistentdata.SpigotPersistentDataLayer;
import com.gmail.nossr50.util.compat.layers.persistentdata.SpigotTemporaryDataLayer;
import com.gmail.nossr50.util.nms.NMSVersion;
import com.gmail.nossr50.util.platform.MinecraftGameVersion;
import org.bukkit.command.CommandSender;

import java.util.HashMap;

/**
 *
 * These classes are a band-aid solution for adding NMS support into 2.1.XXX
 * In 2.2 we are switching to modules and that will clean things up significantly
 *
 */
public class CompatibilityManager {
    private HashMap<CompatibilityType, Boolean> supportedLayers;
    private boolean isFullyCompatibleServerSoftware = true; //true if all compatibility layers load successfully
    private final MinecraftGameVersion minecraftGameVersion;
    private final NMSVersion nmsVersion;

    /* Compatibility Layers */
    private PlayerAttackCooldownExploitPreventionLayer playerAttackCooldownExploitPreventionLayer;
    private AbstractPersistentDataLayer persistentDataLayer;

    public CompatibilityManager(MinecraftGameVersion minecraftGameVersion) {
        mcMMO.p.getLogger().info("Loading compatibility layers...");
        this.minecraftGameVersion = minecraftGameVersion;
        this.nmsVersion = determineNMSVersion();
        init();
        mcMMO.p.getLogger().info("Finished loading compatibility layers.");
    }

    private void init() {
        initSupportedLayersMap();
        initCompatibilityLayers();
    }

    private void initSupportedLayersMap() {
        supportedLayers = new HashMap<>(); //Init map

        for(CompatibilityType compatibilityType : CompatibilityType.values()) {
            //TODO: Remove later
            if(compatibilityType == CompatibilityType.PLAYER_ATTACK_COOLDOWN_EXPLOIT_PREVENTION)
                continue;

            supportedLayers.put(compatibilityType, false); //All layers are set to false when initialized
        }
    }

    /**
     * Initialize all necessary compatibility layers
     * For any unsupported layers, load a dummy layer
     */
    private void initCompatibilityLayers() {
        initPersistentDataLayer();

        isFullyCompatibleServerSoftware = true;
    }

    private void initPersistentDataLayer() {
        if(minecraftGameVersion.getMinorVersion().asInt() >= 14 || minecraftGameVersion.getMajorVersion().asInt() >= 2) {

            persistentDataLayer = new SpigotPersistentDataLayer();
        } else {

            persistentDataLayer = new SpigotTemporaryDataLayer();
        }

        supportedLayers.put(CompatibilityType.PERSISTENT_DATA, true);
    }

    //TODO: move to text manager
    public void reportCompatibilityStatus(CommandSender commandSender) {
        if(isFullyCompatibleServerSoftware) {
            commandSender.sendMessage(LocaleLoader.getString("mcMMO.Template.Prefix",
                    "mcMMO is fully compatible with the currently running server software."));
        } else {
            //TODO: Better messages for each incompatible layer
            for(CompatibilityType compatibilityType : CompatibilityType.values()) {
                if(!supportedLayers.get(compatibilityType)) {
                    commandSender.sendMessage(LocaleLoader.getString("mcMMO.Template.Prefix",
                            LocaleLoader.getString("Compatibility.Layer.Unsupported",  StringUtils.getCapitalized(compatibilityType.toString()))));
                }
            }
        }

        commandSender.sendMessage(LocaleLoader.getString("mcMMO.Template.Prefix", "NMS Status - " + nmsVersion.toString()));
    }

    public boolean isCompatibilityLayerOperational(CompatibilityType compatibilityType) {
        return supportedLayers.get(compatibilityType);
    }

    public boolean isFullyCompatibleServerSoftware() {
        return isFullyCompatibleServerSoftware;
    }

    public NMSVersion getNmsVersion() {
        return nmsVersion;
    }

    private NMSVersion determineNMSVersion() {
        if (minecraftGameVersion.getMajorVersion().asInt() == 1) {
            switch (minecraftGameVersion.getMinorVersion().asInt()) {
                case 12:
                    return NMSVersion.NMS_1_12_2;
                case 13:
                    return NMSVersion.NMS_1_13_2;
                case 14:
                    return NMSVersion.NMS_1_14_4;
                case 15:
                    return NMSVersion.NMS_1_15_2;
                case 16:
                    if (minecraftGameVersion.getPatchVersion().asInt() == 1) {
                        return NMSVersion.NMS_1_16_1;
                    }
            }
        }

        return NMSVersion.UNSUPPORTED;
    }

    public PlayerAttackCooldownExploitPreventionLayer getPlayerAttackCooldownExploitPreventionLayer() {
        return playerAttackCooldownExploitPreventionLayer;
    }

    public AbstractPersistentDataLayer getPersistentDataLayer() {
        return persistentDataLayer;
    }
}
