package net.anei.cadpage.parsers.OK;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OKGradyCountyParser extends FieldProgramParser {

  public OKGradyCountyParser() {
    super("GRADY COUNTY", "OK",
          "CFS:ID! CALLTYPE:CALL! PRIORITY:PRI! PLACE:PLACE! ADDRESS:ADDR! CITY:CITY! STATE:ST! ZIP:ZIP! DATE:DATE! TIME:TIME! UNIT:UNIT! INFO:INFO! INFO/N+ NAME:NAME ADDRESS:SKIP PHONE:PHONE CONTACT:CONTACT INFO/N+ ALERT:ALERT!");
  }

  @Override
  public String getFilter() {
    return "noreplycad@cityoftuttle.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!parseFields(body.split("\n"), data)) return false;
    data.strAddress = stripFieldEnd(data.strAddress, " OK");
    data.strAddress = stripFieldEnd(data.strAddress, ' ' + data.strCity);
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATE")) return new DateField("\\d\\d?/\\d\\d?/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("CONTACT")) return new ContactField();
    return super.getField(name);
  }

  private static final Pattern CONTACT_PTN = Pattern.compile("[YN]\\b[, ]*");
  private class ContactField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CONTACT_PTN.matcher(field);
      if (match.lookingAt()) field = field.substring(match.end());
      data.strSupp = append(data.strSupp, "\n", field);
    }
  }
}
