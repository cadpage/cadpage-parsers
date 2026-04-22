package net.anei.cadpage.parsers.TX;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA72Parser;

public class TXRefugioCountyParser extends DispatchA72Parser {

  public TXRefugioCountyParser() {
    super("REFUGIO COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "texasrefugio@gmail.com";
  }

  private static final Pattern PLACE_CITY_PTN = Pattern.compile("(.*?)\\b(AUSTWELL|BAYSIDE|REFUGIO|REFGUIO|WOODSBORO|TIVOLI)", Pattern.CASE_INSENSITIVE);

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    Matcher match = PLACE_CITY_PTN.matcher(data.strCity);
    if (match.matches()) {
      data.strPlace = append(data.strPlace, " - ", match.group(1).trim());
      data.strCity = match.group(2);
    } else if ((match = PLACE_CITY_PTN.matcher(data.strApt)).matches()) {
      data.strApt = match.group(1).trim();
      data.strCity = match.group(2);
    } else if ((match = PLACE_CITY_PTN.matcher(data.strAddress)).matches()) {
      data.strAddress = match.group(1).trim();
      data.strCity = match.group(2);
    }
    if (data.strCity.equals("REFGUIO")) data.strCity = "REFUGIO";
    return true;
  }
}
