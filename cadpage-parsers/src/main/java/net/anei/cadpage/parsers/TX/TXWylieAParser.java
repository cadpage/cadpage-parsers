package net.anei.cadpage.parsers.TX;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Wylie, TX
 */

public class TXWylieAParser extends FieldProgramParser {

  public TXWylieAParser() {
    super("", "TX",
        "( NATURE:CALL! BOX:BOX? ADDRESS:ADDR! ( CROSS_STREET(S):X! | INTERSECTS_WITH:X | ) CH1+? CITY2 UNITS:UNIT! " +
        "| CALL! BOX:BOX? ADDR! ( CROSS_STREET(S):X | INTERSECTS_WITH:X | ) ( CHANNEL:CH | ) SKIP+? CITY1! " +
        ")");
    setupProtectedNames("BUTSCHERS BLOCK");
  }

  @Override
  public String getFilter() {
    return "wyliefiredispatch@gmail.com,wyliedispatch@wylietexas.gov";
  }

  private static final Pattern SUBJECT_PATTERN = Pattern.compile("(?:Primary: )?(?:ALERT \\- (.+)|String Match .*)");
  private static final Pattern BODY_PATTERN = Pattern.compile("^CITY OF WYLIE DISPATCH\\n\\n(.*)", Pattern.DOTALL);

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    Matcher m = SUBJECT_PATTERN.matcher(subject);
    if (!m.lookingAt()) return false;
    data.strUnit = getOptGroup(m.group(1));
    m = BODY_PATTERN.matcher(body);
    if (!m.matches()) return false;
    body = m.group(1).trim();

    return parseFields(body.split("\n"), data);
  }

  @Override
  public String getProgram() {
    return "UNIT " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("CH1")) return new MyChannel1Field();
    if (name.equals("CITY1")) return new CityField("IN +(.*)", true);
    if (name.equals("CITY2")) return new CityField("CITY +(.*)", true);
    return super.getField(name);
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }

  private static final Pattern CH1_PTN = Pattern.compile("([A-Z]+) C?HANNEL: *(.*)");
  private class MyChannel1Field extends ChannelField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = CH1_PTN.matcher(field);
      if (!match.matches()) return false;
      field = append(match.group(1), "-", match.group(2));
      super.parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
}
