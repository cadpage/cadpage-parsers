package net.anei.cadpage.parsers.dispatch;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


/**
 * Mercer County, NJ
 */
public class DispatchA24Parser extends FieldProgramParser {

  public DispatchA24Parser(String defCity, String defState) {
    super(defCity, defState,
           "UNIT:UNIT? ( CALL:CALL! RUN:SKIP? PLACE:PLACE? ( ADDR:ADDRCITYST! BLDG:APT APT:APT CITY:CITY! XSTREETS:X ID:ID% PRI:PRI DATE:DATE% TIME:TIME% MAP:MAP% CALLERPHONE:PHONE% JURISDICTION:SRC LATITUDE:GPS1/d LONGITUDE:GPS2/d TPPU:SKIP ( ALLUNITS:UNIT |  UNIT:UNIT | ID:ID? ) | ) INFO:INFO INFO/N+ WARNINGS:ALERT" +
                      "| ID:ID! INFO/RN+ " +
                      "| FAIL " +
                      ")");
  }

  private static final Pattern UNIT_PTN = Pattern.compile("\\bUNIT: *([- A-Z0-9]+?) *[;\\n ] *", Pattern.CASE_INSENSITIVE);

  @Override
  public boolean parseMsg(String body, Data data) {

    body = stripFieldEnd(body, "(#1");
    String[] flds = body.split("\n");
    if (flds.length < 3) flds = body.split(";");
    if (flds.length >= 3) {
      if (super.parseFields(flds, data)) return true;
    } else {
      if (super.parseMsg(body, data)) return true;
    }

    Matcher match = UNIT_PTN.matcher(body);
    if (!match.find()) return false;

    setFieldList("UNIT INFO");
    if (match.start() == 0) body = body.substring(match.end()).trim();

    data.parseGeneralAlert(this, body);
    data.strUnit = match.group(1);
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("PLACE")) return new BasePlaceField();
    if (name.equals("ADDRCITYST")) return new BaseAddressCityStateField();
    if (name.equals("CITY")) return new BaseCityField();
    if (name.equals("DATE")) return new DateField("(?:CAD date *\\()?(\\d\\d?/\\d\\d?(?:/\\d{2,4})?)\\)?(?: *\\(.*)?", true);
    if (name.equals("TIME")) return new BaseTimeField();
    return super.getField(name);
  }

  private class BasePlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "ALIAS=");
      super.parse(field, data);
    }
  }

  private static final Pattern TRAIL_APT_PTN = Pattern.compile("(.*), USA\\b *#?(.*)");
  private class BaseAddressCityStateField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      String apt = "";
      Matcher match = TRAIL_APT_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        apt = match.group(2);
      }
      super.parse(field, data);
      data.strApt = append(data.strApt, "-", apt);
    }
  }

  private class BaseCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (field.endsWith(")")) {
        int pt = field.indexOf('(');
        if (pt >= 0) field = field.substring(0, pt).trim();
      }
      if (field.startsWith("Apt/Unit #")) {
        Parser p = new Parser(field.substring(10).trim());
        data.strApt = append(data.strApt, "-", p.get(','));
        field = p.get();
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "APT CITY";
    }
  }

  private static final Pattern TIME_PTN = Pattern.compile("(?:CAD time *\\()?(\\d\\d?:\\d\\d(:\\d\\d)?( [AP]M)?)\\)?(?: *\\(.*)?");
  private static final DateFormat TIME_FMT1 = new SimpleDateFormat("hh:mm:ss aa");
  private static final DateFormat TIME_FMT2 = new SimpleDateFormat("hh:mm aa");
  private class BaseTimeField extends TimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      field = match.group(1);
      if (match.group(3)!=null) {
        setTime((match.group(2)!=null ? TIME_FMT1 : TIME_FMT2), field, data);
      } else {
        data.strTime = field;
      }
    }
  }
}
