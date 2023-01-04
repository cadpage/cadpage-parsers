package net.anei.cadpage.parsers.MO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Cooper County, MO
 */
public class MOCooperCountyParser extends FieldProgramParser {

  public MOCooperCountyParser() {
    super("COOPER COUNTY", "MO",
          "CALL:CALL! PLACE:PLACE ADDR:ADDR! CITY:CITY! ID:ID! DATE:DATE! TIME:TIME! MAP:MAP! UNIT:UNIT? INFO:INFO? INFO/N+ DIRECTIONS:LINFO/N WARNINGS:ALERT");
  }

  @Override
  public String getFilter() {
    return "cooperdispatch@icstech.org";
  }

  private static final Pattern MARKER = Pattern.compile("EMS:([ #A-Z0-9]+) - +");
  protected boolean parseMsg(String body, Data data) {

    Matcher match = MARKER.matcher(body);
    if (!match.lookingAt()) return false;
    data.strUnit = match.group(1).replace(" ", "");
    body = body.substring(match.end());
    return super.parseFields(body.split("\n"), data);
  }

  @Override
  public String getProgram() {
    return "UNIT? " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("ID")) return new MyIdField();
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("(D?\\d{4}) +(.*)");
  private class MyCallField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_CALL_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCode = match.group(1);
      data.strCall = match.group(2);
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "ALIAS=");
      super.parse(field, data);
    }
  }

  private static final Pattern ADDR_APT_PTN = Pattern.compile("(.*?)[, ]+Apt/Unit[ #]+(.*)");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      String apt = "";
      Matcher match = ADDR_APT_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        apt = match.group(2);
      }
      super.parse(field, data);
      data.strApt = append(data.strApt, "-", apt);
    }
  }

  private class MyIdField extends IdField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "EMS:");
      super.parse(field, data);
    }
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("EMS:", "").replace(" ", "");
      super.parse(field, data);
    }
  }
}
