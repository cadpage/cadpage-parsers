package net.anei.cadpage.parsers.NJ;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NJUnionCountyBParser extends FieldProgramParser {

  public NJUnionCountyBParser() {
    super("UNION COUNTY", "NJ",
          "Nature:CALL! Address:ADDR! Location:PLACE? City:CITY! Case_#:ID! Time:DATETIME!");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!body.startsWith("Union County Communcations / ")) return false;
    body = body.substring(29).trim();
    return parseFields(body.split(" / "), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new CallField("\\.? *(.*)", true);
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d{4})-(\\d\\d)-(\\d\\d) +(\\d\\d:\\d\\d:\\d\\d)");

  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate =  match.group(2)+'/'+match.group(3)+'/'+match.group(1);
      data.strTime = match.group(4);
    }
  }
}
