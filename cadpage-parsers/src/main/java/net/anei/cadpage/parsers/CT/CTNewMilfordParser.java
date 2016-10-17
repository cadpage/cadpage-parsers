package net.anei.cadpage.parsers.CT;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;

import net.anei.cadpage.parsers.dispatch.DispatchA16Parser;
/**
 * New Milford Twp, CT
 */
public class CTNewMilfordParser extends DispatchA16Parser {
  
  public CTNewMilfordParser() {
    super(CTLitchfieldCountyParser.CITY_LIST, "NEW MILFORD", "CT");
  }

  @Override
  public String getFilter() {
    return "admin@newmilfordpolice.org";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    data.strCall = cleanTrailingCode(data.strCall, data);
    data.strUnit = cleanTrailingCode(data.strUnit, data);
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram() + " CODE";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("PLACENAME")) return new MyPlaceNameField();
    return super.getField(name);
  }
  
  private class MyPlaceNameField extends PlaceNameField {
    @Override
    public void parse(String field, Data data) {
      field = cleanTrailingCode(field, data);
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CODE";
    }
  }
  
  private String cleanTrailingCode(String field, Data data) {
    Matcher match = CODE_PTN.matcher(field);
    if (match.matches()) {
      field = match.group(1).trim();
      data.strCode = match.group(2).trim();
    }
    return field;
  }
  private static final Pattern CODE_PTN = Pattern.compile("(.*?)\\**(\\d{1,2}-?[A-Z]-?\\d{1,2}[A-Z]?)\\**");
}
