package net.anei.cadpage.parsers.TX;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Wylie, TX
 */

public class TXWylieParser extends FieldProgramParser {
  private static final Pattern SUBJECT_PATTERN = Pattern.compile("ALERT \\- (.+)|String Match .*");
  private static final Pattern BODY_PATTERN = Pattern.compile("^CITY OF WYLIE DISPATCH\\n\\n(.*)", Pattern.DOTALL);

  public TXWylieParser() {
    super("", "TX",
        "CALL! BOX:BOX? ADDR! ( CROSS_STREET(S):X | INTERSECTS_WITH:X | ) ( CHANNEL:CH | ) SKIP+? CITY!");
    setupProtectedNames("BUTSCHERS BLOCK");
  }

  @Override
  public String getFilter() {
    return "wyliefiredispatch@gmail.com";
  }

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
  if (name.equals("CITY")) return new CityField("IN +(.*)", true);
  return super.getField(name);
}

}
