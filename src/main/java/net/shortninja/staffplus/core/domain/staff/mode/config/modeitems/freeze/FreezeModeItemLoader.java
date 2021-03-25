package net.shortninja.staffplus.core.domain.staff.mode.config.modeitems.freeze;

import net.shortninja.staffplus.core.common.JavaUtils;
import net.shortninja.staffplus.core.domain.staff.mode.config.ModeItemLoader;
import org.bukkit.configuration.file.FileConfiguration;

public class FreezeModeItemLoader extends ModeItemLoader<FreezeModeConfiguration> {
    @Override
    protected String getModuleName() {
        return "freeze-module";
    }

    @Override
    protected FreezeModeConfiguration load(FileConfiguration config) {
        FreezeModeConfiguration modeItemConfiguration = new FreezeModeConfiguration(getModuleName(),
            config.getInt("staff-mode.freeze-module.timer"),
            stringToSound(sanitize(config.getString("staff-mode.freeze-module.sound"))),
            config.getBoolean("staff-mode.freeze-module.prompt"),
            config.getString("staff-mode.freeze-module.prompt-title"),
            config.getBoolean("staff-mode.freeze-module.chat"),
            config.getBoolean("staff-mode.freeze-module.damage"),
            config.getBoolean("staff-mode.freeze-module.title-message-enabled"),
            JavaUtils.stringToList(config.getString("staff-mode.freeze-module.logout-commands")));
        return super.loadGeneralConfig(config, modeItemConfiguration);
    }
}