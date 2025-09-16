package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;

public class PAChesterCountyPParser extends PAChesterCountyBaseParser {

  public PAChesterCountyPParser() {
    super("LOCATION:ADDR! XST:X! TYPE:CALL! DATE:DATETIME! DG:CH! LOC_INFO:PLACE END");
  }

  @Override
  public String getFilter() {
    return "desipage@chesco.org,no-reply@cadnetcc.net";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("DES Page Notification")) return false;
    body = stripFieldEnd(body, " |");
    if (!parseFields(body.split(" \\| "), data)) return false;
    if (data.strAddress.length() == 0) {
      data.strAddress = "";
      parseAddress(data.strCross, data);
      data.strCross = "";
    }
    if (data.strCross.equals("&")) data.strCross = "";
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d +\\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  private static final Pattern ADDR_CITY_ST_PTN = Pattern.compile("(.*?) *\\b([A-Z]+) (?:CHEST|BERKS|CECIL|DELAW|LANCA|MONTG|NEWCA) (DE|MD|PA):?");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(": ");
      if (pt >= 0) {
        data.strPlace = stripFieldStart(field.substring(pt+2).trim(), "@");
        field = field.substring(0,pt).trim();
      }
      Matcher match = ADDR_CITY_ST_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strCity = convertCodes(match.group(2), CITY_CODES);
        data.strState = match.group(3);
      }

      parseAddress(field, data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CITY ST PLACE";
    }
  }
}
