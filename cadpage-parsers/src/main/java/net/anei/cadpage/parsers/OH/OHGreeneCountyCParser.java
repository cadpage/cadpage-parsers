package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHGreeneCountyCParser extends FieldProgramParser {

  public OHGreeneCountyCParser() {
    super("GREENE COUNTY", "OH",
          "Units:UNIT! Response_Type:CALL? Call_Type:CALL! Loc.Name:PLACE! Area:MAP! Street:ADDR! X.Street:X! Apt#:APT! Bld#:APT! ");
  }

  @Override
  public String getFilter() {
    return "monitor@firstinalerting.com";
  }

  private static final Pattern NA_FLD_PTN = Pattern.compile("([^:]+:) *n/a");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("Alert:")) return false;
    body = body.replace(" Call Type:", "\nCall Type:");
    String[] flds = body.split("\n");
    for (int jj = 0; jj < flds.length; jj++) {
      Matcher match = NA_FLD_PTN.matcher(flds[jj].trim());
      if (match.matches()) flds[jj] = match.group(1);
    }
    return parseFields(flds, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("APT")) return new MyAptField();
    return super.getField(name);
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      data.strCall = append(field, " - ", data.strCall);
    }
  }

  private class MyAptField extends AptField {
    @Override
    public void parse(String field, Data data) {
      data.strApt = append(field, "-", data.strApt);
    }
  }
}
