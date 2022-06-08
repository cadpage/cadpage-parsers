package net.anei.cadpage.parsers.CO;
import net.anei.cadpage.parsers.dispatch.DispatchA55Parser;

public class COGrandCountyAParser extends DispatchA55Parser {
  
  public COGrandCountyAParser() {
    super("GRAND COUNTY", "CO");
  }
  
  @Override
  public String getFilter() {
    return "cadalerts@eforcesoftware.com,kfpd_dispatches@kremmlingfire.org,brady.mathis@gmail.com,brady.mathis@kremmlingfire.org,eforce_alert@co.grand.co.us";
  }
}
