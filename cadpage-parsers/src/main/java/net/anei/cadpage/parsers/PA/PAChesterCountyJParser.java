/*
Chester County, PA
*/

package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;

public class PAChesterCountyJParser extends PAChesterCountyBaseParser{

  public PAChesterCountyJParser() {
    super("CALL:ADDR/S! ( XST:X! TYPE:CALL! DATETIME! UNIT " +
                       "| CROSS:X! TYPE:CALL! DATE:DATETIMEUNIT! " +
                       ") INFO:INFO/N+");
  }

  @Override
  public String getFilter() {
    return "messaging@iamresponding.com";
  }

  private static final Pattern MARKER = Pattern.compile("(?:FIRE|MEDICAL) (CALL:)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("AVNF")) return false;
    Matcher match = MARKER.matcher(body);
    if (!match.lookingAt()) return false;
    body = body.substring(match.start(1));
    if (!parseFields(body.split("\n"), data)) return false;
    if (data.strAddress.isEmpty() && !data.strCross.isEmpty()) {
      parseAddress(data.strCross, data);
      data.strCross = "";
    }
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d +\\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("DATETIMEUNIT")) return new MyDateTimeUnitField();
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.toUpperCase().indexOf(" ALIAS ");
      if (pt >= 0)  field = field.substring(0,pt).trim();

      pt = field.indexOf('@');
      if (pt >= 0) {
        data.strPlace = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
        field = stripFieldEnd(field, ":");
      }

      if (field.startsWith("/")) {
        data.strCity = convertCodes(field.substring(1).trim(), CITY_CODES);
      } else {
        super.parse(field, data);
      }
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }

  private static final Pattern DATE_TIME_UNIT_PTN = Pattern.compile("(\\d\\d/\\d\\d/\\d\\d) +(\\d\\d?:\\d\\d:\\d\\d)(?: +(\\S+))?");

  private class MyDateTimeUnitField extends Field {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = DATE_TIME_UNIT_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strDate = match.group(1);
      data.strTime = match.group(2);
      data.strUnit = getOptGroup(match.group(3));
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "DATE TIME UNIT";
    }
  }
}

