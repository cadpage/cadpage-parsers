package net.anei.cadpage.parsers.NC;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;



public class NCCurrituckCountyParser extends DispatchOSSIParser {
  
  private static final Pattern B_AND_B_PTN = Pattern.compile("\\bB AND B\\b");
  
  public NCCurrituckCountyParser() {
    super(CITY_CODES, "CURRITUCK COUNTY", "NC",
          "( CANCEL ADDR CITY? PLACE? " +
          "| F6? CALL ADDR/S! CITY? UNIT/C? X+? ( PLACENAME END | PLACENAME PHONE | ) " + 
          ") INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "CAD@co.currituck.nc.us,CAD@currituckcountync.gov";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    body = B_AND_B_PTN.matcher(body).replaceAll("B B");
    return super.parseMsg(body, data);
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("F6")) return new SkipField("F6");
    if (name.equals("PLACE")) return new PlaceField("\\(S\\) *(.*?) *\\(N\\)", true);
    if (name.equals("UNIT")) return new UnitField("[A-Z]\\d[A-Z]", true);
    if (name.equals("PLACENAME")) return new MyPlaceNameField();
    if (name.equals("PHONE")) return new PhoneField("\\d{10}");
    return super.getField(name);
  }
  
  private class MyPlaceNameField extends PlaceNameField {
    @Override
    public void parse(String field, Data data) {
      field = cleanWirelessCarrier(field);
      if (field.startsWith("(S)") && field.endsWith("(N)")) {
        data.strPlace = field.substring(3,field.length()-3).trim();
      } else {
        data.strName = field;
      }
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "CNJC", "COINJOCK",
      "CRLA", "COROLLA",
      "CURR",  "CURRITUCK",
      "DUCK", "DUCK",
      "GNDY", "GRANDY",
      "JVBG", "JARVISBURG",
      "MOYC", "MOYOCK",
      "PTHR", "POINT HARBOR"
  });
}
