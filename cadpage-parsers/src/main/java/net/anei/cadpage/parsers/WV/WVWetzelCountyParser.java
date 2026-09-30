package net.anei.cadpage.parsers.WV;

import net.anei.cadpage.parsers.MsgInfo.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgParser;

public class WVWetzelCountyParser extends MsgParser {

  public WVWetzelCountyParser() {
    super("WETZEL COUNTY", "WV");
    setFieldList("CALL ADDR APT CITY ST DATE TIME ID");
  }

  @Override
  public String getFilter() {
    return "no-reply@zuercherportal.com,wcemt9@hotmail.com,@wetzelwv.com,no-reply@csprosuite.centralsquarecloudgov.com";
  }

  private static final Pattern MASTER = Pattern.compile("(.*?) (\\d\\d/\\d\\d/\\d\\d) (\\d\\d:\\d\\d:\\d\\d)[ \\*]+(CFS\\d+)");
  private static final Pattern ST_ZIP_PTN = Pattern.compile("([A-Z]{2})(?: (\\d+))?");
  private static final Pattern ZIP1_PTN = Pattern.compile("[-+]?\\d{2,3}\\.\\d+");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    data.strCall = subject;
    if (data.strCall.length() == 0) data.strCall = "ALERT";

    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    String addr = match.group(1).trim();
    data.strDate = match.group(2);
    data.strTime = match.group(3);
    data.strCallId = match.group(4);

    Parser p = new Parser(addr);
    String city = p.getLastOptional(',');
    String zip = null;
    match = ST_ZIP_PTN.matcher(city);
    if (match.matches()) {
      data.strState = match.group(1);
      zip = match.group(2);
      city = p.getLastOptional(',');
    }
    addr = p.get();
    if (ZIP1_PTN.matcher(city).matches()) {
      addr = append(addr, ", ", city);
      city = "";
    }

    if (city.length() == 0 && zip != null && zip.length() == 5) city = zip;
    if (!city.equals(data.strState)) data.strCity = city;

    parseAddress(addr, data);

    return true;
  }

}
