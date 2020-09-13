package net.shortninja.staffplus.util.database.migrations.sqlite;

import net.shortninja.staffplus.util.database.migrations.Migration;

public class V4_CreatePlayerDataTableMigration implements Migration {
    @Override
    public String getStatement() {
        return "CREATE TABLE IF NOT EXISTS sp_playerdata ( GlassColor INTEGER NOT NULL DEFAULT 0, Password VARCHAR(255) NOT NULL DEFAULT '', Player_UUID VARCHAR(36) PRIMARY KEY, Name VARCHAR(18) NOT NULL);";
    }

    @Override
    public int getVersion() {
        return 4;
    }
}
