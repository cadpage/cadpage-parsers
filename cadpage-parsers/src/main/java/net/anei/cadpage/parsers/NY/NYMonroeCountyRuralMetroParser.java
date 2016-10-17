package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchProQAParser;

/**
 * Monroe County, NY (Rural Metro MEdical Services)
 */
public class NYMonroeCountyRuralMetroParser extends DispatchProQAParser {
  
  public NYMonroeCountyRuralMetroParser() {
    super("MONROE COUNTY", "NY",
           "ADDR! APT? X:X? PRI CALL! INFO+ LOCI:PLACE ENTRY:INFO+ XST:SKIP");
  }

  @Override
  public boolean parseMsg(String body, Data data) {
    if (!body.contains("/Priority ")) return false;
    body = body.replace("/X=", "/X:").replace(" ENTRY:", "/ENTRY:").replace(" XST:", "/XST:");
    return super.parseMsg(body, data);
  }
  
  private class MyAptField extends AptField {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.startsWith("Priority ")) return false;
      if (field.length() > 4) return false;
      super.parse(field, data);
      return true;
    }
  }
  
  private class MyPriorityField extends PriorityField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("Priority ")) field = field.substring(9).trim();
      super.parse(field, data);
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("PRI")) return new MyPriorityField();
    return super.getField(name);
  }
}
