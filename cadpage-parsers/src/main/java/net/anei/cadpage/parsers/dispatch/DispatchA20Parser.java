package net.anei.cadpage.parsers.dispatch;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class DispatchA20Parser extends FieldProgramParser {

  public static final int A20_UNIT_LABEL_REQ = 1;

  private static final Pattern SUBJECT_PTN = Pattern.compile("(Dispatched Call|Closing Info)(?:, Unit: ([- A-Z0-9]+))? \\(([-A-Z0-9 ]*)\\)(?:\\|.*)?");

  private Properties codeLookupTable;

  private boolean unitLabelReq;

  public DispatchA20Parser(String defCity, String defState) {
    this(null, defCity, defState, 0);
  }

  public DispatchA20Parser(String defCity, String defState, int flags) {
    this(null, defCity, defState, flags);
  }

  public DispatchA20Parser(Properties codeLookupTable, String defCity, String defState) {
    this(codeLookupTable, defCity, defState, 0);
  }

  public DispatchA20Parser(Properties codeLookupTable, String defCity, String defState, int flags) {
    super(defCity, defState,
           "ADDRCITYST PLACE X APT CALL! MAP ID? INFO/N+");
    this.codeLookupTable = codeLookupTable;
    this.unitLabelReq = (flags & A20_UNIT_LABEL_REQ) != 0;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    // Replace paren terms that might have gotten stripped off the beginning
    // of the message
    String[] subParts = subject.split("\\|");
    subject = subParts[0];
    for (int jj = subParts.length-1; jj>0; jj--) {
      body = '(' + subParts[jj] + ") " + body;
    }
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    if (match.group(1).startsWith("C")) data.msgType = MsgType.RUN_REPORT;
    String unit1 = match.group(2);
    String unit2 = match.group(3);
    if (unit1 != null) data.strUnit = unit1;
    else if (!unitLabelReq) data.strUnit = unit2;
    if (body.endsWith("*")) body = body + " ";
    if (!parseFields(body.split(" \\* ", -1), 5, data)) return false;
    return true;
  }

  @Override
  public String getProgram() {
    return "UNIT " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITYST")) return new MyAddressCityStField();
    if (name.equals("ID")) return new IdField("#(\\d+)|([0-9a-f]{24})", true);
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern ADDR_ZIP_PTN = Pattern.compile("(.*) (\\d{5})");
  private class MyAddressCityStField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      String zip = "";
      Matcher match = ADDR_ZIP_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        zip = match.group(2);
      }
      Parser p = new Parser(field);
      String city = p.getLastOptional(',');
      if (city.length() == 2) {
        if (!city.equals(data.defState)) data.strState = city;
        city = p.getLastOptional(',');
      }
      if (city.length() == 0) city = zip;
      String addr = p.get();
      if (addr.length() == 0) abort();
      super.parse(addr, data);
      data.strCity = city;
    }

    @Override
    public String getFieldNames() {
      return "ADDR CITY ST";
    }
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (codeLookupTable != null) {
        data.strCode = field;
        data.strCall = convertCodes(field, codeLookupTable);
      } else {
        data.strCall = field;
      }
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("\n| {3,}");
  private static final Pattern UNIT_PTN = Pattern.compile("ENG .*", Pattern.CASE_INSENSITIVE);
  private static final Pattern GPS_PTN = Pattern.compile("(?:LAT|LON):(.*)", Pattern.CASE_INSENSITIVE);
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      String gpsLoc = "";
      for (String line : INFO_BRK_PTN.split(field)) {
        line = line.trim();
        if (line.startsWith("Service Class:")) continue;
        if ("Service Class:".startsWith(line)) continue;
        int pt = line.indexOf("Cellular E911 Call:");
        if (pt == 0) continue;
        if (pt > 0) line = line.substring(0,pt).trim();
        if ("Cellular E911 Call:".startsWith(line)) continue;
        Matcher match;
        if (UNIT_PTN.matcher(line).matches()) {
          data.strUnit = line;
        } else if ((match = GPS_PTN.matcher(line)).matches()) {
          gpsLoc = append(gpsLoc, ",", match.group(1));
        } else if (data.strCall.length() == 0) {
          data.strCall = line;
        } else {
          super.parse(line, data);
        }
      }
      setGPSLoc(gpsLoc, data);
    }

    @Override
    public String getFieldNames() {
      return "UNIT APT CALL GPS INFO";
    }
  }
}
