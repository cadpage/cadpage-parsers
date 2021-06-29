package net.anei.cadpage.parsers.SC;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class SCOrangeburgCountyCParser extends DispatchOSSIParser {

  public SCOrangeburgCountyCParser() {
    super(CITY_CODES, "ORANGEBURG COUNTY", "SC",
          "( CANCEL ADDR CITY!" +
          "| FYI? ( ID CALL ADDR CITY? GPS1 GPS2 UNIT SRC EMPTY? X X " +
                  "| CALL ( MAP ADDR CITY SKIP INFO/N+? DATETIME! UNIT? ID2 SRC PRI SKIP NAME PHONE? BOX CLS ID/L? X X GPS1 GPS2 SKIP+" +
                         "| ADDR X X CITY ID! ) " +
                  ")  " +
          ") INFO/N+");
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }

  @Override
  public String getFilter() {
    return "CAD@orangeburgcounty.org";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{7,9}|", true);
    if (name.equals("ID2")) return new IdField("\\d{11}", true);
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("GPS1")) return new MyGPSField(1);
    if (name.equals("GPS2")) return new MyGPSField(2);
    if (name.equals("SRC"))  return new SourceField("[A-Z]{3}[A-Z0-9]|", true);
    if (name.equals("MAP")) return new MapField("[NS]\\d{2}[EW]", true);
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("PHONE")) return new PhoneField("\\d{7}|\\d{10}", true);
    if (name.equals("BOX")) return new BoxField("\\d{1,4}");
    if (name.equals("CLS")) return new SkipField("PHONE", true);
    return super.getField(name);
  }

  private class MyCityField extends CityField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.isEmpty() && Character.isDigit(field.charAt(0))) return false;
      parse(field, data);
      return true;
    }
  }

  private static final Pattern GPS_PTN = Pattern.compile("[-+]?\\d{2}\\.\\d{6,}|");
  private class MyGPSField extends GPSField {
    public MyGPSField(int type) {
      super(type);
      setPattern(GPS_PTN, true);
    }
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
      "NRWY", "NORWAY",
      "ORBG", "ORANGEBURG",
      "RWSV", "ROWESVILLE",
      "SNTE", "SANTEE",
      "SPRI", "SPRINGFIELD",
      "VANC", "VANCE"
  });
}
