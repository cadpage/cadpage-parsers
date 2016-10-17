package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NCMartinCountyBParser extends FieldProgramParser {
  
  public NCMartinCountyBParser() {
    super(CITY_LIST, "MARTIN COUNTY", "NC", 
          "ADDR/S6XP CODE CODE EMPTY ID TIME CALL! END");
    removeWords("SQUARE", "TERRACE");
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!body.startsWith("dispatch:")) return false;
    body = body.substring(9).trim();
    return parseFields(body.split(";", -1), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CODE")) return new MyCodeField();
    if (name.equals("ID")) return new IdField("\\d{6}-\\d{5}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
  
  private static final Pattern EXPRESSWAY_PTN = Pattern.compile(" *\\bEXPRESSWAY\\b *", Pattern.CASE_INSENSITIVE);
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = EXPRESSWAY_PTN.matcher(field).replaceAll(" ").trim();
      super.parse(field, data);
    }
  }
  
  private Pattern CODE_PTN = Pattern.compile("(?:MDL|FDL) +(.*)");
  private class MyCodeField extends CodeField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      Matcher match = CODE_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCode = match.group(1);
    }
  }
  
  @Override
  public boolean isNotExtraApt(String apt) {
    if (apt.startsWith("(")) return true;
    return super.isNotExtraApt(apt);
  }
  
  private static final String[] CITY_LIST = new String[]{
    
    // Towns
    "BEAR GRASS",
    "EVERETTS",
    "HAMILTON",
    "HASSELL",
    "JAMESVILLE",
    "OAK CITY",
    "PARMELE",
    "ROBERSONVILLE",
    "WILLIAMSTON",

    // Townships
    "BEAR GRASS TWP",
    "CROSS ROADS TWP",
    "GOOSE NEST TWP",
    "GRIFFINS TWP",
    "HAMILTON TWP",
    "JAMESVILLE TWP",
    "POPLAR POINT TWP",
    "ROBERSONVILLE TWP",
    "WILLIAMS TWP",
    "WILLIAMSTON TWP",
    
    // Halifax County
    "HOBGOOD",
    
    // Pitt County
    "BETHEL",
    
    // Washington County
    "PLYMOUTH"
  };
}
