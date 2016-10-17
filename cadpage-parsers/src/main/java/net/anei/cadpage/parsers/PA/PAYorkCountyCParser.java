package net.anei.cadpage.parsers.PA;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class PAYorkCountyCParser extends FieldProgramParser {
  
  public PAYorkCountyCParser() {
    super(CITY_LIST, "YORK COUNTY", "PA",
           "ADDR PLACE? CITY! APT ( EMPTY | X/Z+? MAPPHONE! ) CALL+ Units_Sent:SKIP UNIT");
  }
  
  @Override
  public String getFilter() {
    return "messaging@iamresponding.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("Station ")) return false;
    data.strSource = subject;
    if (body.startsWith(": ")) body = body.substring(2).trim();
    String[] flds = body.split("\n");
    return parseFields(flds, data);
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
  
  private static final String PLACE_LABEL = "Additional Location Information:";
  private class MyPlaceField extends PlaceField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.startsWith(PLACE_LABEL)) return false;
      field = field.substring(PLACE_LABEL.length()).trim();
      return super.checkParse(field, data);
    }
  }
  
  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("Common Name ")) {
        field = field.substring(12).trim();
        parseAddress(StartType.START_PLACE, FLAG_ONLY_CITY | FLAG_ANCHOR_END, field, data);
      } else {
        data.strCity = field;
      }
      
      if (data.strCity.endsWith(" BORO")) {
        data.strCity = data.strCity.substring(0, data.strCity.length()-5).trim();
      }
    }
    
    @Override
    public String getFieldNames() {
      return "PLACE CITY";
    }
  }
  
  private static final Pattern MAP_PHONE_PTN = Pattern.compile("(?:District: *)?(\\d{2,3}-?\\d{1,3})|Phone *(.*)");
  private class MyMapPhoneField extends Field {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = MAP_PHONE_PTN.matcher(field);
      if (!match.matches()) return false;
      String map = match.group(1);
      if (map != null) {
        data.strMap = match.group(1);
      } else {
        data.strPhone = match.group(2);
      }
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "MAP PHONE";
    }
  }
  
  @Override
  protected Field getField(String name) {
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("EMPTY")) return new SkipField("", true);
    if (name.equals("MAPPHONE")) return new MyMapPhoneField();
    return super.getField(name);
  }
  
  private static final String[] CITY_LIST = new String[]{
    // Cities
    "YORK CITY",
    
    // Boroughs
    "CROSS ROADS BORO",
    "DALLASTOWN BORO",
    "DELTA BORO",
    "DILLSBURG BORO",
    "DOVER BORO",
    "EAST PROSPECT BORO",
    "FAWN GROVE BORO",
    "FELTON BORO",
    "FRANKLINTOWN BORO",
    "GLEN ROCK BORO",
    "GOLDSBORO BORO",
    "HALLAM BORO",
    "HANOVER BORO",
    "JACOBUS BORO",
    "JEFFERSON BORO",
    "LEWISBERRY BORO",
    "LOGANVILLE BORO",
    "MANCHESTER BORO",
    "MOUNT WOLF BORO",
    "NEW FREEDOM BORO",
    "NEW SALEM BORO",
    "NORTH YORK BORO",
    "RAILROAD BORO",
    "RED LION BORO",
    "SEVEN VALLEYS BORO",
    "SHREWSBURY BORO",
    "SPRING GROVE BORO",
    "STEWARTSTOWN BORO",
    "WELLSVILLE BORO",
    "WEST YORK BORO",
    "WINDSOR BORO",
    "WINTERSTOWN BORO",
    "WRIGHTSVILLE BORO",
    "YOE BORO",
    "YORK HAVEN BORO",
    "YORKANA BORO",
    
    // Townships
    "CARROLL TWP",
    "CHANCEFORD TWP",
    "CODORUS TWP",
    "CONEWAGO TWP",
    "DOVER TWP",
    "EAST HOPEWELL TWP",
    "EAST MANCHESTER TWP",
    "FAIRVIEW TWP",
    "FAWN TWP",
    "FRANKLIN TWP",
    "HEIDELBERG TWP",
    "HELLAM TWP",
    "HOPEWELL TWP",
    "JACKSON TWP",
    "LOWER CHANCEFORD TWP",
    "LOWER WINDSOR TWP",
    "MANCHESTER TWP",
    "MANHEIM TWP",
    "MONAGHAN TWP",
    "NEWBERRY TWP",
    "NORTH CODORUS TWP",
    "NORTH HOPEWELL TWP",
    "PARADISE TWP",
    "PEACH BOTTOM TWP",
    "PENN TWP",
    "SHREWSBURY TWP",
    "SPRING GARDEN TWP",
    "SPRINGETTSBURY TWP",
    "SPRINGFIELD TWP",
    "WARRINGTON TWP",
    "WASHINGTON TWP",
    "WEST MANCHESTER TWP",
    "WEST MANHEIM TWP",
    "WINDSOR TWP",
    "YORK TWP"

  };
}
