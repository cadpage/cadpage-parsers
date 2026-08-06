package net.anei.cadpage.parsers.AL;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ALHooverCParser extends FieldProgramParser {

  public ALHooverCParser() {
    super(CITY_CODES, "HOOVER", "AL",
          "CALL:CALL! ADDR:ADDR! CITY:CITY! ID:ID! UNIT:UNIT! INFO:INFO! CROSS:X! LATLONG:GPS! END" );
  }

  @Override
  public String getFilter() {
    return "arns@shelby911.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("Event-")) return false;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CITY")) return new MyCityField();
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(": @");
      if (pt >= 0) {
        data.strPlace = field.substring(pt+3).trim();
        field = field.substring(0,pt).trim();
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }

  private static final Pattern ADDR_CITY_PTN = Pattern.compile("(.*) [A-Z]{4} ([A-Z]{3})");
  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_CITY_PTN.matcher(data.strAddress);
      if (match.matches() && match.group(2).equals(field)) {
        data.strAddress = match.group(1).trim();
      }
      super.parse(field, data);
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "BHM", "BIRMINGHAM"
  });

}
