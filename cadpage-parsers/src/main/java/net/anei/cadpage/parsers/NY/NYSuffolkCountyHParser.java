package net.anei.cadpage.parsers.NY;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class NYSuffolkCountyHParser extends FieldProgramParser {

  public NYSuffolkCountyHParser() {
    super(CITY_LIST, "SUFFOLK COUNTY", "NY",
          "DATETIME CALL ADDR PLACE INFO/N+");
    setupSpecialStreets(
        "BEACHWAY",
        "CAPTAINS WALK",
        "EDWARDS CLOSE",
        "GLENWAY",
        "GREENWAY",
        "HIGHWOOD",
        "QUALITY ROW",
        "SHORIDGE",
        "WATERS EDGE",
        "WATERSEDGE",
        "WINDWARD"
    );
  }

  @Override
  public String getFilter() {
    return "@easthamptonvillageny.gov";
  }

  private static final Pattern MASTER = Pattern.compile("(\\d\\d/\\d\\d/\\d{4}) (\\d\\d:\\d\\d) (.*?) (AM|EH|MO|SP) (.*)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CALL")) return false;

    // Strip off html header
    if (body.startsWith("<meta ")) {
      int pt = body.indexOf('>');
      if (pt < 0) return false;
      body = body.substring(pt+1).trim();
    }

    // New line break delimited format
    String[] flds = body.split("\n");
    if (flds.length >= 4) {
      if (!parseFields(flds, data)) return false;
    }

    else {
      Matcher match = MASTER.matcher(body);
      if (!match.matches()) return false;

      setFieldList("DATE TIME CALL CITY ADDR APT PLACE INFO");
      data.strDate = match.group(1);
      data.strTime = match.group(2);
      data.strCall = match.group(3).trim();
      String code = match.group(4);
      String addr = match.group(5).trim();
      parseAddress(StartType.START_ADDR, FLAG_CROSS_FOLLOWS | FLAG_IGNORE_AT, addr, data);
      data.strSupp = getLeft();
      data.strPlace = data.strCity;
      data.strCity = convertCodes(code, CITY_CODES);
    }

    if (data.strPlace.equals(data.strCity)) data.strPlace = "";
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d", true);
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }

  private static final Pattern ADDR_PTN = Pattern.compile("([A-Z]{2,3}) +(.*)");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCity = convertCodes(match.group(1), CITY_CODES);
      super.parse(match.group(2), data);
    }

    @Override
    public String getFieldNames(){
      return "CITY " + super.getFieldNames();
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "AM",  "AMAGANSETT",
      "BR",  "WAINSCOTT",   // ???
      "EH",  "EAST HAMPTON",
      "EHV", "EAST HAMPTON",
      "EVH", "EAST HAMPTON",
      "MO",  "MONTAUK",
      "SH",  "SAG HARBOR",
      "SHV", "SAG HARBOR",
      "SP",  "SPRINGS AMGANSETT",
      "OT",  "HAMPTON BAYS"   // ???
  });

  private static final String[] CITY_LIST = new String[]{
    "AMAGANSETT",
    "EAST HAMPTON",
    "BARNES LANDING BERGER",
    "NAPEAGUE",
    "SPRINGS AMAM"
  };
}
