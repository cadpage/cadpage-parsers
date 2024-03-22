package net.anei.cadpage.parsers.OR;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.HtmlProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ORUmatillaCountyBParser extends HtmlProgramParser {

  public ORUmatillaCountyBParser() {
    super("UMATILLA COUNTY", "OR",
          "CALL_DATE_/_TIME:DATETIME! FIRE_CALL_TYPE:CALL! EMS_CALL_TYPE:CALL! COMMON_NAME:PLACE! ADDRESS:ADDRCITY/S6! APT/SUITE:APT! CROSS_STREET:X! FIRE_QUAD:MAP! NATURE_OF_CALL:CALL/SDS! DISPATCH_COMMENTS:EMPTY! INFO/N+ LOCAL_INFO:INFO/N! INFO/N+ ALERTS:INFO/N! INFO/N+ INCIDENT_#:ID! UNITS:UNIT! STATUS_TIMES:EMPTY! SKIP+ CALLER_NAME:NAME! CALLER_PHONE_#:PHONE! CALLER_ADDRESS:SKIP!");
  }

  @Override
  public String getFilter() {
    return "@milton-freewater-or.gov,RipnRunReport@wallawallawa.gov";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!subject.startsWith("Automatic R&R Notification:")) return false;
    return super.parseHtmlMsg(subject, body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyAptField extends AptField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals(data.strApt)) return;
      super.parse(field,  data);;
    }
  }

  private static final Pattern INFO_HEADER_PTN = Pattern.compile("\\*{3}\\d\\d?/\\d\\d?/\\d{4}\\*{3}|\\d\\d?:\\d\\d:\\d\\d");
  private class MyInfoField extends InfoField {

    private boolean suppressHeader = false;

    @Override
    public void parse(String field, Data data) {
      if (!suppressHeader) {
        if (INFO_HEADER_PTN.matcher(field).matches()) {
          suppressHeader = true;
          return;
        }
      } else {
        if (field.equals("-")) suppressHeader = false;
        return;
      }

      data.strSupp = append(data.strSupp, "\n", field);
    }
  }
}
