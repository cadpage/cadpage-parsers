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
           "FYI? CALL ADDR! ( CITYST | CITY MAP MAP ) X X INFO+? ID");
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
    Matcher match = OPT_MARKER.matcher(body);
    if (match.lookingAt()) body = body.substring(match.end());
    return super.parseMsg(body, data);
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("CITYST")) return new CityStField();
    if (name.equals("MAP")) return new MyMapField();
    if (name.equals("ID")) return new IdField("\\d{8,}");
    return super.getField(name);
  }

  // We need a special field parser to handle the CITYST field
  private class CityStField extends Field {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (! field.contains(" ")) return false;
      parse(field, data);
      return true;
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
  
  // And we need a special MAP field that will append two map data fields
  private class MyMapField extends MapField {
    @Override
    public void parse(String field, Data data) {
      data.strMap = append(data.strMap, ",", field);
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
