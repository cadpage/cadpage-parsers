package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class OHGreeneCountyBParser extends DispatchH05Parser {

  public OHGreeneCountyBParser() {
    super("GREENE COUNTY", "OH",
          "Call:CALL! Name:NAME! Address:ADDRCITY! Cross:X! Units:UNIT! " +
               "( Incident_Number:ID! Call_Time:SKIP! Dispatch_Time:DATETIME! Quadrant:MAP! " +
               "| ID:SKIP! PRI:PRI! Date:DATETIME! Map:EMPTY! Fire_District:SRC! Incident_Numbers:ID! " +
               ") Alerts:ALERT! Narrative:EMPTY! INFO_BLK+");
  }

  @Override
  public String getFilter() {
    return "PSISN_Dispatch@ci.xenia.oh.us";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!subject.startsWith("Automatic R&R Notification:")) return false;
    return super.parseHtmlMsg(subject, body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  private static final Pattern CITY_APT_PTN = Pattern.compile("([- A-Z]+) +(.*\\d.*|[A-Z])");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String addr = p.get(',');
      String city = p.get(',');
      String apt = p.get();

      if (apt.isEmpty()) {
        Matcher match = CITY_APT_PTN.matcher(city);
        if (match.matches()) {
          city = match.group(1).trim();
          apt = match.group(2).trim();
          if (apt.endsWith("MM")) apt = "";
        }
      }

      if (!apt.isEmpty()) {
        addr = stripFieldEnd(addr, ' '+apt);
      }

      parseAddress(addr, data);
      data.strCity = city;
      data.strApt = append(data.strApt, "-", apt);
    }
  }
}
