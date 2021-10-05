package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;

public class DispatchA80Parser extends FieldProgramParser {

  public DispatchA80Parser(String defCity, String defState) {
    super(defCity, defState,
         "( SELECT/2 ADDRCITY PLACE? UNIT2 CALL! INFO+ " +
         "| CALL:CALL1! PLACE:PLACE? ( ADDR:ADDR! CITY:CITY! ID:ID! | ID:ID! ADDR! CITY_ST! ) DATE:DATE! TIME:TIME! MAP:MAP? UNIT:UNIT% INFO:INFO/N+ DIRECTIONS:INFO/N+ WARNINGS:ALERT/N+ )");
  }

  @Override
  public String getAliasCode() {
    return "MOStFrancoisCountyB";
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }

  private static final Pattern MARKER = Pattern.compile("DISPATCH:([_A-Z0-9]+:[- A-Z0-9]+) - +");

  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = MARKER.matcher(body);
    if (match.lookingAt()) {
      setSelectValue("1");
      data.strSource = match.group(1);
      body = body.substring(match.end());
     if (!parseFields(body.split("\n"), data)) return false;
    }

    else {
      setSelectValue("2");
      if (!parseFields(body.split("//"), data)) return false;
    }
    data.strPlace = stripFieldStart(data.strPlace, "ALIAS=");
    return true;
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL1")) return new BaseCall1Field();
    if (name.equals("CITY_ST")) return new BaseCityStateField();
    if (name.equals("DATE"))  return new DateField("\\d\\d/\\d\\d/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("UNIT2")) return new UnitField("[A-Z0-9]+:[A-Z0-9]+(?:,[A-Z0-9]+:[A-Z0-9]+)*", true);
    return super.getField(name);
  }

  private class BaseCall1Field extends CallField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(' ');
      if (pt >= 0) {
        data.strCode = field.substring(0, pt);
        field = field.substring(pt+1).trim();
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private static final Pattern CITY_ST_ZIP_PTN = Pattern.compile("([^,]*)(?:, *([A-Z]{2})(?: +\\d{5})?)?");
  private class BaseCityStateField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = CITY_ST_ZIP_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCity = match.group(1).trim();
      data.strState = getOptGroup(match.group(2));
    }

    @Override
    public String getFieldNames() {
      return "CITY ST";
    }
  }
}
