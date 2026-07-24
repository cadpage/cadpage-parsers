package net.anei.cadpage.parsers.TX;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXCollinCountyDParser extends FieldProgramParser {

  public TXCollinCountyDParser() {
    super("COLLIN COUNTY", "TX",
          "CITY_OF_WYLIE_DISPATCH%EMPTY! NATURE:CALL! BOX:BOX? ADDRESS:ADDR! ( CROSS_STREET(S):X! | INTERSECTS_WITH:X! | ) CH+? CITY! UNITS:UNIT? END");
  }

  @Override
  public String getFilter() {
    return "wyliefiredispatch@gmail.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("Primary:")) return false;
    if (!parseFields(body.split("\n+"), data)) return false;
    if (data.strUnit.isEmpty()) {
      int pt = subject.lastIndexOf("ALERT - ");
      if (pt >= 0) {
        data.strUnit = subject.substring(pt+8).trim();
      }
    }
    return true;
  }

  @Override
  public String getProgram() {
    return "UNIT? " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CH")) return new MyChannelField();
    if (name.equals("CITY")) return new CityField("(?:CITY|IN)\\b *(.*)|()", true);
    return super.getField(name);
  }

  private static final Pattern CHANNEL_PTN = Pattern.compile("([A-Z]+) C?HANNEL: *(.*)");

  private class MyChannelField extends ChannelField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = CHANNEL_PTN.matcher(field);
      if (!match.matches()) return false;
      field = match.group(1) + '-' + match.group(2);
      data.strChannel = append(data.strChannel, "/", field);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
}
