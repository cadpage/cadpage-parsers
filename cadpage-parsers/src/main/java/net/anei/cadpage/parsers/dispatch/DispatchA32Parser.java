package net.anei.cadpage.parsers.dispatch;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchA32Parser extends FieldProgramParser {

  private List<String> addressPlaceFields = new ArrayList<String>();

  public DispatchA32Parser(String[] cityList, String defCity, String defState) {
    super(cityList, defCity, defState,
          "( GEN_INFO/G DATETIME2 District:MAP? " +
          "| ADDR CALL! " +
          "| CALL ( ( DATETIME2 | CALL/SDS DATETIME2! | CALL/SDS CALL/SDS DATETIME2 ) District:MAP? ID? ( SELECT/ADDRTAG PLACE+? Addr:ADDR! INFO+? CITY_ST! | PLACE+? ADDR/Z CITY_ST! ) ID? " +
                 "| District:MAP! Addr:ADDR! CITY_ST! " +
                 "| ( ADDR_PL ADDR_PL ADDR_PL2 CITY! " +
                   "| ADDR_PL ADDR_PL2 CITY! " +
                   "| ADDR/Z CITY! " +
                   "| PLACE ADDR! " +
                   "| ADDR! " +
                   ") INFO+? ( CITY District:MAP? INFO+? DATETIME | District:MAP INFO+? DATETIME | DATETIME ) " +
                 ") " +
          ") INFO+");
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.endsWith("Page") && !subject.endsWith("Dispatch")) return false;
    addressPlaceFields.clear();
    String[] flds = body.split("\n");
    setSelectValue("");
    for (String fld : flds) {
      if (fld.startsWith("Addr:")) {
        setSelectValue("ADDRTAG");
        break;
      }
    }
    return parseFields(flds, data);
  }

  public Field getField(String name) {
    if (name.equals("GEN_INFO")) return new CallField("GENERAL INFO");
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d:\\d\\d");
    if (name.equals("DATETIME2")) return new BaseDateTime2Field();
    if (name.equals("ID")) return new IdField("\\d\\d-\\d+(?:-[A-Z]{2})?", true);
    if (name.equals("PLACE")) return new BasePlaceField();
    if (name.equals("ADDR")) return new BaseAddressField();
    if (name.equals("CITY_ST")) return new BaseCityStateField();
    if (name.equals("ADDR_PL")) return new BaseAddressPlaceField(false);
    if (name.equals("ADDR_PL2")) return new BaseAddressPlaceField(true);
    if (name.equals("INFO")) return new BaseInfoField();
    return super.getField(name);
  }

  private static final Pattern DATETIME2_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) +(\\d\\d?:\\d\\d[AP]M)", Pattern.CASE_INSENSITIVE);
  private static final DateFormat DATETIME2_FMT = new SimpleDateFormat("hh:mmaa");
  private class BaseDateTime2Field extends DateTimeField {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = DATETIME2_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strDate = match.group(1);
      setTime(DATETIME2_FMT, match.group(2), data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private static final Pattern PLACE_APT_PTN = Pattern.compile("(?:LOT|APT|RM|ROOM)[ #]*(.*)|\\d{1,4}[A-Z]?", Pattern.CASE_INSENSITIVE);
  private class BasePlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = PLACE_APT_PTN.matcher(field);
      if (match.matches()) {
        String apt = match.group(1);
        if (apt == null) apt = field;
        data.strApt = apt;
        return;
      }

      data.strPlace = append(data.strPlace, " - ", field);
    }

    @Override
    public String getFieldNames() {
      return "APT PLACE";
    }
  }

  private class BaseAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "Addr:");
      super.parse(field, data);
    }

    @Override
    public boolean checkParse(String field, Data data) {
      field = stripFieldStart(field, "Addr:");
      return super.checkParse(field, data);
    }
  }

  private static final Pattern CITY_ST_PTN = Pattern.compile("([A-Za-z ]+), +([A-Z]{2})(?: +(\\d{5}))?");
  private class BaseCityStateField extends Field {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = CITY_ST_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strCity = match.group(1).trim();
      data.strState = match.group(2);
      if (data.strCity.length() == 0) {
        String zip = match.group(3);
        if (zip != null) data.strCity = zip;
      }
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "CITY ST";
    }
  }

  private static final Pattern CROSS_PTN = Pattern.compile("(?:X|BETWEEN) +(.*)", Pattern.CASE_INSENSITIVE);
  private static final Pattern PLACE_PTN = Pattern.compile("[A-Z \\.\\+/]+");
  private class BaseAddressPlaceField extends Field {
    private boolean done;

    public BaseAddressPlaceField(boolean done) {
      this.done = done;
    }

    @Override
    public void parse(String field, Data data) {
      if (field.length() > 0) addressPlaceFields.add(field);
      if (!done) return;

      // By now we have collected up to 3 fields, any of which might be the
      // best address field
      if (addressPlaceFields.isEmpty()) abort();

      int addrPt = -1;
      int bestStatus = Integer.MIN_VALUE;
      for (int ii = 0; ii<addressPlaceFields.size(); ii++) {
        int stat = checkAddress(addressPlaceFields.get(ii));
        if (stat >= bestStatus) {
          addrPt = ii;
          bestStatus = stat;
        }
      }
      parseAddress(addressPlaceFields.get(addrPt), data);
      addressPlaceFields.remove(addrPt);

      // Make a second pass through the remaining fields
      for (String fld : addressPlaceFields) {
        Matcher match = CROSS_PTN.matcher(fld);
        if (match.matches()) {
          data.strCross = match.group(1);
          continue;
        }

        if (data.strPlace.length() == 0) {
          if (PLACE_PTN.matcher(fld).matches()) {
            data.strPlace = fld;
            continue;
          }
        }

        data.strSupp = append(data.strSupp, " / ", fld);
      }
    }

    @Override
    public String getFieldNames() {
      return "PLACE X ADDR APT INFO";
    }
  }

  private static final Pattern INFO_JUNK_PTN = Pattern.compile("\\d\\d/\\d\\d/\\d{4} \\d{4} [A-Za-z, ]+");
  private class BaseInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (INFO_JUNK_PTN.matcher(field).matches()) return;
      data.strSupp = append(data.strSupp, "\n", field);
    }
  }
}
