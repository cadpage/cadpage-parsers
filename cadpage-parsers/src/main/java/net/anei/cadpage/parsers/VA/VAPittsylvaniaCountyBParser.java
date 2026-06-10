package net.anei.cadpage.parsers.VA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class VAPittsylvaniaCountyBParser extends FieldProgramParser {

  public VAPittsylvaniaCountyBParser() {
    super("PITTSYLVANIA COUNTY", "VA",
          "AgencyCode:SKIP! ReportNumber:ID! CadInciNumber:ID/L! NatureCode:CODE! NatureDescription:CALL! OriginalNatureCode:SKIP! StreetName:SKIP! StreetType:SKIP! " +
          "FullAddress:ADDR! CityCode:SKIP! CityCodeDescription:CITY! State:ST! EventLatitude:GPS1! EventLongitude:GPS2! DMSLatitude:SKIP! DMSLongitude:SKIP! " +
          "EventStartTime:SKIP! E911CallReceived:SKIP! FirstDispatchTime:DISPATCH_TIME! FirstEnrouteTime:ENROUTE_TIME! FirstOnSceneTime:ONSCENE_TIME! " +
          "UnderControlTime:UNDER_CONTROL_TIME! LastUnitClearTime:CLEAR_TIME! FirstTransportTime:TRANSPORT_TIME! LastTransportArriveTime:TRANSPORT_ARRIVE_TIME! EventCompletedTime:COMPLETED_TIME! " +
          "LastExportTime:SKIP! EventStation:SRC! CadResponsePlan:BOX! CallTakerUserId:SKIP! DispatcherUserId:SKIP! FirstAgencyArriveTime:SKIP! CadNotes:INFO! " +
          "PrimaryUnit:SKIP! ReclassifiedNatureCode:SKIP! ReclassifiedNatureDescription:SKIP! CrossStreet1:SKIP! CrossStreet2:SKIP! CrossStreets:X! " +
          "UnitCode:UNIT/C+", FLDPROG_XML);
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private String times = null;

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    times = "";
    try {
      if (!super.parseHtmlMsg(subject, body,  data)) return false;
      if (data.msgType == MsgType.RUN_REPORT) data.strSupp = append(times, "\n", data.strSupp);
      return true;
    } finally {
      times = null;
    }
  }

  @Override
  public Field getField(String name) {
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("DISPATCH_TIME")) return new MyTimeField("Dispatched", 1);
    if (name.equals("ENROUTE_TIME")) return new MyTimeField("Enroute");
    if (name.equals("ONSCENE_TIME")) return new MyTimeField("On Scene");
    if (name.equals("UNDER_CONTROL_TIME")) return new MyTimeField("Under Control");
    if (name.equals("CLEAR_TIME")) return new MyTimeField("Clear");
    if (name.equals("TRANSPORT_TIME")) return new MyTimeField("Transport");
    if (name.equals("TRANSPORT_ARRIVE_TIME")) return new MyTimeField("Transport Arrive");
    if (name.equals("COMPLETED_TIME")) return new MyTimeField("Completed", 2);
    return super.getField(name);
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile(" +\\[\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d [A-Z0-9]+\\] *| +(?=-- )");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
      data.strSupp = field;
    }
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d{4})-(\\d\\d)-(\\d\\d) +(\\d\\d:\\d\\d:\\d\\d)");

  private class MyTimeField extends InfoField {

    private String title;
    private int type;

    public MyTimeField(String title) {
      this(title, 0);
    }

    public MyTimeField(String title, int type) {
      this.title = title;
      this.type = type;
    }

    @Override
    public void parse(String field, Data data) {
      if (field.isEmpty()) return;
      int pt = field.indexOf(' ');
      if (pt < 0) abort();
      String time = field.substring(pt+1).trim();
      times = append(times, "\n", time + ' ' + title);
      if (type == 1) {
        Matcher match = DATE_TIME_PTN.matcher(field);
        if (match.matches()) {
          data.strDate = match.group(2)+'/'+match.group(3)+'/'+match.group(1);
          data.strTime = match.group(4);
        }
      }
      else if (type == 2) {
        data.msgType = MsgType.RUN_REPORT;
      }
    }

    @Override
    public String getFieldNames() {
      return "DATE TIME INFO";
    }
  }
}
