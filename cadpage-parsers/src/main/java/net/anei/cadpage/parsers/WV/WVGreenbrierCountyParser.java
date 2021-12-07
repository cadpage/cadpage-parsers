package net.anei.cadpage.parsers.WV;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class WVGreenbrierCountyParser extends DispatchA19Parser {

  public WVGreenbrierCountyParser() {
    super(CITY_CODES, "GREENBRIER COUNTY", "WV");
  }

  @Override
  public String getFilter() {
    return "FlexRapidNotification@dccnotify.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    body = body.replace("C & O CROSSOVER", "C O CROSSOVER");
    return super.parseMsg(subject, body, data);
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ALD", "ALDERSON",
      "ASB", "ASHBURY",
      "CHA", "CHARMCO",
      "CRA", "CRAWLEY",
      "FRA", "FRANKFORD",
      "LES", "LESLIE",
      "LWB", "LEWISBURG",
      "MAX", "MAXWELTON",
      "QUI", "QUINWOOD",
      "RAI", "RAINELLE",
      "REN", "RENICK",
      "RON", "RONCEVERTE",
      "RUP", "RUPERT",
      "SMT", "SMOOT",
      "UNI", "UNION",
      "WIL", "WILLIAMSBURG",
      "WSS", "WHITE SULPHUR SPRINGS"
  });
}
