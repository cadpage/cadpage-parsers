package net.anei.cadpage.parsers.OK;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH04Parser;

public class OKCarterCountyAParser extends DispatchH04Parser {

  public OKCarterCountyAParser() {
    super("CARTER COUNTY", "OK");
    setupCities(OKCarterCountyParser.CITY_LIST);
  }

  @Override
  public String getFilter() {
    return "cadams@ardmorecity.org";
  }

  @Override
  protected int getExtraParseAddressFlags() {
    return FLAG_RECHECK_APT;
  }

  private static final Pattern TRAIL_APT_PTN = Pattern.compile("(?:APT|RM|UNIT)\\.*$");

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!super.parseHtmlMsg(subject, body, data)) return false;

    // Apartment info has been appended to the city field.  Sad...
    String city = CITY_SET.getCode(data.strCity);
    if (city != null) {
      data.strCity = city;
    } else {
      data.strCity = stripFieldEnd(data.strCity, data.strApt);
      data.strCity = TRAIL_APT_PTN.matcher(data.strCity).replaceFirst("");
    }
    return true;
  }

  private static final CodeSet CITY_SET = new CodeSet(OKCarterCountyParser.CITY_LIST);
}
