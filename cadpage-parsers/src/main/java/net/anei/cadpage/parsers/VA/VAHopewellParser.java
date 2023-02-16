package net.anei.cadpage.parsers.VA;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;


public class VAHopewellParser extends DispatchOSSIParser {
  
  public VAHopewellParser() {
    super(CITY_CODES, "HOPEWELL", "VA",
          "( CANCEL ADDR CITY | FYI CALL CALL2+? ADDR! ( X X? | PLACE X X? | ) ) INFO+");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace('-', ';');
    body = body.replace(";BLK ", "-BLK ");
    if (!body.startsWith("CAD:")) body = "CAD:" + body;
    return super.parseMsg(body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL2")) return new MyCallField();
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }
  
  private static final Pattern CALL2_PTN = Pattern.compile(".*\\([AB]LS\\)");
  private class MyCallField extends CallField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (!CALL2_PTN.matcher(field).matches()) return false;
      data.strCall = append(data.strCall, " - ", field);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  
  private static final Pattern SPEC_X_PTN = Pattern.compile("[NSEW] [A-Z ]+|[A-Z ]+ PLC");
  private class MyCrossField extends CrossField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (SPEC_X_PTN.matcher(field).matches()) {
        super.parse(field, data);
        return true;
      }
      return super.checkParse(field, data);
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "HPW", "HOPEWELL",
  });
}
