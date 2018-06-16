package net.anei.cadpage.parsers.VA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;


public class VAAccomackCountyParser extends DispatchOSSIParser {
  
  public VAAccomackCountyParser() {
    this("ACCOMACK COUNTY");
  }
  
  public VAAccomackCountyParser(String county) {
    super(CITY_CODES, county, "VA",
           "FYI? CALL ADDR! ( CITYST | CITY ( MAP MAP/C | ) | ) ( X/Z X/Z PLACE ID | X/Z X_PLACE/Z ID | X X? PLACE? ID | PLACE? ID ) END");
    setupSpecialStreets("WILDLIFE LOOP ACCESS");
  }
  
  @Override
  public String getAliasCode() {
    return "VAAccomackCounty";
  }
  
  @Override
  public String getFilter() {
    return "cad@esva911.org,14100,2159700551";
  }
  
  private static final Pattern OPT_MARKER = Pattern.compile("ESVA911:? +");

  @Override
  protected boolean parseMsg(String body, Data data) {
    boolean suspect = true;
    Matcher match = OPT_MARKER.matcher(body);
    if (match.lookingAt()) {
      suspect = false;
      body = body.substring(match.end());
    }
    
    if (body.startsWith("CAD:")) {
      suspect = false;
    } else {
      body = "CAD:" + body;
    }
    
    if (!super.parseMsg(body, data)) return false;
    return (!suspect || data.strCity.length() > 0 || data.strCallId.length() > 0);
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("CITYST")) return new CityStField();
    if (name.equals("MAP")) return new MapField("[A-Z]\\d+(?:-[A-Z]?)?", true);
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("X_PLACE")) return new MyCrossPlaceField();
    if (name.equals("ID")) return new IdField("\\d{8,}", true);
    return super.getField(name);
  }

  // We need a special field parser to handle the CITYST field
  private class CityStField extends Field {
    
    public CityStField() {
      super("[A-Z ]+ (?:DE|MD|VA)", true);
    }

    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strCity = p.get(' ');
      data.strState = p.get();
    }
    
    @Override
    public String getFieldNames() {
      return "CITY ST";
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (super.checkParse(field,  data)) return true;
      if (field.equals("UNNAMED")) {
        super.parse(field,  data);;
        return true;
      }
      return false;
    }
  }
  
  private class MyCrossPlaceField extends MyCrossField {
    
    @Override
    public boolean canFail() {
      return false;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (isValidCrossStreet(field)) {
        super.parse(field, data);
      } else {
        data.strPlace = field;
      }
    }
    
    @Override
    public String getFieldNames() {
      return "X PLACE";
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ACCO", "ACCOMAC",
      "ASSA", "ASSAWOMAN",
      "ATLA", "ATLANTIC",
      "BEHA", "BELLE HAVEN",
      "BIRD", "BIRDSNEST",
      "BLOX", "BLOXOM",
      "CACH", "CAPE CHARLES",
      "CAPE", "CAPE CHARLES",
      "CHER", "CHERITON",
      "CHIN", "CHINCOTEAGUE",
      "CRAD", "CRADDOCKVILLE",
      "EAST", "EASTVILLE",
      "EXMO", "EXMORE",
      "FRAN", "FRANKTOWN",
      "GRBK", "GREENBACKVILLE",
      "GRBS", "GREENBUSH",
      "HALL", "HALLWOOD",
      "HARB", "HARBORTON",
      "HORN", "HORNTOWN",
      "JAME", "JAMESVILLE",
      "KELL", "KELLER",
      "LOCU", "LOCUSTVILLE",
      "MACH", "MACHIPONGO",
      "MAPP", "MAPPSVILLE",
      "MARI", "MARIONVILLE",
      "MEAR", "MEARS",
      "MELF", "MELFA",
      "NASS", "NASSAWADOX",
      "NELS", "NELSONIA",
      "NEWC", "NEW CHURCH",
      "OAKH", "OAK HALL",
      "ONAN", "ONANCOCK",
      "ONLY", "ONLEY",
      "PAIN", "PAINTER",
      "PARK", "PARKSLEY",
      "PUNG", "PUNGOTEAGUE",
      "QUIN", "QUINBY",
      "SANF", "SANFORD",
      "SAXI", "SAXIS",
      "TANG", "TANGIER",
      "TASL", "TASLEY",
      "TEMP", "TEMPERANCEVILLE",
      "TOWN", "TOWNSEND",
      "WACH", "WACHAPREAGUE",
      "WARD", "WARDTOWN",
      "WITH", "WOTHAMS",
      "WIWH", "WILLIS WHARF",
      "WLIS", "WALLOPS ISLAND"

  });
}
