package net.anei.cadpage.parsers.OH;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchPrintrakParser;

/**
 * Warren County, OH
 */
public class OHWarrenCountyAParser extends DispatchPrintrakParser {

  private static final Pattern SPECIAL_PTN = Pattern.compile("Sent by WCDES ([A-Z0-9]+) *\n");
  private static final Pattern DATE_TIME_PTN = Pattern.compile("\n[A-Z][a-z]{2} ([A-Z][a-z]{2} \\d+) (\\d\\d:\\d\\d:\\d\\d) (\\d{4})$");
  private static final DateFormat DATE_FMT = new SimpleDateFormat("MMM dd yyyy");

  public OHWarrenCountyAParser() {
    super("WARREN COUNTY", "OH");
  }

  @Override
  public String getFilter() {
    return "WCPSN@wcoh.net,Notifications@wcoh.net,Mark.Greatorex@htfire.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = SPECIAL_PTN.matcher(body);
    if (match.lookingAt()) {
      setFieldList("UNIT CALL ADDR INFO DATE TIME");
      data.strUnit = match.group(1);
      body = body.substring(match.end()).trim();

      match = DATE_TIME_PTN.matcher(body);
      if (match.find()) {
        setDate(DATE_FMT, match.group(1) + ' ' + match.group(3), data);
        data.strTime = match.group(2);
        body = body.substring(0,match.start());
      }

      int pt = body.indexOf('\n');
      if (pt >= 0) {
        data.strCall = body.substring(0,pt).trim();
        body = body.substring(pt+1).trim();
      } else {
        data.strCall = "WCTRT ALERT";
      }
      data.strSupp = body;
      return true;
    }
    else {
      return super.parseMsg(body, data);
    }
  }
}
