package net.anei.cadpage.parsers.CO;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA64Parser;

public class CORioBlancoCountyBParser extends DispatchA64Parser {
  
  public CORioBlancoCountyBParser() {
    super("RIO BLANCO COUNTY", "CO");
  }
  
  @Override
  public String getFilter() {
    return "reports@messaging.eforcesoftware.net";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    if (data.strCity.equalsIgnoreCase("UNINCORPORATED")) data.strCity = "";
    return true;
  }
  
}
