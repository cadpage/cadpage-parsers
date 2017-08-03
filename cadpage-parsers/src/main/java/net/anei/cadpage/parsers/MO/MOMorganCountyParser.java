package net.anei.cadpage.parsers.MO;
import net.anei.cadpage.parsers.dispatch.DispatchA55Parser;

public class MOMorganCountyParser extends DispatchA55Parser {
  
  public MOMorganCountyParser() {
    super("MORGAN COUNTY", "MO");
  }
  
  @Override
  public String getFilter() {
    return "Dispatch@morgan-911.org";
  }
  
}
