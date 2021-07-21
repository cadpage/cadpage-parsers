package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class OHPortageCountyAParser extends FieldProgramParser {

  private static final Pattern MARKER1 = Pattern.compile(".*?\\b(?=CALL:)");
  private static final Pattern MARKER2 = Pattern.compile("[A-Z0-9\\.]+: +", Pattern.CASE_INSENSITIVE);

  public OHPortageCountyAParser() {
    super(OHPortageCountyParser.CITY_LIST, "PORTAGE", "OH",
          "( SELECT/1 PREFIX CALL:CALL/SDS! PLACE:PLACE! ADDR:ADDR! CITY:CITY! ID:ID! UNIT:UNIT! INFO:INFO/N+ " +
          "| PREFIX? CALL2 ZERO? ADDR! PLACE? CITY/Y INFO/N+ )");
  }

  @Override
  public String getFilter() {
    return "911@ci.ravenna.oh.us,info@sundance-sys.com,sunsrv@sundance-sys.com";
  }

  @Override
  public boolean parseMsg(String body, Data data) {

    Matcher match = MARKER1.matcher(body);
    if (match.lookingAt()) {
      setSelectValue("1");
      int pt = match.end();
      if (pt >= 0) body = body.substring(0, pt) + '\n' + body.substring(pt);
      return parseFields(body.split("\n"), data);
    }

    setSelectValue("2");
    match = MARKER2.matcher(body);
    if (match.lookingAt()) body = body.substring(match.end());
    if (!parseFields(body.split(","), data)) return false;
    data.strCity = OHPortageCountyParser.fixCity(data.strCity);
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("PREFIX")) return new CallField();
    if (name.equals("CALL2")) return new MyCall2Field();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("ZERO")) return new SkipField("0?");
    return super.getField(name);
  }

  private static final Pattern CALL_CODE_PTN = Pattern.compile("(?:\\*+(.*?)\\*+)? *(?:(.*?)[ \\.]+)?([A-Z0-9]{0,7})-([A-Z].*|)");
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
}