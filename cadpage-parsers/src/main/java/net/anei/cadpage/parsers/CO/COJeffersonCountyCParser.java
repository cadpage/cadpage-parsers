package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;

public class COJeffersonCountyCParser extends FieldProgramParser {
  
  public COJeffersonCountyCParser() {
    super("JEFFERSON COUNTY", "CO", 
          "ID CALL ADDR APT X CITY! UNIT EMPTY TIME% END");
  }
  
  @Override
  public String getFilter() {
    return "CADPage@jeffcom911.org";
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
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD Message")) return false;
    if (!parseFields(body.split(" ,"), data)) return false;
    if (data.strCity.equals("UNINC JEFFERSON")) data.strCity = "";
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{4}[A-Z]{2,3}-\\d{7}", true);
    if (name.equals("X")) return new MyCrossField(); 
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("Unk Cross Street", "");
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }
  

}
