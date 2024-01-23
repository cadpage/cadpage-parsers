package net.anei.cadpage.parsers.UT;
import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class UTCarbonCountyParser extends DispatchA19Parser {

  public UTCarbonCountyParser() {
    super("CARBON COUNTY", "UT");
  }

  @Override
  public String getFilter() {
    return "FlexRapidNotification@dccnotify.com";
  }
}
