package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class TXJohnsonCountyAParser extends DispatchOSSIParser {

  public TXJohnsonCountyAParser() {
    super("JOHNSON COUNTY", "TX", "( CANCEL ADDR | FYI SRC SRC? CALL ADDR X+? INFO+ )");
  }

  @Override
  public Field getField(String name) {
    if (name.equals("SRC")) return new MySourceField();
    return super.getField(name);
  }
  
  private class MySourceField extends SourceField {
    
    public MySourceField() {
      super("(.{0,3})", true);
    }
    
    @Override 
    public void parse(String field, Data data) {
      data.strSource = append(data.strSource, " ", field);
    }
  }
  
}
