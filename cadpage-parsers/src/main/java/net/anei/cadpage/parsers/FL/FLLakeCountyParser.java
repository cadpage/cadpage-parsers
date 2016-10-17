package net.anei.cadpage.parsers.FL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class FLLakeCountyParser extends FieldProgramParser {
  
  public FLLakeCountyParser() {
    super(CITY_LIST, "LAKE COUNTY", "FL",
        "ID? CH? SRC CALL UNK? ( GEO_PENDING PEND_ADDR INFO INFO | ADDR/S! (  SELECT_NOCITY MISC+? CITY! | ) ) PRI GPS1 GPS2 INFO+");
    removeWords("PARK");
  }
  
  public String getFilter() {
    return "@lakeems.org";
  }
  
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    if (body.startsWith("/ ")) body = body.substring(2).trim();
    if (!body.startsWith("CAD:")) return false;
    body = body.substring(4).trim();
    if (!parseFields(body.split("\\*"), data)) return false;
    if (data.strCallId.startsWith("-")) data.strCallId = "";
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("-?\\d+", true);
    if (name.equals("CH")) return new ChannelField("PS.*|", true);
    if (name.equals("UNK")) return new SkipField("UNKNOWN", true);
    if (name.equals("GEO_PENDING")) return new SkipField("GEO-PENDING", true);
    if (name.equals("PEND_ADDR")) return new MyPendingAddressField();
    if (name.equals("SELECT_NOCITY")) return new MySelectNoCityField();
    if (name.equals("MISC")) return new MyMiscField();
    return super.getField(name);
  }
  
  private static final Pattern PADDR_APT_PTN = Pattern.compile("\\d{1,4}[A-Z]?|[A-Z]");
  private class MyPendingAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      boolean first = true;
      for (String part : field.split(",")) {
        part = part.trim();
        if (first) {
          super.parse(part, data);
          first = false;
          continue;
        } 
        
        if (isCity(part)) {
          data.strCity = part;
          continue;
        }
        
        if (PADDR_APT_PTN.matcher(part).matches()) {
          data.strApt = append(data.strApt, "-", part);
          continue;
        }
        
        data.strPlace = append(data.strPlace, " - ", part);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT PLACE CITY";
    }
  }
  
  private class MySelectNoCityField extends SelectField {
    @Override
    public boolean checkParse(String field, Data data) {
      return data.strCity.length() == 0;
    }
  }

  private static final Pattern MISC_APT_PTN = Pattern.compile("(?:APT|LOT|RM|ROOM|SUITE) *(\\S+)|\\d{1,4}[A-Z]?|[A-Z]");
  private class MyMiscField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = MISC_APT_PTN.matcher(field);
      if (match.matches()) {
        String apt = match.group(1);
        if (apt == null) apt = field;
        data.strApt = append(data.strApt, " - ", apt);
      } else if (data.strPlace.length() > 0) {
        data.strApt = append(data.strApt, " - ", field);
      } else {
        data.strPlace = append(data.strPlace, " / ", field);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "PLACE APT";
    }
  }
  
  private static final String[] CITY_LIST = new String[] {
    "ASTATULA",
    "CLERMONT",
    "EUSTIS",
    "FRUITLAND PARK",
    "GROVELAND",
    "HOWEY IN THE HILLS",
    "HOWEY-IN-THE-HILLS",
    "LADY LAKE",
    "LEESBURG",
    "MASCOTTE",
    "MINNEOLA",
    "MONTVERDE",
    "MOUNT DORA",
    "TAVARES",
    "UMATILLA",
    "ALTOONA",
    "ASTOR",
    "BASSVILLE PARK",
    "CITRUS RIDGE",
    "FERNDALE",
    "GRAND ISLAND",
    "LAKE KATHRYN",
    "LAKE MACK-FOREST HILLS",
    "LANIER",
    "LISBON",
    "MOUNT PLYMOUTH",
    "OKAHUMPKA",
    "ORANGE BEND",
    "PAISLEY",
    "PINE LAKES",
    "PITTMAN",
    "SILVER LAKE",
    "SORRENTO",
    "YALAHA",
    
    // Volusa County
    "DELAND"
  };
}
