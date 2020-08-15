package net.anei.cadpage.parsers.CO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class CODenverAParser extends FieldProgramParser {
  
  public CODenverAParser() {
    super("DENVER", "CO", 
          "AMB:UNIT! PRI:PRI! ADD:ADDR! APT:APT! NATURE:CODE_CALL! TRIP:ID! TIME:TIME! END");
  }
  
  @Override
  public String getFilter() {
    return "ComCntrTechSupport@ci.denver.co.us";
  }
  
  private static final Pattern DELIM = Pattern.compile(" *(?=PRI:|ADD:|APT:|NATURE:|TRIP:|TIME)");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD Message")) return false;
    return parseFields(DELIM.split(body), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CODE_CALL")) return new MyCodeCallField();
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("(\\d{2}[A-Z]) +(.*)");
  private class MyCodeCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_CALL_PTN.matcher(field);
      if (match.matches()) {
        data.strCode = match.group(1);
        field = match.group(2);
      }
      data.strCall = field;
      
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
}
