package net.anei.cadpage.parsers.OR;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ORKlamathCountyBParser extends FieldProgramParser {

  public ORKlamathCountyBParser() {
    super("KLAMATH COUNTY", "OR",
          "DATETIME CODE_CALL ADDRCITY PLACE X! X+ Priority:PRI! CFS_Number:SKIP! Units:UNIT? Primary_Incident:ID! INFO/N+");
    setupGpsLookupTable(ORKlamathCountyParser.GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "paging@klamath911.gov";
  }

  private static final Pattern DELIM = Pattern.compile("(?<=\\D)/|/(?=//D)");
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("!")) return false;
    String[] flds;
    int pt = body.indexOf('\n');
    if (pt >= 0) {
      List<String> fldLst = new ArrayList<>(Arrays.asList(DELIM.split(body.substring(0,pt)+' ', -1)));
      fldLst.addAll(Arrays.asList(body.substring(pt+1).split("\n")));
      flds = fldLst.toArray(new String[0]);
    } else {
      flds = DELIM.split(body);
    }
    return parseFields(flds, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("CODE_CALL")) return new MyCodeCallField();
    return super.getField(name);
  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("(\\d) -(.*)");
  private class MyCodeCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_CALL_PTN.matcher(field);
      if (match.matches()) {
        data.strCode = match.group(1);
        data.strCall = match.group(2).trim();
      } else {
        data.strCall = field;
      }
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  @Override
  protected String adjustGpsLookupAddress(String addr) {
    return ORKlamathCountyParser.doAdjustGpsLookupAddress(addr);
  }
}
