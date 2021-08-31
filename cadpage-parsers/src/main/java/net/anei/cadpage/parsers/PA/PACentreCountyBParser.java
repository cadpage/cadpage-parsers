package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class PACentreCountyBParser extends FieldProgramParser {

  public PACentreCountyBParser() {
    super("CENTRE COUNTY", "PA",
          "Box:BOX_CALL! CALL? ADDRCITY! PLACE APT NAME Name:NAME ( Due:UNIT | Number:ID ) PLACE APT NAME END");
  }

  @Override
  public String getFilter() {
    return "alerts@centre.ealert911.com,dispatch@centrecountypa.gov,Centre County Alerts";
  }

  private static final Pattern HTTP_PTN = Pattern.compile("[ \n]http:");
  private static final Pattern PREFIX_PTN = Pattern.compile("(?:FD|EMS) (?=Box:)");
  private static final Pattern FIX_CITY_PTN = Pattern.compile("(?:BL(?!AIR)|CT|CF|HT|UN(?!ION)) *(.*)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = HTTP_PTN.matcher(body);
    if (match.find()) body = body.substring(0,match.start()).trim();

    match = PREFIX_PTN.matcher(body);
    if (match.lookingAt()) body = body.substring(match.end());

    body = stripFieldEnd(body, ".");
    body = body.replace(" Due:", "\nDue:");
    if (!parseFields(body.split("\n"), data)) return false;
    data.strCity = stripFieldEnd(data.strCity, " 0");
    match = FIX_CITY_PTN.matcher(data.strCity);
    if (match.matches()) data.strCity = match.group(1);
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("BOX_CALL")) return new MyBoxCallField();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("APT")) return new AptField("(?:APT|LOT|ROOM|RM|SUITE|UNIT) *(.*)|(.*)", true);
    if (name.equals("NAME")) return new MyNameField();
    return super.getField(name);
  }

  private static final Pattern BOX_CALL_PTN = Pattern.compile("(\\d{2,})(?: +(.*))?");
  private class MyBoxCallField extends Field {

    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      Matcher match = BOX_CALL_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strBox = match.group(1);
      data.strCall = getOptGroup(match.group(2));
    }

    @Override
    public String getFieldNames() {
      return "BOX CALL";
    }

  }

  private class MyCallField extends CallField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (data.strCall.length() > 0) return false;
      super.parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private class MyNameField extends NameField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, ",");
      super.parse(field, data);
    }
  }
}
