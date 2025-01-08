package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NCStanlyCountyDParser extends FieldProgramParser {

  public NCStanlyCountyDParser() {
    super("STANLY COUNTY", "NC",
          "ID CALL PLACE ADDR X UNIT CH! END");
  }

  @Override
  public String getFilter() {
    return "888";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("EVT# +(\\d+)", true);
    if (name.equals("CALL")) return new CallField("TYPE +(.*?) *-?", true);
    if (name.equals("PLACE")) return new PlaceField("NAME\\b *(.*)", true);
    if (name.equals("ADDR")) return new AddressField("LOC +(.*)", true);
    if (name.equals("X")) return new CrossField("XST\\b *(.*)", true);
    if (name.equals("UNIT")) return new UnitField("UNIT\\(S\\) *(.*)", true);
    return super.getField(name);
  }

}
