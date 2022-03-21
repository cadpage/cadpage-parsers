package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class DispatchA47Parser extends FieldProgramParser {

  private String subjectMarker;
  private boolean noUnit;
  private Pattern unitPtn;

  public DispatchA47Parser(String[] cityList, String defCity, String defState, String unitPtn) {
    this(null, cityList, defCity, defState, unitPtn);
  }

  public DispatchA47Parser(String subjectMarker, String[] cityList, String defCity, String defState, String unitPtn) {
    super(cityList, defCity, defState,
          "( SELECT/NEW Reported:DATETIME! Priorities:PRI1! CFS:ID! Type:CALL! Loc:ADDRCITYST! Cross:X! Prem:PLACE! Units:UNIT! " +
          "| ( Reported:DATETIME! ID_CALL! Loc:ADDR/S! | ID_CALL! Reported:DATETIME? ADDR/S! ) X? ( PLACE2 UNITQ | UNIT2 | PLACE2 END | ) " +
          ") INFO/N+");
    this.subjectMarker = subjectMarker;
    noUnit = (unitPtn == null);
    this.unitPtn = noUnit ? null : unitPtn.equals(".*") ? null : Pattern.compile("(?:(?:"+unitPtn+")\\b *)+");
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subjectMarker != null && !subject.equals(subjectMarker)) return false;
    if (body.contains("Priorities:")) {
      setSelectValue("NEW");
      body = body.replace(" Type:", "\nType:");
    } else {
      setSelectValue("OLD");
    }
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d/\\d{4} \\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("PRI1")) return new BasePriority1Field();
    if (name.equals("ID_CALL")) return new BaseIdCallField();
    if (name.equals("ADDR")) return new BaseAddressField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("PLACE2")) return new BasePlace2Field();
    if (name.equals("UNIT2")) return new BaseUnit2Field();
    if (name.equals("UNITQ")) return new BaseOptUnitField();
    return super.getField(name);
  }

  private static Pattern PRI_PTN = Pattern.compile("P:(\\d?) +F:(\\d?) +A:(\\d?) +O:(\\d?)");
  private class BasePriority1Field extends PriorityField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = PRI_PTN.matcher(field);
      if (!match.matches()) abort();
      String priority = null;
      for (int ndx =  1; ndx<=match.groupCount(); ndx++) {
        String p1 = match.group(ndx);
        if (!p1.isEmpty()) {
          if (priority == null || p1.charAt(0) < priority.charAt(0)) priority = p1;
        }
      }
      if (priority != null) data.strPriority = priority;
    }
  }

  private static Pattern ID_CALL_PTN = Pattern.compile("(\\d\\d-\\d{6}) +(.*)");
  private class BaseIdCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ID_CALL_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCallId = match.group(1);
      data.strCall = match.group(2);
    }

    @Override
    public String getFieldNames() {
      return "ID CALL";
    }
  }

  private static final Pattern ADDR_CITY_PTN = Pattern.compile("(.*)(?:,| - )(.*)");
  private static final Pattern ST_ZIP_PTN = Pattern.compile("([A-Z]{2})(?: +(\\d{5}))?");
  private class BaseAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ",");
      Matcher match = ADDR_CITY_PTN.matcher(field);
      String city = null;
      if (match.matches()) {
        field = match.group(1).trim();
        String tmp = match.group(2).trim();
        match = ST_ZIP_PTN.matcher(tmp);
        if (match.matches()) {
          String state = match.group(1);
          city = getOptGroup(match.group(2));
          data.strState = state;
        } else {
          city = tmp;
        }
      }

      // If we did not find a city, see if there is a slash delimited city
      if (city == null) {
        int pt = field.lastIndexOf('/');
        if (pt >= 0) {
          String tmp = field.substring(pt+1).trim();
          if (isCity(tmp)) {
            city = tmp;
            field = field.substring(0,pt).trim();
          }
        }
      }

      super.parse(field, data);
      if (city != null && (data.strCity.length() == 0 || data.strCity.equalsIgnoreCase("COUNTY"))) data.strCity = city;
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " ST";
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.contains("/")) return false;
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private class BasePlace2Field extends PlaceField {

    @Override
    public void parse(String field, Data data) {

      if (data.strCity.length() == 0) {
        parseAddress(StartType.START_OTHER, FLAG_ONLY_CITY | FLAG_ANCHOR_END, field, data);
        field = getStart();
      }
      super.parse(field, data);
    }
  }

  private class BaseUnit2Field extends UnitField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (noUnit) return false;
      if (unitPtn != null && !unitPtn.matcher(field).matches()) return false;
      parse(field, data);
      return true;
    }
  }

  private class BaseOptUnitField extends BaseUnit2Field {
    @Override
    public boolean checkParse(String field, Data data) {
      if (noUnit || unitPtn == null && !isLastField()) return false;
      return super.checkParse(field, data);
    }

  }
}
