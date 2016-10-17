package net.anei.cadpage.parsers.IA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class IAPottawattamieCountyParser extends FieldProgramParser {
  
  private static final Pattern SOURCE_PTN = Pattern.compile("\\d{4}"); 
  
  public IAPottawattamieCountyParser() {
    super("POTTAWATTAMIE COUNTY", "IA",
           "IDDATETIME ADDRCITY CALL! XST:X");
  }
  
  @Override
  public String getFilter() {
    return "messaging@iamresponding.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (!SOURCE_PTN.matcher(subject).matches()) return false;
    data.strSource = subject;
    if (!parseFields(body.split("\n"), data)) return false;
    if (data.strCity.equals("POTT COUNTY")) data.strCity = "";
    return true;
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
  
  private static final Pattern ID_DATE_TIME_PTN = 
    Pattern.compile("(?:(\\d\\d-\\d\\d-\\d{3}(?: /\\d\\d-\\d\\d-\\d{3})*) )?(\\d\\d/\\d\\d) (\\d\\d:\\d\\d)");
  private class MyIdDateTimeField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ID_DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCallId = getOptGroup(match.group(1));
      data.strDate = match.group(2);
      data.strTime = match.group(3);
    }
    
    @Override
    public String getFieldNames() {
      return "ID DATE TIME";
    }
  }
  
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("//", "&");
      super.parse(field, data);
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("IDDATETIME")) return new MyIdDateTimeField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    return super.getField(name);
  }
}
