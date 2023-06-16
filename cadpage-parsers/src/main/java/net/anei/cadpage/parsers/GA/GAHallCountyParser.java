package net.anei.cadpage.parsers.GA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class GAHallCountyParser extends FieldProgramParser {

  public GAHallCountyParser() {
    super("HALL COUNTY", "GA",
          "CALL ADDR EMPTY ID INFO1! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "@hallcounty.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Notification")) return false;
    body = body.replace("(;", "(");
    return parseFields(body.split(";"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("ID")) return new MyIdField();
    if (name.equals("INFO1")) return new MyInfo1Field();
    return super.getField(name);
  }

  private static final Pattern ADDR_INTERSECT_PTN = Pattern.compile("([^,]+), [Xx]([A-Z]{2})/([^,]+), [Xx][A-Z]{2}");
  private static final Pattern ADDR_PLACE_PTN = Pattern.compile("@([^(]+) \\(([^)]+)\\), [Xx]?([A-Z]{2})");
  private static final Pattern ADDR_PTN = Pattern.compile("(.*), [Xx]?([A-Z]{2})");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = stripTrailInfo(field, data);

      Matcher match = ADDR_INTERSECT_PTN.matcher(field);
      if (match.matches()) {
        parseAddress(match.group(1).trim() + " & " + match.group(3).trim(), data);
        data.strCity = convertCodes(match.group(2), CITY_CODES);
      }
      else if ((match = ADDR_PLACE_PTN.matcher(field)).matches()) {
        String place = match.group(1).trim();
        parseAddress(match.group(2).trim(), data);
        if (place.startsWith("MM")) {
          data.strAddress = append(data.strAddress, " ", place);
        } else {
          data.strPlace = place;
        }
        data.strCity = convertCodes(match.group(3), CITY_CODES);
      }
      else if ((match = ADDR_PTN.matcher(field)).matches()) {
        parseAddress(match.group(1).trim(), data);
        data.strCity = convertCodes(match.group(2), CITY_CODES);
      }
      else {
        field = stripFieldEnd(field, "/");
        parseAddress(field, data);
      }
    }

    private String stripTrailInfo(String field, Data data) {
      while (field.endsWith(")")) {
        int cnt = 0;
        int pt = field.length()-1;
        for ( ; pt >= 0; pt--) {
          char chr = field.charAt(pt);
          if (chr == ')') cnt++;
          if (chr == '(') {
            if (--cnt == 0) break;
          }
        }
        if (pt < 0) return field;
        String part = field.substring(pt+1, field.length()-1).trim();
        field = field.substring(0,pt).trim();
        if (part.toUpperCase().startsWith("NEAR:")) {
          data.strPlace = append(part, " - ", data.strPlace);
        } else {
          data.strSupp = append(part, "\n", data.strSupp);
        }
      }
      return field;
    }

    public String getFieldNames() {
      return "PLACE ADDR APT CITY";
    }
  }

  private static final Pattern ID_PTN = Pattern.compile("([A-Z]{3,4}):\\d{4}:\\d+");
  private class MyIdField extends IdField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ID_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strSource = match.group(1);
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "SRC ID";
    }
  }

  private class MyInfo1Field extends InfoField {
    @Override
    public void parse(String field, Data data) {
      data.strSupp = append(field, "\n", data.strSupp);
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BU", "BUFORD",
      "FB", "FLOWERY BRANCH",
      "GI", "GILLSVILLE",
      "GV", "GAINESVILLE",
      "HT", "HOSCHTON",
      "LU", "LULA",
      "MU", "MURRAYVILLE"
  });
}
