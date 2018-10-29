package net.anei.cadpage.parsers.FL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class FLPutnamCountyParser extends FieldProgramParser {

  public FLPutnamCountyParser() {
    super("PUTNAM COUNTY", "FL", 
          "CALL ADDR APT ID UNIT! END");
  }
  
  @Override
  public String getFilter() {
    return "@putnamsheriff.org,dispatch@pertdispatch.com";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    int pt = body.indexOf('\n');
    if (pt >= 0) body = body.substring(0,pt).trim();
    return parseFields(body.split(";",-1), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ID")) return new IdField("[A-Z]+[A-Z0-9]+\\d", true);
    return super.getField(name);
  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("([A-Z0-9]+) - +(.*)");
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_CALL_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCode = match.group(1);
      data.strCall = match.group(2);
    }
    
    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
}
