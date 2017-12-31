package net.anei.cadpage.parsers.VA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class VADanvilleParser extends DispatchOSSIParser {
  
  public VADanvilleParser() {
    super("DANVILLE", "VA",
        // A complicated way to do CALL PLACE? ADDR X X
          "CALL ( PLACE ADDR/Z X/Z X/Z ENDMARK " +
               "| ADDR! X X END " +
               "| ADDR/Z X! X END " +
               "| ADDR/Z! END " +
               "| PLACE ADDR! X X END )");
  }
  
  @Override
  public String getFilter() {
    return "CAD@danvilleva.gov";
  }
  
  private static final Pattern BAD_CALL_PTN = Pattern.compile("FYI:|Update:|CANCEL");
  private static final Pattern BAD_FIELD_PTN = Pattern.compile("[A-Z]{3}|\\d{10}");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    body = stripFieldStart(body, "Text Message / ");
    if (!super.parseMsg(body, data)) return false;
    
    // We want to throw in something here that rejects VAPittsylvaniaCounty alerts that
    // have a very similar format
    if (data.strCall.equals("CANCEL")) return false;
    if (BAD_CALL_PTN.matcher(data.strCall).matches()) return false;
    if (BAD_FIELD_PTN.matcher(data.strCross).matches()) return false;
    if (BAD_FIELD_PTN.matcher(data.strAddress).matches()) return false;
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public boolean checkParse(String field, Data data) {
      Result res = parseAddress(StartType.START_ADDR, FLAG_CHECK_STATUS | FLAG_ANCHOR_END, field);
      if (res.getStatus() != STATUS_STREET_NAME) return false;
      res.getData(data);
      return true;
    }
  }
}
