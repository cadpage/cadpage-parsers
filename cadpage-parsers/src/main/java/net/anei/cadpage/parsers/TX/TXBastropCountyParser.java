package net.anei.cadpage.parsers.TX;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXBastropCountyParser extends FieldProgramParser {

  public TXBastropCountyParser() {
    super("BASTROP COUNTY", "TX",
          "( Addr:ADDR! LOC:PLACE City:CITY! Problem:CALL! UNITS:UNIT! Inc_Type:PRI! Station:SRC! NOTES:INFO! Response_Information:EMPTY! CN:ID! CASE_NUMBER:ID_INFO " +
          "| CAD_Paging_MIN:ID! ( DATE:DATETIME! ( JUR:SRC! RA:SKIP! PROB:CALL! PRI:PRI2! ADDR:ADDR! " +
                                                "| UNITS:UNIT! PROB:CALL! ADDR:ADDR! JUR:SRC! PRI:PRI2! " +
                                                ") STREET:SKIP! APT:APT! BLDG:PLACE! XSTREET:X! UNITS:UNIT? CALL_BACK:PHONE! CALLER_NAME:NAME! " +
                               "| ) NOTES:INFO! " +
          "| MIN:ID! PROB:CALL? ADDR:ADDR! CITY:SKIP! COMMENTS:INFO CALL_BACK:PHONE! CALLER_NAME:NAME! " +
          "| Response_Information:EMPTY! Addr:ADDR! Loc:PLACE! City:CITY! Problem:CALL! Caller_Name:NAME! CB_Number:PHONE! UNITS:UNIT! Inc_Type:PRI! Station:SRC! NOTES:INFO! " +
          ") END");
  }

  @Override
  public String getFilter() {
    return "bastropsocommunications@co.bastrop.tx.u";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD Paging")) return false;
    if (body.startsWith("Addr:")) {
      return super.parseMsg(body.replace(" NOTES", " NOTES:"), data);
    }

    if (body.startsWith("CAD Paging")) {
      return super.parseMsg(body.replace("JUR:", " JUR:").replace("UNITS:", " UNITS:"), data);
    }

    if (body.startsWith("MIN ")) {
      body = "MIN: " + body.substring(4).replace(" CITY", " CITY:").replace(" COMMENTS", " COMMENTS:");
      return super.parseMsg(body, data);
    }

    if (body.startsWith("Response Information:")) {
      body = body.replace(" Inc Type:", "\nInc Type:");
      return super.parseMsg(body, data);
    }

    return false;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("SRC")) return new MySourceField();
    if (name.equals("ID_INFO")) return new MyIdInfoField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("PRI2")) return new MyPriority2Field();
    return super.getField(name);
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      super.parse(field.replace(' ', '_'), data);
    }
  }

  private class MySourceField extends SourceField {
    @Override
    public void parse(String field, Data data) {
      super.parse(field.replace(' ', '_'), data);
    }
  }

  private static final Pattern ID_INFO_PTN = Pattern.compile("(.*?) *\\b(\\d+\\).*)");
  private class MyIdInfoField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ID_INFO_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        parseInfoField(match.group(2), data);
      }
      data.strCallId = append(data.strCallId, "/", field);
    }

    @Override
    public String getFieldNames() {
      return "ID INFO";
    }
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      parseInfoField(field, data);
    }
  }

  private static final Pattern INFO_HEAD_PTN = Pattern.compile("\\d+\\) \\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d-");
  private static final Pattern INFO_BRK_PTN = Pattern.compile(" *, *(?=\\[\\d{1,2}\\]|Jurisdiction:)");

  private void parseInfoField(String field, Data data) {
    Matcher match = INFO_HEAD_PTN.matcher(field);
    if (match.lookingAt()) field = field.substring(match.end()).trim();
    field = stripFieldEnd(field, ",");
    for (String line : INFO_BRK_PTN.split(field)) {
      line = stripFieldEnd(line, "[Shared]");
      data.strSupp = append(data.strSupp, "\n", line);
    }
  }

  private class MyPriority2Field extends PriorityField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf('-');
      if (pt >= 0) field = field.substring(0,pt).trim();
      super.parse(field, data);
    }
  }
}
