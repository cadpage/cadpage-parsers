package net.anei.cadpage.parsers.MO;
import net.anei.cadpage.parsers.dispatch.DispatchA57Parser;

public class MOMorganCountyParser extends DispatchA57Parser {
  
  public MOMorganCountyParser() {
    super("MORGAN COUNTY", "MO");
  }
  
  @Override
  public String getFilter() {
    return "Dispatch@morgan-911.org";
  }
}
