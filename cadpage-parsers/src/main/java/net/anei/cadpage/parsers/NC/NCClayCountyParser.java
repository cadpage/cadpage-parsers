package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchA65Parser;

/**
 * Clay County, NC
 */
public class NCClayCountyParser extends DispatchA65Parser {

  public NCClayCountyParser() {
    super(CITY_LIST, "CLAY COUNTY", "NC");
  }

  @Override
  public String getFilter() {
    return "dispatch@911email.net,clayconc@911email.net,@claync911.net";
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }

  private static final String[] CITY_LIST = new String[]{

      // Town
      "HAYESVILLE",

      // Unincorporated communities
      "BRASSTOWN",
      "ELF",
      "TUSQUITTEE",
      "WARNE"
  };
}
