package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA97Parser;

public class NCStanlyCountyCParser extends DispatchA97Parser {

  public NCStanlyCountyCParser() {
    super("STANLY COUNTY", "NC");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    
    // THey put apartment info in the city field and city info in the state field (
    if (data.strCity.startsWith("#")) {
      data.strApt = data.strCity.substring(1).trim();
      data.strCity = data.strState;
      data.strState = "";
    }
    return true;
  }

}
