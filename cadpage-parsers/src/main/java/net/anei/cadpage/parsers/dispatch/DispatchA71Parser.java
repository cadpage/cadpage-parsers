package net.anei.cadpage.parsers.dispatch;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class DispatchA71Parser extends FieldProgramParser {

  public DispatchA71Parser(String defCity, String defState) {
    super(defCity, defState,
         "( ID:ID! FAIL INFO:INFO! " +
         "| CALL:CALL? FAIL CALLS:CALL? FAIL PLACE:PLACE? FAIL ADDR:ADDRCITY? FAIL APT:APT FAIL CITY:CITY? FAIL ( XY:GPS | LAT:GPS1 LONG:GPS2 ) INFO/N+ ( AREA:MAP INFO/N+ | ) ( ID:ID INFO/N+ | ) ( PLACE:PLACE INFO/N+ | ) ( PRI:PRI INFO/N+ | ) ( DATE:DATE FAIL | ) TIME:TIME INFO/N+ INFO:INFO/N " +
         ") INFO/N+");
  }

  private boolean foundInfo;

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.endsWith(" CLEARED")) data.msgType = MsgType.RUN_REPORT;
    return parseMsg(body, data);
  }

  private static final Pattern DELIM = Pattern.compile("\\s+(?=(?:CALL|CALLS|PLACE|ADDR|APT|CITY|XY|LAT|LONG|AREA|ID|PRI|DATE|TIME|NAME|PHONE|MAP|UNIT|X|ESN|ELT[A-Z]|INFO|COMMENT):)");

  @Override
  protected boolean parseMsg(String body, Data data) {
    String[] flds;
    if (body.contains("\n")) {
      flds = body.split("\n+");
    } else {
      flds = DELIM.split(body);
    }
    foundInfo = false;
    if (!parseFields(flds, data)) return false;
    return isGoodResult(data);
  }

  private boolean isGoodResult(Data data) {
    if (!data.strGPSLoc.isEmpty() || !data.strCallId.isEmpty()) return true;
    if (!data.strCall.isEmpty() && (!data.strAddress.isEmpty() || !data.strCity.isEmpty())) return true;
    return false;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new BaseCallField();
    if (name.equals("PLACE")) return new BasePlaceField();
    if (name.equals("ADDRCITY")) return new BaseAddressCityField();
    if (name.equals("CITY")) return new BaseCityField();
    if (name.equals("DATE")) return new DateField("\\d\\d?/\\d\\d?/\\d{2}(?:\\d{2})?|", true);
    if (name.equals("TIME")) return new BaseTimeField();
    if (name.equals("MAP")) return new BaseMapField();
    if (name.equals("INFO")) return new BaseInfoField();
    return super.getField(name);
  }

  private class BaseCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ",");
      if (data.strCall.contains(field)) return;
      if (field.contains(data.strCall)) {
        data.strCall = field;
      } else {
        data.strCall = append(data.strCall, " - ", field);
      }

    }
  }

  private static final Pattern MSPACE_PTN = Pattern.compile(" {2,}");

  private class BasePlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      field = MSPACE_PTN.matcher(field).replaceAll(" ");
      super.parse(field, data);
    }
  }

  private static final Pattern ADDR_SECTOR_PTN = Pattern.compile("(.*?)[- ]+([NSEW]{1,2} SECTOR|SEC [NSEW]{1,2})", Pattern.CASE_INSENSITIVE);
  private static final Pattern ADDR_PFX_PTN = Pattern.compile("[NSEW]B|\\d+");
  private static final Pattern ADDR_APT_PTN = Pattern.compile("(.*?)[, ]+(?:APT|RM|ROOM|LOT|UNIT)\\.? +([^,]+)", Pattern.CASE_INSENSITIVE);

  private class BaseAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {

      field = MSPACE_PTN.matcher(field).replaceAll(" ");

      if (field.isEmpty()) {
        field = data.strPlace;
        int pt = field.indexOf(',');
        if (pt >= 0) {
          int pt2 = field.indexOf(',', pt+1);
          if (pt2 < 0) pt2 = field.length();
          String city = field.substring(pt+1, pt2).trim();
          field = field.substring(0,pt).trim();
          if (city.length() == 2) {
            data.strState = city;
          } else {
            data.strCity = city;
          }
        }
        data.strPlace = "";
      }

      int pt = data.strPlace.indexOf(field);
      if (pt >= 0) data.strPlace = stripFieldEnd(data.strPlace.substring(0,pt).trim(), "(");;

      field = stripFieldStart(field, "Intersection Of ");

      Matcher match = ADDR_SECTOR_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        data.strMap = match.group(2);
      }
      field = stripFieldEnd(field, ",");

      if (ADDR_PFX_PTN.matcher(data.strPlace).matches()) {
        if (!field.startsWith(data.strPlace)) field = append(data.strPlace, " ", field);
        data.strPlace = "";
      }

      String apt = "";
      match = ADDR_APT_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        apt = match.group(2).trim();
      }

      super.parse(field, data);
      if (data.strCity.length() == 2) {
        data.strState = data.strCity;
        data.strCity = "";
      }

      data.strApt = append(data.strApt, "-", apt);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " ST MAP";
    }
  }

  private class BaseCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (field.isEmpty()) return;
      super.parse(field, data);
    }
  }

  private static final Pattern TIME_PTN = Pattern.compile("\\d\\d?:\\d\\d?(:\\d\\d?)?(?: [AP]M)?");
  private static final DateFormat TIME_FMT1 = new SimpleDateFormat("hh:mm:ss aa");
  private static final DateFormat TIME_FMT2 = new SimpleDateFormat("hh:mm aa");

  private class BaseTimeField extends TimeField {
    @Override
    public void parse(String field, Data data) {
      if (foundInfo) abort();
      if (field.isEmpty()) return;
      Matcher match = TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      if (field.endsWith("M")) {
        setTime((match.group(1)!=null ? TIME_FMT1 : TIME_FMT2), field, data);
      }  else {
        data.strTime = field;
      }
    }
  }

  private class BaseMapField extends MapField {
    @Override
    public void parse(String field, Data data) {
      if (foundInfo) abort();
      if (field.equals("Unknown")) return;
      super.parse(field, data);
    }
  }

  private static final Pattern EMPTY_LABEL_PTN = Pattern.compile("(?:ESN|ELT[A-Z]):");

  protected class BaseInfoField extends InfoField {

    @Override
    public void parse(String field, Data data) {
      foundInfo = true;
      if (field.startsWith("NAME:")) {
        if (data.strName.isEmpty()) data.strName = cleanWirelessCarrier(field.substring(5).trim());
      } else if (field.startsWith("PHONE:")) {
        if (data.strPhone.isEmpty()) data.strPhone = field.substring(6).trim();
      } else if (field.startsWith("MAP:")) {
        field = field.substring(4).trim();
        if (!field.equalsIgnoreCase("unknown")) data.strMap = field;
      } else if (field.startsWith("UNIT:")) {
        field = field.substring(5).trim().replace(";", ",");
        field = stripFieldEnd(field, ",");
        data.strUnit = append(data.strUnit, ",", field);
      } else if (field.startsWith("X:")) {
        field = field.substring(2).trim().replace('@', '/').replace("*", "");
        field = stripFieldEnd(field, "/");
        data.strCross = append(data.strCross, " / ", field);
      } else if (!EMPTY_LABEL_PTN.matcher(field).matches()) {
        if (field.startsWith("COMMENT:")) {
          int pt = field.indexOf('|', 8);
          pt = (pt >= 0 ? pt+1 : 8);
          field = field.substring(pt).trim();
        } else {
          field = stripFieldStart(field, "INFO:");
          field = stripFieldStart(field, "REMARKS:");
        }
        if (field.startsWith("Call Initiated by")) return;
        if (data.msgType != MsgType.RUN_REPORT) {
          if (field.contains(" | Dispatched | ") || field.contains(" | Cleared | ")) return;
        }
        super.parse(field, data);
      }
    }

    @Override
    public String getFieldNames() {
      return "NAME PHONE MAP X UNIT INFO";
    }
  }
}
