package net.anei.cadpage.parsers.CO;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA55Parser;

public class CORioBlancoCountyAParser extends DispatchA55Parser {
  
  public CORioBlancoCountyAParser() {
    super("RIO BLANCO COUNTY", "CO");
  }
  
  @Override
  public String getFilter() {
    return "cadalerts@eforcesoftware.com,cadalerts@messaging.eforcesoftware.net";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (body.startsWith("Address:") && !body.contains("\n")) return false;
    if (!super.parseMsg(subject, body, data)) return false;
    if (data.strCity.equalsIgnoreCase("UNINCORPORATED")) data.strCity = "";
    return true;
  }
  
}
