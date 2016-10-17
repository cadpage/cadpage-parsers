package net.anei.cadpage.parsers.CO;
import net.anei.cadpage.parsers.dispatch.DispatchA55Parser;

public class COGrandCountyParser extends DispatchA55Parser {
  
  public COGrandCountyParser() {
    super("GRAND COUNTY", "CO");
  }
  
  @Override
  public String getFilter() {
    return "cadalerts@eforcesoftware.com,kfpd_dispatches@kremmlingfire.org,brady.mathis@gmail.com,brady.mathis@kremmlingfire.org";
  }
}
