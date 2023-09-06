package net.anei.cadpage.parsers.MO;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Monett, MO
 */

public class MOLawrenceCountyBParser extends FieldProgramParser{

  public MOLawrenceCountyBParser() {
    super("LAWRENCE COUNTY", "MO",
          "Address:ADDRCITY! Category:CALL! SubCategory:CALL! Open:DATETIME! Dispatch:DATETIME! Enroute:SKIP! Arrival:SKIP! Closed:SKIP! EMPTY NAME+? INFOMAP+");
  }

  @Override
  public String getFilter() {
    return "noreply@omnigo.com";
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

  private static final Pattern SUBJECT_SRC_PTN = Pattern.compile("(.*?) (?:ACTIVE ?911|CALLS?)", Pattern.CASE_INSENSITIVE);

  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_SRC_PTN.matcher(subject);
    if (match.matches()) data.strSource = match.group(1);
    return parseFields(body.split("\n"), 2, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("NAME")) return new MyNameField();
    if (name.equals("CALL_RECEIVED")) return new SkipField("Call Received on .*");
    if (name.equals("INFOMAP")) return new MyInfoMapField();
    return super.getField(name);
  }

  private static final Pattern ALPHA_CHECK_PTN = Pattern.compile(".*[A-Za-z].*");

  private class MyNameField extends NameField {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (field.equals(",")) return true;
      if (!field.contains(",") || !ALPHA_CHECK_PTN.matcher(field).find()) return false;
      parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, ",");
      field = stripFieldEnd(field, ",");
      data.strName = append(data.strName, " / ", field);
    }
  }

  private class MyInfoMapField extends InfoField {

    @Override
    public void parse(String field, Data data) {
      field = cleanWirelessCarrier(field);
      if (field.startsWith("Call Received on")) return;
      if (field.startsWith("Address:")) return;
      if (field.startsWith("Location:")) {
        data.strMap = field.substring(9).trim();
      } else {
        data.strSupp = append (data.strSupp, "\n", field);
      }
    }

    @Override
    public String getFieldNames() {
      return "PLACE MAP INFO";
    }
  }
}
