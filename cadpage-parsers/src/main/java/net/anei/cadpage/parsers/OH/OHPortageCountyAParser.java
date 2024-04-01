package net.anei.cadpage.parsers.OH;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class OHPortageCountyAParser extends FieldProgramParser {

  private static final Pattern MARKER2 = Pattern.compile("[A-Z0-9\\.]+: +", Pattern.CASE_INSENSITIVE);

  public OHPortageCountyAParser() {
    super(OHPortageCountyParser.CITY_LIST, "PORTAGE", "OH",
          "PREFIX? CALL2 ZERO? ADDR! PLACE? CITY/Y INFO/N+");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "911@ci.ravenna.oh.us,info@sundance-sys.com,sunsrv@sundance-sys.com";
  }

  @Override
  public boolean parseMsg(String body, Data data) {
    Matcher match = MARKER2.matcher(body);
    if (match.lookingAt()) body = body.substring(match.end());
    if (!parseFields(body.split(","), data)) return false;
    data.strCity = OHPortageCountyParser.fixCity(data.strCity);
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("PREFIX")) return new CallField();
    if (name.equals("CALL2")) return new MyCall2Field();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("ZERO")) return new SkipField("0?");
    return super.getField(name);
  }

  private static final Pattern CALL_CODE_PTN = Pattern.compile("(?:\\*+(.*?)\\*+)? *(?:(.*?)[ \\.]+)?([A-Z0-9]{0,8})-([A-Z].*|)");
  private class MyCall2Field extends CallField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = CALL_CODE_PTN.matcher(field);
      if (!match.matches()) {
        if (!field.startsWith("-")) return false;
        field = field.substring(1).trim();
      } else {
        data.strCode = match.group(3);
        field = append(getOptGroup(match.group(1)), " - ", getOptGroup(match.group(2)));
        field = append(getOptGroup(field), " - ", match.group(4).trim());
      }
      data.strCall = append(data.strCall, ", ", field);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("`")) return;
      super.parse(field, data);
    }
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      if (field.endsWith(")")) {
        int pt = field.indexOf('(');
        if (pt < 0) abort();
        String place = field.substring(pt+1, field.length()-1).trim();
        field = field.substring(0, pt).trim();

        if (!data.strPlace.startsWith(place)) {
          data.strPlace = append(data.strPlace, " - ", place);
        }
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }

  private class MyCityField extends CityField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.length() == 0) return true;
      return super.checkParse(field, data);
    }
  }

  @Override
  public String postAdjustMapAddress(String addr) {
    return OHPortageCountyParser.fixMapAddress(addr);
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[] {
      "9428 VISTA CT",                        "+41.240918,-81.354969",
      "9429 VISTA CT",                        "+41.240508,-81.354968",
      "9430 VISTA CT",                        "+41.240918,-81.354969",
      "9431 VISTA CT",                        "+41.240508,-81.354968",
      "9432 VISTA CT",                        "+41.240918,-81.354969",
      "9433 VISTA CT",                        "+41.240508,-81.354968",
      "9434 VISTA CT",                        "+41.240918,-81.354969",
      "9435 VISTA CT",                        "+41.240508,-81.354968",
      "9436 VISTA CT",                        "+41.241229,-81.354847",
      "9437 VISTA CT",                        "+41.240512,-81.355409",
      "9438 VISTA CT",                        "+41.241229,-81.354847",
      "9439 VISTA CT",                        "+41.240512,-81.355409",
      "9440 VISTA CT",                        "+41.241229,-81.354847",
      "9441 VISTA CT",                        "+41.240512,-81.355409",
      "9442 VISTA CT",                        "+41.241229,-81.354847",
      "9443 VISTA CT",                        "+41.240512,-81.355409",
      "9444 VISTA CT",                        "+41.241409,-81.355088",
      "9445 VISTA CT",                        "+41.240529,-81.355852",
      "9446 VISTA CT",                        "+41.241409,-81.355088",
      "9447 VISTA CT",                        "+41.240529,-81.355852",
      "9448 VISTA CT",                        "+41.241409,-81.355088",
      "9449 VISTA CT",                        "+41.240529,-81.355852",
      "9450 VISTA CT",                        "+41.241409,-81.355088",
      "9451 VISTA CT",                        "+41.240529,-81.355852",
      "9453 VISTA CT",                        "+41.240782,-81.355734",
      "9455 VISTA CT",                        "+41.240782,-81.355734",
      "9457 VISTA CT",                        "+41.240782,-81.355734",
      "9459 VISTA CT",                        "+41.240782,-81.355734",
      "9461 VISTA CT",                        "+41.240953,-81.355241"

  });
}