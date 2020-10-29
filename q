[1mdiff --git a/cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAFranklinCountyCParser.java b/cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAFranklinCountyCParser.java[m
[1mindex ebf62861..30f41031 100644[m
[1m--- a/cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAFranklinCountyCParser.java[m
[1m+++ b/cadpage-parsers/src/main/java/net/anei/cadpage/parsers/PA/PAFranklinCountyCParser.java[m
[36m@@ -1,16 +1,82 @@[m
 package net.anei.cadpage.parsers.PA;[m
 [m
[32m+[m[32mimport java.util.Properties;[m
[32m+[m
 import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;[m
 [m
 public class PAFranklinCountyCParser extends DispatchA19Parser {[m
   [m
   public PAFranklinCountyCParser() {[m
[31m-    super("FRANKLIN COUNTY", "PA");[m
[32m+[m[32m    super(CITY_CODES, "FRANKLIN COUNTY", "PA");[m
   }[m
   [m
   @Override[m
   public String getFilter() {[m
     return "FlexRapidNotification@dccnotify.com";[m
   }[m
[32m+[m[41m  [m
[32m+[m[32m  @Override[m
[32m+[m[32m  public int getMapFlags() {[m
[32m+[m[32m    return MAP_FLG_PREFER_GPS;[m
[32m+[m[32m  }[m
[32m+[m[41m  [m
[32m+[m[32m  private static final Properties CITY_CODES = buildCodeTable(new String[]{[m
[32m+[m[32m      "ADM", "ADAMS COUNTY",[m
[32m+[m[32m      "ALG", "ALLEGANY COUNTY",[m
[32m+[m[32m      "ALL", "ALLEGHENY COUNTY",[m
[32m+[m[32m      "ANT", "ANTRIM TWP",[m
[32m+[m[32m      "BAC", "BALTIMORE CITY",[m
[32m+[m[32m      "BAL", "BALTIMORE COUNTY",[m
[32m+[m[32m      "BED", "BEDFORD COUNTY",[m
[32m+[m[32m      "BEK", "BERKELEY COUNTY",[m
[32m+[m[32m      "BER", "BERKS COUNTY",[m
[32m+[m[32m      "BLA", "BLAIR COUNTY",[m
[32m+[m[32m      "CAM", "CAMBRIA COUNTY",[m
[32m+[m[32m      "CAR", "CARROLL COUNTY",[m
[32m+[m[32m      "CEN", "CENTRE COUNTY",[m
[32m+[m[32m      "CHB", "CHAMBERSBURG",[m
[32m+[m[32m      "CUM", "CUMBERLAND COUNTY",[m
[32m+[m[32m      "DAU", "DAUPHIN COUNTY",[m
[32m+[m[32m      "FAN", "FANNETT TWP",[m
[32m+[m[32m      "FRE", "FREDERICK COUNTY",[m
[32m+[m[32m      "FUL", "FULTON COUNTY",[m
[32m+[m[32m      "GAR", "GARRETT COUNTY",[m
[32m+[m[32m      "GRC", "GREENCASTLE",[m
[32m+[m[32m      "GRE", "GREENE TWP",[m
[32m+[m[32m      "GUI", "GUILFORD TWP",[m
[32m+[m[32m      "HAM", "HAMILTON TWP",[m
[32m+[m[32m      "HUN", "HUNTINGDON COUNTY",[m
[32m+[m[32m      "JEF", "JEFFERSON COUNTY",[m
[32m+[m[32m      "JUN", "JUNIATA COUNTY",[m
[32m+[m[32m      "LAN", "LANCASTER COUNTY",[m
[32m+[m[32m      "LEB", "LEBANON COUNTY",[m
[32m+[m[32m      "LEH", "LEHIGH COUNTY",[m
[32m+[m[32m      "LET", "LETTERKENNY TWP",[m
[32m+[m[32m      "LUR", "LURGAN TWP",[m
[32m+[m[32m      "MAB", "MONT ALTO",[m
[32m+[m[32m      "MCB", "MCCONNELLSBURG",[m
[32m+[m[32m      "MER", "MERCERSBURG",[m
[32m+[m[32m      "MET", "METAL TWP",[m
[32m+[m[32m      "MIF", "MIFFLIN COUNTY",[m
[32m+[m[32m      "MON", "MONTGOMERY TWP",[m
[32m+[m[32m      "MOR", "MORGAN COUNTY",[m
[32m+[m[32m      "MTR", "MONTOUR COUNTY",[m
[32m+[m[32m      "NOR", "NORTHUMBERLAND COUNTY",[m
[32m+[m[32m      "ORR", "ORRSTOWN",[m
[32m+[m[32m      "PER", "PERRY COUNTY",[m
[32m+[m[32m      "PET", "PETERS TWP",[m
[32m+[m[32m      "QUI", "QUINCY TWP",[m
[32m+[m[32m      "SCH", "SCHUYLKILL COUNTY",[m
[32m+[m[32m      "SHI", "SHIPPENSBURG",[m
[32m+[m[32m      "SNY", "SNYDER COUNTY",[m
[32m+[m[32m      "SOM", "SOMERSET COUNTY",[m
[32m+[m[32m      "SOU", "SOUTHAMPTON TWP",[m
[32m+[m[32m      "STT", "ST THOMAS TWP",[m
[32m+[m[32m      "WAC", "WASHINGTON COUNTY",[m
[32m+[m[32m      "WAR", "WARREN TWP",[m
[32m+[m[32m      "WAS", "WASHINGTON TWP",[m
[32m+[m[32m      "WAY", "WAYNESBORO",[m
[32m+[m[32m      "YOR", "YORK COUNTY"[m
[32m+[m[32m  });[m
 [m
 }[m
Submodule cadpage-private contains modified content
