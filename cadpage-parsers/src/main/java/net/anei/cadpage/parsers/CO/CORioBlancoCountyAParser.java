package net.anei.cadpage.parsers.CO;
import net.anei.cadpage.parsers.dispatch.DispatchA95Parser;

public class CORioBlancoCountyAParser extends DispatchA95Parser {

  public CORioBlancoCountyAParser() {
    super("RIO BLANCO COUNTY", "CO");
  }

  @Override
  public String getFilter() {
    return "noreply-centralsquare@rangelygovt.com";
  }
}
