package net.anei.cadpage.parsers.CO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class COSummitCountyParser extends FieldProgramParser {

  public COSummitCountyParser() {
    super("SUMMIT COUNTY", "CO",
        "TransactionId:SKIP! TransactionAction:SKIP! TransactionTime:SKIP! IncidentDate:DATETIME! IncidentNumber:ID! " +
              "IncidentTypeCode:CODE! IncidentTypeDescription:CALL! Priority:PRI! LocationAddress:ADDR! LocationName:PLACE! " +
              "City:CITY! Zip:ZIP! Latitude:GPS1! Longitude:GPS2! UTMDatum:SKIP! Area:MAP! Sector:MAP/L! Beat:MAP/L! " +
              "CallerType:SKIP! CallerGivenName:NAME! CallerMiddleName:NAME/S! CallerSurName:NAME/S! " +
              "CallerTelephoneAreaCode:PHONE! CallerTelephoneNumber:PHONE! CallerTelephonseSuffix:PHONE! " +
              "RadioChannel:CH! PrimaryUnit:SKIP! Units:UNIT! Comments:INFO! END", FLDPROG_XML);
  }

  @Override
  public String getFilter() {
    return "Active911Messenger@summit911.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern GPS_PTN = Pattern.compile("<([-+]?\\d{2,3}\\.\\d+)>");
  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!subject.equals("Summit911 Call for Service")) return false;
    body = GPS_PTN.matcher(body).replaceAll("$1");
    return super.parseHtmlMsg(subject, body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("PHONE")) return new MyPhoneField();
    return super.getField(name);
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d{4})-(\\d\\d)-(\\d\\d)T(\\d\\d:\\d\\d:\\d\\d)\\.\\d+-.*");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(2)+'/'+match.group(3)+'/'+match.group(1);
      data.strTime = match.group(4);
    }
  }

  private class MyPhoneField extends PhoneField {
    @Override
    public void parse(String field, Data data) {
      data.strPhone = data.strPhone + field;
    }
  }
}
