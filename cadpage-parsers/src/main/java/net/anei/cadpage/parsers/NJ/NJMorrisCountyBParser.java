package net.anei.cadpage.parsers.NJ;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NJMorrisCountyBParser extends FieldProgramParser {
  
  public NJMorrisCountyBParser() {
    super("MORRIS COUNTY", "NJ", 
          "Case#:ID! CFS_Description:CALL! Location:ADDR! Common_Place:PLACE! Map_ID:MAP! Incident_Recv_DateTime:DATETIME! Contain_DateTime:SKIP! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "DataExport@jeffersonpolice.com,donotreply@lawsoftweb.onmicrosoft.com";
  }
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("Incident# : *\\S+ Resource# : *(\\S+) For Activity Dispatch Resource");
  private static final Pattern DELIM = Pattern.compile("\\n+|\\|");
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strUnit = match.group(1);
    body = body.replace(" :- ", ":");
    return parseFields(DELIM.split(body), data);
  }
  
  @Override
  public String getProgram() {
    return "UNIT " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (!field.startsWith("Comment:")) return;
      field = field.substring(8).trim();
      super.parse(field, data);
    }
  }

}
