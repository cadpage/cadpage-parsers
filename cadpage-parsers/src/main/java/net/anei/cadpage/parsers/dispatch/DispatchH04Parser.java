package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchH04Parser extends DispatchH05Parser {

  public DispatchH04Parser(String defCity, String defState) {
    super(defCity, defState,
          "LEAD_JUNK+? ( ADDR:ADDRCITY/S6! PLACE:PLACE! | ) " +
          "( CALL:CALL! | Fire_Call_Type:CALL! EMS_CALL_TYPE:CALL2 | FIRE_CALL_TYPE:CALL! EMS_CALL_TYPE:CALL2! | EMS_CALL_TYPE:CALL2 ) " +
          "( PLACE:PLACE! ADDR:ADDRCITY/S6! | ) " +
          "( ASSIGNED_UNIT(s):UNIT! NARRATIVE:EMPTY! INFO_BLK/N+? PLACE:PLACE! ADDR:ADDR! | ) " +
          "Lat_/_Long:GPS? ( Cross_Streets:X | CROSS_STREETS:X | ) CALLER_NAME:NAME? " +
             "( CALLER_PHONE:PHONE | Caller_Phone:PHONE | Caller_Number:PHONE | ) Lat_/_Long:GPS? ID:ID? " +
             "( PRI:PRI! | FIRE_PRIORITY:PRI! EMS_PRIORITY:PRI2! | ) DATE:DATETIME! " +
             "( MAP:MAP! | FIRE_QUADRANT:MAP! EMS_District:MAP2! ) " +
             "( UNIT:UNIT! | ASSIGNED_UNIT(s):UNIT! | ) " +
             "( INFO:INFO_BLK! | NARRATIVE:INFO_BLK! | ) INFO_BLK/N+? TIMES:TIMES? TIMES+ " +
             "CALLER_NAME:NAME FIRE_BOX:BOX INCIDENT_NUMBER:ID");
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!super.parseHtmlMsg(subject, body, data)) return false;
    return !data.strAddress.isEmpty();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("LEAD_JUNK")) return new SkipField("(?!(?:ADDR|CALL|Fire Call Type|FIRE CALL TYPE|EMS CALL TYPE):).*", true);
    if (name.equals("CALL2")) return new BaseCall2Field();
    if (name.equals("ADDRCITY")) return new BaseAddressCityField();
    if (name.equals("PRI2")) return new BasePriority2Field();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("MAP2")) return new BaseMap2Field();
    return super.getField(name);
  }

  private class BaseCall2Field extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals(data.strCall)) return;
      data.strCall = append(data.strCall, " / ", field);
    }
  }

  private static final Pattern GPS_PTN = Pattern.compile("(?:100)?(\\+?\\d+\\.\\d+|-361)[, ]*(-\\d{2,3}\\.\\d+|-361)");
  private static final Pattern STATE_PTN = Pattern.compile("[A-Z]{2}");
  private class BaseAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String part = p.getLastOptional(',');
      Matcher match = GPS_PTN.matcher(part);
      if (match.matches()) {
        setGPSLoc(match.group(1)+','+match.group(2), data);
        part = p.getLastOptional(',');
      }

      if ((match = STATE_PTN.matcher(part)).matches()) {
        data.strState = part;
        part = p.getLastOptional(',');
      }

      data.strCity = part;
      super.parse(p.get().replace('@', '&'), data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " ST GPS";
    }
  }

  private class BasePriority2Field extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals(data.strPriority)) return;
      data.strPriority = append(data.strPriority, " / ", field);
    }
  }

  private class BaseMap2Field extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals(data.strMap)) return;
      data.strMap = append(data.strMap, " / ", field);
    }
  }
}
