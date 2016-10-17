package net.anei.cadpage.parsers.VA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Colonial Heights, VA
 */

public class VAColonialHeightsParser extends FieldProgramParser {
  
  public VAColonialHeightsParser() {
    super(CITY_LIST, "COLONIAL HEIGHTS", "VA",
           "( Reported:DATETIME! ID_CALL! Loc:ADDR2/S! X? PLACE? UNIT! END | ID_CALL! Reported:DATETIME! ADDR X? PLACE_CITY? UNIT! END )");
  }
  
  @Override
  public String getFilter() {
    return "comm-page@colonialheightsva.gov";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.equals("from CHECC")) return false;
    return parseFields(body.split("\n"), 4, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID_CALL")) return new MyIdCallField();
    if (name.equals("ADDR2")) return new MyAddress2Field();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} +\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("PLACE_CITY")) return new MyPlaceCityField();
    return super.getField(name);
  }
  
  
  private static Pattern ID_CALL_PTN = Pattern.compile("(\\d\\d-\\d{6}) +(.*)");
  private class MyIdCallField extends Field {
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = ID_CALL_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strCallId = match.group(1);
      data.strCall = match.group(2);
      return true;
    }
    

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "ID CALL";
    }
  }
  
  private static final Pattern ADDR_ST_ZIP_PTN = Pattern.compile("(.*), *VA(?: +(\\d{5}))");
  private class MyAddress2Field extends AddressField {
    @Override
    public void parse(String field, Data data) {
      String zip = null;
      Matcher match = ADDR_ST_ZIP_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        zip = match.group(2);
      }
      super.parse(field, data);
      if (data.strCity.length() == 0 && zip != null) data.strCity = zip;
    }
  }
  
  private class MyCrossField extends CrossField {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.contains("/")) return false;
      parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("/")) field = field.substring(1).trim();
      if (field.endsWith("/")) field = field.substring(0,field.length()-1).trim();
      super.parse(field, data);
    }
  }
  
  private class MyPlaceCityField extends Field {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Result res = parseAddress(StartType.START_PLACE, FLAG_ONLY_CITY | FLAG_CHECK_STATUS | FLAG_ANCHOR_END, field);
      if (!res.isValid()) return false;
      res.getData(data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "PLACE CITY";
    }
  }
  
  private static final String[] CITY_LIST = new String[]{
    "CHESTERFIELD",
    "COLONIAL HEIGHTS"
  };
}
