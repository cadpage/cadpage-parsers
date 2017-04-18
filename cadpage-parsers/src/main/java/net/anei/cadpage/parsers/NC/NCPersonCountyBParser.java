package net.anei.cadpage.parsers.NC;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.StandardCodeTable;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class NCPersonCountyBParser extends DispatchOSSIParser {
  
  public NCPersonCountyBParser() {
    super("PERSON COUNTY", "NC",
          "( CANCEL ADDR! " +
          "| FYI? PLACE? ADDR X/Z+? ( CODE CALL DATETIME! | CALL DATETIME! ) UNIT/C+? ) INFO/CS+");
  }
  
  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }
  
  private static final Pattern DELIM = Pattern.compile("[,;]");

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(DELIM.split(body), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("CODE")) return new MyCodeField();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("UNIT")) return new UnitField("[A-Z]+\\d+|[A-Z]{2,4}FD|\\d{4}|HP", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyPlaceField extends PlaceField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (checkAddress(getRelativeField(+1)) < STATUS_INTERSECTION) return false;
      if (isValidAddress(field)) return false;
      super.parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  
  private class MyCodeField extends CodeField {
    public MyCodeField() {
      super("\\d\\d[A-Z]\\d\\d[A-Z]?", true);
    }
    
    @Override
    public void parse(String field, Data data) {
      super.parse(field, data);
      String call = CALL_CODES.getCodeDescription(data.strCode, true);
      if (call != null) data.strCall = call;
    }
  }
  
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (data.strCall.length() > 0) return;
      super.parse(field, data);
    }
  }
  
  private static final Pattern INFO_JUNK_PTN = Pattern.compile(" *\\[\\d\\d/[^\\]]*(?:\\] *|$)");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_JUNK_PTN.matcher(field).replaceAll("\n").trim();
      super.parse(field, data);
    }
  }
  
  private static final CodeTable CALL_CODES = new StandardCodeTable();

}
