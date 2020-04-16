package net.anei.cadpage.parsers.ZCAAB;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ZCAABTaberParser extends FieldProgramParser {
  
  public ZCAABTaberParser() {
    super(CITY_LIST, "TABER", "AB", 
          "Agency:SRC! Event:ID! Location:ADDRCITY! Event_Type:CALL! Event_SubType:CALL/SDS! Complainant_Name:NAME! Complainant_Phone:PHONE! END");
  }
  
  @Override
  public String getFilter() {
    return "inetmaint@taber.ca";
  }
  
  private static final Pattern MARKER = Pattern.compile(">>> .*? NOTIFICATION <<<\n=========================================\n");;
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Create Event")) return false;
    Matcher match = MARKER.matcher(body);
    if (!match.lookingAt()) return false;
    body = body.substring(match.end()).trim();
    int pt = body.indexOf("\n\n---");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("CALL")) return new MyCallField();
    return super.getField(name);
  }
  
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      
      // Check for GPS coordinates
      if (field.startsWith(":@:")) { 
        data.strAddress = field.substring(3).trim();
        return;
      }
      
      field = stripFieldStart(field, "::Nearest:");
      field = stripFieldStart(field, "@");
      String city = null;
      int pt = field.indexOf(',');
      if (pt >= 0) {
        city = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
      }
      parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, field, data);
      if (data.strCity.length() == 0 && city != null) {
        pt = city.indexOf(',');
        if (pt >= 0) city = city.substring(0,pt).trim();
        data.strCity = city;
      }
      
    }
  }
  
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "-");
      super.parse(field, data);
    }
  }
  
  private static final String[] CITY_LIST = new String[]{

    //Towns
    "TABER",
    "VAUXHALL",

    // Villages
    "BARNWELL",

    // Hamlets
    "ENCHANT",
    "GRASSY LAKE",
    "HAYS",
    "JOHNSONS ADDITION",
    "PURPLE SPRINGS",

    // Localities
    "ANTONIO",
    "ARMELGRA",
    "BARNEY",
    "CRANFORD",
    "ELCAN",
    "FINCASTLE",
    "GRANTHAM",
    "RETLAW",
    "SCOPE",

    // Other places
    "AGRICULTURE RESEARCH",
    "ALBION RIDGE",
    "ASKOW",
    "BARONS",
    "BROXBURN",
    "CHIN",
    "COALDALE",
    "COALHURST",
    "COMMERCE",
    "DIAMOND CITY",
    "EASTVIEW ACRES",
    "FAIRVIEW",
    "GHENT",
    "IRON SPRINGS",
    "KIPP",
    "LENZIE",
    "LETHBRIDGE",
    "MCDERMOTT",
    "MCDERMOTT SUBDIVISION",
    "MONARCH",
    "NOBLEFORD",
    "PICTURE BUTTE",
    "PIYAMI",
    "SHAUGHNESSY",
    "STEWART",
    "STEWART SIDING",
    "SUNSET ACRES",
    "TEMPEST",
    "TENNION",
    "TURIN",
    "WESTVIEW ACRES",
    "WHITNEY",
    "WILSON",
    "WILSON SIDING"
  };
}
