package net.anei.cadpage.parsers.LA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Ouachita, LA
 */
public class LAOuachitaParishParser extends FieldProgramParser {
  public LAOuachitaParishParser() {
    super(CITY_LIST, "OUACHITA PARISH", "LA",
          "CALL_UNIT ADDRCITY X! Narrative:INFO/N+");
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch")) return false;
    return parseFields(body.split("\n"), 3, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL_UNIT")) return new MyCallUnitField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }

  private static final Pattern CALL_UNIT_PTN = Pattern.compile("(.*) Units: (.*)");
  private class MyCallUnitField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_UNIT_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCall = match.group(1).trim();
      data.strUnit = match.group(2).trim();
    }

    @Override
    public String getFieldNames() {
      return "CALL UNIT";
    }
  }
  
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      if (!field.startsWith("at ")) abort();
      field = field.substring(3).trim().replace('@', '&');
      int pt = field.indexOf(',');
      if (pt >= 0) {
        parseAddress(field.substring(0,pt).trim(), data);
        parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, field.substring(pt+1).trim(), data);
        data.strPlace = getLeft();
      } else {
        parseAddress(StartType.START_ADDR, field, data);
        data.strPlace = getLeft();
      }
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT CITY PLACE";
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("Searching Cross Streets...")) return;
      super.parse(field, data);
    }
  }
  
  private static final String[] CITY_LIST = {
    "BROWNSVILLE-BAWCDOMVILLE",
    "CALHOUN",
    "CLAIBORNE",
    "EROS",
    "MONROE",
    "RICHWOOD",
    "STERLINGTON",
    "SWARTZ",
    "WEST MONROE"
  };
}
