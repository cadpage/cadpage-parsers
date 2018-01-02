package net.anei.cadpage.parsers.SC;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class SCOrangeburgCountyCParser extends DispatchOSSIParser {
  
  public SCOrangeburgCountyCParser() {
    super(CITY_CODES, "ORANGEBURG COUNTY", "SC", 
          "( CANCEL ADDR CITY!" +
          "| CALL ADDR X X CITY ID! ) INFO/N+");
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{7}", true);
    return super.getField(name);
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BOWM", "BOWMAN",
      "BWMN", "BOWMAN",
      "COPE", "COPE",
      "CRDV", "CORDOVA",
      "EUTW", "EUTAWVILLE",
      "HHLL", "HOLLY HILL",
      "NEES", "NEESES",
      "NRTH", "NORTH",
      "ORBG", "ORANGEBURG",
      "RWSV", "ROWESVILLE",
      "SNTE", "SANTEE",
      "VANC", "VANCE"
  });
}
