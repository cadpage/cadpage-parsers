package net.anei.cadpage.parsers.MO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MOJacksonCountyCParser extends FieldProgramParser {

  public MOJacksonCountyCParser() {
    super("JACKSON COUNTY", "MO",
          "Date:DATETIME! ID! Location:ADDR! CITY! ST ( Latitude:GPS1! Longitude:GPS2! | ) Map_Grid:MAP! Units_Responding:UNIT! CFS_#:ID/L! " +
              "Nearest_Intersection:X! Subdivision:LINFO! Response_Zone:LINFO! Hazmat:LINFO! Other_Hazardous:LINFO! END");
  }

  @Override
  public String getFilter() {
    return "zmail.relay@cityofls.net";
  }

  private static final Pattern SPC_SUBJECT_PTN = Pattern.compile("CFS - .* - #([A-Z]{2}\\d{6}-\\d{3})");
  private static final Pattern SPC_MASTER = Pattern.compile("There is a (.*) at ([^,]+), *([ A-Z]+), ([A-Z]{2})\\b *\\d*");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SPC_SUBJECT_PTN.matcher(subject);
    if (match.matches()) {
      setFieldList("ID CODE CALL ADDR APT CITY ST");
      data.strCallId = match.group(1);

      if (body.startsWith("Incident Code:")) {
        int pt = body.indexOf("\n", 14);
        if (pt < 0) return false;
        data.strCode = body.substring(14, pt).trim();
        pt = body.indexOf("\nThere is a ");
        if (pt < 0) return false;
        body = body.substring(pt+1);
        pt = body.indexOf("\nCFS:");
        if (pt >= 0) body = body.substring(0,pt).trim();
      }

      match = SPC_MASTER.matcher(body);
      if (!match.matches()) return false;
      data.strCall = match.group(1);
      parseAddress(match.group(2), data);
      data.strCity = match.group(3).trim();
      data.strState = match.group(4);
      return true;
    }

    data.strCall = subject;
    return parseFields(body.split("[,\\|]"), data);
  }

  @Override
  public String getProgram() {
    return "CALL " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d", true);
    if (name.equals("ST")) return new StateField("([A-Z]{2})\\b.*");
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("; ", ",");
      super.parse(field, data);
    }
  }
}
