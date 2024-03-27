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
          "| FYI? ID? CALL ADDR CITY? GPS1 GPS2 UNIT ( SRC | PLACE ) EMPTY? X X! " +
          ") INFO/N+? ID2");
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
  protected boolean parseMsg(String body, Data data) {
    if (!body.startsWith("CAD:")) body = "CAD:" + body;
    if (!super.parseMsg(body, data)) return false;
    if (!data.strCity.isEmpty() || !data.strCallId.isEmpty()) return true;
    int pt = body.lastIndexOf(';');
    if (pt < 0) return false;
    String id2 = body.substring(pt+1).trim();
    if (ID2_PTN.matcher(id2).matches()) {
      data.strCallId = append(data.strCallId, "/", id2);
      return true;
    }
    return false;
  }

  @Override
  public String getProgram() {
    return super.getProgram() + " ID";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{7,11}|", true);
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("GPS1")) return new MyGPSField(1);
    if (name.equals("GPS2")) return new MyGPSField(2);
    if (name.equals("SRC"))  return new SourceField("[A-Z]{3}[A-Z0-9]|", true);
    if (name.equals("MAP")) return new MapField("[NS]\\d{2}[EW]", true);
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("PHONE")) return new PhoneField("\\d{7}|\\d{10}", true);
    if (name.equals("BOX")) return new BoxField("\\d{1,4}");
    if (name.equals("CLS")) return new SkipField("PHONE", true);
    if (name.equals("ID2")) return new MyId2Field();
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

  private static final Pattern ID2_PTN = Pattern.compile("\\d{11}");
  private class MyId2Field extends IdField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!isLastField()) return false;
      if (!ID2_PTN.matcher(field).matches()) return false;
      data.strCallId = append(data.strCallId, "/", field);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
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
