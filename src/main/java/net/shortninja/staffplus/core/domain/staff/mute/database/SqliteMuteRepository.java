package net.shortninja.staffplus.core.domain.staff.mute.database;

import be.garagepoort.mcioc.IocBean;
import be.garagepoort.mcsqlmigrations.SqlConnectionProvider;
import net.shortninja.staffplus.core.application.config.Options;
import net.shortninja.staffplus.core.common.exceptions.DatabaseException;
import net.shortninja.staffplus.core.domain.player.PlayerManager;
import net.shortninja.staffplus.core.domain.staff.mute.Mute;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

@IocBean(conditionalOnProperty = "storage.type=sqlite")
public class SqliteMuteRepository extends AbstractSqlMuteRepository {

    public SqliteMuteRepository(PlayerManager playerManager, Options options, SqlConnectionProvider sqlConnectionProvider) {
        super(playerManager, options, sqlConnectionProvider);
    }

    @Override
    public int addMute(Mute mute) {
        try (Connection connection = getConnection();
             PreparedStatement insert = connection.prepareStatement("INSERT INTO sp_muted_players(reason, player_uuid, player_name, issuer_uuid, issuer_name, end_timestamp, creation_timestamp, server_name, soft_mute) " +
                 "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);")) {
            connection.setAutoCommit(false);
            insert.setString(1, mute.getReason());
            insert.setString(2, mute.getTargetUuid().toString());
            insert.setString(3, mute.getTargetName());
            insert.setString(4, mute.getIssuerUuid().toString());
            insert.setString(5, mute.getIssuerName());
            if (mute.getEndTimestamp() == null) {
                insert.setNull(6, Types.BIGINT);
            } else {
                insert.setLong(6, mute.getEndTimestamp());
            }
            insert.setLong(7, mute.getCreationTimestamp());
            insert.setString(8, options.serverName);
            insert.setBoolean(9, mute.isSoftMute());
            insert.executeUpdate();

            Statement statement = connection.createStatement();
            ResultSet generatedKeys = statement.executeQuery("SELECT last_insert_rowid()");
            int generatedKey = -1;
            if (generatedKeys.next()) {
                generatedKey = generatedKeys.getInt(1);
            }
            connection.commit(); // Commits transaction.

            return generatedKey;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

}
