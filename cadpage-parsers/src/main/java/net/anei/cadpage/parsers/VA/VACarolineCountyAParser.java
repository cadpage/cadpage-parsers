package net.anei.cadpage.parsers.VA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class VACarolineCountyAParser extends MsgParser {

  public VACarolineCountyAParser() {
    super("CAROLINE COUNTY", "VA");
    setFieldList("ADDR CITY ST PLACE GPS UNIT CALL ID DATE TIME INFO");
  }

  @Override
  public String getFilter() {
    return "cad-rms@co.caroline.va.us";
  }

  private static final Pattern MASTER = Pattern.compile("(.*?) ((?:None|[-+]?\\d{2,3}\\.\\d{6}) / (?:None|[-+]?\\d{2,3}\\.\\d{6})) (.*?) //// (.*?) (\\d\\d/\\d\\d/\\d\\d) (\\d\\d:\\d\\d)\\b *(.*)");
  private static final Pattern TRAIL_BSL_PTN = Pattern.compile("\\\\+$");
  private static final Pattern ST_ZIP_PLACE_PTN = Pattern.compile("([A-Z]{2})(?: +(\\d{5}))?(?: +(.*))?");
  private static final Pattern UNIT_DELIM_PTN = Pattern.compile(" *; *");
  private static final Pattern CALL_ID_PTN = Pattern.compile("(.*) (CCFR\\d+)");
  private static final Pattern INFO_SPLIT_PTN = Pattern.compile("[; ]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");

  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;

    String addr = match.group(1).trim();
    setGPSLoc(match.group(2).replace('/', ','), data);
    data.strUnit = UNIT_DELIM_PTN.matcher(match.group(3).trim()).replaceAll(",");
    String call = match.group(4).trim();
    data.strDate = match.group(5);
    data.strTime = match.group(6);
    String info = match.group(7).trim();

    addr = TRAIL_BSL_PTN.matcher(addr).replaceFirst("");
    Parser p = new Parser(addr);
    String city = p.getLastOptional(',');
    match = ST_ZIP_PLACE_PTN.matcher(city);
    if (match.matches()) {
      data.strState = match.group(1);
      String zip = match.group(2);
      data.strPlace = getOptGroup(match.group(3));
      city = p.getLastOptional(',');
      if (city.isEmpty() && zip != null) city = zip;
    }
    data.strCity = city;
    parseAddress(p.get(), data);

    if (! data.strCall.equals("None")) {
      match = CALL_ID_PTN.matcher(call);
      if (match.matches()) {
        call = match.group(1).trim();
        data.strCallId = match.group(2);
      }
      data.strCall = call;
    }

    for (String line : INFO_SPLIT_PTN.split(info)) {
      if (line.isEmpty()) continue;
      if (data.strCall.isEmpty()) {
        data.strCall = line;
      } else {
        data.strSupp = append(data.strSupp, "\n", line);
      }
    }

    return true;
  }
}