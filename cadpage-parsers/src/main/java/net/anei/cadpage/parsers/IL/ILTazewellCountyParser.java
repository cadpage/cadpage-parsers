package net.anei.cadpage.parsers.IL;

import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;


public class ILTazewellCountyParser extends DispatchOSSIParser {
  
  public ILTazewellCountyParser() {
    super("TAZEWELL COUNTY", "IL",
           "FYI SRC DATETIME CALL ADDR! X X INFO+");
  }
  
  @Override
  public String getFilter() {
    return "cad@tazewell911.com";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("SRC")) return new SourceField("[A-Z]{4}", true);
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
    
  }
}
