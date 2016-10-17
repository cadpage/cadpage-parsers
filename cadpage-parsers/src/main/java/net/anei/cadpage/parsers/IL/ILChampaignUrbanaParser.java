package net.anei.cadpage.parsers.IL;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class ILChampaignUrbanaParser extends FieldProgramParser {
  
  public ILChampaignUrbanaParser() {
    super("", "IL",
           "ID NAME NAME? TIME ADDR APT? CITY INFO+");
  }
  
  @Override
  public String getLocName() {
    return "Champaign-Urbana, IL";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!body.startsWith("Run# ")) return false;
    body = body.substring(5).trim();
    return parseFields(body.split(", *"), data);
  }
  
  private class MyNameField extends NameField {
    @Override
    public void parse(String field, Data data) {
      if (field.equalsIgnoreCase("<Unknown>")) return;
      data.strName = append(data.strName, ", ", field);
    }
  }
  
  private class MyAptField extends AptField {
    public MyAptField() {
      setPattern(Pattern.compile(".{1,3}"));
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equalsIgnoreCase("<None>")) return;
      super.parse(field, data);
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("NAME")) return new MyNameField();
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
    
  }
}
