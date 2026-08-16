package net.anei.cadpage.parsers.MN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MNLincolnCountyParser extends FieldProgramParser {

  public MNLincolnCountyParser() {
    super("LINCOLN COUNTY", "MN",
          "ID! Location:ADDRCITYST! Loc._Details:INFO! Coordinates:GPS! Call_Notes:INFO! END");
  }

  @Override
  public String getFilter() {
    return "noreply@co.nobles.mn.us";
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("(\\d\\d/\\d\\d/\\d\\d) (\\d\\d:\\d\\d(?::\\d\\d)?) [-\\* ]*(.*)");
  private static final Pattern MASTER = Pattern.compile("([^,]*), *([ A-Z]+)\\b(?:, *([A-Z]{2}) *\\d{5})? *(.*)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strDate = match.group(1);
    data.strTime =  match.group(2);
    data.strCall = match.group(3);

    if (body.startsWith("CFS# ")) {
      body = body.substring(5).trim();
      return super.parseMsg(body, data);
    } else {
      setFieldList("ADDR CITY ST INFO");
      if (body.endsWith(" None")) {
        body = body.substring(0, body.length()-5).trim();
      } else {
        String[] flds = INFO_BRK_PTN.split(body);
        body = flds[0];
        for ( int jj = 1; jj < flds.length; jj++) {
          data.strSupp = append(data.strSupp, "\n", flds[jj]);
        }
      }

      match = MASTER.matcher(body);
      if (!match.matches())  return false;
      parseAddress(match.group(1).trim(), data);
      data.strCity = match.group(2).trim();
      data.strState = getOptGroup(match.group(3));
      data.strSupp = append(match.group(4), "\n", data.strSupp);
      return true;
    }
  }

  @Override
  public String getProgram() {
    return "DATE TIME CALL " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[; ]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
      data.strSupp = append(data.strSupp, "\n", field);
    }
  }
}
