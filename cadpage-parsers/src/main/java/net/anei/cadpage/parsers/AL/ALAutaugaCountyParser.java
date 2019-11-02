package net.anei.cadpage.parsers.AL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ALAutaugaCountyParser extends FieldProgramParser {
  
  public ALAutaugaCountyParser() {
    super(CITY_CODES, "AUTAUGA COUNTY", "AL", 
          "SKIP CALL ADDRCITY SRC UNIT! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "@prattvilleal.gov";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    body = stripFieldStart(body, "sdspage2732778 ");
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("SRC")) return new SourceField("\\d{3,4}", true);
    if (name.equals("UNIT")) return new UnitField("[A-Z0-9]+", true);
    return super.getField(name);
  }
  
  private static final Pattern APT_PTN = Pattern.compile("(?:APT|RM|ROOM|LOT|UNIT) +(.*)|(\\d{1,4}[A-Z]?|[A-Z])");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strCity = convertCodes(p.getLastOptional(','), CITY_CODES);
      while (true) {
        String place = p.getLastOptional(';');
        if (place.length() == 0) break;
        Matcher match = APT_PTN.matcher(place);
        if (match.matches()) {
          String apt = match.group(1);
          if (apt == null) apt = match.group(2);
          data.strApt = append(data.strApt, "-", apt);
        } else {
          data.strPlace = append(place, " - ", data.strPlace);
        }
      }
      parseAddress(p.get(), data);
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT PLACE CITY";
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "AVL", "AUTAUGAVILLE",
      "BIL", "BILLINGSLEY",
      "DEA", "DEATSVILLE",
      "IND", "INDEPENDENCE",
      "JON", "JONES",
      "MAR", "MARBURY",
      "PRA", "PRATTVILLE",
      "SEL", "SELMA",
      "STA", "",
      "VER", "VERBENA",
      "WHI", "WHITE CITY"
  });

}
