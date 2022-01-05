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
      "ASB", "ASBURY",
      "CHA", "CHARMCO",
      "CLD", "CALDWELL",
      "COV", "COVINGTON",
      "CRA", "CRAWLEY",
      "FRA", "FRANKFORD",
      "GRA", "GRASSY MEADOWS",
      "GRB", "GREENBRIER",
      "HIL", "HILLSBORO",
      "LES", "LESLIE",
      "LEV", "LEIVASY",
      "LWB", "LEWISBURG",
      "MAR", "MARLINTON",
      "MAX", "MAXWELTON",
      "MBG", "MEADOWBRIDGE",
      "NAL", "NALLEN",
      "QUI", "QUINWOOD",
      "RAI", "RAINELLE",
      "REN", "RENICK",
      "RIC", "RICHWOOD",
      "RON", "RONCEVERTE",
      "RUP", "RUPERT",
      "SIN", "SINKSGROVE",
      "SMT", "SMOOT",
      "UNI", "UNION",
      "WIL", "WILLIAMSBURG",
      "WSS", "WHITE SULPHUR SPRINGS"
  });
}
