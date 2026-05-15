package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class PANorthamptonCountyBParser extends FieldProgramParser {

  public PANorthamptonCountyBParser() {
    super("NORTHAMPTON COUNTY", "PA",
          "( SELECT/2 CODE_CALL! Address:ADDRCITY/S6! PLACE! APT! XSt:X! Caller:NAME_PHONE! EMPTY! INFO/N+ Assigned_Units:UNIT! " +
                "Radio_Channel:CH! GPS1! GPS2! Fire_Response_Area:MAP! EMS_Response_Area:MAP_DATE_TIME! Alarm_Level:PRI! " +
          "| UNIT_CALL_ADDR_CITY/S6! ( XST:X! CALLER:NAME! CASE:ID! NARR:INFO! | Cross_Streets:X! Caller:NAME! Case:ID! ) " +
          ") END");
    setupSpecialStreets("BROADWAY", "RAMP");
    addCrossStreetNames("ALLEY ALY", "UNNAMED");
    removeWords("NEW", "PARK");
  }

  @Override
  public String getFilter() {
    return "no-reply@onsolve.com,noReply@notify.onsolve.net,76993";
  }

  private static final Pattern DELIM = Pattern.compile(" \\|\n? ");

  @Override
  protected boolean parseMsg(String body, Data data) {
    int pt = body.indexOf("\n<em>");
    if (pt >=  0) body = body.substring(0,pt).trim();

    String[] flds = DELIM.split(body);
    if (flds.length >= 6) {
      setSelectValue("2");
      return parseFields(flds, data);
    } else {
      setSelectValue("1");
      return super.parseMsg(body, data);
    }
  }

  @Override
  public Field getField(String name) {
    if (name.equals("UNIT_CALL_ADDR_CITY"))  return new MyUnitCallAddressCityField();
    if (name.equals("CODE_CALL")) return new  MyCodeCallField();
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("NAME")) return new MyNameField();
    if (name.equals("NAME_PHONE")) return new MyNamePhoneField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("MAP_DATE_TIME")) return new MyMapDateTimeField();
    return super.getField(name);
  }

  private static final Pattern UNIT_CALL_ADDR_PTN = Pattern.compile("([A-Z]+\\d+[A-Z]?) (.*?) - (.*)");
  private class MyUnitCallAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf('\n');
      if (pt >= 0) field = field.substring(pt+1).trim();
      Matcher match = UNIT_CALL_ADDR_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strUnit = match.group(1);
      data.strCall = match.group(2).trim();
      super.parse(match.group(3).trim(), data);
      data.strAddress = stripFieldEnd(data.strAddress, ",");
    }

    @Override
    public String getFieldNames() {
      return "UNIT CALL " + super.getFieldNames();
    }
  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("([A-Z]+\\d+[A-Z]?) +(.*?)");

  private class MyCodeCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_CALL_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCode = match.group(1);
      data.strCall = match.group(2);
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private static final Pattern APT_PTN = Pattern.compile("(?:APT|ROOM|RM|LOT|STE) *(.*)", Pattern.CASE_INSENSITIVE);

  private class MyAptField extends AptField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = APT_PTN.matcher(field);
      if (match.matches()) field = match.group(1);
      super.parse(field, data);
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, ",");
      field = stripFieldEnd(field, ",");
      field = field.replace('@',  '/');

      if (field.startsWith("No Cross Streets Found")) {
        data.strPlace = field.substring(22).trim();
      }

      else {
        String prefix = null;
        int pt = field.lastIndexOf(',');
        if (pt >= 0) {
          prefix = field.substring(0,pt);
          field = field.substring(pt+1);
        }
        parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS | FLAG_EMPTY_ADDR_OK, field, data);
        if (!isValidAddress()) {
          if (!data.strCross.equals(data.strUnit)) data.strPlace = data.strCross;
          data.strCross = "";
        }
        if (prefix != null) data.strCross = append(prefix, ", ", data.strCross);
        String place = getLeft();
        if (place.equals(data.strUnit)) place = "";
        data.strPlace = append(data.strPlace, " ", place);

        if (data.strPlace.equals("LUKE'S NORTH") && data.strCross.endsWith(" ST")) {
          data.strPlace = "ST " + data.strPlace;
          data.strCross = data.strCross.substring(0, data.strCross.length()-3).trim();
        }
      }
      data.strPlace = stripFieldStart(data.strPlace, "CN:");
    }

    @Override
    public String getFieldNames() {
      return "X PLACE";
    }
  }

  private class MyNameField extends NameField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, ",");
      field = stripFieldEnd(field, ",");
      super.parse(field, data);
    }
  }

  private static final Pattern NAME_PHONE_PTN = Pattern.compile("(.*?)\\b *(\\d{10})");
  private class MyNamePhoneField extends NameField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = NAME_PHONE_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        data.strPhone = match.group(2);
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "NAME PHONE";
    }
  }

  private static final Pattern INFO_PFX_PTN = Pattern.compile("\\d\\d/\\d\\d/\\d{4} \\d{4} +");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      for (String line : field.split("\n")) {
        line = line.trim();
        Matcher match = INFO_PFX_PTN.matcher(line);
        if (match.lookingAt()) line = line.substring(match.end());
        data.strSupp = append(data.strSupp, "\n", line);
      }
    }
  }

  private static final Pattern MAP_TIME_DATE_PTN = Pattern.compile("(.*?) +(\\d\\d?/\\d\\d?/\\d{4}) +(\\d\\d:\\d\\d:\\d\\d)");

  private class MyMapDateTimeField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = MAP_TIME_DATE_PTN.matcher(field);
      if (!match.matches()) abort();
      String map = match.group(1);
      if (!map.equals(data.strMap)) data.strMap = append(data.strMap, "/", map);
      data.strDate = match.group(2);
      data.strTime = match.group(3);
    }

    @Override
    public String getFieldNames() {
      return "MAP DATE TIME";
    }
  }
}