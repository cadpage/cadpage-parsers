package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class PADauphinCountyCParser extends FieldProgramParser {

  public PADauphinCountyCParser() {
    super("DAUPHIN COUNTY", "PA",
          "CALL:CALL! PLACE:PLACE? ADDR:ADDR! ( LAT:GPS1 LNG:GPS2! | ) CITY:CITY! ID:ID! DATE:DATE! TIME:TIME! UNIT:UNIT! NOTES:INFO/N+");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("DATE")) return new DateField("(\\d\\d?/\\d\\d?/\\d{4})\\b.*", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strPlace = append(data.strPlace, " - ", p.getLastOptional(", "));
      String apt = p.getLastOptional(" - ");
      super.parse(p.get(), data);
      data.strApt = append(data.strApt, "-", apt);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }

  private static final Pattern INFO_HDR_PTN = Pattern.compile("\\d\\d?/\\d\\d?/\\d\\d(?:\\d\\d)? \\d\\d?:\\d\\d:\\d\\d\\b *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_HDR_PTN.matcher(field);
      if (match.lookingAt()) field = field.substring(match.end());
      super.parse(field, data);
    }
  }
}
