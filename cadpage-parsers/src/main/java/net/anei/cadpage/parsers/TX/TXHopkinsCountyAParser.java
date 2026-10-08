package net.anei.cadpage.parsers.TX;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.MsgParser;

public class TXHopkinsCountyAParser extends MsgParser {

  public TXHopkinsCountyAParser() {
    super("HOPKINS COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "truckavl@hchdems.com";
  }

  private static final Pattern RUN_RPT_PTN = Pattern.compile("(In Queue:) *(.*?) +(Dispatched:) *(.*?) +(Responding:) *(.*?) +(Staged:) *(.*?) +(At Scene:) *(.*?) +(Cancelled:) *(.*?) +MIN# (\\d+)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("VisiCAD Page")) return false;
    if (body.startsWith("In Queue:")) {
      setFieldList("INFO ID");
      data.msgType = MsgType.RUN_REPORT;
      Matcher match = RUN_RPT_PTN.matcher(body);
      if (!match.matches()) return false;
      StringBuilder sb = new StringBuilder();
      for (int jj = 1; jj < 13; jj+=2) {
        String time = match.group(jj+1);
        if (!time.isEmpty()) {
          if (!sb.isEmpty()) sb.append('\n');
          sb.append(match.group(jj));
          sb.append(' ');
          sb.append(time);
        }
      }
      data.strSupp = sb.toString();
      data.strCallId = match.group(13);
      return true;
    }

    else {
      setFieldList("UNIT PRI CALL ADDR APT CITY ID PHONE");
      FParser fp = new FParser(body);
      data.strUnit = fp.get(10).replace(" ", "");
      if (!fp.check(" respond code ")) return false;
      data.strPriority = fp.get(1);
      if (!fp.check(" for ")) return false;
      data.strCall = fp.get(30);
      if (!fp.check(" at ")) return false;
      parseAddress(fp.get(81), data);
      data.strApt = append(data.strApt, "-", fp.get(10));
      if (!fp.check(", ")) return false;
      data.strCity = fp.get(20);
      if (!fp.check(", Assigned Vehicle Location: ")) return false;
      fp.skip(80);
      if (!fp.check("Assigned Vehicle Latitude: ")) return false;
      fp.skip(8);
      if (!fp.check("  Assigned Vehicle Longitude: ")) return false;
      fp.skip(8);
      if (!fp.check("  Master Response Number: ")) return false;
      data.strCallId = fp.get(20);
      if (!fp.check("Truck Station:")) return false;
      fp.skip(30);
      if (!fp.check(" Call Back Number:")) return false;
      data.strPhone = fp.get();
      return true;
    }
  }
}
