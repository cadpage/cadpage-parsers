package net.anei.cadpage.parsers.CT;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class CTNewLondonCountyEParser extends FieldProgramParser {

  public CTNewLondonCountyEParser() {
    super("NEW LONDON COUNTY", "CT",
          "COMPLAINT_TYPE:CALL! ADDRESS:ADDR! DISPATCH_TIME:TIME! NARRATIVE:INFO! UNITS_ASSIGN:UNIT! CAUTION_NOTES:ALERT! PRIORITY_RESP:PRI!");
  }

  @Override
  public String getFilter() {
    return "CADPaging@waterfordct.org";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile(",(?=\\[\\d+\\])");
  private static final Pattern DUP_NUMBER_PTN = Pattern.compile("\\[\\d+\\] (\\[\\d+\\])");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      for (String line : INFO_BRK_PTN.split(field)) {
        Matcher match = DUP_NUMBER_PTN.matcher(line);
        if (match.lookingAt()) line = line.substring(match.start(1));
      }
    }
  }
}
