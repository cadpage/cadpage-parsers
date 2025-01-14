package net.anei.cadpage.parsers.dispatch;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Cuyahoga County, OH
 */
public class DispatchA39Parser extends FieldProgramParser {

  private static final String PROGRAM_STR = "DEMPTY+? ( ADDR/iS6! | CALL ADDR/iS6! | CALL CALL ADDR/iS6! | CALL ADDR/iS6! ) APT? INFO/N+";

  private Properties cityCodes;

  public DispatchA39Parser(Properties cityCodes, String defCity, String defState) {
    super(cityCodes, defCity, defState, PROGRAM_STR);
    this.cityCodes = cityCodes;
  }

  public DispatchA39Parser(String[] cityList, String defCity, String defState) {
    super(cityList, defCity, defState, PROGRAM_STR);
    this.cityCodes = null;
  }

  @Override
  protected boolean keepLeadBreak() {
    return true;
  }

  @Override
  public boolean parseUntrimmedMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch Message")) return false;
    if (!parseFields(body.split("\n",-1), 2, data)) return false;
    if (data.strCall.length() == 0) data.strCall = "ALERT";
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DEMPTY")) return new BaseDoubleEmptyField();
    if (name.equals("CALL")) return new BaseCallField();
    if (name.equals("ADDR")) return new BaseAddressField();
    if (name.equals("APT")) return new BaseAptField();
    if (name.equals("INFO")) return new BaseInfoField();
    return super.getField(name);
  }

  private class BaseDoubleEmptyField extends SkipField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      return field.length() == 0 && getRelativeField(+1).length() == 0;
    }
  }

  private static final Pattern CALL_ID_PTN = Pattern.compile("(.*?) *\\b(\\d{7,10})");
  private class BaseCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (data.strCallId.length() == 0) {
        Matcher match = CALL_ID_PTN.matcher(field);
        if (match.matches()) {
          field = match.group(1).trim();
          data.strCallId = match.group(2);
        }
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CALL ID";
    }
  }

  private static final Pattern DIR_BOUND_PTN = Pattern.compile("\\b([NSEW])/B\\b");
  private static final Pattern TRAIL_APT_PTN = Pattern.compile("(.*) (?:#|ROOM|APT) *(\\S+)", Pattern.CASE_INSENSITIVE);
  private static final Pattern ADDR_ST_ZIP_PTN = Pattern.compile("(.*?)[, ]+([A-Z]{2}) +(\\d{5})\\b *(.*)");
  private static final Pattern ADDR_ZIP_PTN = Pattern.compile("(.*) (\\d{5})");
  private static final Pattern STATE_PTN = Pattern.compile("[A-Z]{2}");
  private static final Pattern LEFT_PTN = Pattern.compile("([A-Z]{2})(?: +\\d{5})?(?: +(.*))?");
  private static final Pattern APT_PTN = Pattern.compile("[A-Z]?\\d{1,4}[A-Z]?|[A-Z]|(?:APT|RM|ROOM|LOT|UNIT) +(.*)", Pattern.CASE_INSENSITIVE);
  private class BaseAddressField extends AddressField {

    @Override
    public boolean checkParse(String field, Data data) {
      return checkParse(false, field, data);
    }

    @Override
    public void parse(String field, Data data) {
      checkParse(true, field, data);
    }

    private boolean checkParse(boolean force, String field, Data data) {
      if (field.length() == 0) return false;
      field = DIR_BOUND_PTN.matcher(field).replaceAll("$1B");
      String apt = null;
      Matcher match = TRAIL_APT_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        apt = match.group(2);
      }
      String zip = null;
      String place = null;
      String state = "";
      match =  ADDR_ST_ZIP_PTN.matcher(field);
      if (match.find() && !isStreetSuffix(match.group(2))) {
        field = match.group(1);
        state = match.group(2);
        zip = match.group(3);
        place = match.group(4).trim();
      } else {
        match = ADDR_ZIP_PTN.matcher(field);
        if (match.matches()) {
          field = match.group(1).trim();
          zip = match.group(2);
          place = "";
        }
      }
      Parser p = new Parser(field);
      String city = p.getLastOptional(',');
      if (!city.isEmpty()) {
        if (state.isEmpty() && STATE_PTN.matcher(city).matches()) {
          state = city;
          city = p.getLastOptional(',');
        }
        if (place != null || !state.isEmpty() || isCity(city)) {
          field = p.get();
          if (place == null) place = "";
        } else {
          city = "";
        }
      }

      int flags = FLAG_IMPLIED_INTERSECT | FLAG_RECHECK_APT;
      if (!force) flags |= FLAG_CHECK_STATUS;
      if (!city.isEmpty()) flags |= FLAG_NO_CITY;
      if (place != null) flags |= FLAG_ANCHOR_END;

      Result res = parseAddress(StartType.START_ADDR, flags, field);
      if (!force && place == null && !res.isValid()) return false;
      res.getData(data);

      if (data.strCity.isEmpty() && !city.isEmpty()) {
        if (cityCodes != null) city = convertCodes(city, cityCodes);
        data.strCity = city;
      }
      data.strState = state;

      if ((flags & FLAG_ANCHOR_END) == 0) {
        String left = res.getLeft();
        if (!left.isEmpty()) {
          match = LEFT_PTN.matcher(left);
          if (match.matches()) {
            data.strState = match.group(1);
            place = match.group(2);
          } else {
            data.strAddress = data.strCity = "";
            parseAddress(field, data);
          }
        }
      }
      if (place != null) {
        if ((match = APT_PTN.matcher(place)).matches()) {
          String tmp = match.group(1);
          if (tmp != null) place = tmp;
          apt = (apt == null ? place : append(place, "-", apt));
        } else {
          data.strPlace = append(data.strPlace, " - ", place);
        }
      }
      if (apt != null) data.strApt = append(data.strApt, "-", apt);
      if (zip != null && data.strCity.length() == 0) data.strCity = zip;
      return true;
    }

    @Override
    public String getFieldNames() {
      return "ADDR X? APT CITY ST PLACE";
    }
  }

  private class BaseAptField extends AptField {

    public BaseAptField() {
      super("APT *(\\S+)", true);
    }

    @Override
    public void parse(String field, Data data) {
      if (data.strApt.equals(field)) return;
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_JUNK_PTN = Pattern.compile("(?:-- +)?(?:From )?\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d Disp +\\S+[- ]*|[- ]+");
  private class BaseInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_JUNK_PTN.matcher(field);
      if (match.lookingAt()) field = field.substring(match.end());
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CALL " + super.getFieldNames();
    }
  }
}
