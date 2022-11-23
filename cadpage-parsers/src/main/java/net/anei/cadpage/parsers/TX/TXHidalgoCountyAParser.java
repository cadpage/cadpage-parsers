package net.anei.cadpage.parsers.TX;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA37Parser;

public class TXHidalgoCountyAParser extends FieldProgramParser {

  public TXHidalgoCountyAParser() {
    super("HIDALGO COUNTY", "TX", 
          "Date_&_Time:DATETIME! Call_Type:CALL! Address:ADDRCITY! City:NAME_PHONE! Notes:INFO! CAD#:ID! END");
   }

  @Override
  public String getFilter() {
    return "pgalerts@pharr-tx.gov";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("!")) return false;
    return parseMsg(body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("NAME_PHONE")) return new MyNamePhoneField();
    return super.getField(name);
  }
  
  private static final Pattern NAME_PHONE_PTN = Pattern.compile("(.*?) +(\\d{10})");
  
  private class MyNamePhoneField extends Field {

    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, data.strCity);
      Matcher match = NAME_PHONE_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        data.strPhone = match.group(2);
      }
      data.strName = field;
    }

    @Override
    public String getFieldNames() {
      return "NAME PHONE";
    }
    
  }
}
