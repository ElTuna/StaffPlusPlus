package net.shortninja.staffplus.core.domain.staff.reporting.bungee;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcioc.IocMessageListener;
import net.shortninja.staffplus.core.common.Constants;
import net.shortninja.staffplus.core.common.bungee.BungeeClient;
import net.shortninja.staffplus.core.common.config.Options;
import net.shortninja.staffplus.core.domain.staff.reporting.ReportNotifier;
import net.shortninja.staffplus.core.domain.staff.reporting.bungee.dto.ReportReopenedBungee;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.Optional;

import static net.shortninja.staffplus.core.common.Constants.BUNGEE_CORD_CHANNEL;

@IocBean(conditionalOnProperty = "server-sync-module.report-sync=true")
@IocMessageListener(channel = BUNGEE_CORD_CHANNEL)
public class ReportReopenBungeeListener implements PluginMessageListener {

    private final BungeeClient bungeeClient;
    private final ReportNotifier reportNotifier;
    private final Options options;

    public ReportReopenBungeeListener(BungeeClient bungeeClient, ReportNotifier reportNotifier, Options options) {
        this.bungeeClient = bungeeClient;
        this.reportNotifier = reportNotifier;
        this.options = options;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (options.serverSyncConfiguration.isReportSyncEnabled()) {
            Optional<ReportReopenedBungee> reportCreatedBungee = bungeeClient.handleReceived(channel, Constants.BUNGEE_REPORT_REOPEN_CHANNEL, message, ReportReopenedBungee.class);
            reportCreatedBungee.ifPresent(report -> reportNotifier.notifyReportReopen(report.getReopenByName(), report.getReporterName()));
        }
    }
}
