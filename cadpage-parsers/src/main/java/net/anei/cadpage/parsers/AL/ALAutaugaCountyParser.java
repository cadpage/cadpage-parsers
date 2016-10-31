package net.anei.cadpage.parsers.AL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ALAutaugaCountyParser extends FieldProgramParser {
  
  public ALAutaugaCountyParser() {
    super(CITY_CODES, "AUTAUGA COUNTY", "AL", 
          "CALL ADDRCITY SRC UNIT! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "page@prattvilleal.gov";
  }
  
  private static final Pattern MARKER = Pattern.compile("sdspage2732778 +METC\\d+\n");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = MARKER.matcher(body);
    if (!match.lookingAt()) return false;
    body = body.substring(match.end()).trim();
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("SRC")) return new SourceField("\\d{3,4}", true);
    if (name.equals("UNIT")) return new UnitField("[A-Z0-9]+", true);
    return super.getField(name);
  }
  
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strCity = convertCodes(p.getLastOptional(','), CITY_CODES);
      while (true) {
        String place = p.getLastOptional(';');
        if (place.length() == 0) break;
        data.strPlace = append(place, " - ", data.strPlace);
      }
      parseAddress(p.get(), data);
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "AVL", "AUTAUGAVILLE",
      "BIL", "BILLINGSLEY",
      "DEA", "DEATSVILLE",
      "MAR", "MARBURY",
      "PRA", "PRATTVILLE",
      "VER", "VERBENA"
  });

}
