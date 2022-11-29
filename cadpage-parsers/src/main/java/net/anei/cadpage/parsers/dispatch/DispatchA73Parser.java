package net.anei.cadpage.parsers.dispatch;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchA73Parser extends FieldProgramParser {

  private Set<String> citySet = null;

  public DispatchA73Parser(String defCity, String defState) {
    this(null, defCity, defState);
  }

  public DispatchA73Parser(String[] cityList, String defCity, String defState) {
    super(defCity, defState,
          "CALL ADDR EMPTY X GPS! INFO/N+");
    if (cityList != null) {
      citySet = new HashSet<>(Arrays.asList(cityList));
    }
  }

  @Override
  public String getFilter() {
    return "@alert.active911.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\\|"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("GPS")) return new GPSField("Lat:.*? Lon:.*", true);
    return super.getField(name);
  }

  private static final Pattern ADDR_APT_PTN = Pattern.compile("(?:APT|UNIT|RM|ROOM|LOT) +(.*)|\\d{1,5}[A-Z]?|[A-Z]", Pattern.CASE_INSENSITIVE);
  private class MyAddressField extends Field {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strCity = p.getLastOptional(':');
      parseAddress(p.get(';'), data);
      while (!p.isEmpty()) {
        String token = p.get(';');
        Matcher match = ADDR_APT_PTN.matcher(token);
        if (match.matches()) {
          String apt = match.group(1);
          if (apt == null) apt = token;
          data.strApt = append(data.strApt, "-", apt);
        } else if (token.startsWith("MM ") || token.startsWith("mm ")) {
          data.strAddress = append(data.strAddress, " ", token);
        } else {
          String ucToken = token.toUpperCase();
          if (ucToken.equals("U") || ucToken.equals("U:")) continue;
          if (ucToken.endsWith(" CO") || ucToken.endsWith(" COUNTY") ||
              (citySet != null && citySet.contains(ucToken))) {
            if (data.strCity.isEmpty()) data.strCity = token;
          } else {
            data.strPlace = append(data.strPlace, " - ", token);
          }
        }
      }
    }

    @Override
    public String getFieldNames() {
      return "ADDR PLACE APT CITY";
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("Intersection of:")) return;
      super.parse(field, data);
    }
  }
}
