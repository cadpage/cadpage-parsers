package net.anei.cadpage.parsers.IN;


import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

/*
 * Vanderburgh County, IN
 */
public class INVanderburghCountyParser extends DispatchOSSIParser {
 
  public INVanderburghCountyParser() {
    super("VANDERBURGH COUNTY", "IN",
           "BOX UNIT CALL PLACE? ADDR! CH? INFO+");
  }
  
  @Override
  public String getFilter() {
    return "CAD@vanderburghsheriff.com";
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("Radio Channel:")) {
        field = field.substring(14).trim();
        if (data.strChannel.length() == 0) data.strChannel = field;
        return;
      }
      super.parse(field, data);
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CH")) return new ChannelField("TAC ?\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
}
