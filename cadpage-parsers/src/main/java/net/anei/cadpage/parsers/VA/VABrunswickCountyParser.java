package net.anei.cadpage.parsers.VA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class VABrunswickCountyParser extends FieldProgramParser {
    
  
  public VABrunswickCountyParser() {
    super(CITY_LIST, "BRUNSWICK COUNTY", "VA",
          "( Reported:DATETIME IDCALL! Loc:ADDR2/S! X? ( UNIT | PLACE UNIT! ) | " +
            "IDCALL Reported:DATETIME? ADDR! X? ( UNIT | CITY_PLACE1 UNIT ) )");
  }
  
  @Override
  public String getFilter() {
    return "bcso_dispatch@verizon.net,Dispatch@brunswickso.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("BCSO CFS Info - DO NOT REPLY") &&
        !subject.equals("BCSO CFS INFO")) return false;
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("IDCALL")) return new MyIdCallField();
    if (name.equals("ADDR2")) return new MyAddress2Field(); 
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("CITY_PLACE1")) return new MyCityPlace1Field();
    if (name.equals("UNIT")) return new UnitField("(?:\\b(?:\\d{1,4}[A-Z]*|[A-Z]{4}|[A-Z]{1,2}\\d+|FRSTRY)\\b *)+");
    return super.getField(name);
  }
  
  private class MyIdCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(' ');
      if (pt < 0) abort();
      data.strCallId = field.substring(0,pt);
      data.strCall = field.substring(pt+1).trim();
    }
    
    @Override
    public String getFieldNames() {
      return "ID CALL";
    }
  }
  
  private static final Pattern ADDR_ST_ZIP_PTN = Pattern.compile("(.*),(?: *([A-Z]{2}))?(?: (\\d{5}))?");
  private class MyAddress2Field extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_ST_ZIP_PTN.matcher(field);
      if (!match.matches()) abort();
      super.parse(match.group(1).trim(), data);
      data.strState = getOptGroup(match.group(2));
      if (data.strCity.length() == 0) data.strCity = getOptGroup(match.group(3));
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT CITY ST";
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.contains("/")) {
        parse(field, data);
        return true;
      }
      return super.checkParse(field, data);
    }
  }
  
  private class MyCityPlace1Field extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() < 2) return;
      
      String place, city;
      int pt = field.lastIndexOf("  ");
      if (pt >= 0) {
        place = field.substring(0,pt).trim();
        city = field.substring(pt+2).trim();
      } else {
        place = "";
        city = field;
      }
      boolean confirmCity = false;
      for (String cty : CITY_LIST) {
        if (cty.startsWith(city)) {
          confirmCity = true;
          city = cty;
        }
      }
      if (data.strPlace.length() == 0 && !confirmCity) {
        place = city;
        city = "";
      }
      data.strPlace = place;
      data.strCity = city;
    }
    
    @Override
    public String getFieldNames() {
      return "PLACE CITY";
    }
  }
  
  private static final String[] CITY_LIST = new String[]{
    "ALBERTA",
    "BRODNAX",
    "DOLPHIN",
    "EBONY",
    "LAWRENCEVILLE",
    "FREEMAN",
    "GASBURG",
    "RAWLINGS",
    "VALENTINES",
    "WARFIELD",
    "WHITE PLAINS",
    
    // Greensville County
    "EMPORIA",
    
    // Lunenburg County
    "DUNDAS",
    
    // Mecklenburg County
    "BRACEY",
    "SOUTH HILL",
    
    // Notoway County
    "BLACKSTONE"
  };
}
