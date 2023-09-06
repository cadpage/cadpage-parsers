package net.anei.cadpage.parsers.PA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class PAFultonCountyParser extends FieldProgramParser {

  public PAFultonCountyParser() {
    super("FULTON COUNTY", "PA",
          "Units:UNIT! UNIT? CALL! Location:EMPTY! ADDRCITY! Narrative:INFO! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "alerts@centre.ealert911.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals(".")) return false;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    return super.getField(name);
  }

  private class MyUnitField extends UnitField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!data.strUnit.isEmpty()) return false;
      data.strUnit = field;
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private static final Pattern ADDR_GPS_X_PTN = Pattern.compile("(.*?) ([-+]?\\d{2,3}\\.\\d{6,} +[-+]?\\d{2,3}\\.\\d{6,}|-361 +-361) *(.*)");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_GPS_X_PTN.matcher(field);
      if (!match.matches()) abort();
      super.parse(match.group(1).trim(), data);
      data.strCity = stripFieldEnd(data.strCity, " BORO");
      setGPSLoc(match.group(2), data);
      data.strCross = match.group(3);
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT CITY GPS X";
    }
  }
}
