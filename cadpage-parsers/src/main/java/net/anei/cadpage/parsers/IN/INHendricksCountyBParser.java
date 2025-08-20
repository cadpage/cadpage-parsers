package net.anei.cadpage.parsers.IN;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH03Parser;

public class INHendricksCountyBParser extends DispatchH03Parser {

  public INHendricksCountyBParser() {
    super("HENDRICKS COUNTY", "IN");
    setupCities(CITY_CODES);
  }

  @Override
  public String getFilter() {
    return "cchapman@hccom.org";
  }

  private static final Pattern MISSING_BRK_PTN =
      Pattern.compile(" +(?=INCIDENT DETAILS|---------|(?:(?<!RESPONSE )LOCATION|Location|Loc Name|Loc Descr|City|Building|Subdivision|Floor|Apt/Unit|Zip Code|Cross Strs|Area|Sector|Beat|Map Book|INCIDENT|Inc #|Priority|Inc Type|(?<!Loc )Descr|Mod Circum|Created|Caller|Phone|SECONDARY RESPONSE LOCATION|UNITS DISPATCHED|PERSONNEL DISPATCHED|COMMENTS|PREMIS HAZARD):)");
  private static final Pattern MISSING_BRK_PTN2 = Pattern.compile("(?<=SECOONDARY RESPONSE LOCATION|UNITS DISPATCHED|PERSONNEL DISPATCHED|COMMENTS) *");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    body = MISSING_BRK_PTN.matcher(body).replaceAll("\n");
    body = MISSING_BRK_PTN2.matcher(body).replaceAll("\n");
    return super.parseMsg(subject, body, data);
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "NSL", "NORTH SALEM"
  });

}
