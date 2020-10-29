package net.anei.cadpage.parsers.PA;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class PAFranklinCountyCParser extends DispatchA19Parser {
  
  public PAFranklinCountyCParser() {
    super(CITY_CODES, "FRANKLIN COUNTY", "PA");
  }
  
  @Override
  public String getFilter() {
    return "FlexRapidNotification@dccnotify.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ADM", "ADAMS COUNTY",
      "ALG", "ALLEGANY COUNTY",
      "ALL", "ALLEGHENY COUNTY",
      "ANT", "ANTRIM TWP",
      "BAC", "BALTIMORE CITY",
      "BAL", "BALTIMORE COUNTY",
      "BED", "BEDFORD COUNTY",
      "BEK", "BERKELEY COUNTY",
      "BER", "BERKS COUNTY",
      "BLA", "BLAIR COUNTY",
      "CAM", "CAMBRIA COUNTY",
      "CAR", "CARROLL COUNTY",
      "CEN", "CENTRE COUNTY",
      "CHB", "CHAMBERSBURG",
      "CUM", "CUMBERLAND COUNTY",
      "DAU", "DAUPHIN COUNTY",
      "FAN", "FANNETT TWP",
      "FRE", "FREDERICK COUNTY",
      "FUL", "FULTON COUNTY",
      "GAR", "GARRETT COUNTY",
      "GRC", "GREENCASTLE",
      "GRE", "GREENE TWP",
      "GUI", "GUILFORD TWP",
      "HAM", "HAMILTON TWP",
      "HUN", "HUNTINGDON COUNTY",
      "JEF", "JEFFERSON COUNTY",
      "JUN", "JUNIATA COUNTY",
      "LAN", "LANCASTER COUNTY",
      "LEB", "LEBANON COUNTY",
      "LEH", "LEHIGH COUNTY",
      "LET", "LETTERKENNY TWP",
      "LUR", "LURGAN TWP",
      "MAB", "MONT ALTO",
      "MCB", "MCCONNELLSBURG",
      "MER", "MERCERSBURG",
      "MET", "METAL TWP",
      "MIF", "MIFFLIN COUNTY",
      "MON", "MONTGOMERY TWP",
      "MOR", "MORGAN COUNTY",
      "MTR", "MONTOUR COUNTY",
      "NOR", "NORTHUMBERLAND COUNTY",
      "ORR", "ORRSTOWN",
      "PER", "PERRY COUNTY",
      "PET", "PETERS TWP",
      "QUI", "QUINCY TWP",
      "SCH", "SCHUYLKILL COUNTY",
      "SHI", "SHIPPENSBURG",
      "SNY", "SNYDER COUNTY",
      "SOM", "SOMERSET COUNTY",
      "SOU", "SOUTHAMPTON TWP",
      "STT", "ST THOMAS TWP",
      "WAC", "WASHINGTON COUNTY",
      "WAR", "WARREN TWP",
      "WAS", "WASHINGTON TWP",
      "WAY", "WAYNESBORO",
      "YOR", "YORK COUNTY"
  });

}
