package com.palmergames.bukkit.towny.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.TownySettings;

/**
 * 
 * @author ElgarL
 *
 */
public class SQL_Schema {
	
	private static String tb_prefix = TownySettings.getSQLTablePrefix().toUpperCase();
	
	private static String getTOWNS() {
		
		return "CREATE TABLE IF NOT EXISTS " + tb_prefix + "TOWNS ("
				+ "`name` VARCHAR(32) NOT NULL,"
				+ "`residents` mediumtext,"
				+ "`mayor` mediumtext,"
				+ "`nation` mediumtext NOT NULL,"
				+ "`assistants` text DEFAULT NULL,"
				+ "`townBoard` mediumtext DEFAULT NULL,"
				+ "`tag` mediumtext DEFAULT NULL,"
				+ "`protectionStatus` mediumtext DEFAULT NULL,"
				+ "`bonus` int(11) DEFAULT 0,"
				+ "`purchased` int(11)  DEFAULT 0,"
				+ "`taxpercent` bool NOT NULL DEFAULT '0',"
				+ "`taxes` float DEFAULT 0,"
				+ "`hasUpkeep` bool NOT NULL DEFAULT '0',"
				+ "`plotPrice` float DEFAULT NULL,"
				+ "`plotTax` float DEFAULT NULL,"
				+ "`commercialPlotPrice` float DEFAULT NULL,"
				+ "`commercialPlotTax` float NOT NULL,"
				+ "`embassyPlotPrice` float NOT NULL,"
				+ "`embassyPlotTax` float NOT NULL,"
				+ "`open` bool NOT NULL DEFAULT '0',"
				+ "`public` bool NOT NULL DEFAULT '0',"
				+ "`admindisabledpvp` bool NOT NULL DEFAULT '0',"
				+ "`homeblock` mediumtext NOT NULL,"
				+ "`townBlocks` mediumtext NOT NULL,"
				+ "`spawn` mediumtext NOT NULL,"
				+ "`outpostSpawns` mediumtext DEFAULT NULL,"
				+ "PRIMARY KEY (`name`)"
				+ ")";
	}
	
	private static String getRESIDENTS() {
		
		return "CREATE TABLE IF NOT EXISTS " + tb_prefix + "RESIDENTS ("
				+ " `name` VARCHAR(16) NOT NULL,"
				+ "`town` mediumtext,"
				+ "`town-ranks` mediumtext,"
				+ "`nation-ranks` mediumtext,"
				+ "`lastOnline` BIGINT NOT NULL,"
				+ "`registered` BIGINT NOT NULL,"
				+ "`isNPC` bool NOT NULL DEFAULT '0',"
				+ "`title` mediumtext,"
				+ "`surname` mediumtext,"
				+ "`protectionStatus` mediumtext,"
				+ "`friends` mediumtext,"
				+ "`townBlocks` mediumtext,"
				+ "PRIMARY KEY (`name`)"
				+ ")";
	}
	
	private static String getNATIONS() {
		
		return "CREATE TABLE IF NOT EXISTS " + tb_prefix + "NATIONS ("
				+ "`name` VARCHAR(32) NOT NULL,"
				+ "`towns` mediumtext NOT NULL,"
				+ "`capital` mediumtext NOT NULL,"
				+ "`assistants` mediumtext NOT NULL,"
				+ "`tag` mediumtext NOT NULL,"
				+ "`allies` mediumtext NOT NULL,"
				+ "`enemies` mediumtext NOT NULL,"
				+ "`taxes` float NOT NULL,"
				+ "`neutral` bool NOT NULL DEFAULT '0', "
				+ "PRIMARY KEY (`name`)"
				+ ")";
	}
	
	private static String getTOWNBLOCKS() {
		
		return "CREATE TABLE IF NOT EXISTS " + tb_prefix + "TOWNBLOCKS ("
				+ "`world` VARCHAR(32) NOT NULL,"
				+ "`x` mediumint NOT NULL,"
				+ "`z` mediumint NOT NULL,"
				+ "`permissions` mediumtext NOT NULL,"
				+ "`locked` bool NOT NULL DEFAULT '0',"
				+ "`changed` bool NOT NULL DEFAULT '0',"
				+ "PRIMARY KEY (`world`,`x`,`z`)"
				+ ")";
	}
	
	private static String getWORLDS() {
		
		return "CREATE TABLE IF NOT EXISTS " + tb_prefix + "WORLDS ("
				+ "`name` VARCHAR(32) NOT NULL,"
				+ "`towns` mediumtext NOT NULL,"
				+ "`claimable` bool NOT NULL DEFAULT '0',"
				+ "`pvp` bool NOT NULL DEFAULT '0',"
				+ "`forcepvp` bool NOT NULL DEFAULT '0',"
				+ "`forcetownmobs` bool NOT NULL DEFAULT '0',"
				+ "`worldmobs` bool NOT NULL DEFAULT '0',"
				+ "`firespread` bool NOT NULL DEFAULT '0',"
				+ "`forcefirespread` bool NOT NULL DEFAULT '0',"
				+ "`explosions` bool NOT NULL DEFAULT '0',"
				+ "`forceexplosions` bool NOT NULL DEFAULT '0',"
				+ "`endermanprotect` bool NOT NULL DEFAULT '0',"
				+ "`disableplayertrample` bool NOT NULL DEFAULT '0',"
				+ "`disablecreaturetrample` bool NOT NULL DEFAULT '0',"
				+ "`unclaimedZoneBuild` bool NOT NULL DEFAULT '0',"
				+ "`unclaimedZoneDestroy` bool NOT NULL DEFAULT '0',"
				+ "`unclaimedZoneSwitch` bool NOT NULL DEFAULT '0',"
				+ "`unclaimedZoneItemUse` bool NOT NULL DEFAULT '0',"
				+ "`unclaimedZoneName` mediumtext NOT NULL,"
				+ "`unclaimedZoneIgnoreIds` mediumtext NOT NULL,"
				+ "`usingPlotManagementDelete` bool NOT NULL DEFAULT '0',"
				+ "`plotManagementDeleteIds` mediumtext NOT NULL,"
				+ "`usingPlotManagementMayorDelete` bool NOT NULL DEFAULT '0',"
				+ "`plotManagementMayorDelete` mediumtext NOT NULL,"
				+ "`usingPlotManagementRevert` bool NOT NULL DEFAULT '0',"
				+ "`plotManagementRevertSpeed` long NOT NULL,"
				+ "`plotManagementIgnoreIds` mediumtext NOT NULL,"
				+ "`usingPlotManagementWildRegen` bool NOT NULL DEFAULT '0',"
				+ "`plotManagementWildRegenEntities` mediumtext NOT NULL,"
				+ "`plotManagementWildRegenSpeed` long NOT NULL,"
				+ "`usingTowny` bool NOT NULL DEFAULT '0',"
				+ "PRIMARY KEY (`name`)"
				+ ")";
	}
	
	/**
	 * Create and update database schema.
	 * 
	 * @param cntx a database connection
	 * @param db_name the name of a database
	 */
	public static void initTables(Connection cntx, String db_name) {
		
		/*
		 *  Fetch TOWNS Table schema.
		 */
		String town_create = SQL_Schema.getTOWNS();
		
		try {
			
			Statement s = cntx.createStatement();
			s.executeUpdate(town_create);
			
			TownyMessaging.sendDebugMsg("Table TOWNS is ok!");
			
		} catch (SQLException ee) {
			TownyMessaging.sendErrorMsg("Creating table TOWNS :" + ee.getMessage());
		}

		/*
		 * Update the table structure for older databases.
		 */
		String town_update;

		try {
			town_update = "ALTER TABLE `" + db_name + "`.`" + tb_prefix + "TOWNS` ADD COLUMN `admindisabledpvp`  bool";
			Statement s = cntx.createStatement();
			s.executeUpdate(town_update);

			TownyMessaging.sendDebugMsg("Table TOWNS is updated!");
			
		} catch (SQLException ee) {
			
			if (ee.getErrorCode() != 1060)
				TownyMessaging.sendErrorMsg("Error updating table TOWNS :" + ee.getMessage());
			
		}

		/*
		 *  Fetch RESIDENTS Table schema.
		 */
		String resident_create = SQL_Schema.getRESIDENTS();
		
		try {
			
			Statement s = cntx.createStatement();
			s.executeUpdate(resident_create);
			TownyMessaging.sendDebugMsg("Table RESIDENTS is ok!");
			
		} catch (SQLException ee) {
			
			TownyMessaging.sendErrorMsg("Error Creating table RESIDENTS :" + ee.getMessage());
			
		}

		/*
		 * Update the table structure for older databases.
		 */
		String resident_update;

		try {
			
			resident_update = "ALTER TABLE `" + db_name + "`.`" + tb_prefix + "RESIDENTS` ADD COLUMN `town-ranks`  mediumtext";
			Statement s = cntx.createStatement();
			s.executeUpdate(resident_update);

			resident_update = "ALTER TABLE `" + db_name + "`.`" + tb_prefix + "RESIDENTS` ADD COLUMN `nation-ranks`  mediumtext";
			s = cntx.createStatement();
			s.executeUpdate(resident_update);

			TownyMessaging.sendDebugMsg("Table RESIDENTS is updated!");
			
		} catch (SQLException ee) {
			
			if (ee.getErrorCode() != 1060)
				TownyMessaging.sendErrorMsg("Error updating table RESIDENTS :" + ee.getMessage());
			
		}

		/*
		 *  Fetch NATIONS Table schema.
		 */
		String nation_create = SQL_Schema.getNATIONS();
		
		try {
			
			Statement s = cntx.createStatement();
			s.executeUpdate(nation_create);
			TownyMessaging.sendDebugMsg("Table NATIONS is ok!");
			
		} catch (SQLException ee) {
			
			TownyMessaging.sendErrorMsg("Error Creating table NATIONS : " + ee.getMessage());
			
		}

		String townblock_create = SQL_Schema.getTOWNBLOCKS();
		
		try {
			
			Statement s = cntx.createStatement();
			s.executeUpdate(townblock_create);
			TownyMessaging.sendDebugMsg("Table TOWNBLOCKS is ok!");
			
		} catch (SQLException ee) {
			
			TownyMessaging.sendErrorMsg("Error Creating table TOWNBLOCKS : " + ee.getMessage());
			
		}

		/*
		 * Update the table structure for older databases.
		 */
		String townblocks_update;

		try {
			townblocks_update = "ALTER TABLE `" + db_name + "`.`" + tb_prefix + "TOWNBLOCKS` ADD COLUMN `name`  mediumtext";
			Statement s = cntx.createStatement();
			s.executeUpdate(townblocks_update);

			TownyMessaging.sendDebugMsg("Table TOWNBLOCKS is updated!");
		} catch (SQLException ee) {
			if (ee.getErrorCode() != 1060)
				TownyMessaging.sendErrorMsg("Error updating table TOWNBLOCKS :" + ee.getMessage());
			// TownyMessaging.sendErrorMsg("Code: " + ee.getErrorCode());
		}

		/*
		 *  Fetch WORLDS Table schema.
		 */
		String world_create = SQL_Schema.getWORLDS();
		
		try {
			
			Statement s = cntx.createStatement();
			s.executeUpdate(world_create);
			TownyMessaging.sendDebugMsg("Table WORLDS is ok!");
			
		} catch (SQLException ee) {
			
			TownyMessaging.sendErrorMsg("Error Creating table WORLDS : " + ee.getMessage());
			
		}

		TownyMessaging.sendDebugMsg("Checking done!");
	}

}
