package net.anei.cadpage.parsers.MO;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class MOJeffersonCountyBParser extends FieldProgramParser {

  public MOJeffersonCountyBParser() {
    super(CITY_CODES, "JEFFERSON COUNTY", "MO",
          "ADDRCITY PLACE CODE_CALL! Cross_Streets:X? http:GPS! Comments:EMPTY! INFO+? EMPTY! UNIT! END");
    removeWords("LOT");
  }

  @Override
  public String getFilter() {
    return "911@jeffco911.org";
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("CAD Page for CFS ([A-Z]*\\d+-\\d+)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (body.startsWith("---------")) return parseRunReport(body, data);

    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strCallId = match.group(1);
    if (!parseFields(body.split("\n"), data)) return false;
    data.strAddress = stripFieldEnd(data.strAddress, ":");
    return true;
  }

  private static final Pattern UNIT_LINE_PTN = Pattern.compile("(\\d{4}) +[A-Z]+ +.*");

  private boolean parseRunReport(String body, Data data) {
    data.msgType = MsgType.RUN_REPORT;
    data.strSupp = body;
    setFieldList("ID ADDR APT PLACE CODE CALL UNIT INFO");

    boolean unitList = false;
    Set<String> unitSet = new HashSet<>();


    for (String line : body.split("\n")) {
      int pt = line.indexOf(" : ");
      if (pt >= 0) {
        String key = line.substring(0,pt).trim();
        String val = line.substring(pt+3).trim();
        switch (key) {

        case "Event Number":
        case "INCIDENT NUMBER":
          data.strCallId = val;
          break;

        case "Location":
        case "LOCATION":
          parseAddress(val, data);
          break;

        case "INCIDENT CODE":
          data.strCode = val;
          break;

        case "Event Type":
        case "INCIDENT DESC":
          data.strCall = val;
          break;

        case "BUSINESS":
          data.strPlace = val;
          break;
        }
      }

      else {
        if (!unitList) {
          if (line.startsWith("UNIT  ")) unitList = true;
        } else {
          if (line.startsWith("------")) continue;
          Matcher match = UNIT_LINE_PTN.matcher(line);
          if (match.matches()) {
            String unit = match.group(1);
            if (unitSet.add(unit)) data.strUnit = append(data.strUnit, " ", unit);
          } else {
            unitList = false;
          }
        }
      }
    }

    return data.strCallId.length() > 0 && data.strAddress.length() > 0 && data.strCall.length() > 0;
  }

  @Override
  public String getProgram() {
    return "ID " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CODE_CALL")) return new MyCodeCallField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("GPS")) return new MyGPSField();
    return super.getField(name);
  }

  private class MyCodeCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      if (field.equals(":")) return;
      int pt = field.indexOf(" : ");
      if (pt < 0) abort();
      data.strCode = field.substring(0,pt).trim();
      data.strCall = field.substring(pt+3).trim();
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('*',  '/');
      super.parse(field, data);
    }
  }

  private static final Pattern GPS_PTN = Pattern.compile(".*q=([-+]?\\d+\\.\\d+)%2C([-+]?\\d+\\.\\d+)");
  private class MyGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = GPS_PTN.matcher(field);
      if (!match.matches()) return;
      super.parse(match.group(1)+"0,"+match.group(2)+'0', data);
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BLOOMS",        "BLOOMSDALE",
      "CEDARHILL",     "CEDAR HILL",
      "CRYSTALCT",     "CRYSTAL CITY",
      "HERCU",         "HERCULANEUM",
      "HIGHRIDGE",     "HIGH RIDGE",
      "HOUSESPRG",     "HOUSE SPRINGS",
      "SFCO",          "ST FRANCOIS COUNTY",
      "ROBERTSV",      "ROBERTSVILLE",
      "VALLESMIN",     "VALLES MINES"
  });

}
