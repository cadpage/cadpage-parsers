package net.anei.cadpage.parsers.AL;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ALDothanBParser extends FieldProgramParser {
  
  public ALDothanBParser() {
    super(CITY_LIST, "DOTHAN", "AL", 
          "ADDR/S6XP X EMPTY EMPTY EMPTY TIME CALL PRI! INFO+");
    removeWords("AVENUE", "PLACE", "TERRACE");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@dothan.org,777";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    body = stripFieldStart(body, "CITY OF DOTHAN:");
    body = stripFieldEnd(body, "\nstop");
    return parseFields(body.split(";"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("PRI")) return new PriorityField("\\d", true);
    return super.getField(name);
  }
  
  private static final Pattern ADDR_MAP_PTN = Pattern.compile("(.*) ((?:[NSEW]|[NS][EW]) SECTOR)\\b *(.*)");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_MAP_PTN.matcher(field);
      if (match.matches()) {
        String addr = match.group(1).trim();
        data.strMap = match.group(2);
        String extra = match.group(3);
        
        parseAddress(StartType.START_ADDR, FLAG_RECHECK_APT | FLAG_NO_CITY | FLAG_ANCHOR_END, addr, data);
        parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, extra, data);
        data.strPlace = getLeft();
        
      } else {
        super.parse(field, data);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT MAP CITY PLACE";
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("*", "").replace(" X ", " / ");
      super.parse(field, data);
    }
  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("([A-Z0-9]+)-(.*)");
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_CALL_PTN.matcher(field);
      if (match.matches()) {
        data.strCode = match.group(1);
        field = match.group(2).trim();
      }
      data.strCall = field;
    }
    
    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
  
  @Override
  public boolean isNotExtraApt(String apt) {
    if (apt.toUpperCase().startsWith("BLOCK")) return true;
    return super.isNotExtraApt(apt);
  }
  
  @Override
  public String adjustMapCity(String city) {
    return convertCodes(city, CITY_MAP_TABLE);
  }
  
  private static final Properties CITY_MAP_TABLE = buildCodeTable(new String[]{
      "BAY SPRINGS",        "DOTHAN",
      "BAYSPRINGS",         "DOTHAN",
      "LOVETOWN",           "DOTHAN",
      "SOUTHERN JUNCTION",  "DOTHAN"
  });
  
  private static final String[] CITY_LIST = new String[]{
    "ARDILLA",
    "ASHFORD",
    "AVON",
    "BARBER",
    "BAY SPRINGS",
    "BAYSPRINGS",
    "BIG CREEK",
    "BRANNON STAND",
    "COLUMBIA",
    "COTTONWOOD",
    "COWARTS",
    "CROSBY",
    "DOTHAN",
    "DUPREE",
    "ENON",
    "GARRETTS CROSSROADS",
    "GORDON",
    "GRANGEBURG",
    "HARMON",
    "HODGESVILLE",
    "KEYTONS",
    "KINSEY",
    "LOVE HILL",
    "LOVETOWN",
    "LUCY",
    "MADRID",
    "MERRITTS CROSSROADS",
    "PANSEY",
    "REHOBETH",
    "SOUTHERN JUNCTION",
    "TAYLOR",
    "WEBB",
    "WICKSBURG"
  };
}
