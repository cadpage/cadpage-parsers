package net.anei.cadpage.parsers.VA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;

public class VAPatrickCountyBParser extends FieldProgramParser {

  public VAPatrickCountyBParser() {
    super("PATRICK COUNTY", "VA", 
          "SRC CALL ADDRCITY! XST:X!");
  }
  
  @Override
  public String getFilter() {
    return "cad@sheriff.co.patrick.va.us";
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom(){
      @Override public boolean mixedMsgOrder() { return true; }
      @Override public boolean splitBlankIns() { return false; }
      @Override public int splitBreakLength() { return 130; }
      @Override public int splitBreakPad() { return 1; }
    };
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split(" \\| "), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("SRC")) return new SourceField("[- A-Z]+", true);
    if (name.equals("ADDRCITY"))  return new MyAddressCityField();
    if (name.equals("ID")) return new IdField("[A-Z]+\\d{2}-\\d{6}", true);
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }
  
  private static final Pattern ADDR_ZIP_PTN = Pattern.compile("(.*) (\\d{5})");
  private static final Pattern ADDR_ST_PTN = Pattern.compile("[A-Z]{2}");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_ZIP_PTN.matcher(field);
      String zip = null;
      if (match.matches()) {
        field = match.group(1).trim();
        zip = match.group(2);
      }
      
      Parser p = new Parser(field);
      String city = p.getLastOptional(',');
      if (ADDR_ST_PTN.matcher(city).matches()) {
        data.strState = city;
        city = p.getLastOptional(',');
      }
      if (city.length() == 0 && zip != null) city = zip;
      data.strCity = city;
      
      parseAddress(p.get(), data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " ST";
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("//", "/");
      field = stripFieldEnd(field, "/");
      field = stripFieldStart(field, "/");
      super.parse(field, data);
    }
  }
}
