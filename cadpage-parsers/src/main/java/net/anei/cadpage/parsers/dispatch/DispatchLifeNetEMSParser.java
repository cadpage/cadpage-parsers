package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;


public class DispatchLifeNetEMSParser extends DispatchProQAParser {

  private String backupCall = null;

  public DispatchLifeNetEMSParser(String[] cityList, String defCity, String defState) {
    super(cityList, defCity, defState,
          "PRI ( GPS GPS/S | ADDR ) APT_PLACE APT_PLACE ( APT_PLACE CITY | CITY ) CALL! CALL+? INFO/L+", true);
  }

  @Override
  public String getFilter() {
    return "dispatch@lifenetems.org";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    backupCall = "";
    if (!super.parseMsg(body, data)) return false;
    if (data.strPlace.length() > 0 && isValidAddress(data.strPlace)) {
      if (checkAddress(data.strAddress) == STATUS_STREET_NAME) {
        data.strAddress = append(data.strAddress, " & ", data.strPlace);
      } else {
        data.strCross = append(data.strCross, " / ", data.strPlace);
      }
      data.strPlace = "";
    }
    if (data.strCall.length() == 0) {
      if (data.strPlace.length() > 0) {
        data.strCall = data.strPlace;
        data.strPlace = "";
      } else {
        data.strCall = backupCall;
      }
    }
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram() + " CALL";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("PRI")) return new BasePriorityField();
    if (name.equals("GPS")) return new BaseGPSField();
    if (name.equals("APT_PLACE")) return new BaseAptPlaceField();
    if (name.equals("CALL")) return new BaseCallField();
    return super.getField(name);
  }

  private static final Pattern PRIORITY_PTN = Pattern.compile("(\\d)[A-Z]?-(.*)");
  private class BasePriorityField extends PriorityField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = PRIORITY_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strPriority = match.group(1);

      // Call field is overridden by a later field in newer calls.  But
      // we copy it here just in case this is an older call
      backupCall = match.group(2).trim();
    }
  }

  private static final Pattern GPS_PTN = Pattern.compile("\\d+.*'[NSEW]");
  private class BaseGPSField extends Field {

    public BaseGPSField() {
      setPattern(GPS_PTN, true);
    }

    @Override
    public void parse(String field, Data data) {
      data.strAddress = append(data.strAddress, " ", field);
    }

    @Override
    public String getFieldNames() {
      return "ADDR";
    }
  }

  private static final Pattern APT_PTN = Pattern.compile("(?:APT|RM|ROOM|SUITE) +(.*)|LOT +.*|.*\\d[A-Z]?|[A-Z]", Pattern.CASE_INSENSITIVE);
  private class BaseAptPlaceField extends Field {
    @Override
    public void parse(String field, Data data) {
      if (field.isEmpty()) return;
      Matcher match = APT_PTN.matcher(field);
      if (match.matches()) {
        String apt = match.group(1);
        if (apt == null) apt = field;
        data.strApt = append(data.strApt, "-", apt);
      } else {
        data.strPlace = append(data.strPlace, " - ", field);
      }
    }

    @Override
    public String getFieldNames() {
      return "APT PLACE";
    }
  }

  private static final Pattern NOT_CALL_PTN = Pattern.compile("\\d+.*");
  private class BaseCallField extends CallField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (field.startsWith("You are responding")) return false;
      if (field.equals("<PROQA_SCRIPT>")) return false;
      if (field.startsWith("Chief complaint:")) return false;
      if (NOT_CALL_PTN.matcher(field).matches()) return false;
      parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      data.strCall = append(data.strCall, "/", field);
    }
  }
}
