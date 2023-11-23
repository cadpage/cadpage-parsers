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
          "( ID:ID! INFO:INFO! " +
          "| CALL:CALL PLACE:PLACE ADDR:ADDR? APT:APT CITY:CITY? ( XY:GPS | LAT:GPS1 LONG:GPS2 ) AREA:MAP? ID:ID! PLACE:PLACE? PRI:PRI? DATE:DATE? TIME:TIME? NAME:NAME? PHONE:PHONE? MAP:MAP? UNIT:UNIT? ESN:LINFO? ELTE:LINFO? ELTF:LINFO? ELTL:LINFO? X:X? INFO:INFO/N? " +
          ") INFO/N+");
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.endsWith(" CLEARED")) data.msgType = MsgType.RUN_REPORT;
    return parseMsg(body, data);
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (body.contains("\n")) {
      return parseFields(body.split("\n"), data);
    } else {
      return super.parseMsg(body, data);
    }
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new BaseAddressField();
    if (name.equals("CITY")) return new BaseCityField();
    if (name.equals("DATE")) return new DateField("\\d\\d?/\\d\\d?/\\d{2}(?:\\d{2})?", true);
    if (name.equals("TIME")) return new BaseTimeField();
    if (name.equals("X")) return new BaseCrossField();
    if (name.equals("MAP")) return new BaseMapField();
    if (name.equals("UNIT")) return new BaseUnitField();
    if (name.equals("INFO")) return new BaseInfoField();
    return super.getField(name);
  }


  private static final Pattern MSPACE_PTN = Pattern.compile(" {2,}");
  private static final Pattern ADDR_SECTOR_PTN = Pattern.compile("(.*?)[- ]+([NSEW]{1,2} SECTOR|SEC [NSEW]{1,2})", Pattern.CASE_INSENSITIVE);
  private static final Pattern ADDR_PFX_PTN = Pattern.compile("[NSEW]B|\\d+");

  private class BaseAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {

      if (field.isEmpty()) {
        field = data.strPlace;
        int pt = field.indexOf(',');
        if (pt >= 0) {
          int pt2 = field.indexOf(',', pt+1);
          if (pt2 < 0) pt2 = field.length();
          data.strCity = field.substring(pt+1, pt2).trim();
          field = field.substring(0,pt).trim();
        }
        data.strPlace = "";
      }

      field = MSPACE_PTN.matcher(field).replaceAll(" ");
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

      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " MAP";
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
      Matcher match = TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      if (field.endsWith("M")) {
        setTime((match.group(1)!=null ? TIME_FMT1 : TIME_FMT2), field, data);
      }  else {
        data.strTime = field;
      }
    }
  }

  private class BaseCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("@ *", "/").replace('@', '/');
      super.parse(field, data);
    }
  }

  private class BaseMapField extends MapField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("Unknown")) return;
      super.parse(field, data);
    }
  }

  private class BaseUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ",");
      super.parse(field, data);
    }
  }

  private class BaseInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("COMMENT:")) {
        int pt = field.indexOf('|', 8);
        pt = (pt >= 0 ? pt+1 : 8);
        field = field.substring(pt).trim();
      }
      super.parse(field, data);
    }
  }
}
