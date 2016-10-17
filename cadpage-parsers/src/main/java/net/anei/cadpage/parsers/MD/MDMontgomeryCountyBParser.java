package net.anei.cadpage.parsers.MD;

import net.anei.cadpage.parsers.dispatch.DispatchProQAParser;

public class MDMontgomeryCountyBParser extends DispatchProQAParser {
  
  public MDMontgomeryCountyBParser() {
    super("MONTGOMERY COUNTY", "MD", 
          "PRI! TIME! ID! NAME NAME/S CALL ADDR! APT? INFO INFO/N INFO+");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@butlermedicaltransport.com";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("PRI")) return new PriorityField("Priority (\\d+)\\b.*", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    if (name.equals("APT")) return new AptField("\\d+[A-Z]?|ER *\\d+", true);
    return super.getField(name);
  }

}
