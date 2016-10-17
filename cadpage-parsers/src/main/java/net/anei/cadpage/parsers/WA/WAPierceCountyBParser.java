package net.anei.cadpage.parsers.WA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Pierce County, WA
 */
public class WAPierceCountyBParser extends FieldProgramParser {
  
  
  public WAPierceCountyBParser() {
    super( "PIERCE COUNTY", "WA", 
           "CODETIME CALL ADDR MAP! to:UNIT!");
  }
  
  @Override
  public String getFilter() {
    return "41411";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (body.startsWith("FIRE911\n\n")) body = body.substring(9).trim();
    return parseFields(body.split("\n"), 5, data);
  }
  
  private static final Pattern CODE_TIME_PTN = Pattern.compile("([A-Z0-9]+) @(?: (\\d\\d:\\d\\d:\\d\\d))?");
  private class CodeTimeField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCode = match.group(1);
      data.strTime = getOptGroup(match.group(2));
    }
    
    @Override
    public String getFieldNames() {
      return "CODE TIME";
    }
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(',');
      if (pt >= 0) field = field.substring(0,pt).trim();
      super.parse(field, data);
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CODETIME")) return new CodeTimeField();
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }
  
  @Override
  public String adjustMapAddress(String sAddress) {
    return WAPierceCountyParser.adjustMapAddressCommon(sAddress);
  }
}
