package net.anei.cadpage.parsers.FL;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class FLPalmBeachCountyParser extends FieldProgramParser {

  public FLPalmBeachCountyParser() {
    super(CITY_CODES, "PALM BEACH COUNTY", "FL", 
          "Type:CALL! Event_Location:ADDR/S4? APT? Dev:PLACE Map_page:MAP! Map_Coord:MAP! Talk_Group:CH? TIME:TIME! Disp:UNIT? UNIT+");
  }
  
  private static Pattern DOTDOTDOT = Pattern.compile("\\.{3,}");
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    //check subject
    if (!subject.equals("Page Notification")) return false;
    
    //remove \\.{3,}
    body = DOTDOTDOT.matcher(body).replaceAll(" ");
    
    body = body.replace("Disp:", ",Disp:");
    
    return parseFields(body.split(","), data);
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("MAP")) return new MyMapField();
    if (name.equals("TIME")) return new TimeField("\\d{2}:\\d{2}:\\d{2}", true);
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }

  private static final Pattern COLON_AT_PTN = Pattern.compile(":(?: @)?");
  private class MyAddressField extends AddressField {

    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      super.parse(p.get(COLON_AT_PTN), data);
      data.strPlace = p.get(":APT ").trim();
      data.strApt = p.get();
    }

    @Override
    public String getFieldNames() {
      return "ADDR CITY APT PLACE";
    }
    
  }
  
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (!data.strPlace.equals(field)) data.strPlace = append(data.strPlace, " - ", field);
    }
  }
  
  private class MyMapField extends MapField {
    @Override
    public void parse(String field, Data data) {
      data.strMap = append(data.strMap, " / ", field);
    }
  }
  
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      data.strUnit = append(data.strUnit, " ", field);
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "SPB",   "South Palm Beach",
      "NPB",   "North Palm Beach",
      "HB",    "Highland Beach",
      "LW",    "Lake Worth",
      "PBS",   "Palm Beach Shores",
      "PBG",   "Palm Beach Gardens",
      "JB",    "Juno Beach",
      "LG",    "Loxahatchee Groves",
      "JP",    "Jupiter",
      "GR",    "Glren Ridge",
      "BG",    "Belle Glade",
      "DB",    "Delray Beach",
      "RB",    "Riviera Beach",
      "WPB",   "West Palm Beach",
      "PB",    "Palm Beach",
      "LCS",   "Lake Clark Shores",
      "MP",    "Mangonia Park",
      "LP",    "Lake Park",
      "BR",    "Boca Raton",
      "PBC",   "Palm Beach County",
      "JIC",   "Jupiter Inlet Beach Colony",
      "SB",    "South Bay",
      "RPB",   "Royal Palm Beach",
      "PS",    "Palm Springs",
      "OR",    "Ocean Ridge",
      "CL",    "Cloud Lake"
  });
}

