package net.anei.cadpage.parsers.NC;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.StandardCodeTable;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;



public class NCWakeCountyBParser extends DispatchOSSIParser {
  
  public NCWakeCountyBParser() {
    super(CITY_CODES, "CARY", "NC",
          "( CANCEL ADDR CITY PLACE " +
          "| FYI? CH? SRC1 SRC2/C? ( CODE CALL? ADDR | CALL ADDR ) ( UNIT | X/Z UNIT | X/Z X/Z UNIT | X+? ) ) INFO/N+");
  }

  @Override
  public String getFilter() {
    return "CAD@townofcary.org";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!body.startsWith("CAD:")) body = "CAD:" + body;
    if (!super.parseMsg(body, data)) return false;
    if (data.strCode.length() > 0) {
      String call = CALL_CODES.getCodeDescription(data.strCode);
      if (call != null) data.strCall = call;
    }
    return true;
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("CH")) return new ChannelField("OPS_\\d+", true);
    if (name.equals("SRC1")) return new SourceField("[A-Z]{1,4}");
    if (name.equals("SRC2")) return new SourceField("S\\d{2}|[A-Z]{4}");
    if (name.equals("CODE")) return new CodeField("\\d{1,2}[A-Z]\\d{1,2}[A-Z]?", true);
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("UNIT")) return new UnitField("[A-Z0-9]+,[A-Z0-9,]+|[A-Z]+\\d+|[A-Z]+FD|MUT[A-Z0-9]+", true);
    return super.getField(name);
  }
  
  private static final Pattern CALL_CODE_PTN = Pattern.compile("(.*) (\\d{1,2}[A-Z]\\d{1,2})");
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_CODE_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strCode = match.group(2);
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "CALL CODE";
    }
  }
  
  private static final CodeTable CALL_CODES = new StandardCodeTable();
  
  // City codes are only used for CANCEL messages :(
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "APEX", "APEX",
      "CARY", "CARY",
      "MIDD", "MIDDLE CREEK",
      "MORR", "MORRISVILLE",
      "WEND", "WENDELL",
      "ZEBU", "ZEBULON"
  });
}