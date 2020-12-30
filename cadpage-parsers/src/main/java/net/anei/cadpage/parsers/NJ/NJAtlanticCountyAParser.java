package net.anei.cadpage.parsers.NJ;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NJAtlanticCountyAParser extends FieldProgramParser {

  public NJAtlanticCountyAParser() {
    super("ATLANTIC COUNTY", "NJ",
          "CALL:CALL! PLACE:PLACE! ADDR:ADDR/iS6! CITY:CITY! CROSS:X! MAP:MAP DATE:DATETIME INFO:INFO! INFO/N+ ID:ID! GPS:GPS");
    removeWords("LA", "NEW", "TERRACE");
    setupSpecialStreets("ATLANTIC GARDENS PARK");
  }

  @Override
  public String getFilter() {
    return "CAD@ehtpd.com";
  }

  private Set<String> unitSet = new HashSet<>();

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("DISPATCH") && !subject.equals("Dispatch") &&
        !subject.equals("Phoenix Notification")) return false;

    unitSet.clear();
    if (!super.parseFields(body.split("\n"), data)) return false;

    if (!data.strApt.isEmpty()) {
      data.strApt = stripCity(data.strApt, data.strCity);
    } else {
      data.strAddress = stripCity(data.strAddress, data.strCity);
    }
    return true;
  }

  private String stripCity(String field, String city) {
    int pt = field.indexOf(city);
    return pt >= 0 ? field.substring(0,pt).trim() : field;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("DATETIME")) return new DateField("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("ID")) return new IdField("\\d{4}-\\d{8}");
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      String apt = "";
      int pt = field.indexOf(',');
      if (pt >= 0) {
        apt = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
      }

      super.parse(field, data);
      data.strApt = append(data.strApt, "-", apt);
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT";
    }
  }

  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strCity = p.get(',');
      data.strState = p.get(' ');
    }

    @Override
    public String getFieldNames() {
      return "CITY ST";
    }
  }

  private static final Pattern MSPACE_PTN = Pattern.compile(" {2,}");

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      if (field.equals("No Cross Streets Found")) return;
      field = MSPACE_PTN.matcher(field).replaceAll(" ");
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_MARK_PTN = Pattern.compile("(Station Dispatched : )|(Units Recommended: )|(Dispatched: )|(Call Back Information : )");
  private static final Pattern INFO_PHONE_PTN = Pattern.compile("\\d{3}-?\\d{3}-?\\d{4}");

  private class MyInfoField extends InfoField {

    @Override
    public void parse(String field, Data data) {
      int lastPt = 0;
      int type = 0;
      Matcher match = INFO_MARK_PTN.matcher(field);
      while (match.find()) {
        processInfoPart(type, field.substring(lastPt, match.start()), data);
        type = getMatchType(match);
        lastPt = match.end();
      }
      processInfoPart(type, field.substring(lastPt), data);
    }

    private int getMatchType(Matcher match) {
      for (int ndx = 1; ; ndx++) if (match.group(ndx) != null) return ndx;
    }

    private void processInfoPart(int type, String part, Data data) {
      part = part.trim();

      switch (type) {
      case 1:
        part = parseUnitInfo(part, data);
        break;

      case 2:
        part = parseUnitInfo(part, data);
        int pt = part.indexOf(';');
        if (pt < 0) return;
        part = part.substring(pt+1).trim();
        break;

      case 3:
        part = parseUnitInfo(part, data);
        break;

      case 4:
        Matcher match = INFO_PHONE_PTN.matcher(part);
        if (match.lookingAt()) {
          data.strPhone = match.group();
          part = part.substring(match.end()).trim();
        }
      }
      data.strSupp = append(data.strSupp, "\n", part);
    }

    private String parseUnitInfo(String field, Data data) {
      int pt = 0;
      while (pt < field.length() && field.charAt(pt) == '{') {
        int pt2 = field.indexOf('}', pt+1);
        if (pt2 < 0) break;
        String unit = field.substring(pt+1, pt2).trim();
        if (unitSet.add(unit)) data.strUnit = append(data.strUnit, ",", unit);
        pt = pt2+1;
        while (pt < field.length() && (field.charAt(pt)==' ' || field.charAt(pt)==',')) pt++;
      }
      return field.substring(pt);
    }

    @Override
    public String getFieldNames() {
      return "UNIT PHONE INFO";
    }
  }
}
