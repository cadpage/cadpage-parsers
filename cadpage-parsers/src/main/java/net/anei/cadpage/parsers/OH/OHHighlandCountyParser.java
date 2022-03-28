package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;



public class OHHighlandCountyParser extends MsgParser {

  public OHHighlandCountyParser() {
    super("HIGHLAND COUNTY", "OH");
    setFieldList("CALL ADDR APT CITY ST INFO");
  }

  @Override
  public String getFilter() {
    return "centralsquare@highlandcoso.com";
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[; ]+\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");
  private static final Pattern STATE_ZIP_PTN = Pattern.compile("([A-Z]{2})(?: +(\\d{5}))?");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    data.strCall = subject;

    if (body.endsWith(" None")) {
      body = body.substring(0,body.length()-5).trim();
    } else {
      String[] parts = INFO_BRK_PTN.split(body);
      if (parts.length == 1) return false;
      body = parts[0];
      for (int ndx = 1; ndx < parts.length; ndx++) {
        data.strSupp = append(data.strSupp, "\n", parts[ndx].trim());
      }
    }

    Parser p = new Parser(body);
    String city = p.getLastOptional(',');
    Matcher match = STATE_ZIP_PTN.matcher(city);
    if (match.matches()) {
      data.strState = match.group(1);
      String zip = match.group(2);
      city = p.getLastOptional(',');
      if (city.isEmpty() && zip != null) city = zip;
    }
    data.strCity = city;
    parseAddress(p.get().replace('|', '&'), data);

    return true;
  }
}
