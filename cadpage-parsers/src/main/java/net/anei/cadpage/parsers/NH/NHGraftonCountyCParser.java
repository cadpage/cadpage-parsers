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

  private static final Pattern LEAD_ID_PTN = Pattern.compile("(\\d{4}-\\d{6}|\\d{7})(?: [\\[\\(]P#:\\d\\d-\\d{6}[\\)\\]])?\n");
  private static final Pattern GPS_PTN = Pattern.compile("(.*?)\\(((?:[-+]?\\d+\\.\\d{4,}|0), *(?:[-+]?\\d+\\.\\d{4,}|0))\\)");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {

    if (!body.startsWith("Notification from ")) {
      String id = null;
      Matcher match = LEAD_ID_PTN.matcher(body);
      if (match.lookingAt()) {
        id = match.group(1);
        body = body.substring(match.end());
      }

      int pt = body.indexOf('\n');
      if (pt < 0) return false;
      String head = body.substring(0, pt);
      String tail = body.substring(pt);
      match = GPS_PTN.matcher(head);
      if (match.matches()) head = match.group(1) + ", " + match.group(2);
      if (id != null) head = head + ' ' + id;
      body = head + tail;

      body = "Notification from CIS:\n" + body;
    }

    int pt = subject.indexOf('|');
    if (pt >= 0) subject = subject.substring(0,pt).trim();
    return super.parseMsg(subject, body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    return super.getField(name);
  }

  private static final Pattern TRAIL_ST_PTN = Pattern.compile("(.*),? (NH|VT)\\b(?! *ROUTE|[- ]\\d{2,3}\\b)(?: \\d{5}(?:-\\d{4})?)?\\\\?(.*)");
  protected class MyAddressCityField extends BaseAddressCityField {
    @Override
    public void parse(String field, Data data) {

      // They add a trailing extraneous (and often wrong) NH state code
      String state = "";
      while (true) {
        Matcher match = TRAIL_ST_PTN.matcher(field);
        if (!match.matches()) break;
        field = stripFieldEnd(match.group(1), ",") + match.group(3);
        state = match.group(2);
      }

      super.parse(field, data);

      if (data.strState.isEmpty()) data.strState = state;
    }
  }
}
