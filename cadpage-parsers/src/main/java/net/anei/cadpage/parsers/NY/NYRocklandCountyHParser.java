package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NYRocklandCountyHParser extends FieldProgramParser {

  public NYRocklandCountyHParser() {
    super("ROCKLAND COUNTY", "NY",
          "SRC ADDRCITY! CALL_TYPE:CALL! CALLER:PHONE! CAD:ID! GPS! END");
  }

  @Override
  public String getFilter() {
    return "8336651600";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("GPS")) return new GPSField("http://maps\\.google\\.com/maps\\?daddr=(.*)", true);
    return super.getField(name);
  }

  private static final Pattern ADDR_ZIP_PTN = Pattern.compile("(.*) (\\d{5})");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      String zip = null;
      Matcher match = ADDR_ZIP_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        zip = match.group(2);
      }
      super.parse(field, data);
      if (zip != null && data.strCity.isEmpty()) data.strCity = zip;
    }
  }
}
