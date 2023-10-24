package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class PABlairCountyBParser extends FieldProgramParser {

  public PABlairCountyBParser() {
    super(CITY_LIST, "BLAIR COUNTY", "PA",
          "CALL ADDRCITY PLACE PLACE! Due:UNIT! END");
  }

  @Override
  public String getFilter() {
    return "alerts@centre.ealert911.com";
  }

  private static final Pattern DELIM = Pattern.compile("\n| +(?=Due:)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals(".")) return false;
    return parseFields(DELIM.split(body), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("PLACE")) return new MyPlaceField();
    return super.getField(name);
  }

  private static final Pattern ADDR_APT_PTN = Pattern.compile("(.*) Apartment (.*)", Pattern.CASE_INSENSITIVE);
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      super.parse(field, data);

      Matcher match = ADDR_APT_PTN.matcher(data.strAddress);
      if (match.matches()) {
        data.strAddress = match.group(1).trim();
        data.strApt =  match.group(2).trim();
      }

      parseAddress(StartType.START_OTHER, FLAG_ONLY_CITY | FLAG_ANCHOR_END, data.strCity, data);
      data.strCity = stripFieldEnd(data.strCity, " BORO");
    }
  }

  private static final Pattern APT_PTN = Pattern.compile("APT[. #]+(.*)|\\d{1,4}[A-Z]?|[A-Z]");
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = APT_PTN.matcher(field);
      if (match.matches()) {
        String tmp = match.group(1);
        if (tmp != null) field = tmp;
        if (!field.equals(data.strApt)) data.strApt = append(data.strApt, "-", field);
      } else {
        data.strPlace = append(data.strPlace, " - ", field);
      }
    }

    @Override
    public String getFieldNames() {
      return "APT PLACE";
    }
  }

  private static final String[] CITY_LIST = new String[] {

      // City
      "ALTOONA",

      // Boroughs
      "BELLWOOD",
      "DUNCANSVILLE",
      "HOLLIDAYSBURG",
      "MARTINSBURG",
      "NEWRY",
      "ROARING SPRING",
      "TUNNELHILL",
      "TYRONE",
      "WILLIAMSBURG"
  };
}
