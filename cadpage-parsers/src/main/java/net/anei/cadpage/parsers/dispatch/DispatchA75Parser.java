package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class DispatchA75Parser extends FieldProgramParser {

  public DispatchA75Parser(String defCity, String defState) {
    super(defCity, defState,
          "CFS:ID! RUN#:SKIP! CODE:CODE! PLACE:PLACE! LAT:GPS1! LONG:GPS2! ADDR:ADDR! CITY:CITY! CROSS:X! PRI:PRI! DATE:DATE! TIME:TIME! SECT:MAP! DIST:SRC! UNIT:UNIT! DESC:INFO/N+");
  }
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("CFS: *\\d\\d-\\d{6}");
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    int pt = body.indexOf("\n\n\n");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATE")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4}", true);
    if (name.equals("TIME")) return new DateTimeField("\\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
}
