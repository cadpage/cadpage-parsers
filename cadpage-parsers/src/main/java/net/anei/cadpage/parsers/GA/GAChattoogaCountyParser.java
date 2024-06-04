package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.dispatch.DispatchA86Parser;

public class GAChattoogaCountyParser extends DispatchA86Parser {
  
  public GAChattoogaCountyParser() {
    super("CHATTOOGA COUNTY", "GA");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@ChattoogaCo-GA-911.info";
  }
}
