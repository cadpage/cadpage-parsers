package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.MsgInfo.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgParser;

public class ALGenevaCountyCParser extends MsgParser {

  public ALGenevaCountyCParser() {
    super("GENEVA COUNTY", "AL");
    setFieldList("DATE TIME CALL ADDR APT CITY ST INFO UNIT");
  }

  @Override
  public String getFilter() {
    return "cad@34central.com";
  }

  private static final Pattern LEAD_DATE_TIME_PTN = Pattern.compile("(\\d\\d/\\d\\d/\\d\\d) (\\d\\d:\\d\\d) +");
  private static final Pattern TRAIL_UNIT_PTN = Pattern.compile(" +(?:[A-Z0-9]+; *)*[A-Z0-9]+$");
  private static final Pattern UNIT_DELIM_PTN = Pattern.compile(" *; *");
  private static final Pattern MSG_MARKER_PTN = Pattern.compile(";? \\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - message - ");
  private static final Pattern ST_ZIP_PTN = Pattern.compile("([A-Z]{2})(?: +(\\d{5}))?");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    data.strCall = subject;

    Matcher match = LEAD_DATE_TIME_PTN.matcher(body);
    if (match.lookingAt()) {
      data.strDate = match.group(1);
      data.strTime = match.group(2);
      body = body.substring(match.end());
      match = TRAIL_UNIT_PTN.matcher(body);
      if (!match.find()) return false;
      data.strUnit = UNIT_DELIM_PTN.matcher(match.group().trim()).replaceAll(" ");
      body = body.substring(0, match.start());
    }

    String[] parts = MSG_MARKER_PTN.split(body);
    if (parts.length < 2) {
      if (!parts[0].endsWith(" None")) return false;
      parts[0] =  parts[0].substring(0, parts[0].length()-5).trim();
    }

    boolean first = true;
    for (String part : parts) {
      part = part.trim();
      if (first) {
        first = false;
        Parser p = new Parser(part);
        String city = p.getLastOptional(',');
        String zip = null;
        match = ST_ZIP_PTN.matcher(city);
        if (match.matches()) {
          data.strState = match.group(1);
          zip = match.group(2);
          city = p.getLastOptional(',');
        }
        if (city.length() == 0 && zip != null) city = zip;
        data.strCity = city;
        String addr = p.get().replace("//", "@").replace('/', '@');
        addr = addr.replaceAll("GEN BAR LN", "GENBAR LN");
        parseAddress(addr, data);
      }
      else {
        data.strSupp = append(data.strSupp, "\n", part);
      }
    }
    return true;
  }
}
