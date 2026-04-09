package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class PAFultonCountyParser extends FieldProgramParser {

  public PAFultonCountyParser() {
    super("FULTON COUNTY", "PA",
          "Number:ID!  Date/Time:DATETIME! Units:EMPTY! UNIT! CALL! Location:EMPTY! ( SELECT/1 ADDRCITYST1! PLACE! X! | ADDRCITYST2! ) Narrative:INFO! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "alerts@centre.ealert911.com,dispatch@mg.sac911.net";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern DELIM = Pattern.compile("\n| +(?=Date/Time:|Narrative:)");
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (subject.startsWith("| ")) {
      setSelectValue("1");
      data.strSource = subject.substring(2).trim();
    } else if (subject.equals(".")) {
      setSelectValue("2");
    } else {
      return false;
    }
    return parseFields(DELIM.split(body), data);
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("ADDRCITYST1")) return new MyAddressCityStateField(false);
    if (name.equals("ADDRCITYST2")) return new MyAddressCityStateField(true);
    return super.getField(name);
  }

  private static final Pattern ADDR_GPS_X_PTN = Pattern.compile("(.*?) ([-+]?\\d{2,3}\\.\\d{6,} +[-+]?\\d{2,3}\\.\\d{6,}|-361 +-361) *(.*)");
  private class MyAddressCityStateField extends AddressCityStateField {

    private boolean includeCross;

    public MyAddressCityStateField(boolean includeCross) {
      this.includeCross = includeCross;
    }

    @Override
    public void parse(String field, Data data) {
      if (field.equals("<UNKNOWN>")) {
        data.strAddress = field;
        return;
      }

      Matcher match = ADDR_GPS_X_PTN.matcher(field);
      if (!match.matches()) abort();
      String cross = match.group(3);
      if (includeCross) {
        data.strCross = cross;
      } else {
        if (!cross.isEmpty()) abort();
      }
      super.parse(match.group(1).trim(), data);
      data.strCity = stripFieldEnd(data.strCity, " BORO");
      setGPSLoc(match.group(2), data);
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT CITY ST GPS X?";
    }
  }
}
