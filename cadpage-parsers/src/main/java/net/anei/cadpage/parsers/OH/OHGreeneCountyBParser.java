package net.anei.cadpage.parsers.OH;

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

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      parseAddress(p.get(','), data);
      data.strCity = p.get(',');
      data.strApt = append(data.strApt, "-", p.get());
    }
  }
}
