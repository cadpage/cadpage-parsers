package net.anei.cadpage.parsers.OR;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class ORLaneCountyAParser extends DispatchOSSIParser {
  
  public ORLaneCountyAParser() {
    super(CITY_CODES, "LANE COUNTY", "OR",
          "( STATUS ADDR CITY! | UNIT CH? CALL CH? ADDR ( DATETIME! |  CITY? PLACE+? MAP! CH? CODE? DATETIME! ) )");
  }
  
  @Override
  public String getFilter() {
    return "CAD@ci.eugene.or.us";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = FINAL_INTL_PTN.matcher(body);
    if (match.find()) body = body.substring(0,match.start());
    return super.parseMsg(body, data);
  }
  private static Pattern FINAL_INTL_PTN = Pattern.compile(" *;[A-Z]{3}$");

  @Override
  public Field getField(String name) {
    if (name.equals("STATUS")) return new CallField("UNDER CONTROL", true);
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("CH")) return new ChannelField("[A-Z]+ ?\\d+", true);
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("MAP")) return new MapField("(?!RM |SP)[A-Z]{1,2}(?:\\d{1,2}| [A-Z0-9])|\\d{4}|\\d{3}[A-Z]|[A-Z]\\d{3}", true);
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
  
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strSource = p.get(',');
      data.strUnit = p.get();
    }
    
    @Override
    public String getFieldNames() {
      return "SRC UNIT";
    }
  }
  
  private static final Pattern PLACE_APT_PTN = Pattern.compile("(?:APT|ROOM|RM|STE)/? *(.*)|(?:LOT|FLR|FLOOR|SP).*|[A-Z0-9]{1,4}");
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = PLACE_APT_PTN.matcher(field);
      if (match.matches()) {
        String apt = match.group(1);
        if (apt == null) apt = field;
        data.strApt = append(data.strApt, "-", apt);
      } else {
        data.strPlace = append(data.strPlace, " - ", field);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "APT PLACE";
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BLA", "BLACHLY",
      "BLU", "BLUE RIVER",
      "CHE", "CHESHIRE",
      "COT", "COTTAGE GROVE",
      "CRE", "CRESWELL",
      "DEA", "DEADWOOD",
      "DEX", "DEXTER",
      "DOR", "DORENA",
      "ELM", "ELMIRA",
      "EUG", "EUGENE",
      "FAL", "FALL CREEK",
      "FLO", "FLORENCE",
      "HAR", "HARRISBURG",
      "JUN", "JUNCTION CITY",
      "LEA", "LEABURG",
      "LOR", "LORANE",
      "LOW", "LOWELL",
      "MAP", "MAPLETON",
      "MAR", "MARCOLA",
      "MON", "MONROE",
      "NOT", "NOTI",
      "OAK", "OAKRIDGE",
      "PLE", "PLEASANT HILL",
      "SPR", "SPRINGFIELD",
      "SWI", "SWISSHOME",
      "VEN", "VENETA",
      "VID", "VIDA",
      "WAN", "WALTON",
      "WEF", "WESTFIR",
      "WES", "WESTLAKE",
      "YAC", "YACHATS"
  });
}
