package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class OHHarrisonCountyParser extends MsgParser {

  public OHHarrisonCountyParser() {
    super("HARRISON COUNTY", "OH");
    setFieldList("CALL ADDR APT CITY ST INFO");
  }

  @Override
  public String getFilter() {
    return "HarrisonCoRunReports@outlook.com,hcsoreports@harrisoncountyohio.gov";
  }

  private static final Pattern MASTER = Pattern.compile("(.*?) (?:None|\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - +(.*))");
  private static final Pattern ST_ZIP_PTN = Pattern.compile("([A-Z]{2})(?: +(\\d{5}))?");
  private static final Pattern INFO_BRK_PTN = Pattern.compile("[; ]+\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - +");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    data.strCall = subject;
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    String addr = match.group(1);
    String info = match.group(2);

    Parser p = new Parser(addr);
    String city = p.getLastOptional(',');
    String zip = null;
    match = ST_ZIP_PTN.matcher(city);
    if (match.matches()) {
      data.strState = match.group(1);
      zip = match.group(2);
      city = p.getLastOptional(',');
    }
    if (city.isEmpty() && zip != null) city = zip;
    data.strCity = city;
    parseAddress(p.get(), data);

    if (info != null) {
      data.strSupp = INFO_BRK_PTN.matcher(info).replaceAll("\n");
    }

    return true;
  }
}
