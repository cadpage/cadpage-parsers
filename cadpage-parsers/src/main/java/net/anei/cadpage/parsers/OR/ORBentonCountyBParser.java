package net.anei.cadpage.parsers.OR;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;

public class ORBentonCountyBParser extends ORBentonCountyBaseParser {

  public ORBentonCountyBParser() {
    this("BENTON COUNTY");
  }

  public ORBentonCountyBParser(String defCity) {
    super(defCity,
          "( CANCEL ADDR/S CITY! INFO/N+ " +
          "| CALL ADDR/Z CITY_CODE INFO/N+ " +
          "| UNIT/Z ENROUTE/R ADDR/S CITY CALL END " +
          "| INFO INFO/Z+? ( PHONE DATETIME ID! | DATETIME ID! | ID! ) " +
          "| FYI? CALL PLACE ADDR/S X X ( INFO INFO+ " +
                                       "| CITY MAP CODE UNIT UNIT/C INFO! ( PLACE END | ( PHONE | INFO+? PLACE PHONE ) DATETIME ID ID2 END ) ) " +
          ")");
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }

  @Override
  public String getAliasCode() {
    return "ORBentonCountyB";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {

    // DHS Fax Reports contains all kind of very sensitive details about possible child abuse
    // cases that nobody has any business knowing about.
    if (body.startsWith("DHS FAX REPORT")) {
      setFieldList("CALL");
      data.msgType = MsgType.GEN_ALERT;
      data.strCall = "DHS FAX REPORT";
      return true;
    }

    if (body.contains(",Enroute,")) return parseFields(body.split(","), data);

    if (!parseFields(body.split(";", -1), data)) return false;
    fixAddress(data);
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CITY_CODE")) return new MyCityCodeField();
    if (name.equals("CANCEL")) return new CallField("CANCEL|RECALL ALARM", true);
    if (name.equals("ENROUTE")) return new CallField("Enroute", true);
    if (name.equals("FYI")) return new SkipField("FYI:|Update:");
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("PH")) return new PhoneField("\\d{10}|", true);
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("ID")) return new IdField("\\d+", false);
    if (name.equals("ID2")) return new SkipField("\\d*", true);
    if (name.equals("PRI")) return new SkipField("\\d{0,1}");
    return super.getField(name);
  }

  private class MyCityCodeField extends CityField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {

      // Out of county M/A
      if (field.length() == 0 && getRelativeField(+1).length() == 0) {
        data.defCity = "";
        return true;
      }
      String city = cvtCityCode(field);
      if (city == null) return false;
      data.strCity = city;
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private class MyAddressField extends AddressField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (field.startsWith("LL:")) {
        super.parse(field,  data);
        return true;
      }
      Result res = parseAddress(StartType.START_ADDR, FLAG_CHECK_STATUS | FLAG_ANCHOR_END, field);
      if (res.getStatus() < STATUS_INTERSECTION) return false;
      res.getData(data);
      return true;
    }
  }

  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      String addr = getRelativeField(+1);
      if (addr.startsWith(field)) return;
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_DELIM_PTN = Pattern.compile(" *\\[\\d\\d/\\d\\d/\\d\\d .*\\]\\s*|\n");
  private static final Pattern CODE_PTN = Pattern.compile("\\d{1,2}[A-EO]\\d{1,2}[A-Z]?", Pattern.CASE_INSENSITIVE);
  private class MyInfoField extends InfoField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.contains("\n") && !field.endsWith("]")) return false;
      parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      String delim = "; ";
      for (String line : INFO_DELIM_PTN.split(field)) {
        line = stripFieldStart(line, "[PROQA]");
        if (data.strCode.length() == 0 && CODE_PTN.matcher(line).matches()) {
          data.strCode = line;
        }
        else if (line.startsWith("Radio Channel:")) {
          data.strChannel = line.substring(14).trim();
        } else {
          data.strSupp = append(data.strSupp, delim, line);
          delim = "\n";
         }
      }
    }

    @Override
    public String getFieldNames() {
      return "CH " + super.getFieldNames();
    }
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(?:(\\d\\d/\\d\\d/\\d{4})|(\\d{4}-\\d{2}-\\d{2}))? (\\d\\d:\\d\\d:\\d\\d)(?:\\.\\d+)?");
  private static final Pattern DIGIT_PTN = Pattern.compile("\\d");
  private static final String DATE_TIME_MASK1 = "NN/NN/NNNN NN:NN:NN";
  private static final String DATE_TIME_MASK2 = "NNNN-NN-NN NN:NN:NN.NNN";
  private class MyDateTimeField extends DateTimeField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      field = field.replace(" /", "/").replace("/ ", "/");
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) return false;
      String date = match.group(1);
      if (date == null) {
        date = match.group(2);
        date = date.substring(5,7)+'/'+date.substring(8,10) + '/' + date.substring(1,4);
      }
      data.strDate = date;
      data.strTime = match.group(3);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (checkParse(field, data)) return;

      if (!isLastField()) {
        field = DIGIT_PTN.matcher(field).replaceAll("N");
        if (!DATE_TIME_MASK1.startsWith(field) &&
            !DATE_TIME_MASK2.startsWith(field)) abort();
      }
    }
  }

  @Override
  public String adjustMapCity(String city) {
    if (city.equalsIgnoreCase("FOSTER")) city = "SWEET HOME";
    return city;
  }
}
