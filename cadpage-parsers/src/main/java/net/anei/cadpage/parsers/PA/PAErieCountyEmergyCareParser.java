package net.anei.cadpage.parsers.PA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchProQAParser;



public class PAErieCountyEmergyCareParser extends DispatchProQAParser {
  
  public PAErieCountyEmergyCareParser() {
    super("ERIE COUNTY", "PA",
           "SKIP TIME CALL CALL? PRI ADDR PLACE? ZIP! INFO+");
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    if (data.strAddress.equals("Local") && data.strPlace.length() > 0) {
      data.strAddress = data.strPlace;
      data.strPlace = "";
    }
    return true;
  }

  private class MyPriorityField extends PriorityField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Parser p = new Parser(field);
      if (!p.get(' ').equals("Priority")) return false;
      super.parse(p.get(), data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  
  private class MyCityField extends CityField {
    public MyCityField() {
      setPattern(Pattern.compile("\\d{5}"), true);
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    if (name.equals("PRI")) return new MyPriorityField();
    if (name.equals("CITY")) return new MyCityField();
    return super.getField(name);
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
}
