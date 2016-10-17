package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class OHHudsonParser extends FieldProgramParser {
  
  public OHHudsonParser() {
    super("HUDSON", "OH",
           "NUM ADDR PLACE SRC! INFO MAADDR");
  }
  
  @Override
  public String getFilter() {
    return "@hudson.oh.us";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    if (! body.startsWith("HudsonCAD:CAD System ")) return false;
    body = body.substring(21).trim();
    return parseFields(body.split(","), data);
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
  
  private static Pattern CALL_PTN = Pattern.compile("^(FIRE|EMS)");
  private class MyInfoField extends InfoField {
    
    @Override
    public void parse(String field, Data data) {
      if (data.strCall.length() == 0) {
        Matcher match = CALL_PTN.matcher(field);
        if (match.find()) {
          data.strCall = match.group(1);
          field = field.substring(match.end()).trim();
        }
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "CALL INFO";
    }
  }
  
  private static Pattern MUT_AID_PTN = Pattern.compile(" TO (.*) FOR ");
  private class MAAddressField extends AddressField {
    
    @Override
    public void parse(String field, Data data) {
      if (data.strAddress.equals("ANYWHERE")) {
        data.strAddress = "";
        parseAddress(field, data);
        Matcher match = MUT_AID_PTN.matcher(data.strSupp);
        if (match.find()) data.strCity = match.group(1);
      }
      
      else {
        data.strSupp = append(data.strSupp, " / ", field);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "CITY ADDR INFO";
    }
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("NUM")) return new NumberField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("SRC")) return new SourceField("(?:HUDSON|Hudson)? ?(?:EMS|FIRE|F\\d+)", true);
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("MAADDR")) return new MAAddressField();
    return super.getField(name);
  }
}
