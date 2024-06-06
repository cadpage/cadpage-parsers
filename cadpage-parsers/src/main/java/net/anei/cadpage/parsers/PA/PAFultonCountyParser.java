package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class PAFultonCountyParser extends FieldProgramParser {

  public PAFultonCountyParser() {
    super("FULTON COUNTY", "PA",
          "Units:EMPTY! UNIT! CALL! Location:EMPTY! ADDRCITYST! X! Narrative:INFO! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "@centrecountypa.gov,@ccpa.net,@bedfordcountypa.org,@centre.ealert911.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("| ")) return false;
    data.strSource = subject.substring(2).trim();
    return parseFields(body.split("\n"), data);
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITYST")) return new MyAddressCityStateField();
    return super.getField(name);
  }

  private static final Pattern ADDR_GPS_X_PTN = Pattern.compile("(.*?) ([-+]?\\d{2,3}\\.\\d{6,} +[-+]?\\d{2,3}\\.\\d{6,}|-361 +-361)");
  private class MyAddressCityStateField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("<UNKNOWN>")) {
        data.strAddress = field;
        return;
      }

      Matcher match = ADDR_GPS_X_PTN.matcher(field);
      if (!match.matches()) abort();
      super.parse(match.group(1).trim(), data);
      data.strCity = stripFieldEnd(data.strCity, " BORO");
      setGPSLoc(match.group(2), data);
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT CITY ST GPS";
    }
  }
}
