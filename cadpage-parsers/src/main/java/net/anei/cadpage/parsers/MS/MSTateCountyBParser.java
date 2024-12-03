package net.anei.cadpage.parsers.MS;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MSTateCountyBParser extends FieldProgramParser {

  public MSTateCountyBParser() {
    super("TATE COUNTY", "MS",
          "CAD#:ID! Unit:UNIT! Date:DATE! Time:TIME! Addr:ADDRCITYST! Type:CODE! Caller:NAME Phone:PHONE! Remarks:INFO! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "cadpage@tate.adsisoftware.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD Alert Recieved")) return false;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATE")) return new DateField("\\d\\d?/\\d\\d/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    if (name.equals("ADDRCITYST")) return new MyAddressCityStateField();
    return super.getField(name);
  }

  private static final Pattern CITY_ST_PTN = Pattern.compile("(.*) ([A-Z]{2})");
  private class MyAddressCityStateField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      super.parse(field, data);
      Matcher match = CITY_ST_PTN.matcher(data.strCity);
      if (match.matches()) {
        data.strCity = match.group(1).trim();
        data.strState = match.group(2);
      }
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " ST";
    }
  }
}
