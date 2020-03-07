package net.anei.cadpage.parsers.OR;

import net.anei.cadpage.parsers.HtmlProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ORDeschutesCountyBParser extends HtmlProgramParser {
  
  public ORDeschutesCountyBParser() {
    super("DESCHUTES COUNTY", "OR", 
          "CALL:CALL! PLACE:PLACE! ADDR:ADDRCITY! CROSS_ST:X! ID:ID! DATE:DATETIME! UNIT:UNIT! INFO:INFO! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "cad@dc911sd.org,70177";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    
    if (!subject.equals("!") && !subject.startsWith("Automatic R&R Notification:")) return false;
    
    if (body.startsWith("<")) {
      return super.parseHtmlMsg(subject, body, data);
    } else {
      return parseFields(body.split("\n"), data);
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("Radio Channel -")) {
        data.strChannel = append(data.strChannel, "/", field.substring(15).trim());
      } else {
        data.strSupp = append(data.strSupp, "\n", field);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "CH INFO";
    }
  }

}
