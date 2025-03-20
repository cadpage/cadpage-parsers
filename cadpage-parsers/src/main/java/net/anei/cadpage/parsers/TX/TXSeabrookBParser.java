package net.anei.cadpage.parsers.TX;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXSeabrookBParser extends FieldProgramParser {

  public TXSeabrookBParser() {
    super(TXSeabrookParser.CITY_LIST, "HARRIS COUNTY", "TX",
          "CAD#:ID! Call:CALL! UNIT:UNIT? ADDR:ADDRCITYST/S! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "messaging@iamresponding.com,@dispatches.iamresponding.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace(" NOTES::", "\nNOTES::");
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace(' ', ',').replace("UNIT:", "");
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_HDR_PTN = Pattern.compile("NOTES::[A-Z]+-\\d+: *");

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_HDR_PTN.matcher(field);
      if (match.lookingAt()) field = field.substring(match.end());
      super.parse(field, data);
    }
  }
}
