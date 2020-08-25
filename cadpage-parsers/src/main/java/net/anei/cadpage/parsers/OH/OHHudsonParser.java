package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class OHHudsonParser extends FieldProgramParser {
  
  public OHHudsonParser() {
    super("HUDSON", "OH",
           "NUM ADDR PLACE SRC! INFO/CS+");
  }
  
  @Override
  public String getFilter() {
    return "@hudson.oh.us";
  }
  
  private static final Pattern MARKER = Pattern.compile("HudsonCAD: ?CAD System +");
  
  @Override
  public boolean parseMsg(String body, Data data) {
    Matcher match = MARKER.matcher(body);
    if (!match.lookingAt()) return false;
    body = body.substring(match.end());
    return parseFields(body.split(","), data);
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("NUM")) return new NumberField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("SRC")) return new SourceField("(?:HUDSON|Hudson|BOSTON HEIGHTS)? ?(?:EMS|FIRE|F?\\d+[A-Z]?)|HUDSON[A-Z]*", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class NumberField extends Field {
    @Override
    public void parse(String field, Data data) {
      if (!field.equals("0")) data.strAddress = field;
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR";
    }
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = append(data.strAddress, " ", field);
      data.strAddress = "";
      field = field.replace(".", "");
      parseAddress(field, data);
    }
  }
  
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals(getRelativeField(-1))) return;
      super.parse(field, data);
    }
  }
  
  private static Pattern MADDR_PTN  = Pattern.compile("\\d+ +.*");
  private static Pattern MUT_AID_PTN = Pattern.compile(" TO (.*) FOR ");
  private class MyInfoField extends InfoField {
    
    @Override
    public void parse(String field, Data data) {
      if (data.strCall.length() == 0) {
        if (field.startsWith("F-")) {
          data.strCall = field.substring(2).trim();
          return;
        } else {
          String call = CALL_LIST.getCode(field);
          if (call != null) {
            data.strCall = call;
            field = field.substring(call.length()).trim();
            field = stripFieldStart(field, ":");
          }
          else {
            data.strCall = field;
            return;
          }
        }
      }
      
      if (data.strAddress.equals("ANYWHERE")) {
        if (MADDR_PTN.matcher(field).matches() || isValidAddress(field)) {
          data.strAddress = "";
          parseAddress(field, data);
          return;
        }
      }

      Matcher match = MUT_AID_PTN.matcher(field);
      if (match.find()) data.strCity = match.group(1);

      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "CALL INFO ADDR CITY";
    }
  }
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "FIRE",
      "EMS",
      "Crash",
      "Fire Alarm",
      "Fire Alarm: Residential",
      "Lockout: Motor Vehicle",
      "Natural Disaster / Tornado / Earthquake"
   );
}
