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

  private static final Pattern PREFIX_PTN = Pattern.compile("^[: ]*(?:Nearest:|)[ :@]*");
  private static final Pattern ADDR_APT_PTN = Pattern.compile("(.*): (\\S+)");

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      
      // Check for GPS coordinates
      field = PREFIX_PTN.matcher(field).replaceFirst("");
      if (GPS_PATTERN.matcher(field).matches()) {
        data.strAddress = field;
        return;
      }
      
      String apt = "";
      Matcher match = ADDR_APT_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        apt = match.group(2);
      }

      String city = null;
      int pt = field.indexOf(',');
      if (pt >= 0) {
        city = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
        pt = city.indexOf("  ");
        if (pt >= 0) city = city.substring(0,pt).trim();
      }
      parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, field, data);
      if (data.strCity.length() > 0) {
        data.strAddress = stripFieldEnd(data.strAddress, data.strCity);
      } else if (city != null) {
        pt = city.indexOf(',');
        if (pt >= 0) city = city.substring(0,pt).trim();
        data.strCity = city;
      }
      pt = data.strCity.indexOf(" RURAL");
      if (pt >= 0) data.strCity = data.strCity.substring(0,pt).trim();
      if (data.strCity.equals("TABOR TABOR")) data.strCity = "TABOR";
      
      if (apt.length() == 0) {
        match = ADDR_APT_PTN.matcher(data.strAddress);
        if (match.matches()) {
          data.strAddress = match.group(1).trim();
          apt = match.group(2);
        }
      }
      data.strApt = append(data.strApt, "-", apt);
    }
  }
  
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "-");
      super.parse(field, data);
    }
  }
  
  private static final Pattern RNG_RD_PTN = Pattern.compile("\\bRNG RD\\b", Pattern.CASE_INSENSITIVE);
  
  @Override
  public String adjustMapAddress(String addr) {
    return RNG_RD_PTN.matcher(addr).replaceAll("RANGE RD");
  }
  
  private static final String[] CITY_LIST = new String[]{

    //Towns
    "TABER",
    "TABER RURAL",
    "VAUXHALL",

    // Villages
    "BARNWELL",

    // Hamlets
    "ENCHANT",
    "GRASSY LAKE",
    "GRASSY LAKE RURAL",
    "HAYS",
    "HAYS RURAL",
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
