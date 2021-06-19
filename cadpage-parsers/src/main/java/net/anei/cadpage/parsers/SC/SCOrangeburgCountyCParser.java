package net.anei.cadpage.parsers.SC;

import java.util.Properties;

import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class SCOrangeburgCountyCParser extends DispatchOSSIParser {

  public SCOrangeburgCountyCParser() {
    super(CITY_CODES, "ORANGEBURG COUNTY", "SC",
          "( CANCEL ADDR CITY!" +
          "| FYI? ( ID CALL ADDR CITY GPS1 GPS2 UNIT SRC EMPTY X X " +
                  "| CALL ADDR X X CITY ID! " +
                  ")  " +
          ") INFO/N+");
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{7,9}|", true);
    return super.getField(name);
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BOWM", "BOWMAN",
      "BWMN", "BOWMAN",
      "COPE", "COPE",
      "CRDV", "CORDOVA",
      "ELLR", "ELLOREE",
      "EUTW", "EUTAWVILLE",
      "HHLL", "HOLLY HILL",
      "NEES", "NEESES",
      "NORT", "NORTH",
      "NRTH", "NORTH",
      "ORBG", "ORANGEBURG",
      "RWSV", "ROWESVILLE",
      "SNTE", "SANTEE",
      "SPRI", "SPRINGFIELD",
      "VANC", "VANCE"
  });
}
