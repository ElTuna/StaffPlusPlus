package net.shortninja.staffplus.core.domain.staff.mute.appeals;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.configuration.ConfigProperty;
import be.garagepoort.mcioc.configuration.ConfigTransformer;
import net.shortninja.staffplus.core.domain.actions.config.ConfiguredCommand;
import net.shortninja.staffplus.core.domain.actions.config.ConfiguredCommandsConfigTransformer;

import java.util.List;

@IocBean
public class MuteAppealConfiguration {

    @ConfigProperty("mute-module.appeals.enabled")
    public boolean enabled;
    @ConfigProperty("mute-module.appeals.resolve-reason-enabled")
    public boolean resolveReasonEnabled;
    @ConfigProperty("mute-module.appeals.unmute-on-approve")
    public boolean unmuteOnApprove;

    @ConfigProperty("permissions:mutes.appeals.approve")
    public String approveAppealPermission;
    @ConfigProperty("permissions:mutes.appeals.reject")
    public String rejectAppealPermission;
    @ConfigProperty("permissions:mutes.appeals.create")
    public String createAppealPermission;
    @ConfigProperty("permissions:mutes.appeals.create-others")
    public String permissionCreateOthersAppeal;
    @ConfigProperty("permissions:mutes.appeals.notifications")
    public String permissionNotifications;

    @ConfigProperty("mute-module.appeals.fixed-reason")
    public boolean fixedAppealReason;
    @ConfigProperty("mute-module.appeals.reasons")
    public List<String> appealReasons;

    @ConfigProperty("mute-module.on-approved-commands")
    @ConfigTransformer(ConfiguredCommandsConfigTransformer.class)
    public List<ConfiguredCommand> onApprovedCommands;
    @ConfigProperty("mute-module.on-rejected-commands")
    @ConfigTransformer(ConfiguredCommandsConfigTransformer.class)
    public List<ConfiguredCommand> onRejectedCommands;

}
