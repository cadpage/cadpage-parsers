package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHWayneCountyDParser extends FieldProgramParser {

  public OHWayneCountyDParser() {
    this("WAYNE COUNTY", "OH");
  }

  protected OHWayneCountyDParser(String defCity, String defState) {
    super(defCity, defState,
          "( CALL:CALL! | CALL! ) PLACE:PLACE? ADDR:ADDR/S6! CITY:CITY! XSTREET:X? ID:ID! UNIT:UNIT! INFO:INFO! INFO/N+ CROSS:X END");
  }

  @Override
  public String getAliasCode() {
    return "OHWayneCountyD";
  }

  @Override
  public String getFilter() {
    return "info@sundance-sys.com,sunsrv@sundance-sys.com";
  }

  private static final Pattern FLAG_PTN = Pattern.compile("\\*{2,}([-/ A-Z0-9]+)\\*{2,} *");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("From: ")) return false;
    data.strSource = subject.substring(6).trim();
    String flag = "";
    Matcher match = FLAG_PTN.matcher(body);
    if (match.lookingAt()) {
      flag = match.group(1);
      body = body.substring(match.end());
    }
    String[] flds = body.split("\n");
    if (flds.length > 3) {
      if (!parseFields(flds, data)) return false;
    } else {
      if (!super.parseMsg(body, data)) return false;
    }
    data.strCall = append(flag, " - ", data.strCall);

    data.strCity = new Parser(data.strCity).get('/');
    if (data.strCity.equals("NR")) data.strCity = "N RANDALL";
    return true;
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      if (field.endsWith(")")) {
        int pt = field.indexOf('(');
        if (pt >= 0) {
          String place = field.substring(pt+1, field.length()-1).trim();
          field = field.substring(0,pt).trim();
          if (!data.strPlace.contains(place)) {
            if (place.contains(data.strPlace)) {
              data.strPlace = place;
            } else {
              data.strPlace = append(data.strPlace, " - ", place);
            }
          }
        }
      }
      String apt = "";
      int pt = field.indexOf(',');
      if (pt >= 0) {
        apt = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
      }
      super.parse(field, data);
      data.strApt = append(data.strApt, "-", apt);
    }
  }
}
