package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;

public class OHFultonCountyParser extends FieldProgramParser {

  public OHFultonCountyParser() {
    super("FULTON COUNTY", "OH",
          "Type:CALL! Details:CALL/SDS! Address:ADDRCITYST! PLACE! Cross:X! PHONE!");
  }

  @Override
  public String getFilter() {
    return "noreply-centralsquare@fultoncountyoh.com";
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Fulton County Alert")) return false;
    return parseFields(body.split("; "), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("PHONE")) return new PhoneField("From Number / *(.*)", true);
    return super.getField(name);
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }
}
