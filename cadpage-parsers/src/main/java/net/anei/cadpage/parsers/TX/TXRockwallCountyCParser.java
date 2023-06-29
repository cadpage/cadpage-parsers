package net.anei.cadpage.parsers.TX;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH04Parser;

public class TXRockwallCountyCParser extends DispatchH04Parser {

  public TXRockwallCountyCParser() {
    super("ROCKWALL COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "@rockwall.com";
  }

  private static final Pattern APT_PTN = Pattern.compile(" +(?:APT|RM|ROOM|STE|LOT)\\b", Pattern.CASE_INSENSITIVE);

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!super.parseHtmlMsg(subject, body, data)) return false;
    if (!data.strApt.isEmpty()) {
      Matcher match = APT_PTN.matcher(data.strCity);
      if (match.find()) {
        data.strCity = data.strCity.substring(0,match.start());
      } else {
        data.strCity = stripFieldEnd(data.strCity, ' '+data.strApt);
      }
    }
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("INFO_BLK")) return new MyInfoBlockField();
    return super.getField(name);
  }

  protected class MyInfoBlockField extends BaseInfoBlockField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("This email ")) return;
      super.parse(field,  data);
    }
  }
}
