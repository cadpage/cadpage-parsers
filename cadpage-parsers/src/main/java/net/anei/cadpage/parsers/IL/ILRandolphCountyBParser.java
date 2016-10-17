package net.anei.cadpage.parsers.IL;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;

public class ILRandolphCountyBParser extends FieldProgramParser {
  
  private static final Pattern DELIM = Pattern.compile("\\*{3}");

  public ILRandolphCountyBParser() {
    super("RANDOLPH COUNTY", "IL",
          "ADDRCITY CALL CITY X INFO!");
  } 
  
  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom(){
      @Override public boolean splitBlankIns() { return false; }
      @Override public int splitBreakLength() { return 130; }
      @Override public int splitBreakPad() { return 1; }
    };
  }

  @Override
  public String getFilter() {
    return "911mcsd@gmail.com";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(DELIM.split(body,-1), 5, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("//", "/");
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }
}
