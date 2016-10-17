package net.anei.cadpage.parsers.NJ;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Sussex County, NJ
 */
public class NJSussexCountyCParser extends FieldProgramParser {
  
  public NJSussexCountyCParser() {
    super("SUSSEX COUNTY", "NJ",
           "ID DATETIME ADDR Int/Cross:X? CALL! Notes:INFO? INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "donotreply@vernonpolice.com";
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("[A-Z]{3,4}");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (SUBJECT_PTN.matcher(subject).matches()) data.strSource = subject;
    
    int pt = body.indexOf("\n\n-- \n");
    if (pt >= 0) body = body.substring(0,pt);
    return parseFields(body.split("\n"), 4, data);
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{2}-\\d{5}", true);
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }
  

  private static final Pattern DATE_TIME_PTN = Pattern.compile("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d(:\\d\\d)? [AP]M");
  private static final DateFormat DATE_TIME_FMT = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      if (match.group(1) == null) {
        int pt = field.length()-3;
        field = field.substring(0,pt) + ":00" + field.substring(pt);
      }
      setDateTime(DATE_TIME_FMT, field, data);
    }
  }
}
