package net.anei.cadpage.parsers.KS;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class KSCrawfordCountyAParser extends DispatchH05Parser {

  public KSCrawfordCountyAParser() {
    super(CITY_LIST, "CRAWFORD COUNTY", "KS",
          "JUNK+? CALL_ADDR_CITY_X HTTP INFO_BLK/Z+? ID! TIMES+");
    setPreserveWhitespace(true);
    setAccumulateUnits(true);
  }

  @Override
  public String getFilter() {
    return "@crsoks.org,@crosks.org,@police.pittks.org";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!subject.startsWith("Automatic R&R Notification:")) return false;
    String call = subject.substring(27).trim();
    call = stripFieldEnd(call, ", Choose Call Type-->");
    return super.parseHtmlMsg(subject, body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("JUNK")) return new SkipField("\\*+EXTERNAL\\*+|This is an EXTERNAL EMAIL.*|", true);
    if (name.equals("CALL_ADDR_CITY_X")) return new MyCallAddressCityCrossField();
    if (name.equals("HTTP")) return new SkipField("https?://.*", true);
    if (name.equals("ID")) return new MyIdField();
    return super.getField(name);
  }

  private static final Pattern MSPACE_PTN = Pattern.compile(" {3,}");
  private class MyCallAddressCityCrossField extends Field {
    @Override
    public void parse(String field, Data data) {
      String[] parts = MSPACE_PTN.split(field);
      if (parts.length > 4)  abort();

      if (parts.length > 1) {
        data.strCall = parts[0];
        field = parts[1];
        if (parts.length == 3) {
          data.strCross = parts[2];
        } else if (parts.length == 4) {
          data.strPlace = parts[2];
          data.strCross = parts[3];
        }
      }

      else if (data.strCall.length() > 0) {
        if (field.startsWith(data.strCall)) {
          field = field.substring(data.strCall.length()).trim();
        } else {
          data.strCall = "";
        }
      }

      int pt = field.lastIndexOf(',');
      if (pt >= 0) {
        data.strCity = field.substring(pt+1).trim();
        field = field.substring(0, pt).trim();
      }
      field = field.replace('@',  '&');
      if (data.strCall.length() > 0) {
        parseAddress(field, data);
      } else {
        parseAddress(StartType.START_CALL, FLAG_ANCHOR_END, field, data);
      }

      if (data.strCross.length() == 0 && data.strCity.length() > 0) {
        String city = data.strCity;
        data.strCity = "";
        parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, city, data);
        data.strCross = getLeft();
      }
    }

    @Override
    public String getFieldNames() {
      return "CALL ADDR APT CITY PLACE X";
    }
  }

  private static final Pattern NAME_ID_PTN = Pattern.compile("(.*?)(\\[(?:\\d{4}-|Incident).*\\]).*");
  private static final Pattern NAME_PTN = Pattern.compile("[A-Z ]*,[A-Z ]+", Pattern.CASE_INSENSITIVE);
  private static final Pattern NAME_PHONE_PTN = Pattern.compile("([A-Z, ]+?) +(\\(\\d{3}\\) ?\\d{3}-?\\d{4})(?: +.*)?", Pattern.CASE_INSENSITIVE);
  private static final Pattern TIMES_PTN = Pattern.compile("[A-Z0-9]+: .*");
  private class MyIdField extends Field {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      // If this matches the regular Name/ID/Unit pattern parse it as such
      Matcher match = NAME_ID_PTN.matcher(field);
      if (match.matches()) {
        String name = match.group(1).trim();
        data.strCallId = match.group(2);
        match = NAME_PHONE_PTN.matcher(name);
        if (match.matches()) {
          data.strPhone = match.group(2);
          name = match.group(1);
        }
        data.strName = name;
        return true;
      }

      // If it looks like a stand alone name, likewise
      if (NAME_PTN.matcher(field).matches()) {
        data.strName = field;
        return true;
      }

      // See if it matches the name/phone/unit pattern
      match = NAME_PHONE_PTN.matcher(field);
      if (match.matches()) {
        data.strName = match.group(1);
        data.strPhone = match.group(2);
      }

      // If the next field looks like a times field signature, just accept this
      return TIMES_PTN.matcher(getRelativeField(+1)).matches();
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "NAME PHONE ID";
    }
  }

  private static final String[] CITY_LIST = new String[]{

      // Cities
      "ARMA",
      "ARCADIA",
      "CHEROKEE",
      "FRONTENAC",
      "GIRARD",
      "HEPLER",
      "MCCUNE",
      "MULBERRY",
      "PITTSBURG",
      "WALNUT",

      // Townships
      "BAKER",
      "CRAWFORD",
      "GRANT",
      "LINCOLN",
      "OSAGE",
      "SHERIDAN",
      "SHERMAN",
      "WALNUT",
      "WASHINGTON",

      // Census-designated places
      "CHICOPEE",
      "FRANKLIN",

      // Other unincorporated places
      "BEULAH",
      "BRAZILTON",
      "CAMP 50",
      "CAPALDO",
      "CATO",
      "CORNELL",
      "CROWEBURG",
      "CURRANVILLE",
      "DUNKIRK",
      "ENGLEVALE",
      "FARLINGTON",
      "FOXTOWN",
      "FULLER",
      "GREENBUSH",
      "GROSS",
      "KIRKWOOD",
      "KLONDIKE",
      "MIDWAY",
      "MONMOUTH",
      "OPOLIS",
      "RADLEY",
      "RINGO",
      "SOUTH RADLEY",
      "YALE"
  };
}
