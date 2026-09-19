package net.anei.cadpage.parsers.PA;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class PAMonroeCountyDParser extends FieldProgramParser {

  public PAMonroeCountyDParser() {
    super(PAMonroeCountyParser.CITY_LIST, "MONROE COUNTY", "PA",
          "CALL PLACE? ADDRCITY/ZS! CROSS_STREETS:X! GPS! INFO/N+ INCIDENT_#:ID! TIMES+");
  }

  @Override
  public String getFilter() {
    return "messaging@iamresponding.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private String times;
  private Set<String> srcSet = new HashSet<>();;

  private static final Pattern ID_PTN = Pattern.compile("\\[\\d{4}-\\d{8} \\S+\\]");

  @Override
  protected boolean parseMsg(String body, Data data) {
    times = "";
    srcSet.clear();
    if (!body.contains("INCIDENT #:")) {
      Matcher match = ID_PTN.matcher(body);
      if (!match.find()) return false;
      int pt = match.start();
      body = body.substring(0,pt)+"INCIDENT #:"+body.substring(pt);
    }
    if (!parseFields(body.split("\n"), data)) return false;
    if (data.msgType == MsgType.RUN_REPORT) data.strSupp = append(times, "\n", data.strSupp);
    times = null;
    srcSet.clear();
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("GPS")) return new MyGPSField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("TIMES")) return new TimesField();
    return super.getField(name);
  }

  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals(getRelativeField(+1))) return;
      super.parse(field, data);
    }

  }

  private class MyGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      if (!field.contains("/")) abort();
      field = field.replace('/', ',');
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_JUNK_PTN = Pattern.compile("\\*+\\d\\d?/\\d\\d?/\\d{4}\\*+");
  private static final Pattern INFO_HDR_PTN = Pattern.compile("\\d\\d:\\d\\d:\\d\\d \\d+ - *");

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (INFO_JUNK_PTN.matcher(field).matches()) return;
      Matcher match = INFO_HDR_PTN.matcher(field);
      if (match.lookingAt()) field = field.substring(match.end());
      super.parse(field, data);
    }
  }

  private class TimesField extends Field {
    @Override
    public void parse(String field, Data data) {
      if (field.isEmpty()) return;
      if (field.equals("Agency,")) return;
      if (field.equals("Units & Status Times:")) return;
      if (field.startsWith("Assigned Station:")) {
        field = field.substring(17).trim();
        int pt = field.indexOf(' ');
        if (pt >= 0) field = field.substring(0,pt);
        if (srcSet.add(field)) data.strSource = append(data.strSource, ",", field);
      } else {
        if (field.startsWith("Unit:")) {
          String unit = field.substring(5).trim();
          data.strUnit = append(data.strUnit, ",", unit);
          times = append(times, "\n", field);
        } else {
          times = append(times, "\n  ", field);
          if (field.startsWith("CLEARED:")) {
            data.msgType = MsgType.RUN_REPORT;
          } else if (field.startsWith("DISPATCHED:")) {
            data.msgType = MsgType.PAGE;
          }
        }
      }
    }

    @Override
    public String getFieldNames() {
      return "SRC UNIT INFO";
    }
  }
}
