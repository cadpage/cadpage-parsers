package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;

public class COJeffersonCountyCParser extends FieldProgramParser {
  
  public COJeffersonCountyCParser() {
    super("JEFFERSON COUNTY", "CO", 
          "ID CALL ADDR APT X ( MAP GPS1! GPS2 | ) CITY! UNIT EMPTY TIME% END");
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
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!parseFields(body.split(" ,"), data)) return false;
    if (data.strCity.equals("UNINC JEFFERSON")) data.strCity = "";
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{4}[A-Z]{2,3}-\\d{7}|[A-Z]{2,3}\\d{6}-\\d{7}", true);
    if (name.equals("X")) return new MyCrossField(); 
    if (name.equals("MAP")) return new MapField("[A-Z]-\\d{1,2}-[A-Z]", true);
    if (name.equals("GPS1")) return new MyGPS1Field();
    if (name.equals("GPS2")) return new MyGPS2Field();
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
  
  private class MyGPS1Field extends MyGPSField {
    public MyGPS1Field() {
      super(1);
    }
  }
  
  private class MyGPS2Field extends MyGPSField {
    public MyGPS2Field() {
      super(2);
    }
  }
  
  private class MyGPSField extends GPSField {
    public MyGPSField(int type) {
      super(type);
    }
    
    @Override
    public void parse(String field, Data data) {
      int pt = field.length()-6;
      if (pt < 1) return;
      field = field.substring(0,pt)+'.'+field.substring(pt);
      super.parse(field, data);
    }
  }
  

}
