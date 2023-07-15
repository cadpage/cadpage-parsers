package net.anei.cadpage.parsers.NH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

public class NHGraftonCountyCParser extends DispatchA27Parser {

  public NHGraftonCountyCParser() {
    this("GRAFTON COUNTY", "NH");
  }

  public NHGraftonCountyCParser(String defCity, String defState) {
    super(NHGraftonCountyParser.CITY_LIST, defCity, defState);
  }

  @Override
  public String getFilter() {
    return "notification@nhpd.cloud,Dispatch@lebanonnh.gov";
  }

  private static final Pattern LEAD_ID_PTN = Pattern.compile("(\\d{4}-\\d{6})(?: [\\[\\(]P#:\\d\\d-\\d{6}[\\)\\]])?\n");
  private static final Pattern GPS_PTN = Pattern.compile("(.*?)\\(((?:[-+]?\\d+\\.\\d{4,}|0), *(?:[-+]?\\d+\\.\\d{4,}|0))\\)");
  private static final Pattern DOUBLE_STATE_PTN = Pattern.compile("(, (?:NH|VT)), NH\\b");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {

    int pt = subject.lastIndexOf('|');
    if (pt >= 0)subject = subject.substring(pt+1).trim();

    if (body.startsWith("<h4 ")) {
      pt = body.indexOf('>');
      body = body.substring(pt+1).trim().replace("</h4>", "");
      if (body.startsWith("[")) {
        pt = body.indexOf(']');
        if (pt < 0) return false;
        subject = body.substring(1,pt).trim();
        body = body.substring(pt+1).trim();
      }
      body = stripFieldEnd(body, "\n\nCSI TECHNOLOGY GROUP");
    }
    String src = null;
    if (subject.startsWith("CFS Notification - ")) src = subject.substring(19).trim();
    else if (subject.endsWith(" - CFS Notification")) src = subject.substring(0,subject.length()-19).trim();
    if (src != null) {
      String id = null;
      Matcher match = LEAD_ID_PTN.matcher(body);
      if (match.lookingAt()) {
        id = match.group(1);
        body = body.substring(match.end());
      }

      pt = body.indexOf('\n');
      if (pt < 0) return false;
      String head = body.substring(0, pt);
      String tail = body.substring(pt);
      match = GPS_PTN.matcher(head);
      if (match.matches()) head = match.group(1) + ", " + match.group(2);
      if (id != null) head = head + ' ' + id;
      body = head + tail;

      body = "Notification from " + src + ":\n" + body;
      body = body.replace("\nUnit(s) responed:", "\nUnit(s) responded:");
    }

    body = DOUBLE_STATE_PTN.matcher(body).replaceFirst("$1");
    if (!super.parseMsg(subject, body, data)) return false;

    if (data.strApt.equals("NH") && data.strCity.isEmpty()) {
      data.strState = data.strApt;
      data.strApt = "";
      String addr = data.strAddress;
      if (addr.endsWith(" NH") || addr.endsWith(" VT")) {
        data.strState = addr.substring(addr.length()-2);
        addr = addr.substring(0,addr.length()-3).trim();
      }
      addr = stripFieldEnd(addr, ",");
      data.strAddress = "";
      parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, addr, data);
    }
    return true;
  }
}
