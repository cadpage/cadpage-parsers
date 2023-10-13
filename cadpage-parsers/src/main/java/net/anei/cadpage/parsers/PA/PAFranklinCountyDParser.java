package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class PAFranklinCountyDParser extends FieldProgramParser {

  public PAFranklinCountyDParser() {
    super("FRANKLIN COUNTY", "PA",
          "RETONE? CALL/S ADDR CITY! END");
  }

  @Override
  public String getFilter() {
    return "noreply@ispyfire.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("iSpyFire Message")) return false;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("RETONE")) return new CallField("RETONE:|UPDATED ADDRESS!", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CITY")) return new MyCityField();
    return super.getField(name);
  }

  private static final Pattern APT_PTN = Pattern.compile("(?:APT|RM|ROOM|LOT) +(.*)|\\d{1,4}[A-Z]?|[A-Z]", Pattern.CASE_INSENSITIVE);
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      for (String part : field.split(";")) {
        part = part.trim();
        if (data.strAddress.isEmpty()) {
          parseAddress(part, data);
        } else {
          Matcher match = APT_PTN.matcher(part);
          if (match.matches()) {
            String tmp = match.group(1);
            if (tmp != null) part = tmp;
            data.strApt = append(data.strApt, "-", part);
          } else {
            if (data.strPlace.isEmpty()) {
              data.strPlace = part;
            } else {
              data.strApt = append(data.strApt, "-", part);
            }
          }
        }
      }
    }

    @Override
    public String getFieldNames() {
      return "ADDR PLACE APT INFO";
    }
  }

  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, " BORO");
      super.parse(field, data);
    }
  }
}
