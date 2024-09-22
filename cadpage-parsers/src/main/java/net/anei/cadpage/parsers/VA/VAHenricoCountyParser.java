package net.anei.cadpage.parsers.VA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.StandardCodeTable;

public class VAHenricoCountyParser extends FieldProgramParser {

  public VAHenricoCountyParser() {
    super("HENRICO COUNTY", "VA",
          "ID_TIME ADDR! re:CALL! INFO/N+? MARK Info:URL");

    setFieldList("MAP TIME ADDR APT PLACE UNIT CALL INFO");
  }

  @Override
  public String getFilter() {
    return "cad_service@henrico.us,cad_service@henrico.gov";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD Notification")) return false;
    body = body.replace("//Details:", "//\nInfo:");
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID_TIME")) return new MyIdTimeField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("MARK")) return new SkipField("//", true);
    return super.getField(name);
  }

  private static final Pattern ID_TIME_PTN = Pattern.compile("(C\\d+) at (\\d\\d)(\\d\\d) hrs");
  private class MyIdTimeField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = ID_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCallId = match.group(1);
      data.strTime = match.group(2)+':'+match.group(3);
    }

    @Override
    public String getFieldNames() {
      return "ID TIME";
    }
  }

  private static final Pattern ADDR_MAP_PTN = Pattern.compile("(.*?) (?:\\((.*)\\) )?fd:(\\d\\d)");

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_MAP_PTN.matcher(field);
      if (!match.matches()) abort();
      parseAddress(match.group(1).trim(), data);
      data.strPlace = getOptGroup(match.group(2));
      data.strMap = match.group(3);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE MAP";
    }
  }

  private static final Pattern CALL_CODE_PTN = Pattern.compile("ProQA State: [A-Z ]+ \\| (\\S+)");
  private static final CodeTable CALL_CODES = new StandardCodeTable();

  private class MyCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      for (String part : field.split("//")) {
        part = part.trim();
        if (data.strCall.isEmpty()) {
          data.strCall = part;
        } else {
          Matcher match = CALL_CODE_PTN.matcher(part);
          if (match.matches()) {
            data.strCode = match.group(1);
            String call = CALL_CODES.getCodeDescription(data.strCode);
            if (call != null) data.strCall = call;
          } else {
            data.strSupp = append(data.strSupp, "\n", part);
          }
        }
      }
    }

    @Override
    public String getFieldNames() {
      return "CALL CODE INFO?";
    }
  }

  @Override
  public String adjustMapAddress(String addr) {
    Matcher match = INN_INTERSECTION_PTN.matcher(addr);
    if (match.matches()) addr = match.replaceAll("$1 $2");
    return addr;
  }
  private static final Pattern INN_INTERSECTION_PTN =
      Pattern.compile("(I[- ]*\\d+).*( & .*)", Pattern.CASE_INSENSITIVE);
}