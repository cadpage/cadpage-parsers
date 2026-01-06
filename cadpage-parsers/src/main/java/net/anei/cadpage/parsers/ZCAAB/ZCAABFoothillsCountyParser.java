package net.anei.cadpage.parsers.ZCAAB;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class ZCAABFoothillsCountyParser extends DispatchA71Parser {

  public ZCAABFoothillsCountyParser() {
    super("FOOTHILLS COUNTY", "AB");
  }

  @Override
  public String getFilter() {
    return "officialm.outlaws@gmail.com,@de-winton.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace('\n', ' ');
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATE")) return new MyDateField();
    return super.getField(name);
  }

  private static final Pattern DATE_PTN = Pattern.compile("(\\d{4})/(\\d\\d/\\d\\d)");
  private class MyDateField extends DateField {
    @Override
    public void parse(String field, Data data) {
      if (field.isEmpty()) return;
      Matcher match = DATE_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(2)+'/'+match.group(1);
    }
  }
}
