package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class OHPortageCountyAParser extends FieldProgramParser {
  
  private static final Pattern MARKER = Pattern.compile("^[A-Z0-9\\.]+: +", Pattern.CASE_INSENSITIVE);
  
  public OHPortageCountyAParser() {
    super(OHPortageCountyParser.CITY_LIST, "PORTAGE", "OH",
          "PREFIX? CALL ZERO? ADDR! PLACE? CITY/Y INFO+");
  }
  
  @Override
  public String getFilter() {
    return "911@ci.ravenna.oh.us,info@sundance-sys.com";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    
    Matcher match = MARKER.matcher(body);
    if (match.find()) body = body.substring(match.end());
    if (!parseFields(body.split(","), data)) return false;
    data.strCity = OHPortageCountyParser.fixCity(data.strCity);
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("PREFIX")) return new CallField();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("ZERO")) return new SkipField("0?");
    return super.getField(name);
  }

  private static final Pattern CALL_CODE_PTN = Pattern.compile("(?:\\*+(.*?)\\*+)? *(?:(.*?) +)?\\b([A-Z0-9]{1,7})-(.*)");
  private class MyCallField extends CallField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = CALL_CODE_PTN.matcher(field);
      if (!match.matches()) {
        if (!field.startsWith("-")) return false;
        field = field.substring(1).trim();
      } else {
        data.strCode = match.group(3);
        field = append(getOptGroup(match.group(1)), " - ", getOptGroup(match.group(2)));
        field = append(getOptGroup(field), " - ", match.group(4).trim());
      }
      data.strCall = append(data.strCall, ", ", field);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
    
    @Override 
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
  
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("`")) return;
      super.parse(field, data);
    }
  }
  
  private class MyCityField extends CityField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.length() == 0) return true;
      return super.checkParse(field, data);
    }
  }
  
  @Override
  public String postAdjustMapAddress(String addr) {
    return OHPortageCountyParser.fixMapAddress(addr);
  }
}