package net.anei.cadpage.parsers.AL;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ALDothanBParser extends FieldProgramParser {
  
  public ALDothanBParser() {
    super(CITY_LIST, "DOTHAN", "AL", 
          "ADDR/S6XP ( X/Z EMPTY EMPTY EMPTY TIME CALL! | X_CALL | X? TIME? CALL! ) PRI? MAP? INFO+");
    removeWords("AVENUE", "ESTATES", "LANE", "PLACE", "SQUARE", "TERRACE");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@dothan.org,777";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    body = stripFieldStart(body, "CITY OF DOTHAN:");
    body = stripFieldEnd(body, "\nstop");
    String[] flds = body.split(";");
    String[] flds2 = body.split(",");
    if (flds2.length >= flds.length) flds = flds2;
    if (!parseFields(flds, data)) return false;
    
    // Two many optional fields.  There has to be at least one properly
    // formatted data field before we accept the result
    return (data.strTime.length() > 0 || data.strCross.length() > 0 ||
            data.strPriority.length() > 0 || data.strMap.length() > 0);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("X_CALL")) return new MyCrossCallField();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("PRI")) return new PriorityField("\\d", true);
    if (name.equals("MAP")) return new MapField("ZE:.*", true);
    return super.getField(name);
  }
  
  private static final Pattern ADDR_MAP_PTN = Pattern.compile("(.*) (?:[NSEW]|[NS][EW]) SECTOR\\b *(.*)");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_MAP_PTN.matcher(field);
      if (match.matches()) {
        String addr = match.group(1).trim();
        String extra = match.group(2);
        
        parseAddress(StartType.START_ADDR, FLAG_RECHECK_APT | FLAG_NO_CITY | FLAG_ANCHOR_END, addr, data);
        parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, extra, data);
        data.strPlace = getLeft();
        
      } else {
        super.parse(field, data);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT CITY PLACE";
    }
  }
  
  private static final Pattern CROSS_MARK_PTN = Pattern.compile("\\bX\\b");
  private class MyCrossField extends CrossField {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = CROSS_MARK_PTN.matcher(field);
      if (!match.find()) return false;
      field = match.replaceAll("/");
      field = field.replace("*", "");
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      if (!checkParse(field, data)) abort();
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
  
  private static final Pattern CROSS_CALL_PTN = Pattern.compile("(.* X [^a-z]+) (.*[a-z].*)");
  private class MyCrossCallField extends MyCrossField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = CROSS_CALL_PTN.matcher(field);
      if (!match.matches()) return false;
      String cross = match.group(1).trim();
      String call = match.group(2).trim();
      
      super.checkParse(cross, data);
      
      match = CODE_CALL_PTN.matcher(call);
      if (match.matches()) {
        data.strCode = match.group(1);
        call =  match.group(2).trim();
      }
      data.strCall = call;
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
    
    @Override
    public String getFieldNames() {
      return "X CODE CALL";
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
    "DALE CO",
    "DOTHAN",
    "DUPREE",
    "ENON",
    "GARRETTS CROSSROADS",
    "GORDON",
    "GRANGEBURG",
    "HARMON",
    "HEADLAND",
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
    "SLOCOMB",
    "SOUTHERN JUNCTION",
    "TAYLOR",
    "WEBB",
    "WICKSBURG"
  };
}
