package net.anei.cadpage.parsers.CO;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA55Parser;

public class CORioBlancoCountyParser extends DispatchA55Parser {
  
  public CORioBlancoCountyParser() {
    super("RIO BLANCO COUNTY", "CO");
  }
  
  @Override
  public String getFilter() {
    return "cadalerts@eforcesoftware.com,cadalerts@messaging.eforcesoftware.net";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    if (data.strCity.equalsIgnoreCase("UNINCORPORATED")) data.strCity = "";
    return true;
  }
  
}
