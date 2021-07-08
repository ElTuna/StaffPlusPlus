package net.shortninja.staffplus.core.domain.staff.reporting.bungee.receive;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMessageListener;
import net.shortninja.staffplus.core.common.Constants;
import net.shortninja.staffplus.core.common.bungee.BungeeClient;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.domain.staff.reporting.bungee.dto.ReportBungeeDto;
import net.shortninja.staffplus.core.domain.staff.reporting.bungee.events.ReportCreatedBungeeEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.Optional;

import static net.shortninja.staffplus.core.common.Constants.BUNGEE_CORD_CHANNEL;

@IocBean(conditionalOnProperty = "server-sync-module.report-sync=true")
@IocMessageListener(channel = BUNGEE_CORD_CHANNEL)
public class ReportCreatedBungeeListener implements PluginMessageListener {

    private final BungeeClient bungeeClient;
    private final Options options;

    public ReportCreatedBungeeListener(BungeeClient bungeeClient, Options options) {
        this.bungeeClient = bungeeClient;
        this.options = options;
    }


    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if(options.serverSyncConfiguration.isReportSyncEnabled()) {
            Optional<ReportBungeeDto> reportCreatedBungee = bungeeClient.handleReceived(channel, Constants.BUNGEE_REPORT_CREATED_CHANNEL, message, ReportBungeeDto.class);
            reportCreatedBungee.ifPresent(b -> Bukkit.getPluginManager().callEvent(new ReportCreatedBungeeEvent(b)));
        }
    }
}