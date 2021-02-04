package net.anei.cadpage.parsers.CT;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Middletown, CT
 */
public class CTMiddletownParser extends FieldProgramParser {

  public CTMiddletownParser() {
    super("MIDDLETOWN","CT",
          "ID CALL ADDR SKIP APT CITY ZIP X UNIT DATETIME/d! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "NexgenMessage@middletownctpolice.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("NexgenAlert")) return false;
    return parseFields(body.split("\\|"), 3, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{10}", true);
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d-\\d\\d-\\d{4} +\\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  private static final Pattern APT_PTN = Pattern.compile("(?:Apt|Unit|Room|Lot)[ #]*(.*)");
  private class MyAptField extends AptField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = APT_PTN.matcher(field);
      if (match.matches()) field = match.group(1);
      super.parse(field, data);
    }
  }
}
