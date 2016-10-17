package net.anei.cadpage.parsers.MO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MOJacksonCountyBParser extends FieldProgramParser {

  public MOJacksonCountyBParser() {
    super("JACKSON COUNTY", "MO", 
          "ADDR! CALLCODE! CH! CASE! ID! Assigned:UNIT! UNIT/C+");
  }
  
  @Override
  public String getFilter() {
    return "totalaccess@mobilfonetotalaccess.com";
  }
  
  private static Pattern BODY_DATETIME = Pattern.compile("(.*?)(\\d{2}/\\d{2}/\\d{2})? (\\d{2}:\\d{2}:\\d{2})"); //no space between BODY and DATE
  public boolean parseMsg(String subject, String body, Data data) {
    //check subject
    if (!subject.equals("New Message")) return false;
    
    //parse trailing DATE? TIME
    Matcher mat = BODY_DATETIME.matcher(body);
    if (mat.matches()) {
      body = mat.group(1);
      data.strDate = getOptGroup(mat.group(2));
      data.strTime = mat.group(3);
    }
    
    //check for run report format
    if (body.startsWith("RFPD")) return data.parseRunReport(this, body);
    
    //try parsing normally
    if (parseFields(body.split(" *, *"), data)) return true;
    
    //if that fails, check if this is an unlabeled run report
    if (body.contains("First Arrive")) return data.parseRunReport(this, body);
    
    //if we made it here, fail
    return false;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram() + " DATE TIME";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALLCODE")) return new MyCallCodeField();
    if (name.equals("CH")) return new ChannelField("[A-Z]+-[A-Z0-9]+", true); //only two examples, possibly numeric chars before the "-"?
    if (name.equals("CASE")) return new SkipField("Case#(?:[A-Z]+-)?\\d+", true);
    if (name.equals("ID")) return new IdField("Response/PCR #(\\d+)", true);
    return super.getField(name);
  }
  
  //CALL( / CODE)?
  private static Pattern CALL_CODE = Pattern.compile("(.*?)(?:(?:-| / )(.*))?");
  public class MyCallCodeField extends Field {
    @Override
    public void parse(String field, Data data) {
        Matcher mat = CALL_CODE.matcher(field);
        if (mat.matches()) {
          data.strCall = mat.group(1).trim();
          data.strCode = getOptGroup(mat.group(2));
        } else data.strCall = field;
    }
  
    @Override
    public String getFieldNames() {
      return "CALL CODE";
    } 
  }
}