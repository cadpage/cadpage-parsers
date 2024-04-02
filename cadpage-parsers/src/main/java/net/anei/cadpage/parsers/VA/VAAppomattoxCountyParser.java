package net.anei.cadpage.parsers.VA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Appomattox County, VA
 */
public class VAAppomattoxCountyParser extends FieldProgramParser {

  public VAAppomattoxCountyParser() {
    super("APPOMATTOX COUNTY", "VA",
          "ID ADDRCITYST APT SRC SRC/C? PRI? X CALL! APT INFO/N+");
  }

  @Override
  public String getFilter() {
    return "nzuercherportal@appomattoxcountyva.gov";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    data.strUnit = new Parser(subject).getOptional(';');
    String[] flds = body.split(";", -1);
    for (int ii = 0; ii < flds.length; ii++) {
      if (flds[ii].trim().equals("None")) flds[ii] = "";
    }
    return parseFields(flds, data);
  }

  @Override
  public String getProgram() {
    return "UNIT " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("CFS\\d{4}-\\d{5}", true);
    if (name.equals("PRI")) return new PriorityField("ALPHA|BRAVO|CHARLIE|DELTA|ECHO", true);
    if (name.equals("APT")) return new AptField("(?:APT|LOT|ROOM|RM)?\\b *(.*)");
    if (name.equals("SRC")) return new SourceField("[A-Z]+SQD", false);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern INFO_HDR_PTN = Pattern.compile("^\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - +");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_HDR_PTN.matcher(field).replaceFirst("");
      super.parse(field, data);
    }
  }
}