package net.anei.cadpage.parsers.TX;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXBastropCountyParser extends FieldProgramParser {

  public TXBastropCountyParser() {
    super("BASTROP COUNTY", "TX",
          "( Addr:ADDR! LOC:PLACE City:CITY! Problem:CALL! UNITS:UNIT! Inc_Type:PRI! Station:SRC! NOTES:INFO! Response_Information:EMPTY! CN:ID! CASE_NUMBER:ID/L " +
          "| CAD_Paging_MIN:ID! DATE:DATETIME! JUR:SRC! RA:SKIP! PROB:CALL! PRI:PRI2! ADDR:ADDR! STREET:SKIP! APT:APT! BLDG:PLACE! XSTREET:X! UNITS:UNIT! CALL_BACK:PHONE! CALLER_NAME:NAME! NOTES:INFO! " +
          "| MIN:ID! ADDR:ADDR! CITY:SKIP! COMMENTS:INFO CALL_BACK:PHONE! CALLER_NAME:NAME! " +
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
      return super.parseMsg(body.replace("JUR:", " JUR:"), data);
    }

    if (body.startsWith("MIN ")) {
      body = "MIN: " + body.substring(4).replace(" CITY", " CITY:").replace(" COMMENTS", " COMMENTS:");
      return super.parseMsg(body, data);
    }

    return false;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("SRC")) return new MySourceField();
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

  private static final Pattern INFO_BRK_PTN = Pattern.compile(" *, *(?=\\[\\d{1,2}\\]|Jurisdiction:)");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ",");
      for (String line : INFO_BRK_PTN.split(field)) {
        line = stripFieldEnd(line, "[Shared]");
        data.strSupp = append(data.strSupp, "\n", line);
      }
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
