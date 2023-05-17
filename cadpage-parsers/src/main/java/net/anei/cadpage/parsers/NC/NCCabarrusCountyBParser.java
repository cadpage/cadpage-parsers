package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;


public class NCCabarrusCountyBParser extends DispatchOSSIParser {

  private static final Pattern MARKER = Pattern.compile("^(?:\\d{2,4}|CAD):");

  public NCCabarrusCountyBParser() {
    super(NCCabarrusCountyParser.CITY_CODES, "CABARRUS COUNTY", "NC",
          "( CANCEL ADDR CITY EXTRA+ " +
          "| FYI? ( DATETIME CALL CH? ADDR! X+? INFO+ " +
                 "| DIGIT CALL ( ( UNIT | PRI/Z UNIT ) UNIT+? CH? ADDR! X+? EXTRA+? DATETIME " +
                              "| DIGIT? CH? ADDR! X+? EXTRA+? ( PRI | UNIT ) UNIT+? DATETIME " +
                              ") EXTRA+? ID " +
                 ") " +
          ")");
    setupMultiWordStreets("A T ALLEN SCHOOL");
    setupGpsLookupTable(NCCabarrusCountyParser.GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "93001";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = MARKER.matcher(body);
    if (!match.find()) return false;
    body = "CAD:" + body.substring(match.end()).trim();
    body = body.replace('|', ';');
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("FYI")) return new SkipField("FYI:|Update:", true);
    if (name.equals("DIGIT")) return new SkipField("[\\dP]", true);
    if (name.equals("CH")) return new ChannelField("OPS\\d*");
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("PRI")) return new PriorityField("\\d{1,2}", true);
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("EXTRA")) return new MyExtraField();
    if (name.equals("ID")) return new IdField("\\d{7}", true);
    return super.getField(name);
  }

  private static Pattern X_PTN = Pattern.compile("\\bRAMP\\b");
  private class MyCrossField extends CrossField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (X_PTN.matcher(field).find()) {
        super.parse(field, data);
        return true;
      } else {
        return super.checkParse(field, data);
      }
    }
  }

  private static final Pattern UNIT_PTN = Pattern.compile("\\d[A-Z0-9]{1,3}|[A-Z]\\d{1,3}[A-Z]?");
  private class MyUnitField extends UnitField {

    public MyUnitField() {
      setPattern(UNIT_PTN, true);
    }

    @Override
    public void parse(String field, Data data) {
      data.strUnit = append(data.strUnit, " ", field);
    }
  }

  private static final Pattern APT_PTN = Pattern.compile("APT *(.*)");
  private static final Pattern PHONE_PTN = Pattern.compile("\\d{10}");
  private static final Pattern SKIP_PTN = Pattern.compile("\\d{3,6}");
  private class MyExtraField extends Field {
    @Override
    public void parse(String field, Data data) {

      String city = NCCabarrusCountyParser.CITY_CODES.getProperty(field);
      if (city != null) {
        data.strCity = city;
        return;
      }

      Matcher match = APT_PTN.matcher(field);
      if (match.matches()) {
        String apt = match.group(1);
        field = "";
        int pt = apt.indexOf("(S)");
        if (pt >= 0) {
          field = apt.substring(pt+3).trim();
          apt = apt.substring(0, pt).trim();
        }
        data.strApt = append(data.strApt, "-", apt);
        if (field.length() == 0) return;
      }

      match = PHONE_PTN.matcher(field);
      if (match.matches()) {
        data.strPhone = append(data.strPhone, " / ", field);
        return;
      }

      if (field.startsWith("(S)") || field.startsWith("(N)")) {
        field = field.substring(3).trim();
        int pt = field.indexOf("(N)");
        if (pt < 0) {
          data.strPlace = field;
        } else {
          String p1 = field.substring(0,pt);
          String p2 = field.substring(pt+3).trim();
          if (p1.startsWith(p2)) {
            data.strPlace = p1;
          } else if (p2.startsWith(p1)) {
            data.strPlace = p2;
          } else {
            data.strPlace = append(p1, " - ", p2);
          }
        }
        return;
      }

      match = SKIP_PTN.matcher(field);
      if (match.matches()) return;
      data.strPlace = append(data.strPlace, " - ", field);
    }

    @Override
    public String getFieldNames() {
      return "CITY PHONE PLACE";
    }
  }
}
