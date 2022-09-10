package net.anei.cadpage.parsers.MO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

/**
 * Macon County, MO
 */
public class MOMaconCountyParser extends FieldProgramParser {

  public MOMaconCountyParser() {
    this("MACON COUNTY", "MO");
  }

  MOMaconCountyParser(String defCity, String defState) {
    super(defCity, defState,
      "Event_Number:ID! Police_Event_Type:CALL! Fire_Event_Type:CALL! EMS_Event_Type:CALL! Latitude:GPS1! Longitude:GPS2! Address:ADDR! Location:APT_PLACE? City:CITY! Zip:SKIP! ESN:SKIP! Class:SKIP! District:SRC! UnitTimes:TIMES Notes:INFO/N+");
  }

  @Override
  public String getFilter() {
    return "noreply@macondomain.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseFields(body.split("\n"), 12, data)) return false;
    if (data.strUnit.length() == 0) {
      data.strUnit = data.strSource;
      data.strSource = "";
    }
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{8}-\\d+", true);
    if (name.equals("APT_PLACE")) return new MyAptPlaceField();
    if (name.equals("TIMES")) return new MyTimesField();
    return super.getField(name);
  }

  private static final Pattern APT_PTN = Pattern.compile("(?:APT|LOT|RM|ROOM|SUITE) *(.*)", Pattern.CASE_INSENSITIVE);
  private class MyAptPlaceField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = APT_PTN.matcher(field);
      if (match.matches()) {
        data.strApt = append(data.strApt, "-", match.group(1));
      } else {
        data.strPlace = field;
      }
    }

    @Override
    public String getFieldNames() {
      return "APT PLACE";
    }
  }

  private class MyTimesField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      String times = "";

      for (String part : field.split(",")) {
        part = part.trim();
        times = append(times, "\n", part);
        Parser p = new Parser(part);
        String unit = p.get(' ');
        String code = p.get(' ');
        if (code.equals("DSPTCH")) {
          data.strUnit = append(data.strUnit, ",", unit);
        } else if (code.equals("CLRD")) {
          data.msgType = MsgType.RUN_REPORT;
        }
      }

      if (data.msgType == MsgType.RUN_REPORT) data.strSupp = times;
    }

    @Override
    public String getFieldNames() {
      return "UNIT INFO";
    }
  }
}
