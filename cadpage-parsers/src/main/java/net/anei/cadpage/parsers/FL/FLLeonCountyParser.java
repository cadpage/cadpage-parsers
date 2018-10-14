package net.anei.cadpage.parsers.FL;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class FLLeonCountyParser extends FieldProgramParser {
  
  public FLLeonCountyParser() {
    super("LEON COUNTY", "FL", 
          "Location:ADDR! Inc_Type:CALL! Descr:CALL2! Cross_Strs:X! COMMENTS:INFO! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return ".CD@tlccda.org";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("Dispatch Notification for Incident ")) return false;
    data.strCallId = subject.substring(35).trim();
    return parseFields(body.split("\n+"), data);
  }
  
  @Override
  public String getProgram() {
    return "ID " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("CALL2")) return new MyCall2Field();
    return super.getField(name);
  }
  
  private static final Pattern MSPACE_PTN = Pattern.compile(" {2,}");
  private static final Pattern RESP_PTN = Pattern.compile("\\bRESP\\b");
  private static final Pattern ENVENONMATIONS_PTN = Pattern.compile("\\bENVENONMATIONS\\b");
  
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      super.parse(cleanCallField(field), data);
    }
    
    protected String cleanCallField(String field) {
      field = MSPACE_PTN.matcher(field).replaceAll(" ");
      field = RESP_PTN.matcher(field).replaceAll("RESPIRATORY");
      field = ENVENONMATIONS_PTN.matcher(field).replaceAll("ENVENOMATIONS");
      return field;
    }
  }
  
  private class MyCall2Field extends MyCallField {
    @Override
    public void parse(String field, Data data) {
      field = cleanCallField(field);
      if (data.strCall.startsWith(field)) return;
      if (field.startsWith(data.strCall)) {
        data.strCall = field;
      } else {
        data.strCall = append(data.strCall, " - ", field);
      }
    }
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = M_L_KING_PTN.matcher(addr).replaceAll("MARTIN LUTHER KING");
    return super.adjustMapAddress(addr);
  }
  
  private static final Pattern M_L_KING_PTN = Pattern.compile("\\bM L KING\\b");
}
