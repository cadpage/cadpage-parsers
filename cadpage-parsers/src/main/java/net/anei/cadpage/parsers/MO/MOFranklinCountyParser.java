package net.anei.cadpage.parsers.MO;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;


public class MOFranklinCountyParser extends FieldProgramParser {
  public MOFranklinCountyParser() {
    super("FRANKLIN COUNTY", "MO",
          "Location:ADDRCITYST! Intersection:X! Business_Name:PLACE! APT/SUITE:APT! " +
            "( MapPageGrid:MAP! MapCoordinates:MAP/L! LocationDescription:INFO! Subdivision:LINFO/N! Latitude:GPS1! Longitude:GPS2! | ) " +
            "CALL:DATE_TIME_ID! Response_Type:CALL! UNITS/REPORTS_ASSIGNED:EMPTY! TIMES/N+ CAD_INFORMATION:EMPTY INFO/N+");
  }

  @Override
  public String getFilter() {
    return "franklincodispatch@franklinmo.net";
  }

  private String times;
  private Set<String> unitSet = new HashSet<>();

  private static final Pattern TAG_PTN = Pattern.compile("\n<([ /A-Za-z]+: *)>");

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {

    times = "";
    unitSet.clear();
    body = TAG_PTN.matcher(body).replaceAll("\n$1");
    if (!parseFields(body.split("\n"), data)) return false;
    if (data.msgType == MsgType.RUN_REPORT) data.strSupp = append(times, "\n\n", data.strSupp);
    times = null;
    unitSet.clear();
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITYST")) return new MyAddressCityStateField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("DATE_TIME_ID")) return new MyDateTimeIdField();
    if (name.equals("TIMES")) return new MyTimesField();
    return super.getField(name);
  }

  private static final Pattern MSPACE_PTN = Pattern.compile(" {2,}");
  private static final Pattern CITY_STATE_ZIP_PTN = Pattern.compile("(.*?)(?: *\\bMO)?(?: +\\d{5})?");
  private class MyAddressCityStateField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strCross = p.getLastOptional(", Cross Street of");
      p.getLastOptional("Appt or Suite");
      field = MSPACE_PTN.matcher(p.get()).replaceAll(" ");
      super.parse(field, data);
      Matcher match = CITY_STATE_ZIP_PTN.matcher(data.strCity);
      if (match.matches()) data.strCity = match.group(1);
      if (data.strCity.equals("- 0 MO 0")) data.strCity = "";
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.isEmpty()) return;
      data.strCross = "";
      super.parse(field, data);
    }
  }

  private static final Pattern DATE_TIME_ID_PTN = Pattern.compile("(\\d\\d/\\d\\d/\\d{4}) @ (\\d\\d:\\d\\d:\\d\\d) FC911 USE ONLY:(\\d{4}-\\d{8}|)");
  private class MyDateTimeIdField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_ID_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      data.strTime = match.group(2);
      data.strCallId = match.group(3);
    }

    @Override
    public String getFieldNames() {
      return "DATE TIME ID";
    }
  }

  private static final Pattern TIMES_PTN = Pattern.compile("--  (\\S*)  -- (.*?)<T>\\d\\d:\\d\\d:\\d\\d");
  private class MyTimesField extends Field {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ";");
      Matcher match = TIMES_PTN.matcher(field);
      if (match.matches()) {
        String unit = match.group(1);
        if (unitSet.add(unit)) data.strUnit = append(data.strUnit, ",", unit);
        String status = match.group(2);
        if (status.equals("IN SERVICE")) data.msgType = MsgType.RUN_REPORT;
      }
      times = append(times, "\n", field);
    }

    @Override
    public String getFieldNames() {
      return "UNIT INFO";
    }
  }
}