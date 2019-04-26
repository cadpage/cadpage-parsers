package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class PAWarrenCountyBParser extends FieldProgramParser {
  
  public PAWarrenCountyBParser() {
    super("WARREN COUNTY", "PA", 
          "Inc_Code:CALL! Address:ADDRCITY/S6! Common_Name:PLACE! Units:UNIT! Cross_Streets:X END");
  }
  
  @Override
  public String getFilter() {
    return "alerts@warrencounty.ealertgov.com";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Warren County Alert")) return false;
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("PLACE")) return new MyPlaceField();
    return super.getField(name);
  }
  
  private static final Pattern PLACE_PTN = Pattern.compile("\\d\\d +(.*)");
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = PLACE_PTN.matcher(field);
      if (match.matches()) field = match.group(1);
      super.parse(field, data);
    }
  }
  
  @Override
  public String adjustMapCity(String city) {
    if (city.endsWith(" TWP")) {
      city = city.substring(0,city.length()-3) + "TOWNSHIP";
    }
    return city;
  }
}
